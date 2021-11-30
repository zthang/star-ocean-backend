package com.tju.myproject.controller;

import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class PaymentController {
    @Autowired private PaymentService paymentService;

    @PostMapping("/api/getPaymentAddress")
    public ResultEntity getPaymentAddress(@RequestBody Map m)
    {
        return paymentService.getPaymentAddress(m);
    }
    @PostMapping("/api/getVipPaymentAddress")
    public ResultEntity getVipPaymentAddress(@RequestBody Map m)
    {
        return paymentService.getVipPaymentAddress(m);
    }
    @PostMapping("/payment")
    public String payment(@RequestBody Map m)
    {
        paymentService.updatePayment(m);
        return "success";
    }
    @PostMapping("/vipPayment")
    public String vipPayment(@RequestBody Map m)
    {
        paymentService.updateVipPayment(m);
        return "success";
    }
    @PostMapping("/api/checkPayment")
    public ResultEntity checkPayment(@RequestBody Map m)
    {
        return paymentService.checkPayment(m);
    }
    @GetMapping("/api/checkVipPayment")
    public ResultEntity checkVipPayment(Integer userID)
    {
        return paymentService.checkVipPayment(userID);
    }
}
