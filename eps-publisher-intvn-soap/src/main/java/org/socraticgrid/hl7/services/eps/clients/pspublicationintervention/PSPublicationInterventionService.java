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
package org.socraticgrid.hl7.services.eps.clients.pspublicationintervention;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;


import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.ReviewResult;
import org.socraticgrid.hl7.services.eps.interfaces.PSPublicationInterventionIFace;

@WebService(name = "PSPublicationReview", targetNamespace = "org.socraticgrid.hl7.services.eps.clients")
public class PSPublicationInterventionService implements PSPublicationInterventionIFace {

	
	@WebMethod(exclude = true)
	public PSPublicationInterventionIFace getPublicationInterventionImpl() {
		return publicationInterventionImpl;
	}
	@WebMethod(exclude = true)
	public void setPublicationInterventionImpl(
			PSPublicationInterventionIFace publicationInterventionImpl) {
		this.publicationInterventionImpl = publicationInterventionImpl;
	}

	private PSPublicationInterventionIFace publicationInterventionImpl;

	public PSPublicationInterventionService() {
	}

	public ReviewResult reviewMessage(@WebParam(name = "message") Message message) {
	
		return publicationInterventionImpl.reviewMessage(message);
	}


}
