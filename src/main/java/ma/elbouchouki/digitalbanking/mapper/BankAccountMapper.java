package ma.elbouchouki.digitalbanking.mapper;

import ma.elbouchouki.digitalbanking.dto.PagingResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountResponse;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountCreateRequest;
import ma.elbouchouki.digitalbanking.dto.bankaccount.BankAccountUpdateRequest;
import ma.elbouchouki.digitalbanking.model.BankAccount;
import ma.elbouchouki.digitalbanking.model.CurrentAccount;
import ma.elbouchouki.digitalbanking.model.Customer;
import ma.elbouchouki.digitalbanking.model.SavingAccount;
import org.mapstruct.*;
import org.springframework.data.domain.Page;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface BankAccountMapper {
    default BankAccount toBankAccount(BankAccountCreateRequest request) {
        if (request == null) {
            return null;
        }
        if (request.interestRate() != null)
            return SavingAccount.builder()
                    .interestRate(request.interestRate().doubleValue())
                    .currency(request.currency())
                    .balance(0)
                    .status(request.status())
                    .createdAt(new java.util.Date())
                    .build();
        return CurrentAccount.builder()
                .overDraft(request.overDraft().doubleValue())
                .currency(request.currency())
                .balance(0)
                .status(request.status())
                .createdAt(new java.util.Date())
                .build();
    }


    default BankAccountResponse toBankAccountResponse(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }

        BankAccountResponse bankAccountResponse = BankAccountResponse.builder()
                .id(bankAccount.getId())
                .balance(new BigDecimal(String.valueOf(bankAccount.getBalance())))
                .currency(bankAccount.getCurrency())
                .status(bankAccount.getStatus())
                .createdAt(bankAccount.getCreatedAt())
                .build();
        if (bankAccount instanceof SavingAccount)
            bankAccountResponse.setInterestRate(
                    new BigDecimal(
                            String.valueOf(((SavingAccount) bankAccount).getInterestRate())
                    )
            );
        else
            bankAccountResponse.setOverDraft(
                    new BigDecimal(
                            String.valueOf(((CurrentAccount) bankAccount).getOverDraft())
                    )
            );

        return bankAccountResponse;
    }

    List<BankAccountResponse> toBankAccountResponseList(List<BankAccount> bankAccountList);

    Set<BankAccountResponse> toBankAccountResponseSet(Set<BankAccount> bankAccountList);

    default void fromUpdate(BankAccountUpdateRequest bankAccountUpdateRequest, @MappingTarget BankAccount bankAccount) {
        if (bankAccountUpdateRequest == null) {
            return;
        }

        if (bankAccount instanceof SavingAccount)
            if (bankAccountUpdateRequest.interestRate() != null)
                ((SavingAccount) bankAccount).setInterestRate(bankAccountUpdateRequest.interestRate().doubleValue());
            else if (bankAccountUpdateRequest.overDraft() != null)
                ((CurrentAccount) bankAccount).setOverDraft(bankAccountUpdateRequest.overDraft().doubleValue());
        if (bankAccountUpdateRequest.currency() != null)
            bankAccount.setCurrency(bankAccountUpdateRequest.currency());
        if (bankAccountUpdateRequest.status() != null)
            bankAccount.setStatus(bankAccountUpdateRequest.status());
    }

    @Mapping(target = "page", expression = "java(bankAccountPage.getNumber())")
    @Mapping(target = "size", expression = "java(bankAccountPage.getSize())")
    @Mapping(target = "totalPages", expression = "java(bankAccountPage.getTotalPages())")
    @Mapping(target = "totalElements", expression = "java(bankAccountPage.getNumberOfElements())")
    @Mapping(target = "records", expression = "java(toBankAccountResponseList(bankAccountPage.getContent()))")
    PagingResponse<BankAccountResponse> toPagingResponse(Page<BankAccount> bankAccountPage);

}
