package tinqle.tinqleServer.util.image.dto.response;

import java.util.List;

public class ImageResponseDto {

    public record FileResponseDto(
            String filename,
            String fileUrl
    ) {}

    public record FileListResponseDto(
            List<FileResponseDto> files
    ) {}
}
