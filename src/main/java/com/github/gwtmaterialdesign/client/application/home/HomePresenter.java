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
package com.github.gwtmaterialdesign.client.application.home;

import com.github.gwtmaterialdesign.client.application.AppServiceWorkerManager;
import com.github.gwtmaterialdesign.client.application.ApplicationPresenter;
import com.github.gwtmaterialdesign.client.application.HasNetworkStatus;
import com.github.gwtmaterialdesign.client.events.NetworkStatusEvent;
import com.github.gwtmaterialdesign.client.place.NameTokens;
import com.google.gwt.core.client.GWT;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import gwt.material.design.client.pwa.PwaManager;

public class HomePresenter extends Presenter<HomePresenter.MyView, HomePresenter.MyProxy>
        implements NetworkStatusEvent.NetworkStatusHandler, HomeUiHandlers {

    interface MyView extends View, HasNetworkStatus, HasUiHandlers<HomeUiHandlers> {}

    PwaManager manager = PwaManager.getInstance();

    @ProxyStandard
    @NameToken(NameTokens.HOME)
    interface MyProxy extends ProxyPlace<HomePresenter> {
    }

    @Inject
    HomePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
        addRegisteredHandler(NetworkStatusEvent.TYPE, this);
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    public void onNetworkStatus(NetworkStatusEvent event) {
        getView().updateUi(event.isOnline());
    }

    @Override
    public AppServiceWorkerManager getServiceWorkerManager() {
        if (manager.getServiceWorkerManager() instanceof AppServiceWorkerManager) {
            return (AppServiceWorkerManager) manager.getServiceWorkerManager();
        }
        GWT.log("Push Notification Manager is not yet registered");
        return null;
    }
}
