package tinqle.tinqleServer.domain.friendship.template;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.Friendship;

public class FriendshipTemplate {

    private static final Long ID_A = 1L;
    private static final Long ID_B = 2L;
    private static final String FRIEND_NICKANME_A = "바꾼 닉네임";


    private static Friendship createFriendship(Long id, Account accountSelf, Account accountFriend,
                                       String friendNickname, boolean isChangeFriendNickname) {
        return Friendship.builder()
                .id(id)
                .accountSelf(accountSelf)
                .accountFriend(accountFriend)
                .friendNickname(friendNickname)
                .isChangeFriendNickname( isChangeFriendNickname).build();
    }

    public static Friendship createDummyFriendship(Account accountSelf, Account accountFriend, boolean isChangeFriendNickname) {
        return createFriendship(ID_A, accountSelf, accountFriend, FRIEND_NICKANME_A, isChangeFriendNickname);
    }

    public static Friendship createDummyFriendshipExceptId(Account accountSelf, Account accountFriend, boolean isChangeFriendNickname) {
        return createFriendship(null, accountSelf, accountFriend, FRIEND_NICKANME_A, isChangeFriendNickname);
    }
}
