package com.mindhub.homebankingJDK17.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    // Methods

    // Constructors

    public Client() { }

    public Client(String firstName, String lastName, String email, String password, boolean admin){
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.admin = admin;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        this.accounts.add(account);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Client{" +
                "ID=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public boolean isAdmin() {
        return admin;
    }

    @JsonIgnore
    public List<Loan> getLoans() {
        List<Loan> loans = new ArrayList<>();
        for (ClientLoan clientLoan : clientLoans) {
            loans.add(clientLoan.getLoan());
        }
        return loans;
    }

    public void addClientLoan(ClientLoan clientLoan) {
        clientLoan.setClient(this);
        clientLoans.add(clientLoan);
    }

    public Set<ClientLoan> getClientLoans() {
        return clientLoans;
    }

    public void addCard(Card card) {
        card.setClient(this);
        this.cards.add(card);
    }

    public Set<Card> getCards() { return cards; }

    // Método para poner el cardholder
    public String fullName() {
        return this.firstName + " " + this.lastName;
    }
}
