package cn.huwhy.katyusha.tags;

import java.math.BigDecimal;

import org.beetl.core.Context;
import org.beetl.core.Function;

public class CurrencyFunction implements Function{

    @Override
    public Object call(Object[] objects, Context context) {
        if(objects.length < 1 || objects.length > 2){
            throw new RuntimeException("参数错误");
        }
        Long amount = Long.valueOf(objects[0].toString());
        String unit = objects.length == 2 ? objects[1].toString() : "￥";
        BigDecimal amt = new BigDecimal(amount).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_FLOOR);
        return amt + unit;
    }
}
