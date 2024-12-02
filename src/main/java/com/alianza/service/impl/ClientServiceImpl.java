package com.alianza.service.impl;

import com.alianza.dto.ClientDto;
import com.alianza.error.Errors;
import com.alianza.exception.CustomException;
import com.alianza.model.Client;
import com.alianza.repository.ClientRepository;
import com.alianza.service.IClientService;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

@Service
public class ClientServiceImpl implements IClientService {

    private ClientRepository clientRepository;

    public ClientServiceImpl(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void createClient(ClientDto dto) {
        validateEmail(dto.getEmail());
        validateBusinessID(dto.getBusinessID());
        dto.setSharedKey(createShared(dto.getBusinessID()));
        dto.setDataAdded(LocalDate.now());
        validateDate(dto.getDataAdded(), dto.getDataFinished());
        clientRepository.save(new Client(dto));
    }

    @Override
    public void updateClient(ClientDto dto, Long id) {

        Client client = clientRepository.findById(id).get();

        if (!Objects.equals(dto.getBusinessID(), client.getBusinessID())) {
            validateBusinessID(dto.getBusinessID());
            client.setBusinessID(dto.getBusinessID());
            client.setSharedKey(createShared(dto.getBusinessID()));
        }

        if (!Objects.equals(dto.getPhone(), client.getPhone()))
            client.setPhone(dto.getPhone());

        if (!Objects.equals(dto.getEmail(), client.getEmail())){
            validateEmail(dto.getEmail());
            client.setEmail(dto.getEmail());
        }
        if (!dto.getDataFinished().isEqual(client.getDataFinished()) && !validateDate(client.getDataAdded(), dto.getDataFinished()))
            client.setDataFinished(dto.getDataFinished());

        clientRepository.save(client);
    }

    @Override
    public void deleteClient(Long id) {
        Client client = clientRepository.findById(id).get();
        clientRepository.delete(client);
    }

    @Override
    public Page<ClientDto> getClients(Predicate predicate, Pageable pageable) {
        return clientRepository.findAll(predicate, pageable).map(ClientDto::new);
    }

    private String createShared(String businessID) {
        StringTokenizer tokenizer = new StringTokenizer(businessID, " ");
        String sharedKey = (tokenizer.nextToken().charAt(0) + tokenizer.nextToken()).toLowerCase();
        if (clientRepository.existsBySharedKey(sharedKey)) {
            long number = clientRepository.countBySharedKeyContains(sharedKey);
            sharedKey += number;
        }
        return sharedKey;
    }

    private boolean validateDate(LocalDate dataAdded, LocalDate dataFinished) {
        if (dataAdded.isAfter(dataFinished)) {
            throw new CustomException(HttpStatus.BAD_REQUEST, Errors.INCORRECT_DATE);
        }
        return false;
    }

    private void validateBusinessID(String businessID) {
        if(Objects.isNull(businessID) || businessID.trim().isEmpty())
            throw new CustomException(HttpStatus.BAD_REQUEST, Errors.INCORRECT_STRING);
        String[] words = businessID.trim().split("\\s+");
        if(words.length != 2){
            throw new CustomException(HttpStatus.BAD_REQUEST, Errors.INCORRECT_STRING);
        }
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (email == null || !pattern.matcher(email).matches()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, Errors.INCORRECT_EMAIL);
        }
    }

}
