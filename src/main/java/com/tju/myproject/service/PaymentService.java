package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;

import java.util.Map;

public interface PaymentService
{
    ResultEntity getPaymentAddress(Map m);
    void updatePayment(Map m);
    ResultEntity checkPayment(Map m);
}
