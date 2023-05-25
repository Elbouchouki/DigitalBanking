package ma.elbouchouki.digitalbanking.dto.bankaccount;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;

import java.math.BigDecimal;

@Builder
public record BankAcoountCreateRequest(
        @NotNull String currency,
        @NotNull AccountStatus status,
        @DecimalMin("0") BigDecimal interestRate,
        @DecimalMin("0") BigDecimal overDraft
) {
}

