package com.so2042.finalproject.global.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo {

    @Id
    private Integer id;

    private String cardNumber;

    private String securityCode;

    private String expirationDate;

    private String ownerAccount;


    private String email;

}
