package com.alianza.service.impl;

import com.alianza.dto.ClientDto;
import com.alianza.error.Errors;
import com.alianza.model.Client;
import com.alianza.repository.ClientRepository;
import com.alianza.exception.CustomException;
import com.querydsl.core.types.Predicate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ClientServiceImplTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Test
    void createClient_ShouldSaveClient_WhenValidDtoIsProvided() {
        ClientDto dto = new ClientDto();
        dto.setBusinessID("Test Business");
        dto.setPhone("12345");
        dto.setEmail("test@example.com");
        dto.setDataFinished(LocalDate.now().plusDays(1));

        Mockito.when(clientRepository.existsBySharedKey(Mockito.anyString())).thenReturn(false);

        clientService.createClient(dto);

        Mockito.verify(clientRepository, Mockito.times(1)).save(Mockito.any(Client.class));
    }

    @Test
    void updateClient_ShouldUpdateFields_WhenDtoHasDifferentValues() {
        Long id = 1L;
        Client existingClient = new Client();
        existingClient.setId(id);
        existingClient.setBusinessID("Old Business");
        existingClient.setPhone("12345");
        existingClient.setEmail("old@example.com");
        existingClient.setDataAdded(LocalDate.now());
        existingClient.setDataFinished(LocalDate.now().plusDays(1));

        ClientDto dto = new ClientDto();
        dto.setBusinessID("New Business");
        dto.setPhone("67890");
        dto.setEmail("new@example.com");
        dto.setDataFinished(LocalDate.now().plusDays(2));

        Mockito.when(clientRepository.findById(id)).thenReturn(Optional.of(existingClient));
        Mockito.when(clientRepository.existsBySharedKey(Mockito.anyString())).thenReturn(false);

        clientService.updateClient(dto, id);

        Mockito.verify(clientRepository, Mockito.times(1)).save(Mockito.any(Client.class));
        Assertions.assertEquals("New Business", existingClient.getBusinessID());
        Assertions.assertEquals("67890", existingClient.getPhone());
        Assertions.assertEquals("new@example.com", existingClient.getEmail());
    }

    @Test
    void deleteClient_ShouldDeleteClient_WhenIdIsValid() {
        Long id = 1L;
        Client client = new Client();
        client.setId(id);

        Mockito.when(clientRepository.findById(id)).thenReturn(Optional.of(client));

        clientService.deleteClient(id);

        Mockito.verify(clientRepository, Mockito.times(1)).delete(client);
    }

    @Test
    void getClients_ShouldReturnPageOfClients_WhenCalled() {

        Predicate predicate = Mockito.mock(Predicate.class);
        Pageable pageable = PageRequest.of(0, 10);
        Page<Client> page = new PageImpl<>(Collections.emptyList());

        Mockito.when(clientRepository.findAll(predicate, pageable)).thenReturn(page);

        Page<ClientDto> result = clientService.getClients(predicate, pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(clientRepository, Mockito.times(1)).findAll(predicate, pageable);
    }

    @Test
    void createClient_ShouldThrowException_WhenInvalidBusinessID() {
        ClientDto dto = new ClientDto();
        dto.setBusinessID("InvalidID");
        dto.setDataFinished(LocalDate.now().plusDays(1));

        CustomException exception = Assertions.assertThrows(CustomException.class, () -> clientService.createClient(dto));
        Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), exception.getStatusCode().value());
    }


}