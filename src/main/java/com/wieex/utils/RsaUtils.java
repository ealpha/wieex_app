package com.wieex.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class RsaUtils {

    private static final String PRIVATE_KEY_PEM = "-----BEGIN PRIVATE KEY-----\n" +
            "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDKDXTDjETn3yFa\n" +
            "OrMDdgFEe7wTGiNQyPvJYSWER+hHaTZP36+QeY8dWN5rRfQQX1zO8T+g5fiBCB7X\n" +
            "dSJC9K2/HbQc3l2+dUmALeY7Azto9NQOboquRRdSFUCUikcDfb7U1Jldl5QMu/py\n" +
            "F089ZO1YKCt20gPE0KIGrWaZ6jveb6ydZa+tHI8VYo0kRLKN9Fox+1I56oAD05J5\n" +
            "cDJpSez2LTy/2IEk6tSNWU5j9mYv3JmOz2Ue6xlhBnSQksoCs2TDquZvJLa1bNHX\n" +
            "6MZCQKUuHkUZjAr9mIG7193qwu3BVdOdwgqqHyvYlCPa2+uXF1hMnv+CY9RypfqY\n" +
            "GXo+35oDAgMBAAECgf9F7cm69h1PnEhXgZR1yNnQ02mf/ImZjF07GG08zg2xP19x\n" +
            "kwdvgPBTcgrd15dzeASyiIFXjMu5cAKmnAkPu9qFS9hzDhEJPU3e5EcRfW3YLIGa\n" +
            "KlKcCs7KqBGb01HK52jQZvfO/qW0XbfK2XodmDlgxD3polawwNmqvltTuyZaYgjM\n" +
            "l9RpEIHNB6T44IOorwPi69fnmGN+MDReNhCDPRU+HkN37y6DAcRIpY9F11f4AIFj\n" +
            "oGppW2wT+mlFbZ68qAbLVQsXsMh8wx3ONDt3F4JKqjuU8RZJSfROnKhReGb52b/v\n" +
            "wZQ1Z5Fb9THBKOHzZGwr++x9O80wLFDW96L38TUCgYEA8xbvEkmF9PvryWP8uUtf\n" +
            "9Viw1hU6gbRhzDoLDQauzh4Kgi/TD1jV+WTPrazKyYEYgJtBeGF/CsQO3+B2w4yA\n" +
            "/UjmIPOEVIhVCn2lhk71UhYe+89cRXH+vaA3H72o29F24TKEVro3MRHs64QzGLbt\n" +
            "Noifz3eyjvXYDWAXn3wSSP8CgYEA1MiUWNn+KCtKsS7raEtclaJUJxMXQBZppQ/G\n" +
            "QFGTddPGWoLIeuJB5tqG5iHO/cM7uuyMOg7+xWYuoFtLKtw/Z2OFoYokSAmLn03s\n" +
            "3ljMcVD19sjbo2knzVwVCE35iIbRKUooJgIMfpazVnjOD+PDQSi5Dl21QvwVGTU7\n" +
            "2yaMiv0CgYEAg9uz0IqbJBkme/ZFlskBAxeZzHZ1ZvfOeLYfWnB+j4WSE64XYWLB\n" +
            "pmb9k+p/kS/6d4A/0imofNF+dAfQxB/JtBo/4i3VYPkWj/s5txHLVjxzkAmZtn2w\n" +
            "PXHTlN8O4jXLsznwuQ6Hc5GSnun1wXkwUP1pGmOGMq++AvtVtQfa70cCgYBgEmgY\n" +
            "beR5EoLY2QxkvJIC5ZYFRnNa9kOgySyD4dEohAOs3hvSj9Gdf9EkmuMwZDPVw/PU\n" +
            "SuzxB5/Juy6HHYZrEd5F/28+L0EhHjfydDlqeFl1oV65u0LDT+4XH82ovOY3NEpI\n" +
            "TuiBR12Vj1nKhtWj19UmDV1H7zU/em+aFR+jqQKBgQCkUjA0e26VtzqDa2hb4gTt\n" +
            "RqqqmW+W7bGVhIB9KnkhHNeTC5iWrV9WdR4gkCFCadVTdjjUy59HTt5cnVQj/Z1j\n" +
            "eveQEX8szQZaQd4ySy92ojQk+h0ArtMPIf7wjbvI7FIB/j14F5Df4NwnfivFcQvf\n" +
            "0pITrvntSDkzpvWGqQBSgw==\n" +
            "-----END PRIVATE KEY-----";

    private static final String PUBLIC_KEY_PEM = "-----BEGIN PUBLIC KEY-----\n" +
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyg10w4xE598hWjqzA3YB\n" +
            "RHu8ExojUMj7yWElhEfoR2k2T9+vkHmPHVjea0X0EF9czvE/oOX4gQge13UiQvSt\n" +
            "vx20HN5dvnVJgC3mOwM7aPTUDm6KrkUXUhVAlIpHA32+1NSZXZeUDLv6chdPPWTt\n" +
            "WCgrdtIDxNCiBq1mmeo73m+snWWvrRyPFWKNJESyjfRaMftSOeqAA9OSeXAyaUns\n" +
            "9i08v9iBJOrUjVlOY/ZmL9yZjs9lHusZYQZ0kJLKArNkw6rmbyS2tWzR1+jGQkCl\n" +
            "Lh5FGYwK/ZiBu9fd6sLtwVXTncIKqh8r2JQj2tvrlxdYTJ7/gmPUcqX6mBl6Pt+a\n" +
            "AwIDAQAB\n" +
            "-----END PUBLIC KEY-----";

    public static PrivateKey loadPrivateKey() throws Exception {
        String key = PRIVATE_KEY_PEM
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

    public static PublicKey loadPublicKey() throws Exception {
        String key = PUBLIC_KEY_PEM
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decoded = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(decoded);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    public static String encrypt(String content, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encrypted = cipher.doFinal(content.getBytes());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    public static String decrypt(String encrypted, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encrypted));
        return new String(decrypted);
    }
}
