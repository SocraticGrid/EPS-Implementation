/* 
 * Copyright 2015 Cognitive Medical Systems, Inc (http://www.cognitivemedciine.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.socraticgrid.hl7.services.eps.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import kafka.consumer.ConsumerThreadId;
import kafka.utils.ZKStringSerializer;
import kafka.utils.ZkUtils;

import org.I0Itec.zkclient.ZkClient;
import org.I0Itec.zkclient.exception.ZkMarshallingError;
import org.I0Itec.zkclient.serialize.ZkSerializer;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.BrokerMonitoringIFace;
import org.socraticgrid.hl7.services.eps.internal.model.KafkaBrokerBean;
import org.socraticgrid.hl7.services.eps.model.BrokerStatistics;
import org.socraticgrid.hl7.services.eps.model.BrokerStatus;
import org.socraticgrid.hl7.services.eps.model.TopicStatistics;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Jerry Goodnough
 *
 */
public class BrokerMonitoringServiceImpl implements BrokerMonitoringIFace {

	private final Logger logger = LoggerFactory.getLogger(BrokerMonitoringServiceImpl.class);
	
	@Autowired 
	KafkaBrokerBean kafkaBrokerBean;
	
	/**
	 * 
	 */
	public BrokerMonitoringServiceImpl() { 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.socraticgrid.hl7.services.eps.interfaces.BrokerMonitoringIFace#
	 * getBrokerStatus()
	 */
	@Override
	public BrokerStatus getBrokerStatus() throws NotAuthorizedException,
			AuthenicationRequiredException, NoSuchTopicException,
			FeatureNotAvailableException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.socraticgrid.hl7.services.eps.interfaces.BrokerMonitoringIFace#
	 * getBrokerStatistics()
	 */
	@Override
	public BrokerStatistics getBrokerStatistics()
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, FeatureNotAvailableException {
		return getKafkaStatistics();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.socraticgrid.hl7.services.eps.interfaces.BrokerMonitoringIFace#
	 * getTopicStatistics(java.lang.String)
	 */
	@Override
	public TopicStatistics getTopicStatistics(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, FeatureNotAvailableException {
	    return	getKafkaTopicStatistics(topic);
	}

	public static void main(String[] args) throws Exception {
		//getKafkaTopicStatistics("test");
	 

	} 
	private BrokerStatistics getKafkaStatistics() {
		logger.debug("getKafkaStatistics | start");
		BrokerStatistics statistics = new BrokerStatistics();
		try{
			ZooKeeper zk = new ZooKeeper(kafkaBrokerBean.getZookeeperConnect(), 10000, null);
			List<String> topics = zk.getChildren(ZkUtils.BrokerTopicsPath(), false);
			statistics.setTotalTopics(topics.size());// total topics in a broker

			List<String> brokers = zk.getChildren(ZkUtils.BrokerIdsPath(), false);
			for (String broker : brokers) {
				statistics.setName(broker);
				String brokerInfo = new String(zk.getData(ZkUtils.BrokerIdsPath()
						+ "/" + broker, false, null)); 
				if (brokerInfo != null) {
					String[] brokerStatistics = brokerInfo.split(",");
					if (brokerStatistics != null && brokerStatistics.length > 1) {
						String strTime = brokerStatistics[1].split(":")[1]
								.replaceAll("\"", "");
						long time = Long.parseLong(strTime);
						statistics.setStarted(new Date(time));// broker starting time
					}
				}
			}
			statistics.setTotalMessages(getTotalMessages());
		}catch(Exception e){
			e.printStackTrace();
		}
		logger.debug("getKafkaStatistics | end");
		return statistics;
	}

	private long getTotalMessages() throws Exception { 
		logger.debug("getTotalMessages | start");
		long count = 0; 
		JMXServiceURL serviceUrl = new JMXServiceURL(kafkaBrokerBean.getJmxURL());
		JMXConnector jmxConnector = JMXConnectorFactory.connect(serviceUrl,
				null);
		try {
			MBeanServerConnection mbeanConn = jmxConnector
					.getMBeanServerConnection();
			ObjectName name = new ObjectName(
					"kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec"); 
			
			count = Long.parseLong(String.valueOf(mbeanConn.getAttribute(name,
					"Count")));

		} finally {
			jmxConnector.close();
		}
		logger.debug("Total message count in Broker:"+count);
		logger.debug("getTotalMessages | end"); 
		return count;
	}

	private TopicStatistics getKafkaTopicStatistics(String topic){
		logger.debug("getKafkaTopicStatistics | start");
		TopicStatistics topicStatistics = new TopicStatistics();		
		long activeMsgs = 0; 
		long totalMsgs=0;
		long deliveryFaults=0;
		JMXConnector jmxConnector=null;
		
		try {
			JMXServiceURL serviceUrl = new JMXServiceURL(kafkaBrokerBean.getJmxURL());
			jmxConnector = JMXConnectorFactory.connect(serviceUrl,null);
			MBeanServerConnection mbeanConn = jmxConnector.getMBeanServerConnection();
			
			//fetch active messages
			ObjectName name = new ObjectName(
					"kafka.server:type=BrokerTopicMetrics,name=MessagesInPerSec,topic="+topic); 
			
			activeMsgs = Long.parseLong(String.valueOf(mbeanConn.getAttribute(name,
					"Count")));
			
			topicStatistics.setActiveMessages(activeMsgs);
			
			//fetch delivery faults messages
			name = new ObjectName(
					"kafka.server:type=BrokerTopicMetrics,name=FailedProduceRequestsPerSec,topic="+topic); 
			deliveryFaults= Long.parseLong(String.valueOf(mbeanConn.getAttribute(name,
					"Count")));
			topicStatistics.setDeliveryFaults(deliveryFaults);
			
			//fetch total messages
			name = new ObjectName(
					"kafka.server:type=BrokerTopicMetrics,name=FailedFetchRequestsPerSec,topic="+topic); 
			totalMsgs= activeMsgs+deliveryFaults+Long.parseLong(String.valueOf(mbeanConn.getAttribute(name,
					"Count")));
			topicStatistics.setTotalMessages(totalMsgs);
			
			//get subscriber or consumers list
			 ZkClient zkClient = new ZkClient(kafkaBrokerBean.getZookeeperConnect());
			 zkClient.setZkSerializer(new ZkSerializer() {
					@Override
					public byte[] serialize(Object data) throws ZkMarshallingError {
						return ZKStringSerializer.serialize(data);
					}

					@Override
					public Object deserialize(byte[] bytes) throws ZkMarshallingError {
						return ZKStringSerializer.deserialize(bytes);
					}
				});
			 ZooKeeper zk = new ZooKeeper(kafkaBrokerBean.getZookeeperConnect(), 10000, null);
			 List<String> consumers = zk.getChildren("/consumers", false);
			 List<String> subscriberList = new ArrayList<String>(); 
			 for (String group : consumers) {
				 scala.collection.Map<String, scala.collection.immutable.List<ConsumerThreadId>> consumersPerTopicMap = ZkUtils.getConsumersPerTopic(zkClient, group,false);
				 if(consumersPerTopicMap.contains(topic)){
					 subscriberList.add(group);
				 }
			 
			 }
			 logger.debug("Active consumers list:"+subscriberList);
			 topicStatistics.setActiveSubscriptions(subscriberList.size());
			
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			try {
				jmxConnector.close();
			} catch (IOException e) { 
				e.printStackTrace();
			}
		}  
		logger.debug("Total messages in "+topic +":topic-> "+totalMsgs);
		logger.debug("Active messages in "+topic +":topic-> "+activeMsgs);
		logger.debug("getKafkaTopicStatistics | End"); 
		
		return topicStatistics;
	}

}
