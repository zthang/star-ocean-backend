package com.tju.myproject.service;

import com.tju.myproject.entity.ResultEntity;

import java.util.Map;

public interface PaymentService
{
    ResultEntity getPaymentAddress(Map m);
    ResultEntity getVipPaymentAddress(Map map);
    void updatePayment(Map m);
    void updateVipPayment(Map m);
    ResultEntity checkPayment(Map m);
    ResultEntity checkVipPayment(Integer userID);
}
