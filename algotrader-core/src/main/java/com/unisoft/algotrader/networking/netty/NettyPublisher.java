package com.unisoft.algotrader.networking.netty;

import com.unisoft.algotrader.networking.Publisher;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 4/19/15.
 */
@ChannelHandler.Sharable
public class NettyPublisher extends ChannelInboundHandlerAdapter implements Publisher {
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;
    private ChannelFuture channelFuture;
    private ExecutorService executor;
    //private static ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public NettyPublisher(){
    }

    private AtomicBoolean connected = new AtomicBoolean(false);
    @Override
    public void connect() {
        executor = Executors.newFixedThreadPool(1);

        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    //.option(ChannelOption.SO_BACKLOG, 100)
                    .handler(new LoggingHandler(LogLevel.ERROR))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast( NettyPublisher.this);
                        }
                    });

            // Start the server.
            channelFuture = b.bind(8007).sync();
            // Wait until the server socket is closed.
            //channelFuture.channel().closeFuture().sync();
            connected.set(true);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void disconnect() {
        channelFuture.cancel(true);
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
        connected.set(false);
        executor.shutdown();
    }

    ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public boolean publish(byte[] bytes) {
        if (ctx != null) {
            ByteBuf buf = Unpooled.buffer(bytes.length);
            buf.writeBytes(bytes);
            ctx.writeAndFlush(buf);
            //channelFuture.channel().write(buf);
            return true;
        }
        Thread.yield();
        return false;
    }


    @Override
    public boolean publish(ByteBuffer byteBuffer, int length) {
        if (ctx != null) {
            ByteBuf buf = Unpooled.buffer(length);
            buf.writeBytes(byteBuffer);
            ctx.writeAndFlush(buf);
            //channelFuture.channel().write(buf);
            return true;
        }
        Thread.yield();
        return false;
    }

    @Override
    public boolean connected() {
        return connected.get();
    }
}
