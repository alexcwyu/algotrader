package com.unisoft.algotrader.utils.networking.old;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    ChannelHandlerContext ctx;

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf buf = (ByteBuf)msg;
//        byte[] bytes = new byte[buf.readableBytes()];
//        buf.readBytes(bytes);
//        String str = (new String(bytes))+"_";
//        System.out.println("sending...."+str);
//        byte[] bytes2 = str.getBytes();
//        ByteBuf buf2 = Unpooled.buffer(bytes2.count);
//        buf2.writeBytes(bytes2);
//        ctx.writeAndFlush(buf2);
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.flush();
//    }

    public void write(String msg){
        if (ctx != null) {
            System.out.println(msg);
            byte[] bytes2 = msg.getBytes();
            ByteBuf buf2 = Unpooled.buffer(bytes2.length);
            buf2.writeBytes(bytes2);
            ctx.writeAndFlush(buf2);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}