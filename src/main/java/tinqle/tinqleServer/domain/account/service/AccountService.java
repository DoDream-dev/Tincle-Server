package tinqle.tinqleServer.domain.account.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tinqle.tinqleServer.common.dto.SliceResponse;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.account.dto.request.AccountRequestDto.UpdateCodeRequest;
import tinqle.tinqleServer.domain.account.dto.request.AccountRequestDto.UpdateNicknameRequest;
import tinqle.tinqleServer.domain.account.dto.request.AccountRequestDto.UpdateProfileImageUrlRequest;
import tinqle.tinqleServer.domain.account.dto.request.AccountRequestDto.UpdateFcmTokenRequest;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.MyAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.OthersAccountInfoResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateNicknameResponse;
import tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.UpdateStatusResponse;
import tinqle.tinqleServer.domain.account.dto.response.AuthResponseDto.RevokeResponse;
import tinqle.tinqleServer.domain.account.exception.AccountException;
import tinqle.tinqleServer.domain.account.model.Account;
import tinqle.tinqleServer.domain.account.model.SocialType;
import tinqle.tinqleServer.domain.account.model.Status;
import tinqle.tinqleServer.domain.account.repository.AccountRepository;
import tinqle.tinqleServer.domain.friendship.model.Friendship;
import tinqle.tinqleServer.domain.friendship.model.FriendshipRequest;
import tinqle.tinqleServer.domain.friendship.model.RequestStatus;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRepository;
import tinqle.tinqleServer.domain.friendship.repository.FriendshipRequestRepository;

import java.util.Optional;
import java.util.regex.Pattern;

