package sk.rsc.sql.logging;

/**
 * NoLogger logs no messages. This logger is det as default logger implementation.
 *
 * @author Radovan Sninsky
 * @since 2014-04-27 21:04
 * @see Logger for interface definition
 */
public class NoLogger implements Logger {

  @Override
  public void log(boolean important, String message) {
  }
}
