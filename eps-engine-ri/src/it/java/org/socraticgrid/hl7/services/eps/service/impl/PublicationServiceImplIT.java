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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;
import org.socraticgrid.hl7.services.eps.model.MessageHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:ITTest-TransientTopicEnv.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class PublicationServiceImplIT {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired

	//@InjectMocks
	private PublicationServiceImpl pubSrv;;

	//@Mock(name = "dlvMgr")
	///DeliveryManager deliveryMgr;
	//@Mock(name = "msgMgr")
	//MessageManager mockMsgMgr;

	//@Before
	//public void setUp() {
	//	MockitoAnnotations.initMocks(this);
	//}

	//The following test needs the subscription endpoint to be updated to
	//deal with a test point  
	//It is not very clear how this should worl
	@Ignore("Test requires a push subscirption service")
	@Test
	public void testPublishEvent_PushChannel(){
		String topic = "/Test Topic";
		Message event = new Message();
		event.getTopics().add(topic);
		MessageHeader hdr = event.getHeader();
		hdr.setTopicId(topic);
		hdr.setPriority(0);
		hdr.setSubject("Test Event");
	
		MessageBody body = new MessageBody();
		body.setBody("The Event Body");
		body.setType("text");
		event.getMessageBodies().add(body);
		try {
			String result = pubSrv.publishEvent(topic, event);
			
			assertNotNull("Publication returned as Null",result);	
			
			
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	}
	


	@Ignore("Not ready yet")
	@Test
	public void testDeleteEvent() {
		fail("Not yet implemented");
	}

	@Ignore("Not Ready yet")
	@Test
	public void testAssertPublisherPresence() {
		fail("Not yet implemented");
	}

}
