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

import com.github.gwtmaterialdesign.client.pwa.AppServiceWorkerManager;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewImpl;
import gwt.material.design.client.constants.Color;
import gwt.material.design.client.pwa.PwaManager;
import gwt.material.design.client.ui.*;

import javax.inject.Inject;

public class ApplicationView extends ViewImpl implements ApplicationPresenter.MyView {

    interface Binder extends UiBinder<Widget, ApplicationView> {
    }

    @UiField
    MaterialContainer container;

    @UiField
    MaterialButton btnAdd;

    @UiField
    MaterialCard offlineCard;

    @UiField
    MaterialPanel onlinePanel;

    @UiField
    MaterialNavBar navBar;

    @Inject
    ApplicationView(
            Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        setInSlot(ApplicationPresenter.SLOT_MAIN, container);

        AppServiceWorkerManager serviceWorkerManager = new AppServiceWorkerManager("service-worker.js");
        serviceWorkerManager.addConnectionStatusUpdateHandler(event -> {
            btnAdd.setEnabled(event.isOnline());
            onlinePanel.setVisible(event.isOnline());
            offlineCard.setVisible(!event.isOnline());

            if (event.isOnline()) {
                navBar.setBackgroundColor(Color.INDIGO);
            } else {
                navBar.setBackgroundColor(Color.GREY);
            }
        });

        PwaManager.getInstance()
                .setServiceWorker(serviceWorkerManager)
                .setWebManifest("manifest.json")
                .setThemeColor("#2196f3")
                .load();
    }


    @Override
    protected void onAttach() {
        super.onAttach();

        Document.get().getElementById("splashscreen").removeFromParent();
    }

    @UiHandler("btnAdd")
    void onAdd(ClickEvent e) {
        MaterialToast.fireToast("I love GaMD");
    }

    @Override
    public void updateConnectionStatus(boolean online) {
        btnAdd.setEnabled(online);
    }
}
