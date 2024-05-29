package mqtt;


import database.Maquina;
import database.Producto;
import database.User;
import database.Venta;
import java.util.ArrayList;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import logic.Log;
import logic.Logic;
import org.json.JSONObject;

public class MQTTSuscriber implements MqttCallback {

    public void suscribeToMaquetaTopics(MQTTBroker broker) {

        ArrayList<String> topics = new ArrayList<>();
        topics.add(MQTTBroker.VENDIFY_TOPICS);
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
        Log.logmqtt.info("Received mqtt msg {}: {}", topic, message.toString());
        

        if (topic.contains("maquina/info")) {

            Logic.insertMaquina(getMachine(message));

        }else if(topic.contains("maquina/producto")){
        
            Logic.insertProducto(getProducto(message));
        
        }else if(topic.contains("maquina/compras")){
        
            Logic.insertVenta(getVenta(message));
        }
        

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
    }

   
 
    private Maquina getMachine(MqttMessage message) {

        Maquina maquina = new Maquina();

        try {
            String payload = new String(message.getPayload());
            JSONObject json = new JSONObject(payload);
            if(json!=null){
            
                maquina.setId(json.getInt("id"));
                maquina.setLocation(json.getString("location"));
                maquina.setSaldo(json.getInt("saldo"));
            
            
                  
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return maquina;
    }
    
    
    private Venta getVenta(MqttMessage message) {

        Venta venta = new Venta();

        try {
            String payload = new String(message.getPayload());
            JSONObject json = new JSONObject(payload);
            if (json != null) {

                
                venta.setId_user(1);
                venta.setId_producto(json.getInt("id_producto"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return venta;
    }
    
    private Producto getProducto(MqttMessage message) {

        Producto producto = new Producto();

        try {
            String payload = new String(message.getPayload());
            JSONObject json = new JSONObject(payload);
            if(json!=null){
            
                producto.setId(json.getInt("id"));
                producto.setNombre(json.getString("nombre"));
                producto.setDescripcion(json.getString("descripcion"));
                producto.setPrecio(json.getInt("precio"));
            
                  
            }
            

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return producto;
    }

   
}
