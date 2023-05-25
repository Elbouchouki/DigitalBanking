package ma.elbouchouki.digitalbanking.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@DiscriminatorValue("SAVING")
public class SavingAccount extends BankAccount {
    private double interestRate;

}
