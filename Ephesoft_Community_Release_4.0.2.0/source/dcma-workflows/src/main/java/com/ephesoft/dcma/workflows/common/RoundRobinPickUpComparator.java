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

package com.ephesoft.dcma.workflows.common;

import java.util.Comparator;

import com.ephesoft.dcma.da.domain.BatchInstance;
import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.NumberUtil;
import com.ephesoft.dcma.workflows.util.WorkflowUtil;

/**
 * This class represents a Round Robin based comparator for batch instances.
 * 
 * @author Ephesoft
 * 
 *         <b>created on</b> Oct 21, 2014 <br/>
 * @version $LastChangedDate:$ <br/>
 *          $LastChangedRevision:$ <br/>
 */
public class RoundRobinPickUpComparator implements Comparator<BatchInstance> {

	@Override
	public int compare(final BatchInstance batchInstance1, final BatchInstance batchInstance2) {
		int result;
		if (batchInstance1 != null && batchInstance2 != null) {
			int batchInstancePriority1 = batchInstance1.getPriority();
			int batchInstancePriority2 = batchInstance2.getPriority();
			if (batchInstancePriority1 == batchInstancePriority2) {
				result = compareBatchProcess(batchInstance1, batchInstance2);
			} else {
				result = NumberUtil.compare(batchInstancePriority1, batchInstancePriority2);
			}
		} else {
			result = -1;
		}
		return result;
	}

	/**
	 * Compares two batch instances on the basis of their batch process names.
	 * 
	 * @param batchInstance1 {@link BatchInstance}
	 * @param batchInstance2 {@link BatchInstance}
	 * @return int 0 if they are equal, -1 if batchInstance1 is less than batchInstance2 and 1 if batchInstance1 is more than
	 *         batchInstance2.
	 */
	private int compareBatchProcess(final BatchInstance batchInstance1, final BatchInstance batchInstance2) {
		int result;
		String processName1 = batchInstance1.getBatchClass().getProcessName();
		String processName2 = batchInstance2.getBatchClass().getProcessName();
		if (EphesoftStringUtil.isNullOrEmpty(processName1) || EphesoftStringUtil.isNullOrEmpty(processName2)) {
			result = WorkflowUtil.comparePickUpStatus(batchInstance1, batchInstance2);
		} else {
			result = processName1.compareTo(processName2);
		}
		return result;
	}

}
