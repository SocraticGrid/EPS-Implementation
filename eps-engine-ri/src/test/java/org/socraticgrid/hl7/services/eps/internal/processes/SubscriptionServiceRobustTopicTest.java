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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.socraticgrid.hl7.services.eps.model.AccessModel;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.User;
import org.socraticgrid.hl7.services.eps.service.impl.SubscriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Test-SubscriptionRobustTopic.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SubscriptionServiceRobustTopicTest {
	@Autowired
	@InjectMocks
	private SubscriptionServiceImpl subscriptionService;

	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Ignore
	@Test
	public void A_test_subscribe() throws Exception {
		List<String> topics = new ArrayList<String>();
		topics.add("KafkaTopic");
		Options options = new Options();
		options.setDurability(Durability.Robust);
		options.setAccess(AccessModel.Open);
		User user = new User();
		user.setName("Test User");
		user.setUserId("2");
		try {
			String subId = subscriptionService.subscribe(user, topics, SubscriptionType.Pull,
					options, "testCallBack");
			assertNotNull(subId);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Ignore
	@Test
	public void B_test_retrieveEvents_Recent() throws Exception {
		Options options = new Options();
		options.setDurability(Durability.Robust);
		String topic = "KafkaTopic";
		User user = new User();
		user.setName("Test User");
		user.setUserId("2");
		try {
			List<Message> messages = subscriptionService.retrieveEvents(user,
					topic, PullRange.Recent, null, null, null);
			System.out.println("Fetched Messages : ");
			for (Message message : messages) {
				System.out.println("Message Publish Date : "
						+ message.getHeader().getMessagePublicationTime());
				System.out.println("Message Body : "
						+ message.getMessageBodies());
				System.out
						.println("============================================");
			}
			assertNotNull(messages);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Ignore
	@Test
	public void C_test_retrieveEvents_SpecificAll() throws Exception {
		Options options = new Options();
		options.setDurability(Durability.Robust);
		String topic = "KafkaTopic";
		User user = new User();
		user.setName("Test User");
		user.setUserId("2");
		try {
			A_test_subscribe();
			List<Message> messages = subscriptionService.retrieveEvents(user,
					topic, PullRange.Specific, null, null, null);
			System.out.println("Fetched Messages : ");
			for (Message message : messages) {
				System.out.println("Message Publish Date : "
						+ message.getHeader().getMessagePublicationTime());
				System.out.println("Message Body : "
						+ message.getMessageBodies());
				System.out
						.println("============================================");
			}
			assertNotNull(messages);
		} catch (Exception e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}

	@Ignore
	@Test
	public void D_test_retrieveEvents_SpecificDate() throws Exception {
		Options options = new Options();
		options.setDurability(Durability.Robust);
		String topic = "KafkaTopic";
		User user = new User();
		user.setName("Test User");
		user.setUserId("2");
		try {
			A_test_subscribe();
			List<Message> messages = subscriptionService.retrieveEvents(user,
					topic, PullRange.Specific, new Date(), null, null);
			System.out.println("Fetched Messages : ");
			for (Message message : messages) {
				System.out.println("Message Publish Date : "
						+ message.getHeader().getMessagePublicationTime());
				System.out.println("Message Body : "
						+ message.getMessageBodies());
				System.out
						.println("============================================");
			}
			assertNotNull(messages);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

	@Ignore
	@Test
	public void E_test_retrieveEvents_Polling() throws Exception {
		Options options = new Options();
		options.setDurability(Durability.Robust);
		String topic = "KafkaTopic";
		User user = new User();
		user.setName("Test User");
		user.setUserId("2");
		try {
			A_test_subscribe();
			List<Message> messages = subscriptionService.retrieveEvents(user,
					topic, null);
			System.out.println("Fetched Messages : ");
			for (Message message : messages) {
				System.out.println("Message Publish Date : "
						+ message.getHeader().getMessagePublicationTime());
				System.out.println("Message Body : "
						+ message.getMessageBodies());
				System.out
						.println("============================================");
			}
			assertNotNull(messages);
		} catch (Exception e) {
			fail(e.toString());
		}
	}

}
