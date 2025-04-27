# COROS Android和iOS 应用的许可证管理接口 文档

## 基础信息

- **基础URL**: `http://121.4.106.36:9981`

## 接口说明

接口提供了 Coros 应用的许可证管理接口，包括生成和验证许可证的功能。

## 接口列表

### 1. 生成许可证

- **接口路径**: `/coros/license/generate`
- **请求方式**: POST
- **接口描述**: 根据应用ID和平台生成加密的许可证
- **请求参数**:

  | 参数名 | 类型 | 必填 | 说明 | 示例值 |
  |--------|------|------|------|--------|
  | appId | String | 是 | 应用ID | com.yf.smart.coros.dist 或 com.coros.coros |
  | platform | String | 是 | 平台类型 | android 或 ios |

- **curl 示例**:
  ```bash
  # 生成 iOS 许可证
  curl -X POST "http://121.4.106.36:9981/coros/license/generate?appId=com.coros.coros&platform=ios" \
       -H "accept: */*" \
       -d ""

  # 生成 Android 许可证
  curl -X POST "http://121.4.106.36:9981/coros/license/generate?appId=com.yf.smart.coros.dist&platform=android" \
       -H "accept: */*" \
       -d ""
  ```

- **返回示例**:
  - 生成成功:
    ```json
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "license": "加密后的许可证字符串",
            "app_id": "com.coros.coros",
            "platform": "ios",
            "expiry": "2024-03-21"
        }
    }
    ```
  - 生成失败:
    ```json
    {
        "code": 404,
        "message": "错误信息",
        "data": null
    }
    ```

- **错误信息说明**:

  | 错误信息 | 说明 |
  |----------|------|
  | 无效的平台类型 | 平台类型不是 android 或 ios |
  | 无效的Android应用ID | Android应用ID不是 com.yf.smart.coros.dist |
  | 无效的iOS应用ID | iOS应用ID不是 com.coros.coros |

### 2. 验证许可证

- **接口路径**: `/coros/license/verify`
- **请求方式**: POST
- **接口描述**: 验证许可证的有效性
- **请求参数**:

  | 参数名 | 类型 | 必填 | 说明 |
  |--------|------|------|------|
  | encryptedLicense | String | 是 | 加密的许可证字符串 |

- **curl 示例**:
  ```bash
  # 验证许可证
  curl -X POST "http://121.4.106.36:9981/coros/license/verify?encryptedLicense=gxvaXMkdzJ1yORsyDXTbDKW7g00F%2BUTFqrgvXtjfNH7cCHZeeoxy8zixr9lGzvwuYP3fEoNQSz%2FjCbQlXSoxFduxSJN0oWtNdkBk1dAw0%2BC2rooxFGbZgoHEOEkoWz5xfKI56rXQFiexIEtGI%2FUEEwk1WYRSFXjgWAUtdfbFJtXFVK7SVtOcaB0x1bp%2FrW%2ByCKxHFEnLjnXk6qPpex70VzMTmKRaqx4cRlLZkFTFiy3%2F6DoGfiJLZYfnMlvRO6%2Fv0SRxnufJIqCzqHlX%2FjcWwi4BxaAAFs5TumPFxu974jT3LcvPWIoiprmD1sLKiP4Com2zpN9bCjSLXI%2BD3HQuyA%3D%3D" \
       -H "accept: */*" \
       -d ""
  ```

- **返回示例**:
  - 验证成功:
    ```json
    {
        "code": 200,
        "message": "操作成功",
        "data": {
            "status": "valid",
            "app_id": "com.coros.coros",
            "platform": "ios",
            "expiry": "2024-03-21"
        }
    }
    ```
  - 验证失败:
    ```json
    {
        "code": 404,
        "message": "错误信息",
        "data": null
    }
    ```

- **错误信息说明**:

  | 错误信息 | 说明 |
  |----------|------|
  | 无效的平台类型 | 平台类型不是 android 或 ios |
  | 无效的Android应用ID | Android应用ID不是 com.yf.smart.coros.dist |
  | 无效的iOS应用ID | iOS应用ID不是 com.coros.coros |
  | 许可证已过期 | 许可证已超过有效期 |
  | 许可证验证失败 | 其他验证失败的情况 |

## 注意事项

1. 生成许可证时，需要确保应用ID和平台类型的合法性
2. 验证许可证时，需要提供完整的加密许可证字符串
3. 许可证的有效期为7天，建议每天请求获得后续7天的许可证。COROS服务器自行保存，并提供给APP使用。