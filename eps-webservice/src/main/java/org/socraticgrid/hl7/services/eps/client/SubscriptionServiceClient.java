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
package org.socraticgrid.hl7.services.eps.client;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;

import org.socraticgrid.hl7.services.eps.exceptions.AuthenicationRequiredException;
import org.socraticgrid.hl7.services.eps.exceptions.ExpiredException;
import org.socraticgrid.hl7.services.eps.exceptions.FeatureNotAvailableException;
import org.socraticgrid.hl7.services.eps.exceptions.IncompleteDataException;
import org.socraticgrid.hl7.services.eps.exceptions.InvalidDataException;
import org.socraticgrid.hl7.services.eps.exceptions.MediaFormatNotExceptedException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchItemException;
import org.socraticgrid.hl7.services.eps.exceptions.NoSuchTopicException;
import org.socraticgrid.hl7.services.eps.exceptions.NotAuthorizedException;
import org.socraticgrid.hl7.services.eps.interfaces.SubscriptionIFace;
import org.socraticgrid.hl7.services.eps.model.AccessModel;
import org.socraticgrid.hl7.services.eps.model.Durability;
import org.socraticgrid.hl7.services.eps.model.Options;
import org.socraticgrid.hl7.services.eps.model.SubscriptionType;

public class SubscriptionServiceClient {
	public static void main(String[] args) throws Exception {

		URL url = new URL(
				"file:D:/eps-webservice/wsdl/subscription-service.wsdl");
		QName qname = new QName("org.socraticgrid.hl7.services.eps",
				"SubscriptionServiceService");

		Service service = Service.create(url, qname);

		SubscriptionIFace subs = service.getPort(SubscriptionIFace.class);
		 //add username and password for container authentication
        BindingProvider bp = (BindingProvider) subs;
        bp.getRequestContext().put(BindingProvider.USERNAME_PROPERTY, "testuser");
        bp.getRequestContext().put(BindingProvider.PASSWORD_PROPERTY, "test123");
		SubscriptionServiceClient client = new SubscriptionServiceClient();
		//client.sendSubscribe(subs);
		client.sendRetrieveEvents(subs);

	}

	private void sendSubscribe(SubscriptionIFace service)
			throws MediaFormatNotExceptedException, IncompleteDataException,
			NotAuthorizedException, AuthenicationRequiredException,
			ExpiredException, FeatureNotAvailableException,
			InvalidDataException, NoSuchTopicException {
		List<String> topics = new ArrayList<String>();
		topics.add("KafkaTopic");
		Options options = new Options();
		options.setDurability(Durability.Robust);
		options.setAccess(AccessModel.Open);
		System.out.println(service.subscribe(topics, SubscriptionType.Pull,
				options, "testCallBack"));
	}

	private void sendRetrieveEvents(SubscriptionIFace service)
			throws MediaFormatNotExceptedException, NotAuthorizedException,
			AuthenicationRequiredException, NoSuchTopicException,
			ExpiredException, FeatureNotAvailableException, NoSuchItemException {
		System.out.println(service.retrieveEvent("KafkaTopic", null));
	}
}
