package com.mindhub.homebankingJDK17.models;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Client {

    // Properties

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    private String firstName, lastName, email, password;

    private boolean admin;

    // fetchType.EAGER hace que al llamar a un client traiga tambien esta propiedad
    @OneToMany(mappedBy="client", fetch = FetchType.EAGER)
    private final Set<Account> accounts = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private final Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private final Set<Card> cards = new HashSet<>();
}
