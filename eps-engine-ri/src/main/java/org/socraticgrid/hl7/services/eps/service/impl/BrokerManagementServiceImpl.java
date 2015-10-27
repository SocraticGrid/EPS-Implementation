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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.BrokerManagementIFace;
import org.socraticgrid.hl7.services.eps.model.BrokerAccessRequest;
import org.socraticgrid.hl7.services.eps.model.CreationResult;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.PublicationContract;
import org.socraticgrid.hl7.services.eps.model.RemoveContentScope;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.Topic;
import org.socraticgrid.hl7.services.eps.model.TopicMetadata;
import org.socraticgrid.hl7.services.eps.model.User;
import org.socraticgrid.hl7.services.eps.model.Role; 
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicLocatorIFace;
import org.socraticgrid.hl7.services.eps.internal.model.KafkaBrokerBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
 

/**
 * @author Jerry Goodnough
 *
 */
public class BrokerManagementServiceImpl implements BrokerManagementIFace {
	public BrokerManagementServiceImpl()
	{
		
	}
	
	
	@Autowired
	@Qualifier("Admin")
	private User user;
	
	@Autowired
	TopicLocatorIFace topicLocator;
	
	@Autowired(required = false)
	KafkaBrokerBean kafkaBrokerBean;
	
	@Override
	public CreationResult createTopic(String parentTopic, String topicName,
			Topic topicOptions) throws NotAuthorizedException,
			AuthenicationRequiredException, ConflictException,
			NoSuchTopicException, MediaFormatNotExceptedException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, IncompleteDataException {
		
			CreationResult out;
			List<Role> userRole = user.getPrivileges();
	   
			boolean userAuth = false;
	  		 
			for (int i = 0; i < userRole.size();i++) 
			{
				if ( userRole.get(i) == Role.Admin || userRole.get(i) == Role.Normal ) 
				{
					userAuth = true;
				}
			}
			
			if (userAuth == false) 
			{
				throw new NotAuthorizedException();
			}
			else 
			{
				final Producer<String,String> producer;
				Properties producerProps = new Properties();
				producerProps.put("metadata.broker.list", kafkaBrokerBean.getMetadataBrokerList());
				producerProps.put("serializer.class", kafkaBrokerBean.getSerializerClass());
				//producerProps.put("partitioner.class", KafkaProperties.partitionerClass);
				//producerProps.put("request.required.acks", KafkaProperties.requestRequiredAcks);
				ProducerConfig config = new ProducerConfig(producerProps);
	  		  
				producer = new Producer<String,String>(config);
	  		  
				KeyedMessage<String, String> data = new KeyedMessage<String,String>(parentTopic, topicName); 
	 		  
				producer.send(data);
	  
				producer.close();
	  
				out = CreationResult.Success;
			}
	  
			return out;
		//throw new FeatureNotAvailableException();
	}

	@Override
	public boolean deleteTopic(String topic) throws NotAuthorizedException,
			AuthenicationRequiredException, ConflictException,
			NoSuchTopicException {
		throw new NoSuchTopicException();
	}

	@Override
	public boolean configureTopic(String topic, Topic topicOptions)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, NoSuchTopicException,
			MediaFormatNotExceptedException, ExpiredException,
			FeatureNotAvailableException, InvalidDataException,
			IncompleteDataException {
		throw new FeatureNotAvailableException();
	}

	@Override
	public TopicMetadata getTopicMetadata(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException {
		throw new NoSuchTopicException();
	}

	@Override
	public List<PublicationContract> getPublishersForTopic(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException {
		
			List<Role> userRole = user.getPrivileges();
		   
			boolean pubAuth = false;
		  		 
			for (int i = 0; i < userRole.size();i++) 
			{
				if ( userRole.get(i) == Role.Admin || userRole.get(i) == Role.Normal ) 
				{
					pubAuth = true;
				}
			}
	
			if (pubAuth == false) 
			{
				throw new NotAuthorizedException();
			}
			else 
			{
	  		
				List<PublicationContract> outContract = new ArrayList<PublicationContract>();
    
				PublicationContract pubContract = new PublicationContract();    
				TopicIFace topicPub = topicLocator.locateTopic(topic);
	  		
				if (topicPub == null) 
				{
					throw new NoSuchTopicException();
				}
				else 
				{
					pubContract.setPublisher(user);
					outContract.add(pubContract);
				}
    
				return outContract;
			}	
	}

	@Override
	public List<Subscription> getSubscriptionsForTopic(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException {
		
			List<Role> userRole = user.getPrivileges();
		   
			boolean subAuth = false;
  		  		 
			for (int i = 0; i < userRole.size();i++) 
			{
				if ( userRole.get(i) == Role.Admin || userRole.get(i) == Role.Normal ) 
				{
					subAuth = true;
				}
			}
  		  
			if (subAuth == false) 
			{
				throw new NotAuthorizedException();
			}
			else 
			{
				TopicIFace topicSub = topicLocator.locateTopic(topic);
				   
				if (topicSub.getSubscriptions().isEmpty()) 
				{
					throw new NoSuchTopicException();
				}
				else 
				return topicSub.getSubscriptions();
			}
	}

	@Override
	public boolean purgeAllTopicEvents(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException {
		throw new NoSuchTopicException();
	}

	@Override
	public boolean createUser(User userInfo) throws NotAuthorizedException,
			AuthenicationRequiredException, ConflictException,
			InvalidDataException, IncompleteDataException {
		return false;
	}

	@Override
	public boolean deleteUser(String userId, RemoveContentScope scope)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException {
		throw new NoSuchItemException();
	}

	@Override
	public User getUser(String userId) throws NotAuthorizedException,
			AuthenicationRequiredException, InvalidDataException,
			NoSuchItemException {
		throw new NoSuchItemException();
	}

	@Override
	public List<User> discoverUser(String query) throws NotAuthorizedException,
			AuthenicationRequiredException, InvalidDataException,
			NoSuchItemException {
		List<User> out = new LinkedList<User>();
		return out;
	}

	@Override
	public void moveTopic(String sourceTopic, String destinationPath)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, NoSuchTopicException {
		throw new NoSuchTopicException();
		
	}

	@Override
	public void updateUser(String userId, Options options, User userData)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException, MediaFormatNotExceptedException,
			InvalidDataException, IncompleteDataException {
		throw new NoSuchItemException();
		
	}

	@Override
	public List<BrokerAccessRequest> getBrokerAccessRequests()
			throws NotAuthorizedException, AuthenicationRequiredException,FeatureNotAvailableException {
		List<BrokerAccessRequest> out = new LinkedList<BrokerAccessRequest>();
		throw new FeatureNotAvailableException();
	}

	@Override
	public boolean grantBrokerAccessRequest(List<BrokerAccessRequest> request)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException, InvalidDataException, IncompleteDataException, FeatureNotAvailableException {
		throw new FeatureNotAvailableException();
	}

	@Override
	public boolean rejectBrokerAccessRequest(List<BrokerAccessRequest> requests)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException, InvalidDataException, IncompleteDataException,FeatureNotAvailableException {
	
		throw new FeatureNotAvailableException();
		
	}
	
}
