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
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.functional.CommonSubscriptionValidationSteps;
import org.socraticgrid.hl7.services.eps.interfaces.PSDeliveryInterventionIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.SubscriptionValidationStepIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;

public class SubscriptionValidation {
	private final Logger logger = LoggerFactory
			.getLogger(SubscriptionValidation.class);
	
	
	@Autowired
	private CommonSubscriptionValidationSteps commonSubscriptionValidations;



	public boolean validateSubscription(Subscription subscription,
			TopicIFace topic) throws NotAuthorizedException,
			AuthenicationRequiredException, ExpiredException,
			FeatureNotAvailableException, InvalidDataException,
			MediaFormatNotExceptedException, NoSuchTopicException,
			IncompleteDataException {
		boolean out = true;
		Iterator<SubscriptionValidationStepIFace> itr = commonSubscriptionValidations.getValidationSteps().iterator();
		while (itr.hasNext()) {
			SubscriptionValidationStepIFace step = itr.next();

			try {
				out = step.valdiateSubscription(subscription, topic);
			} catch (NotAuthorizedException | AuthenicationRequiredException
					| ExpiredException | FeatureNotAvailableException
					| InvalidDataException | NoSuchTopicException e) {
				logger.warn("Rejected by Common Validation Step: "
						+ step.toString());
				throw e;
			}
			if (out == false) {
				logger.warn("Rejected by Common Validation Step: "
						+ step.toString());
				break;
			}

		}

		// We enforce topic specific validations here?
		itr = topic.getSubscriptionValdiationSteps().iterator();
		while (itr.hasNext()) {
			SubscriptionValidationStepIFace step = itr.next();
			try {
				out = step.valdiateSubscription(subscription, topic);
			} catch (NotAuthorizedException | AuthenicationRequiredException
					| ExpiredException | FeatureNotAvailableException
					| InvalidDataException | NoSuchTopicException e) {
				logger.warn("Rejected by Topic Validation Step: "
						+ step.toString()+", Exp = "+e.getMessage());
				throw e;
			}
			if (out == false) {
				logger.warn("Rejected by Topic Validation Step: "
						+ step.toString());
				break;
			}
		}

		// Check the engines last
		Iterator<PSDeliveryInterventionIFace> delvyItr = topic
				.getDeliveryInterventions().iterator();
		while (delvyItr.hasNext()) {
			PSDeliveryInterventionIFace intervention = delvyItr.next();
			out = intervention.controlTopicAccess(subscription.getSubscriber()
					.getUserId(), subscription, topic.getTopicId());
			if (out == false) {
				logger.warn("Rejected by DeliveryIntervention: "
						+ intervention.toString());
				throw new NotAuthorizedException();
			}
		}

		return out;
	}
}
