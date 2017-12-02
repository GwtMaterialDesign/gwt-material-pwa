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
package com.github.gwtmaterialdesign.client.application.page;

import com.github.gwtmaterialdesign.client.application.ApplicationPresenter;
import com.github.gwtmaterialdesign.client.application.HasNetworkStatus;
import com.github.gwtmaterialdesign.client.events.NetworkStatusEvent;
import com.github.gwtmaterialdesign.client.place.NameTokens;
import com.github.gwtmaterialdesign.shared.RpcService;
import com.github.gwtmaterialdesign.shared.RpcServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import gwt.material.design.client.ui.MaterialLoader;
import gwt.material.design.client.ui.MaterialToast;

public class MaintenancePresenter extends Presenter<MaintenancePresenter.MyView, MaintenancePresenter.MyProxy>
        implements NetworkStatusEvent.NetworkStatusHandler, MaintenanceUiHandler {

    interface MyView extends View, HasNetworkStatus, HasUiHandlers<MaintenanceUiHandler> {}

    @Inject
    Provider<ApplicationPresenter> applicationPresenter;

    @ProxyStandard
    @NameToken(NameTokens.MAINTENANCE)
    interface MyProxy extends ProxyPlace<MaintenancePresenter> {
    }

    private RpcServiceAsync service = GWT.create(RpcService.class);

    @Inject
    MaintenancePresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, RevealType.Root);
        addRegisteredHandler(NetworkStatusEvent.TYPE, this);
        getView().setUiHandlers(this);
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        applicationPresenter.get().removeSplashScreen();
        reconnectServer();
    }

    @Override
    public void reconnectServer() {
        MaterialLoader.loading(true);
        service.reconnect(new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                MaterialToast.fireToast(throwable.getMessage());
                MaterialLoader.loading(false);
            }

            @Override
            public void onSuccess(Void aVoid) {
                String newURL = Window.Location.createUrlBuilder().setHash(NameTokens.HOME).buildString();
                Window.Location.replace(newURL);
                MaterialLoader.loading(false);
            }
        });
    }

    @Override
    protected void onBind() {
        super.onBind();
    }

    @Override
    public void onNetworkStatus(NetworkStatusEvent event) {
        getView().updateUi(event.isOnline());
    }
}
