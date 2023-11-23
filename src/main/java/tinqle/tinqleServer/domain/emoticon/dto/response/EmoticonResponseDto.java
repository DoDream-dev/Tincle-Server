package tinqle.tinqleServer.domain.emoticon.dto.response;

import java.util.List;

public class EmoticonResponseDto {

    public record EmoticonReactResponse(boolean isCheckedEmoticon) {}

    public record GetNicknameListResponse(
            List<String> heartEmoticonNicknameList,
            List<String> smileEmoticonNicknameList,
            List<String> sadEmoticonNicknameList,
            List<String> surpriseEmoticonNicknameList
    ) {}
}
