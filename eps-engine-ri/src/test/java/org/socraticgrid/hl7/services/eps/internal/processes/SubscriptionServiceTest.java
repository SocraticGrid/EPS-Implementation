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
import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;
import org.socraticgrid.hl7.services.eps.model.User;
import org.socraticgrid.hl7.services.eps.service.impl.SubscriptionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Test-TransientTopicEnv.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class SubscriptionServiceTest {
	@Autowired
	private ApplicationContext applicationContext;

	@Autowired 
	@InjectMocks
	private SubscriptionServiceImpl subscriptionService; 
	
	@Before
	public void setUp() {
		MockitoAnnotations.initMocks(this);
	} 
	
	
	@Test
	public void testPullSubscription()throws Exception{
		Options options = new Options();
		options.setDurability(Durability.Robust);
		List<String> topics = new ArrayList<String>();
		User user = new User();
		user.setName("Test User");
		user.setUserId(UUID.randomUUID().toString());
		topics.add("KafkaTopic");
		try {
			String subscriptionId = subscriptionService.subscribe(user, topics, SubscriptionType.Pull, options, null); 
			assertNotNull("Created Subscription ID",subscriptionId);
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	}
	
	@Test
	public void testPushSubscription()throws Exception{
		Options options = new Options();
		options.setDurability(Durability.Robust);
		List<String> topics = new ArrayList<String>();
		User user = new User();
		user.setName("Test User");
		user.setUserId(UUID.randomUUID().toString());
		topics.add("KafkaTopic");
		try {
			String subscriptionId = subscriptionService.subscribe(user, topics, SubscriptionType.Push, options, "http://testcallbackaddress.com"); 
			assertNotNull("Created Subscription ID",subscriptionId);
		}
		catch (  NotAuthorizedException | AuthenicationRequiredException
				| NoSuchTopicException | InvalidDataException e) {
			fail(e.toString());
		}
	}
}
