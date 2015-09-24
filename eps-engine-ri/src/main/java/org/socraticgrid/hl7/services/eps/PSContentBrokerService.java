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

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.PSContentBrokeringIFace;
import org.socraticgrid.hl7.services.eps.model.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "pscontentbroker", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class PSContentBrokerService implements PSContentBrokeringIFace {

	@Autowired
	@Qualifier("PSContentBrokeringtServiceImpl")
	PSContentBrokeringIFace pscontentBrokerImpl;

	@Override
	public MessageBody getContent(
			@WebParam(name = "messageId") String messageId,
			@WebParam(name = "forms") List<String> forms)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchItemException {
		return pscontentBrokerImpl.getContent(messageId, forms);
	}

	@Override
	public MessageBody getPreferredContent(
			@WebParam(name = "messageId") String messageId)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchItemException {
		return pscontentBrokerImpl.getPreferredContent(messageId);
	}

	@Override
	public boolean supportsContentForm(
			@WebParam(name = "messageId") String messageId,
			@WebParam(name = "form") String form)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchItemException {
		return pscontentBrokerImpl.supportsContentForm(messageId, form);
	}

}
