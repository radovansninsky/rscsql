package sk.rsc.sql.fields;

import java.time.Instant;
import java.util.Date;

/**
 * Fields factory.
 *
 * @author Radovan Sninsky
 * @since 2026-03-21
 */
public final class Fields {
  public static Field create(String field, Object value) {
    if (value instanceof Date) {
      return new DateField(field, (Date) value);
    } else if (value instanceof Enum<?>) {
      return new EnumField(field, (Enum<?>)value);
    } else if (value instanceof Instant) {
      return new InstantField(field, (Instant)value);
    }
    return new Field(field, value);
  }
}
