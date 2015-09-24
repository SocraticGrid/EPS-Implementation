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

import java.util.Iterator;
import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.functional.CommonPublicationInterventions;
import org.socraticgrid.hl7.services.eps.functional.CommonPublicationValidationSteps;
import org.socraticgrid.hl7.services.eps.functional.Configuration;
import org.socraticgrid.hl7.services.eps.interfaces.PSPublicationInterventionIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.PublicationValidationStepIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicLocatorIFace;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.ReviewAction;
import org.socraticgrid.hl7.services.eps.model.ReviewResult;
import org.springframework.beans.factory.annotation.Autowired;

public class PublicationAcceptance {
	private final Logger logger = LoggerFactory.getLogger(PublicationAcceptance.class);

	@Autowired
	CommonPublicationValidationSteps commonValidation;

	@Autowired
	CommonPublicationInterventions commonInterventions;

	@Autowired
	TopicLocatorIFace topicLocator;

	@Autowired
	Configuration config;

	public String publishEvent(String topic, Message event)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, IncompleteDataException,
			InvalidDataException, MediaFormatNotExceptedException {

		
		logger.debug("Start Publication Acceptance");
		// This process will check a publication
		if (topic == null) {
			throw new NoSuchTopicException();
		}
		logger.debug("Locate Topic");
		
		TopicIFace theTopic;
		theTopic = topicLocator.locateTopic(topic);
		if (theTopic == null) {
			throw new NoSuchTopicException();
		}
		logger.debug("Check config");

		boolean validated = false;
		boolean holdForApproval = false;
//		if (config.isPublicationPreCheck()) {
//			validateEvent(theTopic, event);
//			validated = true;
//		}
		logger.debug("Build Interventions");

		// Build List of interventions
		LinkedList<PSPublicationInterventionIFace> interventionList = new LinkedList<PSPublicationInterventionIFace>();
		interventionList.addAll(commonInterventions.getInterventions());
		interventionList.addAll(theTopic.getPublicationInterventions());

		if (!interventionList.isEmpty()) {
			logger.debug("Processing publication interventions");
			// Don't bother doing a clone when the list is empty
			// Message check = (Message) event.clone();
			
			// TODO Implement protection scheme.
			Iterator<PSPublicationInterventionIFace> itr = interventionList
					.iterator();
			while (itr.hasNext()) {
				
				PSPublicationInterventionIFace intervention = itr.next();
				//TODO Do we ask the engine if needs a clone?
				logger.debug("Check :"+intervention.toString());
				logger.debug("Bodies before call "+event.getMessageBodies().size());
				ReviewResult result = intervention.reviewMessage(event);
				logger.debug("Bodies after call "+event.getMessageBodies().size());
				ReviewAction action = result.getAction();
				logger.debug("Review Action = "+action);
				if (action == ReviewAction.RequestApproval) {
					holdForApproval = true;;
				}
				if (action == ReviewAction.Rejected) {
					// TODO Consider a rejection message
					throw new InvalidDataException();
				}
				if (action == ReviewAction.Approved) {
					// Some engines might override others
					holdForApproval = false;
				}
				if (result.isAltered()) {
					// Update version to use
					logger.debug("Transformer Altered Message");
				}
			}

		}
//		if (!validated) {
//			logger.debug("Validate Event");
//
//			validateEvent(theTopic, event);
//		}
		String id;
		if (holdForApproval) {
			logger.debug("Hold for approval");

			id = theTopic.holdEventForApproval(event);
		} else {
			logger.debug("Publish Event");
			id = theTopic.publishEvent(event);
		}
		return id;
	}

	private void validateEvent(TopicIFace topic, Message event)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, IncompleteDataException,
			InvalidDataException, MediaFormatNotExceptedException {
		// First Handle all common validations
		String topicId = topic.getTopicId();
		Iterator<PublicationValidationStepIFace> itr = commonValidation.getValidationSteps()
				.iterator();
		while (itr.hasNext()) {
			PublicationValidationStepIFace step = itr.next();
			step.valdiateMessage(topicId, event);
		}
		// Now handle topic specific validations
		itr = topic.getPublicationValidationSteps().iterator();
		while (itr.hasNext()) {
			PublicationValidationStepIFace step = itr.next();
			step.valdiateMessage(topicId, event);
		}
	}

}
