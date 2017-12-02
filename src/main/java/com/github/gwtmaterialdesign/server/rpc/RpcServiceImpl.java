package com.github.gwtmaterialdesign.server.rpc;

import com.github.gwtmaterialdesign.server.push.Subscription;
import com.github.gwtmaterialdesign.server.push.WebPushAPI;
import com.github.gwtmaterialdesign.shared.Message;
import com.github.gwtmaterialdesign.shared.NotificationDTO;
import com.github.gwtmaterialdesign.shared.RpcService;
import com.google.gson.JsonObject;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.jose4j.lang.JoseException;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class RpcServiceImpl extends RemoteServiceServlet implements RpcService {

    static List<Subscription> subscriptions = new ArrayList<>();

    @Override
    public Message getMessage(String input) {
        return new Message("Hello World");
    }

    @Override
    public void reconnect() {}

    @Override
    public void subscribeUser(String endpoint, String auth, String key) {
        subscriptions.add(new Subscription(endpoint, auth, key));
    }

    @Override
    public void notifyAllUser(NotificationDTO notification) {
        WebPushAPI webPushAPI = new WebPushAPI();
        try {
            for (Subscription subscription : subscriptions) {
                webPushAPI.sendPushMessage(subscription, generatePayload(notification));
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (JoseException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected byte[] generatePayload(NotificationDTO notification) {
        JsonObject object = new JsonObject();
        object.addProperty("title", notification.getTitle());
        object.addProperty("description", notification.getDescription());
        object.addProperty("image", notification.getImage());
        return object.toString().getBytes();
    }
}
