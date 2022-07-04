package com.hj.platform.common.util;

import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Slf4j
public class Md5Utils {

    protected Md5Utils() throws InstantiationException {}

    public static String md5(String source) {
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] bytes = Base64.getEncoder().encode(source.getBytes(StandardCharsets.UTF_8));
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            md.update(bytes);
            md.update("I'm NaCl".getBytes(StandardCharsets.UTF_8));
            byte[] result = md.digest();
            return new String(encoder.encode(result));
        } catch (NoSuchAlgorithmException e) {
            throw new Md5EncodeException(e);
        }
    }

    static class Md5EncodeException extends RuntimeException{
        public Md5EncodeException(Throwable cause) {
            super(cause);
        }
    }
}
