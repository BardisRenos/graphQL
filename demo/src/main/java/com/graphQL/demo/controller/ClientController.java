package com.graphQL.demo.controller;

import com.graphQL.demo.dao.ClientRepository;
import com.graphQL.demo.model.Client;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Controller
public class ClientController {

    @Autowired
    private ClientRepository clientRepository;

    private GraphQL graphQL;

    @Value("classpath:graphql/schema.graphqls")
    private Resource schemaResource;

    @PostConstruct
    public void loadSchema() throws IOException {

        loadDataIntoHSQL();

        File schemaFile = schemaResource.getFile();
        TypeDefinitionRegistry registry = new SchemaParser().parse(schemaFile);
        RuntimeWiring wiring = buildWiring();
        GraphQLSchema schema = new SchemaGenerator().makeExecutableSchema(registry, wiring);
        graphQL = GraphQL.newGraphQL(schema).build();
    }

    private RuntimeWiring buildWiring() {
        DataFetcher<List<Client>> fetcher1 = data -> (List<Client>) clientRepository.findAll();

        DataFetcher<Client> fetcher2 = data -> clientRepository.findByEmail(data.getArgument("email"));

        return RuntimeWiring.newRuntimeWiring().type("Query",
                        typeWriting -> typeWriting.dataFetcher("getAllClient", fetcher1).dataFetcher("findClient", fetcher2))
                .build();

    }

    private void loadDataIntoHSQL() {

        Stream.of(
                new Client(123, "Renos", "Bardis", "69898999", "renos@gmail.com", "Athens BVD 145"),
                new Client(124, "Nikos", "papas", "69898999", "renos@gmail.com", "Athens BVD 145"),
                new Client(125, "georgo", "Bardis", "69898999", "renos@gmail.com", "Athens BVD 145")
        ).forEach(client -> {
            clientRepository.save(client);
        });
    }

//    @PostMapping("/addClient")
    @QueryMapping
    public String addPerson(@RequestBody Client client) {
        clientRepository.save(client);
        return "successfully added";
    }
//
//    @GetMapping("/findAllClients")
//    public List<Client> getPersons() {
//        return (List<Client>) clientRepository.findAll();
//    }


//    @PostMapping("/getAllClient")
    @QueryMapping
    public ResponseEntity<Object> getAllClient(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

//    @PostMapping("/getPersonByEmail")
    @QueryMapping
    public ResponseEntity<Object> getClientByEmail(@RequestBody String query) {
        ExecutionResult result = graphQL.execute(query);
        return new ResponseEntity<Object>(result, HttpStatus.OK);
    }

}
