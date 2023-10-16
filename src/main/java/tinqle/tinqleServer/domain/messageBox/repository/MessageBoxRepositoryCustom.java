package tinqle.tinqleServer.domain.messageBox.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.messageBox.model.MessageBox;

public interface MessageBoxRepositoryCustom {

    Slice<MessageBox> findAllByAccountCustom(Account account, Pageable pageable, Long cursorId);
}
