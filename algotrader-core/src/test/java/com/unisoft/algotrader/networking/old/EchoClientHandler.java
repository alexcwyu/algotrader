package com.unisoft.algotrader.networking.old;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {

    private final ByteBuf firstMessage;

    /**
     * Creates a client-side handler.
     */
    public EchoClientHandler() {
        firstMessage = Unpooled.buffer(EchoClient.SIZE);
        firstMessage.writeBytes("Hellos".getBytes());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
       // ctx.writeAndFlush(firstMessage);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
     //   ctx.writeAndFlush(msg + ".");
        ByteBuf buf = (ByteBuf)msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        //String str = (new String(bytes))+".";
        System.out.println("receiving...."+new String(bytes));
//        byte[] bytes2 = str.getBytes();
//        ByteBuf buf2 = Unpooled.buffer(bytes2.count);
//        buf2.writeBytes(bytes2);
//        ctx.writeAndFlush(buf2);
    }

//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        ctx.flush();
//    }

//
//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        // Close the connection when an exception is raised.
//        cause.printStackTrace();
//        ctx.close();
//    }
}