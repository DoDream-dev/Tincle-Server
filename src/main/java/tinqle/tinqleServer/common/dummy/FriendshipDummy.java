package tinqle.tinqleServer.common.dummy;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.ArrayList;
import java.util.List;

@Component("friendshipDummy")
@DependsOn("accountDummy")
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FriendshipDummy {

    private final FriendshipRepository friendshipRepository;
    private final AccountRepository accountRepository;

    @PostConstruct
    public void init() {
        if (friendshipRepository.count() > 0) {
            log.info("[friendshipDummy] 친구 데이터가 이미 존재");
        } else {
            createFriendships();
            log.info("[friendshipDummy] 친구 더미 생성 완료");
        }
    }

    private void createFriendships() {
        List<Account> accounts = accountRepository.findAll();

        ArrayList<Boolean> contents = new ArrayList<>();
        contents.add(true);
        contents.add(false);
        for (int i = 0; i < 50; i++) {
            for (int j = i+1; j < 50; j++) {
                Friendship friendshipA = Friendship.builder()
                        .accountSelf(accounts.get(i))
                        .accountFriend(accounts.get(j))
                        .friendNickname("변경닉네임"+(j+1))
                        .isChangeFriendNickname(contents.get((int) (Math.random() * 100) % 2))
                        .build();

                Friendship friendshipB = Friendship.builder()
                        .accountSelf(accounts.get(j))
                        .accountFriend(accounts.get(i))
                        .friendNickname("변경닉네임"+(i+1))
                        .isChangeFriendNickname(contents.get((int) (Math.random() * 100) % 2))
                        .build();

                friendshipRepository.save(friendshipA);
                friendshipRepository.save(friendshipB);
            }

        }
    }
}
