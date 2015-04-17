package sk.rsc.sql.logging;

/**
 * SoutLogger logs messages into standard console output.
 * <br/>
 * There is no difference between levels. Log message looks:
 * <br/>
 * <pre>
 *   Executing SQL:
 *   &lt;orginal message&gt;
 * </pre>
 *
 * @author Radovan Sninsky
 * @since 2014-04-27 21:09
 * @see Logger for interface definition
 */
public class SoutLogger implements Logger {

  @Override
  public void log(boolean important, String message) {
    System.out.println("Executing SQL:\n"+message);
  }
}
