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

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ConflictException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.model.Capabilities;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;
import org.socraticgrid.hl7.services.eps.model.MessageHeader;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.TopicSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ITTest-TransientTopicEnv.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class BrokerServiceImplIT {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	// @InjectMocks
	private BrokerServiceImpl brokerSrv;;

	@Test
	public void testDiscoverTopics() {
		try {
			List<TopicSummary> rootTopics;
			rootTopics = brokerSrv.discoverTopics("");
			assertNotNull(rootTopics);
			assertFalse(rootTopics.isEmpty());
			rootTopics = brokerSrv.discoverTopics("/");
			assertNotNull(rootTopics);
			assertFalse(rootTopics.isEmpty());

		} catch (NotAuthorizedException e) {
			e.printStackTrace();
			fail("NotAuthorizedException");
		} catch (AuthenicationRequiredException e) {
			e.printStackTrace();
			fail("AuthenicationRequiredException");
		} catch (ConflictException e) {
			e.printStackTrace();
			fail("ConflictException");
		} catch (ExpiredException e) {
			e.printStackTrace();
			fail("ExpiredException");
		} catch (NoSuchTopicException e) {
			e.printStackTrace();
			fail("NoSuchTopicException");
		}

	}

	@Test
	public void testMissingTopic() {
		List<TopicSummary> missingTopics = null;
		try {
			missingTopics = brokerSrv.discoverTopics("/NotExistentTopic");
			assertNotNull(
					"Topic list null, should never happen either an exception should have occured or an empty list",
					missingTopics);
			assertTrue("List of found topics is not empty",
					missingTopics.isEmpty());
		} catch (NotAuthorizedException | AuthenicationRequiredException
				| ConflictException | ExpiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail(e.getMessage());
		} catch (NoSuchTopicException e) {
			// We expect that we might reach here
		}
	}


	@Ignore
	@Test
	public void testGetSubTopics() {
		try {
			List<TopicSummary> subtopics = brokerSrv.getSubTopics("/Test Topic");
			assertNotNull("Null returned by getSubTopics",subtopics);
			assertFalse("No subtopics found",subtopics.isEmpty());
		} catch (NotAuthorizedException | AuthenicationRequiredException
				| ExpiredException | NoSuchTopicException e) {
			e.printStackTrace();
			fail(e.getMessage());
		}
	}


	@Ignore
	@Test
	public void testGetTopicOptions() {
		try {
			Options opts = brokerSrv.getTopicOptions("/Test Topic");
			assertNotNull("LabOrders Topic options are null", opts);
			assertTrue("LabOrder Topic is not Transient",
					opts.getDurability() == Durability.Transient);
		} catch (NotAuthorizedException | AuthenicationRequiredException
				| FeatureNotAvailableException | NoSuchTopicException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Test
	public void testDiscoverEventsForTopic() {
		// fail("Not yet implemented");
	}

	@Test
	public void testDiscoverCapabilities() {
		Capabilities cap;
		try {
			cap = brokerSrv.discoverCapabilities();
			assertNotNull("discoverCapabilities returned null", cap);
			assertTrue("Push Events is not listed as supported",
					cap.isPushEvents());
		} catch (NotAuthorizedException | AuthenicationRequiredException e) {
			fail(e.getMessage());
		}

	}

	@Test
	public void testRequestAccess() {
		// fail("Not yet implemented");
	}

}
