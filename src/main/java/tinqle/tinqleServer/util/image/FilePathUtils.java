package tinqle.tinqleServer.util.image;

import org.springframework.web.multipart.MultipartFile;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.util.Objects;
import java.util.UUID;

import static java.text.MessageFormat.format;
import static tinqle.tinqleServer.common.constant.GlobalConstants.*;

public class FilePathUtils {


    public static String createS3UploadFilePath(MultipartFile multipartFile) {
        String originalFilename = Normalizer.normalize(Objects.requireNonNull(multipartFile.getOriginalFilename()), Normalizer.Form.NFC);
        return createS3UploadFileName(originalFilename);
    }

    private static String createS3UploadFileName(String originalFilename) {
        int pos = originalFilename.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        String userFileName = originalFilename.substring(0, pos);
        String fileExtension = originalFilename.substring(pos + 1);
        return format(S3_OBJECT_NAME_PATTERN, userFileName, UUID.randomUUID(), fileExtension);
    }


    public static String parseFilePathFromUrl(String imageUrl) {
        String[] parseUrl = imageUrl.split("/");
        String fileName = parseUrl[parseUrl.length-1];
        return URLDecoder.decode(fileName, StandardCharsets.UTF_8);

    }

}

