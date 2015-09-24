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

import java.util.LinkedList;
import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.exceptions.PubSubException;
import org.socraticgrid.hl7.services.eps.interfaces.BrokerManagementIFace;
import org.socraticgrid.hl7.services.eps.model.BrokerAccessRequest;
import org.socraticgrid.hl7.services.eps.model.CreationResult;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.PublicationContract;
import org.socraticgrid.hl7.services.eps.model.RemoveContentScope;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.Topic;
import org.socraticgrid.hl7.services.eps.model.TopicMetadata;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@WebService(name = "brokermanagement", targetNamespace = "org.socraticgrid.hl7.services.eps")
public class BrokerManagementService implements BrokerManagementIFace
{

	@Autowired
	@Qualifier("BrokerManagementServiceImpl")
	BrokerManagementIFace brokerManagementImpl;
	private final Logger logger = LoggerFactory
			.getLogger(BrokerManagementService.class);

	public BrokerManagementService()
	{

	}

	@Override
	public CreationResult createTopic(
			@WebParam(name = "parentTopic") String parentTopic,
			@WebParam(name = "topicName") String topicName,
			@WebParam(name = "topicOptions") Topic topicOptions)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, NoSuchTopicException,
			MediaFormatNotExceptedException, ExpiredException,
			FeatureNotAvailableException, InvalidDataException,
			IncompleteDataException
	{

		CreationResult out;
		try
		{
			logger.debug("Calling createTopic");
			out = brokerManagementImpl.createTopic(parentTopic, topicName,
					topicOptions);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception createTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception createTopic", exp);
			out = CreationResult.Failure;
		}

		return out;
	}

	@Override
	public boolean deleteTopic(@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, NoSuchTopicException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling deleteTopic");
			out = brokerManagementImpl.deleteTopic(topic);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception deleteTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception deleteTopic", exp);
			out = false;
		}

