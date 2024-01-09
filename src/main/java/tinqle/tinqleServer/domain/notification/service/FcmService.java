package tinqle.tinqleServer.domain.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.auth.oauth2.GoogleCredentials;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tinqle.tinqleServer.common.exception.StatusCode;
import tinqle.tinqleServer.domain.notification.dto.FcmDto;
import tinqle.tinqleServer.domain.notification.dto.FcmDto.FcmMessage;
import tinqle.tinqleServer.domain.notification.dto.FcmDto.Message;
import tinqle.tinqleServer.domain.notification.dto.NotificationDto;
import tinqle.tinqleServer.domain.notification.exception.NotificationException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmService {

    private final ObjectMapper objectMapper;
    private final JSONParser jsonParser;


    private static final String FCM_PRIVATE_KEY_PATH = "tinqle-firebase-private-key.json";
    private static final String FIREBASE_SCOPE = "https://www.googleapis.com/auth/cloud-platform";
    private static final String PROJECT_ID_URL = "https://fcm.googleapis.com/v1/projects/tinqle-8706b/messages:send";


    private String getAccessToken() {
        try {
            GoogleCredentials credentials = GoogleCredentials
                    .fromStream(new ClassPathResource(FCM_PRIVATE_KEY_PATH).getInputStream())
                    .createScoped(List.of(FIREBASE_SCOPE));
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            log.warn("FCM getAccessToken Error : {}", e.getMessage());
            throw new NotificationException(StatusCode.GET_FCM_ACCESS_TOKEN_ERROR);
        }
    }

    public String makeMessage(String targetToken, NotificationDto.NotifyParams params) {
        try {
            FcmMessage fcmMessage = new FcmMessage(false, new Message(
                    new FcmDto.Android("high",
                            new FcmDto.Notification("default", params.title(), params.content()),
                            new FcmDto.Data(String.valueOf(params.redirectTargetId()), params.type().toString())),
                    new FcmDto.Apns(
                            new FcmDto.Payload(
                                    new FcmDto.Aps("default", 1L, new FcmDto.Alert(
                                            params.title(), params.content(), "PLAY"
                                    )), String.valueOf(params.redirectTargetId()), params.type().toString(), params.title(), params.content())),
                    targetToken
            ));
//            FcmMessage fcmMessage = new FcmMessage(
//                    false,
//                    new Message(
//                            targetToken,
//                            new FcmDto.Notification(
//                                    params.title(),
//                                    params.content(),
//                                    String.valueOf(params.redirectTargetId()),
//                                    params.type().toString()
//                            )
//                    ));
            return objectMapper.writeValueAsString(fcmMessage);
        } catch (JsonProcessingException e) {
            log.warn("FCM [makeMessage] Error : {}", e.getMessage());
            throw new NotificationException(StatusCode.FCM_MESSAGE_JSON_PARSING_ERROR);
        }
    }

    @Async(value = "AsyncBean")
    public CompletableFuture<Boolean> sendPushMessage(String fcmToken, NotificationDto.NotifyParams params) {
        String message = makeMessage(fcmToken, params);
        String accessToken = getAccessToken();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(PROJECT_ID_URL)
                .addHeader("Authorization", "Bearer " + accessToken)
                .addHeader("Content-Type", "application/json; UTF-8")
                .post(RequestBody.create(message, MediaType.parse("application/json; charset=urf-8")))
                .build();
        try (Response response = client.newCall(request).execute()){
            if (!response.isSuccessful() && response.body() != null) {
                JSONObject responseBody = (JSONObject) jsonParser.parse(response.body().string());
                String errorMessage = ((JSONObject) responseBody.get("error")).get("message").toString();
                log.warn("FCM [sendPushMessage] okHttp response is not OK : {}", errorMessage);
                return CompletableFuture.completedFuture(false);
            }
            return CompletableFuture.completedFuture(true);
        } catch (Exception e) {
            log.warn("FCM [sendPushMessage] I/O Exception : {}", e.getMessage());
            throw new NotificationException(StatusCode.SEND_FCM_PUSH_ERROR);
        }
    }
}
