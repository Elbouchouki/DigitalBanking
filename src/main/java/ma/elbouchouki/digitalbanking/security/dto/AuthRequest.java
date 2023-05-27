package ma.elbouchouki.digitalbanking.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(
        @NotBlank String username,
        @NotBlank String password
) {
}
