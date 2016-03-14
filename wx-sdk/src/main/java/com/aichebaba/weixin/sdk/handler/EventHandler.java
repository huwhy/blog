package com.aichebaba.weixin.sdk.handler;

import com.aichebaba.weixin.sdk.command.Command;
import com.aichebaba.weixin.sdk.exception.BusinessException;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class EventHandler {

    private static Map<String, WXBizMsgCrypt> cryptMap = new HashMap<>();
    private static Logger log = Logger.getLogger(EventHandler.class);
    private Map<String, Listener> listenerMap = new HashMap<String, Listener>();

    private EventHandler() {
    }

    public static final EventHandler wo = new EventHandler();

    public static void addCrypt(String mp, String token, String aesKey, String appId) {
        try {
            WXBizMsgCrypt crypt = new WXBizMsgCrypt(token, aesKey, appId);
            cryptMap.put(mp, crypt);
        } catch (AesException e) {
            throw new BusinessException("init wxbizMsgCrypt failure", e);
        }
    }

    public String handler(String msgSignature, String timestamp, String nonce, String postXML) {
        log.info("handler msg : " + postXML);
        WXBizMsgCrypt crypt = cryptMap.values().iterator().next();
        Command command = transform(crypt, msgSignature, timestamp, nonce, postXML);
        log.info(command);
        return getListener(command).handle(command);
    }

    public String handler(String mp, String msgSignature, String timestamp, String nonce, String postXML) {
        log.info("handler msg : " + postXML);
        WXBizMsgCrypt crypt = cryptMap.get(mp);
        Command command = transform(crypt, msgSignature, timestamp, nonce, postXML);
        command.setData(mp);
        log.info(command);
        return getListener(command).handle(command);
    }

    public Listener getListener(Command command) {
        Listener listener = listenerMap.get(command.getCommandKey());
        return listener == null ? new DefaultListener() : listener;
    }

    public void register(String commandKey, Listener listener) {
        listenerMap.put(commandKey, listener);
    }

    public Command transform(WXBizMsgCrypt crypt, String msgSignature, String timestamp, String nonce, String postXML) {
        try {
            String ss = crypt.decryptMsg(msgSignature, timestamp, nonce, postXML);
            log.info("decrypt msg : " + ss);
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(ss);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);
            Element root = document.getDocumentElement();

            Command command = new Command();
            NodeList msgTypeNode = root.getElementsByTagName("MsgType");
            String msgType = msgTypeNode.item(0).getTextContent().trim();
            command.setMsgType(msgType);

            NodeList toNode = root.getElementsByTagName("ToUserName");
            String toUser = toNode.item(0).getTextContent();
            command.setToUserName(toUser);

            NodeList fromNode = root.getElementsByTagName("FromUserName");
            String fromUser = fromNode.item(0).getTextContent();
            command.setFromUserName(fromUser);

            NodeList createTimeNode = root.getElementsByTagName("CreateTime");
            long createTime = Long.parseLong(createTimeNode.item(0).getTextContent());
            command.setCreateTime(createTime);

            NodeList eventNode = root.getElementsByTagName("Event");
            if (eventNode.getLength() == 1) {
                command.setEvent(eventNode.item(0).getTextContent());
            }

            NodeList ticketNode = root.getElementsByTagName("Ticket");
            if (ticketNode.getLength() == 1) {
                command.setTicket(ticketNode.item(0).getTextContent());
            }

            NodeList ekNode = root.getElementsByTagName("EventKey");
            if (ekNode.getLength() == 1) {
                command.setEventKey(ekNode.item(0).getTextContent());
            }

            NodeList latitudeNode = root.getElementsByTagName("Latitude");
            if (latitudeNode.getLength() == 1) {
                command.setLatitude(latitudeNode.item(0).getTextContent());
            }

            NodeList longitudeNode = root.getElementsByTagName("Longitude");
            if (longitudeNode.getLength() == 1) {
                command.setLongitude(longitudeNode.item(0).getTextContent());
            }

            NodeList precisionNode = root.getElementsByTagName("Precision");
            if (precisionNode.getLength() == 1) {
                command.setPrecision(precisionNode.item(0).getTextContent());
            }

            NodeList msgIdNode = root.getElementsByTagName("MsgId");
            if (msgIdNode.getLength() == 1) {
                command.setMsgId(msgIdNode.item(0).getTextContent());
            }

            NodeList contentNode = root.getElementsByTagName("Content");
            if (contentNode.getLength() == 1) {
                command.setContent(contentNode.item(0).getTextContent());
            }

            NodeList picUrlNode = root.getElementsByTagName("PicUrl");
            if (picUrlNode.getLength() == 1) {
                command.setPicUrl(picUrlNode.item(0).getTextContent());
            }

            NodeList mediaIdNode = root.getElementsByTagName("MediaId");
            if (mediaIdNode.getLength() == 1) {
                command.setMediaId(mediaIdNode.item(0).getTextContent());
            }

            NodeList formatNode = root.getElementsByTagName("Format");
            if (formatNode.getLength() == 1) {
                command.setFormat(formatNode.item(0).getTextContent());
            }

            command.setThumbMediaid(nodeValue("ThumbMediaId", root));
            command.setLocationX(nodeValue("Location_X", root));
            command.setLocationY(nodeValue("Location_Y", root));
            command.setScale(nodeValue("Scale", root));
            command.setLabel(nodeValue("Label", root));
            command.setTitle(nodeValue("Title", root));
            command.setDescription(nodeValue("Description", root));
            command.setUrl(nodeValue("Url", root));
            command.setRecognition(nodeValue("Recognition", root));
            command.setStatus(nodeValue("Status", root));
            return command;

        } catch (IOException e) {
            throw new BusinessException("receive msg fail", e);
        } catch (ParserConfigurationException e) {
            throw new BusinessException("receive msg fail", e);
        } catch (AesException e) {
            throw new BusinessException("receive msg fail", e);
        } catch (SAXException e) {
            throw new BusinessException("receive msg fail", e);
        }
    }

    private String nodeValue(String nodeName, Element root) {
        NodeList node = root.getElementsByTagName(nodeName);
        if (node.getLength() == 1) {
            return node.item(0).getTextContent();
        }
        return null;
    }
}
