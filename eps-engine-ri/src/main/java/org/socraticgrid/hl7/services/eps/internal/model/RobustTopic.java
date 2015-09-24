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
package org.socraticgrid.hl7.services.eps.internal.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import kafka.javaapi.consumer.SimpleConsumer;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.MessageSerializationException;
import org.socraticgrid.hl7.services.eps.functional.KafkaSimpleConsumer;
import org.socraticgrid.hl7.services.eps.internal.validatationstep.subscription.DurabilityMatch;
import org.socraticgrid.hl7.services.eps.model.DeliveryAction;
import org.socraticgrid.hl7.services.eps.model.DeliveryReviewResult;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageHeader;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @author jhughes
 */
@Component
public class RobustTopic extends CommonTopicBase {
	private final Logger logger = LoggerFactory.getLogger(RobustTopic.class);
	private Cache<String, Message> topicMessages;
	private Cache<String, List<String>> consumedEventRegister;
	private int topicBufferSize = 100;
	private Producer<String, String> producer;
	private String kafkaTopic;

	private KafkaBrokerBean kafkaBrokerBean;

	private AtomicLong keyCnt = new AtomicLong();

	@Autowired
	private KafkaSimpleConsumer kafkaConsumer;

	public KafkaSimpleConsumer getKafkaConsumer() {
		return kafkaConsumer;
	}

