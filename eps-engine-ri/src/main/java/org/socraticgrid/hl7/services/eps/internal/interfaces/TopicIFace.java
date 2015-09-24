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
package org.socraticgrid.hl7.services.eps.internal.interfaces;

import java.util.Date;
import java.util.List;
import java.util.Map;

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
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.Topic;
import org.socraticgrid.hl7.services.eps.model.TopicSummary;
import org.socraticgrid.hl7.services.eps.model.User;

public interface TopicIFace {
	
	public String getTopicId();

	public Topic getTopic();

	public void setTopic(Topic topic);

	public String publishEvent(Message event);

	public String holdEventForApproval(Message event);

	public String deleteEvent(Message event);

	public TopicIFace getSubTopic(String subtopic);

	public void setParentTopic(TopicIFace parent);

	public TopicIFace getParentTopic();

	public void setSubTopics(Map<String, TopicIFace> subTopics);

	public Map<String, TopicIFace> getSubTopics();
	
	public List<TopicSummary> getSubTopicList();

	public List<PSDeliveryInterventionIFace> getDeliveryInterventions();
	public void setDeliveryInterventions(List<PSDeliveryInterventionIFace> interventions);
	
	public List<PSPublicationInterventionIFace> getPublicationInterventions();

	public void setPublicationInterventions(
			List<PSPublicationInterventionIFace> interventions);

	public List<PublicationValidationStepIFace> getPublicationValidationSteps();

	public void setPublicationValidationSteps(List<PublicationValidationStepIFace> steps);

	public List<Subscription> getSubscriptions();

	public void setSubscriptions(List<Subscription> subscriptions);

	public String addSubscription(Subscription subscription)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException;

	public boolean validateSubscription(Subscription subscription)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException;

	public void removeSubscription(Subscription subscription)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException;
	
	public void removeSubscriptionById(String subscriptionId)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, MediaFormatNotExceptedException,
			NoSuchTopicException, IncompleteDataException;
	
	public List<SubscriptionValidationStepIFace> getSubscriptionValdiationSteps();
	public void  setSubScriptionValdiationSteps(List<SubscriptionValidationStepIFace> steps);
	
	public TopicSummary getTopicSummary();
	
	public List<MessageSummary> discoverEvents(User user, Date start, Date end);
	
	public List<Message> retrieveEvents(Subscription sub, PullRange pullRange, Date start, Date end, List<String> mediaForms);

	public List<Message> retrieveEvents(Subscription sub, List<String> mediaForms);
	
	
	public Subscription findSubscription(User user, SubscriptionType type);

}
