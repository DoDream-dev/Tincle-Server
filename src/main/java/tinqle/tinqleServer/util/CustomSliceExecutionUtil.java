package tinqle.tinqleServer.util;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

public class CustomSliceExecutionUtil {
    public static <T> Slice<T> getSlice(List<T> content, int size) {
        boolean hasNext = false;

        if (content.size() > size) {  // content.size가 최대일 경우: 항상 page size + 1 이고 다음 레코드가 있다.
            content.remove(size); // limit걸 때 +1 했던 마지막 레코드를 삭제
            hasNext = true;
        }

        return new SliceImpl<>(content, Pageable.ofSize(size), hasNext);
    }

    public static int buildSliceLimit(int size) {   // 언제나 요청한 size + 1개 조회
        return size + 1;
    }
}
