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

    public void suscribeToMaquetaTopics() {

        ArrayList<String> topics = new ArrayList<>();
        topics.add(MQTTBroker.VENDIFY_TOPICS);
        suscribeTopic(topics);

    }

    public void suscribeTopic(ArrayList<String> topics) {
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

    private void handleAuthentication(MqttMessage message) {

        String logged = "false";

        ArrayList<User> usuarios = Logic.getUsersFromDB();
        try {
            String payload = new String(message.getPayload());
            JSONObject json = new JSONObject(payload);
            User usuarioDeseado = new User();
            if (json.has("id_usuario") && json.has("password")) {
                String usuarioArduino = json.getString("id_usuario");
                String contrasenaArduino = json.getString("password");

                for (User usuario : usuarios) {

                    if (usuario.getId() == Integer.parseInt(usuarioArduino) && usuario.getPassword() == Integer.parseInt(contrasenaArduino)) {

                        logged = "true";
                        usuarioDeseado = usuario;
                    }

                }

            }

            if (usuarioDeseado != null) {
                JSONObject userJson = new JSONObject();
                userJson.put("id", usuarioDeseado.getId());
                userJson.put("email", usuarioDeseado.getEmail());
                userJson.put("username", usuarioDeseado.getUsername());
                userJson.put("telefono", usuarioDeseado.getTelefono());
                userJson.put("saldo", usuarioDeseado.getSaldo());

                MQTTPublisher.publish("servidor/autentificacion", userJson.toString());
                
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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
                maquina.setId_producto(json.getInt("id_producto"));
            
                  
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

    private void searchProducto(MqttMessage message) {

        ArrayList<Maquina> maquinas = Logic.getMaquinasFromDB();
        ArrayList<Producto> productos = Logic.getProductosFromDB();

        try {
            String payload = new String(message.getPayload());
            JSONObject json = new JSONObject(payload);
            int productoBuscar=3;

            if (json.has("id_maquina")) {
                String idMaquina = json.getString("id_maquina");
                Maquina maquinaDeseada = null;
                Producto productoDeseado = null;

                // Buscar la maquina deseada
                for (Maquina maquina : maquinas) {
                    if (maquina.getId() == Integer.parseInt(idMaquina)) {
                        maquinaDeseada = maquina;
                        productoBuscar = maquinaDeseada.getId_producto();
                        break; // Salir del bucle una vez encontrada la maquina
                    }
                }
                
                for(Producto product: productos){
                    
                    if(product.getId() == 3){
                    
                        productoDeseado = product;
                        break;
                    }
                
                
                }
                
                if (productoDeseado != null) {
                    JSONObject productoJson = new JSONObject();
                    productoJson.put("id", productoDeseado.getId());
                    productoJson.put("nombre", productoDeseado.getNombre());
                    productoJson.put("precio", productoDeseado.getPrecio());

                    MQTTPublisher.publish("servidor/devolverProducto", productoJson.toString());
                } else {
                    // Manejar el caso en que la máquina no se encuentra
                }
               
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
