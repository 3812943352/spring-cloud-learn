/**
 * @Author: 3812943352 168046603+3812943352@users.noreply.github.com
 * @Date: 2025-03-12 10:27:20
 * @LastEditors: 3812943352 168046603+3812943352@users.noreply.github.com
 * @LastEditTime: 2025-03-12 19:51:49
 * @FilePath: ApiSupervision-service/src/main/java/org/learning/apisupervisionservice/ApiSupervisionServiceApplication.java
 * @Description: 这是默认设置, 可以在设置》工具》File Description中进行配置
 */
package org.learning.apisupervisionservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class ApiSupervisionServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiSupervisionServiceApplication.class, args);
    }

}
