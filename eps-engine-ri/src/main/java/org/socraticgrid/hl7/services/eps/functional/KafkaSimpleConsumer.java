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
package org.socraticgrid.hl7.services.eps.functional;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kafka.api.FetchRequest;
import kafka.api.FetchRequestBuilder;
import kafka.api.PartitionOffsetRequestInfo;
import kafka.common.ErrorMapping;
import kafka.common.TopicAndPartition;
import kafka.javaapi.FetchResponse;
import kafka.javaapi.OffsetResponse;
import kafka.javaapi.PartitionMetadata;
import kafka.javaapi.TopicMetadata;
import kafka.javaapi.TopicMetadataRequest;
import kafka.javaapi.consumer.SimpleConsumer;
import kafka.message.MessageAndOffset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.internal.model.MessageMarshall;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.springframework.stereotype.Component;

@Component
public class KafkaSimpleConsumer {
	private final static Logger logger = LoggerFactory
			.getLogger(KafkaSimpleConsumer.class);

	private List<String> m_replicaBrokers = new ArrayList<String>();

	public KafkaSimpleConsumer() {
		m_replicaBrokers = new ArrayList<String>();
	}

	public List<Message> getPersistedEvents(String a_topic, int a_partition,
			String a_seedBroker, int a_port) throws Exception {
		List<Message> messages = new ArrayList<Message>();
		// find the meta data about the topic and partition we are interested in
		//
		List<String> a_seedBrokers = new ArrayList<String>();
		a_seedBrokers.add(a_seedBroker);
		PartitionMetadata metadata = findLeader(a_seedBrokers, a_port, a_topic,
				a_partition);
		if (metadata == null) {
			logger.debug("Can't find metadata for Topic and Partition. Exiting");
			return messages;
		}
		if (metadata.leader() == null) {
			logger.debug("Can't find Leader for Topic and Partition. Exiting");
			return messages;
		}
		String leadBroker = metadata.leader().host();
		String clientName = "Client_" + a_topic + "_" + a_partition;
		SimpleConsumer consumer = new SimpleConsumer(leadBroker, a_port,
				100000, 64 * 1024, clientName);
		long readOffset = getLastOffset(consumer, a_topic, a_partition,
				kafka.api.OffsetRequest.EarliestTime(), clientName);

		FetchRequest req = new FetchRequestBuilder().clientId(clientName)
				.addFetch(a_topic, a_partition, readOffset, 100000) // Note:
																	// this
																	// fetchSize
																	// of 100000
																	// might
																	// need to
																	// be
																	// increased
																	// if large
																	// batches
																	// are
																	// written
																	// to Kafka
				.build();
		FetchResponse fetchResponse = consumer.fetch(req);

		if (fetchResponse.hasError()) {
			// Something went wrong!
			short code = fetchResponse.errorCode(a_topic, a_partition);
			logger.debug("Error fetching data from the Broker:" + leadBroker
					+ " Reason: " + code);
			if (code == ErrorMapping.OffsetOutOfRangeCode()) {
				// We asked for an invalid offset. For simple case ask for the
				// last element to reset
				readOffset = getLastOffset(consumer, a_topic, a_partition,
						kafka.api.OffsetRequest.LatestTime(), clientName);
			}
			consumer.close();
			consumer = null;
			leadBroker = findNewLeader(leadBroker, a_topic, a_partition, a_port);
		}

		long numRead = 0;
		for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(
				a_topic, a_partition)) {
			long currentOffset = messageAndOffset.offset();
			if (currentOffset < readOffset) {
				logger.debug("Found an old offset: " + currentOffset
						+ " Expecting: " + readOffset);
				continue;
			}
			readOffset = messageAndOffset.nextOffset();
			ByteBuffer payload = messageAndOffset.message().payload();

			byte[] bytes = new byte[payload.limit()];
			payload.get(bytes);
			String serializedMessage = new String(bytes);
			messages.add(MessageMarshall.toMessage(serializedMessage));
			logger.debug(String.valueOf(messageAndOffset.offset()) + ": "
					+ serializedMessage);
			numRead++;
		}

