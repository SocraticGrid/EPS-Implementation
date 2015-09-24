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

import java.io.ByteArrayInputStream;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.socraticgrid.hl7.services.eps.exceptions.MessageSerializationException;
import org.socraticgrid.hl7.services.eps.model.Message;
 

/**
 * @author pavan
 */
public class MessageMarshall {
	private static final Logger logger = LoggerFactory.getLogger(MessageMarshall.class);
	
	public static String toString(Message msg)throws MessageSerializationException{
		StringWriter result=null;
		if(msg==null){
			throw new MessageSerializationException("Message can not be null.");
		}
		try {
            logger.debug("Serializing Message {}", msg);
            JAXBContext context = JAXBContext.newInstance(Message.class);
            Marshaller m = context.createMarshaller(); 
            result = new StringWriter();
            m.marshal(msg, result);  
        } catch (Exception e) { 
        	throw new MessageSerializationException("Exception while marshalling Message object,"+e.getMessage());
        }
		return result.toString();
	}
	
	public static Message toMessage(String message) throws MessageSerializationException {
		try {
			if(message==null){
				throw new MessageSerializationException("Message can not be null.");
			}
            logger.debug("Deserializing Message");
            JAXBContext context = JAXBContext.newInstance(Message.class);
            Unmarshaller u = context.createUnmarshaller();
            
            return (Message)u.unmarshal(new ByteArrayInputStream(message.getBytes()));
        } catch (Exception e) {
        	throw new MessageSerializationException("Exception while unmarshalling Message object,"+e.getMessage());
        }
	}       
}
