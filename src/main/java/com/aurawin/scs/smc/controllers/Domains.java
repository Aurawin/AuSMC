package com.aurawin.scs.smc.controllers;

import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.KeyValue;

import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class Domains {
    public static Domain Domain;
    public static ArrayList<Domain> List;
    public static ArrayList<KeyValue> Keywords;

    public static void setDomain(Domain domain){
        Domain = domain;
        Keywords = Entities.Domains.KeyValues.listAll(Domain);
    }
    public static void refresh(){
        List = Entities.Domains.listAll();
        Keywords = Entities.Domains.KeyValues.listAll(Domain);
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
    public static int indexOf(Domain d){
        return List.indexOf(d);
    }
    public static void saveDomain(){
        Controller.domainView.saveView();
        Entities.Update(Domain,CascadeOff);
    }

    public static KeyValue getKeyword(String name){
        return Keywords.stream()
                .filter(k->k.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
    public static boolean keywordExists(String name){
        return (Keywords.stream()
                .filter(k->k.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null))!=null;
    }


    public static boolean Exists(String domain){
        return (List.stream()
                .filter(d->d.getName().equalsIgnoreCase(domain))
                .findFirst()
                .orElse(null)!=null);


    }

}
