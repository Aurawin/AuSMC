package com.aurawin.scs.smc.controllers;

import com.aurawin.scs.smc.models.SettingsModel;
import com.aurawin.scs.smc.models.SettingsTimerTask;

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
        timer.schedule(new SettingsTimerTask(),SettingsModel.SaveDelay);

    }
}
