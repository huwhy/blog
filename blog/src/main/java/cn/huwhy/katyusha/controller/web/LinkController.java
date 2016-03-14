package cn.huwhy.katyusha.controller.web;

import cn.huwhy.common.json.Json;
import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.LinkDao;
import cn.huwhy.katyusha.enums.LinkType;
import cn.huwhy.katyusha.model.Link;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.jfinal.core.RequestMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope("prototype")
@AK("/link")
public class LinkController extends BaseController {

    @Autowired
    private LinkDao linkDao;

    @ActionKey(method = RequestMethod.POST)
    public void add() {
        String name = getPara("name");
        String link = getPara("link");
        if (StringUtils.isBlank(name)) {
            jsonView(Json.ERR("名称不能为空"));
            return;
        }
        if (StringUtils.isBlank(link)) {
            jsonView(Json.ERR("链接不能为空"));
            return;
        } else {
            if (!link.startsWith("http://") && !link.startsWith("https://")) {
                link = "http://" + link;
            }
        }

        Link o = new Link();
        o.setName(name);
        o.setUrl(link);
        o.setType(LinkType.navigation);
        o.setCreator(uid());
        linkDao.add(o);
        Json succ = Json.SUCC("添加链接成功 ^_^");
        succ.setData(o);
        jsonView(succ);
    }
}
