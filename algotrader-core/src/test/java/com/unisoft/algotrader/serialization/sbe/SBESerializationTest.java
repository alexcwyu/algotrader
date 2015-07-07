package com.unisoft.algotrader.serialization.sbe;

import com.unisoft.algotrader.model.event.data.sbe.MessageHeader;
import com.unisoft.algotrader.model.event.data.sbe.Quote;
import org.junit.Test;
import uk.co.real_logic.agrona.concurrent.UnsafeBuffer;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;

/**
 * Created by alex on 4/27/15.
 */
public class SBESerializationTest {

    @Test
    public void testSingleMessage() throws Exception{

        final MessageHeader MESSAGE_HEADER = new MessageHeader();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        final short messageTemplateVersion = 0;
        encodeDecode(MESSAGE_HEADER, directBuffer, messageTemplateVersion);


    }

    private void encodeDecode(MessageHeader MESSAGE_HEADER, UnsafeBuffer directBuffer, short messageTemplateVersion) throws Exception {
        //encode
        int bufferOffset = 0;
        final Quote quote = new Quote();
        int encodingLength = 0;
        MESSAGE_HEADER.wrap(directBuffer, bufferOffset, messageTemplateVersion)
                .blockLength(quote.sbeBlockLength())
                .templateId(quote.sbeTemplateId())
                .schemaId(quote.sbeSchemaId())
                .version(quote.sbeSchemaVersion());
        bufferOffset += MESSAGE_HEADER.size();
        encodingLength += MESSAGE_HEADER.size();
        encodingLength += encode(quote, directBuffer, bufferOffset, 0);
        bufferOffset += quote.size();


        //decode
        bufferOffset = 0;
        MessageHeader MESSAGE_HEADER2 = new MessageHeader().wrap(directBuffer, bufferOffset, messageTemplateVersion);
        final int templateId = MESSAGE_HEADER.templateId();
        if (templateId != Quote.TEMPLATE_ID) {
            throw new IllegalStateException("Template ids do not match");
        }

        final int actingBlockLength = MESSAGE_HEADER.blockLength();
        final int schemaId = MESSAGE_HEADER.schemaId();
        final int actingVersion = MESSAGE_HEADER.version();

        bufferOffset += MESSAGE_HEADER.size();
        final Quote quote2 = new Quote();
        decode(quote2, directBuffer, bufferOffset, actingBlockLength, schemaId, actingVersion);
        bufferOffset += quote2.size();

        assertEquals(quote.symbolId(), quote2.symbolId());
        assertEquals(quote.bid(), quote2.bid(), 0.0001);
        assertEquals(quote.ask(), quote2.ask(), 0.0001);
        assertEquals(quote.bidVol(), quote2.bidVol());
        assertEquals(quote.askVol(), quote2.askVol());
    }


    @Test
    public void testMultipleMessages() throws Exception{

        final MessageHeader MESSAGE_HEADER = new MessageHeader();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        final short messageTemplateVersion = 0;

        for (int i =0; i < 10; i ++) {
            encodeDecode(MESSAGE_HEADER, directBuffer, messageTemplateVersion);
        }
    }

    @Test
    public void testSequenceMessages() throws Exception{

        final MessageHeader MESSAGE_HEADER = new MessageHeader();
        final ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4096);
        final UnsafeBuffer directBuffer = new UnsafeBuffer(byteBuffer);
        final short messageTemplateVersion = 0;
        int bufferOffset = 0;

        int msgNum =50;

        for (int i =0 ;i < msgNum; i++) {
            int encodingLength = 0;
            final Quote quote = new Quote();
            // Setup for encoding a message
            MESSAGE_HEADER.wrap(directBuffer, bufferOffset, messageTemplateVersion)
                    .blockLength(quote.sbeBlockLength())
                    .templateId(quote.sbeTemplateId())
                    .schemaId(quote.sbeSchemaId())
                    .version(quote.sbeSchemaVersion());
            bufferOffset += MESSAGE_HEADER.size();
            encodingLength += MESSAGE_HEADER.size();
            encodingLength += encode(quote, directBuffer, bufferOffset, i);
            bufferOffset += quote.size();
        }

        // Decode the encoded message
        bufferOffset = 0;

        for (int i = 0; i < msgNum; i ++) {
            MESSAGE_HEADER.wrap(directBuffer, bufferOffset, messageTemplateVersion);

            // Lookup the applicable flyweight to decode this type of message based on templateId and version.
            final int templateId = MESSAGE_HEADER.templateId();
            if (templateId != Quote.TEMPLATE_ID) {
                throw new IllegalStateException("Template ids do not match");
            }

            final int actingBlockLength = MESSAGE_HEADER.blockLength();
            final int schemaId = MESSAGE_HEADER.schemaId();
            final int actingVersion = MESSAGE_HEADER.version();

            bufferOffset += MESSAGE_HEADER.size();
            final Quote quote2 = new Quote();
            decode(quote2, directBuffer, bufferOffset, actingBlockLength, schemaId, actingVersion);
            bufferOffset += quote2.size();

            assertQuote(quote2, i);
        }
    }

    public static void assertQuote(Quote quote, long id){
        assertEquals(id, quote.symbolId());
        assertEquals(98, quote.bid(), 0.0001);
        assertEquals(99, quote.ask(), 0.0001);
        assertEquals(500, quote.bidVol());
        assertEquals(329, quote.askVol());

    }

    public static int encode(final Quote quote, final UnsafeBuffer directBuffer, final int bufferOffset, int id)
    {
        final int srcOffset = 0;
        quote.wrapForEncode(directBuffer, bufferOffset)
                .symbolId(id)
                .datetime(System.currentTimeMillis())
                .bid(98)
                .ask(99)
                .bidVol(500)
                .askVol(329);



        return quote.size();
    }

    public static void decode(
            final Quote quote,
            final UnsafeBuffer directBuffer,
            final int bufferOffset,
            final int actingBlockLength,
            final int schemaId,
            final int actingVersion)
            throws Exception
    {
        final byte[] buffer = new byte[128];
        final StringBuilder sb = new StringBuilder();
        quote.wrapForDecode(directBuffer, bufferOffset, actingBlockLength, actingVersion);

//        sb.append("\ncar.templateId=").append(quote.sbeTemplateId());
//        sb.append("\ncar.schemaId=").append(schemaId);
//        sb.append("\ncar.schemaVersion=").append(quote.sbeSchemaVersion());
//        sb.append("\ncar.symbolId=").append(quote.symbolId());
//        sb.append("\ncar.datetime=").append(quote.datetime());
//        sb.append("\ncar.bid=").append(quote.bid());
//        sb.append("\ncar.ask=").append(quote.ask());
//        sb.append("\ncar.bidVol=").append(quote.bidVol());
//        sb.append("\ncar.askVol=").append(quote.askVol());
//
//        System.out.println(sb);
    }
}
