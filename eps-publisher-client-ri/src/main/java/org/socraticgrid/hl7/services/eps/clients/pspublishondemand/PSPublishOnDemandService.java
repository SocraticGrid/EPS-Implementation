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
package org.socraticgrid.hl7.services.eps.clients.pspublishondemand;

import java.util.Date;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.PSPublishOnDemandIFace;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.TopicSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "PSPublishOnDemand", targetNamespace = "org.socraticgrid.hl7.services.eps.clients")
public class PSPublishOnDemandService implements PSPublishOnDemandIFace {

	@Autowired
	@Qualifier("PSPublishOnDemandImpl")
	private PSPublishOnDemandIFace publishOnDemandImpl;

	public PSPublishOnDemandService() {
	}

	public List<TopicSummary> query(@WebParam(name = "messageId") String query) {
		return publishOnDemandImpl.discoverTopics(query);
	}

	public boolean endPublishingOnTopic(@WebParam(name = "topic") String topic) {

		return publishOnDemandImpl.endPublishingOnTopic(topic);
	}

	@Override
	public List<TopicSummary> getSubTopics(
			@WebParam(name = "topic") String topic) {
		return publishOnDemandImpl.getSubTopics(topic);
	}

	@Override
	public boolean replayEvents(
			@WebParam(name = "messageId") List<String> topics,
			@WebParam(name = "start") Date start,
			@WebParam(name = "end") Date end,
			@WebParam(name = "replayId") String replayId,
			@WebParam(name = "mediaForms") List<String> mediaForms,
			@WebParam(name = "replayRate") int replayRate)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {

		return publishOnDemandImpl.replayEvents(topics, start, end, replayId,
				mediaForms, replayRate);
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

		return publishOnDemandImpl.retrieveEvents(topic, pullRange, start, end,mediaForms);
	}
	@Override
	public List<MessageSummary> retrieveEventSumariess(
			@WebParam(name = "topic") String topic,
			@WebParam(name = "pullRange") PullRange pullRange,
			@WebParam(name = "start") Date start,
			@WebParam(name = "end") Date end,
			@WebParam(name = "mediaForms") List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		return publishOnDemandImpl.retrieveEventSumariess(topic, pullRange,
				start, end, mediaForms);
	}
	@Override
	public boolean startPublishingOnTopic(
			@WebParam(name = "startTime") Date startTime,
			@WebParam(name = "topic") String topic) {
		return publishOnDemandImpl.startPublishingOnTopic(startTime, topic);
	}

	@Override
	public List<TopicSummary> discoverTopics(String query) {
		// TODO Auto-generated method stub
		return publishOnDemandImpl.discoverTopics(query);
	}

	@Override
	public List<Message> retrieveEvents(String topic, Date start, Date end,
			List<String> mediaForms) throws NotAuthorizedException,
			AuthenicationRequiredException, NoSuchTopicException,
			ExpiredException, FeatureNotAvailableException,
			MediaFormatNotExceptedException, NoSuchItemException {
		// TODO Auto-generated method stub
		return publishOnDemandImpl.retrieveEvents(topic, start, end, mediaForms);
	}

}
