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
package org.socraticgrid.hl7.services.eps.internal.model;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.socraticgrid.hl7.services.eps.internal.StaticTopicTree;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicIFace;
import org.socraticgrid.hl7.services.eps.internal.interfaces.TopicLocatorIFace;
import org.socraticgrid.hl7.services.eps.internal.model.TransientTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Test-TransientTopicEnv.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class CommonTopicBaseTest {
	
	@Autowired
	private ApplicationContext applicationContext;


	@Autowired
	@Qualifier("TestTopicBean")
	@InjectMocks
	private TransientTopic testTopic;
	
	//@Mock(name = "dlvMgr")
	///DeliveryManager deliveryMgr;
	//@Mock(name = "msgMgr")
	//MessageManager mockMsgMgr;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}


	//@Ignore
	@Test
	public void testInitialize() {
		//fail("Not yet implemented");
		assertNotNull("Topic is null",testTopic);
	}

	@Ignore
	@Test
	public void testValidateExisingSubscriptions() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testAddStandardValidations() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testPublishEvent() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testDeleteEvent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTopic() {

		assertNotNull("Nested Model topic is Missing",testTopic.getTopic());
	}

	@Ignore
	@Test
	public void testSetTopic() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetPublicationInterventions() {
		assertNotNull("PublicationInterventions is null",testTopic.getPublicationInterventions());
	}

	@Test
	public void testGetPublicationValidationSteps() {
		assertNotNull("PublicationValidationSteps is null",testTopic.getPublicationValidationSteps());
	}

	@Ignore
	@Test
	public void testHoldEventForApproval() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testGetSubTopic() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testSetParentTopic() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testGetParentTopic() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testSetSubTopics() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testGetSubTopics() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetSubscriptions() {
		assertNotNull("Subscriptions List is null",testTopic.getSubscriptions());
	}
	@Ignore
	@Test
	public void testAddSubscription() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testSetPublicationInterventions() {
		fail("Not yet implemented");
	}

	@Ignore
	@Test
	public void testSetPublicationValidationSteps() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testSetSubscribions() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testValidateSubscription() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDeliveryInterventions() {
		assertNotNull("DeliveryInterventions List is Null",testTopic.getDeliveryInterventions());
	}
	
	@Ignore
	@Test
	public void testSetDeliveryInterventions() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testRemoveSubscription() {
		fail("Not yet implemented");
	}
	@Test
	public void testGetSubscriptionValdiationSteps() {
		assertNotNull("SubscriptionValationSteps List is Null",testTopic.getSubscriptionValdiationSteps());
	}
	@Ignore
	@Test
	public void testSetSubScriptionValdiationSteps() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTopicId() {
		assertNotNull("Topic Id is Null",testTopic.getTopicId());
	}

}
