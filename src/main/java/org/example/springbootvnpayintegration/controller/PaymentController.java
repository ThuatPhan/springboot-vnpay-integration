package org.example.springbootvnpayintegration.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.springbootvnpayintegration.dto.ApiResponse;
import org.example.springbootvnpayintegration.dto.VNPayResponse;
import org.example.springbootvnpayintegration.service.VNPayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PaymentController {
    VNPayService vnPayService;

    @GetMapping("/vnpay")
    public ApiResponse<VNPayResponse> createPaymentUrl(HttpServletRequest request, @RequestParam long amount) {
        return ApiResponse.success(vnPayService.createPaymentUrl(request, amount));
    }

    @GetMapping("/payment-callback")
    public ApiResponse<VNPayResponse> paymentCallbackHandler(HttpServletRequest servletRequest) {
        return ApiResponse.success(vnPayService.handlePaymentCallback(servletRequest));
    }
}
