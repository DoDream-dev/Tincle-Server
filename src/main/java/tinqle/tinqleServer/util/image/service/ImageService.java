package tinqle.tinqleServer.util.image.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tinqle.tinqleServer.util.image.FilePathUtils;
import tinqle.tinqleServer.util.image.S3SaveDir;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UpdateFileRequest;
import tinqle.tinqleServer.util.image.dto.request.ImageRequestDto.UploadFileRequest;

import java.util.List;

import static tinqle.tinqleServer.util.image.dto.response.ImageResponseDto.*;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final S3FileService s3FileService;

    public FileListResponseDto upload(UploadFileRequest uploadFileRequest) {

        List<FileResponseDto> uploadedFiles = uploadFileRequest.files().stream()
                .map(file -> uploadImage(file, uploadFileRequest.type()))
                .toList();

        return new FileListResponseDto(uploadedFiles);
    }

    private FileResponseDto uploadImage(MultipartFile multipartFile, String type) {
        S3SaveDir s3SaveDir = S3SaveDir.toEnum(type);
        //파일 타입에 따라 업로드 파일 경로 만들기
        String s3UploadFilePath = FilePathUtils.createS3UploadFilePath(multipartFile);
        //s3 업로드
        return s3FileService.upload(multipartFile, s3UploadFilePath, s3SaveDir);
    }

    public FileListResponseDto updateImage(UpdateFileRequest updateFileRequest) {

        deleteMultipleImages(updateFileRequest.type(), updateFileRequest.urlsToDelete());

        List<FileResponseDto> uploadedFiles = updateFileRequest.newFiles().stream()
                .map(file -> uploadImage(file, updateFileRequest.type()))
                .toList();

        return new FileListResponseDto(uploadedFiles);
    }


    public void deleteMultipleImages(String type, List<String> urlsToDelete) {
        S3SaveDir s3SaveDir = S3SaveDir.toEnum(type);
        urlsToDelete.forEach((imageUrl) -> {
            String filePath = FilePathUtils.parseFilePathFromUrl(imageUrl);
            s3FileService.delete(filePath, s3SaveDir);
        });
    }

}
