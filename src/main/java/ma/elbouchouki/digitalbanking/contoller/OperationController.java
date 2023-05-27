package ma.elbouchouki.digitalbanking.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.operation.OperationCreateRequest;
import ma.elbouchouki.digitalbanking.dto.operation.OperationResponse;
import ma.elbouchouki.digitalbanking.service.OperationService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/operations")
public class OperationController {
    private final OperationService service;

    @PostMapping
    public ResponseEntity<OperationResponse> save(@Valid @RequestBody OperationCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<PagingResponse<OperationResponse>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size
    ) {
        return ResponseEntity.ok(
                service.findAll(page, size)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperationResponse> get(
            @PathVariable("id") String id
    ) {
        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @GetMapping("/bank-account/{bankAccountId}")
    public ResponseEntity<Set<OperationResponse>> getAllByBankAccountId(
            @PathVariable("bankAccountId") String bankAccountId
    ) {
        return ResponseEntity.ok(
                service.findAllByBankAccountId(bankAccountId)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") String id) {
        service.deleteById(id);
        return ResponseEntity
                .noContent()
                .build();
    }
}
