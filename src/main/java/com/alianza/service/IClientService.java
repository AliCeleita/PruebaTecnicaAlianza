package com.alianza.service;

import com.alianza.dto.ClientDto;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public interface IClientService {

    void createClient(ClientDto dto);

    void updateClient(ClientDto dto, Long id);

    void deleteClient(Long id);

    Page<ClientDto> getClients(Predicate predicate, Pageable pageable);
}
