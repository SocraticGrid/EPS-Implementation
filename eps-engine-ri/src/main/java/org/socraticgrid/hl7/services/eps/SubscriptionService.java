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

import java.security.Principal;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.socraticgrid.hl7.services.eps.exceptions.PubSubException;
import org.socraticgrid.hl7.services.eps.interfaces.SubscriptionIFace;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.PresenceState;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.User;
import org.socraticgrid.hl7.services.eps.service.impl.SubscriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "subscriptionService", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class SubscriptionService implements SubscriptionIFace {

	@Autowired
	@Qualifier("SubscriptionServiceImpl")
	SubscriptionServiceImpl subscriptionServiceImpl;

	@WebMethod(exclude = true)
	public SubscriptionServiceImpl getSubscriptionServiceImpl() {
		return subscriptionServiceImpl;
	}

	@WebMethod(exclude = true)
	public void setSubscriptionServiceImpl(
			SubscriptionServiceImpl subscriptionServiceImpl) {
		this.subscriptionServiceImpl = subscriptionServiceImpl;
	}

	@Resource
	private WebServiceContext webCtx;

	private final Logger logger = LoggerFactory
			.getLogger(SubscriptionService.class);

	@Override
	public String subscribe(@WebParam(name = "topics") List<String> topics,
			@WebParam(name = "type") SubscriptionType type,
			@WebParam(name = "options") Options options,
			@WebParam(name = "callbackAddress") String callbackAddress)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException {

		String out = null;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);

		try {
			logger.debug("Subscribe to Topics");
			out = subscriptionServiceImpl.subscribe(user, topics, type, options,
					callbackAddress);
		} catch (PubSubException exp) {
			logger.warn("Expected possible Exception Subscribe to Topics", exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception Subscribe to Topisc", exp);
		}
		return out;
	}

	@Override
	public boolean unsubscribe(@WebParam(name = "topics") List<String> topics,
			@WebParam(name = "subscriberId") String subscriberId,
			@WebParam(name = "subscriptionid") String subscriptionid)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, NoSuchItemException, InvalidDataException {

		boolean out = false;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("Unsubscribe to Topics");
			out = subscriptionServiceImpl.unsubscribe(user, topics,
					subscriberId, subscriptionid);
		} catch (PubSubException exp) {
			logger.warn("Expected possible Exception Unsubscribe to Topics",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception Unsubscribe to Topics", exp);
		}
		return out;
	}

	@Override
	public boolean unsubscribeByOptions(
			@WebParam(name = "topics") List<String> topics,
			@WebParam(name = "subscriberId") String subscriberId,
			@WebParam(name = "options") Options options)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, NoSuchItemException, InvalidDataException {
		boolean out = false;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("unsubscribeByOptions to Topics");
			out = subscriptionServiceImpl.unsubscribeByOptions(user, topics,
					subscriberId, options);
		} catch (PubSubException exp) {
			logger.warn(
					"Expected possible Exception unsubscribeByOptions to Topics",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception unsubscribeByOptions to Topics",
					exp);
		}

		return out;
	}

	@Override
	public List<Message> retrieveEvents(@WebParam(name = "topic") String topic,
			@WebParam(name = "pullRange") PullRange pullRange,
			@WebParam(name = "start") Date start,
			@WebParam(name = "end") Date end,
			@WebParam(name = "mediaForms") List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {

		List<Message> out = null;
		Principal userCtx = webCtx.getUserPrincipal();
		logger.debug(userCtx + "");
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("retrieveEvents to Topics: " + topic);
			out = subscriptionServiceImpl.retrieveEvents(user, topic,
					pullRange, start, end, mediaForms);
		} catch (PubSubException exp) {
			logger.warn("Expected possible Exception retrieveEvents to Topic",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception retrieveEvents to Topic", exp);
			out = new LinkedList<Message>();
		}

		return out;
	}

	@Override
	public List<MessageSummary> retrieveEventSummaries(
			@WebParam(name = "topic") String topic,
			@WebParam(name = "pullRange") PullRange pullRange,
			@WebParam(name = "start") Date start,
			@WebParam(name = "end") Date end,
			@WebParam(name = "mediaForms") List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {

		List<MessageSummary> out = null;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("retrieveEventSummaries to Topics: " + topic);
			out = subscriptionServiceImpl.retrieveEventSummaries(user, topic,
					pullRange, start, end, mediaForms);
		} catch (PubSubException exp) {
			logger.warn(
					"Expected possible Exception retrieveEventSummaries to Topic",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error(
					"Unexcepted Exception retrieveEventSummaries to Topic", exp);
			out = new LinkedList<MessageSummary>();
		}
		return out;
	}

	// TODO The name of this method is suspect - It should be retrieveEvents

	@Override
	public List<Message> retrieveEvent(@WebParam(name = "topic") String topic,
			@WebParam(name = "mediaForms") List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		List<Message> out = null;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("retrieveEvent to Topics: " + topic);
			out = subscriptionServiceImpl.retrieveEvents(user, topic,
					mediaForms);

		} catch (PubSubException exp) {
			logger.warn("Expected possible Exception retrieveEvent to Topic",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception retrieveEvent to Topic", exp);
		}
		return out;
	}

	@Override
	public boolean replayEvents(@WebParam(name = "topics") List<String> topics,
			@WebParam(name = "start") Date start,
			@WebParam(name = "end") Date end,
			@WebParam(name = "replayId") String replayId,
			@WebParam(name = "mediaForms") List<String> mediaForms,
			@WebParam(name = "replayRate") int replayRate)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			InvalidDataException {
		boolean out = false;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("replayEvents for Topics");
			out = subscriptionServiceImpl.replayEvents(user, topics, start,
					end, replayId, mediaForms, replayRate);
		} catch (PubSubException exp) {
			logger.warn("Expected possible Exception replayEvents for Topics",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception replayEvents for Topics", exp);
		}
		return out;
	}

	@Override
	public boolean assertSubscriberPresence(
			@WebParam(name = "subscriberId") String subscriberId,
			@WebParam(name = "presence") PresenceState presence)
			throws NotAuthorizedException, ConflictException,
			AuthenicationRequiredException, ExpiredException {
		boolean out = false;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("retrieveEvent ");
			out = subscriptionServiceImpl.assertSubscriberPresence(user,
					subscriberId, presence);
		} catch (PubSubException exp) {
			logger.warn("Expected possible Exception retrieveEvent", exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error("Unexcepted Exception retrieveEvent", exp);
		}
		return out;
	}

	@Override
	public boolean configureSubscriptionOptions(
			@WebParam(name = "topic") String topic,
			@WebParam(name = "subscriberId") String subscriberId,
			@WebParam(name = "subscriptionId") String subscriptionId,
			@WebParam(name = "options") Options options)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException, IncompleteDataException,
			FeatureNotAvailableException, ConflictException,
			NoSuchItemException, InvalidDataException {
		boolean out = false;
		Principal userCtx = webCtx.getUserPrincipal();
		User user = getUserFromPrinciple(userCtx);
		logger.debug("User = " + user);
		try {
			logger.debug("configureSubscriptionOptions to Topics: " + topic);
			out = subscriptionServiceImpl.configureSubscriptionOptions(user,
					topic, subscriberId, subscriptionId, options);
		} catch (PubSubException exp) {
			logger.warn(
					"Expected possible Exception configureSubscriptionOptions to Topic",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error(
					"Unexcepted Exception configureSubscriptionOptions to Topic",
					exp);
		}
		return out;
	}

	@Override
	public Options getDefaultSuscriptionOptions(
			@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, FeatureNotAvailableException {
		Options out = null;
		try {
			logger.debug("getDefaultSuscriptionOptions to Topics: " + topic);
			out = subscriptionServiceImpl.getDefaultSuscriptionOptions(topic);
		} catch (PubSubException exp) {
			logger.warn(
					"Expected possible Exception getDefaultSuscriptionOptions to Topic",
					exp);
			throw exp;
		} catch (Throwable exp) {
			logger.error(
					"Unexcepted Exception getDefaultSuscriptionOptions to Topic",
					exp);
		}
		return out;
	}

	private User getUserFromPrinciple(Principal webUser) {
		User user = new User();
		user.setUserId(webUser.getName());
		return user;
	}
}
