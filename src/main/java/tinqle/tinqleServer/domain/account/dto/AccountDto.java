package tinqle.tinqleServer.domain.account.dto;

public class AccountDto {

    public record SigningAccount(String socialEmail, String socialUuid, String socialType, String nickname) {}
}
