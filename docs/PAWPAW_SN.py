import os
import random

def generate_payload(sn: str, chip_id: str, flash_id: str) -> bytes:
    # 转换为 bytes
    chip_id_bytes = chip_id.encode('utf-8')
    flash_id_bytes = flash_id.encode('utf-8')
    sn_bytes = sn.encode('utf-8')

    # 检查长度（确保符合预期）
    assert len(chip_id_bytes) == 16
    assert len(flash_id_bytes) == 32
    assert len(sn_bytes) == 32

    # SN 数据块
    sn_block = b'S' + bytes([len(sn_bytes)]) + sn_bytes

    # 固定头部：ChipId + FlashId（总 48 字节）
    fixed_part = chip_id_bytes + flash_id_bytes

    # 剩余空间
    remaining_space = 256 - 48 - 1  # 预留1字节放 sn_offset

    # 确定 SN 数据在 payload 中的偏移位置
    sn_offset = random.randint(48 + 1, 256 - len(sn_block))  # 不要覆盖前48+1字节

    # 创建随机内容的初始 payload
    body = bytearray(os.urandom(remaining_space))

    # 插入 SN 数据
    body[sn_offset - 49 : sn_offset - 49 + len(sn_block)] = sn_block

    # 组合 payload
    payload = fixed_part + bytes([sn_offset]) + body
    return payload


def extract_data(payload: bytes):
    chip_id = payload[0:16].decode('utf-8')
    flash_id = payload[16:48].decode('utf-8')
    sn_offset = payload[48]

    # 提取 SN
    if payload[sn_offset] == ord('S'):
        length = payload[sn_offset + 1]
        sn_bytes = payload[sn_offset + 2 : sn_offset + 2 + length]
        sn = sn_bytes.decode('utf-8')
    else:
        sn = None

    return sn, chip_id, flash_id


sn = "04446bb53755beabf1836eae635e4444"
chip_id = "90465834153CE90A"
flash_id = "000000005136363633159d4c45ffffff"

payload = generate_payload(sn, chip_id, flash_id)
# 打印出 Payload 的十六进制表示，以便查看内容
print(f"Payload: {payload.hex()}")
print(f"Payload长度: {len(payload)}")

sn_out, chip_id_out, flash_id_out = extract_data(payload)
print("提取结果：")
print("SN:", sn_out)
print("ChipId:", chip_id_out)
print("FlashId:", flash_id_out)