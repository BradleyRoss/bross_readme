package bradleyross.library.helpers;
import java.io.IOException;
// import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.StringWriter;
import java.io.PrintWriter;
import org.slf4j.Marker;
import org.slf4j.LoggerFactory;
import org.apache.log4j.PropertyConfigurator;
/**
 * Allows conversion from log4j to slf4j by simply changing
 * import statements.
 * 
 * <p>In DCM4CHEE org.dcm4che,log.DatasetRenderer serves as
 *    a subclass of org.apache.log4j.or.ObjectRenderer to
 *    convert datasets to string objects.</p>
 * <p>dcm4jboss-all/dcm4jboss-ejb/src/resources/log4j.properties has
 *    the lines<br />
 * log4j.rootLogger=INFO, stdout<br />
 * log4j.appender.stdout=org.apache.log4j.ConsoleAppender<br />
 * log4j.appender.stdout.layout=org.apache.log4j.PatternLayout<br />
 * log4j.appender.stdout.layout.ConversionPattern=%d{ABSOLUTE} %-5p - %m\n<br />
 * log4j.renderer.org.dcm4che.data.Dataset=org.dcm4che.log.DatasetRenderer</p>
 * <p>The order of the levels are FATAL, ERROR, WARN, INFO, DEBUG, and
 *    TRACE.</p>   
 * @see org.apache.log4j.Logger
 * @see org.slf4j.Logger
 * @see org.slf4j.LoggerFactory
 * @see java.util.logging.Logger
 * 
 * @author Bradley Ross
 *
 */
