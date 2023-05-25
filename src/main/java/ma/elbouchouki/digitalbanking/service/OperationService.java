package ma.elbouchouki.digitalbanking.service;

import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.operation.OperationCreateRequest;
import ma.elbouchouki.digitalbanking.dto.operation.OperationResponse;
import ma.elbouchouki.digitalbanking.exception.ElementAlreadyExistsException;
import ma.elbouchouki.digitalbanking.exception.ElementNotFoundException;

import java.util.Set;

public interface OperationService {
    OperationResponse save(OperationCreateRequest request) throws ElementAlreadyExistsException;

    OperationResponse findById(String id) throws ElementNotFoundException;

    PagingResponse<OperationResponse> findAll(int page, int size);

    Set<OperationResponse> findAllByBankAccountId(String bankAccountId);

    void deleteById(String id) throws ElementNotFoundException;

}
