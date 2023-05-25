package ma.elbouchouki.digitalbanking.dto.operation;

import jakarta.validation.constraints.Min;
import lombok.*;
import ma.elbouchouki.digitalbanking.enums.OperationType;

import java.math.BigDecimal;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class OperationResponse {
    private String id;
    private Date date;
    private @Min(0) BigDecimal amount;
    private OperationType type;
}