public class Logger implements org.slf4j.Logger {
	/**
	 * Encapsulated logger object.
	 */
	protected org.slf4j.Logger logger = null;
	/**
	 * Create a logger using a descriptive string.
	 * @param value name of logger
	 * @see org.slf4j.LoggerFactory#getLogger(String)
	 * @see org.apache.log4j.Logger#getLogger(String)
	 */
	public Logger(String value) {
		logger =  LoggerFactory.getLogger(value);
		return;
	}
	/**
	 * Create a logger by encapsulating an existing
	 * {@link org.slf4j.Logger} object.
	 * @param value org.slf4j.Logger object
	 */
	public Logger(org.slf4j.Logger value) {
		logger = value;
		return;
	}
	/**
	 * Create a logger using the name of a class
	 * @param value class for which logger is created
	 * @see org.slf4j.LoggerFactory#getLogger(Class)
	 * @see org.apache.log4j.Logger#getLogger(Class)
	 */
	@SuppressWarnings("rawtypes")
	public Logger(Class value) {
		logger = LoggerFactory.getLogger(value);
		return;
	}
	/**
	 * Create a logger based on a String value
	 * @param value name for logger
	 * @return logger
	 */
	public static Logger  getLogger(String value) {
		return new Logger(value);
	}
	/**
	 * Create a logger based on the name of a class.
	 * @param value class for which logger is desired
	 * @return logger
	 */
	@SuppressWarnings("rawtypes")
	public static Logger getLogger(Class value) {
		return new Logger(value);
	}
	/**
	 * Test driver.
	 * 
	 * <p>It appears that this test has to be run from the command
	 *    line.  If run from within the Eclipse environment, it
	 *    retains log4j settings from previous runs and this
	 *    produces problems when trying to execute the test.</p>
	 *    
	 *  @param args not used in this program
	 *  @see org.apache.log4j.PatternLayout
	 */
	public static void main(String[] args) {
		System.out.println("org.acr.Dicom.Logger");
		PropertyConfigurator.configure(Logger.class.getResource("log4j.properties"));
		LineNumberReader reader = 
			new LineNumberReader(new InputStreamReader(Logger.class.getResourceAsStream("log4j.properties")));
		try {
			System.out.println("*****  Logger configuration");
			while (true) {
				String line = reader.readLine();
				if (line == null) { break; }
				System.out.println(line);
			}
			System.out.println("*****");
		} catch (IOException e) {
			System.out.println(e.getClass().getName() + " " + e.getMessage());
		}
		
		Logger test = new Logger("test");
		Logger test2 = new Logger("test.beta");
		test.info("First test");
		test2.error("Second test");
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @param arg0 message to be logged
	 * @see org.slf4j.Logger#error(String)
	 */
	public void fatal(String arg0) {
		logger.error(arg0);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(String, Object)
	 */
	public void fatal(String arg0, Object arg1) {
		logger.error(arg0, arg1);	
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(String, Object[])
	 */
	public void fatal(String arg0, Object[] arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(String, Throwable)
	 */
	public void fatal(String arg0, Throwable arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(Marker, String)
	 */
	public void fatal(Marker arg0, String arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(String, Object, Object)
	 */
	public void fatal(String arg0, Object arg1, Object arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(Marker, String, Object)
	 */
	public void fatal(Marker arg0, String arg1, Object arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(Marker, String, Object[])
	 */
	public void fatal(Marker arg0, String arg1, Object[] arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(Marker, String, Throwable)
	 */
	public void fatal(Marker arg0, String arg1, Throwable arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Since fatal level messages are not supported in slf4j,
	 * an error level message is generated instead.
	 * @see org.slf4j.Logger#error(Marker, String, Object, Object)
	 */
	public void fatal(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Not supported in slf4j.
	 * 
	 * <p>I'm assuming that we should use
	 *    {@link #fatal(String, Object)} with a
	 *    null value for the format string.</p>
	 * @see org.apache.log4j.Logger#fatal(Object)
	 * @see Logger#fatal(Object)
	 * @param arg0
	 */
	public void fatal(Object arg0) {
		fatal("{}", arg0);
	}
	/**
	 * Not support slf4j.
	 * @see org.apache.log4j.Logger#fatal(Object, Throwable)
	 * 
	 * @param arg0
	 * @param arg1
	 */
	public void fatal(Object arg0, Throwable arg1) {
		fatal("{}", arg0, arg1);
	}
	public void fatal(Throwable arg0) {
		error(arg0);
	}
	/**
	 * Generate a string from a throwable for use in log messages.
	 * @param e throwable object
	 * @return message for log
	 */
	protected String errorToString(Throwable e) {
		StringWriter writer = new StringWriter();
		PrintWriter output = new PrintWriter(writer);
		output.println(e.getClass() + " " + e.getMessage());
		e.printStackTrace(output);
		return writer.toString();
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#error(Object)
	 * @param arg0
	 */
	public void error(Object arg0) {
		logger.error("{}", arg0);
	}
	public void error(Throwable arg0) {
		error(errorToString(arg0));
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#error(Object, Throwable)
	 * @param arg0
	 */
	public void error(Object arg0, Throwable arg1) {
		logger.error("{}", arg0, arg1);
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#warn(Object)
	 * @param arg0
	 */
	public void warn(Object arg0) {
		logger.warn("{}", arg0);
	}
	public void warn(Throwable arg0) {
		warn(errorToString(arg0));
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#warn(Object, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public void warn(Object arg0, Throwable arg1) {
		logger.warn("{}", arg0, arg1);
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#info(Object)
	 * @param arg0
	 */
	public void info(Object arg0) {
		logger.info("{}", arg0);
	}
	public void info(Throwable arg0) {
		info(errorToString(arg0));
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#info(Object, Throwable)
	 * @param arg0 object to be placed in log
	 * @param arg1 throwable (exception/error) to be placed in log
	 */
	public void info(Object arg0, Throwable arg1) {
		logger.info("{}", arg0, arg1);
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#debug(Object)
	 * @param arg0 object to be placed in log
	 */
	public void debug(Object arg0) {
		logger.debug("{}", arg0);
	}
	public void debug(Throwable arg0) {
		debug(errorToString(arg0));
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#debug(Object, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public void debug(Object arg0, Throwable arg1) {
		logger.debug("{}", arg0, arg1);
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#trace(Object)
	 * @param arg0
	 */
	public void trace(Object arg0) {
		logger.trace("{}", arg0);
	}
	public void trace(Throwable arg0) {
		trace(errorToString(arg0));
	}
	/**
	 * Not supported in slf4j.
	 * @see org.apache.log4j.Logger#trace(Object, Throwable)
	 * @param arg0
	 * @param arg1
	 */
	public void trace (Object arg0, Throwable arg1) {
		logger.trace("{}", arg0, arg1);
	}
	/** 
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 * @param arg0 message to be placed in log
	 */
	public void debug(String arg0) {
		logger.debug(arg0);		
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 * @param arg0 formatting to be applied to message with the characters
	 *        <code>{}</code> replaced by arg1.toString().
	 * @param arg1  object to be placed in message
	 */
	public void debug(String arg0, Object arg1) {
		logger.debug(arg0, arg1);	
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(String arg0, Object[] arg1) {
		logger.debug(arg0, arg1);
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(String arg0, Throwable arg1) {
		logger.debug(arg0, arg1);
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(Marker arg0, String arg1) {
		logger.debug(arg0, arg1);	
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(String arg0, Object arg1, Object arg2) {
		logger.debug(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(Marker arg0, String arg1, Object arg2) {
		logger.debug(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(Marker arg0, String arg1, Object[] arg2) {
		logger.debug(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(Marker arg0, String arg1, Throwable arg2) {
		logger.debug(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the DEBUG level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void debug(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.debug(arg0, arg1, arg2, arg3);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(String arg0) {
		logger.error(arg0);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(String arg0, Object arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(String arg0, Object[] arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(String arg0, Throwable arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(Marker arg0, String arg1) {
		logger.error(arg0, arg1);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(String arg0, Object arg1, Object arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(Marker arg0, String arg1, Object arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(Marker arg0, String arg1, Object[] arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(Marker arg0, String arg1, Throwable arg2) {
		logger.error(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the ERROR level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void error(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.error(arg0, arg1, arg2, arg3);
	}

	public String getName() {
		return logger.getName();
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(String arg0) {
		logger.info(arg0);	
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(String arg0, Object arg1) {
		logger.info(arg0, arg1);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(String arg0, Object[] arg1) {
		logger.info(arg0, arg1);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(String arg0, Throwable arg1) {
		logger.info(arg0, arg1);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(Marker arg0, String arg1) {
		logger.info(arg0, arg1);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(String arg0, Object arg1, Object arg2) {
		logger.info(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(Marker arg0, String arg1, Object arg2) {
		logger.info(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(Marker arg0, String arg1, Object[] arg2) {
		logger.info(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(Marker arg0, String arg1, Throwable arg2) {
		logger.info(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the INFO level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void info(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.info(arg0, arg1, arg2, arg3);
	}
	/**
	 * Is generating messages at the DEBUG level enabled.
	 */
	public boolean isDebugEnabled() {
		return logger.isDebugEnabled();
	}
	/**
	 * Is generating messages at the DEBUG level enabled.
	 */
	public boolean isDebugEnabled(Marker arg0) {
		return logger.isDebugEnabled(arg0);
	}
	/**
	 * Is generating messages at the ERROR level enabled.
	 */
	public boolean isErrorEnabled() {
		return logger.isErrorEnabled();
	}
	/**
	 * Is generating messages at the ERROR level enabled.
	 */
	public boolean isErrorEnabled(Marker arg0) {
		return logger.isErrorEnabled(arg0);
	}
	/**
	 * Fatal level of error not supported by slf4j although
	 * ut us supported by log4j.
	 * @return always returns false
	 */
	public boolean isFatalEnabled() {
		return false;
	}
	/**
	 * Fatal level of error not supported by slf4j although
	 * it is supported by log4j.
	 * @param arg0
	 * @return always returns false
	 */
	public boolean isFatalEnabled(Marker arg0) {
		return false;
	}
	/**
	 * Is generating messages at the INFO level enabled.
	 */
	public boolean isInfoEnabled() {
		return logger.isInfoEnabled();
	}
	/**
	 * Is generating messages at the INFO level enabled.
	 */
	public boolean isInfoEnabled(Marker arg0) {
		return logger.isInfoEnabled(arg0);
	}
	/**
	 * Is generating messages at the TRACE level enabled.
	 */
	public boolean isTraceEnabled() {
		return logger.isTraceEnabled();
	}
	/**
	 * Is generating messages at the TRACE level enabled.
	 */
	public boolean isTraceEnabled(Marker arg0) {
		return logger.isTraceEnabled(arg0);
	}
	/**
	 * Is generating messages at the WARN level enabled.
	 */
	public boolean isWarnEnabled() {
		return logger.isWarnEnabled();
	}
	/**
	 * Is generating messages at the WARN level enabled.
	 */
	public boolean isWarnEnabled(Marker arg0) {
		return logger.isWarnEnabled(arg0);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(String arg0) {
		logger.trace(arg0);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(String arg0, Object arg1) {
		logger.trace(arg0, arg1);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(String arg0, Object[] arg1) {
		logger.trace(arg0, arg1);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(String arg0, Throwable arg1) {
		logger.trace(arg0, arg1);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(Marker arg0, String arg1) {
		logger.trace(arg0, arg1);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(String arg0, Object arg1, Object arg2) {
		logger.trace(arg0, arg1);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(Marker arg0, String arg1, Object arg2) {
		logger.trace(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(Marker arg0, String arg1, Object[] arg2) {
		logger.trace(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(Marker arg0, String arg1, Throwable arg2) {
		logger.trace(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the TRACE level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void trace(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.trace(arg0, arg1, arg2, arg3);
	}
	/**
	 * Log a message at the WARN level.
	 *  
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(String arg0) {
		logger.warn(arg0);
	}
	/**
	 * Log a message at the WARN level.
	 *  
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(String arg0, Object arg1) {
		logger.warn(arg0, arg1);
	}
	/**
	 * Log a message at the WARN level.
	 *  
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(String arg0, Object[] arg1) {
		logger.warn(arg0, arg1);
	}
	/**
	 * Log a message at the WARN level.
	 */
	public void warn(String arg0, Throwable arg1) {
		logger.warn(arg0, arg1);
	}
	/**
	 * Log a message at the WARN level.
	 *  
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(Marker arg0, String arg1) {
		logger.warn(arg0, arg1);
	}
	/**
	 * Log a message at the WARN level. 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(String arg0, Object arg1, Object arg2) {
		logger.warn(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the WARN level.
	 */
	public void warn(Marker arg0, String arg1, Object arg2) {
		logger.warn(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the WARN level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(Marker arg0, String arg1, Object[] arg2) {
		logger.warn(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the WARN level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(Marker arg0, String arg1, Throwable arg2) {
		logger.warn(arg0, arg1, arg2);
	}
	/**
	 * Log a message at the WARN level.
	 * 
	 * <p>Passes message to {@link #logger}.</p>
	 */
	public void warn(Marker arg0, String arg1, Object arg2, Object arg3) {
		logger.warn(arg0, arg1, arg2, arg3);
	}
}
