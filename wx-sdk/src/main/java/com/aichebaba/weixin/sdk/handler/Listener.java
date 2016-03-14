package com.aichebaba.weixin.sdk.handler;

import com.aichebaba.weixin.sdk.command.Command;

public abstract class Listener {
    public abstract String handle(Command command);
}
