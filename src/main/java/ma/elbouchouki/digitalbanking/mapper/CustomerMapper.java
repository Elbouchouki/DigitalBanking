package ma.elbouchouki.digitalbanking.mapper;

import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerCreateRequest;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerUpdateRequest;
import ma.elbouchouki.digitalbanking.model.Customer;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CustomerMapper {
    Customer toCustomer(CustomerCreateRequest request);

    CustomerResponse toCustomerResponse(Customer customer);

    List<CustomerResponse> toCustomerResponseList(List<Customer> customerList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCustomerFromDTO(CustomerUpdateRequest request, @MappingTarget Customer customer);

    @Mapping(target = "page", expression = "java(customerPage.getNumber())")
    @Mapping(target = "size", expression = "java(customerPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(customerPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(customerPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toCustomerResponseList(customerPage.getContent()))")
    PagingResponse<CustomerResponse> toPagingResponse(Page<Customer> customerPage);

}
