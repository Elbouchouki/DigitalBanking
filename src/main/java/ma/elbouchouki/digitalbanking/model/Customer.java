package ma.elbouchouki.digitalbanking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Customer {
    @Id
    @Column(updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @NotBlank
    @Column(nullable = false, length = 50)
    private String firstname;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String lastname;

    @NotBlank
    @Column(nullable = false, length = 100)
    private String email;

    @NotBlank
    @Column(nullable = false, length = 15)
    private String phone;


}
