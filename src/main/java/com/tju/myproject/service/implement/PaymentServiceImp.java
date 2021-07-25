package com.tju.myproject.service.implement;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.tju.myproject.dao.PaymentDao;
import com.tju.myproject.entity.ResultEntity;
import com.tju.myproject.service.PaymentService;
import com.tju.myproject.utils.JavaPostJson;
import com.tju.myproject.utils.SignUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.tju.myproject.utils.HttpRequest.generatePostJson;

@Service(value = "PaymentService")

public class PaymentServiceImp implements PaymentService
{
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private PaymentDao paymentDao;

    @Override
    public ResultEntity getPaymentAddress(Map map)
    {
        Map m=new HashMap(map);
        m.remove("userID");
        m.remove("activityID");
        String key = "d491e071235b6e1c4873e31794abfd04";
        SignUtil.sign s = new SignUtil.sign();
        String sign = s.sign(m,key);
        m.put("sign", sign);

        String url = "https://service-3ekf6607-1252021128.sh.apigw.tencentcs.com/api/miniapp";
        String str = JavaPostJson.post(url,new JSONObject(m).toString());

        paymentDao.addUserPayment(map);

        return new ResultEntity(200,"",JSONObject.parseObject(str));
    }
    @Override
    public void updatePayment(Map m)
    {
        paymentDao.updatePayment(m);
    }

    @Override
    public ResultEntity checkPayment(Map m)
    {
        ArrayList<Map> orders=paymentDao.getTradeNumber(m);
        for(Map order:orders)
        {
            if(order.containsKey("in_trade_no")&&order.get("in_trade_no").toString().length()>0)
                return new ResultEntity(200,"",order);
        }
        return new ResultEntity(200,"",orders.size()>0?orders.get(0):new HashMap<>());
    }
}
