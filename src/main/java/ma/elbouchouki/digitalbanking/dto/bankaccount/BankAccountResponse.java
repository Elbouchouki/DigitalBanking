package ma.elbouchouki.digitalbanking.dto.bankaccount;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.elbouchouki.digitalbanking.dto.operation.OperationResponse;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;

import java.math.BigDecimal;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class BankAccountResponse {
    private String id;
    private String currency;
    private AccountStatus status;
    private BigDecimal interestRate;
    private BigDecimal overDraf;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<OperationResponse> operations;

}