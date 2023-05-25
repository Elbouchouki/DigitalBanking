package ma.elbouchouki.digitalbanking.contoller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerCreateRequest;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerResponse;
import ma.elbouchouki.digitalbanking.dto.customer.CustomerUpdateRequest;
import ma.elbouchouki.digitalbanking.service.CustomerService;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/customers")
public class CustomerController {
    private final CustomerService service;

    @PostMapping
    public ResponseEntity<CustomerResponse> save(@Valid @RequestBody CustomerCreateRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(service.save(request));
    }

    @GetMapping
    public ResponseEntity<PagingResponse<CustomerResponse>> getAll(
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
            @RequestParam(defaultValue = "", required = false) String search
    ) {
        return ResponseEntity.ok(
                service.findAll(page, size, search)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> get(@PathVariable("id") String id) {
        return ResponseEntity.ok(
                service.findById(id)
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerResponse> update(
            @PathVariable("id") String id,
            @Valid @RequestBody CustomerUpdateRequest request
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
