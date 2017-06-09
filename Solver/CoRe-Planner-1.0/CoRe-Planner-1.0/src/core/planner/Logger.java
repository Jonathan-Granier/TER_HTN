/*
 * Copyright MAGMA TEAM Laboratory Leibniz.
 *
 * This software is a computer program whose purpose is to propose an
 * environment for multi-agent planning.
 *
 * This software is governed by the CeCILL  license under French law and
 * abiding by the rules of distribution of free software.  You can  use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and  rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty  and the software's author,  the holder of the
 * economic rights,  and the successive licensors  have only  limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading,  using,  modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean  that it is complicated to manipulate,  and  that  also
 * therefore means  that it is reserved for developers  and  experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and,  more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package core.planner;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * The <code>Logger</code> class implements a list a useful methods used to log the Planner.
 *
 * @author Pellier Damien
 * @version 1.0, 14/01/03
 */
public final class Logger {
	
	/**
	 * Default log level.
	 */
	private static final int DEFAULT_LOG_LEVEL = 10;
	
	/**
	 * Default font.
	 */
	private static final String DEFAULT_FONT = "monospaced";
	
	/**
	 * Default font size.
	 */
	private static final int DEFAULT_FONT_SIZE = 12;
	
	/**
	 * The display <tt>Document</tt>.
	 */
	private static Document display;
	
	/**
	 * The level of the log.
	 */
	public static int level = DEFAULT_LOG_LEVEL;
	
	/**
	 * The attribute of the normal text.
	 */
	private static MutableAttributeSet normal;
	
	/**
	 * The attribute of the info text.
	 */
	private static MutableAttributeSet info;
	
	/**
	 * The attribute of the error text.
	 */
	private static MutableAttributeSet error;
	
	/**
	 * The attribute of the warning text.
	 */
	private static MutableAttributeSet warning;
	
	/**
	 * Creates a empty <tt>Logger</tt>.
	 */
	private Logger() {
	}
	
	/**
	 * Sets the level of this Logger.
	 *
	 * @param l the level of the logs.
	 */
	public static void setLevel(final int l) {
		level = l;
	}
	
	/**
	 * Set the display of the <tt>Logger</tt>.
	 *
	 * @param d the <tt>Document</tt> where the log must be stored.
	 */
	public static void setDocumentDisplay(final Document d) {
		normal = new SimpleAttributeSet();
		StyleConstants.setForeground(normal, Color.BLACK);
		StyleConstants.setFontFamily(normal, DEFAULT_FONT);
		StyleConstants.setFontSize(normal, DEFAULT_FONT_SIZE);
		
		info = new SimpleAttributeSet();
		StyleConstants.setForeground(info, Color.BLUE);
		StyleConstants.setFontFamily(info, DEFAULT_FONT);
		StyleConstants.setFontSize(info, DEFAULT_FONT_SIZE);
		
		warning = new SimpleAttributeSet();
		StyleConstants.setForeground(warning, Color.ORANGE);
		StyleConstants.setFontFamily(warning, DEFAULT_FONT);
		StyleConstants.setFontSize(warning, DEFAULT_FONT_SIZE);
		
		error = new SimpleAttributeSet();
		StyleConstants.setForeground(error, Color.RED);
		StyleConstants.setFontFamily(error, DEFAULT_FONT);
		StyleConstants.setFontSize(error, DEFAULT_FONT_SIZE);
		
		display = d;
	}
	
	/**
	 * Returns the <tt>Document</tt> display of the <tt>Logger</tt>.
	 *
	 * @return the <tt>Document</tt> display.
	 */
	public static Document getDocumentDisplay() {
		return display;
	}
	
	/**
	 * Displays an error on the current output stream.
	 *
	 * @param values the list of objects representing the message to log.
	 */
	public static void error(final Object... values) {
		final StringBuilder msg = argsToStringBuilder(values);
		if (display != null) {
			try {
				display.insertString(display.getLength(), "Error: " + msg + "\n", error);
			} catch (final BadLocationException e) {
				// Do nothing
			}
		} else {
			System.out.println("Error: " + msg);
		}
	}
	
	/**
	 * Displays an warning on the current output stream.
	 *
	 * @param values the list of objects representing the message to log.
	 */
	public static void warning(final Object... values) {
		final StringBuilder msg = argsToStringBuilder(values);
		if (display != null) {
			try {
				display.insertString(display.getLength(), "Warning: " + msg + "\n", warning);
			} catch (final BadLocationException e) {
				// Do nothing
			}
		} else {
			System.out.println("Warning: " + msg);
		}
	}
	
	/**
	 * Displays an info on the current output stream.
	 *
	 * @param values the list of objects representing the message to log.
	 */
	public static void info(final Object... values) {
		final StringBuilder msg = argsToStringBuilder(values);
		if (display != null) {
			try {
				display.insertString(display.getLength(), msg + "\n", info);
			} catch (final BadLocationException e) {
				// Do nothing
			}
		} else {
			System.out.println("Info: " + msg);
		}
	}
	
	/**
	 * Displays a log message on the current output stream.
	 *
	 * @param l the level message.
	 * @param values the list of objects representing the message to log.
	 */
	public static void log(final int l, final Object... values) {
		if (level >= l) {
			final StringBuilder msg = argsToStringBuilder(values);
			if (display != null) {
				try {
					display.insertString(display.getLength(), msg + "\n", normal);
				} catch (final BadLocationException e) {
					// Do nothing
				}
			} else {
				System.out.println(msg);
			}
		}
	}
	
	/**
	 * Return a list of arguments into a <tt>StringBuilder</tt>. <br>
	 * This method allows to make the string concatnation only when it is needed. Therefore, it improves the planner performance.
	 *
	 * @param values the list of arguments to convert.
	 * @return a list of arguments into a <tt>StringBuilder</tt>.
	 */
	private static StringBuilder argsToStringBuilder(final Object... values) {
		final StringBuilder msg = new StringBuilder();
		for (final Object v : values) {
			try {
				final Method m = v.getClass().getMethod("toString", new Class[0]);
				final String str = (String) m.invoke(v, new Object[0]);
				msg.append(str);
			} catch (final SecurityException e) {
				Logger.error("SecurityException toString: ", v);
			} catch (final NoSuchMethodException e) {
				Logger.error("NoSuchMethodException toString: ", v);
			} catch (final IllegalArgumentException e) {
				// Should never happen
				Logger.error("IllegalArgumentException toString: ", v);
			} catch (final IllegalAccessException e1) {
				Logger.error("IllegalAccessException toString: ", v);
			} catch (final InvocationTargetException e1) {
				Logger.error("InvocationTargetException toString: ", v);
			}
		}
		return msg;
	}
	
	/**
	 * Breaks the current flow of execution and wait for a standard input stream.
	 */
	public static void pressAnyKey() {
		try {
			System.out.println("Press any key...");
			System.in.read(new byte[System.in.available()]);
			System.in.read();
		} catch (final Exception e) {
			// Do nothing
		}
	}
	
}
