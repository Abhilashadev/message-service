package com.ascent.message.messageservice.util;

import com.ascent.message.messageservice.model.Queue;

/**
 * @author abhilasha
 * @since 28/11/19
 */

public class CommonUtil {
    public static boolean isNullOrEmpty(Object object) {
        if(object == null) {
            return true;
        }else if(object.toString().length() == 0) {
            return true;
        }
        return false;
    }

    public static String getStringFromByte(byte[] payload){
        return new String(payload);
    }

    public static  byte[] convertToByteArray(String payload){
        return payload.getBytes();
    }

    public static int messageLength(String payload){
        return payload.getBytes().length;
    }
}
