package com.so2042.finalproject.global.repository;

import com.so2042.finalproject.global.entity.ClientInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientInfoRepository extends JpaRepository<ClientInfo, Integer> {
}
