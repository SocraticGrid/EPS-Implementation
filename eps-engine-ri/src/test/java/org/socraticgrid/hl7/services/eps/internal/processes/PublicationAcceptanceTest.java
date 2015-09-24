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
package org.socraticgrid.hl7.services.eps.internal.processes;

/*import org.junit.Test;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.internal.model.TransientTopic;
import org.socraticgrid.hl7.services.eps.model.ContractType;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.Subscription;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.Topic;
import org.socraticgrid.hl7.services.eps.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;*/
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Test-TransientTopicEnv.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class PublicationAcceptanceTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired

	@InjectMocks
	private PublicationAcceptance paTask;;

	//@Mock(name = "dlvMgr")
	///DeliveryManager deliveryMgr;
	//@Mock(name = "msgMgr")
	//MessageManager mockMsgMgr;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}



	@Test
	public void testPublishEvent(){
		String topic = "/Test Topic";
		Message event = new Message();
		event.getTopics().add(topic);

		
		try {
			String result = paTask.publishEvent(topic, event);
			
			assertNotNull("Publication returned as Null",result);	
			
			
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testPublishEvent_FixRootName(){
		String topic = "Test Topic";
		Message event = new Message();
		event.getTopics().add(topic);

		
		try {
			String result = paTask.publishEvent(topic, event);
			
			assertNotNull("Publication returned as Null",result);	
			
			
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	}

	@Test
	public void testPublishEvent_IncompleteTopic() {
		String topic = "/Test Topic";
		Message event = new Message();

		
		try {
			paTask.publishEvent(topic, event);
			
			fail("Incomplete Data Exception expected");
			
			
		}
		catch(IncompleteDataException exp)
		{
			//We Except this exception
			assertTrue("Exception did not have the matching topic name",exp.getMissingElementName().compareTo(topic)==0);
		
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	}
	
	
	@Test
	public void testPublishEvent_MissingTopic() {
		String topic = "/Test Topic";
		Message event = new Message();
		event.getTopics().add(topic);
		event.getTopics().add("/Missing Topic");

		
		try {
			paTask.publishEvent(topic, event);
			
			fail("Incomplete Data Exception expected");
			
			
		}
		catch(NoSuchTopicException exp)
		{
			//We Except this exception
			
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| InvalidDataException e) {
			fail(e.toString());
		}
	}
}*/
