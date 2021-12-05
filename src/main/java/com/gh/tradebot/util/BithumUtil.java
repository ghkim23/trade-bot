package com.gh.tradebot.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class BithumUtil {

    public static MultiValueMap<String, String> getHttpHeaders(String endpoint, MultiValueMap<String, String> body, String apiKey, String secret) throws Exception{

        String strData = mapToQueryString(body).replace("?", "");
        String nNonce = String.valueOf(System.currentTimeMillis());

        strData = strData.substring(0, strData.length()-1);
        strData = encodeURIComponent(strData);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        String str = endpoint + ";"	+ strData + ";" + nNonce;
        String encoded = hmacSha512(str, secret);

        map.add("Api-Key", apiKey);
        map.add("Api-Sign", encoded);
        map.add("api-client-type", "2");
        map.add("Api-Nonce", nNonce);

        return map;

    }


    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String HMAC_SHA512 = "HmacSHA512";

    public static String hmacSha512(String data, String secret) throws Exception{
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(DEFAULT_ENCODING), HMAC_SHA512);
        Mac mac = Mac.getInstance(HMAC_SHA512);
        mac.init(secretKey);

        byte[] hash = mac.doFinal(data.getBytes());
        byte[] hex = new Hex().encode(hash);

        return new String(Base64.encodeBase64(hex));
    }

    public static String mapToQueryString(MultiValueMap<String, String> map) {
        StringBuilder string = new StringBuilder();

        if (map.size() > 0) {
            string.append("?");
        }
        map.toSingleValueMap().forEach((key,value) -> {
            string.append(key);
            string.append("=");
            string.append(value);
            string.append("&");
        });

        return string.toString();
    }

    public static String encodeURIComponent(String s) {
        String result = null;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%26", "&")
                    .replaceAll("\\%3D", "=")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }
}
