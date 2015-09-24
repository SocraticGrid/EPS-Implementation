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
package org.socraticgrid.hl7.services.eps.internal.validatationstep.subscription;

import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.internal.interfaces.SubscriptionValidationStepIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Subscription;

public class DurabilityMatch implements SubscriptionValidationStepIFace {

	Durability requiredDurability;
	
	public DurabilityMatch()
	{
		
	}
	
	public DurabilityMatch(Durability requiredDurability)
	{
		this.requiredDurability = requiredDurability;
	}
	

	public boolean valdiateSubscription(Subscription subscription,
			TopicIFace topic) throws NotAuthorizedException,
			AuthenicationRequiredException, ExpiredException,
			FeatureNotAvailableException, InvalidDataException,
			MediaFormatNotExceptedException, NoSuchTopicException,
			IncompleteDataException {
		
		if (subscription.getDurability()!= requiredDurability)
		{
			throw new InvalidDataException("Topic is configured for "+requiredDurability.toString()+" Durability only");
		}
		return true;
	}


	/**
	 * @return the requiredDurability
	 */
	public Durability getRequiredDurability() {
		return requiredDurability;
	}


	/**
	 * @param requiredDurability the requiredDurability to set
	 */
	public void setRequiredDurability(Durability requiredDurability) {
		this.requiredDurability = requiredDurability;
	}

}
