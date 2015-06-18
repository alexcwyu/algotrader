package com.unisoft.algotrader.data;

import com.unisoft.algotrader.core.id.InstId;
import com.unisoft.algotrader.event.data.Bar;
import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectRowProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

/**
 * Created by alex on 5/16/15.
 */
public class CSVTest {
    private static final Logger LOG = LogManager.getLogger(CSVTest.class);
    public static SimpleDateFormat FORMAT= new SimpleDateFormat("yyyyMMdd");
    public static void main(String ... args) throws Exception{
        CsvParserSettings settings = new CsvParserSettings();
        //the file used in the example uses '\n' as the line separator sequence.
        //the line separator sequence is defined here to ensure systems such as MacOS and Windows
        //are able to process this file correctly (MacOS uses '\r'; and Windows uses '\r\n').
        settings.getFormat().setLineSeparator("\n");
        settings.getFormat().setDelimiter(';');
        settings.setHeaderExtractionEnabled(false);


        // creates a CSV parser
        Reader reader = new FileReader("/mnt/data/trading/data/HSI.txt");

        read2(settings, reader);
    }

    public static void read1(CsvParserSettings settings, Reader reader)throws Exception{
        // parses all rows in one go.
        CsvParser parser = new CsvParser(settings);
        List<String[]> allRows = parser.parseAll(reader);
    }

    public static void read2(CsvParserSettings settings, Reader reader)throws Exception{
        // call beginParsing to read records one by one, iterator-style.

        CsvParser parser = new CsvParser(settings);
        parser.beginParsing(reader);

        String[] row;
        int size = 60*60*24;
        while ((row = parser.parseNext()) != null) {
            Bar bar = new Bar(InstId.Builder.as().symbol("HSI").exchId("HKEX").build(),
                    size,
            FORMAT.parse(row[0]).getTime(),
            Double.parseDouble(row[1]),
            Double.parseDouble(row[2]),
            Double.parseDouble(row[3]),
            Double.parseDouble(row[4]),
            Integer.parseInt(row[5]),
                    0);
            LOG.info(bar);
        }

        // The resources are closed automatically when the end of the input is reached,
        // or when an error happens, but you can call stopParsing() at any time.

        // You only need to use this if you are not parsing the entire content.
        // But it doesn't hurt if you call it anyway.
        parser.stopParsing();
    }

    public static void read3(CsvParserSettings settings, Reader reader)throws Exception{
        ObjectRowProcessor rowProcessor = new ObjectRowProcessor() {
            @Override
            public void rowProcessed(Object[] row, ParsingContext context) {
                //here is the row. Let's just print it.
                LOG.info(Arrays.toString(row));
            }
        };
        settings.setRowProcessor(rowProcessor);
        CsvParser parser = new CsvParser(settings);
        parser.parse(reader);
    }



}
