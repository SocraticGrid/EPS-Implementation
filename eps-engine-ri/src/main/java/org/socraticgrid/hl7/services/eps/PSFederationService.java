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
package org.socraticgrid.hl7.services.eps;

import java.util.Set;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.socraticgrid.hl7.services.eps.interfaces.PSFederationIFace;
import org.socraticgrid.hl7.services.eps.model.LinkManagementMessage;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.SynchronizationMessage;
import org.socraticgrid.hl7.services.eps.model.SynchronizationStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "psfederation", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class PSFederationService implements PSFederationIFace {

	@Autowired
	@Qualifier("PSFederationServiceImpl")
	PSFederationIFace psfederationImpl;

	@Override
	public Set<Message> pullEvents(@WebParam(name = "targetId") String targetId) {

		return psfederationImpl.pullEvents(targetId);
	}

	@Override
	public SynchronizationStatus synchronize(
			@WebParam(name = "syncMsg") SynchronizationMessage syncMsg) {

		return psfederationImpl.synchronize(syncMsg);
	}

	@Override
	public boolean linkMananageUpdate(
			@WebParam(name = "msg") LinkManagementMessage msg) {

		return psfederationImpl.linkMananageUpdate(msg);
	}

	@Override
	public void receiveEvents(@WebParam(name = "sourceId") String sourceId,
			@WebParam(name = "events") Set<Message> events) {
		psfederationImpl.receiveEvents(sourceId, events);

	}

}
