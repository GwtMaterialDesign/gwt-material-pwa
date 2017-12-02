package com.github.gwtmaterialdesign.shared;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("message")
public interface RpcService extends RemoteService {

    Message getMessage(String input);

    void reconnect();

    void subscribeUser(String endpoint, String auth, String key);

    void notifyAllUser(NotificationDTO notification);
}
