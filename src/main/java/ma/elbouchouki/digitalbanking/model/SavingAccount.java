package ma.elbouchouki.digitalbanking.model;

import jakarta.persistence.*;
import lombok.*;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@DiscriminatorValue("SAVING")
public class SavingAccount extends BankAccount {
    @Column(nullable = false)
    private double interestRate;

}
