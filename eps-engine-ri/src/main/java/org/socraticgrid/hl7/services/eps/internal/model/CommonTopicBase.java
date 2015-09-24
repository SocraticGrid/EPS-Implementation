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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.PSDeliveryInterventionIFace;
import org.socraticgrid.hl7.services.eps.interfaces.PSPublicationInterventionIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.PublicationValidationStepIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.SubscriptionIdGeneratorIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.SubscriptionValidationStepIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.processes.PushToSubscriber;
import org.socraticgrid.hl7.services.eps.internal.processes.SubscriptionValidation;
import org.socraticgrid.hl7.services.eps.model.DeliveryAction;
import org.socraticgrid.hl7.services.eps.model.DeliveryReviewResult;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.Topic;
import org.socraticgrid.hl7.services.eps.model.TopicSummary;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class CommonTopicBase implements TopicIFace {
	@Autowired
	SubscriptionIdGeneratorIFace idGen;
	

	public SubscriptionIdGeneratorIFace getIdGen() {
		return idGen;
	}

	public void setIdGen(SubscriptionIdGeneratorIFace idGen) {
		this.idGen = idGen;
	}

	private final Logger logger = LoggerFactory
			.getLogger(CommonTopicBase.class);

	protected List<PSDeliveryInterventionIFace> deliveryInterventions = null;
	protected TopicIFace parentTopic;
	protected List<PSPublicationInterventionIFace> pubInterventions = null;
	protected List<PublicationValidationStepIFace> publicationValidationSteps = null;
	protected List<Subscription> subscriptions = null;
	protected List<SubscriptionValidationStepIFace> subscriptionValidationSteps = null;

	@Autowired
	protected SubscriptionValidation subscriptionValidator;

	public SubscriptionValidation getSubscriptionValidator() {
		return subscriptionValidator;
	}

	public void setSubscriptionValidator(
			SubscriptionValidation subscriptionValidator) {
		this.subscriptionValidator = subscriptionValidator;
	}

	protected Map<String, TopicIFace> subTopics;

	protected Topic topic;

	
	/*
	 * Call to allow sub classes to add additional validations
	 */
	protected void addStandardValidations() {
		//
	}

	@Override
	public String addSubscription(Subscription subscription)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException {
		String subscriptionId = subscription.getId();
		if(subscriptionId == null || subscriptionId.trim().length()<=0) {
			subscriptionId = idGen.generateSubscriptionId(this, subscription, null);
		}

		subscription.setId(subscriptionId);
		if (validateSubscription(subscription)) {
			this.subscriptions.add(subscription);
		}

		// Add to Inner topic subscriptions
		topic.getSubscriptionsList().add(subscription);

		return subscriptionId;
	}

	@Override
	public String deleteEvent(Message event) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<PSDeliveryInterventionIFace> getDeliveryInterventions() {

		return deliveryInterventions;
	}

	@Override
	public TopicIFace getParentTopic() {
		return parentTopic;
	}

	@Override
	public List<PSPublicationInterventionIFace> getPublicationInterventions() {
		return pubInterventions;
	}

	@Override
	public List<PublicationValidationStepIFace> getPublicationValidationSteps() {
		return publicationValidationSteps;
	}

	@Override
	public List<Subscription> getSubscriptions() {
		return this.subscriptions;
	}

	@Override
	public List<SubscriptionValidationStepIFace> getSubscriptionValdiationSteps() {
		return subscriptionValidationSteps;
	}

	@Override
	public TopicIFace getSubTopic(String subtopic) {
		return subTopics.get(subtopic);
	}

	@Override
	public Map<String, TopicIFace> getSubTopics() {
		return subTopics;
	}

	/**
	 * @return the topic
	 */
	@Override
	public Topic getTopic() {
		return topic;
	}

	@Override
	public String getTopicId() {
		String out;
		if (this.parentTopic != null) {
			out = parentTopic.getTopicId() + "/" + topic.getName();
		} else if (topic != null) {

			out = topic.getName();
		} else {
			out = "";
		}
		return out;
	}

	@Override
	public String holdEventForApproval(Message event) {
		// TODO Auto-generated method stub
		return null;
	}

	@PostConstruct
	protected void initialize() {
		// Initialize only if not already set via a property
		if (pubInterventions == null)
			pubInterventions = new LinkedList<PSPublicationInterventionIFace>();
		if (subscriptions == null)
			subscriptions = new LinkedList<Subscription>();
		if (publicationValidationSteps == null)
			publicationValidationSteps = new LinkedList<PublicationValidationStepIFace>();
		if (subscriptionValidationSteps == null)
			subscriptionValidationSteps = new LinkedList<SubscriptionValidationStepIFace>();
		if (deliveryInterventions == null)
			deliveryInterventions = new LinkedList<PSDeliveryInterventionIFace>();

		addStandardValidations();

		if (topic == null) {
			topic = new Topic();
			topic.setName("");
			topic.setTopicId(this.getTopicId());
			// TODO Initialize the nested topic
		}
		if (topic.getSubscriptionsList() != null) {
			if (topic.getSubscriptionsList().isEmpty() == false) {
				subscriptions.addAll(topic.getSubscriptionsList());
			}
		}
		validateExistingSubscriptions();

		// Replace Inner topic subscriptions

		topic.setSubscriptionsList(subscriptions);

	}

	@Override
	public String publishEvent(Message event) {

		// Broadly - Add it to the topic
		String key = storeEvent(event);

		// Ask Channels to dispatch as required
		pushEventToChannels(event);
		return key;
	}

	/*
	 * Saves event and assigns it's id.
	 */
	abstract protected String storeEvent(Message event);
	

	
	
	/*
	 * Handle giving the message to each channel.
	 */
	protected void pushEventToChannels(Message event) {
		String id = this.getTopicId();

		logger.debug("Pushing event " + event.getHeader().getMessageId());
		// TODO - Decouple and a runnable here
		
		logger.debug("Subscriptions to check " + subscriptions.size());
		Iterator<Subscription> itr = subscriptions.iterator();
		while (itr.hasNext()) {
			Subscription curSub = itr.next();
			String user = curSub.getSubscriber().getUserId() + "("
					+ curSub.getSubscriber().getName() + ")";
			logger.debug("Check subscriber " + user);
			
			DeliveryReviewResult drResult = getDeliveryReview(id,curSub,event);
			
			if (drResult.getAction() != DeliveryAction.Filter)
			{
				if (curSub.getType() == SubscriptionType.Push) {
					PushToSubscriber task = new PushToSubscriber(id, curSub, drResult.getEvent());
					try {
						logger.debug("Pushing to subscriber " + user);
						PushStatus result = task.call();
						curSub.setLastMessage(event.getHeader().getMessageId());
					} catch (Exception e) {
						logger.error(e.toString());
					}
				}
			}
		}
	}

	@Override
	public void removeSubscription(Subscription subscription)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException {
		topic.getSubscriptionsList().remove(subscription);
	}

	@Override
	public void removeSubscriptionById(String subscriptionId)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDeliveryInterventions(
			List<PSDeliveryInterventionIFace> interventions) {
		deliveryInterventions = interventions;

	}

	@Override
	public void setParentTopic(TopicIFace parent) {
		parentTopic = parent;
	}

	@Override
	public void setPublicationInterventions(
			List<PSPublicationInterventionIFace> interventions) {
		this.pubInterventions = interventions;
	}

	@Override
	public void setPublicationValidationSteps(
			List<PublicationValidationStepIFace> steps) {
		//

		this.publicationValidationSteps = steps;
	}

	@Override
	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;

	}

	@Override
	public void setSubScriptionValdiationSteps(
			List<SubscriptionValidationStepIFace> steps) {
		subscriptionValidationSteps = steps;

	}

	@Override
	public void setSubTopics(Map<String, TopicIFace> subTopics) {
		this.subTopics = subTopics;
	}

	/**
	 * @param topic
	 *            the topic to set
	 */
	@Override
	public void setTopic(Topic topic) {
		this.topic = topic;
	}

	protected void validateExistingSubscriptions() {
		Iterator<Subscription> itr = subscriptions.iterator();
		while (itr.hasNext()) {
			Subscription subscription = itr.next();

			boolean valid;
			try {
				valid = subscriptionValidator.validateSubscription(
						subscription, this);
			} catch (NotAuthorizedException | AuthenicationRequiredException
					| ExpiredException | FeatureNotAvailableException
					| InvalidDataException | NoSuchTopicException e) {
				valid = false;
			}
			if (!valid) {
				// Remove the subscription from the list
				logger.warn("Static Subscription invalid and removed from runtime, please correct.  = "
						+ subscription.toString());
				itr.remove();
			}
		}
	}

	@Override
	public boolean validateSubscription(Subscription subscription)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException {
		return true;
	}
	
	@Override
	public TopicSummary getTopicSummary()
	{
		TopicSummary out = new TopicSummary();
		out.setName(getTopic().getName());
		out.setTopicId(this.getTopicId());
		out.setDescription("TODO");
		return null;
		
	}
	@Override
	public List<TopicSummary> getSubTopicList() {
		 List<TopicSummary> out = new LinkedList<TopicSummary>();
		 Map <String,TopicIFace> tpMap = getSubTopics();
		 if (tpMap != null)
		 {
			 Iterator<TopicIFace> itr = tpMap.values().iterator();
			 while (itr.hasNext())
			 {
				 TopicIFace tp = itr.next();
				 out.add(tp.getTopicSummary());
			 }
		 }
		 return out;
	}
	
	public Subscription findSubscription(User user, SubscriptionType type)
	{
		//TODO  Implement actual lookup
		Subscription out = null;
		for(Subscription sub : getSubscriptions()) {
			if(sub.getSubscriber().getUserId().equals(user.getUserId())) {
				out = sub;
			}
		}
		//TODO  Implement Auto subscribe
		
		//Return an anonymous subscription
//		Subscription out = new Subscription();
//		out.setType(type);
//		out.setSubscriber(user);
		
		return out;
	}
	
	protected DeliveryReviewResult getDeliveryReview(String id, Subscription curSub, Message event)
	{
		DeliveryReviewResult out = new DeliveryReviewResult();		
		out.setEvent(event);
		out.setAction(DeliveryAction.Pass);
	
		//TODO   Implement the delivery intervention change.
		
		return out;
	}
	
	protected DeliveryReviewResult getDeliveryReview(String id, User user, Message event)
	{
		DeliveryReviewResult out = new DeliveryReviewResult();		
		out.setEvent(event);
		out.setAction(DeliveryAction.Pass);
	
		//TODO   Implement the delivery intervention change.
		
		return out;
	}
}
