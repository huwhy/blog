package cn.huwhy.katyusha.controller.admin;

import cn.huwhy.common.utils.StringUtils;
import cn.huwhy.katyusha.controller.BaseController;
import cn.huwhy.katyusha.dao.MemberDao;
import cn.huwhy.katyusha.enums.MemberSource;
import cn.huwhy.katyusha.model.Member;
import cn.huwhy.weixin.api.AccessToken;
import cn.huwhy.weixin.api.AccessTokenApi;
import cn.huwhy.weixin.api.UserInfo;
import cn.huwhy.weixin.api.UserInfoApi;
import cn.huwhy.weixin.handler.EventHandler;
import com.jfinal.core.AK;
import com.jfinal.core.ActionKey;
import com.qq.weixin.mp.aes.AesException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.xml.sax.SAXException;

import javax.servlet.ServletInputStream;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

@Controller
@Scope("prototype")
@AK("/weixin")
public class WeiXinController extends BaseController {

    private static final String APP_ID = "wxe4e3ed91b669e6ff";
    private static final String APP_SECRET = "b706a4dcf70cf661c6af5516d2e32c11 ";

    @Autowired
    private MemberDao memberDao;

    @ActionKey(value = "/callback/{appName}")
    public void callBack() throws ParserConfigurationException, AesException, SAXException, IOException {
        String code = getPara("code");
        String state = getPara("state");
        if (StringUtils.notBlanks(code, state)) {
            AccessToken accessToken = AccessTokenApi.getUserAccessToken(APP_ID, APP_SECRET, code);
            Member member = memberDao.getMemberByResourceId(accessToken.getOpenid());
            if (member == null) {
                UserInfo userInfo = UserInfoApi.getUserInfo(accessToken.getAccess_token(), accessToken.getOpenid());
                member = new Member();
                member.setHeader(userInfo.getHeadimgurl());
                member.setName(userInfo.getNickname());
                member.setNick(userInfo.getNickname());
                member.setPassword("");
                member.setSource(MemberSource.WeiXin);
                member.setSourceId(accessToken.getOpenid());
                member.setLastLogin(new Date());
                member.setCreated(new Date());
                memberDao.add(member);
            }
        } else {
            if (getRequest().getMethod().equalsIgnoreCase("GET")) {
                callGet();
            } else {
                callPost();
            }
        }
    }

    private void callGet() {
        String echostr = getPara("echostr");
        renderText(echostr);
    }

    public void callPost() throws IOException, AesException, ParserConfigurationException, SAXException {
        // 处理接收消息
        ServletInputStream in = getRequest().getInputStream();
        StringBuilder xmlMsg = new StringBuilder();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            xmlMsg.append(new String(b, 0, n, "iso8859-1"));
        }
        String timestamp = getPara("timestamp");
        String nonce = getPara("nonce");
        String msgSignature = getPara("msg_signature");
        String result = EventHandler.wo.handler(msgSignature, timestamp, nonce, xmlMsg.toString());
        renderText(result);
    }
}
