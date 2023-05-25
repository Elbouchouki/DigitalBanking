package ma.elbouchouki.digitalbanking.dto.customer;


import jakarta.validation.constraints.NotBlank;

import lombok.Builder;


@Builder
public record CustomerCreateRequest(
        @NotBlank String firstname,
        @NotBlank String lastname,
        @NotBlank String email,
        @NotBlank String phone

) {
}

