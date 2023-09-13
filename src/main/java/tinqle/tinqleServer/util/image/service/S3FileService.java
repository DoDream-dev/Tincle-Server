package tinqle.tinqleServer.util.image.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.util.image.S3SaveDir;
import tinqle.tinqleServer.util.image.exception.ImageException;

import java.io.IOException;
import java.io.InputStream;

import static tinqle.tinqleServer.util.image.dto.response.ImageResponseDto.*;

@Service
@RequiredArgsConstructor
public class S3FileService {
    private final AmazonS3Client amazonS3Client;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public FileResponseDto upload(MultipartFile uploadFile, String s3UploadFilePath, S3SaveDir s3SaveDir) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());
        String bucketName = bucket + s3SaveDir.path;
        try (InputStream inputStream = uploadFile.getInputStream()) {
            amazonS3Client.putObject(new PutObjectRequest(bucketName, s3UploadFilePath,inputStream,metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (AmazonServiceException e) {
            throw new ImageException(StatusCode.AWS_S3_UPLOAD_FAIL);
        }

        String fileUrl = amazonS3Client.getUrl(bucketName, s3UploadFilePath).toString();

        return new FileResponseDto(uploadFile.getOriginalFilename(), fileUrl);
    }
}