import static tinqle.tinqleServer.common.constant.GlobalConstants.PATTERN_REGEX;
import static tinqle.tinqleServer.domain.account.dto.response.AccountResponseDto.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final GoogleService googleService;
    private final KakaoService kakaoService;
    private final AppleService appleService;
    private final FriendshipRepository friendshipRepository;
    private final FriendshipRequestRepository requestRepository;

    public MyAccountInfoResponse getMyAccountInfo(Long accountId) {
        Account account = getAccountById(accountId);

        return MyAccountInfoResponse.of(account);
    }

    public OthersAccountInfoResponse getOthersAccountInfo(Long accountId, Long getInfoTargetId) {
        Account loginAccount = getAccountById(accountId);
        Account targetAccount = getAccountById(getInfoTargetId);

        if (accountId.equals(getInfoTargetId)) return OthersAccountInfoResponse.of(getMyAccountInfo(accountId));

        //친구인지 확인
        Optional<Friendship> friendshipOptional = friendshipRepository.findByAccountSelfAndAccountFriend(loginAccount, targetAccount);

        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();

            return OthersAccountInfoResponse.of(friendship);
        }

        // 상대방이 친구 신청 걸었는지 확인
        Optional<FriendshipRequest> friendshipRequestOptional = requestRepository.findByRequestAccountAndResponseAccountAndRequestStatus(targetAccount, loginAccount, RequestStatus.WAITING);
        if (friendshipRequestOptional.isPresent())
            return OthersAccountInfoResponse.of(targetAccount, "request", friendshipRequestOptional.get().getId());

        //친구 신청상태인지 확인
        boolean exists = requestRepository.
                existsByRequestAccountAndResponseAccountAndRequestStatus(loginAccount, targetAccount, RequestStatus.WAITING);



        return (exists) ? OthersAccountInfoResponse.of(targetAccount, "waiting", 0L)
                : OthersAccountInfoResponse.of(targetAccount, "false", 0L);
    }

    public SearchCodeResponse searchByCode(Long accountId, Pageable pageable, Long cursorId, String keyword) {
        checkAccountById(accountId);
        Slice<Friendship> friendships = friendshipRepository.findContainFriendNicknameAndIdNotEqual(accountId, pageable, cursorId, keyword);
        Slice<OthersAccountInfoResponse> result = friendships
                .map(OthersAccountInfoResponse::of);

        if (cursorId == null || cursorId == 0L) {
            Optional<Account> accountOptional = accountRepository.findByCode(keyword);

            return new SearchCodeResponse(((accountOptional.map(account -> getOthersAccountInfo(accountId, account.getId())).orElse(null))),
                    SliceResponse.of(result));
        }

        return new SearchCodeResponse(null, SliceResponse.of(result));
    }

    //추후 삭제
    public OthersAccountInfoResponse searchByCode(Long accountId, String code) {
        checkAccountById(accountId);
        Optional<Account> accountOptional = accountRepository.findByCode(code);

        if (accountOptional.isEmpty()) throw new AccountException(StatusCode.NOT_FOUND_ACCOUNT_CODE);

        return getOthersAccountInfo(accountId, accountOptional.get().getId());
    }

    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId).orElseThrow(() -> new AccountException(StatusCode.NOT_FOUND_ACCOUNT));
    }

    public void checkAccountById(Long accountId) {
        boolean exists = accountRepository.existsById(accountId);
        if (!exists) throw new AccountException(StatusCode.NOT_FOUND_ACCOUNT);
    }

    public CheckCodeResponse isDuplicatedCode(String code) {
        validateDuplicatedCode(code);
        return new CheckCodeResponse(true);
    }

    public void validateDuplicatedCode(String code) {
        if (!Pattern.matches(PATTERN_REGEX, code))
            throw new AccountException(StatusCode.CODE_VALIDATE_ERROR);

        if (accountRepository.existsByCode(code))
            throw new AccountException(StatusCode.CODE_DUPLICATE_ERROR);
    }

    @Transactional
    public UpdateNicknameResponse updateNickname(Long accountId, UpdateNicknameRequest updateNicknameRequest) {
        Account loginAccount = getAccountById(accountId);
        String nickname = updateNicknameRequest.nickname();

        validateNickname(nickname);

        if (nickname.equals(loginAccount.getNickname())) throw new AccountException(StatusCode.SAME_NICKNAME_ERROR);
        loginAccount.updateNickname(nickname);

        return new UpdateNicknameResponse(loginAccount.getNickname());
    }

    private void validateNickname(String nickname) {
        if (nickname.length() > 10) throw new AccountException(StatusCode.NICKNAME_LENGTH_ERROR);
    }

    @Transactional
    public UpdateStatusResponse updateStatus(Long accountId, String paramStatus) {
        Account loginAccount = getAccountById(accountId);
        Status status = Status.toEnum(paramStatus);

        if (loginAccount.getStatus().equals(status)) throw new AccountException(StatusCode.SAME_STATUS_ERROR);

        loginAccount.updateStatus(status);

        return new UpdateStatusResponse(status.toString());
    }

    @Transactional
    public UpdateProfileImageUrlResponse updateProfileImageUrl(Long accountId, UpdateProfileImageUrlRequest updateProfileImageUrlRequest) {
        Account loginAccount = getAccountById(accountId);
        loginAccount.updateProfileImageUrl(updateProfileImageUrlRequest.profileImageUrl());
        return new UpdateProfileImageUrlResponse(loginAccount.getProfileImageUrl());
    }

    @Transactional
    public UpdateCodeResponse updateCode(Long accountId, UpdateCodeRequest updateCodeRequest) {
        Account loginAccount = getAccountById(accountId);
        String code = updateCodeRequest.code();
        validateDuplicatedCode(code);

        loginAccount.updateCode(code);
        return new UpdateCodeResponse(loginAccount.getCode());
    }

    public PushNotificationStatusResponse getPushNotificationStatus(Long accountId) {
        Account loginAccount = getAccountById(accountId);

        return new PushNotificationStatusResponse(loginAccount.isReceivedPushNotification());
    }


    @Transactional
    public PushNotificationStatusResponse updatePushNotificationStatus(Long accountId, boolean isReceived) {
        Account loginAccount = getAccountById(accountId);
        loginAccount.updatePushNotificationStatus(isReceived);
        return new PushNotificationStatusResponse(loginAccount.isReceivedPushNotification());
    }

    @Transactional
    public UpdateFcmTokenResponse updateFcmToken(Long accountId, UpdateFcmTokenRequest updateFcmTokenRequest) {
        Account loginAccount = getAccountById(accountId);

        loginAccount.updateFcmToken(updateFcmTokenRequest.fcmToken());
        return new UpdateFcmTokenResponse(loginAccount.getFcmToken());
    }

    @Transactional
    public RevokeResponse revoke(Long accountId) {
        Account loginAccount = getAccountById(accountId);
        SocialType socialType = loginAccount.getSocialType();
        String refreshToken = loginAccount.getProviderRefreshToken();
        String[] split = loginAccount.getSocialEmail().split("@");

        boolean result = switch (socialType) {
            case GOOGLE -> googleService.revokeGoogle(refreshToken);
            case KAKAO -> kakaoService.revokeKakao(split[0]);
            case APPLE -> appleService.revokeApple(refreshToken);
        };
        accountRepository.delete(loginAccount);
        return new RevokeResponse(result);
    }


}
