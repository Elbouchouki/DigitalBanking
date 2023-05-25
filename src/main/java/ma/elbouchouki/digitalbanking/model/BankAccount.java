package ma.elbouchouki.digitalbanking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ma.elbouchouki.digitalbanking.enums.AccountStatus;
import ma.elbouchouki.digitalbanking.enums.OperationType;

import java.util.Date;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TYPE", length = 8)
public class BankAccount {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false)
    private double balance;
    @Column(nullable = false, length = 4)
    private String currency;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    private Date createdAt;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    @OneToMany(mappedBy = "bankAccount")
    private Set<Operation> operations;
}
