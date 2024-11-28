package com.spotifyxp.msnstatus;

import com.spotifyxp.PublicValues;
import com.spotifyxp.events.EventSubscriber;
import com.spotifyxp.events.Events;
import com.spotifyxp.events.SpotifyXPEvents;
import com.spotifyxp.injector.InjectorInterface;
import com.spotifyxp.msnstatus.msn.MSNStatus;

public class Initiator implements InjectorInterface {
    @Override
    public String getIdentifier() {
        return "MSNStatusSupport";
    }

    @Override
    public String getVersion() {
        return "v0.0.1";
    }

    @Override
    public String getAuthor() {
        return "Werwolf2303";
    }

    @Override
    public void init() {
        Events.subscribe(SpotifyXPEvents.onFrameReady.getName(), new EventSubscriber() {
            @Override
            public void run(Object... data) {
                PublicValues.spotifyplayer.addEventsListener(new PlayerListener());
                Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                    public void run() {
                        MSNStatus.clear();
                    }
                }, "MSNStatusSupport-ExitHook"));
            }
        });
    }
}
