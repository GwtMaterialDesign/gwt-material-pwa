package com.github.gwtmaterialdesign.client.events;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HasHandlers;

public class ConnectionStatusUpdatedEvent extends GwtEvent<ConnectionStatusUpdatedEvent.ConnectionStatusUpdatedHandler> {

    public interface ConnectionStatusUpdatedHandler extends EventHandler {
        void onConnectionStatusUpdated(ConnectionStatusUpdatedEvent event);
    }

    private boolean online;

    public static final Type<ConnectionStatusUpdatedHandler> TYPE = new Type<>();


    public ConnectionStatusUpdatedEvent(boolean online) {
        this.online = online;
    }

    public static void fire(HasHandlers source, boolean online) {
        source.fireEvent(new ConnectionStatusUpdatedEvent(online));
    }

    @Override
    public Type<ConnectionStatusUpdatedHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(ConnectionStatusUpdatedHandler handler) {
        handler.onConnectionStatusUpdated(this);
    }

    public boolean isOnline() {
        return online;
    }
}