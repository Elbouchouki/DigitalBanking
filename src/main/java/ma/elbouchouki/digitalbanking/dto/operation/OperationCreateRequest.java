package ma.elbouchouki.digitalbanking.dto.operation;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.elbouchouki.digitalbanking.enums.OperationType;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record OperationCreateRequest(
        @NotBlank String bankAccountId,
        @NotNull @DecimalMin("0") BigDecimal amount,
        @NotNull OperationType type
) {
}
