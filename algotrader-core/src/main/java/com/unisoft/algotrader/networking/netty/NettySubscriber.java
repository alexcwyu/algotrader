package com.unisoft.algotrader.networking.netty;

import com.google.common.collect.Lists;
import com.unisoft.algotrader.networking.DataHandler;
import com.unisoft.algotrader.networking.Subscriber;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alex on 4/19/15.
 */
public class NettySubscriber extends ChannelInboundHandlerAdapter implements Subscriber {

    private final List<DataHandler> dataHandlers = Lists.newArrayList();

    private EventLoopGroup group;
    private ChannelFuture channelFuture;
    private AtomicBoolean connected = new AtomicBoolean(false);

    private ExecutorService executor = Executors.newFixedThreadPool(1);

    public NettySubscriber(){
    }

    @Override
    public void connect() {
        executor.submit(()->{
        group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                            //.option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline p = ch.pipeline();
                            p.addLast(NettySubscriber.this);
                        }
                    });

            // Start the client.
            channelFuture = b.connect("127.0.0.1", 8007).sync();

            //channelFuture.channel().closeFuture().sync();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        });
    }

    @Override
    public void disconnect() {

        channelFuture.cancel(true);
        group.shutdownGracefully();
        connected.set(false);
    }

    @Override
    public boolean connected() {
        return connected.get();
    }


    ChannelHandlerContext ctx;
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object data) {
        ByteBuf buf = (ByteBuf)data;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        for (DataHandler dataHandler : dataHandlers){
            dataHandler.onData(bytes);
        }
        buf.release();
    }


    @Override
    public void subscribe(DataHandler handler) {
        dataHandlers.add(handler);
    }
}
