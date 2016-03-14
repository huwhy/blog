package cn.huwhy.katyusha.virtualattr;

import org.beetl.core.Context;
import org.beetl.core.VirtualClassAttribute;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.model.Item;

public class ItemVirtualClassAttribute implements VirtualClassAttribute {
    @Override
    public Object eval(Object o, String s, Context context) {
        if (o == null) return null;
        Item item = (Item) o;
        if ("path".equals(s)) {
            return StringUtils.isBlank(item.getUrl()) ? "/item/" + item.getId() + ".html" : item.getUrl();
        }
        return null;
    }
}
