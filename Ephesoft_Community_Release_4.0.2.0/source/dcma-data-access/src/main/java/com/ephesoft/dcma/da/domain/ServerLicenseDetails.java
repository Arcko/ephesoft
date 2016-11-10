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
import javax.persistence.Table;

import com.ephesoft.dcma.core.model.common.AbstractChangeableEntity;

/**
 * @author Ephesoft
 * 
 *         <b>created on</b> Nov 10, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
@Entity
@Table(name = "ephesoft_meta_data")
public class ServerLicenseDetails extends AbstractChangeableEntity {

	/**
	 * Serial Version UID long
	 */
	private static final long serialVersionUID = 6213724039823490819L;

	/**
	 * IP Address of the server {@link String}
	 */
	@Column(name = "meta_data_1")
	private String dummyField1;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "meta_data_2")
	private String licenseKey;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "meta_data_3")
	private String imageCount;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "meta_data_4")
	private String dummyField2;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "meta_data_5")
	private String resetDate;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "meta_data_6")
	private String createdAt;

	/**
	 * ApplicationContext {@link String}
	 */
	@Column(name = "meta_data_7")
	private String isLicenseField;

	public String getImageCount() {
		return imageCount;
	}

	public void setImageCount(final String imageCount) {
		this.imageCount = imageCount;
	}

	public String getDummyField1() {
		return dummyField1;
	}

	public void setDummyField1(final String dummyField1) {
		this.dummyField1 = dummyField1;
	}

	public String getDummyField2() {
		return dummyField2;
	}

	public void setDummyField2(final String dummyField2) {
		this.dummyField2 = dummyField2;
	}

	public String getResetDate() {
		return resetDate;
	}

	public void setResetDate(final String dummyField3) {
		this.resetDate = dummyField3;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(final String createdAt) {
		this.createdAt = createdAt;
	}

	public String getLicenseKey() {
		return licenseKey;
	}

	public void setLicenseKey(String licenseKey) {
		this.licenseKey = licenseKey;
	}

	public String getIsLicenseField() {
		return isLicenseField;
	}

	public void setIsLicenseField(String isLicenseField) {
		this.isLicenseField = isLicenseField;
	}

}