package ma.elbouchouki.digitalbanking.service;

import ma.elbouchouki.digitalbanking.dto.customer.CustomerCreateRequest;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerUpdateRequest;
import ma.elbouchouki.digitalbanking.mapper.BankAccountMapper;
import ma.elbouchouki.digitalbanking.mapper.CustomerMapper;
import ma.elbouchouki.digitalbanking.mapper.OperationMapper;
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
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private CustomerRepository repository;
    @Mock
    private OperationRepository operationRepository;
    @Spy
    private BankAccountMapper bankAccountMapper = Mappers.getMapper(BankAccountMapper.class);
    @Spy
    private OperationMapper operationMapper = Mappers.getMapper(OperationMapper.class);
    @Spy
    private CustomerMapper mapper = Mappers.getMapper(CustomerMapper.class);
    private BankAccountService bankAccountService;
    private OperationService operationService;
    private CustomerService service;


    @BeforeEach
    void setup() {
        operationService = new OperationServiceImpl(
                operationMapper,
                operationRepository,
                bankAccountRepository
        );
        bankAccountService = new BankAccountServiceImpl(
                bankAccountMapper,
                bankAccountRepository,
                repository,
                operationService
        );
        service = new CustomerServiceImpl(
                mapper,
                repository,
                bankAccountService
        );
    }

    @Test
    void should_create_a_customer() {
        when(repository.save(any(Customer.class))).thenReturn(Customer.builder().build());

        service.save(CustomerCreateRequest.builder().build());

        verify(repository, times(1)).save(any(Customer.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById() {
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(Customer.builder().build()));

        service.findById("id", false, false);

        verify(repository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_find_all_customers() {
        when(repository.findAll(
                any(PageRequest.class)
        )).thenReturn(org.springframework.data.domain.Page.empty());

        service.findAll(
                10, 10, "", false, false
        );

        verify(repository, times(1)).findAll(
                any(PageRequest.class)
        );
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_find_all_customers_with_bank_accounts() {
        when(repository.findAll(
                any(PageRequest.class)
        )).thenReturn(
                new PageImpl<>(
                        List.of(
                                Customer.builder()
                                        .id("id")
                                        .build()
                        )
                )
        );

        when(repository.findById(
                anyString()
        )).thenReturn(
                Optional.ofNullable(
                        Customer.builder()
                                .id("id")
                                .build()
                )
        );

        when(bankAccountRepository.findAllByCustomerId(
                anyString()
        )).thenReturn(
                Set.of(
                        SavingAccount.builder().build(),
                        CurrentAccount.builder().build()
                )
        );

        service.findAll(
                10, 10, "", true, false
        );

        verify(repository, times(1)).findAll(
                any(PageRequest.class)
        );

        verify(bankAccountRepository, times(1)).findAllByCustomerId(
                anyString()
        );

        verify(repository, times(1)).findById(
                anyString()
        );

        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_update_a_customer() {
        when(repository.save(any(Customer.class))).thenReturn(Customer.builder().build());
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(Customer.builder().build()));

        service.update(
                "id",
                CustomerUpdateRequest.builder()
                        .build()
        );

        verify(repository, times(1)).save(any(Customer.class));
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