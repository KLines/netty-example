package com.ljja.custom;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class CustomEncoder extends MessageToByteEncoder<CustomMsg> {

    public String Encoding = "utf-8";

    @Override
    protected void encode(ChannelHandlerContext ctx, CustomMsg msg, ByteBuf out) throws Exception {

        if (null == msg) {
            throw new Exception("msg is null");
        }

        String body = msg.getBody();

        byte[] bodyBytes = body.getBytes(Charset.forName(Encoding));

        out.writeByte(msg.getType());
        out.writeByte(msg.getFlag());
        out.writeInt(bodyBytes.length);
        out.writeBytes(bodyBytes);
    }
}
