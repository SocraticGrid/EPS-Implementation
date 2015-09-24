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
package org.socraticgrid.hl7.services.eps.internal.interfaces;

/**
 * @author speddibhotla
 *
 */
public interface KafkaProperties {

	final static String metadataBrokerList = "192.168.1.178:9092";
	final static String metadataZKLead = "192.168.1.178";
	final static int metadataZKPort = 9092;
	final static String serializerClass = "kafka.serializer.StringEncoder";
	final static String partitionerClass = "example.producer.SimplePartitioner";
	final static String requestRequiredAcks = "1";
	final static String zookeeperConnect = "192.168.1.178:2181";
	final static String groupId = "testgroup";
	final static String defaultGroupId = "/consumers/test-consumer-group";
	final static int zookeeperSessionTimeoutMs = 500;
	final static int zookeeperSyncTimeoutMs = 250;
	final static int autoCommitIntervalMs = 1000;
	final static String roottopic = "kafkatopic";
	final static int JMX_PORT = 9999;
	final static String JMX_URL = "service:jmx:rmi:///jndi/rmi://" + "localhost" + ":" + JMX_PORT + "/jmxrmi";
}
