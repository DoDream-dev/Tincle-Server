package tinqle.tinqleServer.util.image.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UpdateFileRequest;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UploadFileRequest;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UploadSingleFileRequest;
import tinqle.tinqleServer.util.image.dto.response.ImageResponseDto.FileDeleteResponse;
import tinqle.tinqleServer.util.image.dto.response.ImageResponseDto.FileListResponseDto;
import tinqle.tinqleServer.util.image.service.ImageService;

import java.util.List;

import static tinqle.tinqleServer.common.constant.SwaggerConstants.*;
import static tinqle.tinqleServer.common.dto.ApiResponse.success;

@RestController
@RequiredArgsConstructor
@Tag(name = TAG_IMAGE, description = TAG_IMAGE_DESCRIPTION)
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<FileListResponseDto> uploadSingleImages(@ModelAttribute @Valid UploadSingleFileRequest uploadSingleFileRequest) {
        return success(imageService.uploadSingle(uploadSingleFileRequest));
    }


    // 다중 S3 이미지 업로드
    @Operation(summary = IMAGE_UPLOAD, description = IMAGE_UPLOAD_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<FileListResponseDto> uploadImages(@ModelAttribute @Valid UploadFileRequest uploadFileRequest) {
        return success(imageService.upload(uploadFileRequest));
    }

    // 이미지 파일 업데이트
    @Operation(summary = IMAGE_UPDATE, description = IMAGE_UPDATE_DESCRIPTION)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> updateImages(@ModelAttribute @Valid UpdateFileRequest updateFileRequest) {
        return success(imageService.updateImage(updateFileRequest));
    }

    // 이미지 파일 삭제
    @Operation(summary = IMAGE_DELETE)
    @SecurityRequirement(name = SECURITY_SCHEME_NAME)
    @DeleteMapping("{type}")
    public ApiResponse<FileDeleteResponse> deleteImages(
            @PathVariable String type,
            @RequestParam List<String> fileUrls
    ) {
        imageService.deleteMultipleImages(type, fileUrls);
        return success(FileDeleteResponse.of());
    }
}
