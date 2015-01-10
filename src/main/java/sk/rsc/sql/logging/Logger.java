package sk.rsc.sql.logging;

/**
 * Logger.
 *
 * @author Radovan Sninsky
 * @since 2014-04-27 20:59
 */
public interface Logger {

  void log(boolean important, String message);
}
