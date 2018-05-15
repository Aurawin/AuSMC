package com.aurawin.scs.smc.models;

import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Node;
import com.aurawin.scs.stored.cloud.Service;

import java.util.ArrayList;
import java.util.HashMap;

public class ServiceModel {
    public static Service Service;
    public static Node Node;
    public static ArrayList<Service> List;
    public static final HashMap<Boolean,String> Enabled = new HashMap<>();

    public void reloadServices(){
        List = Entities.Cloud.Service.listAll(Node);

    }
    public void setService(Service service){
        Service = service;

    }

    public static void init() {
        Enabled.put(true, Controller.Lang.Domain.getString("label.domain.enabled"));
        Enabled.put(false, Controller.Lang.Domain.getString("label.domain.disabled"));
    }
}
