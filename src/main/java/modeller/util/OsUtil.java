package modeller.util;

/**
 * Created by IntelliJ IDEA.
 * User: andrew
 * Date: Jan 26, 2011
 * Time: 6:13:16 PM
 * To change this template use File | Settings | File Templates.
 */

public class OsUtil {

    public static boolean isMacOs() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public static boolean isWindowsOs() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}

