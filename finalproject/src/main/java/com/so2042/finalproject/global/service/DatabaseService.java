package com.so2042.finalproject.global.service;

import com.so2042.finalproject.global.entity.ClientInfo;
import com.so2042.finalproject.global.repository.ClientInfoRepository;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    private final ClientInfoRepository clientInfoRepository;

    public DatabaseService(ClientInfoRepository clientInfoRepository) {
        this.clientInfoRepository = clientInfoRepository;
    }

    public void save(ClientInfo clientInfo){
        this.clientInfoRepository.save(clientInfo);
    }

    public int findAll(){
        return this.clientInfoRepository.findAll().size();
    }
}
