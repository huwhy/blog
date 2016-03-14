package com.aichebaba.weixin.sdk;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class WeiXinUtils {
    public static final String encodingAesKey = "qxMqtvGkZPnaXc1npDddd5TztRyT889oZcD2F7h9jQeMC";
    public static final String token = "jianku2ddd0150423";
    public static final String appId = "wx26b9f44aa940d8607";
    private static WXBizMsgCrypt CRYPT = null;
    private static Logger log = Logger.getLogger(WeiXinUtils.class);

    static {
        try {
            CRYPT = new WXBizMsgCrypt(token, encodingAesKey, appId);
        } catch (AesException e) {
            throw new RuntimeException("init wxbizMsgCrypt failure", e);
        }
    }

    public static WeiXinFromMsg receiveMsg(String msgSignature, String timestamp, String nonce, String encrptMsg) {
        try {
            String ss = CRYPT.decryptMsg(msgSignature, timestamp, nonce, encrptMsg);
            log.info("decrypt msg : " + ss);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(ss);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList toNode = root.getElementsByTagName("ToUserName");
            NodeList fromNode = root.getElementsByTagName("FromUserName");
            NodeList createTimeNode = root.getElementsByTagName("CreateTime");
            NodeList msgTypeNode = root.getElementsByTagName("MsgType");
            NodeList contentNode = root.getElementsByTagName("Content");
            NodeList msgIdNode = root.getElementsByTagName("MsgId");
            WeiXinFromMsg fromMsg = new WeiXinFromMsg();
            fromMsg.setTo(toNode.item(0).getTextContent());
            fromMsg.setFrom(fromNode.item(0).getTextContent());
            fromMsg.setCreateTime(createTimeNode.item(0).getTextContent());
            fromMsg.setMsgType(msgTypeNode.item(0).getTextContent());
            Node item = contentNode.item(0);
            if (item != null) {
                fromMsg.setContent(item.getTextContent());
            }
            fromMsg.setMsgId(msgIdNode.item(0).getTextContent());
            return fromMsg;
        } catch (IOException | ParserConfigurationException | AesException | SAXException e) {
            throw new RuntimeException("receive msg fail", e);
        }
    }
}
