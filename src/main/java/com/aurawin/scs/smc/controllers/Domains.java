package com.aurawin.scs.smc.controllers;

import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.Domain;

import javax.naming.ldap.Control;
import java.util.ArrayList;
import java.util.Set;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class Domains {
    public static Domain Domain;
    public static ArrayList<Domain> List;

    public static void setDomain(Domain domain){
        Domain = domain;
    }
    public static void refresh(){
        List = Entities.Domains.listAll();
        Controller.domainView.refreshView();

    }
    public static void loggedIn(){
        Domains.refresh();
    }

    public static void resetView(){
        Domains.refresh();
        Controller.domainView.resetView();
    }

    public static void loadView(Domain d){
        setDomain(d);
        Controller.domainView.loadView(d);
    }

    public static void saveDomain(){
        Controller.domainView.saveView();
        Entities.Update(Domain,CascadeOff);
    }

}
