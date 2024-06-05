
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import logic.Logic;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */

/**
 *
 * @author David
 */
public class prueba {


    public static void main(String[] args) throws ParseException {
        
            String username = "Davinci";
            System.out.println("Username: " + username);

            List<Integer> ids = Logic.getUsersID();
          
            System.out.println("User ID: " + ids);
            String h_inicio = "17:00";
            String h_fin = "20:00";
            String fecha = "2024-08-06";
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            String fechaHoraInicio = fecha + " " + h_inicio;
            java.util.Date utilFechaHoraInicio = formatoFechaHora.parse(fechaHoraInicio);
            Timestamp horaInicio = new Timestamp(utilFechaHoraInicio.getTime());
            System.out.println("Timestamp Hora Inicio: " + horaInicio);

            String fechaHoraFin = fecha + " " + h_fin;
            java.util.Date utilFechaHoraFin = formatoFechaHora.parse(fechaHoraFin);
            Timestamp horaFin = new Timestamp(utilFechaHoraFin.getTime());
            System.out.println("Timestamp Hora Fin: " + horaFin);
            
            
    }
    
}
