package com.example.mkostiuk.android_sound_recorder.upnp;

import org.fourthline.cling.binding.annotations.UpnpAction;
import org.fourthline.cling.binding.annotations.UpnpInputArgument;
import org.fourthline.cling.binding.annotations.UpnpService;
import org.fourthline.cling.binding.annotations.UpnpServiceId;
import org.fourthline.cling.binding.annotations.UpnpServiceType;
import org.fourthline.cling.binding.annotations.UpnpStateVariable;

import java.beans.PropertyChangeSupport;

/**
 * Created by mkostiuk on 21/06/2017.
 */

@UpnpService(
        serviceId = @UpnpServiceId("SendPathService"),
        serviceType = @UpnpServiceType(value = "SendPathService", version = 1)
)
public class SendPathService {

    private final PropertyChangeSupport propertyChangeSupport;

    public SendPathService() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public PropertyChangeSupport getPropertyChangeSupport() {
        return propertyChangeSupport;
    }

    @UpnpStateVariable(name = "Path")
    private String path = "";

    @UpnpAction(name = "SetPath")
    public void sendPath(@UpnpInputArgument(name = "Path") String p) {
        path = p;

        getPropertyChangeSupport().firePropertyChange("Path", "", path);
    }
}
