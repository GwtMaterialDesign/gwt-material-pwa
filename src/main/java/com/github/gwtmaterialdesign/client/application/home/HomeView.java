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

import com.github.gwtmaterialdesign.shared.Message;
import com.github.gwtmaterialdesign.shared.RpcService;
import com.github.gwtmaterialdesign.shared.RpcServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import gwt.material.design.addins.client.overlay.MaterialOverlay;
import gwt.material.design.client.pwa.manifest.constants.DisplayMode;
import gwt.material.design.client.pwa.manifest.js.AppInstaller;
import gwt.material.design.client.ui.*;

import javax.inject.Inject;

public class HomeView extends ViewWithUiHandlers<HomeUiHandlers> implements HomePresenter.MyView {

    interface Binder extends UiBinder<Widget, HomeView> {
    }

    @UiField
    MaterialSwitch enablePushNotification;

    @UiField
    MaterialButton btnAdd;

    @UiField
    MaterialCard offlineCard;

    @UiField
    MaterialPanel onlinePanel;

    @UiField
    MaterialButton install;

    @UiField
    MaterialOverlay overlay;

    RpcServiceAsync messageService = GWT.create(RpcService.class);

    private AppInstaller appInstaller;

    @Inject
    HomeView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    protected void onAttach() {
        super.onAttach();

        appInstaller = new AppInstaller(() -> overlay.open());
        if (appInstaller.isLaunched(DisplayMode.FULLSCREEN)) {
            install.setVisible(false);
        }
    }

    @Override
    public void updateUi(boolean online) {
        btnAdd.setEnabled(online);
        onlinePanel.setVisible(online);
        offlineCard.setVisible(!online);
    }

    @UiHandler("btnAdd")
    void onAdd(ClickEvent e) {
        messageService.getMessage("Test", new AsyncCallback<Message>() {
            @Override
            public void onFailure(Throwable throwable) {
                MaterialToast.fireToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(Message message) {
                MaterialToast.fireToast("Connected to server via RPC : Response (" + message.getMessage() + ")");
            }
        });
    }

    @UiHandler("install")
    void onInstall(ClickEvent e) {
        appInstaller.prompt();
    }

    @UiHandler("gotIt")
    void onGotIt(ClickEvent e) {
        overlay.close();
    }

    @UiHandler("enablePushNotification")
    void enablePushNotification(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            getUiHandlers().getServiceWorkerManager().subscribe(() -> updateSwitch());
        } else {
            getUiHandlers().getServiceWorkerManager().unsubscribe(() -> updateSwitch());
        }
    }

    protected void updateSwitch() {
        enablePushNotification.setValue(getUiHandlers().getServiceWorkerManager().isSubscribed());
        if (getUiHandlers().getServiceWorkerManager().isSubscribed()) {
            MaterialToast.fireToast("Subscribed to Push Notification");
        } else {
            MaterialToast.fireToast("Unsubscribed to Push Notification");
        }
    }
}
