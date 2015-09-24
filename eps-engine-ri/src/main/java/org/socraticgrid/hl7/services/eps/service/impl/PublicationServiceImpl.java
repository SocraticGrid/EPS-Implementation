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

import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.PublicationIFace;
import org.socraticgrid.hl7.services.eps.internal.processes.PublicationAcceptance;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.PresenceState;
import org.springframework.beans.factory.annotation.Autowired;

public class PublicationServiceImpl implements PublicationIFace {

	@Autowired
	PublicationAcceptance pubAccept;
	
	public PublicationServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String publishEvent(String topic, Message event)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, IncompleteDataException,
			InvalidDataException, MediaFormatNotExceptedException {
		
		//Delgate to the Publication Acceptance process	
		return pubAccept.publishEvent(topic, event);
	}

	@Override
	public void deleteEvent(String topic, String messageId)
			throws NoSuchItemException, NoSuchTopicException, ExpiredException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean assertPublisherPresence(String publisherId,
			PresenceState presence) throws NotAuthorizedException,
			ConflictException, AuthenicationRequiredException, ExpiredException {
		// TODO Auto-generated method stub
		return false;
	}

}
