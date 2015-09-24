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
import java.util.Date;
import java.util.List;

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

/**
 * On Demand Publisher Implementation
 * @author Jerry Goodnough
 * @version 1.0
 * @created 04-Jan-2014 6:15:20 PM
 */
public class OnDemandPublisher extends Publisher implements PSPublishOnDemandIFace {

	public OnDemandPublisher(){

	}

	@Override
	public List<TopicSummary> discoverTopics(String query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean endPublishingOnTopic(String Topic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<TopicSummary> getSubTopics(String topic) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean replayEvents(List<String> topics, Date start, Date end,
			String replayId, List<String> mediaForms, int replayRate)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		// TODO Auto-generated method stub
		return false;
	}

	

	@Override
	public List<Message> retrieveEvents(String topic, PullRange pullRange,
			Date start, Date end, List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<MessageSummary> retrieveEventSumariess(String topic,
			PullRange pullRange, Date start, Date end, List<String> mediaForms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ExpiredException,
			FeatureNotAvailableException, MediaFormatNotExceptedException,
			NoSuchItemException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean startPublishingOnTopic(Date StartTime, String Topic) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Message> retrieveEvents(String topic, Date start, Date end,
			List<String> mediaForms) throws NotAuthorizedException,
			AuthenicationRequiredException, NoSuchTopicException,
			ExpiredException, FeatureNotAvailableException,
			MediaFormatNotExceptedException, NoSuchItemException {
		// TODO Auto-generated method stub
		return null;
	}




}