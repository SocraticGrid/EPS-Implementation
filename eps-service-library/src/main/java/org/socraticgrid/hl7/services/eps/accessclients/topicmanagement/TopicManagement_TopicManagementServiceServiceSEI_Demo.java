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
package org.socraticgrid.hl7.services.eps.accessclients.topicmanagement;


import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;

import org.socraticgrid.hl7.services.eps.interfaces.TopicManagementIFace;
import org.socraticgrid.hl7.services.eps.model.AffiliationRole;

//TODO  Add more demos....

public final class TopicManagement_TopicManagementServiceServiceSEI_Demo {

	private static final QName SERVICE_NAME = new QName(
			"org.socraticgrid.hl7.services.eps",
			"TopicManagementServiceService");

	private TopicManagement_TopicManagementServiceServiceSEI_Demo() {
	}

	public static void main(String args[]) throws java.lang.Exception {
		URL wsdlURL = TopicManagementServiceSE.WSDL_LOCATION;
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

		String endpoint = "http://172.31.5.68:8080/EPSService/topicmanagement";
		
		TopicManagementServiceSE ss = new TopicManagementServiceSE(wsdlURL,
				SERVICE_NAME);
		
		TopicManagementIFace port = ss.getTopicManagementPort();
		((BindingProvider)port).getRequestContext().put(
        	    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
        	    endpoint);

		//Example createAffiliation Message
		{
		
			System.out.println("Invoking createAffiliation...");
			String topic = "test Topic";
			String userId="TestUser";
			AffiliationRole role =AffiliationRole.Owner;
			
			boolean result  = port.createAffiliation(topic, userId, role);
			System.out.println("createAffiliation.result=" + Boolean.toString(result));

		}

		
		System.exit(0);
	}

}
