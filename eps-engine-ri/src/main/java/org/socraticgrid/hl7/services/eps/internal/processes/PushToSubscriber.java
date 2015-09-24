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
package org.socraticgrid.hl7.services.eps.internal.processes;

import java.util.Date;
import java.util.concurrent.Callable;

import javax.xml.ws.BindingProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.accessclients.pssubscriber.PSSubscriptionClientServiceSE;
import org.socraticgrid.hl7.services.eps.interfaces.PSSubscriptionClientIFace;
import org.socraticgrid.hl7.services.eps.internal.model.PushStatus;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.NotificationAddress;
import org.socraticgrid.hl7.services.eps.model.Subscription;

/*
 * Task Oriented Class
 */
public class PushToSubscriber implements Callable<PushStatus> {

	/*
	 * Possible properties
	 * 
	 *  Use common or local pool
	 *  local pool size
	 *  Worker Threads
	 *  QOS
	 *  Priority
	 *  Track Delivery
	 *  
	 *  Replay
	 *  Replay Target
	 *  
	 */
	
	
	private final Logger logger = LoggerFactory
			.getLogger(PushToSubscriber.class);
	
	Subscription subscriber;
	Message event;
	String topic;
	
	public PushToSubscriber(String topic, Subscription subscriber, Message event)
	{
		this.subscriber=subscriber;
		this.event=event;
		this.topic=topic;
	}
	
	@Override
	public PushStatus call() throws Exception {
		PushStatus out = new PushStatus();
		out.setMessageId(event.getHeader().getMessageId());
		out.setDelivered(false);
		
		//TODO   Replace with a better channel implementation
	
		
		NotificationAddress address = subscriber.getSubscriberNotificationAddress();
		//TODO  Change to string format
		String idString =event.getHeader().getMessageId()+", for Topic "+topic+" to Subscriber "+subscriber.getSubscriber().getName()+" at "+address.getEndpoint(); 
		logger.debug("Push Event: "+idString);
		
		PSSubscriptionClientServiceSE ss = new PSSubscriptionClientServiceSE();
		
		PSSubscriptionClientIFace port = ss.getPSSubscriptionClientPort();
		((BindingProvider)port).getRequestContext().put(
        	    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        	    address.getEndpoint());
		try
		{
			boolean status = port.topicEvent(topic, event);
	
			if (status)
			{	
				out.setDeliveryTime(new Date(System.currentTimeMillis()));
				out.setDelivered(status);
				logger.debug("Delivered Event: "+idString);
			}
			else
			{
				logger.debug("Delivery failed to "+idString);
			}

		}
		catch(Exception exp)
		{
			logger.error("Exception during event push: "+idString,exp);
		}
		
		return out;
	}

	
	
}
