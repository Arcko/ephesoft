/********************************************************************************* 
* Ephesoft is a Intelligent Document Capture and Mailroom Automation program 
* developed by Ephesoft, Inc. Copyright (C) 2015 Ephesoft Inc. 
* 
* This program is free software; you can redistribute it and/or modify it under 
* the terms of the GNU Affero General Public License version 3 as published by the 
* Free Software Foundation with the addition of the following permission added 
* to Section 15 as permitted in Section 7(a): FOR ANY PART OF THE COVERED WORK 
* IN WHICH THE COPYRIGHT IS OWNED BY EPHESOFT, EPHESOFT DISCLAIMS THE WARRANTY 
* OF NON INFRINGEMENT OF THIRD PARTY RIGHTS. 
* 
* This program is distributed in the hope that it will be useful, but WITHOUT 
* ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS 
* FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more 
* details. 
* 
* You should have received a copy of the GNU Affero General Public License along with 
* this program; if not, see http://www.gnu.org/licenses or write to the Free 
* Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 
* 02110-1301 USA. 
* 
* You can contact Ephesoft, Inc. headquarters at 111 Academy Way, 
* Irvine, CA 92617, USA. or at email address info@ephesoft.com. 
* 
* The interactive user interfaces in modified source and object code versions 
* of this program must display Appropriate Legal Notices, as required under 
* Section 5 of the GNU Affero General Public License version 3. 
* 
* In accordance with Section 7(b) of the GNU Affero General Public License version 3, 
* these Appropriate Legal Notices must retain the display of the "Ephesoft" logo. 
* If the display of the logo is not reasonably feasible for 
* technical reasons, the Appropriate Legal Notices must display the words 
* "Powered by Ephesoft". 
********************************************************************************/ 

package com.ephesoft.dcma.da.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.ephesoft.dcma.core.EphesoftProperty;
import com.ephesoft.dcma.core.common.BatchInstanceStatus;
import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;
import com.ephesoft.dcma.da.id.BatchInstanceID;
import com.ephesoft.dcma.util.constant.PrivateKeyEncryptionAlgorithm;

/**
 * 
 * @author Ephesoft
 * @version 1.0
 */

@Entity
@Table(name = "batch_instance")
public class BatchInstance extends AbstractChangeableEntity {

	private static final long serialVersionUID = 4777280763161216105L;

	private transient BatchInstanceID batchInstanceID;

	@ManyToOne
	@JoinColumn(name = "batch_class_id")
	private BatchClass batchClass;

	@Column(name = "review_operator_user_name")
	private String reviewUserName;

	@Column(name = "validation_operator_user_name")
	private String validationUserName;

	@Column(name = "batch_priority")
	private int priority;

	@Column(name = "identifier")
	private String identifier;

	@Enumerated(EnumType.STRING)
	@Column(name = "batch_status")
	private BatchInstanceStatus status;

	@Column(name = "unc_subfolder")
	private String uncSubfolder;

	@Column(name = "local_folder")
	private String localFolder;

	@Column(name = "curr_user")
	private String currentUser;

	@Column(name = "batch_name")
	private String batchName;
	
	@Column(name = "batch_description")
	private String batchDescription;

	@Column(name = "is_remote", columnDefinition = "bit default 0")
	private boolean isRemote;

	@Column(name = "executed_modules")
	private String executedModules;

	@Column(name = "server_ip")
	private String serverIP;

	@Column(name = "executing_server")
	private String executingServer;

	@ManyToOne
	@JoinColumn(name = "lock_owner")
	private ServerRegistry lockOwner;

	@OneToOne
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	@JoinColumn(name = "remote_batch_instance_id")
	private RemoteBatchInstance remoteBatchInstance;

	/**
	 * Encryption algorithm associated with the batch instance. If null then batch will be Un-encrypted.
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "encryption_algorithm")
	private PrivateKeyEncryptionAlgorithm encryptionAlgorithm;

	/**
	 * {@link String} User Defined Data Column
	 */
	@Column(name = "custom_column1")
	private String customColumn1;

	/**
	 * {@link String} User Defined Data Column
	 */
	@Column(name = "custom_column2")
	private String customColumn2;
	
	/**
	 * {@link String} User Defined Data Column
	 */
	@Column(name = "custom_column3")
	private String customColumn3;
	
	/**
	 * {@link String} User Defined Data Column
	 */
	@Column(name = "custom_column4")
	private String customColumn4;

	public int getPriority() {
		return priority;
	}

