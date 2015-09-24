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

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.BrokerIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicLocatorIFace;
import org.socraticgrid.hl7.services.eps.internal.processes.TopicQuery;
import org.socraticgrid.hl7.services.eps.model.Capabilities;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.RequestStatus;
import org.socraticgrid.hl7.services.eps.model.TopicSummary;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;

public class BrokerServiceImpl implements BrokerIFace {
	@Autowired
	TopicLocatorIFace topicLocator;

	@Override
	public List<TopicSummary> discoverTopics(String query)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, ExpiredException, NoSuchTopicException {
		List<TopicSummary> out; 
		if (query.isEmpty() || (query.compareTo("/")==0))
		{
			out = new LinkedList<TopicSummary>();
			TopicIFace topic = topicLocator.locateTopic(query);
			if (topic != null)
			{
				Map<String,TopicIFace> subTopics = topic.getSubTopics();
				Iterator<String> itr = subTopics.keySet().iterator();
				while(itr.hasNext())
				{
					String topicName = itr.next();
					TopicIFace tp = subTopics.get(topicName);
					out.add(tp.getTopicSummary());
				}
				
			}
		}
		else
		{
			//We have a non root level query
			TopicQuery tpquery = new TopicQuery();
			
			out = tpquery.discoverTopics(query);
		}
		return out;
	}
	

	@Override
	public List<TopicSummary> getSubTopics(String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException {
		
		List<TopicSummary> out; 
		TopicIFace top = topicLocator.locateTopic(topic);
		if (top == null)
		{
			NoSuchTopicException exp = new NoSuchTopicException();
			exp.setTopicId(topic);
			throw exp;
		}
		else
		{
			out = top.getSubTopicList();
			
		}
		
		return out;
	}

	@Override
	public Options getTopicOptions(String topic) throws NotAuthorizedException,
			AuthenicationRequiredException, FeatureNotAvailableException,
			NoSuchTopicException {
	
		Options out; 
		out = new Options();
		TopicIFace top = topicLocator.locateTopic(topic);
		if (top == null)
		{
			NoSuchTopicException exp = new NoSuchTopicException();
			exp.setTopicId(topic);
			throw exp;
		}
		else
		{
			out = top.getTopic().getOptionsList();
			
		}	

		return out;
	}

	@Override
	public List<MessageSummary> discoverEventsForTopic(String topic,
			Date start, Date end) throws NotAuthorizedException,
			AuthenicationRequiredException, InvalidDataException,
			ExpiredException, NoSuchTopicException {

		User user = null;
		
		List<MessageSummary> out;
		if (start != null)
		{
			if (end != null)
			{
				if (end.compareTo(start)<0)
				{
					throw new InvalidDataException("End date before start date");
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

	@Override
	public Capabilities discoverCapabilities() throws NotAuthorizedException,
			AuthenicationRequiredException {
		Capabilities out = new Capabilities();
		out.setDurableTopics(false);
		out.setPublishOnDeman(false);
		out.setPushEvents(true);
		return out;
	}

	@Override
	public RequestStatus requestAccess(User userInfo) throws ConflictException,
			FeatureNotAvailableException, InvalidDataException,
			IncompleteDataException {
		throw new FeatureNotAvailableException();
		//return null;
	}


}
