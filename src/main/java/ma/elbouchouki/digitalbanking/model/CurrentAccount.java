package ma.elbouchouki.digitalbanking.model;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@DiscriminatorValue("CURRENT")
public class CurrentAccount extends BankAccount {
    @Column(nullable = false)
    private double overDraft;
}
