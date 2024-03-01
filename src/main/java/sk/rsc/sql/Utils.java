package sk.rsc.sql;

/**
 * Utils.
 *
 * @author Radovan Sninsky
 * @since 2017-10-11 15:59
 */
final class Utils {

  public static String makeAliasIfDotField(String column) {
    if (column.contains(".")) {
//      int i = column.indexOf(".");
//      return column.substring(0, i) + "$_$" + column.substring(i+1);
      return column.replace(".", "$_$");
    }
    return column;
  }

}
