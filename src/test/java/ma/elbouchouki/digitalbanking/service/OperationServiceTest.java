package ma.elbouchouki.digitalbanking.service;

import ma.elbouchouki.digitalbanking.dto.operation.OperationCreateRequest;
import ma.elbouchouki.digitalbanking.enums.OperationType;
import ma.elbouchouki.digitalbanking.mapper.OperationMapper;
import ma.elbouchouki.digitalbanking.model.*;
import ma.elbouchouki.digitalbanking.repository.BankAccountRepository;
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

import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@ExtendWith(MockitoExtension.class)
class OperationServiceTest {
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private OperationRepository repository;
    @Spy
    private OperationMapper mapper = Mappers.getMapper(OperationMapper.class);
    private OperationService service;


    @BeforeEach
    void setup() {
        service = new OperationServiceImpl(
                mapper,
                repository,
                bankAccountRepository
        );
    }

    @Test
    void should_create_a_debit_operation() {
        when(repository.save(any(Operation.class))).thenReturn(Operation.builder()
                .build());
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.ofNullable(BankAccount.builder().build()));

        service.save(OperationCreateRequest.builder()
                .bankAccountId("id")
                .type(OperationType.DEBIT)
                .build());

        verify(bankAccountRepository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(Operation.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_create_a_credit_operation() {
        when(repository.save(any(Operation.class))).thenReturn(Operation.builder()
                .build());
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.ofNullable(BankAccount.builder().build()));

        service.save(OperationCreateRequest.builder()
                .bankAccountId("id")
                .type(OperationType.CREDIT)
                .build());

        verify(bankAccountRepository, times(1)).findById(anyString());
        verify(repository, times(1)).save(any(Operation.class));
        verifyNoMoreInteractions(repository);
    }

    @Test
    void findById() {
        when(repository.findById(anyString())).thenReturn(Optional.ofNullable(Operation.builder().build()));

        service.findById("id");

        verify(repository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_find_all_operations() {
        when(repository.findAll(
                any(PageRequest.class)
        )).thenReturn(org.springframework.data.domain.Page.empty());

        service.findAll(
                10, 10
        );

        verify(repository, times(1)).findAll(
                any(PageRequest.class)
        );
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_find_all_operations_by_bank_account_id() {
        when(repository.findAllByBankAccountId(
                anyString()
        )).thenReturn(
                Set.of(
                        Operation.builder().build()
                )
        );
        when(bankAccountRepository.findById(anyString())).thenReturn(Optional.ofNullable(BankAccount.builder().build()));

        service.findAllByBankAccountId(
                "id"
        );

        verify(repository, times(1)).findAllByBankAccountId(
                anyString()
        );
        verify(bankAccountRepository, times(1)).findById(anyString());
        verifyNoMoreInteractions(repository);
    }

    @Test
    void should_delete_operation_by_id() {
        when(repository.existsById(anyString())).thenReturn(true);
        service.deleteById("id");

        verify(repository, times(1)).existsById(anyString());
        verify(repository, times(1)).deleteById(anyString());
        verifyNoMoreInteractions(repository);
    }
}