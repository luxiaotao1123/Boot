package com.cool.boot.notify;

import com.cool.boot.entity.WXPayEntity;
import com.cool.boot.utils.Iptools;
import com.cool.boot.utils.MD5;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Vincent
 */
@Slf4j
@RestController
@RequestMapping("pay")
public class WXNotify {

    @Value("${wx.appid}")
    private String appId;
    @Value("${WXPay.notifyUrl}")
    private String notifyUrl;
    @Value("${WXPay.shop.id}")
    private String shopId;
    @Value("${WXPay.body}")
    private String body;
    @Value("${WXPay.key}")
    private String key;


    /*
        微信支付前调
     */
    @RequestMapping("/wxAuth")
    public String wxMsg(String openId,
                        String orderNo,
                        String payMoney,
                        HttpServletRequest req) throws IllegalAccessException {

        String ip = Iptools.gainRealIp(WebUtils.toHttp(req));

        WXPayEntity entity = new WXPayEntity();
        entity.setAppid(appId);
        entity.setMch_id(shopId);
        entity.setNonce_str(getRandomStringByLength(32));
        entity.setBody(body);
        entity.setOut_trade_no(orderNo);
        entity.setTotal_fee(payMoney);
        entity.setSpbill_create_ip(ip);
        entity.setNotify_url(notifyUrl);
        entity.setTrade_type("JSAPI");
        entity.setOpenid(openId);
        entity.setSign_type("MD5");
        //生成签名
        String sign = getSign(entity);
        entity.setSign(sign);

        XStream xStreamForRequestPostData = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStreamForRequestPostData.alias("xml", entity.getClass());

        return xStreamForRequestPostData.toXML(entity);
    }


    /*
        微信支付回调
     */
    @GetMapping("wxPayNotify")
    public String wxPayNotify(@RequestParam("appid") String appId,
                              @RequestParam("mch_id") String shopId,
                              @RequestParam("nonce_str") String nonce_str,
                              @RequestParam("sign") String sign,
                              @RequestParam("result_code") String result_code,
                              @RequestParam("openid") String openid,
                              @RequestParam("trade_type") String trade_type,
                              @RequestParam("bank_type") String bank_type,
                              @RequestParam("total_fee") Integer totalFee,
                              @RequestParam("cash_fee") Integer cash_fee,
                              @RequestParam("transaction_id") String orderNo,
                              @RequestParam("out_trade_no") String out_trade_no,
                              @RequestParam("time_end") String time_end) {

        String res = "";
        log.info("开始进行微信支付回调函数的处理");
        try {
            //todo
        } catch (Exception e) {

            log.info("进行微信支付回调函数的处理 异常 订单为{}", orderNo, e);
        }
        return res;
    }


    /**
     * 签名算法
     *
     * @param o 要参与签名的数据对象
     * @return 签名
     * @throws IllegalAccessException
     */
    private String getSign(Object o) throws IllegalAccessException {
        ArrayList<String> list = new ArrayList<String>();
        Class cls = o.getClass();
        Field[] fields = cls.getDeclaredFields();
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.get(o) != null && f.get(o) != "") {
                String name = f.getName();
                XStreamAlias anno = f.getAnnotation(XStreamAlias.class);
                if (anno != null)
                    name = anno.value();
                list.add(name + "=" + f.get(o) + "&");
            }
        }
        int size = list.size();
        String[] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + key;
        return MD5.MD5Encode(result).toUpperCase();
    }

    /**
     * 获取一定长度的随机字符串
     *
     * @param length 指定字符串长度
     * @return 一定长度的字符串
     */
    private String getRandomStringByLength(int length) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }
}
