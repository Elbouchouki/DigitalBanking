package ma.elbouchouki.digitalbanking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.elbouchouki.digitalbanking.constant.CoreConstants;
import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.operation.OperationCreateRequest;
import ma.elbouchouki.digitalbanking.dto.operation.OperationResponse;
import ma.elbouchouki.digitalbanking.enums.OperationType;
import ma.elbouchouki.digitalbanking.exception.ElementAlreadyExistsException;
import ma.elbouchouki.digitalbanking.exception.ElementNotFoundException;
import ma.elbouchouki.digitalbanking.exception.InsufficientBalanceException;
import ma.elbouchouki.digitalbanking.mapper.OperationMapper;
import ma.elbouchouki.digitalbanking.model.BankAccount;
import ma.elbouchouki.digitalbanking.model.Operation;
import ma.elbouchouki.digitalbanking.repository.BankAccountRepository;
import ma.elbouchouki.digitalbanking.repository.OperationRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class OperationServiceImpl implements OperationService {
    private final String ELEMENT_TYPE = "Operation";
    private final String ID_FIELD_NAME = "id";
    private final OperationMapper mapper;
    private final OperationRepository repository;
    private final BankAccountRepository bankAccountRepository;

    @Override
    @Transactional
    public OperationResponse save(final OperationCreateRequest request) throws ElementAlreadyExistsException {
        BankAccount bankAccount = bankAccountRepository.findById(request.bankAccountId())
                .orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{"BankAccount", "id", request.bankAccountId()},
                                null
                        ));

        Operation operation = mapper.toOperation(request);

        if (operation.getType().equals(OperationType.CREDIT)) {
            if (bankAccount.getBalance() < operation.getAmount()) {
                throw new InsufficientBalanceException();
            }
            bankAccount.setBalance(bankAccount.getBalance() - operation.getAmount());
        } else {
            bankAccount.setBalance(bankAccount.getBalance() + operation.getAmount());
        }

        operation.setDate(new Date());
        operation.setBankAccount(bankAccount);

        return mapper.toOperationResponse(
                repository.save(operation)
        );
    }

    @Override
    public OperationResponse findById(String id) throws ElementNotFoundException {
        return mapper.toOperationResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ElementNotFoundException(
                                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                                        null
                                ))
        );
    }

    @Override
    public PagingResponse<OperationResponse> findAll(int page, int size) {
        return mapper.toPagingResponse(
                repository.findAll(
                        PageRequest.of(page, size)
                )
        );
    }

    @Override
    public Set<OperationResponse> findAllByBankAccountId(String bankAccountId) {
        return mapper.toOperationResponseSet(
                repository.findAllByBankAccountId(
                        bankAccountId
                )
        );
    }

    @Override
    @Transactional
    public void deleteById(String id) throws ElementNotFoundException {
        if (!repository.existsById(id)) {
            throw new ElementNotFoundException(
                    CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                    new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                    null
            );
        }
        repository.deleteById(id);
    }
}
