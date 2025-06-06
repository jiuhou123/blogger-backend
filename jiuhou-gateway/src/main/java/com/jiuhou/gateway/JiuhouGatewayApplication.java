package com.jiuhou.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 网关启动程序
 */
@EnableDiscoveryClient
@SpringBootApplication
public class JiuhouGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(JiuhouGatewayApplication.class, args);
    }
} 