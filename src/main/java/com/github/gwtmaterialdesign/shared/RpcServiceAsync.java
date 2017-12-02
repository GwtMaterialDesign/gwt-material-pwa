package com.github.gwtmaterialdesign.shared;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RpcServiceAsync {
    void getMessage(String input, AsyncCallback<Message> async);

    void subscribeUser(String endpoint, String auth, String key , AsyncCallback<Void> okininam);

    void reconnect(AsyncCallback<Void> async);

    void notifyAllUser(NotificationDTO notification, AsyncCallback<Void> async);
}
