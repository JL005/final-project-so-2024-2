package com.so2042.finalproject.serial.service;

import com.so2042.finalproject.global.service.DatabaseService;
import com.so2042.finalproject.global.service.DecryptionService;
import com.so2042.finalproject.global.service.FileReaderService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;

@Service
public class SerialProcessing {

    private final DecryptionService decryptionService;
    private final FileReaderService fileReaderService;
    private final DatabaseService databaseService;

    public SerialProcessing(DecryptionService decryptionService,
                            FileReaderService fileReaderService,
                            DatabaseService databaseService) {
        this.decryptionService = decryptionService;
        this.fileReaderService = fileReaderService;
        this.databaseService = databaseService;
    }


    public String execute(String filePath){
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long startTime = System.currentTimeMillis();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                // Limpiar la línea de caracteres de espacio en blanco
                line = line.trim();
                String[] encryptedFields = line.split(",");

                for (String encryptedField : encryptedFields) {
                    // Limpiar el campo de caracteres de espacio en blanco
                    String sanitizedField = encryptedField.replaceAll("\\s", "");
                    // Desencriptar el campo y guardarlo en la base de datos
                    String decryptedData = decryptionService.decrypt(sanitizedField);
                    System.out.println(decryptedData);
                }
            }
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            return String.format("%d", executionTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
            //return "Error: " + e.getMessage();
        }
    }

}
