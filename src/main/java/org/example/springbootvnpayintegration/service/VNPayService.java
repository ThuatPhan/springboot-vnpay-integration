package org.example.springbootvnpayintegration.service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.example.springbootvnpayintegration.config.VNPayConfig;
import org.example.springbootvnpayintegration.dto.VNPayResponse;
import org.example.springbootvnpayintegration.util.VNPayUtil;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class VNPayService {
    VNPayConfig vnpayConfig;

    public VNPayResponse createPaymentUrl(HttpServletRequest request, long amount) {
        Map<String, String> vnpParams = buildParams(request, amount);

        String queryUrl = VNPayUtil.buildQuery(vnpParams, true);

        String hashData = VNPayUtil.buildQuery(vnpParams, false);
        String vnpSecureHash = VNPayUtil.hmacSHA512(vnpayConfig.getSecretKey(), hashData);

        String paymentUrl = vnpayConfig.getPayUrl() + "?" + queryUrl + "&vnp_SecureHash=" + vnpSecureHash;

        return VNPayResponse.builder().paymentUrl(paymentUrl).build();
    }

    public VNPayResponse handlePaymentCallback(HttpServletRequest servletRequest) {
        String responseCode = servletRequest.getParameter("vnp_ResponseCode");

        if (responseCode.equals("00")) {
            return VNPayResponse.builder()
                    .code(responseCode)
                    .message("Payment successful, thank you for your purchase!")
                    .build();
        }

        return VNPayResponse.builder()
                .code(responseCode)
                .message("Payment failed. Please try again or use another payment method.")
                .build();
    }

    private Map<String, String> buildParams(HttpServletRequest request, long amount) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", vnpayConfig.getVersion());
        vnpParams.put("vnp_Command", vnpayConfig.getCommand());
        vnpParams.put("vnp_TmnCode", vnpayConfig.getTmnCode());
        vnpParams.put("vnp_CurrCode", "VND");

        String orderId = VNPayUtil.getRandomNumber(8);
        vnpParams.put("vnp_TxnRef", orderId);
        vnpParams.put("vnp_OrderInfo", "Thanh toan don hang " + orderId);

        vnpParams.put("vnp_OrderType", vnpayConfig.getOrderType());
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", vnpayConfig.getReturnUrl());
        vnpParams.put("vnp_IpAddr", VNPayUtil.getIpAddress(request));
        vnpParams.put("vnp_Amount", String.valueOf(amount * 100));

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");

        String vnpCreateDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_CreateDate", vnpCreateDate);

        calendar.add(Calendar.MINUTE, 15);
        String vnpExpireDate = formatter.format(calendar.getTime());
        vnpParams.put("vnp_ExpireDate", vnpExpireDate);

        return vnpParams;
    }
}
