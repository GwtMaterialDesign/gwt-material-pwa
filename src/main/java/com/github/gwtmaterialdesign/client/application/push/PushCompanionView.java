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

import com.github.gwtmaterialdesign.shared.NotificationDTO;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.ViewWithUiHandlers;
import gwt.material.design.client.ui.MaterialTextArea;
import gwt.material.design.client.ui.MaterialTextBox;

import javax.inject.Inject;

public class PushCompanionView extends ViewWithUiHandlers<PushCompanionUiHandlers> implements PushCompanionPresenter.MyView {

    interface Binder extends UiBinder<Widget, PushCompanionView> {
    }

    @UiField
    MaterialTextBox title, image;

    @UiField
    MaterialTextArea description;

    @Inject
    PushCompanionView(Binder uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @UiHandler("notifyAllUser")
    void notifyAllUser(ClickEvent e) {
        getUiHandlers().push(new NotificationDTO(title.getValue(), description.getValue(), image.getValue()));
    }
}
