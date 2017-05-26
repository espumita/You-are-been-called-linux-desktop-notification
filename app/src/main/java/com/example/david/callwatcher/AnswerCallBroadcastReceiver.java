package com.example.david.callwatcher;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.EditText;

import org.fusesource.mqtt.client.BlockingConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.net.URISyntaxException;

import static org.fusesource.hawtbuf.UTF8Buffer.utf8;

public class AnswerCallBroadcastReceiver extends BroadcastReceiver{

    private static String HOST ="";
    private static final String TOPIC = "call";
    private MQTT mqttClient;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("android.intent.action.PHONE_STATE")){
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            }
            else if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
                try {
                    loadHost();
                    BlockingConnection connection = setUpConnection();
                    connection.connect();
                    connection.subscribe(getTopics());
                    publish(TelephonyManager.EXTRA_INCOMING_NUMBER, connection);
                    connection.disconnect();
                } catch (Exception e) {

                }
            }
            else if(state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            }
        }
    }
    private void loadHost() {
        HOST ="tcp://192.168.1.37:1883";
    }

    private BlockingConnection setUpConnection() throws URISyntaxException {
        mqttClient = new MQTT();
        mqttClient.setClientId("1234");
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
