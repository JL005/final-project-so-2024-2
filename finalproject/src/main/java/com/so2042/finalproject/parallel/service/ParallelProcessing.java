package com.so2042.finalproject.parallel.service;

import com.so2042.finalproject.global.entity.ClientInfo;
import com.so2042.finalproject.global.service.DatabaseService;
import com.so2042.finalproject.global.service.DecryptionService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

@Service
public class ParallelProcessing {

    private final DecryptionService decryptionService;
    private final DatabaseService databaseService;

    // Estructura para monitorear los hilos utilizados
    private final ConcurrentMap<String, Boolean> threadMap = new ConcurrentHashMap<>();

    public ParallelProcessing(DecryptionService decryptionService, DatabaseService databaseService) {
        this.decryptionService = decryptionService;
        this.databaseService = databaseService;
    }

    public String execute(String filePath) throws FileNotFoundException {
        long startTime = System.currentTimeMillis();

        // Crear un ForkJoinPool con 4 hilos
        ForkJoinPool customPool = new ForkJoinPool(4);

        try {
            customPool.submit(() -> {
                try {
                    // Etapa 1: Leer el archivo y cargar líneas
                    List<String> lines = Files.lines(Path.of(filePath))
                            .skip(1) // Omitir la cabecera
                            .collect(Collectors.toList());

                    // Etapa 2: Procesar líneas para crear objetos ClientInfo
                    List<ClientInfo> clientInfoList = lines.parallelStream()
                            .map(this::processLine)
                            .collect(Collectors.toList());

                    // Etapa 3: Guardar los objetos en la base de datos
                    clientInfoList.parallelStream().forEach(databaseService::save);

                } catch (IOException e) {
                    throw new RuntimeException("Error al leer el archivo", e);
                }
            }).join();
        } finally {
            customPool.shutdown();
        }

        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;
        return String.format("Finaliza después de %d ms", executionTime);
    }

    private ClientInfo processLine(String line) {
        // Registrar el hilo actual
        String threadName = Thread.currentThread().getName();
        threadMap.putIfAbsent(threadName, true);

        String[] encryptedFields = line.split(",");
        if (encryptedFields.length != 8) {
            throw new IllegalArgumentException("Formato de línea incorrecto: " + line);
        }

        try {
            ClientInfo clientInfo = new ClientInfo();
            clientInfo.setCardNumber(decryptionService.decrypt(encryptedFields[0].trim()));
            clientInfo.setSecurityCode(decryptionService.decrypt(encryptedFields[1].trim()));
            clientInfo.setExpirationDate(decryptionService.decrypt(encryptedFields[2].trim()));
            clientInfo.setOwnerAccount(decryptionService.decrypt(encryptedFields[3].trim()));
            clientInfo.setAccountNumber(decryptionService.decrypt(encryptedFields[4].trim()));
            clientInfo.setBank(decryptionService.decrypt(encryptedFields[5].trim()));
            clientInfo.setIdentificationNumber(decryptionService.decrypt(encryptedFields[6].trim()));
            clientInfo.setIdentificationType(decryptionService.decrypt(encryptedFields[7].trim()));
            return clientInfo;
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la línea: " + e.getMessage(), e);
        }
    }
}
