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

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.NaturalId;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

@Entity
@Table(name = "plugin")
@org.hibernate.annotations.Cache(usage = org.hibernate.annotations.CacheConcurrencyStrategy.READ_WRITE)
public class Plugin extends AbstractChangeableEntity implements Serializable {

	private static final long serialVersionUID = 5694761325202724778L;
	
	@Column(name = "plugin_name")
	@NaturalId
	private String pluginName;

	@Column(name = "plugin_desc")
	private String description;

	@Column(name = "plugin_version")
	private String version;

	@Column(name = "workflow_name")
	private String workflowName;

	@Column(name = "script_name")
	private String scriptName;
	
	@Column(name = "plugin_info")
	private String information;

	@Column(name = "is_deleted")
	private Boolean isDeleted;
	

	@OneToMany
	@JoinColumn(name = "plugin_id")
	@Cascade( {CascadeType.DELETE, CascadeType.SAVE_UPDATE, CascadeType.DELETE_ORPHAN, CascadeType.MERGE, CascadeType.EVICT})
	private List<Dependency> dependencies;

	public String getPluginName() {
		return pluginName;
	}

	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWorkflowName() {
		return workflowName;
	}

	public void setWorkflowName(String workflowName) {
		this.workflowName = workflowName;
	}

	/**
	 * @return the scriptName
	 */
	public String getScriptName() {
		return scriptName;
	}

	/**
	 * @param scriptName the scriptName to set
	 */
	public void setScriptName(String scriptName) {
		this.scriptName = scriptName;
	}
	
	/**
	 * @return the information
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * @param information the information to set
	 */
	public void setInformation(String information) {
		this.information = information;
	}

	/**
	 * @return the dependencies
	 */
	public List<Dependency> getDependencies() {
		return dependencies;
	}

	/**
	 * @param dependencies the dependencies to set
	 */
	public void setDependencies(List<Dependency> dependencies) {
		this.dependencies = dependencies;
	}

	public Dependency getDependencyById(Long identifier) {
		Dependency dependency = null;

		for (Dependency dependencyObject : dependencies) {

			if (dependencyObject.getId() == identifier) {
				dependency = dependencyObject;
				break;
			}
		}
		return dependency;
	}
	
	/**
	 * @return the deleted
	 */
	public Boolean isDeleted() {
		if (this.isDeleted == null) {
			this.isDeleted = Boolean.FALSE;
		}
		return isDeleted;
	}

	/**
	 * @param deleted the deleted to set
	 */
	public void setDeleted(Boolean isDeleted) {
		if (isDeleted == null) {
			isDeleted = Boolean.FALSE;
		}
		this.isDeleted = isDeleted;
	}
}
