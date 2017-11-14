package com.github.gwtmaterialdesign.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class NetworkStatusEvent extends GwtEvent<NetworkStatusEvent.NetworkStatusHandler> {

    public static final Type<NetworkStatusEvent.NetworkStatusHandler> TYPE = new Type<>();
    private boolean online;

    public NetworkStatusEvent(boolean online) {
        this.online = online;
    }

    public static void fire(HasHandlers source, boolean online) {
        source.fireEvent(new NetworkStatusEvent(online));
    }

    @Override
    public Type<NetworkStatusEvent.NetworkStatusHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(NetworkStatusEvent.NetworkStatusHandler handler) {
        handler.onNetworkStatus(this);
    }

    public boolean isOnline() {
        return online;
    }

    public interface NetworkStatusHandler extends EventHandler {
        void onNetworkStatus(NetworkStatusEvent event);
    }
}