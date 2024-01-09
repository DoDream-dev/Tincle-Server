package tinqle.tinqleServer.domain.knock.template;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.knock.model.Knock;

public class KnockTemplate {

    public static Knock createKnock(Long id, Account account, Account sendAccount) {
        return Knock.builder()
                .id(id)
                .account(account)
                .sendAccount(sendAccount).build();
    }

    public static Knock createKnockExceptId(Account account, Account sendAccount) {
        return Knock.builder()
                .account(account)
                .sendAccount(sendAccount).build();
    }
}
