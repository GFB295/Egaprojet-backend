package com.example.Ega.backend.service;

import com.example.Ega.backend.dto.ClientDTO;
import com.example.Ega.backend.entity.Client;
import com.example.Ega.backend.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ClientService {
    
    @Autowired
    private ClientRepository clientRepository;
    
    public ClientDTO createClient(ClientDTO clientDTO) {
        if (clientRepository.existsByCourriel(clientDTO.getCourriel())) {
            throw new RuntimeException("Le courriel existe déjà");
        }
        
        Client client = new Client();
        client.setNom(clientDTO.getNom());
        client.setPrenom(clientDTO.getPrenom());
        client.setDateNaissance(clientDTO.getDateNaissance());
        client.setSexe(clientDTO.getSexe());
        client.setAdresse(clientDTO.getAdresse());
        client.setTelephone(clientDTO.getTelephone());
        client.setCourriel(clientDTO.getCourriel());
        client.setNationalite(clientDTO.getNationalite());
        
        client = clientRepository.save(client);
        return toDTO(client);
    }
    
    public ClientDTO getClientById(String id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        return toDTO(client);
    }
    
    public List<ClientDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }
    
    public ClientDTO updateClient(String id, ClientDTO clientDTO) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID: " + id));
        
        if (!client.getCourriel().equals(clientDTO.getCourriel()) && 
            clientRepository.existsByCourriel(clientDTO.getCourriel())) {
            throw new RuntimeException("Le courriel existe déjà");
        }
        
        client.setNom(clientDTO.getNom());
        client.setPrenom(clientDTO.getPrenom());
        client.setDateNaissance(clientDTO.getDateNaissance());
        client.setSexe(clientDTO.getSexe());
        client.setAdresse(clientDTO.getAdresse());
        client.setTelephone(clientDTO.getTelephone());
        client.setCourriel(clientDTO.getCourriel());
        client.setNationalite(clientDTO.getNationalite());
        
        client = clientRepository.save(client);
        return toDTO(client);
    }
    
    public void deleteClient(String id) {
        if (!clientRepository.existsById(id)) {
            throw new RuntimeException("Client non trouvé avec l'ID: " + id);
        }
        clientRepository.deleteById(id);
    }
    
    private ClientDTO toDTO(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setId(client.getId());
        dto.setNom(client.getNom());
        dto.setPrenom(client.getPrenom());
        dto.setDateNaissance(client.getDateNaissance());
        dto.setSexe(client.getSexe());
        dto.setAdresse(client.getAdresse());
        dto.setTelephone(client.getTelephone());
        dto.setCourriel(client.getCourriel());
        dto.setNationalite(client.getNationalite());
        return dto;
    }
}
