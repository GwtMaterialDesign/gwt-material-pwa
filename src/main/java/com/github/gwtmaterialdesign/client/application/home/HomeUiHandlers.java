package com.github.gwtmaterialdesign.client.application.home;

import com.github.gwtmaterialdesign.client.application.AppServiceWorkerManager;
import com.gwtplatform.mvp.client.UiHandlers;

public interface HomeUiHandlers extends UiHandlers {

    AppServiceWorkerManager getServiceWorkerManager();
}
