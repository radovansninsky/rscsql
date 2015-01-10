package sk.rsc.sql.logging;

/**
 * NullLogger.
 *
 * @author Radovan Sninsky
 * @since 2014-04-27 21:04
 */
public class NullLogger implements Logger {

  @Override
  public void log(boolean important, String message) {
  }
}
