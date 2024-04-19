package com.wieex.utils;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;

public class AesEcbCodec {

    /**
     * <tt>Java</tt> 支持 <tt>PKCS5Padding</tt> 填充方式 <br>
     * <tt>BouncyCastle</tt> 支持 <tt>PKCS7Padding</tt> 填充方式
     */
    private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

    private static final String ALGORITHM = "AES";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * 加密数据
     *
     * @param data 待加密数据
     * @param key  密钥
     * @return 加密后的数据
     * @throws GeneralSecurityException
     */
    public static byte[] encrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
        // 初始化Cipher对象，设置为加密模式
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        // 执行加密操作。加密后的结果通常都会用Base64编码进行传输
        return cipher.doFinal(data);
    }

    /**
     * 加密数据 (charset="UTF-8")
     *
     * @param data      待加密数据
     * @param base64Key 密钥 (base64)
     * @return 加密后的数据 (base64)
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String encrypt_base64(String data, String base64Key)
            throws GeneralSecurityException, IOException {
        return encrypt_base64(data, base64Key, "UTF-8");
    }

    /**
     * 加密数据 (charset="UTF-8")
     *
     * @param data   待加密数据
     * @param hexKey 密钥 (hex)
     * @return 加密后的数据 (hex)
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String encrypt_hex(String data, String hexKey)
            throws GeneralSecurityException, IOException {
        return encrypt_hex(data, hexKey, "UTF-8");
    }

    /**
     * 加密数据
     *
     * @param data      待加密数据
     * @param base64Key 密钥 (base64)
     * @param charset   字符集编码
     * @return 加密后的数据 (base64)
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String encrypt_base64(String data, String base64Key, String charset)
            throws GeneralSecurityException, IOException {
        byte[] target = encrypt(data.getBytes(charset), decodeBase64(base64Key));
        return encodeBase64String(target);
    }

    /**
     * 加密数据
     *
     * @param data    待加密数据
     * @param hexKey  密钥 (hex)
     * @param charset 字符集编码
     * @return 加密后的数据 (hex)
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String encrypt_hex(String data, String hexKey, String charset)
            throws GeneralSecurityException, IOException {
        byte[] target = encrypt(data.getBytes(charset), decodeHex(hexKey));
        return encodeHexString(target);
    }

    /**
     * 解密数据
     *
     * @param data 待解密数据
     * @param key  密钥
     * @return 解密后的数据
     * @throws GeneralSecurityException
     */
    public static byte[] decrypt(byte[] data, byte[] key) throws GeneralSecurityException {
        // 实例化Cipher对象，它用于完成实际的加密操作
        Cipher cipher = Cipher.getInstance(TRANSFORMATION, BouncyCastleProvider.PROVIDER_NAME);
        // 初始化Cipher对象，设置为解密模式
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ALGORITHM));
        // 执行解密操作
        return cipher.doFinal(data);
    }

    /**
     * 解密数据 (charset="UTF-8")
     *
     * @param base64Data 待解密数据 (base64)
     * @param base64Key  密钥
     * @return 解密后的数据
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decrypt_base64(String base64Data, String base64Key)
            throws GeneralSecurityException, IOException {
        return decrypt_base64(base64Data, base64Key, "UTF-8");
    }

    /**
     * 解密数据 (charset="UTF-8")
     *
     * @param hexData 待解密数据 (hex)
     * @param hexKey  密钥 (hex)
     * @return 解密后的数据
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decrypt_hex(String hexData, String hexKey)
            throws GeneralSecurityException, IOException {
        return decrypt_hex(hexData, hexKey, "UTF-8");
    }

    /**
     * 解密数据
     *
     * @param base64Data 待解密数据 (base64)
     * @param base64Key  密钥 (base64)
     * @param charset    字符集编码
     * @return 解密后的数据
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decrypt_base64(String base64Data, String base64Key, String charset)
            throws GeneralSecurityException, IOException {
        byte[] encrypt = decrypt(decodeBase64(base64Data), decodeBase64(base64Key));
        return new String(encrypt, charset);
    }

    /**
     * 解密数据
     *
     * @param hexData 待解密数据 (hex)
     * @param hexKey  密钥 (hex)
     * @param charset 字符集编码
     * @return 解密后的数据
     * @throws GeneralSecurityException
     * @throws IOException
     */
    public static String decrypt_hex(String hexData, String hexKey, String charset)
            throws GeneralSecurityException, IOException {
        byte[] encrypt = decrypt(decodeHex(hexData), decodeHex(hexKey));
        return new String(encrypt, charset);
    }

    // ============================================================================================

    /**
     * Converts a String representing hexadecimal values into an array of bytes of those same values. The
     * returned array will be half the length of the passed String, as it takes two characters to represent any given
     * byte. An exception is thrown if the passed String has an odd number of elements.
     *
     * @param data A String containing hexadecimal digits
     * @return A byte array containing binary data decoded from the supplied char array.
     * @throws IOException Thrown if an odd number or illegal of characters is supplied
     */
    public static byte[] decodeHex(String data) throws IOException {
        try {
            return Hex.decodeHex(data);
        } catch (DecoderException e) {
            throw new IOException(e.getMessage(), e.getCause());
        }
    }

    /**
     * Converts an array of bytes into a String representing the hexadecimal values of each byte in order. The returned
     * String will be double the length of the passed array, as it takes two characters to represent any given byte.
     *
     * @param data a byte[] to convert to Hex characters
     * @return A String containing upper-case hexadecimal characters
     */
    public static String encodeHexString(byte[] data) {
        return Hex.encodeHexString(data, false);
    }

    /**
     * Decodes a Base64 String into octets.
     * <p>
     * <b>Note:</b> this method seamlessly handles data encoded in URL-safe or normal mode.
     * </p>
     *
     * @param data String containing Base64 data
     * @return Array containing decoded data.
     */
    public static byte[] decodeBase64(String data) {
        return Base64.decodeBase64(data);
    }

    /**
     * Encodes binary data using the base64 algorithm but does not chunk the output.
     * <p>
     * NOTE:  We changed the behaviour of this method from multi-line chunking (commons-codec-1.4) to
     * single-line non-chunking (commons-codec-1.5).
     *
     * @param data binary data to encode
     * @return String containing Base64 characters.
     */
    public static String encodeBase64String(byte[] data) {
        return Base64.encodeBase64String(data);
    }

    public static void main(String[] args) {
    }

}