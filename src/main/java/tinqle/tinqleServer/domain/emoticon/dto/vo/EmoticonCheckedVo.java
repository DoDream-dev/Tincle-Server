package tinqle.tinqleServer.domain.emoticon.dto.vo;

import lombok.Builder;

import java.util.List;

public record EmoticonCheckedVo (
        boolean isCheckedSmileEmoticon,
        boolean isCheckedSadEmoticon,
        boolean isCheckedHeartEmoticon,
        boolean isCheckedSurpriseEmoticon
) {
    @Builder
    public EmoticonCheckedVo{}

    public static EmoticonCheckedVo of(List<EmoticonCountVo> emoticonCountVoList) {
        boolean isCheckedSmileEmoticonInit = false, isCheckedSadEmoticonInit = false,
                isCheckedHeartEmoticonInit = false, isCheckedSurpriseEmoticonInit = false;

        for (EmoticonCountVo emoticonCountVo : emoticonCountVoList) {
            switch (emoticonCountVo.getEmoticonType()) {
                case "SMILE" -> isCheckedSmileEmoticonInit = emoticonCountVo.getCount() == 1;
                case "SAD" -> isCheckedSadEmoticonInit = emoticonCountVo.getCount() == 1;
                case "HEART" -> isCheckedHeartEmoticonInit = emoticonCountVo.getCount() == 1;
                case "SURPRISE" -> isCheckedSurpriseEmoticonInit = emoticonCountVo.getCount() == 1;
            }
        }

        return EmoticonCheckedVo.builder()
                .isCheckedSmileEmoticon(isCheckedSmileEmoticonInit)
                .isCheckedSadEmoticon(isCheckedSadEmoticonInit)
                .isCheckedHeartEmoticon(isCheckedHeartEmoticonInit)
                .isCheckedSurpriseEmoticon(isCheckedSurpriseEmoticonInit).build();
    }

}



