package com.graphQL.demo.dao;

import com.graphQL.demo.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    Client findByEmail(String email);
}
