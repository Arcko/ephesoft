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

package com.ephesoft.dcma.openoffice.service;

import java.io.File;
import java.net.URI;

import org.artofsolving.jodconverter.OfficeDocumentConverter;
import org.artofsolving.jodconverter.document.DefaultDocumentFormatRegistry;
import org.artofsolving.jodconverter.office.ExternalOfficeManagerConfiguration;
import org.artofsolving.jodconverter.office.OfficeException;
import org.artofsolving.jodconverter.office.OfficeManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.SmartLifecycle;

import com.ephesoft.dcma.core.common.FileType;
import com.ephesoft.dcma.core.service.FileFormatConvertor;
import com.ephesoft.dcma.util.FileUtils;

/**
 * This is open office convertor class.
 * 
 * @author Ephesoft
 * @version 1.0
 */
public class OpenOfficeConvertor implements SmartLifecycle, FileFormatConvertor {

	/**
	 * autoStartup boolean.
	 */
	private boolean autoStartup;

	/**
	 * server String.
	 */
	private String server;

	/**
	 * homePath String.
	 */
	private String homePath;

	/**
	 * serverPort int.
	 */
	private int serverPort;

	/**
	 * maxTasksPerProcess int.
	 */
	private int maxTasksPerProcess;

	/**
	 * taskExecutionTimeout int.
	 */
	private int taskExecutionTimeout;

	/**
	 * sharedLocation String.
	 */
	private String sharedLocation;

	/**
	 * absoluteLocation String.
	 */
	private String absoluteLocation;

	/**
	 * jodOOManager OfficeManager.
	 */
	private OfficeManager jodOOManager;

	/**
	 * jodConverter OfficeDocumentConverter.
	 */
	private OfficeDocumentConverter jodConverter;

	/**
	 * LOGGER to print the logging information.
	 */
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenOfficeConvertor.class);

	/**
	 * Returns auto startup is true or not.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isAutoStartup() {
		return autoStartup;
	}

	/**
	 * To stop.
	 * 
	 * @param callback {@link Runnable}
	 */
	@Override
	public void stop(Runnable callback) {
		callback.run();
	}

	/**
	 * Returns running is true or not.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean isRunning() {
		return false;
	}

	/**
	 * init method.
	 */
	public void init() {
		ExternalOfficeManagerConfiguration externalProcessOfficeManager = new ExternalOfficeManagerConfiguration();
		externalProcessOfficeManager.setPortNumber(serverPort);
		externalProcessOfficeManager.setConnectOnStart(true);
		this.jodOOManager = externalProcessOfficeManager.buildOfficeManager(server);
		this.jodConverter = new OfficeDocumentConverter(this.jodOOManager);
	}

	
	@Override
	public void start() {
		try {
			this.jodOOManager.start();
		} catch (OfficeException officeException) {
			LOGGER.error(
					"Problem in connecting to external open office. Reason may be open office is not running on configured url/port.",
					officeException);
		}
		catch(Exception exception)
		{
			LOGGER.error(
					"Problem in connecting to external open office. Reason may be open office is not running on configured url/port.",
					exception);
		}
	}

	/**
	 * Stop method.
	 */
	@Override
	public void stop() {
		this.jodOOManager.stop();
	}

	/**
	 * To get phase.
	 * 
	 * @return int
	 */
	@Override
	public int getPhase() {
		return 0;
	}

	/**
	 * To convert input file URI to output file URI.
	 * 
	 * @param inputFileURI URI
	 * @param outputFileURI URI
	 * @param outputFileType FileType
	 */
	@Override
	public void convert(URI inputFileURI, URI outputFileURI, FileType outputFileType) {
		jodConverter.convert(new File(inputFileURI), new File(outputFileURI), new DefaultDocumentFormatRegistry()
		.getFormatByExtension(outputFileType.getExtension()));
	}

	/**
	 * To convert input file path to output file path.
	 * 
	 * @param inputFilePath String
	 * @param encodedFileName String
	 * @param outputFileType FileType
	 * 
	 */
	@Override
	public void convert(final String inputFilePath, final String outputFilePath, final FileType outputFileType) {
		LOGGER.info("Converting file at " + inputFilePath + " to  " + outputFilePath + " into " + outputFileType + " type");
		File inputFile = new File(inputFilePath);
		File outputFile = new File(outputFilePath);
		outputFile.getParentFile().mkdirs();
		jodConverter.convert(inputFile, outputFile, new DefaultDocumentFormatRegistry().getFormatByExtension(outputFileType
				.getExtension()));
		LOGGER.info("Output File created at " + outputFilePath);
	}

	@Override
	public void convert(final byte[] buffer, String outputFilePath, final FileType outputFileType) {
		File outFile = new File(outputFilePath);
		outFile.getParentFile().mkdirs();
		outputFilePath = outFile.getPath();

		outputFilePath = FileUtils.changeFileExtension(outputFilePath, outputFileType.getExtension());
		LOGGER.info("Performing conversion");
		outFile = new File(outputFilePath);
		jodConverter.convert(buffer, outFile, new DefaultDocumentFormatRegistry().getFormatByExtension(outputFileType.getExtension()));
		LOGGER.info("Output File created at " + outputFilePath);
	}

	/**
	 * To set auto startup.
	 * 
	 * @param autoStartup boolean
	 */
	public void setAutoStartup(boolean autoStartup) {
		this.autoStartup = autoStartup;
	}

	/**
	 * To set Home Path.
	 * 
	 * @param homePath String
	 */
	public void setHomePath(String homePath) {
		this.homePath = homePath;
		LOGGER.debug(this.homePath);
	}

	/**
	 * To set Server Port.
	 * 
	 * @param serverPort int
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * To set Max Tasks Per Process.
	 * 
	 * @param maxTasksPerProcess int
	 */
	public void setMaxTasksPerProcess(int maxTasksPerProcess) {
		this.maxTasksPerProcess = maxTasksPerProcess;
		LOGGER.debug(Integer.toString(this.maxTasksPerProcess));
	}

	/**
	 * To set Task Execution Timeout.
	 * 
	 * @param taskExecutionTimeout int
	 */
	public void setTaskExecutionTimeout(int taskExecutionTimeout) {
		this.taskExecutionTimeout = taskExecutionTimeout;
		LOGGER.debug(Integer.toString(this.taskExecutionTimeout));
	}

	/**
	 * To get server.
	 * 
	 * @return String
	 */
	public String getServer() {
		return server;
	}

	/**
	 * To set server.
	 * 
	 * @param server String
	 */
	public void setServer(String server) {
		this.server = server;
	}

	/**
	 * To get Shared Location.
	 * 
	 * @return String
	 */
	public String getSharedLocation() {
		return sharedLocation;
	}

	/**
	 * To set Shared Location.
	 * 
	 * @param sharedLocation String
	 */
	public void setSharedLocation(String sharedLocation) {
		this.sharedLocation = sharedLocation;
	}

	public String getAbsoluteLocation() {
		return absoluteLocation;
	}

	/**
	 * To get home path.
	 * 
	 * @return String
	 */
	public String getHomePath() {
		return homePath;
	}

	/**
	 * To get Max Tasks Per Process.
	 * 
	 * @return int
	 */
	public int getMaxTasksPerProcess() {
		return maxTasksPerProcess;
	}

	/**
	 * To get Task Execution Timeout.
	 * 
	 * @return int
	 */
	public int getTaskExecutionTimeout() {
		return taskExecutionTimeout;
	}

	/**
	 * This method will refresh OpenOffice ComponentContext 
	 * 
	 */
	@Override
	public void refreshOpenOfficeComponentContext() {
		init();
		start();
	}

}
