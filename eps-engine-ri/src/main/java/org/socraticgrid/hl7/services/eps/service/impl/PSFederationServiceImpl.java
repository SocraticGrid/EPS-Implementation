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

import java.util.Set;

import org.socraticgrid.hl7.services.eps.interfaces.PSFederationIFace;
import org.socraticgrid.hl7.services.eps.model.LinkManagementMessage;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.SynchronizationMessage;
import org.socraticgrid.hl7.services.eps.model.SynchronizationStatus;

/**
 * @author Jerry Goodnough
 *
 */
public class PSFederationServiceImpl implements PSFederationIFace {

	/**
	 * 
	 */
	public PSFederationServiceImpl() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see org.socraticgrid.hl7.services.eps.interfaces.PSFederationIFace#receiveEvents(java.lang.String, java.util.Set)
	 */
	@Override
	public void receiveEvents(String sourceId, Set<Message> events) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.socraticgrid.hl7.services.eps.interfaces.PSFederationIFace#pullEvents(java.lang.String)
	 */
	@Override
	public Set<Message> pullEvents(String targetId) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.socraticgrid.hl7.services.eps.interfaces.PSFederationIFace#synchronize(org.socraticgrid.hl7.services.eps.model.SynchronizationMessage)
	 */
	@Override
	public SynchronizationStatus synchronize(SynchronizationMessage syncMsg) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.socraticgrid.hl7.services.eps.interfaces.PSFederationIFace#linkMananageUpdate(org.socraticgrid.hl7.services.eps.model.LinkManagementMessage)
	 */
	@Override
	public boolean linkMananageUpdate(LinkManagementMessage msg) {
		// TODO Auto-generated method stub
		return false;
	}

}
