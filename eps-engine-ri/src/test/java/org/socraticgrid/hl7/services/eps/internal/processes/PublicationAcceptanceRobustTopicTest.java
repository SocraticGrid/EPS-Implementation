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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Test-TransientTopicEnv.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class PublicationAcceptanceRobustTopicTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired 
	@InjectMocks
	private PublicationAcceptance paTask; 
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	} 
	
	@Ignore
	@Test
	public void testPublishEvent()throws Exception{
		String topic = "kafkatopic";
		Message event = new Message();
		event.getHeader().setMessageId(Long.toString(System.currentTimeMillis()));
		event.getHeader().setMessagePublicationTime(new Date());
		event.getTopics().add(topic);
		MessageBody body = new MessageBody();
		body.setType("text/plain");
		body.setBody("A Test Body");
		event.getMessageBodies().add(body); 
		
		try {
			String result = paTask.publishEvent("KafkaTopic", event); 
			assertNotNull("Publication returned as Null",result); 
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	} 
}
