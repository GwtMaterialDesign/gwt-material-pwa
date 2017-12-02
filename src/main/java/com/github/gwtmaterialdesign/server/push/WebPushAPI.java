package com.github.gwtmaterialdesign.server.push;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import nl.martijndwars.webpush.Utils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.util.concurrent.ExecutionException;
import java.util.logging.Logger;

public class WebPushAPI {

    static Logger logger = Logger.getLogger(WebPushAPI.class.getSimpleName());

    public void sendPushMessage(Subscription sub, byte[] payload) throws GeneralSecurityException, InterruptedException, JoseException, ExecutionException, IOException {
        // Create a notification with the endpoint, userPublicKey from the subscription and a custom payload
        Notification notification = new Notification(sub.getEndpoint(), sub.getKey(), sub.getAuth(), payload);
        PushService pushService = new PushService();
        pushService.setSubject(PushConfig.SUBJECT);
        pushService.setPublicKey(Utils.loadPublicKey(PushConfig.ENCODED_PUBLIC_KEY));
        pushService.setPrivateKey(Utils.loadPrivateKey(PushConfig.ENCODED_PRIVATE_KEY));
        HttpResponse httpResponse = pushService.send(notification);

        logger.info(String.valueOf(httpResponse.getStatusLine().getStatusCode()));
        logger.info(IOUtils.toString(httpResponse.getEntity().getContent(), StandardCharsets.UTF_8));
    }
}
