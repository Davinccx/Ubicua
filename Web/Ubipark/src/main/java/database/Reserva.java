package database;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;


public class Reserva {
    
    private int reserva_id;
    private int user_id;
    private int parking_id;
    private Date fecha_reserva;
    private Timestamp hora_inicio;
    private Timestamp hora_fin;
    private int id_plaza;
    
    public Reserva() {
    }

    //Setter
    public void setReserva_id(int reserva_id) {
        this.reserva_id = reserva_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public void setParking_id(int parking_id) {
        this.parking_id = parking_id;
    }

    public void setFecha_reserva(Date fecha_reserva) {
        this.fecha_reserva = fecha_reserva;
    }

    public void setHora_inicio(Timestamp hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public void setHora_fin(Timestamp hora_fin) {
        this.hora_fin = hora_fin;
    }

    public void setId_plaza(int id_plaza) {
        this.id_plaza = id_plaza;
    }

    
    
    
    //Getter
    public int getReserva_id() {
        return reserva_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getParking_id() {
        return parking_id;
    }

    public Date getFecha_reserva() {
        return fecha_reserva;
    }

    public Timestamp getHora_inicio() {
        return hora_inicio;
    }

    public Timestamp getHora_fin() {
        return hora_fin;
    }

    public int getId_plaza() {
        return id_plaza;
    }
    
    
    
    
}
