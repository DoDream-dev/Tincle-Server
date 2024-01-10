package tinqle.tinqleServer.domain.knock.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.service.AccountService;
import tinqle.tinqleServer.domain.account.template.AccountTemplate;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.service.FriendshipService;
import tinqle.tinqleServer.domain.knock.dto.request.KnockRequestDto.SendKnockRequest;
import tinqle.tinqleServer.domain.knock.dto.response.KnockResponseDto.SendKnockResponse;
import tinqle.tinqleServer.domain.knock.model.Knock;
import tinqle.tinqleServer.domain.knock.exception.KnockException;
import tinqle.tinqleServer.domain.knock.repository.KnockRepository;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams;
import tinqle.tinqleServer.domain.notification.service.NotificationService;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static tinqle.tinqleServer.domain.friendship.template.FriendshipTemplate.createDummyFriendship;
import static tinqle.tinqleServer.domain.knock.template.KnockTemplate.createKnock;
import static tinqle.tinqleServer.domain.notification.dto.NotificationDto.NotifyParams.ofSendKnockMessage;


@ExtendWith(MockitoExtension.class)
public class KnockServiceTest {

    @InjectMocks
    KnockService knockService;

    @Mock
    AccountService accountService;
    @Mock
    KnockRepository knockRepository;
    @Mock
    NotificationService notificationService;
    @Mock
    FriendshipService friendshipService;

    @BeforeEach
    public void setDummyAccount() {
        dummyAccountA = AccountTemplate.createDummyAccountA();
        dummyAccountB = AccountTemplate.createDummyAccountB();
    }

    Account dummyAccountA;
    Account dummyAccountB;

    @Test
    @DisplayName("지금 뭐해? - 실패(중복된 지금 뭐해?)")
    public void sendKnock_fail_duplicate() throws Exception {
        //given
        given(accountService.getAccountById(dummyAccountB.getId())).willReturn(dummyAccountB);
        given(accountService.getAccountById(dummyAccountA.getId())).willReturn(dummyAccountA);
        given(knockRepository.existsByAccountAndSendAccountAndVisibilityIsTrue(dummyAccountA, dummyAccountB)).willReturn(true);

        //when - then
        assertThatThrownBy(() -> knockService.sendKnock(dummyAccountB.getId(), new SendKnockRequest(dummyAccountA.getId())))
                .isInstanceOf(KnockException.class)
                .hasMessage(StatusCode.DUPLICATE_KNOCK_REQUEST.getMessage());
    }

    @Test
    @DisplayName("지금 뭐해? - 성공(친구 이름을 바꿨을 때)")
    public void sendKnock_success_changeFriendNickname() throws Exception {
        //given
        given(accountService.getAccountById(dummyAccountA.getId())).willReturn(dummyAccountA);
        given(accountService.getAccountById(dummyAccountB.getId())).willReturn(dummyAccountB);
        given(knockRepository.existsByAccountAndSendAccountAndVisibilityIsTrue(dummyAccountA, dummyAccountB)).willReturn(false);
        Friendship dummyFriendship = createDummyFriendship(dummyAccountA, dummyAccountB, true);
        given(friendshipService.getFriendshipByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB)).willReturn(dummyFriendship);
        given(friendshipService.getFriendNickname(dummyFriendship)).willReturn(dummyFriendship.getFriendNickname());
        Knock knock = createKnock(1L, dummyAccountA, dummyAccountB);
        given(knockRepository.save(any())).willReturn(knock);
        NotifyParams sendKnockMessage = ofSendKnockMessage(dummyAccountA, dummyAccountB, dummyFriendship.getFriendNickname());
        doNothing().when(notificationService).pushMessage(sendKnockMessage);

        //when
        SendKnockResponse sendKnockResponse = knockService.sendKnock(dummyAccountB.getId(), new SendKnockRequest(dummyAccountA.getId()));

        //then
        assertThat(sendKnockResponse.accountId()).isEqualTo(dummyAccountA.getId());
        assertThat(sendKnockResponse.sendAccountId()).isEqualTo(dummyAccountB.getId());
    }

    @Test
    @DisplayName("지금 뭐해? - 성공(친구 이름을 안바꿨을 때)")
    public void sendKnock_success_notChangeFriendNickname() throws Exception {
        //given
        given(accountService.getAccountById(dummyAccountA.getId())).willReturn(dummyAccountA);
        given(accountService.getAccountById(dummyAccountB.getId())).willReturn(dummyAccountB);
        given(knockRepository.existsByAccountAndSendAccountAndVisibilityIsTrue(dummyAccountA, dummyAccountB)).willReturn(false);
        Friendship dummyFriendship = createDummyFriendship(dummyAccountA, dummyAccountB, false);
        given(friendshipService.getFriendshipByAccountSelfAndAccountFriend(dummyAccountA, dummyAccountB)).willReturn(dummyFriendship);
        given(friendshipService.getFriendNickname(dummyFriendship)).willReturn(dummyAccountB.getNickname());
        Knock knock = createKnock(1L, dummyAccountA, dummyAccountB);
        given(knockRepository.save(any())).willReturn(knock);
        NotifyParams sendKnockMessage = ofSendKnockMessage(dummyAccountA, dummyAccountB, dummyAccountB.getNickname());
        doNothing().when(notificationService).pushMessage(sendKnockMessage);

        //when
        SendKnockResponse sendKnockResponse = knockService.sendKnock(dummyAccountB.getId(), new SendKnockRequest(dummyAccountA.getId()));

        //then
        assertThat(sendKnockResponse.accountId()).isEqualTo(dummyAccountA.getId());
        assertThat(sendKnockResponse.sendAccountId()).isEqualTo(dummyAccountB.getId());
    }

    @Test
    @DisplayName("지금 뭐해?를 사용자에게 전송한 사람 조회 - 실패(존재하지 않음)")
    public void getAllKnockByAccountAndVisibilityIsTrue_fail_notExist() throws Exception {
        //given
        given(knockRepository.findAllByAccountAndVisibilityIsTrue(dummyAccountA)).willReturn(List.of());

        //when - then
        assertThatThrownBy(() -> knockService.getAllKnockByAccountAndVisibilityIsTrue(dummyAccountA))
                .isInstanceOf(KnockException.class)
                .hasMessage(StatusCode.NOT_FOUND_KNOCK.getMessage());
    }

    @Test
    @DisplayName("지금 뭐해?를 사용자에게 전송한 사람 조회 - 성공")
    public void getAllKnockByAccountAndVisibilityIsTrue_success() throws Exception {
        //given
        Knock knock = createKnock(1L, dummyAccountA, dummyAccountB);
        given(knockRepository.findAllByAccountAndVisibilityIsTrue(dummyAccountA)).willReturn(List.of(knock));

        //when
        List<Knock> knocks = knockService.getAllKnockByAccountAndVisibilityIsTrue(dummyAccountA);

        //then
        assertThat(knocks.size()).isEqualTo(1);
        assertThat(knocks.get(0)).isEqualTo(knock);

    }

}
