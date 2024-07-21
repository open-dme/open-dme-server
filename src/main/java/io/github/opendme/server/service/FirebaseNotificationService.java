package io.github.opendme.server.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import org.springframework.stereotype.Service;

@Service
public class FirebaseNotificationService {

    private final FirebaseMessaging firebaseMessaging;

    public FirebaseNotificationService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public String sendMessageForTopic(String topic, String title, String rawMessage) throws FirebaseMessagingException {
        Message message = Message.builder()
                                 .setTopic(topic)
                                 .putData("title", title)
                                 .putData("message", rawMessage)
                                 .build();
        return firebaseMessaging.send(message);
    }

    public String sendNotificationToDevice(String registrationToken, String title, String rawMessage) throws FirebaseMessagingException {
        Message message = Message.builder()
                                 .setToken(registrationToken)
                                 .putData("title", title)
                                 .putData("message", rawMessage)
                                 .build();
        return firebaseMessaging.send(message);
    }
}
