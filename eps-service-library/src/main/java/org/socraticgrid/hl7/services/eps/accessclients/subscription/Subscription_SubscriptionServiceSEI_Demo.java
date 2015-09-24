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
package org.socraticgrid.hl7.services.eps.accessclients.subscription;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.socraticgrid.hl7.services.eps.interfaces.SubscriptionIFace;
import org.socraticgrid.hl7.services.eps.model.AccessModel;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageSummary;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.PullRange;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;

public final class Subscription_SubscriptionServiceSEI_Demo {

	private static final QName SERVICE_NAME = new QName(
			"org.socraticgrid.hl7.services.eps", "SubscriptionServiceService");

	private Subscription_SubscriptionServiceSEI_Demo() {
	}

	public static void main(String args[]) throws java.lang.Exception {
		URL wsdlURL = SubscriptionServiceSE.WSDL_LOCATION;
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

		String endpoint = "http://172.31.5.68:8080/EPSService/subscriptionService";

		SubscriptionServiceSE ss = new SubscriptionServiceSE();

		SubscriptionIFace port = ss.getSubscriptionPort();
		((BindingProvider) port).getRequestContext().put(
				BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpoint);
		// Example subscribe Event Message
		{

			System.out.println("Invoking subscribe...");
			String topic = "test Topic";
			List<String> topics = new LinkedList<String>();
			topics.add(topic);
			SubscriptionType type = SubscriptionType.Pull;
			Options options = new Options();
			options.setAccess(AccessModel.Open);
			options.setDurability(Durability.Transient);
			String callbackAddress = null;

			String subscriptionId = port.subscribe(topics, type, options,
					callbackAddress);
			System.out.println("subscribe.result=" + subscriptionId);

		}
		// Example unsubscribe Event Message
		{

			System.out.println("Invoking unsubscribe...");
			String topic = "test Topic";
			List<String> topics = new LinkedList<String>();
			topics.add(topic);
			SubscriptionType type = SubscriptionType.Pull;
			String subscriberId = "subscriber";
			String subscriptionid = "12345678";

			boolean result = port.unsubscribe(topics, subscriberId,
					subscriptionid);

			System.out
					.println("unsubscribe.result=" + Boolean.toString(result));

		}
		// Example unsubscribeByOptions Event Message
		{

			System.out.println("Invoking unsubscribeByOptions...");
			String topic = "test Topic";
			List<String> topics = new LinkedList<String>();
			topics.add(topic);
			SubscriptionType type = SubscriptionType.Pull;
			String subscriberId = "subscriber";
			Options options = new Options();
			options.setAccess(AccessModel.Open);
			options.setDurability(Durability.Transient);

			boolean result = port.unsubscribeByOptions(topics, subscriberId, options);
			System.out
					.println("unsubscribeByOptions.result=" + Boolean.toString(result));

		}
		// Example retrieveEvents Event Message
		{

			System.out.println("Invoking retrieveEvents...");
			String topic = "test Topic";
	

			PullRange pullRange= PullRange.Specific;
			Date start = new Date(System.currentTimeMillis()-(24*3600*1000*4));
			Date end = new Date(System.currentTimeMillis());
			List<String> mediaForms = new LinkedList<String>();
			List<Message> result = port.retrieveEvents(topic, pullRange, start, end, mediaForms);
			System.out.println("retrieveEvents.result=" + result);

		}
		
		// Example retrieveEventSummaries Event Message
		{

			System.out.println("Invoking retrieveEventSummaries...");
			String topic = "test Topic";
	

			PullRange pullRange= PullRange.Specific;
			Date start = new Date(System.currentTimeMillis()-(24*3600*1000*4));
			Date end = new Date(System.currentTimeMillis());
			List<String> mediaForms = new LinkedList<String>();
			List<MessageSummary> result = port.retrieveEventSummaries(topic, pullRange, start, end, mediaForms);
			System.out
					.println("retrieveEventSummaries.result=" + result);

		}

		// Example retrieveEventSummaries Event Message
				{

					System.out.println("Invoking retrieveEventSummaries...");
					String topic = "test Topic";
			

					PullRange pullRange= PullRange.Specific;
					Date start = new Date(System.currentTimeMillis()-(24*3600*1000*4));
					Date end = new Date(System.currentTimeMillis());
					List<String> mediaForms = new LinkedList<String>();
					List<MessageSummary> result = port.retrieveEventSummaries(topic, pullRange, start, end, mediaForms);
					System.out
							.println("retrieveEventSummaries.result=" + result);

				}

		System.exit(0);
	}

}