	public void postPersist() {
		super.postPersist();
		this.identifier = EphesoftProperty.BATCH_INSTANCE.getProperty() + Long.toHexString(this.getId()).toUpperCase();
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public BatchClass getBatchClass() {
		return batchClass;
	}

	public void setBatchClass(BatchClass batchClass) {
		this.batchClass = batchClass;
	}

	public String getReviewUserName() {
		return reviewUserName;
	}

	public void setReviewUserName(String reviewUserName) {
		this.reviewUserName = reviewUserName;
	}

	public String getValidationUserName() {
		return validationUserName;
	}

	public void setValidationUserName(String validationUserName) {
		this.validationUserName = validationUserName;
	}

	public BatchInstanceStatus getStatus() {
		return status;
	}

	public void setStatus(BatchInstanceStatus status) {
		this.status = status;
	}

	public String getUncSubfolder() {
		return uncSubfolder;
	}

	public void setUncSubfolder(String uncSubfolder) {
		this.uncSubfolder = uncSubfolder;
	}

	public String getLocalFolder() {
		return localFolder;
	}

	public void setLocalFolder(String localFolder) {
		this.localFolder = localFolder;
	}

	public String getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(String currentUser) {
		this.currentUser = currentUser;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Transient
	public String getProcessInstanceKey() {
		return this.getBatchClass().getName() + '.' + this.getIdentifier();
	}

	public ServerRegistry getLockOwner() {
		return lockOwner;
	}

	public void setLockOwner(ServerRegistry lockOwner) {
		this.lockOwner = lockOwner;
	}

	@Transient
	public BatchInstanceID getBatchInstanceID() {
		if (getId() != 0 && batchInstanceID == null) {
			batchInstanceID = new BatchInstanceID(identifier);
		}
		return batchInstanceID;
	}

	public boolean isRemote() {
		return isRemote;
	}

	public void setRemote(boolean isRemote) {
		this.isRemote = isRemote;
	}

	public RemoteBatchInstance getRemoteBatchInstance() {
		return remoteBatchInstance;
	}

	public void setRemoteBatchInstance(RemoteBatchInstance remoteBatchInstance) {
		this.remoteBatchInstance = remoteBatchInstance;
	}

	public String getExecutedModules() {
		return executedModules;
	}

	public void setExecutedModules(String executedModules) {
		this.executedModules = executedModules;
	}

	public String getServerIP() {
		return serverIP;
	}

	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;
	}

	public String getExecutingServer() {
		return executingServer;
	}

	public void setExecutingServer(String executingServer) {
		this.executingServer = executingServer;
	}

	/**
	 * Gets the algorithm with which the batch is encrypted. If null then batch instance should be un-encrypted.
	 * 
	 * @return {@link PrivateKeyEncryptionAlgorithm} encryptionAlgorithm associated with the batch instance.
	 */
	public PrivateKeyEncryptionAlgorithm getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	/**
	 * Set the encryption algorithm for the batch instance.
	 * 
	 * @param encryptionAlgorithm {@link PrivateKeyEncryptionAlgorithm} The encryptionAlgorithm to set.
	 */
	public void setEncryptionAlgorithm(PrivateKeyEncryptionAlgorithm encryptionAlgorithm) {
		this.encryptionAlgorithm = encryptionAlgorithm;
	}
	
	/**
	 * Gets the Batch Description of a batch instance.
	 *  
	 * @return batchDescription {@link String}.
	 */
	public String getBatchDescription() {
		return batchDescription;
	}

	/**
	 * Sets the Batch Description of a batch instance.
	 * 
	 * @param batchDescription {@link String}
	 */
	public void setBatchDescription(String batchDescription) {
		this.batchDescription = batchDescription;
	}

	
	/**
	 * @return the customColumn1
	 */
	public String getCustomColumn1() {
		return customColumn1;
	}

	
	/**
	 * @param customColumn1 the customColumn1 to set
	 */
	public void setCustomColumn1(String customColumn1) {
		this.customColumn1 = customColumn1;
	}

	
	/**
	 * @return the customColumn2
	 */
	public String getCustomColumn2() {
		return customColumn2;
	}

	
	/**
	 * @param customColumn2 the customColumn2 to set
	 */
	public void setCustomColumn2(String customColumn2) {
		this.customColumn2 = customColumn2;
	}

	
	/**
	 * @return the customColumn3
	 */
	public String getCustomColumn3() {
		return customColumn3;
	}

	
	/**
	 * @param customColumn3 the customColumn3 to set
	 */
	public void setCustomColumn3(String customColumn3) {
		this.customColumn3 = customColumn3;
	}

	
	/**
	 * @return the customColumn4
	 */
	public String getCustomColumn4() {
		return customColumn4;
	}

	
	/**
	 * @param customColumn4 the customColumn4 to set
	 */
	public void setCustomColumn4(String customColumn4) {
		this.customColumn4 = customColumn4;
	}

}
