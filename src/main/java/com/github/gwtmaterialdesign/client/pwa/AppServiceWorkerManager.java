package com.github.gwtmaterialdesign.client.pwa;

import gwt.material.design.client.pwa.serviceworker.DefaultServiceWorkerManager;

public class AppServiceWorkerManager extends DefaultServiceWorkerManager {

    public AppServiceWorkerManager(String resource) {
        super(resource);
    }

    @Override
    protected void onOffline() {
        super.onOffline();
    }

    @Override
    protected void onOnline() {
        super.onOnline();
    }
}
