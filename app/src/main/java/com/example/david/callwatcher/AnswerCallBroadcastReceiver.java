package com.example.david.callwatcher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

public class AnswerCallBroadcastReceiver extends BroadcastReceiver{

    private static String HOST = "tcp://192.168.1.37:1883";
    private static final String TOPIC = "call";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                try {
                    BlockingConnection connection = setUpConnection();
                    connection.connect();
                    connection.subscribe(getTopics());
                    publish(number, connection);
                    connection.disconnect();
                } catch (Exception e) { }
            }

        }
    }

    private BlockingConnection setUpConnection() throws URISyntaxException {
        MQTT mqttClient = new MQTT();
        mqttClient.setHost(HOST);
        return mqttClient.blockingConnection();
    }

    private Topic[] getTopics() {
        return new Topic[]{ new Topic(utf8(TOPIC), QoS.AT_LEAST_ONCE) };
    }

    private void publish(String message, BlockingConnection connection) throws Exception {
        connection.publish(TOPIC, message.getBytes(), QoS.AT_LEAST_ONCE, false);
    }
}
