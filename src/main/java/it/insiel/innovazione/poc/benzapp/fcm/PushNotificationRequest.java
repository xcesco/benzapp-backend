package it.insiel.innovazione.poc.benzapp.fcm;

public class PushNotificationRequest {

    public PushNotificationRequest() {}

    public String getTitle() {
        return title;
    }

    public PushNotificationRequest setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public PushNotificationRequest setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getTopic() {
        return topic;
    }

    public PushNotificationRequest setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public String getToken() {
        return token;
    }

    public PushNotificationRequest setToken(String token) {
        this.token = token;
        return this;
    }

    private String title;
    private String message;
    private String topic;
    private String token;
}
