package com.so2042.finalproject.global.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientInfo {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String cardNumber;

    private String securityCode;

    private String expirationDate;

    private String ownerAccount;

    private String accountNumber;

    private String bank;

    private String identificationNumber;

    private String identificationType;

    private String email;

}
