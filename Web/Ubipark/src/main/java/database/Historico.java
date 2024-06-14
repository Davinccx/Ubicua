
package database;

import java.sql.Timestamp;

public class Historico {
    
    private int id_historico;
    private String matricula;
    private int id_parking;
    private Timestamp hora_inicio;
    private Timestamp hora_fin;
    private int importe;
    

    public int getId_historico() {
        return id_historico;
    }

    public void setId_historico(int id_historico) {
        this.id_historico = id_historico;
    }

    public int getId_parking() {
        return id_parking;
    }

    public void setId_parking(int id_parking) {
        this.id_parking = id_parking;
    }

    public Timestamp getHora_inicio() {
        return hora_inicio;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    
    public void setHora_inicio(Timestamp hora_inicio) {
        this.hora_inicio = hora_inicio;
    }

    public Timestamp getHora_fin() {
        return hora_fin;
    }

    public void setHora_fin(Timestamp hora_fin) {
        this.hora_fin = hora_fin;
    }

    public int getImporte() {
        return importe;
    }

    public void setImporte(int importe) {
        this.importe = importe;
    }
    
    
    
}
