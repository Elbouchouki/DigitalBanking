package ma.elbouchouki.digitalbanking.service;

import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountCreateRequest;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountUpdateRequest;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;
import ma.elbouchouki.digitalbanking.mapper.BankAccountMapper;
import ma.elbouchouki.digitalbanking.mapper.OperationMapper;
import ma.elbouchouki.digitalbanking.model.BankAccount;
import ma.elbouchouki.digitalbanking.model.CurrentAccount;
import ma.elbouchouki.digitalbanking.model.Customer;
import ma.elbouchouki.digitalbanking.model.SavingAccount;
import ma.elbouchouki.digitalbanking.repository.BankAccountRepository;
import ma.elbouchouki.digitalbanking.repository.CustomerRepository;
import ma.elbouchouki.digitalbanking.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class BankAccountServiceTest {
    @Mock
    private BankAccountRepository repository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private OperationRepository operationRepository;
    @Spy
    private BankAccountMapper mapper = Mappers.getMapper(BankAccountMapper.class);
    @Spy
    private OperationMapper operationMapper = Mappers.getMapper(OperationMapper.class);
    private BankAccountService service;
    private OperationService operationService;


    @BeforeEach
    void setup() {
        operationService = new OperationServiceImpl(
                operationMapper,
                operationRepository,
                repository
        );
        service = new BankAccountServiceImpl(
                mapper,
                repository,
                customerRepository,
                operationService
        );
    }

    @Test
    void should_create_saving_account() {
        when(repository.save(any(BankAccount.class))).thenReturn(SavingAccount.builder().build());
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(Customer.builder().build()));

        service.save(BankAccountCreateRequest.builder()
                .customerId("id")
                .interestRate(new BigDecimal("20.0"))
                .build());

        verify(customerRepository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(BankAccount.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_create_current_account() {
        when(repository.save(any(BankAccount.class))).thenReturn(CurrentAccount.builder().build());
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(Customer.builder().build()));

        service.save(BankAccountCreateRequest.builder()
                .customerId("id")
                .overDraft(new BigDecimal("20.0"))
                .build());

        verify(customerRepository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(BankAccount.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById() {
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(SavingAccount.builder().build()));

        service.findById("id", false);

        verify(repository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_find_all_bank_accounts() {
        when(repository.findAll(
                any(PageRequest.class)
        )).thenReturn(org.springframework.data.domain.Page.empty());

        service.findAll(
                10, 10, false
        );

        verify(repository, times(1)).findAll(
                any(PageRequest.class)
        );
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_find_all_bank_accounts_by_customer_id() {
        when(repository.findAllByCustomerId(
                anyString()
        )).thenReturn(
                Set.of(
                        SavingAccount.builder().build(),
                        CurrentAccount.builder().build()
                )
        );
        when(customerRepository.findById(anyString())).thenReturn(Optional.ofNullable(Customer.builder().build()));

        service.findAllByCustomerId(
                "id", false
        );

        verify(repository, times(1)).findAllByCustomerId(
                anyString()
        );
        verify(customerRepository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_update_saving_account() {
        when(repository.save(any(BankAccount.class))).thenReturn(SavingAccount.builder().build());
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(SavingAccount.builder().build()));

        service.update(
                "id",
                BankAccountUpdateRequest.builder()
                        .interestRate(new BigDecimal("20.0"))
                        .build()
        );

        verify(repository, times(1)).save(any(BankAccount.class));
        verify(repository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_update_current_account() {
        when(repository.save(any(BankAccount.class))).thenReturn(CurrentAccount.builder().build());
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(CurrentAccount.builder().build()));

        service.update(
                "id",
                BankAccountUpdateRequest.builder()
                        .overDraft(new BigDecimal("20.0"))
                        .build()
        );

        verify(repository, times(1)).save(any(BankAccount.class));
        verify(repository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_delete_bank_account_by_id() {
        when(repository.existsById(anyString())).thenReturn(true);
        service.deleteById("id");

        verify(repository, times(1)).existsById(anyString());
        verify(repository, times(1)).deleteById(anyString());
        verifyNoMoreInteractions(repository);
    }
}