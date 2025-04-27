package com.wieex.modules.chip.service;

import com.wieex.utils.RsaUtils;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class CorosLicenseService {

    private static final Set<String> ALLOWED_ANDROID_PACKAGES =
            Collections.unmodifiableSet(Collections.singleton("com.yf.smart.coros.dist"));
    private static final Set<String> ALLOWED_IOS_BUNDLES =
            Collections.unmodifiableSet(Collections.singleton("com.coros.coros"));
    private static final Set<String> ALLOWED_PLATFORMS;
    
    static {
        Set<String> platforms = new HashSet<>();
        platforms.add("android");
        platforms.add("ios");
        ALLOWED_PLATFORMS = Collections.unmodifiableSet(platforms);
    }

    public String generateLicense(String appId, String platform) throws Exception {
        System.out.println("开始生成许可证 - appId: " + appId + ", platform: " + platform);

        if (!ALLOWED_PLATFORMS.contains(platform.toLowerCase())) {
            System.out.println("无效的平台类型: " + platform);
            throw new IllegalArgumentException("Invalid platform: " + platform);
        }

        if (platform.equalsIgnoreCase("android") && !ALLOWED_ANDROID_PACKAGES.contains(appId)) {
            System.out.println("无效的Android应用ID: " + appId);
            throw new IllegalArgumentException("Unauthorized Android package name: " + appId);
        }
        if (platform.equalsIgnoreCase("ios") && !ALLOWED_IOS_BUNDLES.contains(appId)) {
            System.out.println("无效的iOS应用ID: " + appId);
            throw new IllegalArgumentException("Unauthorized iOS bundle ID: " + appId);
        }

        String expiry = getExpiryAfterDays(7);
        System.out.println("许可证过期时间: " + expiry);

        JSONObject json = new JSONObject();
        json.put("app_id", appId);
        json.put("platform", platform);
        json.put("expiry", expiry);

        PrivateKey privateKey = RsaUtils.loadPrivateKey();
        String encryptedLicense = RsaUtils.encrypt(json.toString(), privateKey);
        System.out.println("许可证生成成功 - appId: " + appId + ", platform: " + platform);

        return encryptedLicense;
    }

    public String verifyLicense(String encryptedLicense) throws Exception {
        System.out.println("开始验证许可证");
        
        PublicKey publicKey = RsaUtils.loadPublicKey();
        String decrypted = RsaUtils.decrypt(encryptedLicense, publicKey);
        System.out.println("解密后的许可证内容: " + decrypted);
        
        JSONObject json = new JSONObject(decrypted);
        String appId = json.getString("app_id");
        String platform = json.getString("platform");
        String expiry = json.getString("expiry");

        System.out.println("验证许可证信息 - appId: " + appId + ", platform: " + platform + ", expiry: " + expiry);

        if (!ALLOWED_PLATFORMS.contains(platform.toLowerCase())) {
            System.out.println("无效的平台类型: " + platform);
            return "Invalid platform";
        }

        if (platform.equalsIgnoreCase("android") && !ALLOWED_ANDROID_PACKAGES.contains(appId)) {
            System.out.println("无效的Android应用ID: " + appId);
            return "Invalid app ID for Android";
        }
        if (platform.equalsIgnoreCase("ios") && !ALLOWED_IOS_BUNDLES.contains(appId)) {
            System.out.println("无效的iOS应用ID: " + appId);
            return "Invalid app ID for iOS";
        }

        if (new Date().after(new SimpleDateFormat("yyyy-MM-dd").parse(expiry))) {
            System.out.println("许可证已过期 - expiry: " + expiry);
            return "License expired";
        }

        System.out.println("许可证验证通过 - appId: " + appId + ", platform: " + platform);
        return "License valid";
    }

    private String getExpiryAfterDays(int days) {
        long millis = System.currentTimeMillis() + days * 24L * 60 * 60 * 1000;
        return new SimpleDateFormat("yyyy-MM-dd").format(new Date(millis));
    }
}
