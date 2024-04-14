package com.graphQL.demo.database;

import com.graphQL.demo.dao.ClientRepository;
import com.graphQL.demo.model.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class Database {

    @Autowired
    private ClientRepository clientRepository;

    @PostConstruct
    private void postConstruct() {
        Client client = new Client(1, "Renos", "Bardis", "69898999", "renos@gmail.com", "Athens BVD 145");
        Client client1 = new Client(2, "John", "Papas", "7858865", "john@gmail.com", "Bucharest BVD 58");

        clientRepository.save(client);
        clientRepository.save(client1);
    }

}
