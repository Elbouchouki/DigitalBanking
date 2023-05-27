package ma.elbouchouki.digitalbanking.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountCreateRequest;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountUpdateRequest;
import ma.elbouchouki.digitalbanking.service.BankAccountService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/bank-accounts")
public class BankAccountController {
    private final BankAccountService service;

    @PostMapping
    public ResponseEntity<BankAccountResponse> save(@Valid @RequestBody BankAccountCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<PagingResponse<BankAccountResponse>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
            @RequestParam(defaultValue = "", required = false) boolean includeOperations
    ) {
        return ResponseEntity.ok(
                service.findAll(page, size, includeOperations)
        );
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Set<BankAccountResponse>> getAllByCustomerId(
            @PathVariable("customerId") String customerId,
            @RequestParam(defaultValue = "", required = false) boolean includeOperations
    ) {
        return ResponseEntity.ok(
                service.findAllByCustomerId(customerId, includeOperations)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BankAccountResponse> get(
            @PathVariable("id") String id,
            @RequestParam(defaultValue = "", required = false) boolean includeOperations
    ) {
        return ResponseEntity.ok(
                service.findById(id, includeOperations)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BankAccountResponse> update(
            @PathVariable("id") String id,
            @Valid @RequestBody BankAccountUpdateRequest request
    ) {
        return ResponseEntity.ok(
                service.update(id, request)
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
