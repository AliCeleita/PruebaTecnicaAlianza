package com.alianza.controller;

import com.alianza.dto.ClientDto;
import com.alianza.model.Client;
import com.alianza.service.IClientService;
import com.querydsl.core.types.Predicate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/clients")
public class ClientController {

    private final IClientService clientService;

    @GetMapping()
    private ResponseEntity<Page<ClientDto>> getAllClients(@QuerydslPredicate(root = Client.class) Predicate predicate,
                                                          Pageable pageable){
        Page<ClientDto> clients = clientService.getClients(predicate, pageable);
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    @PostMapping()
    private ResponseEntity<ClientDto> createClient(@RequestBody @Valid ClientDto clientDto){
        clientService.createClient(clientDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    private ResponseEntity<ClientDto> updateClient(@RequestBody @Valid ClientDto clientDto, @PathVariable Long id){
        clientService.updateClient(clientDto, id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<ClientDto> deleteClient(@PathVariable Long id){
        clientService.deleteClient(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
