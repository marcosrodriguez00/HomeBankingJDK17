package com.mindhub.homebankingJDK17.controllers;

import com.mindhub.homebankingJDK17.dto.LoanDTO;
import com.mindhub.homebankingJDK17.dto.receiver.LoanApplicationDTO;
import com.mindhub.homebankingJDK17.dto.receiver.NewLoanDTO;
import com.mindhub.homebankingJDK17.models.*;
import com.mindhub.homebankingJDK17.services.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import static com.mindhub.homebankingJDK17.services.utils.LoanUtils.addInterest;
import static com.mindhub.homebankingJDK17.services.utils.LoanUtils.dateFormatter;

@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/loans")
    public List<LoanDTO> getAllLoans () {
        return loanService.getAllLoanDTO();
    }

    String approvedMessage = "Loan approved";

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> requestLoan (@RequestBody LoanApplicationDTO loanApplication, Authentication authentication) {

        Client client = clientService.getClientByEmail(authentication.getName());

        Loan loan = loanService.getLoanById(loanApplication.loanId());

        Account account = accountService.getAccountByNumber(loanApplication.destinyAccountNumber());

        // verificar que no exista ya un prestamo de ese tipo
        if ( clientLoanService.existsClientLoanByLoanAndClientAndIsActive(loan, client, true) ) {
            return new ResponseEntity<>("In order to request another loan of this type, pay in full the one that you already requested",
                    HttpStatus.FORBIDDEN);
        }

        if ( loanApplication.destinyAccountNumber().isBlank() ) {
            return new ResponseEntity<>("Missing an account to take the loan", HttpStatus.FORBIDDEN);
        }

        if ( loanApplication.amount() <= 0 ) {
            return new ResponseEntity<>("Requested amount cannot be 0 or less", HttpStatus.FORBIDDEN);
        }

        if ( loanApplication.payments() == 0 ) {
            return new ResponseEntity<>("The amount of payments cannot be 0", HttpStatus.FORBIDDEN);
        }

        if ( loan == null ) {
            return new ResponseEntity<>("The loan you are applying to does not exist", HttpStatus.FORBIDDEN);
        }

        if ( loanApplication.amount() > loan.getMaxAmount() ) {
            return new ResponseEntity<>("The amount you asked for exceeds the maximum for this type of loan", HttpStatus.FORBIDDEN);
        }

        if ( !loan.getPayments().contains(loanApplication.payments()) ) {
            return new ResponseEntity<>("Please select an available amount of payments", HttpStatus.FORBIDDEN);
        }

        if ( !accountService.existsAccountByNumber(loanApplication.destinyAccountNumber()) ){
            return new ResponseEntity<>("The account number you selected does not exist", HttpStatus.FORBIDDEN);
        }

        if ( !client.getAccounts().contains(accountService.getAccountByNumber(loanApplication.destinyAccountNumber())) ) {
            return new ResponseEntity<>("The account you inserted does not belong to you!", HttpStatus.FORBIDDEN);
        }

        ClientLoan clientLoan = new ClientLoan( addInterest(loanApplication.amount(), loanApplication.interestRate() ), loanApplication.payments() );
        client.addClientLoan( clientLoan );
        loan.addClientLoan( clientLoan );
        clientLoan.setEachPaymentAmount( (clientLoan.getAmount() * (1 + clientLoan.getLoan().getInterestRate())) / clientLoan.getPayments() );

        clientLoanService.saveClientLoan( clientLoan );

        Transaction transaction = new Transaction( TransactionType.CREDIT, loanApplication.amount(), approvedMessage, dateFormatter(LocalDateTime.now()) );
        account.addTransaction( transaction );
        transactionService.saveTransaction( transaction );
        account.setBalance( account.getBalance() + loanApplication.amount() );
        accountService.saveAccount( account );

        return new ResponseEntity<>("Loan created successfully",HttpStatus.CREATED);
    }

    @PostMapping("/loans/create")
    public ResponseEntity<Object> createNewLoan(@RequestBody NewLoanDTO newLoanDTO) {

        // verificar que los campos no esten vacios / menores a 0
        if (newLoanDTO.name().isBlank()){
            return new ResponseEntity<>("The name cannot be empty", HttpStatus.FORBIDDEN);
        }

        if (newLoanDTO.interestRate() <= 0) {
            return new ResponseEntity<>("The interest rate cannot be lower or equal to 0", HttpStatus.FORBIDDEN);
        }

        if (newLoanDTO.payments().isEmpty()) {
            return new ResponseEntity<>("There has to be an amount of available payments", HttpStatus.FORBIDDEN);
        }

        if (newLoanDTO.maxAmount() <= 0) {
            return new ResponseEntity<>("The max amount cannot be less or equal to 0", HttpStatus.FORBIDDEN);
        }

        // verificar que no exista un prestamo con el mismo nombre
        if ( loanService.existsLoanByName(newLoanDTO.name())) {
            return new ResponseEntity<>("A loan with the same name already exists", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(newLoanDTO.name(), newLoanDTO.maxAmount(), newLoanDTO.payments(), newLoanDTO.interestRate());

        loanService.saveLoan(loan);

        return new ResponseEntity<>("Loan created", HttpStatus.ACCEPTED);
    }

    @PostMapping("/loans/delete")
    public ResponseEntity<Object> deactivateLoan (@RequestParam long loanId) {

        // verificar que exista el prestamo
        if ( !loanService.existsLoanById(loanId) ) {
            return new ResponseEntity<>("This loan doesn't exist", HttpStatus.FORBIDDEN);
        }

        loanService.deleteLoanById(loanId);

        return new ResponseEntity<>("Loan deactivated", HttpStatus.ACCEPTED);
    }

    @Transactional
    @PostMapping("/loans/pay")
    public ResponseEntity<Object> payClientLoan(Authentication authentication,
                                                @RequestParam int amountOfPayments, @RequestParam long clientLoanId,
                                                @RequestParam String accountNumber) {

        Client client = clientService.getClientByEmail(authentication.getName());
        ClientLoan clientLoan = clientLoanService.getById(clientLoanId);
        Account account = accountService.getAccountByNumber(accountNumber);

        // verificar que el clientLoan exista
        if ( !clientLoanService.existsById( clientLoanId ) ) {
            return new ResponseEntity<>("The loan does not exist", HttpStatus.FORBIDDEN);
        }

        // verificar que exista la cuenta
        if ( !accountService.existsAccountByNumber( accountNumber ) ) {
            return new ResponseEntity<>("The account you are trying to pay with does not exist", HttpStatus.FORBIDDEN);
        }

        // verificar que la cuenta pertenezca al cliente
        if ( !client.getAccounts().contains(account) ) {
            return new ResponseEntity<>("The account does not belong to this client", HttpStatus.FORBIDDEN);
        }

        // verificar que la cantidad de cuotas no sea menor a 1
        if (amountOfPayments < 1) {
            return new ResponseEntity<>("The amount of payments you want to pay cannot be less than 1", HttpStatus.FORBIDDEN);
        }

        // verificar que el prestamo aun no se haya terminado de pagar
        if ( clientLoan.getPayedPayments() == clientLoan.getPayments() ) {
            return new ResponseEntity<>("This loan has already been paid in full", HttpStatus.FORBIDDEN);
        }

        // ni mayor a la cantidad de pagos restantes
        if ( amountOfPayments > clientLoan.getPayments() - clientLoan.getPayedPayments() ) {
            return new ResponseEntity<>("You are paying more payments than you owe, you only have " + ( clientLoan.getPayments() - clientLoan.getPayedPayments() ) + " payments left", HttpStatus.FORBIDDEN);
        }

        // verificar que el cliente tenga la cantidad de dinero suficiente en la cuenta
        if (account.getBalance() < clientLoan.getEachPaymentAmount()*amountOfPayments) {
            return new ResponseEntity<>("You don't have enough money in this account to pay", HttpStatus.FORBIDDEN);
        }

        // agregar los pagos al clientLoan y actualizar las cuotas pagadas
        clientLoan.setPayedPayments( clientLoan.getPayedPayments() + amountOfPayments );
        clientLoan.setPayedAmount( clientLoan.getPayedAmount() + amountOfPayments * clientLoan.getEachPaymentAmount() );

        // crear la transaccion y agregarla a la cuenta, descontar la plata de la cuenta
        account.setBalance( account.getBalance() - clientLoan.getEachPaymentAmount()*amountOfPayments );

        String description = clientLoan.getLoan().getName() + " loan payment";
        Transaction transaction = new Transaction( TransactionType.DEBIT, -(clientLoan.getEachPaymentAmount()*amountOfPayments), description, dateFormatter(LocalDateTime.now()) );
        transaction.setCurrentBalance(account.getBalance());
        account.addTransaction(transaction);

        // si todas las cuotas estan pagadas cambiar el booleano
        if ( clientLoan.getPayedPayments() == clientLoan.getPayments() ) {
            clientLoan.setActive(false);
        }

        // guardo todo
        clientLoanService.saveClientLoan(clientLoan);
        accountService.saveAccount(account);
        transactionService.saveTransaction(transaction);

        return new ResponseEntity<>("Loan payments received", HttpStatus.ACCEPTED);
    }
}