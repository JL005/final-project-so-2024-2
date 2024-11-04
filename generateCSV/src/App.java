import java.util.Base64;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import app.EncryptedCSVGenerator;

public class App {
    public static void main(String[] args) throws Exception {

        EncryptedCSVGenerator generator = new EncryptedCSVGenerator();
        generator.generateEncryptedCSV("datos_encriptados.csv");

        /*
         * 
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(128); // Puedes cambiar a 192 o 256 si tu entorno lo soporta
        SecretKey secretKey = keyGenerator.generateKey();

        // Codificar la clave en Base64 para almacenarla como una cadena
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Clave de encriptación generada: " + encodedKey);
        */
    }
}
