package com.cool.boot.notify;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.cool.boot.entity.Response;
import com.cool.boot.enums.HttpStatusEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * 支付宝
 * @author Vincent
 */
@Slf4j
@RestController
@RequestMapping("/pay")
public class AliNotify {

    @Value("${aliPay.app.id}")
    private String app_id;
    @Value("${aliPay.partner}")
    private String partner;
    @Value("${aliPay.notifyUrl}")
    private String notifyUrl;
    @Value("${aliPay.public.key}")
    private String aliPay_public_key;
    @Value("${aliPay.private.key}")
    private String aliPay_private_key;


    /*
        支付宝前调
     */
    @GetMapping("aliAuth")
    public Response getAuth(String orderNo, String bugInfo, String payMoney) {
        if (StringUtils.isEmpty(orderNo) || StringUtils.isEmpty(bugInfo) || StringUtils.isEmpty(payMoney)) {
            log.warn("获取订单信息的参数为空");
            return Response.error(HttpStatusEnum.EMPTY_PARAMS.getCode(), "获取订单信息的参数为空");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String body = UUID.randomUUID().toString();
        Map<String, String> map = new TreeMap<>();
        map.put("app_id", app_id);
        map.put("method", "alipay.trade.app.pay");
        map.put("format", "json");
        map.put("charset", "utf-8");
        map.put("sign_type", "RSA");
        map.put("timestamp", LocalDateTime.now().format(formatter));
        map.put("version", "1.0");
        map.put("notify_url", notifyUrl);

        JSONObject json = new JSONObject();
        json.put("app_id", app_id);
        json.put("method", "alipay.trade.app.pay");
        json.put("format", "json");
        json.put("charset", "utf-8");
        json.put("sign_type", "RSA");
        json.put("timestamp", LocalDateTime.now().format(formatter));
        json.put("version", "1.0");
        json.put("notify_url", notifyUrl);

        Map<String, String> content = new TreeMap<>();
        content.put("body", body);
        content.put("subject", bugInfo);
        content.put("out_trade_no", orderNo);
        content.put("timeout_express", "30m");
        content.put("total_amount", payMoney);
        content.put("product_code", "QUICK_MSECURITY_PAY");
        String contentJson = JSONObject.toJSONString(content);
        json.put("biz_content", contentJson);
        map.put("biz_content", contentJson);
        String rsaSign = "";
        try {
            rsaSign = AlipaySignature.rsaSign(map, aliPay_private_key, "utf-8");
        } catch (AlipayApiException ignore) {
        }
        json.put("sign", rsaSign);
        json.put("out_trade_no", orderNo);

        String builder_biz = "{" + "\"body\":" + "\"" + body.intern() + "\"," + "\"subject\":" + "\"" + bugInfo.intern() + "\"," +
                "\"out_trade_no\":" + "\"" + orderNo.intern() + "\"," + "\"timeout_express\":" + "\"30m\"," + "\"total_amount\":" + payMoney.intern() +
                "," + "\"product_code\":" + "\"QUICK_MSECURITY_PAY\"" + "}";

        String builder_url = "app_id=" + app_id + "&biz_content=" + builder_biz.intern() + "&charset=" + "utf-8" + "&format=" + "json" + "&method=" + "alipay.trade.app.pay" +
                "&notify_url=" + notifyUrl + "&sign=" + rsaSign.intern() + "&sign_type=" + "RSA" + "&timestamp=" + LocalDateTime.now().format(formatter) +
                "&version=" + "1.0";

        json.put("url", builder_url);
        return Response.ok(json);
    }


    /*
        支付宝回调
     */
    @GetMapping("/aliPayNotify")
    public String payByAli(@RequestParam("out_trade_no") String orderNo, @RequestParam("sign") String sign,
                           @RequestParam(value = "invoice_amount", required = false) Double total_fee, @RequestParam(value = "seller_email") String buyer_email,
                           @RequestParam(value = "notify_id", required = false) String notifyId) {

        String echo = "";
        try {

            log.info("开始进行支付宝回调函数的处理");
            //todo

        } catch (Exception e) {

            log.error("进行支付宝回调函数的处理 异常 订单为{}", orderNo, e);
        }
        return echo;
    }
}
