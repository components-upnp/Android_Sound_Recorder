package com.example.mkostiuk.android_sound_recorder.upnp;

import org.fourthline.cling.binding.LocalServiceBindingException;
import org.fourthline.cling.binding.annotations.AnnotationLocalServiceBinder;
import org.fourthline.cling.model.DefaultServiceManager;
import org.fourthline.cling.model.ValidationException;
import org.fourthline.cling.model.meta.DeviceDetails;
import org.fourthline.cling.model.meta.DeviceIdentity;
import org.fourthline.cling.model.meta.LocalDevice;
import org.fourthline.cling.model.meta.LocalService;
import org.fourthline.cling.model.meta.ManufacturerDetails;
import org.fourthline.cling.model.meta.ModelDetails;
import org.fourthline.cling.model.types.DeviceType;
import org.fourthline.cling.model.types.UDADeviceType;
import org.fourthline.cling.model.types.UDN;

/**
 * Created by mkostiuk on 21/06/2017.
 */

public class SoundRecorderDevice {
    static LocalDevice createDevice(UDN udn)
            throws ValidationException, LocalServiceBindingException {

        DeviceType type =
                new UDADeviceType("AndroidSoundRecorder", 1);

        DeviceDetails details =
                new DeviceDetails(
                        "Android Sound Recorder",
                        new ManufacturerDetails("IRIT"),
                        new ModelDetails("AndroidController", "Enregistre un fichier audio" +
                                "puis envoie le chemin du fichier", "v1")
                );

        LocalService service =
                new AnnotationLocalServiceBinder().read(SendPathService.class);

        service.setManager(
                new DefaultServiceManager<>(service, SendPathService.class)
        );

        return new LocalDevice(
                new DeviceIdentity(udn),
                type,
                details,

                service
        );
    }
}
