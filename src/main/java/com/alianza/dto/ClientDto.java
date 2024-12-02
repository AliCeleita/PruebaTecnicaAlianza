package com.alianza.dto;

import com.alianza.model.Client;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class ClientDto {

    private Long id;
    private String sharedKey;

    private String businessID;

    private String email;

    @Pattern(regexp = "\\d+", message = "El campo solo debe contener números")
    private String phone;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataAdded;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dataFinished;

    public ClientDto() {
    }

    public ClientDto (Client client) {
        this(client.getId(), client.getSharedKey(), client.getBusinessID(), client.getEmail(),
                client.getPhone(), client.getDataAdded(), client.getDataFinished());
    }
}
