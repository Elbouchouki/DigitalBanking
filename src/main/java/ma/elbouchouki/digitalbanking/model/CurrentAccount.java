package ma.elbouchouki.digitalbanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@Data
@DiscriminatorValue("CURRENT")
public class CurrentAccount extends BankAccount {
    private double overDraft;
}
