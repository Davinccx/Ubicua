package servlets;

import java.util.Random;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.ThreadLocalRandom;

public class GeneradorDatos {

    private static final int MIN_CP = 1000;  // Código postal más bajo (sin ceros iniciales)
    private static final int MAX_CP = 52999;
    
    private static final String[] USERNAMES = {
        "user", "member", "guest", "contact", "info", "support", "service"
    };

    private static final String[] DOMAINS = {
        "example", "test", "demo", "ubipark", "webmail"
    };

    private static final String[] TLDs = {
        "com", "net", "es", "edu", "uah", "gov"
    };
    
    private static final String[] CALLES = {
        "Calle A", "Calle B", "Calle C", "Calle D", "Calle E", "Calle F", "Calle G",
        "Calle H", "Calle I", "Calle J", "Calle K", "Calle L", "Calle M", "Calle N"
    };
    
    private static final int MAX_NUMERO = 200;
    private static final String[] CIUDADES = {
        "Madrid", "Barcelona", "Valencia", "Sevilla", "Zaragoza", "Málaga", "Murcia",
        "Palma", "Las Palmas", "Bilbao", "Alicante", "Córdoba", "Valladolid", "Vigo"
    };
    
    private static final String[] PARKINGS = {
        "Parking", "Garaje", "Aparcamiento", "Estacionamiento", "Parqueadero", "Párking",
        "Zona de Parking", "Área de Parking", "Plaza de Parking", "Parking Cubierto",
        "Parking Descubierto", "Garaje Subterráneo", "Aparcamiento Central", "Parking VIP"
    };
    
    private static final String[] NOMBRES = {
        "Alejandro", "María", "Carlos", "Lucía", "David", "Sofía", "Manuel", "Ana",
        "José", "Marta", "Miguel", "Elena", "Antonio", "Isabel", "Juan", "Carmen",
        "Pedro", "Laura", "Francisco", "Sara"
    };
    
    private static final String[] APELLIDOS = {
        "García", "Martínez", "López", "Sánchez", "González", "Pérez", "Rodríguez", 
        "Fernández", "Gómez", "Martín", "Jiménez", "Ruiz", "Hernández", "Díaz", 
        "Moreno", "Álvarez", "Muñoz", "Romero", "Alonso", "Gutiérrez"
    };

    private static final String[] ADJECTIVES = {
        "Quick", "Lazy", "Happy", "Sad", "Angry", "Brave", "Clever", "Lucky"
    };

    private static final String[] NOUNS = {
        "Fox", "Dog", "Cat", "Mouse", "Lion", "Tiger", "Bear", "Wolf"
    };
    
    
 
    
    private static final String UPPERCASE_LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE_LETTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()-_+=<>?";
    private static final String ALL_CHARACTERS = UPPERCASE_LETTERS + LOWERCASE_LETTERS + DIGITS + SPECIAL_CHARACTERS;
    private static final int PASSWORD_LENGTH = 12;
    

    private static final Random RANDOM = new Random();

    public static String generarEmail() {

        String username = USERNAMES[RANDOM.nextInt(USERNAMES.length)];
        String domain = DOMAINS[RANDOM.nextInt(DOMAINS.length)];
        String tld = TLDs[RANDOM.nextInt(TLDs.length)];

        int number = RANDOM.nextInt(1000);

        return username + number + "@" + domain + "." + tld;
    }

