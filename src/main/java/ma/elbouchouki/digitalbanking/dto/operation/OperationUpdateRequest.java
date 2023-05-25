package ma.elbouchouki.digitalbanking.dto.operation;

import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import ma.elbouchouki.digitalbanking.enums.OperationType;

import java.math.BigDecimal;
import java.util.Date;

@Builder
public record OperationUpdateRequest(
        Date date,
        @DecimalMin("0") BigDecimal amount,
        OperationType type
) {
}
