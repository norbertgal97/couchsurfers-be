package com.norbertgal.couchsurfersbe.services;

import com.pusher.pushnotifications.PushNotifications;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Profile("dev")
@Service
public class PushNotificationServiceImpl implements PushNotificationService{

    @Override
    public void sendNotification(String title, String body) {
        String instanceId = "caf30c4a-5c02-4803-af99-4f54d74229eb";
        String secretKey = "35E9D93CEACE4418F1F1E2D0E4ABF954B2F4BD2735A7517CE8058035B660EAB3";

        PushNotifications pushNotifications = new PushNotifications(instanceId, secretKey);

        List<String> interests = Collections.singletonList("hosted");

        HashMap<String, HashMap> publishRequest = new HashMap<>();

        HashMap<String, String> apsAlert = new HashMap<>();
        apsAlert.put("title", title);
        apsAlert.put("body", body);
        HashMap<String, HashMap> alert = new HashMap<>();
        alert.put("alert", apsAlert);
        HashMap<String, HashMap> aps = new HashMap<>();
        aps.put("aps", alert);
        publishRequest.put("apns", aps);

        try {
            pushNotifications.publishToInterests(interests, publishRequest);
        } catch (IOException | InterruptedException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
