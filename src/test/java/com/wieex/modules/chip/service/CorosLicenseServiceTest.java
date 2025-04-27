package com.wieex.modules.chip.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CorosLicenseServiceTest {

    private static CorosLicenseService corosLicenseService;

    @BeforeAll
    public static void setup() {
        corosLicenseService = new CorosLicenseService();
    }

    @Test
    public void testGenerateAndVerifyAndroidLicense() throws Exception {
        System.out.println("开始测试Android许可证生成和验证");
        
        String appId = "com.yf.smart.coros.dist";
        String platform = "android";
        
        String license = corosLicenseService.generateLicense(appId, platform);
        System.out.println("生成的Android许可证: " + license);
        assertNotNull(license);
        assertFalse(license.isEmpty());
        
        String result = corosLicenseService.verifyLicense(license);
        System.out.println("Android许可证验证结果: " + result);
        assertEquals("License valid", result);
        
        System.out.println("Android许可证测试完成");
    }

    @Test
    public void testGenerateAndVerifyIosLicense() throws Exception {
        System.out.println("开始测试iOS许可证生成和验证");
        
        String appId = "com.coros.coros";
        String platform = "ios";
        
        String license = corosLicenseService.generateLicense(appId, platform);
        System.out.println("生成的iOS许可证: " + license);
        assertNotNull(license);
        assertFalse(license.isEmpty());
        
        String result = corosLicenseService.verifyLicense(license);
        System.out.println("iOS许可证验证结果: " + result);
        assertEquals("License valid", result);
        
        System.out.println("iOS许可证测试完成");
    }

    @Test
    public void testInvalidAppId() {
        System.out.println("开始测试无效应用ID");
        
        String invalidAppId = "com.invalid.app";
        String platform = "android";
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            corosLicenseService.generateLicense(invalidAppId, platform);
        });
        System.out.println("无效应用ID测试结果: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Unauthorized Android package name"));
        
        System.out.println("无效应用ID测试完成");
    }

    @Test
    public void testInvalidPlatform() {
        System.out.println("开始测试无效平台类型");
        
        String appId = "com.yf.smart.coros.dist";
        String invalidPlatform = "windows";
        
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            corosLicenseService.generateLicense(appId, invalidPlatform);
        });
        System.out.println("无效平台类型测试结果: " + exception.getMessage());
        assertTrue(exception.getMessage().contains("Invalid platform"));
        
        System.out.println("无效平台类型测试完成");
    }

    @Test
    public void testInvalidLicense() throws Exception {
        System.out.println("开始测试无效许可证");
        
        // 使用一个有效的Base64格式字符串，但内容不是有效的RSA加密数据
        String invalidLicense = "aGVsbG8gd29ybGQ="; // Base64编码的"hello world"
        
        try {
            String result = corosLicenseService.verifyLicense(invalidLicense);
            System.out.println("无效许可证验证结果: " + result);
            assertNotEquals("License valid", result);
        } catch (Exception e) {
            System.out.println("无效许可证测试异常: " + e.getMessage());
            // 预期会抛出异常，因为解密失败
            assertTrue(e.getMessage().contains("Decryption error"));
        }
        
        System.out.println("无效许可证测试完成");
    }

    public static void main(String[] args) {
        System.out.println("开始运行许可证测试");
        
        try {
            CorosLicenseServiceTest test = new CorosLicenseServiceTest();
            test.setup();
            
            // 运行所有测试
            test.testGenerateAndVerifyAndroidLicense();
            test.testGenerateAndVerifyIosLicense();
            test.testInvalidAppId();
            test.testInvalidPlatform();
            test.testInvalidLicense();
            
            System.out.println("所有测试运行完成");
        } catch (Exception e) {
            System.out.println("测试运行失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 