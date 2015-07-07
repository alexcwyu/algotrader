/**
 *  Copyright (c) 2011-2014 Exxeleron GmbH
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import com.exxeleron.qjava.*;
import com.exxeleron.qjava.QConnection.MessageType;
import com.unisoft.algotrader.model.event.data.Trade;

import java.io.IOException;
import java.util.Date;
import java.util.TimeZone;

public class SyncQuery {

    static final TimeZone TIME_ZONE = TimeZone.getDefault();
    static long tzOffsetToQ(long var0) {
        return var0 - (long)TIME_ZONE.getOffset(var0 - (long)TIME_ZONE.getOffset(var0));
    }

    public static void main( final String[] args ) throws IOException {
        // create connection to localhost:5001 with login myUser and password myPassword
        final QConnection q = new QBasicConnection("localhost", 5000, null, null);
        try {
            q.open(); // open connection

            // low level query
            q.query(MessageType.SYNC, "select from trade");
            final QMessage message = (QMessage) q.receive(false, false);

            QTable table = (QTable)message.getData();
            Object[] data = (Object[])table.getData();
            QDate[] col0 = (QDate[] )data[0];
            QTime[] col1 = (QTime[] )data[1];
            String[] col2 = (String[] )data[2];
            float[] col3 = (float[] )data[3];
            int[] col4 = (int[] )data[4];


            for (int i =0 ; i< table.getRowsCount(); i++) {
                QTable.Row row = table.get(i);
                Trade trade = new Trade(
                        (int)row.get(2),
                        0,
                        (float)row.get(3),
                        (int)row.get(4));
                System.out.println(((QDate)row.get(0)).toDateTime());
                System.out.println(((QTime)row.get(1)).toDateTime());
                System.out.println(trade);

                System.out.println(new Date(tzOffsetToQ(946684800000L + 86400000L * ((QDate)row.get(0)).getValue() + ((QTime)row.get(1)).getValue())).getTime());
            }



            System.out.println(new Date(946684800000L));
        } catch ( final QException e ) {
            System.err.println(e);
        } finally {
            q.close(); // close connection
        }
    }

}
