package tinqle.tinqleServer.util.image.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tinqle.tinqleServer.common.dto.ApiResponse;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UploadFileRequest;
import tinqle.tinqleServer.util.image.service.ImageService;

@RestController
@RequestMapping("/images")
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponse<?> uploadFiles(@ModelAttribute UploadFileRequest uploadFileRequest) {
        return ApiResponse.success(imageService.upload(uploadFileRequest));
    }
}
