package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.Controller;

import java.util.Timer;
import java.util.TimerTask;

import static com.aurawin.scs.smc.models.ClusterTimer.Mode.cmGroup;
import static com.aurawin.scs.smc.models.ClusterTimer.Mode.cmNode;
import static com.aurawin.scs.smc.models.ClusterTimer.Mode.cmResource;

public class ClusterTimer {
    private Timer timer;
    private Mode saveMode;
    public enum Mode{
        cmGroup, cmResource,cmNode
    }

    public ClusterTimer() {
    }

    private class Task extends TimerTask {
        @Override
        public void run(){
            try {
                switch (saveMode){
                    case cmGroup:
                        Controller.clusteringView.saveGroup();
                        break;
                    case cmResource:
                        Controller.clusteringView.saveResource();
                        break;
                    case cmNode:
                        Controller.clusteringView.saveNode();
                        break;
                }

            } catch (Exception ex){

            }
        }
    }
    public void Enable(Mode SaveMode){
        saveMode=SaveMode;
        if (timer!=null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new Task(),Settings.SaveDelay);

    }
}
