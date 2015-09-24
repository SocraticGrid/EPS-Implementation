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
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicLocatorIFace;
import org.socraticgrid.hl7.services.eps.model.AddressType;
import org.socraticgrid.hl7.services.eps.model.ContractType;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.NotificationAddress;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.PresenceState;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionServiceImpl {

	//TODO   Extend this implementation to include User
	
	@Autowired
	TopicLocatorIFace topicLocator;
	
	public TopicLocatorIFace getTopicLocator() {
		return topicLocator;
	}

	public void setTopicLocator(TopicLocatorIFace topicLocator) {
		this.topicLocator = topicLocator;
	}

	public SubscriptionServiceImpl() {
	}

	public String subscribe(User user, List<String> topics, SubscriptionType type,
			Options options, String callbackAddress)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException {
		// Checking User authentication
		if(user == null) {
			throw new AuthenicationRequiredException();
		}
		// Checking Authorization
		
		// Preparing Subscription Model
		Subscription subscription = new Subscription();
		subscription.setSubscriber(user);
		subscription.setType(type);
		subscription.setDurability(options.getDurability());
		switch (type) {
		case Pull:
			subscription.setContractType(ContractType.Pull);	
			break;
		case Push:
			subscription.setContractType(ContractType.Push);
			NotificationAddress notificationAddress = new NotificationAddress();
			notificationAddress.setType(AddressType.URI);
			notificationAddress.setEndpoint(callbackAddress);
			subscription.setSubscriberNotificationAddress(notificationAddress);
			break;
		default:
			subscription.setContractType(ContractType.Pull);
		}
		
		// Adding subscription for each topic
		for(String topic : topics) {
			TopicIFace theTopic = topicLocator.locateTopic(topic);
			if(theTopic == null) {
				NoSuchTopicException exp = new NoSuchTopicException();
				exp.setTopicId(topic);
				throw exp;
			}
			theTopic.addSubscription(subscription);
		}
		return subscription.getSubscriber().getUserId();
	}

	public boolean unsubscribe(User user, List<String> topics, String subscriberId,
			String subscriptionid) throws NotAuthorizedException,
			AuthenicationRequiredException, NoSuchTopicException,
			NoSuchItemException, InvalidDataException {
		boolean status = true;
		
		// Checking User authentication
		if(user == null) {
			throw new AuthenicationRequiredException();
		}
		
		// Removing subscription from each topic
		for(String topic : topics) {
			TopicIFace theTopic = topicLocator.locateTopic(topic);
			if(theTopic == null) {
				NoSuchTopicException exp = new NoSuchTopicException();
				exp.setTopicId(topic);
				throw exp;
			}
			List<Subscription> subscriptions = theTopic.getSubscriptions();
			List<Subscription> subsToBeRemoved = new ArrayList<Subscription>();
			int unmatchedCount = 0;
			for(Subscription subscription : subscriptions) {
				if(subscriptionid != null && subscriptionid.trim().length() > 0) {
					if(subscriberId.equals(subscription.getSubscriber().getUserId()) && subscriptionid.equals(subscription.getId())) {
						subsToBeRemoved.add(subscription);
					} else {
						unmatchedCount++;
					}
				} else {
					if(subscription.getSubscriber().getUserId().equals(subscriberId)) {
						subsToBeRemoved.add(subscription);
					} else {
						unmatchedCount++;
					}
				}
			}
			if(subscriptions.size() == unmatchedCount) {
				throw new NoSuchItemException();
			}
			
			// Finally removing the subscriptions
			for(Subscription subTobeRemoved : subsToBeRemoved) {
				try {
					theTopic.removeSubscription(subTobeRemoved);
				} catch (ExpiredException | FeatureNotAvailableException e) {
					status = false;
					throw new InvalidDataException(e.getMessage());
				}
			}
		}
		return status;
	}

	
	public boolean unsubscribeByOptions(User user, List<String> topics,
			String subscriberId, Options options)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, NoSuchItemException, InvalidDataException {
		boolean status = true;
		
		// Checking User authentication
		if(user == null) {
			throw new AuthenicationRequiredException();
		}
		
		// Removing subscription from each topic
		for(String topic : topics) {
			TopicIFace theTopic = topicLocator.locateTopic(topic);
			if(theTopic == null) {
				NoSuchTopicException exp = new NoSuchTopicException();
				exp.setTopicId(topic);
				throw exp;
			}
			List<Subscription> subscriptions = theTopic.getSubscriptions();
			List<Subscription> subsToBeRemoved = new ArrayList<Subscription>();
			int unmatchedCount = 0;
			for(Subscription subscription : subscriptions) {
				if(options != null && options.getDurability() != null) {
					if(subscriberId.equals(subscription.getSubscriber().getUserId()) && options.getDurability().equals(subscription.getDurability())) {
						subsToBeRemoved.add(subscription);
					} else {
						unmatchedCount++;
					}
				} else {
					if(subscription.getSubscriber().getUserId().equals(subscriberId)) {
						subsToBeRemoved.add(subscription);
					} else {
						unmatchedCount++;
					}
				}
			}
			if(subscriptions.size() == unmatchedCount) {
				throw new NoSuchItemException();
			}
			
			// Finally removing the subscriptions
			for(Subscription subTobeRemoved : subsToBeRemoved) {
				try {
					theTopic.removeSubscription(subTobeRemoved);
				} catch (ExpiredException | FeatureNotAvailableException e) {
					status = false;
					throw new InvalidDataException(e.getMessage());
				}
			}
		}
		return status;
	}


	public List<Message> retrieveEvents(User user, String topic, PullRange pullRange,
			Date start, Date end, List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {

		
		List<Message> out = new LinkedList<Message>();
		
		if (start != null)
		{
			if (end != null)
			{
				if (end.compareTo(start)<0)
				{
					throw new ExpiredException("End date before start date");
				}
			}
		}	
		
		TopicIFace top = topicLocator.locateTopic(topic);
		if (top == null)
		{
			NoSuchTopicException exp = new NoSuchTopicException();
			exp.setTopicId(topic);
			throw exp;
		}
		
		Subscription curSub = top.findSubscription(user, SubscriptionType.Pull);
		if (curSub != null)
		{
			out = top.retrieveEvents(curSub, pullRange, start, end, mediaForms);
		}
		else
		{
			//TODO  Address autosubscribe issues
			NotAuthorizedException exp = new NotAuthorizedException();
			exp.setMessage("No pull subscription for this user");
			throw exp;
		}
		
		return out;
	}


	public List<MessageSummary> retrieveEventSummaries(User user,String topic,
			PullRange pullRange, Date start, Date end, List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		
		//TODO  Implement Pull Range Dynamics
		//TODO  Implement MediaForms
		
		List<MessageSummary> out;
		if (start != null)
		{
			if (end != null)
			{
				if (end.compareTo(start)<0)
				{
					throw new ExpiredException("End date before start date");
				}
			}
		}
		TopicIFace top = topicLocator.locateTopic(topic);
		if (top == null)
		{
			NoSuchTopicException exp = new NoSuchTopicException();
			exp.setTopicId(topic);
			throw exp;
		}
		else
		{
		
			out = top.discoverEvents(user, start, end);
			
		}	
		return out;
	}


	public List<Message> retrieveEvents(User user, String topic, List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		
		List<Message> out = new LinkedList<Message>();
		
		TopicIFace top = topicLocator.locateTopic(topic);
		if (top == null)
		{
			NoSuchTopicException exp = new NoSuchTopicException();
			exp.setTopicId(topic);
			throw exp;
		}
		else
		{
			Subscription curSub = top.findSubscription(user, SubscriptionType.Pull);
			if (curSub != null)
			{	
				out = top.retrieveEvents(curSub, mediaForms);
			}
			else
			{
				//TODO  Address autosubscribe issues
				NotAuthorizedException exp = new NotAuthorizedException();
				exp.setMessage("No pull subscription for this user");
				throw exp;
			}
			
		}	
		
		return out;
	}

	
	public boolean replayEvents(User user, List<String> topics, Date start, Date end,
			String replayId, List<String> mediaForms, int replayRate)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			InvalidDataException {

		throw new FeatureNotAvailableException();
	
	}


	public boolean assertSubscriberPresence(User user, String subscriberId,
			PresenceState presence) throws NotAuthorizedException,
			ConflictException, AuthenicationRequiredException, ExpiredException {
		// TODO Auto-generated method stub
		return false;
	}


	public boolean configureSubscriptionOptions(User user, String topic,
			String subscriberId, String subscriptionId, Options options)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException, IncompleteDataException,
			FeatureNotAvailableException, ConflictException,
			NoSuchItemException, InvalidDataException {
		// TODO Auto-generated method stub
		return false;
	}

	public Options getDefaultSuscriptionOptions(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, FeatureNotAvailableException {
		Options out = new Options();
		return out;
	}

}
