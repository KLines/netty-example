package com.ljja.client;

import com.ljja.protocol.CustomMsg;
import com.ljja.encoder.CustomEncoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ClientApp {

    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8080"));
    static final int SIZE = Integer.parseInt(System.getProperty("size", "256"));

    public static void main(String[] args) throws Exception {

        // Configure the client.
        EventLoopGroup group = new NioEventLoopGroup();

        try {

            Bootstrap b = new Bootstrap();

            b.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new CustomEncoder());
                            ch.pipeline().addLast(new CustomClientHandler());
                        }
                    });

            //异步连接到服务
            ChannelFuture future = b.connect(HOST, PORT).sync();

            Channel clientChannel = future.channel();

            int currentCount = 10;

            while (currentCount > 0) {

                //Thread.sleep(1000);

                clientChannel.writeAndFlush("Hello Netty Server");

                String msgBody = String.format("客户端主动发个消息吧 %s", System.currentTimeMillis());

                CustomMsg msgEntity = new CustomMsg(
                        (byte) 0xAB,
                        (byte) 0xCD,
                        msgBody.length(),
                        msgBody);

                clientChannel.writeAndFlush(msgEntity);

                //currentCount -= 1;
            }

            System.out.println("Close Channel");
            clientChannel.closeFuture().sync();

            System.out.println("Client Exit");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }
}