    public static String generarUsername() {
        String adjective = ADJECTIVES[RANDOM.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[RANDOM.nextInt(NOUNS.length)];
        int number = RANDOM.nextInt(1000);  // Agregar un número para hacer el nombre de usuario más único

        return adjective + noun + number;
    }
    
    public static String generarNombre(){
    
        return NOMBRES[RANDOM.nextInt(NOMBRES.length)];
        
    }
    
    public static String generarApellido(){
    
        return APELLIDOS[RANDOM.nextInt(APELLIDOS.length)];
        
    }

    public static String generarTelefono() {
        int firstDigit = RANDOM.nextBoolean() ? 6 : 7; // Elige aleatoriamente entre 6 y 7
        int[] digits = new int[8];

        for (int i = 0; i < 8; i++) {
            digits[i] = RANDOM.nextInt(10); // Genera un dígito aleatorio entre 0 y 9
        }

        StringBuilder phoneNumber = new StringBuilder();
        phoneNumber.append(firstDigit);
        for (int digit : digits) {
            phoneNumber.append(digit);
        }

        return phoneNumber.toString();
    }
    
    public static String generarPassword() {
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH);

        // Asegurarse de que la contraseña tenga al menos un carácter de cada tipo
        password.append(UPPERCASE_LETTERS.charAt(RANDOM.nextInt(UPPERCASE_LETTERS.length())));
        password.append(LOWERCASE_LETTERS.charAt(RANDOM.nextInt(LOWERCASE_LETTERS.length())));
        password.append(DIGITS.charAt(RANDOM.nextInt(DIGITS.length())));
        password.append(SPECIAL_CHARACTERS.charAt(RANDOM.nextInt(SPECIAL_CHARACTERS.length())));

        // Llenar el resto de la contraseña con caracteres aleatorios
        for (int i = 4; i < PASSWORD_LENGTH; i++) {
            password.append(ALL_CHARACTERS.charAt(RANDOM.nextInt(ALL_CHARACTERS.length())));
        }

        // Mezclar los caracteres de la contraseña
        char[] passwordArray = password.toString().toCharArray();
        for (int i = passwordArray.length - 1; i > 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            char temp = passwordArray[i];
            passwordArray[i] = passwordArray[j];
            passwordArray[j] = temp;
        }

        return new String(passwordArray);
    }
    
    
    public static String generarNombreParking(){
        
        return PARKINGS[RANDOM.nextInt(PARKINGS.length)];
        
    }
   
    public static String generarLocalizacion(){
        
        return CIUDADES[RANDOM.nextInt(CIUDADES.length)];
        
    }
    
    public static String generarDireccion() {
        Random random = new Random();
        String calle = CALLES[random.nextInt(CALLES.length)];
        int numero = random.nextInt(MAX_NUMERO) + 1; // Evitar el número 0
        
        return calle + " " + numero;
    }

    public static String generarCodigoPostal() {
        Random random = new Random();
        int cp = random.nextInt(MAX_CP - MIN_CP + 1) + MIN_CP;
        return String.format("%05d", cp);
    }
    
    public static Date generarFecha(Date startDate, Date endDate) {
        
        
        long startMillis = startDate.getTime();
        long endMillis = endDate.getTime();
        long randomMillis = ThreadLocalRandom.current().nextLong(startMillis, endMillis + 1);

        return new Date(randomMillis);
    }
    
    public static Timestamp generarHoraAleatoria(Date fecha, int startHour, int endHour) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);

        int randomHour = ThreadLocalRandom.current().nextInt(startHour, endHour);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
        int randomSecond = ThreadLocalRandom.current().nextInt(0, 60);

        calendar.set(Calendar.HOUR_OF_DAY, randomHour);
        calendar.set(Calendar.MINUTE, randomMinute);
        calendar.set(Calendar.SECOND, randomSecond);

        return new Timestamp(calendar.getTimeInMillis());
    }

    public static Timestamp generarHoraAleatoria(Date fecha) {
        return generarHoraAleatoria(fecha, 0, 24);
    }

    public static Timestamp generarHoraFin(Timestamp horaInicio) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(horaInicio.getTime());

        int randomHour = ThreadLocalRandom.current().nextInt(calendar.get(Calendar.HOUR_OF_DAY) + 1, 24);
        int randomMinute = ThreadLocalRandom.current().nextInt(0, 60);
        int randomSecond = ThreadLocalRandom.current().nextInt(0, 60);

        calendar.set(Calendar.HOUR_OF_DAY, randomHour);
        calendar.set(Calendar.MINUTE, randomMinute);
        calendar.set(Calendar.SECOND, randomSecond);

        return new Timestamp(calendar.getTimeInMillis());
    }
    
}
