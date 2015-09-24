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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;



public class TransientTopic extends CommonTopicBase {

	private final Logger logger = LoggerFactory.getLogger(TransientTopic.class);
	
	private int topicBufferSize = 100;

	private AtomicLong keyCnt = new AtomicLong();
	
	Cache<String, Message> topicMessages; 
	
	
	/* (non-Javadoc)
	 * @see org.socraticgrid.hl7.services.eps.internal.model.CommonTopicBase#addStandardValidations()
	 */
	@Override
	protected void addStandardValidations() {
		super.addStandardValidations();
		
		//Add Stock Subscription Validations 
		this.subscriptionValidationSteps.add(0, new DurabilityMatch(Durability.Transient ));
	
	}
	
	@Override
	public String deleteEvent(Message event) {
		String status="";
		Iterator<Message> itr = topicMessages.asMap().values().iterator();
		while(itr.hasNext()){
			Message msg = itr.next();
			if(event.getHeader().getMessageId().equals(msg.getHeader().getMessageId())){
				topicMessages.invalidate(event.getHeader().getMessageId());
				status="Message removed successfully.";
				return status;
			}
		}
		status="Message does not exist to remove.";
		return status;
	}

	/**
	 * @return the topicBufferSize
	 */
	public int getTopicBufferSize() {
		return topicBufferSize;
	}

	@Override
	public String holdEventForApproval(Message event) {
		// TODO Auto-generated method stub
		return null;
	}



	@PostConstruct
	protected void initialize() {
		super.initialize();
		
		
		topicMessages=CacheBuilder.newBuilder()
	    .maximumSize(1000)
	    .build();
		
	}

	/**
	 * @param topicBufferSize the topicBufferSize to set
	 */
	public void setTopicBufferSize(int topicBufferSize) {
		this.topicBufferSize = topicBufferSize;
	}

	@Override
	protected String storeEvent(Message event) {
	
		//Get the next key
		String key = Long.toString(keyCnt.incrementAndGet());
		logger.debug("Storing event to local cache under key "+key);
		event.getHeader().setMessageId(key);
		//Save to Cache
		topicMessages.put(key, event);
		//Save the event to the local store	
		return key;
	}

	@Override
	public List<MessageSummary> discoverEvents(User user, Date start, Date end) {
		List<MessageSummary> out = new LinkedList<MessageSummary>();
		Iterator<Message> itr = topicMessages.asMap().values().iterator();
		
		String id = this.getTopicId();
				
		SearchType srchType;
		if (start != null)
		{
			if (end != null)
			{
				srchType = SearchType.within;
			}
			else
			{
				srchType = SearchType.after;
			}
		}
		else
		{
			if (end != null)
			{
				srchType = SearchType.before;
			}
			else
			{
				srchType = SearchType.open;
			}
		}
		while(itr.hasNext())
		{
			Message msg = itr.next();
			switch(srchType)
			{
				case open:
				{
					DeliveryReviewResult drResult = getDeliveryReview(id,user,msg);
					if (drResult.getAction() != DeliveryAction.Filter)
					{
						out.add(getSummary(drResult.getEvent()));
					}
					break;
				}
				case before:
				{
					Date chkDate =  msg.getHeader().getMessagePublicationTime();
					if (chkDate.before(end))
					{
						DeliveryReviewResult drResult = getDeliveryReview(id,user,msg);
						if (drResult.getAction() != DeliveryAction.Filter)
						{
							out.add(getSummary(drResult.getEvent()));
						}
					}
					break;
				}
				case after:
				{
					Date chkDate =  msg.getHeader().getMessagePublicationTime();
					if (chkDate.after(start))
					{
						DeliveryReviewResult drResult = getDeliveryReview(id,user,msg);
						if (drResult.getAction() != DeliveryAction.Filter)
						{
							out.add(getSummary(drResult.getEvent()));
						}
					}
					break;
				}
				case within:
				{
					Date chkDate =  msg.getHeader().getMessagePublicationTime();
					if (chkDate.before(end))
					{
						if (chkDate.after(start))
						{
							DeliveryReviewResult drResult = getDeliveryReview(id,user,msg);
							if (drResult.getAction() != DeliveryAction.Filter)
							{
								out.add(getSummary(drResult.getEvent()));
							}
						}
					}
					break;
				}
			}
		}
		
		return out;
	}

	private MessageSummary getSummary(Message msg)
	{
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
	
	private enum SearchType
	{
		open, within, after, before
	}

	@Override
	public List<Message> retrieveEvents(Subscription curSub, PullRange pullRange,
			Date start, Date end, List<String> mediaForms)
	{
		String id = this.getTopicId();
		
		
		LinkedList<Message> out = new LinkedList<Message>();
		
		Iterator<Message> itr = topicMessages.asMap().values().iterator();
		SearchType srchType;
		if (start != null)
		{
			if (end != null)
			{
				srchType = SearchType.within;
			}
			else
			{
				srchType = SearchType.after;
			}
		}
		else
		{
			if (end != null)
			{
				srchType = SearchType.before;
			}
			else
			{
				srchType = SearchType.open;
			}
		}
		while(itr.hasNext())
		{
			Message msg = itr.next();
			switch(srchType)
			{
				case open:
				{
					DeliveryReviewResult drResult = getDeliveryReview(id,curSub,msg);
					if (drResult.getAction() != DeliveryAction.Filter)
					{
						out.add(drResult.getEvent());
					}
					break;
				}
				case before:
				{
					Date chkDate =  msg.getHeader().getMessagePublicationTime();
					if (chkDate.before(end))
					{
						out.add(msg);
						DeliveryReviewResult drResult = getDeliveryReview(id,curSub,msg);
						if (drResult.getAction() != DeliveryAction.Filter)
						{
							out.add(drResult.getEvent());
						}
					}
					break;
				}
				case after:
				{
					Date chkDate =  msg.getHeader().getMessagePublicationTime();
					if (chkDate.after(start))
					{
						DeliveryReviewResult drResult = getDeliveryReview(id,curSub,msg);
						if (drResult.getAction() != DeliveryAction.Filter)
						{
							out.add(drResult.getEvent());
						}
						
					}
					break;
				}
				case within:
				{
					Date chkDate =  msg.getHeader().getMessagePublicationTime();
					if (chkDate.before(end))
					{
						if (chkDate.after(start))
						{
							DeliveryReviewResult drResult = getDeliveryReview(id,curSub,msg);
							if (drResult.getAction() != DeliveryAction.Filter)
							{
								out.add(drResult.getEvent());
							}

						}
					}
					break;
				}
			}
		}
		return out;
	}

	@Override
	public List<Message> retrieveEvents(Subscription curSub, List<String> mediaForms)
	{
		String id = this.getTopicId();
		LinkedList<Message> out = new LinkedList<Message>();
		
		Iterator<Message> itr = topicMessages.asMap().values().iterator();
	
		while(itr.hasNext())
		{
			Message msg = itr.next();
			DeliveryReviewResult drResult = getDeliveryReview(id,curSub,msg);
			if (drResult.getAction() != DeliveryAction.Filter)
			{
				out.add(drResult.getEvent());
			}
		}
		return out;
	}




}
