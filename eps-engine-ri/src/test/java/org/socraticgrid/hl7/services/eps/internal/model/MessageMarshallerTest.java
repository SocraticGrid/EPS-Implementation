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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;

public class MessageMarshallerTest { 
	
	@Test
	public void testMarshal() throws Exception{
		String topic = "kafkatopic";
		Message event = new Message();
		event.getHeader().setMessageId(Long.toString(System.currentTimeMillis()));
		event.getTopics().add(topic);
		MessageBody body = new MessageBody();
		body.setType("txt/test");
		body.setBody("A Test Body");
		event.getMessageBodies().add(body); 
		String marshalMessage = MessageMarshall.toString(event);
		assertNotNull(marshalMessage);
		
	}

	@Test
	public void testUnMarshal() throws Exception{
		String topic = "kafkatopic";
		Message event = new Message();
		event.getHeader().setMessageId(Long.toString(System.currentTimeMillis()));
		event.getTopics().add(topic);
		MessageBody body = new MessageBody();
		body.setType("txt/test");
		body.setBody("A Test Body");
		event.getMessageBodies().add(body); 
		String marshalMessage = MessageMarshall.toString(event);
		assertNotNull(marshalMessage);
		Message newMessage = MessageMarshall.toMessage(marshalMessage);
		assertEquals(event.getHeader().getMessageId(), newMessage.getHeader().getMessageId());
	}

	 
	 
}
