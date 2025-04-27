package com.wieex.utils;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class PawpawSNPayload {
    private static final SecureRandom random = new SecureRandom();
    private static final byte[] AIZIP_MARKER = "AIZIP".getBytes(StandardCharsets.UTF_8);
    private static final int AIZIP_LENGTH = AIZIP_MARKER.length;
    private static final int MAX_PAYLOAD_LENGTH = 256;
    private static final int FIXED_HEADER_LENGTH = 48; // ChipId(16) + FlashId(32)
    private static final int SN_OFFSET_LENGTH = 1;

    public static byte[] generatePayload(String sn, String chipId, String flashId) {
        // 转换为 bytes
        byte[] chipIdBytes = chipId.getBytes(StandardCharsets.UTF_8);
        byte[] flashIdBytes = flashId.getBytes(StandardCharsets.UTF_8);
        byte[] snBytes = sn.getBytes(StandardCharsets.UTF_8);

        // 检查长度（确保符合预期）
        if (chipIdBytes.length != 16 || flashIdBytes.length != 32 || snBytes.length != 32) {
            throw new IllegalArgumentException("Invalid input lengths");
        }

        // SN 数据块
        byte[] snBlock = new byte[snBytes.length + 2];
        snBlock[0] = 'S';
        snBlock[1] = (byte) snBytes.length;
        System.arraycopy(snBytes, 0, snBlock, 2, snBytes.length);

        System.out.println("SN 数据块长度：" + snBlock.length);

        // 固定头部：ChipId + FlashId（总 48 字节）
        byte[] fixedPart = new byte[FIXED_HEADER_LENGTH];
        System.arraycopy(chipIdBytes, 0, fixedPart, 0, chipIdBytes.length);
        System.arraycopy(flashIdBytes, 0, fixedPart, chipIdBytes.length, flashIdBytes.length);

        // 计算可用空间
        int availableSpace = MAX_PAYLOAD_LENGTH - FIXED_HEADER_LENGTH - SN_OFFSET_LENGTH - AIZIP_LENGTH;



        // 确保 SN 数据块不会超出可用空间
        if (snBlock.length > availableSpace) {
            throw new IllegalArgumentException("SN data block is too large for the available space");
        }

        // 确定 SN 数据在 payload 中的偏移位置
        // 确保 SN 数据块的结束位置不会超出 payload 的最大长度
        int maxPossibleOffset = MAX_PAYLOAD_LENGTH - snBlock.length;
        int minPossibleOffset = FIXED_HEADER_LENGTH + SN_OFFSET_LENGTH + AIZIP_LENGTH;
        
        if (minPossibleOffset >= maxPossibleOffset) {
            throw new IllegalArgumentException("No available space for SN data block");
        }
        
        int snOffset = minPossibleOffset + random.nextInt(maxPossibleOffset - minPossibleOffset);

        // 创建随机内容的初始 payload
        byte[] body = new byte[availableSpace];
        random.nextBytes(body);

        // 插入 SN 数据
        System.arraycopy(snBlock, 0, body, snOffset - FIXED_HEADER_LENGTH - SN_OFFSET_LENGTH - AIZIP_LENGTH, snBlock.length);

        // 组合 payload
        byte[] payload = new byte[MAX_PAYLOAD_LENGTH];
        // 第一个字节放 snOffset
        payload[0] = (byte) snOffset;
        // 然后是 AIZIP 标记
        System.arraycopy(AIZIP_MARKER, 0, payload, SN_OFFSET_LENGTH, AIZIP_LENGTH);
        // 然后是固定头部
        System.arraycopy(fixedPart, 0, payload, SN_OFFSET_LENGTH + AIZIP_LENGTH, fixedPart.length);
        // 最后是随机内容和 SN 数据
        System.arraycopy(body, 0, payload, SN_OFFSET_LENGTH + AIZIP_LENGTH + fixedPart.length, body.length);

        return payload;
    }

    public static class ExtractedData {
        public final String sn;
        public final String chipId;
        public final String flashId;

        public ExtractedData(String sn, String chipId, String flashId) {
            this.sn = sn;
            this.chipId = chipId;
            this.flashId = flashId;
        }
    }

    public static ExtractedData extractData(byte[] payload) {
        if (payload.length != MAX_PAYLOAD_LENGTH) {
            throw new IllegalArgumentException("Invalid payload length");
        }

        // 从第一个字节获取 snOffset
        int snOffset = payload[0] & 0xFF;
        
        // 验证 AIZIP 标记
        byte[] aizipMarker = Arrays.copyOfRange(payload, SN_OFFSET_LENGTH, SN_OFFSET_LENGTH + AIZIP_LENGTH);
        if (!Arrays.equals(aizipMarker, AIZIP_MARKER)) {
            throw new IllegalArgumentException("Invalid AIZIP marker");
        }
        
        // 提取固定头部
        String chipId = new String(Arrays.copyOfRange(payload, SN_OFFSET_LENGTH + AIZIP_LENGTH, 
            SN_OFFSET_LENGTH + AIZIP_LENGTH + 16), StandardCharsets.UTF_8);
        String flashId = new String(Arrays.copyOfRange(payload, SN_OFFSET_LENGTH + AIZIP_LENGTH + 16, 
            SN_OFFSET_LENGTH + AIZIP_LENGTH + 48), StandardCharsets.UTF_8);

        String sn = null;
        if (payload[snOffset] == 'S') {
            int length = payload[snOffset + 1] & 0xFF;
            sn = new String(Arrays.copyOfRange(payload, snOffset + 2, snOffset + 2 + length), StandardCharsets.UTF_8);
        }

        return new ExtractedData(sn, chipId, flashId);
    }

    /**
     * 将 byte[] 转换为十六进制字符串
     * @param bytes 要转换的字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    /**
     * 将十六进制字符串转换为 byte[]
     * @param hexString 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return data;
    }

    public static void main(String[] args) {
        String sn = "04446bb53755beabf1836eae635e4444";
        String chipId = "90465834153CE90A";
        String flashId = "000000005136363633159d4c45ffffff";

        System.out.println("输入数据：");
        System.out.println("SN: " + sn);
        System.out.println("ChipId: " + chipId);
        System.out.println("FlashId: " + flashId);
        System.out.println();

        // 生成 payload
        byte[] payload = generatePayload(sn, chipId, flashId);
        
        // 将 payload 转换为十六进制字符串
        String payloadHex = bytesToHexString(payload);
        System.out.println("Payload: " + payloadHex);
        System.out.println("Payload长度: " + payload.length);
        
        // 打印数据分布
        int snOffset = payload[0] & 0xFF;
        System.out.println("\n数据分布：");
        System.out.println("0x00: snOffset = " + snOffset);
        System.out.println("0x01-0x05: AIZIP标记 = " + new String(Arrays.copyOfRange(payload, 1, 6), StandardCharsets.UTF_8));
        System.out.println("0x06-0x15: ChipId = " + new String(Arrays.copyOfRange(payload, 6, 22), StandardCharsets.UTF_8));
        System.out.println("0x16-0x35: FlashId = " + new String(Arrays.copyOfRange(payload, 22, 54), StandardCharsets.UTF_8));
        System.out.println("0x" + String.format("%02x", snOffset) + ": SN数据块起始位置");
        if (payload[snOffset] == 'S') {
            int snLength = payload[snOffset + 1] & 0xFF;
            System.out.println("0x" + String.format("%02x", snOffset) + "-0x" + 
                String.format("%02x", snOffset + 1 + snLength) + ": SN数据 = " + 
                new String(Arrays.copyOfRange(payload, snOffset + 2, snOffset + 2 + snLength), StandardCharsets.UTF_8));
        }
        System.out.println();

        // 演示从十六进制字符串恢复 payload
        byte[] restoredPayload = hexStringToBytes(payloadHex);
        System.out.println("从十六进制字符串恢复的 Payload 长度: " + restoredPayload.length);
        System.out.println("恢复的 Payload 是否与原 Payload 相同: " + Arrays.equals(payload, restoredPayload));
        System.out.println();

        // 提取数据
        ExtractedData extracted = extractData(payload);
        System.out.println("提取结果：");
        System.out.println("SN: " + extracted.sn);
        System.out.println("ChipId: " + extracted.chipId);
        System.out.println("FlashId: " + extracted.flashId);
        System.out.println();

        // 验证结果
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder("验证失败：\n");

        if (!sn.equals(extracted.sn)) {
            isValid = false;
            errorMessage.append("- SN 不匹配\n");
            errorMessage.append("  期望: ").append(sn).append("\n");
            errorMessage.append("  实际: ").append(extracted.sn).append("\n");
        }

        if (!chipId.equals(extracted.chipId)) {
            isValid = false;
            errorMessage.append("- ChipId 不匹配\n");
            errorMessage.append("  期望: ").append(chipId).append("\n");
            errorMessage.append("  实际: ").append(extracted.chipId).append("\n");
        }

        if (!flashId.equals(extracted.flashId)) {
            isValid = false;
            errorMessage.append("- FlashId 不匹配\n");
            errorMessage.append("  期望: ").append(flashId).append("\n");
            errorMessage.append("  实际: ").append(extracted.flashId).append("\n");
        }

        // 验证 AIZIP 标记
        byte[] aizipMarker = Arrays.copyOfRange(payload, SN_OFFSET_LENGTH, SN_OFFSET_LENGTH + AIZIP_LENGTH);
        if (!Arrays.equals(aizipMarker, AIZIP_MARKER)) {
            isValid = false;
            errorMessage.append("- AIZIP 标记不匹配\n");
            errorMessage.append("  期望: ").append(new String(AIZIP_MARKER, StandardCharsets.UTF_8)).append("\n");
            errorMessage.append("  实际: ").append(new String(aizipMarker, StandardCharsets.UTF_8)).append("\n");
        }

        // 验证 SN 数据块
        if (payload[snOffset] != 'S') {
            isValid = false;
            errorMessage.append("- SN 数据块标记 'S' 不存在\n");
        }

        if (isValid) {
            System.out.println("验证通过：所有数据匹配正确");
        } else {
            System.out.println(errorMessage.toString());
        }
    }
} 