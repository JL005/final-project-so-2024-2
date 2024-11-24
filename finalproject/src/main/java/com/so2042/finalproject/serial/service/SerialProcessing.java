package com.so2042.finalproject.serial.service;

import com.so2042.finalproject.global.entity.ClientInfo;
import com.so2042.finalproject.global.service.DatabaseService;
import com.so2042.finalproject.global.service.DecryptionService;
import com.so2042.finalproject.global.service.FileReaderService;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
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


    public String execute(String filePath) throws FileNotFoundException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            long startTime = System.currentTimeMillis();
            List<ClientInfo> clientInfoList = new ArrayList<>();
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                // Limpiar la línea de caracteres de espacio en blanco
                line = line.trim();
                String[] encryptedFields = line.split(",");

                if (encryptedFields.length == 8) { // Verificar que haya 8 campos
                    try {
                        // Crear un nuevo objeto CardInfo
                        ClientInfo clientInfo = new ClientInfo();
                        clientInfo.setCardNumber(decryptionService.decrypt(encryptedFields[0].replaceAll("\\s", "")));
                        clientInfo.setSecurityCode(decryptionService.decrypt(encryptedFields[1].replaceAll("\\s", "")));
                        clientInfo.setExpirationDate(decryptionService.decrypt(encryptedFields[2].replaceAll("\\s", "")));
                        clientInfo.setOwnerAccount(decryptionService.decrypt(encryptedFields[3].replaceAll("\\s", "")));
                        clientInfo.setAccountNumber(decryptionService.decrypt(encryptedFields[4].replaceAll("\\s", "")));
                        clientInfo.setBank(decryptionService.decrypt(encryptedFields[5].replaceAll("\\s", "")));
                        clientInfo.setIdentificationNumber(decryptionService.decrypt(encryptedFields[6].replaceAll("\\s", "")));
                        clientInfo.setIdentificationType(decryptionService.decrypt(encryptedFields[7].replaceAll("\\s", "")));
                        clientInfoList.add(clientInfo);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            for (ClientInfo clientInfo : clientInfoList) {
                this.databaseService.save(clientInfo);
            }
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            return String.format("Finaliza despues de %d ms", executionTime);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

