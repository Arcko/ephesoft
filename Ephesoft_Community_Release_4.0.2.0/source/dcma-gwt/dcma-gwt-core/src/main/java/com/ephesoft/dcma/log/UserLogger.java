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

package com.ephesoft.dcma.log;

import com.ephesoft.dcma.util.EphesoftStringUtil;
import com.ephesoft.dcma.util.logger.EphesoftLogger;
import com.ephesoft.dcma.util.logger.EphesoftLoggerFactory;

/**
 * UserLogger is used for logging information specific for users in a different
 * log file. The purpose of these logs is to log specific user information.
 * 
 * @author Ephesoft
 * @version 1.0
 * 
 */
public class UserLogger {

	/**
	 * LOGGER for <code>UserLogger</code> class.
	 */
	private static final EphesoftLogger USER_LOGGER = EphesoftLoggerFactory
			.getLogger(UserLogger.class);

	/**
	 * Log INFO level logs.
	 * 
	 * @param screenName
	 *            {@link String} Name of screen for which user logs are been
	 *            generated.
	 * @param args
	 *            logging information.
	 */
	public static void info(final String screenName, final Object... args) {
		if (validateInput(screenName, args)) {
			USER_LOGGER.info(screenName, EphesoftStringUtil.concatenate(args));
		}
	}

	/**
	 * Log DEBUG level logs.
	 * 
	 * @param screenName
	 *            {@link String} Name of screen for which user logs are been
	 *            generated.
	 * @param args
	 *            logging information.
	 */
	public static void debug(final String screenName, final Object... args) {
		if (validateInput(screenName, args)) {
			USER_LOGGER.debug(screenName, EphesoftStringUtil.concatenate(args));
		}
	}

	/**
	 * Log WARN level logs.
	 * 
	 * @param screenName
	 *            {@link String} Name of screen for which user logs are been
	 *            generated.
	 * @param args
	 *            logging information.
	 */
	public static void warn(final String screenName, final Object... args) {
		if (validateInput(screenName, args)) {
			USER_LOGGER.warn(screenName, EphesoftStringUtil.concatenate(args));
		}
	}

	/**
	 * Log ERROR level logs with exception stack trace.
	 * 
	 * <p>
	 * Pass the exception object as it is.
	 * 
	 * @param screenName
	 *            {@link String} Name of screen for which user logs are been
	 *            generated.
	 * @param throwable
	 *            {@link Throwable} Exception trace.
	 * @param args
	 *            logging information.
	 */
	public static void warn(final String screenName, final Throwable throwable,
			final Object... args) {
		if (validateInput(screenName, args)) {
			USER_LOGGER.warn(throwable, screenName,
					EphesoftStringUtil.concatenate(args));
		}
	}

	/**
	 * Log ERROR level logs.
	 * 
	 * @param screenName
	 *            {@link String} Name of screen for which user logs are been
	 *            generated.
	 * @param args
	 *            logging information.
	 */
	public static void error(final String screenName, final Object... args) {
		if (validateInput(screenName, args)) {
			USER_LOGGER.error(screenName, EphesoftStringUtil.concatenate(args));
		}
	}

	/**
	 * Log ERROR level logs with exception stack trace.
	 * 
	 * <p>
	 * Pass the exception object as it is.
	 * 
	 * @param screenName
	 *            {@link String} Name of screen for which user logs are been
	 *            generated.
	 * @param throwable
	 *            {@link Throwable} Exception trace.
	 * @param args
	 *            logging information.
	 */
	public static void error(final String screenName,
			final Throwable throwable, final Object... args) {
		if (validateInput(screenName, args)) {
			USER_LOGGER.error(throwable, screenName,
					EphesoftStringUtil.concatenate(args));
		}
	}

	// Validates input parameters passed for logging.
	private static boolean validateInput(final String screenName,
			final Object... args) {
		return !EphesoftStringUtil.isNullOrEmpty(screenName) && null != args
				&& args.length != 0;
	}

}