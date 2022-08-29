package com.example.hibernatetest.controller;

import com.example.hibernatetest.data.ClientRepository;
import com.example.hibernatetest.domain.Address;
import com.example.hibernatetest.domain.Client;
import com.example.hibernatetest.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ClientController {
    @Autowired
    private ClientRepository clientRepository;

    @GetMapping("/clients")
    public ResponseEntity<Collection<Client.ClientPureDTO>> getAllClients(){
        return new ResponseEntity<>(
                clientRepository.findAll().stream().map(client -> new Client.ClientPureDTO().build(client)).collect(Collectors.toSet()),
                HttpStatus.OK);
    }

    @GetMapping("/clients/{id}")
    public ResponseEntity<Client.ClientDTO> getClientById(@PathVariable Long id){
        return clientRepository.findById(id)
                .map(val -> new ResponseEntity<>( new Client.ClientDTO().build(val), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/clients/{id}/addresses")
    public ResponseEntity<Set<Address>> getAddressesByClientId (@PathVariable Long id){
        return clientRepository.findById(id)
                .map(val -> new ResponseEntity<>( val.getAddresses() , HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/clients/{id}/orders")
    public ResponseEntity<Set<Order.OrderForClientDTO>> getOrdersByClientId (@PathVariable Long id){
        return clientRepository.findById(id)
                .map(val -> {
                    Set<Order.OrderForClientDTO> orderNoClient = val.getOrders().stream().map(order -> new Order.OrderForClientDTO().build(order)).collect(Collectors.toSet());
                    return new ResponseEntity<>( orderNoClient, HttpStatus.OK);
                }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
