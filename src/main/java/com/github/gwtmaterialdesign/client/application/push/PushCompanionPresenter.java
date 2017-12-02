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
package com.github.gwtmaterialdesign.client.application.push;

import com.github.gwtmaterialdesign.client.application.ApplicationPresenter;
import com.github.gwtmaterialdesign.client.place.NameTokens;
import com.github.gwtmaterialdesign.shared.NotificationDTO;
import com.github.gwtmaterialdesign.shared.RpcService;
import com.github.gwtmaterialdesign.shared.RpcServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import gwt.material.design.client.ui.MaterialToast;

public class PushCompanionPresenter extends Presenter<PushCompanionPresenter.MyView, PushCompanionPresenter.MyProxy>
        implements PushCompanionUiHandlers {


    private RpcServiceAsync service = GWT.create(RpcService.class);

    interface MyView extends View, HasUiHandlers<PushCompanionUiHandlers> {}

    @ProxyStandard
    @NameToken(NameTokens.PUSH_COMPANION)
    interface MyProxy extends ProxyPlace<PushCompanionPresenter> {
    }

    @Inject
    PushCompanionPresenter(
            EventBus eventBus,
            MyView view,
            MyProxy proxy) {
        super(eventBus, view, proxy, ApplicationPresenter.SLOT_MAIN);
        getView().setUiHandlers(this);
    }

    @Override
    public void push(NotificationDTO notification) {
        service.notifyAllUser(notification, new AsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable throwable) {
                MaterialToast.fireToast(throwable.getMessage());
            }

            @Override
            public void onSuccess(Void aVoid) {
                MaterialToast.fireToast("Successfully notify all the users.");
            }
        });
    }
}
