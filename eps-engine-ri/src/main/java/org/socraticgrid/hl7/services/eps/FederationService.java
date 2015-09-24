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

import javax.jws.WebParam;
import javax.jws.WebService;

import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.FederationIFace;
import org.socraticgrid.hl7.services.eps.model.FederationMap;
import org.socraticgrid.hl7.services.eps.model.FederationSource;
import org.socraticgrid.hl7.services.eps.model.FederationTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "federation", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class FederationService implements FederationIFace {

	@Autowired
	@Qualifier("FederationServiceImpl")
	FederationIFace federationImpl;

	@Override
	public FederationMap GetMasterFederationMap()
			throws NotAuthorizedException, FeatureNotAvailableException {
		return federationImpl.GetMasterFederationMap();
	}

	@Override
	public boolean UpdateSource(@WebParam(name = "sourceId") String sourceId,
			@WebParam(name = "sourceInfo") FederationSource sourceInfo)

	throws NotAuthorizedException, FeatureNotAvailableException,
			NoSuchItemException {
		return federationImpl.UpdateSource(sourceId, sourceInfo);
	}

	@Override
	public FederationMap GetSourceMap(
			@WebParam(name = "sourceId") String sourceId)
			throws NotAuthorizedException, FeatureNotAvailableException,
			NoSuchItemException {
		return federationImpl.GetSourceMap(sourceId);
	}

	@Override
	public boolean UpdateSourceMap(
			@WebParam(name = "sourceId") String sourceId,
			@WebParam(name = "sourceMap") FederationMap sourceMap)
			throws NotAuthorizedException, FeatureNotAvailableException,
			NoSuchItemException, NoSuchTopicException {
		return federationImpl.UpdateSourceMap(sourceId, sourceMap);
	}

	@Override
	public boolean UpdateTarget(@WebParam(name = "targetId") String targetId,
			@WebParam(name = "targetInfo") FederationTarget targetInfo)
			throws NotAuthorizedException, FeatureNotAvailableException,
			NoSuchItemException {
		return federationImpl.UpdateTarget(targetId, targetInfo);
	}

	@Override
	public FederationMap GetTargetMap(
			@WebParam(name = "targetId") String targetId)
			throws NotAuthorizedException, FeatureNotAvailableException,
			NoSuchItemException {
		return federationImpl.GetTargetMap(targetId);
	}

	@Override
	public boolean UpdateTargetMap(
			@WebParam(name = "targetId") String targetId,
			@WebParam(name = "targetMap") FederationMap targetMap)
			throws NotAuthorizedException, FeatureNotAvailableException,
			NoSuchItemException, NoSuchTopicException {
		return federationImpl.UpdateTargetMap(targetId, targetMap);
	}

}
