package com.alianza.model;

import com.alianza.dto.ClientDto;
import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@QueryEntity
@Entity
@AllArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sharedKey;

    private String businessID;

    @Email()
    private String email;

    private String phone;

    private LocalDate dataAdded;

    private LocalDate dataFinished;

    public Client() {
    }

    public Client(ClientDto dto){
        this(dto.getId(), dto.getSharedKey(), dto.getBusinessID(), dto.getEmail(),
                dto.getPhone(), dto.getDataAdded(), dto.getDataFinished());
    }


}
