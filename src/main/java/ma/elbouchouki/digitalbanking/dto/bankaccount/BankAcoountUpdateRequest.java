package ma.elbouchouki.digitalbanking.dto.bankaccount;

import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;

import java.math.BigDecimal;

@Builder
public record BankAcoountUpdateRequest(
        String currency,
        AccountStatus status,
        @DecimalMin("0") BigDecimal interestRate,
        @DecimalMin("0") BigDecimal overDraft
) {
}
