package servlets;

import java.util.Random;

public class GeneradorDatos {

    private static final String[] USERNAMES = {
        "user", "member", "guest", "contact", "info", "support", "service"
    };

    private static final String[] DOMAINS = {
        "example", "test", "demo", "vendify", "webmail"
    };

    private static final String[] TLDs = {
        "com", "net", "es", "edu", "uah", "gov"
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

}
