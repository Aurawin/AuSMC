package com.aurawin.scs.smc.models;

import java.util.Timer;

public class SettingsTimer {
    private Timer timer;

    public SettingsTimer() {
    }
    public void Enable(){
        if (timer!=null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new SettingsTimerTask(),Settings.SaveDelay);

    }
}
