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

package com.ephesoft.dcma.da.service;

import java.util.List;

import com.ephesoft.dcma.da.domain.Plugin;

/**
 * This is a database service to get the plugin details for a plugin.
 * 
 * @author Ephesoft
 * @version 1.0
 * @see com.ephesoft.dcma.da.service.PluginServiceImpl
 */
public interface PluginService {

	/**
	 * API to get the plugin details by Id.
	 * 
	 * @param pluginId {@link Long}
	 * @return Plugin {@link Plugin}
	 */
	Plugin getPluginPropertiesForPluginId(Long pluginId);

	/**
	 * API to get the plugin details by name.
	 * 
	 * @param pluginName {@link String}
	 * @return Plugin {@link Plugin}
	 */
	Plugin getPluginPropertiesForPluginName(String pluginName);

	/**
	 * Api to get plugins on the basis of given moduleId, and result bounds.
	 * 
	 * @param moduleId {@link Long}
	 * @param startResult {@link Integer}
	 * @param maxResult {@link Integer}
	 * @return {@link List}<{@link Plugin}>
	 */
	List<Plugin> getPlugins(Long moduleId, int startResult, int maxResult);

	/**
	 * Api to get all plugins.
	 * 
	 * @return {@link List} <{@link Plugin}>
	 */
	List<Plugin> getAllPlugins();

	/**
	 * API to create a new plugin.
	 * 
	 * @param plugin {@link Plugin}
	 */
	void createNewPlugin(Plugin plugin);

	/**
	 * API to merge/update the given plugin.
	 * 
	 * @param plugin {@link Plugin}
	 */
	void mergePlugin(Plugin plugin);

	/**
	 * API To delete the given plugin.
	 * 
	 * @param plugin {@link Plugin}
	 * @param removeReferences {@link Boolean}
	 */
	void removePluginAndReferences(Plugin plugin, boolean removeReferences);

	/**
	 * API to retrieve names of all the plugins.
	 * 
	 * @return {@link List}< {@link String}>
	 */
	List<String> getAllPluginsNames();
}