		return out;
	}

	@Override
	public boolean configureTopic(@WebParam(name = "topic") String topic,
			@WebParam(name = "topicOptions") Topic topicOptions)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, NoSuchTopicException,
			MediaFormatNotExceptedException, ExpiredException,
			FeatureNotAvailableException, InvalidDataException,
			IncompleteDataException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling configureTopic");
			out = brokerManagementImpl.configureTopic(topic, topicOptions);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception configureTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception configureTopic", exp);
			out = false;
		}
		return out;
	}

	@Override
	public TopicMetadata getTopicMetadata(@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException
	{
		TopicMetadata out;
		try
		{
			logger.debug("Calling getTopicMetadata");
			out = brokerManagementImpl.getTopicMetadata(topic);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception getTopicMetadata", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception getTopicMetadata", exp);
			out = null;
		}
		return out;
	}

	@Override
	public List<PublicationContract> getPublishersForTopic(
			@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException
	{
		List<PublicationContract> out;
		try
		{
			logger.debug("Calling getPublishersForTopic");
			out = brokerManagementImpl.getPublishersForTopic(topic);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception getPublishersForTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception getPublishersForTopic", exp);
			out = new LinkedList<PublicationContract>();
		}
		return out;
	}

	@Override
	public List<Subscription> getSubscriptionsForTopic(
			@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, NoSuchTopicException
	{
		List<Subscription> out;
		try
		{
			logger.debug("Calling getSubscriptionsForTopic");
			out = brokerManagementImpl.getSubscriptionsForTopic(topic);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception getSubscriptionsForTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception getSubscriptionsForTopic", exp);
			out = new LinkedList<Subscription>();
		}
		return out;
	}

	@Override
	public boolean purgeAllTopicEvents(@WebParam(name = "topic") String topic)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchTopicException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling purgeAllTopicEvents");
			out = brokerManagementImpl.purgeAllTopicEvents(topic);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception purgeAllTopicEvents", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception purgeAllTopicEvents", exp);
			out = false;
		}
		return out;
	}

	@Override
	public boolean createUser(@WebParam(name = "userInfo") User userInfo)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, InvalidDataException, IncompleteDataException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling createUser");
			out = brokerManagementImpl.createUser(userInfo);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception createUser", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception createUser", exp);
			out = false;
		}
		return out;
	}

	@Override
	public boolean deleteUser(@WebParam(name = "userId") String userId,
			@WebParam(name = "query") RemoveContentScope scope)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling deleteUser");
			out = brokerManagementImpl.deleteUser(userId, scope);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception deleteUser", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception deleteUser", exp);
			out = false;
		}
		return out;
	}

	@Override
	public User getUser(@WebParam(name = "userId") String userId)
			throws NotAuthorizedException, AuthenicationRequiredException,
			InvalidDataException, NoSuchItemException
	{

		User out;
		try
		{
			logger.debug("Calling getUser");
			out = brokerManagementImpl.getUser(userId);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception getUser", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception getUser", exp);
			out = null;
		}
		return out;
	}

	@Override
	public List<User> discoverUser(@WebParam(name = "query") String query)
			throws NotAuthorizedException, AuthenicationRequiredException,
			InvalidDataException, NoSuchItemException
	{
		List<User> out;
		try
		{
			logger.debug("Calling discoverUser");
			out = brokerManagementImpl.discoverUser(query);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception discoverUser", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception discoverUser", exp);
			out = new LinkedList<User>();
		}
		return out;
	}

	// TODO  Update Update Contract
	@Override
	public void moveTopic(@WebParam(name = "sourceTopic") String sourceTopic,
			@WebParam(name = "destinationPath") String destinationPath)
			throws NotAuthorizedException, AuthenicationRequiredException,
			ConflictException, NoSuchTopicException
	{
		try
		{
			logger.debug("Calling moveTopic");
			brokerManagementImpl.moveTopic(sourceTopic, destinationPath);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception moveTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception moveTopic", exp);
			//TODO  Throw the correct high level Exception - TBD
		}

	}
	
	// TODO  Update Update Contract
	@Override
	public void updateUser(@WebParam(name = "userId") String userId,
			@WebParam(name = "options") Options options,
			@WebParam(name = "userData") User userData)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException, MediaFormatNotExceptedException,
			InvalidDataException, IncompleteDataException
	{
		try
		{
			logger.debug("Calling configureTopic");
			brokerManagementImpl.updateUser(userId, options, userData);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception configureTopic", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception configureTopic", exp);
			//TODO  Throw the correct high level Exception - TBD
		}

	}

	public List<BrokerAccessRequest> getBrokerAccessRequests()
			throws NotAuthorizedException, AuthenicationRequiredException,
			FeatureNotAvailableException
	{

		List<BrokerAccessRequest> out;
		try
		{
			logger.debug("Calling getBrokerAccessRequests");
			out = brokerManagementImpl.getBrokerAccessRequests();
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception getBrokerAccessRequests", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception getBrokerAccessRequests", exp);
			out = new LinkedList<BrokerAccessRequest>();
		}
		return out;
	}

	public boolean grantBrokerAccessRequest(
			@WebParam(name = "requests") List<BrokerAccessRequest> request)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException, InvalidDataException, IncompleteDataException,
			FeatureNotAvailableException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling grantBrokerAccessRequest");
			out = brokerManagementImpl.grantBrokerAccessRequest(request);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception grantBrokerAccessRequest", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception grantBrokerAccessRequest", exp);
			out = false;
		}
		return out;
	}

	public boolean rejectBrokerAccessRequest(
			@WebParam(name = "requests") List<BrokerAccessRequest> requests)
			throws NotAuthorizedException, AuthenicationRequiredException,
			NoSuchItemException, InvalidDataException, IncompleteDataException,
			FeatureNotAvailableException
	{
		boolean out = false;
		try
		{
			logger.debug("Calling rejectBrokerAccessRequest");
			out = brokerManagementImpl.rejectBrokerAccessRequest(requests);
		}
		catch (PubSubException exp)
		{
			logger.warn("Expected possible Exception rejectBrokerAccessRequest", exp);
			throw exp;
		}
		catch (Throwable exp)
		{
			logger.error("Unexcepted Exception rejectBrokerAccessRequest", exp);
			out = false;
		}
		return out;

	}

}
