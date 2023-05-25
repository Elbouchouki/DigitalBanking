package ma.elbouchouki.digitalbanking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.elbouchouki.digitalbanking.constant.CoreConstants;
import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountCreateRequest;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountUpdateRequest;
import ma.elbouchouki.digitalbanking.exception.ElementAlreadyExistsException;
import ma.elbouchouki.digitalbanking.exception.ElementNotFoundException;
import ma.elbouchouki.digitalbanking.mapper.BankAccountMapper;
import ma.elbouchouki.digitalbanking.model.BankAccount;
import ma.elbouchouki.digitalbanking.model.Customer;
import ma.elbouchouki.digitalbanking.repository.BankAccountRepository;
import ma.elbouchouki.digitalbanking.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl implements BankAccountService {
    private final String ELEMENT_TYPE = "BankAccount";
    private final String ID_FIELD_NAME = "id";
    private final BankAccountMapper mapper;
    private final BankAccountRepository repository;
    private final CustomerRepository customerRepository;
    private final OperationService operationService;

    @Override
    public BankAccountResponse save(final BankAccountCreateRequest request) throws ElementAlreadyExistsException {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{"Customer", "id", request.customerId()},
                                null
                        ));

        BankAccount bankAccount = mapper.toBankAccount(request);
        bankAccount.setCustomer(customer);


        return mapper.toBankAccountResponse(
                repository.save(bankAccount)
        );
    }

    @Override
    public BankAccountResponse findById(String id, boolean includeOperations) throws ElementNotFoundException {
        BankAccountResponse bankAccountResponse = mapper.toBankAccountResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ElementNotFoundException(
                                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                                        null
                                ))
        );

        if (includeOperations) {
            bankAccountResponse.setOperations(
                    operationService.findAllByBankAccountId(id)
            );
        }

        return bankAccountResponse;
    }

    @Override
    public PagingResponse<BankAccountResponse> findAll(int page, int size, boolean includeOperations) {
        PagingResponse<BankAccountResponse> response = mapper.toPagingResponse(
                repository.findAll(
                        PageRequest.of(page, size)
                )
        );

        if (includeOperations) {
            response.records().forEach(
                    bankAccountResponse -> bankAccountResponse.setOperations(
                            operationService.findAllByBankAccountId(bankAccountResponse.getId())
                    )
            );
        }

        return response;
    }

    @Override
    public Set<BankAccountResponse> findAllByCustomerId(String customerId, boolean includeOperations) {
        Set<BankAccountResponse> bankAccountResponses = mapper.toBankAccountResponseSet(
                repository.findAllByCustomerId(customerId)
        );

        if (includeOperations) {
            bankAccountResponses.forEach(
                    bankAccountResponse -> bankAccountResponse.setOperations(
                            operationService.findAllByBankAccountId(bankAccountResponse.getId())
                    )
            );
        }

        return bankAccountResponses;
    }

    @Override
    public BankAccountResponse update(String id, BankAccountUpdateRequest bankAccountUpdateRequest) throws ElementNotFoundException {
        final BankAccount bankAccount = repository.findById(id)
                .orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                                null
                        ));

        mapper.fromUpdate(bankAccountUpdateRequest, bankAccount);

        return mapper.toBankAccountResponse(
                repository.save(bankAccount)
        );
    }

    @Override
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
