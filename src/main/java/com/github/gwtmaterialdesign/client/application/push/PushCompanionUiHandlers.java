package com.github.gwtmaterialdesign.client.application.push;

import com.github.gwtmaterialdesign.shared.NotificationDTO;
import com.gwtplatform.mvp.client.UiHandlers;

public interface PushCompanionUiHandlers extends UiHandlers {

    void push(NotificationDTO notification);
}