		if (numRead == 0) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException ie) {
			}
		}

		if (consumer != null)
			consumer.close();
		return messages;
	}

	public List<Message> pollEvents(String a_topic, int a_partition,
			String a_seedBroker, int a_port) throws Exception {
		List<Message> messages = new ArrayList<Message>();
		// find the meta data about the topic and partition we are interested in
		//
		List<String> a_seedBrokers = new ArrayList<String>();
		a_seedBrokers.add(a_seedBroker);
		PartitionMetadata metadata = findLeader(a_seedBrokers, a_port, a_topic,
				a_partition);
		if (metadata == null) {
			System.out
					.println("Can't find metadata for Topic and Partition. Exiting");
			return messages;
		}
		if (metadata.leader() == null) {
			System.out
					.println("Can't find Leader for Topic and Partition. Exiting");
			return messages;
		}
		String leadBroker = metadata.leader().host();
		String clientName = "Client_" + a_topic + "_" + a_partition;

		SimpleConsumer consumer = new SimpleConsumer(leadBroker, a_port,
				100000, 64 * 1024, clientName);
		long readOffset = getLastOffset(consumer, a_topic, a_partition,
				kafka.api.OffsetRequest.LatestTime(), clientName);

		while (true) {
			if (consumer == null) {
				consumer = new SimpleConsumer(leadBroker, a_port, 100000,
						64 * 1024, clientName);
			}
			FetchRequest req = new FetchRequestBuilder().clientId(clientName)
					.addFetch(a_topic, a_partition, readOffset, 100000) // Note:
																		// this
																		// fetchSize
																		// of
																		// 100000
																		// might
																		// need
																		// to be
																		// increased
																		// if
																		// large
																		// batches
																		// are
																		// written
																		// to
																		// Kafka
					.build();
			FetchResponse fetchResponse = consumer.fetch(req);

			if (fetchResponse.hasError()) {
				// Something went wrong!
				short code = fetchResponse.errorCode(a_topic, a_partition);
				System.out.println("Error fetching data from the Broker:"
						+ leadBroker + " Reason: " + code);
				if (code == ErrorMapping.OffsetOutOfRangeCode()) {
					// We asked for an invalid offset. For simple case ask for
					// the last element to reset
					readOffset = getLastOffset(consumer, a_topic, a_partition,
							kafka.api.OffsetRequest.LatestTime(), clientName);
					continue;
				}
				consumer.close();
				consumer = null;
				leadBroker = findNewLeader(leadBroker, a_topic, a_partition,
						a_port);
				continue;
			}

			long numRead = 0;
			for (MessageAndOffset messageAndOffset : fetchResponse.messageSet(
					a_topic, a_partition)) {
				long currentOffset = messageAndOffset.offset();
				if (currentOffset < readOffset) {
					System.out.println("Found an old offset: " + currentOffset
							+ " Expecting: " + readOffset);
					continue;
				}
				readOffset = messageAndOffset.nextOffset();
				ByteBuffer payload = messageAndOffset.message().payload();

				byte[] bytes = new byte[payload.limit()];
				payload.get(bytes);
				String serializedMessage = new String(bytes);
				Message message = MessageMarshall.toMessage(serializedMessage);
				messages.add(message);
				logger.debug(String.valueOf(messageAndOffset.offset()) + ": "
						+ serializedMessage);
				numRead++;
				System.out.println("Message Publish Date : "
						+ message.getHeader().getMessagePublicationTime());
				System.out.println("Message Body : "
						+ message.getMessageBodies());
				System.out
						.println("============================================");
			}

			if (numRead == 0) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
				}
			}
		}
	}

	public static long getLastOffset(SimpleConsumer consumer, String topic,
			int partition, long whichTime, String clientName) {
		TopicAndPartition topicAndPartition = new TopicAndPartition(topic,
				partition);
		Map<TopicAndPartition, PartitionOffsetRequestInfo> requestInfo = new HashMap<TopicAndPartition, PartitionOffsetRequestInfo>();
		requestInfo.put(topicAndPartition, new PartitionOffsetRequestInfo(
				whichTime, 1));
		kafka.javaapi.OffsetRequest request = new kafka.javaapi.OffsetRequest(
				requestInfo, kafka.api.OffsetRequest.CurrentVersion(),
				clientName);
		OffsetResponse response = consumer.getOffsetsBefore(request);

		if (response.hasError()) {
			logger.debug("Error fetching data Offset Data the Broker. Reason: "
					+ response.errorCode(topic, partition));
			return 0;
		}
		long[] offsets = response.offsets(topic, partition);
		return offsets[0];
	}

	private String findNewLeader(String a_oldLeader, String a_topic,
			int a_partition, int a_port) throws Exception {
		for (int i = 0; i < 3; i++) {
			boolean goToSleep = false;
			PartitionMetadata metadata = findLeader(m_replicaBrokers, a_port,
					a_topic, a_partition);
			if (metadata == null) {
				goToSleep = true;
			} else if (metadata.leader() == null) {
				goToSleep = true;
			} else if (a_oldLeader.equalsIgnoreCase(metadata.leader().host())
					&& i == 0) {
				// first time through if the leader hasn't changed give
				// ZooKeeper a second to recover
				// second time, assume the broker did recover before failover,
				// or it was a non-Broker issue
				//
				goToSleep = true;
			} else {
				return metadata.leader().host();
			}
			if (goToSleep) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException ie) {
				}
			}
		}
		logger.debug("Unable to find new leader after Broker failure. Exiting");
		throw new Exception(
				"Unable to find new leader after Broker failure. Exiting");
	}

	private PartitionMetadata findLeader(List<String> a_seedBrokers,
			int a_port, String a_topic, int a_partition) {
		PartitionMetadata returnMetaData = null;
		loop: for (String seed : a_seedBrokers) {
			SimpleConsumer consumer = null;
			try {
				consumer = new SimpleConsumer(seed, a_port, 100000, 64 * 1024,
						"leaderLookup");
				List<String> topics = Collections.singletonList(a_topic);
				TopicMetadataRequest req = new TopicMetadataRequest(topics);
				kafka.javaapi.TopicMetadataResponse resp = consumer.send(req);

				List<TopicMetadata> metaData = resp.topicsMetadata();
				for (TopicMetadata item : metaData) {
					for (PartitionMetadata part : item.partitionsMetadata()) {
						if (part.partitionId() == a_partition) {
							returnMetaData = part;
							break loop;
						}
					}
				}
			} catch (Exception e) {
				logger.debug("Error communicating with Broker [" + seed
						+ "] to find Leader for [" + a_topic + ", "
						+ a_partition + "] Reason: " + e);
			} finally {
				if (consumer != null)
					consumer.close();
			}
		}
		if (returnMetaData != null) {
			m_replicaBrokers.clear();
			for (kafka.cluster.Broker replica : returnMetaData.replicas()) {
				m_replicaBrokers.add(replica.host());
			}
		}
		return returnMetaData;
	}

}