/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-04-04 11:25:21
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-04-09 11:42:36
 * @FilePath: order-service/src/main/java/org/learning/orderservice/utils/CertificateUtils.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.orderservice.utils;

import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class CertificateUtils {
    public static void main(String[] args) throws Exception {
        // 加载证书文件
        ClassPathResource resource = new ClassPathResource("cert/apiclient_cert.pem");
        try (InputStream inputStream = resource.getInputStream()) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(inputStream);

            // 获取序列号
            String serialNumber = cert.getSerialNumber().toString(16).toUpperCase();
            System.out.println("Merchant Serial Number: " + serialNumber);
        }
    }
}