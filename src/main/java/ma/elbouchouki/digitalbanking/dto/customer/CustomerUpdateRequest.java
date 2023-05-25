package ma.elbouchouki.digitalbanking.dto.customer;

import lombok.Builder;

@Builder
public record CustomerUpdateRequest(
        String firstname,
        String lastname,
        String email,
        String phone
) {
}
