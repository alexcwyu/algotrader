package org.msgpack.packer;

import org.msgpack.MessagePack;
import org.msgpack.io.ByteBufferOutput;

import java.nio.ByteBuffer;

/**
 * Created by alex on 4/26/15.
 */
public class MessagePackByteBufferPacker extends MessagePackPacker {
    public MessagePackByteBufferPacker(MessagePack msgpack, ByteBuffer byteBuffer) {
        super(msgpack, new ByteBufferOutput(byteBuffer));
    }
}

