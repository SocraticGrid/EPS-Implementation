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
package org.socraticgrid.hl7.services.eps.functional;

import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.socraticgrid.hl7.services.eps.exceptions.MessageSerializationException;
import org.socraticgrid.hl7.services.eps.internal.model.MessageMarshall;
import org.socraticgrid.hl7.services.eps.model.Message;
import org.socraticgrid.hl7.services.eps.model.MessageBody;

/**
 * Created by user on 8/4/14.
 */
public class HelloKafkaProducer {
	final static String TOPIC = "kafkatopic";

	public static void main(String[] argv) throws MessageSerializationException {
		Properties properties = new Properties();
		properties.put("metadata.broker.list", "localhost:9092");
		properties.put("serializer.class", "kafka.serializer.StringEncoder");
		ProducerConfig producerConfig = new ProducerConfig(properties);
		kafka.javaapi.producer.Producer<String, String> producer = new kafka.javaapi.producer.Producer<String, String>(
				producerConfig);
		Message event = new Message();
		event.getHeader().setMessageId(UUID.randomUUID().toString());
		event.getHeader().setMessagePublicationTime(new Date());
		event.getTopics().add(TOPIC);
		MessageBody body = new MessageBody();
		body.setType("text/plain");
		body.setBody("Message Body 3");
		event.getMessageBodies().add(body); 
		KeyedMessage<String, String> message = new KeyedMessage<String, String>(
				TOPIC, MessageMarshall.toString(event));
		producer.send(message);
		producer.close();
	}
}
