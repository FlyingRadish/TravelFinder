package org.liangxw.travelfinder.util;

/**
 * Created by houxg on 2015/4/10.
 */
public class CheckTool {

    public static final int RESULT_OK = 0;
    public static final int MASK_EMPTY = 1;

    public static int check(String string, int mask) {
        if (string == null || string.equals("")) {
            return MASK_EMPTY;
        }
        return RESULT_OK;
    }

    public static int check(String[] string, int mask) {
        for (String text : string) {
            if ((mask & MASK_EMPTY) != 0 && (text == null || text.equals(""))) {
                return MASK_EMPTY;
            }
        }
        return RESULT_OK;
    }


}
