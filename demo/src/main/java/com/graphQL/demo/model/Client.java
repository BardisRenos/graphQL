package com.graphQL.demo.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table(name = "client")
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Client {

    @Id
    private Integer id;
    private String name;
    private String lastName;
    private String mobile;
    private String email;
    private String address;
}
