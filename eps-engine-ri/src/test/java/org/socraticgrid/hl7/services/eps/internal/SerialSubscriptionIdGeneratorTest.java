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
package org.socraticgrid.hl7.services.eps.internal;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:Test-SerialSubscriptionIdGenerator.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)

public class SerialSubscriptionIdGeneratorTest {

	@Autowired
	SerialSubscriptionIdGenerator generator;
	
	@Test
	public void testGenerateSubscriptionId() {
	
		String id = generator.generateSubscriptionId(null, null, null);
		assertNotNull("Generated a null Id", id);
		String id2 = generator.generateSubscriptionId(null, null, null);
		assertFalse("New Id matches old",id.compareToIgnoreCase(id2)==0);
	}

	
	@Test
	public void testGenerateMultipleUniqueSubscriptionId() {
	
		int limit=1000;
		Set<String> seen = new HashSet<String>();
		for (int i=0;i<limit;i++)
		{
			String id =generator.generateSubscriptionId(null, null, null);
			assertTrue("Non Unique Id seen",seen.add(id));
		}
	}

}
