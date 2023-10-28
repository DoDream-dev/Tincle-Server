package tinqle.tinqleServer.util.image.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static tinqle.tinqleServer.common.constant.ValidConstants.S3_FILE_TYPE_MAX_LENGTH;

public class ImageRequestDto {

    public record UploadFileRequest(
            @NotBlank @Size(max = S3_FILE_TYPE_MAX_LENGTH)
            String type,
            List<MultipartFile> files
    ) {}

    public record UploadSingleFileRequest(
            @NotBlank @Size(max = S3_FILE_TYPE_MAX_LENGTH)
            String type,
            MultipartFile file
    ) {}

    public record UpdateFileRequest(
            @NotBlank @Size(max = S3_FILE_TYPE_MAX_LENGTH)
            String type,
            List<String> urlsToDelete,
            List<MultipartFile> newFiles
    ) {}



}
