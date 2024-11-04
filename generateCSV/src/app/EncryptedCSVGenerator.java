package app;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Random;

public class EncryptedCSVGenerator {

    private static final String[] HEADERS = {
        "numero_tarjeta_debito", "cvv", "fecha_expiracion",
        "dueno_cuenta", "numero_cuenta", "banco"
    };

    private static final String[] BANK_NAMES = {
        "Banco Ejemplo", "Banco Central", "Banco Popular", "Banco Nacional"
    };

    private static final String ENCRYPTION_KEY = "ffE/rlqAydJr9jBqOVZRBA=="; // Clave de 16 caracteres para AES
    private Cipher cipher;
    private SecretKey secretKey;
    private Random random = new Random();

    public EncryptedCSVGenerator() throws Exception {
        // Crear la clave de encriptación directamente en el código
        secretKey = new SecretKeySpec(ENCRYPTION_KEY.getBytes(StandardCharsets.UTF_8), "AES");
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    }

    public void generateEncryptedCSV(String fileName) {
        try (FileWriter csvWriter = new FileWriter(fileName)) {
            // Escribir las cabeceras
            csvWriter.append(String.join(",", HEADERS)).append("\n");

            // Generar y escribir 100 filas de datos aleatorios encriptados
            for (int i = 0; i < 10000000; i++) {
                String[] row = generateRandomData();
                for (int j = 0; j < row.length; j++) {
                    String encryptedData = encrypt(row[j]);
                    csvWriter.append(encryptedData);
                    if (j < row.length - 1) {
                        csvWriter.append(",");
                    }
                }
                csvWriter.append("\n");
            }

            System.out.println("Archivo CSV generado con éxito y datos encriptados.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para encriptar datos
    private String encrypt(String data) throws Exception {
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    // Método para generar datos aleatorios
    private String[] generateRandomData() {
        String cardNumber = String.format("%016d", random.nextLong() & Long.MAX_VALUE); // Número de tarjeta de 16 dígitos
        String cvv = String.format("%03d", random.nextInt(1000)); // CVV de 3 dígitos
        String expirationDate = String.format("%02d/%02d", random.nextInt(12) + 1, random.nextInt(5) + 24); // Fecha de expiración
        String accountOwner = "Usuario " + (random.nextInt(9000) + 1000); // Nombre de usuario aleatorio
        String accountNumber = String.format("%09d", random.nextInt(1000000000)); // Número de cuenta de 9 dígitos
        String bankName = BANK_NAMES[random.nextInt(BANK_NAMES.length)]; // Banco aleatorio

        return new String[]{cardNumber, cvv, expirationDate, accountOwner, accountNumber, bankName};
    }

}