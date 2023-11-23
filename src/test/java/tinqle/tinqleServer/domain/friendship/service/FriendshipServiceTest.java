package tinqle.tinqleServer.domain.friendship.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.account.template.AccountTemplate;
import tinqle.tinqleServer.domain.friendship.dto.request.FriendshipRequestDto;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto;
import tinqle.tinqleServer.domain.friendship.dto.response.FriendshipResponseDto.CodeResponse;
import tinqle.tinqleServer.domain.friendship.exception.FriendshipException;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipTemplate.createDummyFriendship;

@ExtendWith(MockitoExtension.class)
public class FriendshipServiceTest {

    @InjectMocks
    FriendshipService friendshipService;

    @Mock
    AccountService accountService;
    @Mock
    FriendshipRepository friendshipRepository;

    private static final Account dummyAccountA = AccountTemplate.createDummyAccountA();
    private static final Account dummyAccountB = AccountTemplate.createDummyAccountB();

    @Test
    @DisplayName("내 코드 조회 - 성공")
    public void getCode_success() throws Exception {
        //given
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        //when
        CodeResponse responseDto = friendshipService.getCode(1L);

        //then
        assertThat(responseDto.code()).isEqualTo(dummyAccountA.getCode());
    }

    @Test
    @DisplayName("친구 닉네임 변경 - 성공")
    public void changeFriendNickname_success() throws Exception {
        //given
        Friendship dummyFriendship = createDummyFriendship(dummyAccountA, dummyAccountB, false);
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(friendshipRepository.findByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB))
                .willReturn(Optional.of(dummyFriendship));

        //when
        FriendshipResponseDto.ChangeFriendNicknameResponse responseDto = friendshipService.changeFriendNickname(1L, new FriendshipRequestDto.ChangeFriendNicknameRequest(2L, "닉네임"));

        //then
        assertThat(responseDto.friendNickname()).isEqualTo("닉네임");
    }

    @Test
    @DisplayName("친구 닉네임 변경 - 실패 (똑같은 닉네임)")
    public void changeFriendNickname_failForSameNickname() throws Exception {
        //given
        Friendship dummyFriendship = createDummyFriendship(dummyAccountA, dummyAccountB, true);
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(friendshipRepository.findByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB))
                .willReturn(Optional.of(dummyFriendship));

        //when - then
        assertThatThrownBy(() -> friendshipService.changeFriendNickname(1L, new FriendshipRequestDto.ChangeFriendNicknameRequest(2L, "바꾼 닉네임")))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("request nickname equal account's nickname");
    }

    @Test
    @DisplayName("친구 닉네임 변경 - 실패 (친구가 아님)")
    public void changeFriendNickname_failForNotExistFriendship() throws Exception {
        //given
        given(accountService.getAccountById(1L)).willReturn(dummyAccountA);
        given(accountService.getAccountById(2L)).willReturn(dummyAccountB);
        given(friendshipRepository.findByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB))
                .willReturn(Optional.empty());

        //when - then
        assertThatThrownBy(() -> friendshipService.changeFriendNickname(1L, new FriendshipRequestDto.ChangeFriendNicknameRequest(2L, "바꾼 닉네임")))
                .isInstanceOf(FriendshipException.class)
                .hasMessage("not found friendship error");
    }
}
