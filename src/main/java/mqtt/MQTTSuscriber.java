package mqtt;

import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import logic.Log;


public class MQTTSuscriber implements MqttCallback {

    public void suscribeToUbiparkTopics(MQTTBroker broker) {

        ArrayList<String> topics = new ArrayList<>();
        topics.add(MQTTBroker.UBIPARK_TOPICS);
        suscribeTopic(broker,topics);

    }

    public void suscribeTopic(MQTTBroker broker,ArrayList<String> topics) {
        Log.logmqtt.debug("Suscribe to topics");
        MemoryPersistence persistence = new MemoryPersistence();
        try {
            MqttClient sampleClient = new MqttClient(MQTTBroker.getBroker(), MQTTBroker.getClientId(), persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setAutomaticReconnect(true);
            Log.logmqtt.debug("Mqtt Connecting to broker: " + MQTTBroker.getBroker());
            sampleClient.connect(connOpts);
            Log.logmqtt.debug("Mqtt Connected");
            sampleClient.setCallback(this);
            for (String topic : topics) {
                sampleClient.subscribe(topic);
                Log.logmqtt.info("Subscribed to {}", topic);

            }

        } catch (MqttException me) {
            Log.logmqtt.error("Error suscribing topic: {}", me);
        } catch (Exception e) {
            Log.logmqtt.error("Error suscribing topic: {}", e);
        }
    }

    @Override
    public void connectionLost(Throwable cause) {
    }

    @Override
    public void messageArrived(String topic, MqttMessage message) throws Exception {
        try {
        if (topic.contains("parking/entrada")) {
            Log.logmqtt.info("Received mqtt msg {}: {}", topic, message.toString());
        }
        } catch (Exception e) {
            Log.logmqtt.error("Error processing message: {}", e);
        }
      

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

   
 
   
}
