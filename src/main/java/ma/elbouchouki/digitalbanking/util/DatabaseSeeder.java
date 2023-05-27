package ma.elbouchouki.digitalbanking.util;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;
import ma.elbouchouki.digitalbanking.enums.OperationType;
import ma.elbouchouki.digitalbanking.model.*;
import ma.elbouchouki.digitalbanking.repository.BankAccountRepository;
import ma.elbouchouki.digitalbanking.repository.CustomerRepository;
import ma.elbouchouki.digitalbanking.repository.OperationRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@AllArgsConstructor
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final BankAccountRepository bankAccountRepository;
    private final OperationRepository operationRepository;
    private final Faker faker = new Faker();

    @Override
    public void run(String... args) throws Exception {

        List<Customer> customers = IntStream.range(0, 10)
                .mapToObj(i -> Customer.builder()
                        .firstname(faker.name().firstName())
                        .lastname(faker.name().lastName())
                        .email(faker.internet().emailAddress())
                        .phone(faker.phoneNumber().cellPhone())
                        .build())
                .collect(java.util.stream.Collectors.toList());

        log.info("Saving customers...");
        customerRepository.saveAll(customers);
        log.info("Customers saved successfully.");

        List<BankAccount> bankAccounts = customers.stream()
                .map(customer -> CurrentAccount.builder()
                        .customer(customer)
                        .balance(faker.number().randomDouble(2, 1000, 10000))
                        .overDraft(0.05)
                        .currency(faker.currency().code())
                        .createdAt(new Date())
                        .status(faker.random().nextBoolean() ? AccountStatus.CREATED : AccountStatus.ACTIVATED)
                        .build())
                .collect(Collectors.toList());

        for (int i = 0; i < customers.size(); i++) {
            bankAccounts.add(SavingAccount.builder()
                    .customer(customers.get(i))
                    .balance(faker.number().randomDouble(2, 1000, 10000))
                    .interestRate(0.05)
                    .currency(faker.currency().code())
                    .createdAt(new Date())
                    .status(faker.random().nextBoolean() ? AccountStatus.CREATED : AccountStatus.ACTIVATED)
                    .build());
        }

        log.info("Saving bank accounts...");
        bankAccountRepository.saveAll(bankAccounts);
        log.info("Bank accounts saved successfully.");

        List<Operation> operations = bankAccounts.stream()
                .map(bankAccount -> IntStream.range(0, 10)
                        .mapToObj(i -> {
                            OperationType operationType = faker.random().nextBoolean() ? OperationType.CREDIT : OperationType.DEBIT;
                            return Operation.builder()
                                    .bankAccount(bankAccount)
                                    .amount(faker.number().randomDouble(2, 100, 1000))
                                    .type(operationType)
                                    .date(faker.date().birthday())
                                    .build();
                        })
                        .collect(Collectors.toList()))
                .flatMap(List::stream)
                .toList();

        log.info("Saving operations...");
        operationRepository.saveAll(operations);
        log.info("Operations saved successfully.");

    }
}
