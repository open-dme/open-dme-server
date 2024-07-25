package io.github.opendme.server.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class FirebaseNotificationService {

    private static final Logger log = LogManager.getLogger(FirebaseNotificationService.class);
    private final FirebaseMessaging firebaseMessaging;

    public FirebaseNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void alertDepartment(Long departmentId) {
        try {
            sendMessageForTopic("department_" + departmentId, "Alarm", "Alarm");
        } catch (FirebaseMessagingException e) {
            log.atError().log("Error sending alarm to topic {} with {} \n {}",
                    "department_" + departmentId,
                    e.getMessagingErrorCode(),
                    e.getStackTrace());
        }
    }

    public void alertMember(Long memberId) {
        //todo
    }

    private String sendMessageForTopic(String topic, String title, String rawMessage) throws FirebaseMessagingException {
        Message message = Message.builder()
                                 .setTopic(topic)
                                 .putData("title", title)
                                 .putData("message", rawMessage)
                                 .build();
        return firebaseMessaging.send(message);
    }

    private String sendNotificationToDevice(String registrationToken, String title, String rawMessage) throws FirebaseMessagingException {
        Message message = Message.builder()
                                 .setToken(registrationToken)
                                 .putData("title", title)
                                 .putData("message", rawMessage)
                                 .build();
        return firebaseMessaging.send(message);
    }
}
