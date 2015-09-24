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
package org.socraticgrid.hl7.services.eps.accessclients.publication;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.socraticgrid.hl7.services.eps.interfaces.PublicationIFace;
import org.socraticgrid.hl7.services.eps.model.ManagementEvent;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;
import org.socraticgrid.hl7.services.eps.model.PresenceState;


public final class Publication_PublicationServiceSEI_Demo {

	private static final QName SERVICE_NAME = new QName(
			"org.socraticgrid.hl7.services.eps",
			"PublicationServiceService");

	private Publication_PublicationServiceSEI_Demo() {
	}

	public static void main(String args[]) throws java.lang.Exception {
		URL wsdlURL = PublicationServiceSE.WSDL_LOCATION;
		if (args.length > 0 && args[0] != null && !"".equals(args[0])) {
			File wsdlFile = new File(args[0]);
			try {
				if (wsdlFile.exists()) {
					wsdlURL = wsdlFile.toURI().toURL();
				} else {
					wsdlURL = new URL(args[0]);
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		String endpoint = "http://172.31.5.68:8080/EPS/publication";
		
		PublicationServiceSE ss = new PublicationServiceSE(wsdlURL,
				SERVICE_NAME);
		
		PublicationIFace port = ss.getPublicationPort();
		((BindingProvider)port).getRequestContext().put(
        	    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        	    endpoint);
		String msgId;
		//Example Publish Event Message
		{
		
			System.out.println("Invoking publishEvent...");
			String topic = "test Topic";
			Message event = new Message();
			event.getHeader().setMessageId(Long.toString(System.currentTimeMillis()));
			event.getTopics().add(topic);
			MessageBody body = new MessageBody();
			body.setType("txt/test");
			body.setBody("A Test Body");
			event.getMessageBodies().add(body);
			msgId = port.publishEvent(topic, event);
			
			System.out.println("publishEvent.result=" + msgId);

		}
		//Example deleteEvent
		{
		
			System.out.println("Invoking deleteEvent...");
			String topic = "test Topic";
			ManagementEvent event = new ManagementEvent();
			event.setTitle("Title");
			event.setEventType(666);
			event.getTopics().add(topic);
			port.deleteEvent(topic, msgId);
			
			System.out.println("deleteEvent called");

		}
		//Example deleteEvent
		{
		
			System.out.println("Invoking deleteEvent...");
			String publisherId = "TestPublisher";
			PresenceState presence = PresenceState.Online;
			port.assertPublisherPresence(publisherId, presence);
			
			System.out.println("assertPublisherPresence called");

		}
		
		System.exit(0);
	}

}
