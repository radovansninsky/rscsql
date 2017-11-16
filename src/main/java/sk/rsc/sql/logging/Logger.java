package sk.rsc.sql.logging;

/**
 * Logger interface used by library clasess for emitting log messages.
 * <br>
 * There are two level of messages, standard level and important level.
 * Handling of levels is based on logging implementation.
 * See logging initialization {@link sk.rsc.sql.Sql#setCustomLogger}.
 *
 * @author Radovan Sninsky
 * @since 2014-04-27 20:59
 */
public interface Logger {

  /**
   * Logs message based of logging mechanism and importance of message.
   *
   * @param important true if message is important, otherwise false
   * @param message log message
   */
  void log(boolean important, String message);
}
