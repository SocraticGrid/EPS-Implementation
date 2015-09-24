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

/**
 * @author pavan
 *
 */
public class KafkaBrokerBean {

	private String metadataBrokerList;
	private String metadataZKLead;
	private int metadataZKPort;
	private String serializerClass;
	private String partitionerClass;
	private String requestRequiredAcks;
	private String zookeeperConnect;
	private String groupId;
	private String defaultGroupId;
	private String zookeeperSessionTimeoutMs;
	private String zookeeperSyncTimeoutMs;
	private String autoCommitIntervalMs;
	private String jmxURL;
	
	/**
	 * @return the metadataBrokerList
	 */
	public String getMetadataBrokerList() {
		return metadataBrokerList;
	}
	/**
	 * @param metadataBrokerList the metadataBrokerList to set
	 */
	public void setMetadataBrokerList(String metadataBrokerList) {
		this.metadataBrokerList = metadataBrokerList;
	}
	/**
	 * @return the metadataZKLead
	 */
	public String getMetadataZKLead() {
		return metadataZKLead;
	}
	/**
	 * @param metadataZKLead the metadataZKLead to set
	 */
	public void setMetadataZKLead(String metadataZKLead) {
		this.metadataZKLead = metadataZKLead;
	}
	/**
	 * @return the metadataZKPort
	 */
	public int getMetadataZKPort() {
		return metadataZKPort;
	}
	/**
	 * @param metadataZKPort the metadataZKPort to set
	 */
	public void setMetadataZKPort(int metadataZKPort) {
		this.metadataZKPort = metadataZKPort;
	}
	/**
	 * @return the serializerClass
	 */
	public String getSerializerClass() {
		return serializerClass;
	}
	/**
	 * @param serializerClass the serializerClass to set
	 */
	public void setSerializerClass(String serializerClass) {
		this.serializerClass = serializerClass;
	}
	/**
	 * @return the partitionerClass
	 */
	public String getPartitionerClass() {
		return partitionerClass;
	}
	/**
	 * @param partitionerClass the partitionerClass to set
	 */
	public void setPartitionerClass(String partitionerClass) {
		this.partitionerClass = partitionerClass;
	}
	/**
	 * @return the requestRequiredAcks
	 */
	public String getRequestRequiredAcks() {
		return requestRequiredAcks;
	}
	/**
	 * @param requestRequiredAcks the requestRequiredAcks to set
	 */
	public void setRequestRequiredAcks(String requestRequiredAcks) {
		this.requestRequiredAcks = requestRequiredAcks;
	}
	/**
	 * @return the zookeeperConnect
	 */
	public String getZookeeperConnect() {
		return zookeeperConnect;
	}
	/**
	 * @param zookeeperConnect the zookeeperConnect to set
	 */
	public void setZookeeperConnect(String zookeeperConnect) {
		this.zookeeperConnect = zookeeperConnect;
	}
	/**
	 * @return the groupId
	 */
	public String getGroupId() {
		return groupId;
	}
	/**
	 * @param groupId the groupId to set
	 */
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	/**
	 * @return the defaultGroupId
	 */
	public String getDefaultGroupId() {
		return defaultGroupId;
	}
	/**
	 * @param defaultGroupId the defaultGroupId to set
	 */
	public void setDefaultGroupId(String defaultGroupId) {
		this.defaultGroupId = defaultGroupId;
	}
	/**
	 * @return the zookeeperSessionTimeoutMs
	 */
	public String getZookeeperSessionTimeoutMs() {
		return zookeeperSessionTimeoutMs;
	}
	/**
	 * @param zookeeperSessionTimeoutMs the zookeeperSessionTimeoutMs to set
	 */
	public void setZookeeperSessionTimeoutMs(String zookeeperSessionTimeoutMs) {
		this.zookeeperSessionTimeoutMs = zookeeperSessionTimeoutMs;
	}
	/**
	 * @return the zookeeperSyncTimeoutMs
	 */
	public String getZookeeperSyncTimeoutMs() {
		return zookeeperSyncTimeoutMs;
	}
	/**
	 * @param zookeeperSyncTimeoutMs the zookeeperSyncTimeoutMs to set
	 */
	public void setZookeeperSyncTimeoutMs(String zookeeperSyncTimeoutMs) {
		this.zookeeperSyncTimeoutMs = zookeeperSyncTimeoutMs;
	}
	/**
	 * @return the autoCommitIntervalMs
	 */
	public String getAutoCommitIntervalMs() {
		return autoCommitIntervalMs;
	}
	/**
	 * @param autoCommitIntervalMs the autoCommitIntervalMs to set
	 */
	public void setAutoCommitIntervalMs(String autoCommitIntervalMs) {
		this.autoCommitIntervalMs = autoCommitIntervalMs;
	}
	/**
	 * @return the jmxURL
	 */
	public String getJmxURL() {
		return jmxURL;
	}
	/**
	 * @param jmxURL the jmxURL to set
	 */
	public void setJmxURL(String jmxURL) {
		this.jmxURL = jmxURL;
	} 
}
