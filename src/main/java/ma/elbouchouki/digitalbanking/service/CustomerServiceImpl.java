package ma.elbouchouki.digitalbanking.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ma.elbouchouki.digitalbanking.constant.CoreConstants;
import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerCreateRequest;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerUpdateRequest;
import ma.elbouchouki.digitalbanking.exception.ElementAlreadyExistsException;
import ma.elbouchouki.digitalbanking.exception.ElementNotFoundException;
import ma.elbouchouki.digitalbanking.mapper.CustomerMapper;
import ma.elbouchouki.digitalbanking.model.Customer;
import ma.elbouchouki.digitalbanking.repository.CustomerRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final String ELEMENT_TYPE = "Customer";
    private final String ID_FIELD_NAME = "id";
    private final CustomerMapper mapper;
    private final CustomerRepository repository;
    private final BankAccountService bankAccountService;

    @Override
    public CustomerResponse save(final CustomerCreateRequest request) throws ElementAlreadyExistsException {
        return mapper.toCustomerResponse(
                repository.save(
                        mapper.toCustomer(request)
                )
        );
    }

    @Override
    public CustomerResponse findById(String id, boolean includeBankAccounts, boolean includeOperations) throws ElementNotFoundException {
        CustomerResponse customerResponse = mapper.toCustomerResponse(
                repository.findById(id)
                        .orElseThrow(() ->
                                new ElementNotFoundException(
                                        CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                        new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                                        null
                                ))
        );

        if (includeBankAccounts) {
            customerResponse.setBankAccounts(
                    bankAccountService.findAllByCustomerId(id, includeOperations)
            );
        }

        return customerResponse;
    }

    @Override
    public PagingResponse<CustomerResponse> findAll(int page, int size, String search, boolean includeBankAccounts, boolean includeOperations) {
        PagingResponse<CustomerResponse> response = mapper.toPagingResponse(
                repository.findAll(
                        PageRequest.of(page, size)
                )
        );

        if (includeBankAccounts) {
            response.records().forEach(customerResponse -> {
                customerResponse.setBankAccounts(
                        bankAccountService.findAllByCustomerId(customerResponse.getId(), includeOperations)
                );
            });
        }

        return response;
    }

    @Override
    public CustomerResponse update(String id, CustomerUpdateRequest customerUpdateRequest) throws ElementNotFoundException {
        final Customer customer = repository.findById(id)
                .orElseThrow(() ->
                        new ElementNotFoundException(
                                CoreConstants.BusinessExceptionMessage.NOT_FOUND,
                                new Object[]{ELEMENT_TYPE, ID_FIELD_NAME, id},
                                null
                        ));

        mapper.updateCustomerFromDTO(customerUpdateRequest, customer);

        return mapper.toCustomerResponse(
                repository.save(customer)
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
