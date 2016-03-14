package com.aichebaba.weixin.sdk.handler;

import com.aichebaba.weixin.sdk.command.Command;

public class DefaultListener extends Listener{
    @Override
    public String handle(Command command) {
        return "success";
    }
}
