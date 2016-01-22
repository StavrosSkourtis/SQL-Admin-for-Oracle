package net.skourtis.sqladmin.utils;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Stavros
 */
public class StringUtils {
    /**
     * Utility method , converts a byte array to a string containing the hex
     * values of that array
     *
     * @param bytes the array of bytes
     * @return a string containing hex numbers
     */
    public static String getHex(byte[] bytes) {
        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < bytes.length; i++) {
            buffer.append(Integer.toHexString(0xFF & bytes[i]));
        }

        return buffer.toString();
    }
}
