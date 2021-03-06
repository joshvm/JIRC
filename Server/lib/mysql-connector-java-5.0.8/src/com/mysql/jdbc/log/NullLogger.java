/*
 Copyright (C) 2002-2004 MySQL AB

 This program is free software; you can redistribute it and/or modify
 it under the terms of version 2 of the GNU General Public License as 
 published by the Free Software Foundation.

 There are special exceptions to the terms and conditions of the GPL 
 as it is applied to this software. View the full text of the 
 exception in file EXCEPTIONS-CONNECTOR-J in the directory of this 
 software distribution.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA



 */
package com.mysql.jdbc.log;

/**
 * A logger that does nothing. Used before the log is configured via the URL or
 * properties.
 * 
 * @author Mark Matthews
 * 
 * @version $Id: NullLogger.java 3726 2005-05-19 15:52:24Z mmatthews $
 */
public class NullLogger implements Log {

	/**
	 * Creates a new NullLogger with the given name
	 * 
	 * @param instanceName
	 *            (ignored)
	 */
	public NullLogger(String instanceName) {
	}

	/**
	 * @see Log#isDebugEnabled()
	 */
	public boolean isDebugEnabled() {
		// XXX Auto-generated method stub
		return false;
	}

	/**
	 * @see Log#isErrorEnabled()
	 */
	public boolean isErrorEnabled() {
		// XXX Auto-generated method stub
		return false;
	}

	/**
	 * @see Log#isFatalEnabled()
	 */
	public boolean isFatalEnabled() {
		// XXX Auto-generated method stub
		return false;
	}

	/**
	 * @see Log#isInfoEnabled()
	 */
	public boolean isInfoEnabled() {
		// XXX Auto-generated method stub
		return false;
	}

	/**
	 * @see Log#isTraceEnabled()
	 */
	public boolean isTraceEnabled() {
		// XXX Auto-generated method stub
		return false;
	}

	/**
	 * @see Log#isWarnEnabled()
	 */
	public boolean isWarnEnabled() {
		// XXX Auto-generated method stub
		return false;
	}

	/**
	 * @see Log#logDebug(Object)
	 */
	public void logDebug(Object msg) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logDebug(Object,
	 *      Throwable)
	 */
	public void logDebug(Object msg, Throwable thrown) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logError(Object)
	 */
	public void logError(Object msg) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logError(Object,
	 *      Throwable)
	 */
	public void logError(Object msg, Throwable thrown) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logFatal(Object)
	 */
	public void logFatal(Object msg) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logFatal(Object,
	 *      Throwable)
	 */
	public void logFatal(Object msg, Throwable thrown) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logInfo(Object)
	 */
	public void logInfo(Object msg) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logInfo(Object,
	 *      Throwable)
	 */
	public void logInfo(Object msg, Throwable thrown) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logTrace(Object)
	 */
	public void logTrace(Object msg) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logTrace(Object,
	 *      Throwable)
	 */
	public void logTrace(Object msg, Throwable thrown) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logWarn(Object)
	 */
	public void logWarn(Object msg) {
		// XXX Auto-generated method stub

	}

	/**
	 * @see Log#logWarn(Object,
	 *      Throwable)
	 */
	public void logWarn(Object msg, Throwable thrown) {
		// XXX Auto-generated method stub

	}

}
