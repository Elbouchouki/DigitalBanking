package ma.elbouchouki.digitalbanking.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record PagingResponse<I>(
        int page,
        int size,
        int totalPages,
        int totalElements,
        List<I> records
) {
}
