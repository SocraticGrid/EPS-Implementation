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
package org.socraticgrid.hl7.services.eps.clients.pssubscriptionclient;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.interfaces.PSSubscriptionClientIFace;
import org.socraticgrid.hl7.services.eps.model.ManagementEvent;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "PSSubscriptionClient", targetNamespace = "org.socraticgrid.hl7.services.eps.clients")
public class PSSubscriptionClientService implements PSSubscriptionClientIFace {
	@Autowired
	@Qualifier("PSSubscriptionClientImpl")
	PSSubscriptionClientIFace psSubscriberClientImpl;

	private final Logger logger = LoggerFactory
			.getLogger(PSSubscriptionClientService.class);

	public PSSubscriptionClientService() {
	}

	@Override
	public boolean topicEvent(@WebParam(name = "topic") String topic,
			@WebParam(name = "event") Message event) {
		logger.debug("In Topic Envent");
		return psSubscriberClientImpl.topicEvent(topic, event);
	}

	@Override
	public boolean topicManagementEvent(@WebParam(name = "topic") String topic,
			@WebParam(name = "event") ManagementEvent event) {
		return psSubscriberClientImpl.topicManagementEvent(topic, event);
	}

}
