package com.github.gwtmaterialdesign.client.pwa;

import com.github.gwtmaterialdesign.client.application.ApplicationPresenter;
import com.github.gwtmaterialdesign.client.events.NetworkStatusEvent;
import com.google.web.bindery.event.shared.EventBus;
import gwt.material.design.client.pwa.serviceworker.DefaultServiceWorkerManager;
import gwt.material.design.client.ui.MaterialToast;

public class AppServiceWorkerManager extends DefaultServiceWorkerManager {

    private final EventBus eventBus;

    public AppServiceWorkerManager(String resource, EventBus eventBus) {
        super(resource);
        setPollingInterval(1000);
        this.eventBus = eventBus;
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

}
