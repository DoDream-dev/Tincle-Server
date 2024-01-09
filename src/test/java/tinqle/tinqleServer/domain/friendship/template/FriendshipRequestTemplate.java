package tinqle.tinqleServer.domain.friendship.template;

import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.friendship.model.RequestStatus;

public class FriendshipRequestTemplate {

    public static FriendshipRequest createDummyFriendshipRequest(Account requestAccount, Account responseAccount, RequestStatus requestStatus) {
        return FriendshipRequest.builder()
                .id(1L)
                .requestAccount(requestAccount)
                .responseAccount(responseAccount)
                .requestStatus(requestStatus)
                .message("안녕")
                .build();
    }

    public static FriendshipRequest createDummyFriendshipRequestExceptId(Account requestAccount, Account responseAccount, RequestStatus requestStatus) {
        return FriendshipRequest.builder()
                .requestAccount(requestAccount)
                .responseAccount(responseAccount)
                .requestStatus(requestStatus)
                .message("안녕")
                .build();
    }
}
