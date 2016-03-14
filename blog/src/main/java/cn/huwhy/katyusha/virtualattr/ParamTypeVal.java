package cn.huwhy.katyusha.virtualattr;

import cn.huwhy.katyusha.model.Param;
import org.beetl.core.Context;
import org.beetl.core.VirtualClassAttribute;

public class ParamTypeVal implements VirtualClassAttribute {
    @Override
    public Object eval(Object o, String s, Context context) {
        if (o == null) return null;
        Param slide = (Param) o;
        return null;
    }
}