/*
 * #%L
 * GwtMaterial
 * %%
 * Copyright (C) 2015 - 2017 GwtMaterialDesign
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package com.github.gwtmaterialdesign.client.application;

import com.github.gwtmaterialdesign.client.events.NetworkStatusEvent;
import com.github.gwtmaterialdesign.client.pwa.AppServiceWorkerManager;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.presenter.slots.NestedSlot;
import com.gwtplatform.mvp.client.proxy.Proxy;
import gwt.material.design.client.pwa.PwaManager;

public class ApplicationPresenter
        extends Presenter<ApplicationPresenter.MyView, ApplicationPresenter.MyProxy> implements NetworkStatusEvent.NetworkStatusHandler {


    interface MyView extends View, HasNetworkStatus {}

    public static final NestedSlot SLOT_MAIN = new NestedSlot();

    @ProxyStandard
    interface MyProxy extends Proxy<ApplicationPresenter> {
    }

    @Inject
    ApplicationPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);

        addRegisteredHandler(NetworkStatusEvent.TYPE, this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        initPwa();
    }

    protected void initPwa() {
        PwaManager.getInstance()
                .setServiceWorker(new AppServiceWorkerManager("service-worker.js", getEventBus()))
                .setWebManifest("manifest.json")
                .setThemeColor("#2196f3")
                .load();
    }

    @Override
    public void onNetworkStatus(NetworkStatusEvent event) {
        getView().updateUi(event.isOnline());
    }
}