	public void setKafkaConsumer(KafkaSimpleConsumer kafkaConsumer) {
		this.kafkaConsumer = kafkaConsumer;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.socraticgrid.hl7.services.eps.internal.model.CommonTopicBase#
	 * addStandardValidations()
	 */
	@Override
	protected void addStandardValidations() {
		super.addStandardValidations();

		// Add Stock Subscription Validations
		this.subscriptionValidationSteps.add(0, new DurabilityMatch(
				Durability.Robust));
	}

	@Override
	public String deleteEvent(Message event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String holdEventForApproval(Message event) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	protected void initialize() {
		logger.debug("Initializing Robust Topic");
		super.initialize();
		topicMessages = CacheBuilder.newBuilder().maximumSize(1000).build();
		consumedEventRegister = CacheBuilder.newBuilder().build();

		// kafkaTopic = topic.getPath() + topic.getName();
		kafkaTopic = topic.getName();

		// TODO JPH
		// Go and pull in prior events from the backing store
		// Construct a Consumer for this topic
		SimpleConsumer consumer = new SimpleConsumer(
				kafkaBrokerBean.getMetadataZKLead(),
				kafkaBrokerBean.getMetadataZKPort(), 100000, 64 * 1024,
				kafkaBrokerBean.getGroupId());
		long last = getOffset(consumer, kafka.api.OffsetRequest.LatestTime());
		long first = getOffset(consumer, kafka.api.OffsetRequest.EarliestTime());
		long offset = first;
		String msgKey = "0";
		while (offset != last) {
			// Build FetchRequest
			// consumer.fetch(req);
			// if (req.hasError()) puke;
			// for messagesets in response
			// Unmarshall message
			// offset += message.nextOffset();
			// String key = msg.getHeader().getMessageId();
			// topicMessages.put(key, msg);
		}

		// Close Consumer
		consumer.close();
		consumer = null;
		// Set keyCnt to last message id value
		keyCnt.set(Long.parseLong(msgKey));

		// Create a Kafka Producer - taken from their example
		Properties producerProps = new Properties();
		producerProps.put("metadata.broker.list",
				kafkaBrokerBean.getMetadataBrokerList());
		producerProps.put("serializer.class", "kafka.serializer.StringEncoder");
		// producerProps.put("partitioner.class",
		// KafkaProperties.partitionerClass);
		// producerProps.put("request.required.acks",
		// KafkaProperties.requestRequiredAcks);
		ProducerConfig config = new ProducerConfig(producerProps);

		producer = new Producer<String, String>(config);
	}

	@PreDestroy
	protected void destroy() {
		if (producer != null)
			producer.close();
	}

	private long getOffset(SimpleConsumer consumer, long whichTime) {
		// TODO JPH
		return 0l;
	}

	/**
	 * @param topicBufferSize
	 *            the topicBufferSize to set
	 */
	public void setTopicBufferSize(int topicBufferSize) {
		this.topicBufferSize = topicBufferSize;
	}

	@Override
	protected String storeEvent(Message event) {
		String key = "";
		try {
			// Get the next key
			key = Long.toString(keyCnt.incrementAndGet());
			logger.debug("Storing event to local cache under key " + key);
			event.getHeader().setMessageId(key);
			// Save to Cache
			topicMessages.put(key, event);
			// Save the event to the backing store
			producer.send(new KeyedMessage<String, String>(kafkaTopic,
					MessageMarshall.toString(event)));
		} catch (MessageSerializationException e) {
			e.printStackTrace();
		}

		return key;
	}

	@Override
	public List<MessageSummary> discoverEvents(User user, Date start, Date end) {
		List<MessageSummary> out = new LinkedList<MessageSummary>();
		String id = this.getTopicId();
		List<Message> persistedMessages = null;
		try {
			String topic = id;
			if (topic != null && topic.startsWith("/")) {
				topic = topic.replaceAll("/", "");
			}
			logger.debug("Fetching from Topic : " + topic);
			persistedMessages = kafkaConsumer.getPersistedEvents(topic, 0,
					kafkaBrokerBean.getMetadataZKLead(),
					kafkaBrokerBean.getMetadataZKPort());
		} catch (Exception e) {
			logger.error(
					"Some error occurred while fetching events from Kafka Topic : "
							+ id, e);
		}
		SearchType srchType;
		if (start != null) {
			if (end != null) {
				srchType = SearchType.within;
			} else {
				srchType = SearchType.after;
			}
		} else {
			if (end != null) {
				srchType = SearchType.before;
			} else {
				srchType = SearchType.open;
			}
		}
		if (persistedMessages != null) {
			for (Message msg : persistedMessages) {
				switch (srchType) {
				case open: {
					DeliveryReviewResult drResult = getDeliveryReview(id, user,
							msg);
					if (drResult.getAction() != DeliveryAction.Filter) {
						out.add(getSummary(drResult.getEvent()));
					}
					break;
				}
				case before: {
					Date chkDate = msg.getHeader().getMessagePublicationTime();
					if (chkDate.before(end)) {
						DeliveryReviewResult drResult = getDeliveryReview(id,
								user, msg);
						if (drResult.getAction() != DeliveryAction.Filter) {
							out.add(getSummary(drResult.getEvent()));
						}
					}
					break;
				}
				case after: {
					Date chkDate = msg.getHeader().getMessagePublicationTime();
					if (chkDate.after(start)) {
						DeliveryReviewResult drResult = getDeliveryReview(id,
								user, msg);
						if (drResult.getAction() != DeliveryAction.Filter) {
							out.add(getSummary(drResult.getEvent()));
						}
					}
					break;
				}
				case within: {
					Date chkDate = msg.getHeader().getMessagePublicationTime();
					if (chkDate.before(end)) {
						if (chkDate.after(start)) {
							DeliveryReviewResult drResult = getDeliveryReview(
									id, user, msg);
							if (drResult.getAction() != DeliveryAction.Filter) {
								out.add(getSummary(drResult.getEvent()));
							}
						}
					}
					break;
				}
				}
			}
		}
		return out;
	}

	private MessageSummary getSummary(Message msg) {
		MessageSummary out = new MessageSummary();
		MessageHeader hdr = msg.getHeader();
		out.setTopicId(hdr.getTopicId());
		out.setMessageId(hdr.getMessageId());
		out.setSubject(hdr.getSubject());
		out.setAuthor(hdr.getAuthor());
		out.setMessageCreatedTime(hdr.getMessageCreatedTime());
		out.setMessagePublicationTime(hdr.getMessagePublicationTime());
		out.setMessageExpirationTime(hdr.getMessageExpirationTime());
		out.setPriority(hdr.getPriority());
		out.setPublisher(hdr.getPublisher());
		return out;
	}

	private enum SearchType {
		open, within, after, before
	}

	@Override
	public List<Message> retrieveEvents(Subscription curSub,
			PullRange pullRange, Date start, Date end, List<String> mediaForms) {
		String id = this.getTopicId();
		LinkedList<Message> out = new LinkedList<Message>();

		Iterator<Message> itr = topicMessages.asMap().values().iterator();
		SearchType srchType;
		if (start != null) {
			if (end != null) {
				srchType = SearchType.within;
			} else {
				srchType = SearchType.after;
			}
		} else {
			if (end != null) {
				srchType = SearchType.before;
			} else {
				srchType = SearchType.open;
			}
		}
		switch (pullRange) {
		case Recent:
			while (itr.hasNext()) {
				Message msg = itr.next();
				DeliveryReviewResult drResult = getDeliveryReview(id, curSub,
						msg);
				if (drResult.getAction() != DeliveryAction.Filter) {
					out.add(drResult.getEvent());
				}
			}
			break;
		case Specific:
			String topic = id;
			if (topic != null && topic.startsWith("/")) {
				topic = topic.replaceAll("/", "");
			}
			logger.debug("Fetching from Topic : " + topic);
			List<Message> allPersistedMessages = null;
			try {
				allPersistedMessages = kafkaConsumer.getPersistedEvents(topic,
						0, kafkaBrokerBean.getMetadataZKLead(),
						kafkaBrokerBean.getMetadataZKPort());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(
						"Some error occurred while fetching events from Kafka Topic : "
								+ id, e);
			}
			if (allPersistedMessages != null) {
				for (Message message : allPersistedMessages) {
					switch (srchType) {
					case open: {
						DeliveryReviewResult drResult = getDeliveryReview(id,
								curSub, message);
						if (drResult.getAction() != DeliveryAction.Filter) {
							out.add(drResult.getEvent());
						}
						break;
					}
					case before: {
						Date chkDate = message.getHeader()
								.getMessagePublicationTime();
						if (chkDate.before(end)) {
							out.add(message);
							DeliveryReviewResult drResult = getDeliveryReview(
									id, curSub, message);
							if (drResult.getAction() != DeliveryAction.Filter) {
								out.add(drResult.getEvent());
							}
						}
						break;
					}
					case after: {
						Date chkDate = message.getHeader()
								.getMessagePublicationTime();
						if (chkDate.after(start)) {
							DeliveryReviewResult drResult = getDeliveryReview(
									id, curSub, message);
							if (drResult.getAction() != DeliveryAction.Filter) {
								out.add(drResult.getEvent());
							}

						}
						break;
					}
					case within: {
						Date chkDate = message.getHeader()
								.getMessagePublicationTime();
						if (chkDate.before(end)) {
							if (chkDate.after(start)) {
								DeliveryReviewResult drResult = getDeliveryReview(
										id, curSub, message);
								if (drResult.getAction() != DeliveryAction.Filter) {
									out.add(drResult.getEvent());
								}

							}
						}
						break;
					}
					}
				}
			}
			break;
		}
		return out;
	}

	@Override
	public List<Message> retrieveEvents(Subscription curSub,
			List<String> mediaForms) {
		String id = this.getTopicId();
		List<Message> out = new LinkedList<Message>();
		List<Message> persistedMessages = null;
		List<Message> unreadEvents = new ArrayList<Message>();
		List<String> consumedEventIds = new ArrayList<String>();
		try {
			String topic = id;
			if (topic != null && topic.startsWith("/")) {
				topic = topic.replaceAll("/", "");
			}
			logger.debug("Fetching from Topic : " + topic);
			persistedMessages = kafkaConsumer.getPersistedEvents(topic, 0,
					kafkaBrokerBean.getMetadataZKLead(),
					kafkaBrokerBean.getMetadataZKPort());
			Map<String, List<String>> register = consumedEventRegister.asMap();
			if (register != null) {
				consumedEventIds = register.get(curSub
						.getSubscriber().getUserId());
				logger.debug("Consumed IDs : "+consumedEventIds);
				if (consumedEventIds == null || consumedEventIds.size() <= 0) {
					consumedEventIds = new ArrayList<String>();
					register.put(curSub.getSubscriber().getUserId(), consumedEventIds);
					unreadEvents = persistedMessages;
				} else {
					for (Message event : persistedMessages) {
						if (!consumedEventIds.contains(event.getHeader()
								.getMessageId())) {
							unreadEvents.add(event);
						}
					}
				}
			} else {
				unreadEvents = persistedMessages;
			}
		} catch (Exception e) {
			logger.error(
					"Some error occurred while fetching events from Kafka Topic : "
							+ id, e);
		}
		if (unreadEvents != null) {
			for (Message msg : unreadEvents) {
				DeliveryReviewResult drResult = getDeliveryReview(id, curSub,
						msg);
				if (drResult.getAction() != DeliveryAction.Filter) {
					out.add(drResult.getEvent());
					consumedEventIds.add(msg.getHeader().getMessageId());
				}
			}
		}
		return out;
	}

	/**
	 * @return the kafkaBrokerBean
	 */
	public KafkaBrokerBean getKafkaBrokerBean() {
		return kafkaBrokerBean;
	}

	/**
	 * @param kafkaBrokerBean
	 *            the kafkaBrokerBean to set
	 */
	public void setKafkaBrokerBean(KafkaBrokerBean kafkaBrokerBean) {
		this.kafkaBrokerBean = kafkaBrokerBean;
	}
}
