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
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.TopicManagementIFace;
import org.socraticgrid.hl7.services.eps.model.AccessRequest;
import org.socraticgrid.hl7.services.eps.model.Affiliation;
import org.socraticgrid.hl7.services.eps.model.AffiliationMapping;
import org.socraticgrid.hl7.services.eps.model.AffiliationRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "topicmanagement", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class TopicManagementService implements TopicManagementIFace {

	@Autowired
	@Qualifier("TopicManagementServiceImpl")
	TopicManagementIFace topicManagementImpl;

	@Override
	public boolean createAffiliation(@WebParam(name = "topic") String topic,
			@WebParam(name = "userId") String userId,
			@WebParam(name = "role") AffiliationRole role)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, NoSuchItemException, InvalidDataException {
		return topicManagementImpl.createAffiliation(topic, userId, role);
	}

	@Override
	public List<Affiliation> getAffiliationsForTopic(
			@WebParam(name = "topic") String topicId)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException {
		return topicManagementImpl.getAffiliationsForTopic(topicId);
	}

	@Override
	public List<AffiliationMapping> getAffiliationsForUser(
			@WebParam(name = "userId") String userId)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException {
		return topicManagementImpl.getAffiliationsForUser(userId);
	}

	@Override
	public boolean updateAffiliation(@WebParam(name = "topic") String topic,
			@WebParam(name = "userId") String userId,
			@WebParam(name = "role") AffiliationRole role)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, ConflictException {
		return topicManagementImpl.updateAffiliation(topic, userId, role);
	}

	@Override
	public boolean deleteAffiliation(@WebParam(name = "topic") String topic,
			@WebParam(name = "userId") String userId,
			@WebParam(name = "role") AffiliationRole role)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException {
		return topicManagementImpl.deleteAffiliation(topic, userId, role);
	}

	@Override
	public List<AccessRequest> getPendingAccessRequests(
			@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, FeatureNotAvailableException {
		return topicManagementImpl.getPendingAccessRequests(topic);
	}

	@Override
	public boolean grantAccessRequest(
			@WebParam(name = "request") AccessRequest request)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, FeatureNotAvailableException {
		return topicManagementImpl.grantAccessRequest(request);
	}

	@Override
	public boolean rejectAccessRequest(
			@WebParam(name = "request") AccessRequest request)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, FeatureNotAvailableException {
		return topicManagementImpl.rejectAccessRequest(request);
	}

	@Override
	public boolean processPendingAccessRequests(
			@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException, FeatureNotAvailableException {
		return topicManagementImpl.processPendingAccessRequests(topic);
	}

}
