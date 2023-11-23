package tinqle.tinqleServer.domain.emoticon.dto.vo;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class EmoticonCountVo {

    private String emoticonType;
    private Long count;

    @QueryProjection
    public EmoticonCountVo(String emoticonType, Long count) {
        this.emoticonType = emoticonType;
        this.count = count;
    }

}
