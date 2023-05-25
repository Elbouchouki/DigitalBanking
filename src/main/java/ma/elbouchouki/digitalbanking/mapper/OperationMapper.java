package ma.elbouchouki.digitalbanking.mapper;

import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.operation.OperationCreateRequest;
import ma.elbouchouki.digitalbanking.dto.operation.OperationResponse;
import ma.elbouchouki.digitalbanking.dto.operation.OperationUpdateRequest;
import ma.elbouchouki.digitalbanking.model.Operation;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface OperationMapper {

    Operation toOperation(OperationCreateRequest request);

    @Mapping(
            target = "amount",
            expression = "java( new java.math.BigDecimal(String.valueOf(operation.getAmount())))"
    )
    OperationResponse toOperationResponse(Operation operation);

    List<OperationResponse> toOperationResponseList(List<Operation> operationList);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOperationFromDTO(OperationUpdateRequest request, @MappingTarget Operation operation);

    @Mapping(target = "page", expression = "java(operationPage.getNumber())")
    @Mapping(target = "size", expression = "java(operationPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(operationPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(operationPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toOperationResponseList(operationPage.getContent()))")
    PagingResponse<OperationResponse> toPagingResponse(Page<Operation> operationPage);

}
