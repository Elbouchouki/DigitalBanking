package ma.elbouchouki.digitalbanking.service;

import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerCreateRequest;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerUpdateRequest;
import ma.elbouchouki.digitalbanking.exception.ElementAlreadyExistsException;
import ma.elbouchouki.digitalbanking.exception.ElementNotFoundException;

public interface CustomerService {
    CustomerResponse save(CustomerCreateRequest request) throws ElementAlreadyExistsException;

    CustomerResponse findById(String id) throws ElementNotFoundException;

    PagingResponse<CustomerResponse> findAll(int page, int size, String search);

    CustomerResponse update(String id, CustomerUpdateRequest request) throws ElementNotFoundException;

    void deleteById(String id) throws ElementNotFoundException;

}
