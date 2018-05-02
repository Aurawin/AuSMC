package com.aurawin.scs.smc.models;

import com.aurawin.core.solution.DBMSMode;
import com.aurawin.core.solution.Table;
import com.aurawin.core.stream.MemoryStream;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.File;
import java.nio.file.Files;

import static com.aurawin.core.solution.DBMSMode.dbmsmNone;

public class Settings {
    public Settings() {
        Encoding = Table.DBMS.Default.Encoding;
    }

    public static final int SaveDelay = 3000;
    public static final SettingsTimer Timer = new SettingsTimer();

    @Expose
    @SerializedName("u")
    private String Username;

    @Expose
    @SerializedName("pd")
    private String Password;

    @Expose
    @SerializedName("h")
    private String Hostname;

    @Expose
    @SerializedName("p")
    private int Port;

    @Expose
    @SerializedName("s")
    private String Schema;

    @Expose
    @SerializedName("e")
    private String Encoding;

    @Expose
    @SerializedName("m")
    private DBMSMode Mode;

    @Expose
    @SerializedName("c")
    private long ClusterId;

    @Expose
    @SerializedName("r")
    private long ResourceId;

    @Expose
    @SerializedName("n")
    private long NodeId;

    @Expose
    @SerializedName("csps")
    private int ClusteringSplitterSize;

    public void Load(){
        MemoryStream ms = new MemoryStream();
        try {
            File f = new File(com.aurawin.core.solution.Settings.File.Settings());
            ms.LoadFromFile(f);
            Gson gson = new GsonBuilder().create();
            Settings s = gson.fromJson(ms.toString(),Settings.class);
            Assign(s);
        } catch (Exception ex){

        } finally {
            ms.close();
        }
    }
    public void Save(){
        MemoryStream ms = new MemoryStream();
        try {
            Gson gson = new GsonBuilder().create();
            ms.Write(gson.toJson(this));
            File p = new File(com.aurawin.core.solution.Settings.Folder.Base());
            if (!p.isDirectory()) Files.createDirectories(p.toPath());
            File f = new File(com.aurawin.core.solution.Settings.File.Settings());

            ms.SaveToFile(f);
        } catch (Exception ex){

        } finally {
            ms.close();
        }
    }
    public void Assign(Settings s){
        Username=s.Username;
        Password=s.Password;
        Hostname=s.Hostname;
        Port = s.Port;
        Schema=s.Schema;
        Encoding=s.Encoding;
        Mode=s.Mode;
        ClusterId=s.ClusterId;
        ResourceId=s.ResourceId;
        ClusteringSplitterSize=s.ClusteringSplitterSize;
        NodeId=s.NodeId;
        if ((Encoding==null) || Encoding.isEmpty()) Encoding = Table.DBMS.Default.Encoding;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
        Timer.Enable();
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
        Timer.Enable();
    }

    public String getHostname() {
        return Hostname;
    }

    public void setHostname(String hostname) {
        Hostname = hostname;
        Timer.Enable();
    }


    public int getPort() {
        return Port;
    }

    public void setPort(int port) {
        Port = port;
        Timer.Enable();
    }

    public String getSchema() {
        return Schema;
    }

    public void setSchema(String schema) {
        Schema = schema;
        Timer.Enable();
    }

    public String getEncoding() {
        return Encoding;
    }

    public void setEncoding(String encoding) {
        Encoding = encoding;
        Timer.Enable();
    }

    public DBMSMode getMode() {

        return (Mode==null) ? dbmsmNone: Mode;
    }

    public void setMode(DBMSMode mode) {
        Mode = mode;
        Timer.Enable();
    }

    public long getClusterId() {
        return ClusterId;
    }

    public void setClusterId(long clusterId) {
        ClusterId = clusterId;
        Timer.Enable();
    }

    public long getResourceId() {
        return ResourceId;
    }

    public void setResourceId(long resourceId) {
        ResourceId = resourceId;
        Timer.Enable();
    }

    public long getNodeId() {
        return NodeId;
    }

    public void setNodeId(long nodeId) {
        NodeId = nodeId;
        Timer.Enable();
    }

    public void setClusteringSplitterSize(int size){
        ClusteringSplitterSize = size;
        Timer.Enable();
    }

    public int getClusteringSplitterSize(){
        return ClusteringSplitterSize;
    }

    public String getDialect(){
        return Mode.getDialect();
    }
    public String getDriver(){
        return Mode.getDriver();
    }
}
