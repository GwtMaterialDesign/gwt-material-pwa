package com.github.gwtmaterialdesign.client.application;

import com.github.gwtmaterialdesign.client.events.NetworkStatusEvent;
import com.github.gwtmaterialdesign.client.place.NameTokens;
import com.github.gwtmaterialdesign.shared.RpcService;
import com.github.gwtmaterialdesign.shared.RpcServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;
import gwt.material.design.client.pwa.push.PushNotificationManager;
import gwt.material.design.client.pwa.push.helper.PushCryptoHelper;
import gwt.material.design.client.pwa.push.js.PushSubscription;
import gwt.material.design.client.pwa.serviceworker.DefaultServiceWorkerManager;
import gwt.material.design.client.pwa.serviceworker.js.ServiceWorkerRegistration;
import gwt.material.design.client.ui.MaterialToast;
import gwt.material.design.jquery.client.api.Functions;

public class AppServiceWorkerManager extends DefaultServiceWorkerManager {

    private final EventBus eventBus;
    private String endpoint, auth, key;
    private RpcServiceAsync messageService = GWT.create(RpcService.class);
    private PushNotificationManager pushNotificationManager;

    public AppServiceWorkerManager(String resource, EventBus eventBus) {
        super(resource);
        this.eventBus = eventBus;
        // Polling Interval should be every 1 minute
        /*setPollingInterval(1000);*/
    }

    @Override
    public void onRegistered(ServiceWorkerRegistration registration) {
        pushNotificationManager = new PushNotificationManager(registration);
        pushNotificationManager.load(param1 -> {
            if (param1 == null) {
                MaterialToast.fireToast("Not subscribed Push Notifications");
            } else {
                MaterialToast.fireToast("Subscribed to Push Notifications");
            }
        });
    }

    public void subscribe(Functions.Func callback) {
        pushNotificationManager.subscribe(true, "BAvr2GL1EQdLnxgQDVeZSXnsWYNSaBbIkq4DsWQXwnpGrqXoGp_7YK0CiSPvszzPnAj-D49Ne-zKDBRWHHXBL1c", subscription -> {
            if (subscription != null) {
                sendSubscriptionToServer(subscription);
                callback.call();
            }
        });
    }

    protected void sendSubscriptionToServer(PushSubscription subscription) {
        endpoint = subscription.endpoint;
        key = PushCryptoHelper.arrayBufferToBase64(subscription.getKey("p256dh"));
        auth = PushCryptoHelper.arrayBufferToBase64(subscription.getKey("auth"));
        messageService.subscribeUser(endpoint, auth, key, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                MaterialToast.fireToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                MaterialToast.fireToast("Subscribed user to Server Web Push. Ready for receiving push notifications.");
            }
        });
    }

    public void unsubscribe(Functions.Func callback) {
        pushNotificationManager.unsubscribe(() -> callback.call());
    }

    public boolean isSubscribed() {
        return pushNotificationManager.isSubscribed();
    }

    @Override
    protected void onServerFailing() {
        super.onServerFailing();
        String newURL = Window.Location.createUrlBuilder().setHash(NameTokens.MAINTENANCE).buildString();
        Window.Location.replace(newURL);
    }

    @Override
    public void onActivated() {
        super.onActivated();
    }

    @Override
    protected void onOffline() {
        super.onOffline();
        eventBus.fireEvent(new NetworkStatusEvent(false));
    }

    @Override
    protected void onOnline() {
        super.onOnline();
        eventBus.fireEvent(new NetworkStatusEvent(true));
    }

    public PushNotificationManager getPushNotificationManager() {
        return pushNotificationManager;
    }
}
