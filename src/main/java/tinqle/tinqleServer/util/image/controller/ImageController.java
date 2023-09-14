package tinqle.tinqleServer.util.image.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UpdateFileRequest;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UploadFileRequest;
import tinqle.tinqleServer.util.image.dto.response.ImageResponseDto.FileDeleteResponse;
import tinqle.tinqleServer.util.image.dto.response.ImageResponseDto.FileListResponseDto;
import tinqle.tinqleServer.util.image.service.ImageService;

import java.util.List;

import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    // S3 이미지 업로드
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<FileListResponseDto> uploadImages(@ModelAttribute @Valid UploadFileRequest uploadFileRequest) {
        return success(imageService.upload(uploadFileRequest));
    }

    // 이미지 파일 업데이트
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateImages(@ModelAttribute @Valid UpdateFileRequest updateFileRequest) {
        return success(imageService.updateImage(updateFileRequest));
    }

    // 이미지 파일 삭제
    @DeleteMapping("{type}")
    public ApiResponse<FileDeleteResponse> deleteImages(
            @PathVariable String type,
            @RequestParam List<String> fileUrls
    ) {
        imageService.deleteMultipleImages(type, fileUrls);
        return success(FileDeleteResponse.of());
    }
}
