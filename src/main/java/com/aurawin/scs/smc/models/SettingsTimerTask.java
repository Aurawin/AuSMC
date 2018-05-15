package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.controllers.Controller;

import java.util.TimerTask;

public class SettingsTimerTask extends TimerTask {

    @Override
    public void run(){
        try {
            Controller.Configuration.Save();
        } catch (Exception ex){

        }
    }


}
