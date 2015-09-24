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
package org.socraticgrid.hl7.services.eps.clients.psdeliveryintervention;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;

import org.socraticgrid.hl7.services.eps.interfaces.PSDeliveryInterventionIFace;
import org.socraticgrid.hl7.services.eps.model.DeliveryReviewResult;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.Subscription;

@WebService(name = "PSDeliveryIntervention", targetNamespace = "org.socraticgrid.hl7.services.eps.clients")
public class PSDeliveryInterventionService implements
		PSDeliveryInterventionIFace {

	private PSDeliveryInterventionIFace deliveryInterventionImpl;

	@WebMethod(exclude = true)
	public PSDeliveryInterventionIFace getDeliveryInterventionImpl() {
		return deliveryInterventionImpl;
	}

	@WebMethod(exclude = true)
	public void setDeliveryInterventionImpl(
			PSDeliveryInterventionIFace deliveryInterventionImpl) {
		this.deliveryInterventionImpl = deliveryInterventionImpl;
	}

	public PSDeliveryInterventionService() {
	}

	@Override
	public DeliveryReviewResult reviewContent(
			@WebParam(name = "userId") String userid,
			@WebParam(name = "event") Message event) {
		return deliveryInterventionImpl.reviewContent(userid, event);
	}

	@Override
	public DeliveryReviewResult reviewMessage(
			@WebParam(name = "userId") String userid,
			@WebParam(name = "event") Message event) {
		return deliveryInterventionImpl.reviewMessage(userid, event);
	}

	/**
	 * 
	 * @param userId
	 * @param subscription
	 * @param topicId
	 * @return True iif access is allowed
	 */
	@Override
	public boolean controlTopicAccess(@WebParam(name = "userId") String userId,
			@WebParam(name = "subscription") Subscription subscription,
			@WebParam(name = "topic") String topic) {
		return deliveryInterventionImpl.controlTopicAccess(userId,
				subscription, topic);
	}

}
