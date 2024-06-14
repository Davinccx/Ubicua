import paho.mqtt.client as mqtt
import pymysql
import json
from datetime import datetime
import threading

broker_address = "localhost"
broker_port = 1883

topic = "Ubipark/Parking1/#"

class ConnectionDDBB:
    def obtain_connection(self, autocommit=True):
        # Proporciona tu configuración de conexión aquí
        connection = pymysql.connect(
            host='vendifydb.mysql.database.azure.com',         # Reemplaza con la dirección de tu host
            user='davinccx',      # Reemplaza con tu usuario
            password='Vendify2024',# Reemplaza con tu contraseña
            database='parking',  # Reemplaza con el nombre de tu base de datos
            autocommit=autocommit
        )
        return connection
    
def entrada_parking(matricula, id_parking):
    sql = "INSERT INTO tiempoparking (tiempo_entrada, matricula, id_parking) VALUES(%s, %s, %s)"
    con = None
    ps = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()        

        with con.cursor() as cursor:
            timestamp_actual = datetime.now()
            cursor.execute(sql, (timestamp_actual, matricula, id_parking))
            con.commit()
            rows_affected = cursor.rowcount
        
    except pymysql.MySQLError as e:
        
        print(e)

    finally:
        if con:
            con.close()


def salida_reserva(matricula):
    sql = """
    UPDATE reservas AS r
    INNER JOIN users AS u ON r.user_id = u.user_id
    SET r.hora_fin = NOW()
    WHERE u.matricula = %s
    """
    con = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()

        with con.cursor() as cursor:
            cursor.execute(sql, (matricula,))
            con.commit()

    except pymysql.MySQLError as e:
        print(e)

    finally:
        if con:
            con.close()

def salida_parking(matricula):
    sql = "UPDATE tiempoparking SET tiempo_salida = %s WHERE matricula = %s"
    con = None
    ps = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()


        with con.cursor() as cursor:
            timestamp_salida = datetime.now()
            cursor.execute(sql, (timestamp_salida, matricula))
            con.commit()
            rows_affected = cursor.rowcount
            

    except pymysql.MySQLError as e:
        print(e)

    finally:
        if con:
            con.close()

def mover_datos(matricula):
    sql_insert = """
        INSERT INTO historico (matricula, id_parking, hora_inicio, hora_fin)
        SELECT matricula, id_parking, tiempo_entrada, tiempo_salida
        FROM tiempoparking
        WHERE matricula = %s;
    """
    
    sql_delete = """
        DELETE FROM tiempoparking
        WHERE matricula = %s;
    """
    
    con = None
    ps = None

    try:
        # Establecer la conexión con la base de datos
        conector = ConnectionDDBB()
        con = conector.obtain_connection()
        
        # Crear un cursor para ejecutar los comandos SQL
        ps = con.cursor()
        
        # Iniciar una transacción
        con.autocommit(False)
        
        # Ejecutar el comando de inserción
        ps.execute(sql_insert, (matricula,))
        
        # Ejecutar el comando de eliminación
        ps.execute(sql_delete, (matricula,))
        
        # Confirmar la transacción
        con.commit()
        
    except (Exception, pymysql.DatabaseError) as error:
        # Si ocurre un error, revertir la transacción
        if con is not None:
            con.rollback()
        print(f"Error: {error}")
        
    finally:
        # Cerrar el cursor y la conexión
        if ps is not None:
            ps.close()
        if con is not None:
            con.close()

def estado_plaza(id_parking,id_plaza,estado):
    sql = "UPDATE plaza SET ocupado = %s WHERE id_plaza = %s AND id_parking = %s"

    con = None
    ps = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()


        with con.cursor() as cursor:
           
            cursor.execute(sql, (estado, id_plaza,id_parking))
            con.commit()
            rows_affected = cursor.rowcount
            

    except pymysql.MySQLError as e:
        print(e)

    finally:
        if con:
            con.close()

def insertar_importe(matricula,importe):
    sql = "UPDATE historico SET importe = %s WHERE matricula = %s AND  importe IS NULL"
    con = None
    ps = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()


        with con.cursor() as cursor:
            cursor.execute(sql, (importe,matricula))
            con.commit()
            rows_affected = cursor.rowcount
            

    except pymysql.MySQLError as e:
        print(e)

    finally:
        if con:
            con.close()


