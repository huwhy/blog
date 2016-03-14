package com.aichebaba;

import com.aichebaba.weixin.sdk.textmsg.MsgStrategy;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class NewsMsgStrategyTest extends TestCase{
    public void testContent() {
        List<String> params = new ArrayList<>();
        params.add(MsgStrategy.convertParams("1", "2", "3", "4"));
        String reply = MsgStrategy.getReply(MsgStrategy.NEWS, "1", "2", params);
        System.out.println(reply);
    }
}
