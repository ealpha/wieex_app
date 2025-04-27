import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;

public class PAWPAW_SN {
    private static final SecureRandom random = new SecureRandom();
    private static final int MAX_PAYLOAD_LENGTH = 256;
    private static final int FIXED_HEADER_LENGTH = 48; // ChipId(16) + FlashId(32)
    private static final int SN_OFFSET_LENGTH = 1;
    private static final byte[] AIZIP_MARKER = "AIZIP".getBytes(StandardCharsets.UTF_8);
    private static final int AIZIP_LENGTH = AIZIP_MARKER.length;

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
        int maxOffset = FIXED_HEADER_LENGTH + SN_OFFSET_LENGTH + AIZIP_LENGTH + (availableSpace - snBlock.length);
        int snOffset = FIXED_HEADER_LENGTH + SN_OFFSET_LENGTH + AIZIP_LENGTH + random.nextInt(availableSpace - snBlock.length);

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

    public static void main(String[] args) {
        String sn = "04446bb53755beabf1836eae635e4444";
        String chipId = "90465834153CE90A";
        String flashId = "000000005136363633159d4c45ffffff";

        byte[] payload = generatePayload(sn, chipId, flashId);
        
        // 打印出 Payload 的十六进制表示
        StringBuilder hexPayload = new StringBuilder();
        for (byte b : payload) {
            hexPayload.append(String.format("%02x", b));
        }
        System.out.println("Payload: " + hexPayload.toString());
        System.out.println("Payload长度: " + payload.length);

        ExtractedData extracted = extractData(payload);
        System.out.println("提取结果：");
        System.out.println("SN: " + extracted.sn);
        System.out.println("ChipId: " + extracted.chipId);
        System.out.println("FlashId: " + extracted.flashId);
    }
} 