def tiempo_parking(matricula):
    diferencia_segundos = 0

    sql = "SELECT TIMESTAMPDIFF(SECOND, tiempo_entrada, tiempo_salida) AS diferencia_segundos FROM tiempoparking WHERE matricula = %s"
    con = None
    ps = None
    rs = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()

        with con.cursor() as cursor:
            ps = cursor
            ps.execute(sql, (matricula,))
            rs = ps.fetchone()
            if rs:
                diferencia_segundos = rs[0]

    except pymysql.MySQLError as e:
        print("Error retrieving tiempoParking: " + str(e))

    finally:
        try:
            if rs:
                rs = None  # Liberar la memoria asignando None a rs
            if ps:
                ps.close()
            if con:
                con.close()
        except pymysql.MySQLError as e:
            print("Error closing resources: " + str(e))

    return diferencia_segundos

def tiene_reserva(matricula):
    sql = """
    SELECT COUNT(*) 
    FROM users AS u
    INNER JOIN reservas AS r ON u.user_id = r.user_id
    WHERE u.matricula = %s
    """
    con = None

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()

        with con.cursor() as cursor:
            cursor.execute(sql, (matricula))
            reservation_count = cursor.fetchone()[0]
            
            return reservation_count  # Devolver el resultado de la consulta

    except pymysql.MySQLError as e:
        print(e)
        return None  # En caso de error, devuelve None

    finally:
        if con:
            con.close()

def on_connect(client, userdata,flag,rc):
    print("Conectado al broker MQTT en el host: "+broker_address)
    client.subscribe(topic)
    print("Suscrito a "+topic)

def fetch_new_inserts():
    sql = "SELECT id, user_id, parking_id, fecha_reserva, hora_inicio, id_plaza FROM notification_table"
    delete_sql = "DELETE FROM notification_table WHERE id = %s"
    con = None
    new_inserts = []

    try:
        conector = ConnectionDDBB()
        con = conector.obtain_connection()

        with con.cursor() as cursor:
            cursor.execute(sql)
            new_inserts = cursor.fetchall()

            # Delete the fetched records from notification_table
            for record in new_inserts:
                cursor.execute(delete_sql, (record[0],))

            con.commit()

    except pymysql.MySQLError as e:
        print(e)

    finally:
        if con:
            con.close()
    return new_inserts

def fetch_and_process_inserts():
    while True:
        new_inserts = fetch_new_inserts()
        for insert in new_inserts:
            user_id = insert[1]
            parking_id = insert[2]
            fecha_reserva = insert[3].strftime('%Y-%m-%d')
            hora_inicio = insert[4].strftime('%H:%M:%S')
            id_plaza = insert[5]

            data = {
                "user_id": user_id,
                "parking_id": parking_id,
                "fecha_reserva": fecha_reserva,
                "hora_inicio": hora_inicio,
                "id_plaza": id_plaza
            }

            if(parking_id==4):
                client.publish("Ubipark/Parking1/P1/Reserva", 1)
            else:
                print("Se ha recibido una reserva"+json.dumps(data))
            

def on_message(client,userdata,msg):
    topic = msg.topic
    payload = msg.payload.decode()
    jsonObject = json.loads(payload)

    if "Ubipark/Parking1/Maquina/Entrada" in topic:
            
            matricula = jsonObject.get("matricula")
            id_parking = jsonObject.get("id_parking")
            entrada_parking(matricula, id_parking)

    elif "Ubipark/Parking1/Maquina/Salida" in topic:
         
         matricula = jsonObject.get("matricula")
         if(tiene_reserva(matricula)):
             
             salida_parking(matricula)
             salida_reserva(matricula)
             tiempo = tiempo_parking(matricula)
             client.publish("Ubipark/Parking1/Maquina/Respuesta", str(tiempo))
             client.publish("Ubipark/Parking1/P1/Reserva", 0)
             
         
         else:
            salida_parking(matricula)
            tiempo = tiempo_parking(matricula)
            client.publish("Ubipark/Parking1/Maquina/Respuesta", str(tiempo))
            

    elif "Ubipark/Parking1/P1/Estado" in topic:
        
        id_plaza = jsonObject.get("id_plaza")
        id_parking = jsonObject.get("id_parking")
        estado = jsonObject.get("estado")
        estado_plaza(id_parking,id_plaza,estado)

    elif "Ubipark/Parking1/Maquina/Pago" in topic:
        
        cantidad = jsonObject.get("importe")
        matricula = jsonObject.get("matricula")
        insertar_importe(matricula,cantidad) 
        mover_datos(matricula)        
   
client = mqtt.Client()
client.on_connect = on_connect
client.on_message = on_message

client.connect(broker_address, broker_port)

mqtt_thread = threading.Thread(target=client.loop_forever)
fetch_thread = threading.Thread(target=fetch_and_process_inserts)

mqtt_thread.start()
fetch_thread.start()