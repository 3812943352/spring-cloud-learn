/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-02 19:59:57
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-04 11:31:11
 * @FilePath: order-service/src/main/java/org/learning/orderservice/config/WeChatPayConfig.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.config;

import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.core.notification.NotificationParser;
import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
public class WeChatPayConfig {

    @Value("${wechat.mchId}")
    private String mchId;
    @Value("${wechat.apiKey}")
    private String apiKey;


    @Bean
    public RSAAutoCertificateConfig rsaAutoCertificateConfig() throws IOException {
        // 加载商户私钥（需PKCS#8格式）
        String privateKey = loadPemKey("cert/apiclient_key.pem");

        return new RSAAutoCertificateConfig.Builder()
                .merchantId(mchId)
                .privateKey(privateKey)
                .merchantSerialNumber("65AA7238223BB577F2CFF860AB2471F1ABC9846A") // 商户证书序列号
                .apiV3Key(apiKey)
                .build();
    }

    private String loadPemKey(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream is = resource.getInputStream()) {
            String key = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            // 清理PEM格式标记
            return key.replaceAll("-----BEGIN PRIVATE KEY-----", "")
                    .replaceAll("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s+", "");
        }
    }

    // 添加证书下载监听（调试用）


    @Bean
    public NativePayService nativePayService(RSAAutoCertificateConfig config) {
        return new NativePayService.Builder().config(config).build();
    }

    @Bean
    public NotificationParser notificationParser(RSAAutoCertificateConfig config) {
        return new NotificationParser(config);
    }
}