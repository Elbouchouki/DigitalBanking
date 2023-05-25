package ma.elbouchouki.digitalbanking.dto.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAcoountResponse;

import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Data
public class CustomerResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Set<BankAcoountResponse> bankAccounts;
}