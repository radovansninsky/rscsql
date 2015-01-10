package sk.rsc.sql.logging;

/**
 * SoutLogger.
 *
 * @author Radovan Sninsky
 * @since 2014-04-27 21:09
 */
public class SoutLogger implements Logger {

  @Override
  public void log(boolean important, String message) {
    System.out.println("Executing SQL:\n"+message);
  }
}
