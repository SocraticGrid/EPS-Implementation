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
package org.socraticgrid.hl7.services.eps;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.exceptions.PubSubException;
import org.socraticgrid.hl7.services.eps.interfaces.BrokerIFace;
import org.socraticgrid.hl7.services.eps.model.Capabilities;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.RequestStatus;
import org.socraticgrid.hl7.services.eps.model.TopicSummary;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "broker", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class BrokerService implements BrokerIFace {
	//TODO   Address User context issues
	
	@Autowired
	@Qualifier("BrokerServiceImpl")
	BrokerIFace brokerImpl;

	private final Logger logger = LoggerFactory
			.getLogger(BrokerService.class);
	
	@Override
	public List<TopicSummary> discoverTopics(
			@WebParam(name = "query") String query)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, ExpiredException, NoSuchTopicException {

		List<TopicSummary> out=null;
		
		try
		{
			logger.debug("DiscoverTopics: "+query);
			out = brokerImpl.discoverTopics(query);
		}
		catch(PubSubException exp)
		{
			logger.warn("Expected possible Exception in discoverTopics",exp);
			throw exp;
		}
		catch(Throwable exp)
		{
			logger.error("Unexcepted Exception in discoverTopics",exp);
			out = new LinkedList<TopicSummary>();
		}

		return out;
	}

	@Override
	public List<TopicSummary> getSubTopics(
			@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException {
		List<TopicSummary> out=null;
		try
		{
			logger.debug("Get SubTopics: "+topic);
			out = brokerImpl.getSubTopics(topic);
		}
		catch(PubSubException exp)
		{
			logger.warn("Expected possible Exception in getSubTopics",exp);
			throw exp;
		}
		catch(Throwable exp)
		{
			logger.error("Unexcepted Exception in getSubTopics",exp);
			out = new LinkedList<TopicSummary>();
		}
		return out;
		
	}

	@Override
	public Options getTopicOptions(@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			FeatureNotAvailableException, NoSuchTopicException {

		Options out=null;
		try
		{
			logger.debug("getTopicOptions: "+topic);
			out =brokerImpl.getTopicOptions(topic);
		}
		catch(PubSubException exp)
		{
			logger.warn("Expected possible Exception in getTopicOptions",exp);
			throw exp;
		}
		catch(Throwable exp)
		{
			logger.error("Unexcepted Exception in getTopicOptions",exp);
		}
		
		return out;
	}

	@Override
	public List<MessageSummary> discoverEventsForTopic(
			@WebParam(name = "topic") String topic,
			@WebParam(name = "start") Date start,
			@WebParam(name = "end") Date end) throws NotAuthorizedException,
			AuthenicationRequiredException, InvalidDataException,
			ExpiredException, NoSuchTopicException {
		List<MessageSummary> out = null;
		
		try
		{
			logger.debug("discoverEventsForTopic: "+topic);
			out= brokerImpl.discoverEventsForTopic(topic, start, end);
		}
		catch(PubSubException exp)
		{
			logger.warn("Expected possible Exception in discoverEventsForTopic",exp);
			throw exp;
		}
		catch(Throwable exp)
		{
			logger.error("Unexcepted Exception in discoverEventsForTopic",exp);
		}
		
		return out;
	}

	@Override
	public Capabilities discoverCapabilities() throws NotAuthorizedException,
			AuthenicationRequiredException {
		Capabilities  out = null;
		try
		{
			logger.debug("discoverCapabilities");
			out = brokerImpl.discoverCapabilities();
		}
		catch(PubSubException exp)
		{
			logger.warn("Expected possible Exception in discoverCapabilities",exp);
			throw exp;
		}
		catch(Throwable exp)
		{
			logger.error("Unexcepted Exception in discoverCapabilities",exp);
		}
		
		return out;
	}
	
	@Override
	public RequestStatus requestAccess(
			@WebParam(name = "userInfo") User userInfo)
			throws ConflictException, FeatureNotAvailableException,
			InvalidDataException, IncompleteDataException {
		RequestStatus out = null;
		try
		{
			logger.debug("requestAccess: "+userInfo);
			out = brokerImpl.requestAccess(userInfo);
		}
		catch(PubSubException exp)
		{
			logger.warn("Expected possible Exception in requestAccess",exp);
			throw exp;
		}
		catch(Throwable exp)
		{
			logger.error("Unexcepted Exception in requestAccess",exp);
		}
		
		
		return out;
	}

}
