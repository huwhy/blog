package cn.huwhy.katyusha.virtualattr;

import org.beetl.core.Context;
import org.beetl.core.VirtualClassAttribute;

import cn.huwhy.katyusha.model.Slide;

public class SlideStatusVal implements VirtualClassAttribute {
    @Override
    public Object eval(Object o, String s, Context context) {
        if (o == null) return null;
        Slide slide = (Slide) o;
        if ("statusVal".equals(s)) {
            return slide.getStatus().name();
        }
        return null;
    }
}
