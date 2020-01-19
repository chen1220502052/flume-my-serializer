package org.example.hbase;

import com.google.common.base.Charsets;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.conf.ComponentConfiguration;
import org.apache.flume.sink.hbase.HbaseEventSerializer;
import org.apache.flume.sink.hbase.SimpleHbaseEventSerializer;
import org.apache.hadoop.hbase.client.Increment;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Row;

import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

public class MyRowKeySerializer implements HbaseEventSerializer {

    private String rowPrefix;
    private byte[] incrementRow;
    private byte[] cf;
    private byte[] plCol;
    private byte[] incCol;
    private SimpleHbaseEventSerializer.KeyType keyType;
    private byte[] payload;

    public MyRowKeySerializer() {
    }

    @Override
    public void configure(Context context) {
        rowPrefix = context.getString("rowPrefix", "default");
        incrementRow =
                context.getString("incrementRow", "incRow").getBytes(Charsets.UTF_8);
        String suffix = context.getString("suffix", "uuid");

        String payloadColumn = context.getString("payloadColumn", "pCol");
        String incColumn = context.getString("incrementColumn", "iCol");
        if (payloadColumn != null && !payloadColumn.isEmpty()) {
            if (suffix.equals("timestamp")) {
                keyType = SimpleHbaseEventSerializer.KeyType.TS;
            } else if (suffix.equals("random")) {
                keyType = SimpleHbaseEventSerializer.KeyType.RANDOM;
            } else if (suffix.equals("nano")) {
                keyType = SimpleHbaseEventSerializer.KeyType.TSNANO;
            } else {
                keyType = SimpleHbaseEventSerializer.KeyType.UUID;
            }
            plCol = payloadColumn.getBytes(Charsets.UTF_8);
        }
        if (incColumn != null && !incColumn.isEmpty()) {
            incCol = incColumn.getBytes(Charsets.UTF_8);
        }
    }

    @Override
    public void configure(ComponentConfiguration conf) {
    }

    @Override
    public void initialize(Event event, byte[] cf) {
        this.payload = event.getBody();
        this.cf = cf;
    }

    @Override
    public List<Row> getActions() throws FlumeException {
        List<Row> actions = new LinkedList<Row>();
        if (plCol != null) {
            byte[] rowKey;
            try {
                String value = new String(payload, Charset.forName("UTF-8"));
                String[] elemets = value.split("_");
                String key = "0";
                if(elemets != null && elemets.length > 1){
                    key = elemets[0];
                }
                key = key + "_" + LocalDate.now().toString().replaceAll("-", "");
                rowKey = key.getBytes(Charset.forName("UTF-8"));
                Put put = new Put(rowKey);
                put.add(cf, plCol, payload);
                actions.add(put);
            } catch (Exception e) {
                throw new FlumeException("Could not get row key!", e);
            }

        }
        return actions;
    }

    @Override
    public List<Increment> getIncrements() {
        List<Increment> incs = new LinkedList<Increment>();
//        if (incCol != null) {
//            Increment inc = new Increment(incrementRow);
//            inc.addColumn(cf, incCol, 1);
//            incs.add(inc);
//        }
        return incs;
    }

    @Override
    public void close() {
    }

}
