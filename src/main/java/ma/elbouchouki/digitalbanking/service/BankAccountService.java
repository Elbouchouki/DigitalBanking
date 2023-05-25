package ma.elbouchouki.digitalbanking.service;

import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountCreateRequest;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountUpdateRequest;
import ma.elbouchouki.digitalbanking.exception.ElementAlreadyExistsException;
import ma.elbouchouki.digitalbanking.exception.ElementNotFoundException;

import java.util.Set;

public interface BankAccountService {
    BankAccountResponse save(BankAccountCreateRequest request) throws ElementAlreadyExistsException;

    BankAccountResponse findById(String id, boolean includeOperations) throws ElementNotFoundException;

    PagingResponse<BankAccountResponse> findAll(int page, int size, boolean includeOperations);

    Set<BankAccountResponse> findAllByCustomerId(String customerId, boolean includeOperations);

    BankAccountResponse update(String id, BankAccountUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;
}
