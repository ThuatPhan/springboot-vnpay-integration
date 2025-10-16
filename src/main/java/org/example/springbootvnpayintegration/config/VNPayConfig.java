package org.example.springbootvnpayintegration.config;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "vnpay")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayConfig {
    String payUrl;
    String returnUrl;
    String tmnCode;
    String secretKey;
    String version;
    String command;
    String orderType;
}
