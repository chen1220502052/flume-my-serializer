package org.example.flume.client;

import org.apache.flume.Event;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;

public class FlumeClient {
    private static Logger logger = LoggerFactory.getLogger(FlumeClient.class);

    private RpcClient client;
    private String hostname;
    private int port;

    public void init(String hostname, int port){
        this.hostname = hostname;
        this.port = port;
        this.client = RpcClientFactory.getDefaultInstance(hostname, port);
    }

    public void sendDataToFlume(String data){
        Event envent = EventBuilder.withBody(data, Charset.forName("utf-8"));
        try{
            client.append(envent);
        }catch (Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
            client.close();
            client = null;
            client = RpcClientFactory.getDefaultInstance(hostname, port);
        }
    }

    public void cleanUp(){
        client.close();
    }

    public static void main(String[] args){
        FlumeClient flumeClient = new FlumeClient();
        flumeClient.init("10.27.14.179", 41414);
        flumeClient.sendDataToFlume("hello word");
        flumeClient.cleanUp();
    }
}
