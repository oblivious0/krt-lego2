package krt.wid.bit;

import android.util.Log;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.LogUtils;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * @author: MaGua
 * @create_on:2021/9/13 15:26
 * @description
 */
public class EncryptUtil {

    private static final String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCADHVANwxV6eKLJUbJiJ/yiEerMKljBJnvc+Tt1JVh0lGFw/+o1no9h0Ld0folgsVlO45FtSGEGSrhI4R2LbHhHg6KZACL/8ACUvPS2Rqn5M/hlAp9CxP3gfxFN8Kb67WUZkntuhQDgzkndNHVxi6FyG5iScnikrgpHwOF4YdIowIDAQAB";

    public static String getAes16LenK() {
        String result = "";
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(64);
            SecretKey sk = keyGenerator.generateKey();
            byte[] b = sk.getEncoded();
            result = ConvertUtils.bytes2HexString(b);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String encryptAES2HexString(String data, String aes) {
        return EncryptUtils.encryptAES2HexString(data.getBytes(), aes.getBytes(), "AES/ECB/PKCS5Padding", null);
    }

    public static String encryptRSAHexString(String data) {
        RSA rsa = SecureUtil.rsa(null,key);
        byte[] code = rsa.encrypt(data.getBytes(), KeyType.PublicKey);
        return ConvertUtils.bytes2HexString(code);
//        return EncryptUtils.encryptRSA2HexString(data.getBytes(), key.getBytes(), 2048, "RSA/ECB/PKCS1Padding");
    }

}
