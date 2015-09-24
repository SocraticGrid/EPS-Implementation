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

/*import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.apache.cxf.helpers.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.socraticgrid.hl7.services.eps.interfaces.PSPublicationInterventionIFace;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;
import org.socraticgrid.hl7.services.eps.model.MessageHeader;
import org.socraticgrid.hl7.services.eps.model.ReviewAction;
import org.socraticgrid.hl7.services.eps.model.ReviewResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;*/
/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:beans/LabFHIRIntervention.xml" })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PublicationInterventionIT {

	@Autowired
	private ApplicationContext applicationContext;
	
	
	
	@Test
	public void testPublishIntervention(){
		
		PSPublicationInterventionIFace intervention = (PSPublicationInterventionIFace)applicationContext.getBean("FHIRLabReview");
		String topic = "/Test Topic";
		Message event = new Message();
		event.getTopics().add(topic);
		MessageHeader hdr = event.getHeader();
		hdr.setTopicId(topic);
		hdr.setPriority(0);
		hdr.setSubject("Test Event");
	
		MessageBody body = new MessageBody();
		Resource rsc = applicationContext.getResource("vistalab.xml");
		try {
			body.setBody(IOUtils.toString(rsc.getInputStream(), "UTF-8"));
		} catch (IOException e) {
			fail(e.toString());
		}
		body.setType("application/hl7-v2+xml");
		event.getMessageBodies().add(body);
		ReviewResult result = intervention.reviewMessage(event);
		ReviewAction action = result.getAction();
		assertTrue("Should not be rejected",action != ReviewAction.Rejected);
		assertTrue("Should be altered",result.isAltered());
		assertTrue("Should have an additional Body",event.getMessageBodies().size()==2);
		MessageBody newBdy = event.getMessageBodies().get(1);
		assertTrue("The new boudyu should the xml+fhir",newBdy.getType().compareTo("application/xml+fhir")==0);
	}
	
	
}*/
