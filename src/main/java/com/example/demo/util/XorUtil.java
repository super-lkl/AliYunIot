package com.example.demo.util;

/**
 * @author 李康龙
 */
public class XorUtil {

    public static int getXor(byte[] buffer){
        int result = 0;
        for (int i = 1 ; i < buffer.length-1; i++) {
                result^= buffer[i];
        }
        String hexString = Integer.toHexString(result);
        if(hexString.length()>1){
            hexString = hexString.substring(hexString.length()-2);
        }
        return Integer.parseInt(hexString,16);
    }
}
