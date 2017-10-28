package com.ljja.custom;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class CustomClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {

        String msg = "Hello,Netty";

        CustomMsg customMsg = new CustomMsg(
                (byte) 0xAB,
                (byte) 0xCD,
                msg.length(),
                msg);

        ctx.writeAndFlush(customMsg);
    }
}
