package com.aurawin.scs.smc.views;

import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.scs.smc.controllers.Controller;
import com.aurawin.scs.smc.controllers.ClusterTimer;
import com.aurawin.scs.smc.controllers.DialogCompletion;
import com.aurawin.scs.smc.controllers.OnDialogCompletion;
import com.aurawin.scs.smc.models.*;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.*;
import static com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind.svcAUDISK;

public class viewClustering {
    private static boolean Loading = true;
    private final ClusterTimer AutosaveTimer = new ClusterTimer();
    public Group Cluster;
    public Resource Resource;
    public Node Node;
    public  Service Service;
    public Location Location;
    public ArrayList<Domain>Domains;


    private JComboBox cbDomainAllocation;
    private JButton btnDomainAllocate;
    private JButton btnDomainRelease;
    public JList tvClusters;
    public JPanel mainPanel;
    private JPanel pnlNode;
    private JPanel tabNode;
    private JPanel tabResource;
    private JPanel tabCluster;
    private JTextField txtNodeAddress;
    private JLabel lblNodeStatus;
    private JPanel pnlTop;
    private JButton btnCurrent;
    private JButton btnClustering;
    private JButton btnSettings;
    private JButton btnSecurity;
    private JButton btnChange;
    private JButton btnDomain;
    private JLabel lblTitle;
    private JSplitPane spClustering;
    private JTextField txtClusterDescription;
    private JTextField txtClusterTown;
    private JTextField txtClusterCity;
    private JTextField txtClusterState;
    private JTextField txtClusterCountry;
    private JTextField txtClusterPostal;
    private JTextField txtClusterBuilding;
    private JTextField txtClusterStreet;
    private JTextField txtClusterFloor;
    private JTextField txtClusterRoom;
    private JPanel pnlKeywordTools;
    private JButton btnClusterAdd;
    private JButton btnClusterDelete;
    private JTextField txtClusterName;
    private JTextField txtClusterRack;
    private JTextField txtClusterRow;
    private JTextField txtResourceName;
    private JButton btnResourceAdd;
    private JButton btnResourceDelete;
    private JTextField txtNodeName;
    private JTabbedPane tpClustering;
    private JButton btnResourceSave;
    private JButton btnClusterSave;
    private JButton btnNodeSave;
    private JButton btnNodeNew;
    private JButton btnNodeRemove;
    private JCheckBox cbServiceEnabled;
    private JSlider sldServiceScale;
    private JTable tblServices;
    private JTextField txtMountPoint;
    private JPanel pnlMountPoint;
    private JTable tblCluster;
    private JTable tblResources;
    private JPanel tabServices;
    private JPanel pnlScroller;
    private JScrollPane spServices;
    private JTable tblNodes;
    private JLabel lblMount;
    private JLabel lblNode;
    private JLabel lblResource;
    private JLabel lblGroup;

    protected ArrayList<Group> Clusters;

    public static ClusterTableModel gTableModel;
    public static ServiceTableModel svcTableModel;
    public static ResourceTableModel rTableModel;
    public static NodeTableModel nTableModel;

    private boolean navigationExpanded;

    public viewClustering() {
        navigationExpanded=true;

        ServiceModel.init();

        svcTableModel = new ServiceTableModel(tblServices);
        gTableModel = new ClusterTableModel(tblCluster);
        rTableModel = new ResourceTableModel(tblResources);
        nTableModel = new NodeTableModel(tblNodes);

        tblServices.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int idx = tblServices.getSelectedRow();
                    if (idx!=-1) {
                        setServiceView(svcTableModel.getService(idx));
                    }

                }
            }

        });

        tblCluster.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    int idx = tblCluster.getSelectedRow();
                    if (idx!=-1) {
                        selectGroup(gTableModel.getCluster(idx),true);
                    }

                }
            }

        });

        tblResources.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int idx = tblResources.getSelectedRow();
                if (!e.getValueIsAdjusting()) {

                    if (idx!=-1) {
                        selectResource(rTableModel.getResource(idx),false);
                    }

                }
            }

        });


        tblNodes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                int idx =tblNodes.getSelectedRow();
                if (!e.getValueIsAdjusting()) {
                    if (idx!=-1) {
                        selectNode(nTableModel.getNode(idx),false);
                    }

                }
            }

        });

        sldServiceScale.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if (Loading) return;
                int idx = tblServices.getSelectedRow();
                if (idx!=-1) {
                    Service svc = svcTableModel.getService(idx);
                    svc.setScaleStart(sldServiceScale.getValue());
                    svcTableModel.serviceChanged(svc);
                    tblServices.repaint();
                }
            }
        });
        cbServiceEnabled.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Loading) return;
                int idx =tblServices.getSelectedRow();
                if (idx!=-1) {
                    Service svc = svcTableModel.getService(idx);
                    if (svc != null) {
                        svc.setEnabled(cbServiceEnabled.isSelected());
                        svcTableModel.serviceChanged(svc);
                        tblServices.repaint();

                    }
                }
            }
        });

        Cluster=null;
        tpClustering.setEnabledAt(0,true);
        tpClustering.setEnabledAt(1,false);
        tpClustering.setEnabledAt(2,false);
        tpClustering.setEnabledAt(3, false);


        btnClusterDelete.setEnabled(true);
        btnClusterSave.setEnabled(true);


        btnDomain.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapDomainView();
            }
        });

        txtClusterName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                String sName = txtClusterName.getText();
                boolean enName = ( (sName!=null) && (sName.length()>0));
                if (enName) {
                    boolean enNotExist = Entities.Lookup(Group.class, sName)==null;
                    btnClusterAdd.setEnabled(enNotExist);
                } else {
                    btnClusterAdd.setEnabled(false);
                }

            }
        });


        btnClusterAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Cluster = new Group();
                Location=new Location();

                Location.setLocality(txtClusterTown.getText());
                Location.setArea(txtClusterCity.getText());
                Location.setRegion(txtClusterState.getText());
                Location.setCountry(txtClusterCountry.getText());
                Location.setZip(txtClusterPostal.getText());
                Location.setBuilding(txtClusterBuilding.getText());
                Location.setStreet(txtClusterStreet.getText());
                Location.setFloor(txtClusterFloor.getText());
                Location.setRoom(txtClusterRoom.getText());

                Entities.Save(Location,CascadeOn);
                Cluster.setName(txtClusterName.getText());
                Cluster.setDescription(txtClusterDescription.getText());
                Cluster.setRack(txtClusterRack.getText());
                Cluster.setRow(txtClusterRow.getText());

                Cluster.setLocation(Location);
                Entities.Save(Cluster,CascadeOn);

                gTableModel.addCluster(Cluster);

                Cluster = null;  // so we can switch clusters by hand.
            }
        });
        txtClusterName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);

            }
        });
        txtClusterRack.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterRow.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterDescription.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterTown.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterCity.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtClusterState.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterCountry.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterPostal.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterBuilding.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterStreet.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterFloor.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtClusterRack.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtClusterRoom.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtResourceName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                String sName = txtResourceName.getText();
                boolean enName = ( (Cluster!=null) && (sName!=null) && (sName.length()>0));
                btnResourceDelete.setEnabled(Resource!=null);
                if (enName) {
                    boolean enNotExist = !Entities.Cloud.Resource.Exists(Cluster,sName);
                    btnResourceAdd.setEnabled(enNotExist);
                } else {
                    btnResourceAdd.setEnabled(false);
                }

            }
        });

        btnResourceDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Resource!=null){
                    Controller.dialogView.showDialog(
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){
                                     if (Data.Confirmed) {
                                         Entities.Delete(Resource, CascadeOn,UseNewTransaction);
                                         gTableModel.refreshView();
                                     }
                                }
                            },
                        DialogMode.dmConfirmation,
                        Controller.Lang.Dialog.getString("title.cluster.resource.confirm.delete"),
                        Controller.Lang.Dialog.getString("message.cluster.resource.confirm.delete")
                    );

                }

            }
        });
        btnResourceAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Resource = new Resource();
                Resource.setName(txtResourceName.getText());
                Resource.setGroup(Cluster);

                Entities.Save(Resource,CascadeOff);

                rTableModel.addResource(Resource);

                rTableModel.refreshView(Cluster);
            }
        });
        btnClusterSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveGroup();
            }
        });
        btnResourceSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveResource();
            }
        });
        txtClusterName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Cluster!=null){
                    saveGroup();
                }
            }
        });
        txtResourceName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Resource!=null){
                    saveResource();
                }
            }
        });
        btnClusterDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Cluster!=null){
                    Controller.dialogView.showDialog(
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){
                                    if (Data.Confirmed) {
                                        gTableModel.deleteCluster(Cluster);
                                        Entities.Delete(Cluster,CascadeOn,UseNewTransaction);
                                        clearView();

                                    }
                                }
                            },
                            DialogMode.dmConfirmation,
                            Controller.Lang.Dialog.getString("title.cluster.confirm.delete"),
                            Controller.Lang.Dialog.getString("message.cluster.confirm.delete")
                    );
                }
            }
        });

        txtNodeName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                String sName = txtNodeName.getText();
                boolean enName = ((sName!=null) && (sName.length()>0));
                btnNodeRemove.setEnabled(Node!=null);
                btnNodeSave.setEnabled(Node!=null);
                if (enName) {
                    boolean enNotExist = !Entities.Cloud.Node.Exists(Resource,sName);
                    btnNodeNew.setEnabled(enNotExist);
                } else {
                    btnNodeNew.setEnabled(false);
                }

            }
        });
        txtNodeAddress.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                String sName = txtNodeName.getText();
                String sAddr = txtNodeAddress.getText();
                boolean enAddr = ((sAddr!=null) && (sAddr.length()>0));
                boolean enName = ((sName!=null) && (sName.length()>0));
                btnNodeRemove.setEnabled(Node!=null);
                btnNodeSave.setEnabled(Node!=null);
                if ((enAddr) && (enName)) {
                    boolean enNotExist = !Entities.Cloud.Node.Exists(Resource,sName);
                    btnNodeNew.setEnabled(enNotExist);
                } else {
                    btnNodeNew.setEnabled(false);
                }

            }
        });
        txtMountPoint.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                if (Loading) return;
                if (Service!=null) {
                    Service.setMountPoint(txtMountPoint.getText());
                    Disk d = Entities.Cloud.Disk.byService(Service);
                    if (d!=null){
                        d.setMount(Service.getMountPoint());
                        Entities.Update(d,CascadeOn);
                    } else {
                        d = new Disk();

                        d.setMount(Service.getMountPoint());
                        d.setServiceId(Service.getId());
                        d.setOwnerId(Service.getOwner().getId());

                        Entities.Save(d,CascadeOn);
                    }
                    svcTableModel.serviceChanged(Service);

                    if (
                            (Controller.Cloud.Cluster!=null) &&
                            (Controller.Cloud.Resource!=null) &&
                            (Controller.Cloud.Node!=null) &&
                            (Controller.Cloud.Node.getId()==Service.getOwner().getId())

                            ){
                        Controller.mountPointChanged(Service);
                    }

                }

            }
        });
        btnNodeRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Node!=null){
                    Controller.dialogView.showDialog(
                            new OnDialogCompletion(){
                                @Override
                                public void Complete (DialogCompletion Data){
                                    if (Data.Confirmed) {
                                        Entities.Delete(Node,CascadeOn,UseNewTransaction);
                                        gTableModel.refreshView();
                                    }
                                }
                            },
                            DialogMode.dmConfirmation,
                            Controller.Lang.Dialog.getString("title.cluster.node.confirm.delete"),
                            Controller.Lang.Dialog.getString("message.cluster.node.confirm.delete")
                    );
                }
            }
        });
        btnNodeSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveNode();
            }
        });
        btnNodeNew.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createNode();
            }
        });
        txtNodeName.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Node!=null){
                    saveNode();
                }
            }
        });
        txtNodeAddress.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Node!=null){
                    saveNode();
                }
            }
        });
        spClustering.addPropertyChangeListener(new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (Loading) return;
                JSplitPane sp = (JSplitPane) evt.getSource();
                String propertyName = evt.getPropertyName();
                if (propertyName.equals(JSplitPane.DIVIDER_LOCATION_PROPERTY)) {
                    Controller.Configuration.setClusteringSplitterSize(sp.getDividerLocation());
                }
            }
        });


        btnDomainAllocate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int idx = cbDomainAllocation.getSelectedIndex();
                if ((idx!=-1) && (Node!=null)) {
                    Domain d = (Domain) cbDomainAllocation.getItemAt(idx);
                    Node.setDomain(d);
                    saveNode();
                }
            }
        });
        btnChange.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                navigationExpanded = !navigationExpanded;
                btnClustering.setVisible(navigationExpanded);
                btnSecurity.setVisible(navigationExpanded);
                btnDomain.setVisible(navigationExpanded);
                btnSettings.setVisible(navigationExpanded);
                try {
                    btnChange.setIcon(new ImageIcon(ImageIO.read(getClass().getClassLoader().getResourceAsStream((navigationExpanded) ? "icons/arrowhead_left.png" : "icons/arrowhead_right.png"))));
                } catch (Exception ex){

                }
            }
        });
        btnSettings.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapSettingsView();
            }
        });
        btnSecurity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Controller.swapSecurityView();
            }
        });
    }
    public void clearView(){
        Loading = true;
        try{
            txtClusterName.setText("");
            txtClusterDescription.setText("");
            txtClusterTown.setText("");
            txtClusterCity.setText("");
            txtClusterState.setText("");
            txtClusterCountry.setText("");
            txtClusterPostal.setText("");
            txtClusterBuilding.setText("");
            txtClusterStreet.setText("");
            txtClusterFloor.setText("");
            txtClusterRoom.setText("");
            txtClusterRack.setText("");
            txtClusterRow.setText("");
            tblCluster.getSelectionModel().clearSelection();
            gTableModel.Clear();
            Cluster=null;


            txtResourceName.setText("");
            tblResources.getSelectionModel().clearSelection();
            rTableModel.Clear();
            Resource=null;

            txtNodeName.setText("");
            txtNodeAddress.setText("");
            cbDomainAllocation.setSelectedIndex(-1);
            nTableModel.Clear();
            tblNodes.getSelectionModel().clearSelection();
            Node=null;

            txtMountPoint.setText("");
            lblNodeStatus.setText("");
            svcTableModel.Clear();
            tblServices.getSelectionModel().clearSelection();

            Service = null;

            tpClustering.setSelectedIndex(0);
            tpClustering.setEnabledAt(0,true);
            tpClustering.setEnabledAt(1,false);
            tpClustering.setEnabledAt(2,false);
            tpClustering.setEnabledAt(3,false);


        }finally{
            Loading=false;
        }
    }

    public void selectGroup(Group group, boolean switchTab){
        if ( (Cluster!=null) &&(Cluster.getId()==group.getId())) return;
        Loading=true;
        try {
            rTableModel.refreshView(group);

            resetNodeView();
            resetResourceView();
            resetServiceView();
            tpClustering.setEnabledAt(0, true);
            tpClustering.setEnabledAt(1, true);
            tpClustering.setEnabledAt(2, false);
            tpClustering.setEnabledAt(3, false);

            btnResourceDelete.setEnabled(false);

            Cluster = group;
            txtClusterName.setText(Cluster.getName());
            txtClusterRack.setText(Cluster.getRack());
            txtClusterRow.setText(Cluster.getRow());
            txtClusterDescription.setText(Cluster.getDescription());
            txtClusterTown.setText(Cluster.getLocation().getLocality());
            txtClusterCity.setText(Cluster.getLocation().getArea());
            txtClusterState.setText(Cluster.getLocation().getRegion());
            txtClusterCountry.setText(Cluster.getLocation().getCountry());
            txtClusterPostal.setText(Cluster.getLocation().getZip());
            txtClusterBuilding.setText(Cluster.getLocation().getBuilding());
            txtClusterStreet.setText(Cluster.getLocation().getStreet());
            txtClusterFloor.setText(Cluster.getLocation().getFloor());
            txtClusterRoom.setText(Cluster.getLocation().getRoom());

            btnClusterDelete.setEnabled(true);

            rTableModel.refreshView(group);

            if (switchTab) tpClustering.setSelectedIndex(0);
        } finally{
            Loading=false;
        }

    }
    public void selectResource(Resource resource, boolean switchTab){
        if ((Resource!=null) && Resource.getId()==resource.getId()) return;

        //selectGroup(Resource.getGroup(), false);

        tpClustering.setEnabledAt(0, true);
        tpClustering.setEnabledAt(1, true);
        tpClustering.setEnabledAt(2, true);
        tpClustering.setEnabledAt(3, false);

        if (switchTab) tpClustering.setSelectedIndex(1);

        btnResourceDelete.setEnabled(true);
        btnResourceSave.setEnabled(true);

        txtResourceName.setText(resource.getName());
        rTableModel.refreshView(resource.getGroup());

        nTableModel.refreshView(resource);

        Resource = resource;


        selectNode(null,false);
    }
    public void selectNode(Node node, boolean switchTab){

        if (node!=null) {
            resetServiceView();
            selectGroup(node.getGroup(),false);
            selectResource(node.getResource(),false);


            tpClustering.setEnabledAt(0,true);
            tpClustering.setEnabledAt(1,true);
            tpClustering.setEnabledAt(2,true);
            tpClustering.setEnabledAt(3, true);

            if (switchTab) tpClustering.setSelectedIndex(2);

            btnNodeNew.setEnabled(true);
            btnNodeRemove.setEnabled(false);
            btnNodeSave.setEnabled(false);

            int idx = Domains.indexOf(node.getDomain());
            cbDomainAllocation.setSelectedIndex(idx);
            if (idx!=-1){
                btnDomainRelease.setEnabled(true);
                btnDomainAllocate.setEnabled(false);
            } else {
                btnDomainAllocate.setEnabled(true);
                btnDomainRelease.setEnabled(false);
            }

            txtNodeName.setText(node.getName());
            txtNodeAddress.setText(com.aurawin.core.rsr.IpHelper.fromLong(node.getIP()));
            svcTableModel.refreshView(node);

            Node = node;

        } else {
            btnNodeNew.setEnabled(true);
            txtNodeName.setText("");
            txtNodeAddress.setText("");
            lblNodeStatus.setText("");
            cbDomainAllocation.setSelectedIndex(-1);
        }


    }
    public void updateStatusBar(){
        long iValue;
        String sMount;
        iValue =  (Controller.Cloud.Node==null) ? 0 : Controller.Cloud.Node.getId();
        lblNode.setText(String.valueOf(iValue));
        iValue =  (Controller.Cloud.Resource==null) ? 0 : Controller.Cloud.Resource.getId();
        lblResource.setText(String.valueOf(iValue));
        iValue =  (Controller.Cloud.Cluster==null) ? 0 : Controller.Cloud.Cluster.getId();
        lblGroup.setText(String.valueOf(iValue));
        sMount = (Controller.Cloud.Disk==null) ? "" : Controller.Cloud.Disk.getMount();
        sMount = (sMount.length()==0) ? Controller.Lang.Clustering.getString("label.clustering.disk.mount.none") : sMount;
        lblMount.setText(sMount);
    }
    public void refreshView(){
        Loading = true;
        resetServiceView();
        resetResourceView();
        resetNodeView();
        Domains = Entities.Domains.listAll();

        cbDomainAllocation.removeAllItems();
        //svcTableModel.Clear();
        for (Domain d : Domains){
            cbDomainAllocation.addItem(d);
        }

        gTableModel.refreshView();


        int size = Controller.Configuration.getClusteringSplitterSize();
        if (size <120) size = 120;
        spClustering.setDividerLocation(size);
        Loading = false;
    }
    public void saveGroup(){
        if (Cluster!=null) {
            Cluster.setName(txtClusterName.getText());
            Cluster.setRack(txtClusterRack.getText());
            Cluster.setRow(txtClusterRow.getText());
            Cluster.setDescription(txtClusterDescription.getText());
            Cluster.getLocation().setLocality(txtClusterTown.getText());
            Cluster.getLocation().setArea(txtClusterCity.getText());
            Cluster.getLocation().setRegion(txtClusterState.getText());
            Cluster.getLocation().setCountry(txtClusterCountry.getText());
            Cluster.getLocation().setZip(txtClusterPostal.getText());
            Cluster.getLocation().setBuilding(txtClusterBuilding.getText());
            Cluster.getLocation().setStreet(txtClusterStreet.getText());
            Cluster.getLocation().setFloor(txtClusterFloor.getText());
            Cluster.getLocation().setRoom(txtClusterRoom.getText());

            Entities.Update(Cluster,CascadeOn);

            //gTableModel.refreshView();
        }
    }
    public void setServiceView(Service svc){
        Loading=true;
        try {
            Service = svc;
            cbServiceEnabled.setEnabled(true);
            sldServiceScale.setEnabled(true);
            cbServiceEnabled.setSelected(svc.getEnabled());
            sldServiceScale.setValue(svc.getScaleStart());
            pnlMountPoint.setVisible(svc.getKind()==svcAUDISK);
            txtMountPoint.setText(svc.getMountPoint());
            if (svc.getKind()==svcAUDISK){
                Node n = svc.getOwner();
                Disk d = Entities.Cloud.Disk.byService(svc);
                if (svc.getEnabled()) {
                    if (d == null) {
                        d = new Disk();
                        d.setOwnerId(n.getId());
                        d.setMount(svc.getMountPoint());
                        d.setServiceId(svc.getId());
                        Entities.Save(d, CascadeOn);

                    }
                }
            }

        } finally {
            Loading=false;
        }
    }
    public void resetNodeView(){
        txtNodeName.setText("");
        txtNodeAddress.setText("");

    }
    public void resetResourceView(){

        txtResourceName.setText("");

    }
    public void resetServiceView(){
        svcTableModel.Clear();
        Service = null;
        cbServiceEnabled.setSelected(false);
        cbServiceEnabled.setEnabled(false);
        sldServiceScale.setEnabled(false);
        sldServiceScale.setValue(0);
        txtMountPoint.setText("");
        pnlMountPoint.setVisible(false);
    }
    public void saveResource(){
        if (Resource!=null){
            Resource.setName(txtResourceName.getText());
            btnResourceAdd.setEnabled(false);
            rTableModel.ResourceChanged(Resource);
        }
    }
    public void createNode(){
        Node= new Node();
        Node.setResource(Resource);
        Node.setName(txtNodeName.getText());
        Node.setIP(com.aurawin.core.rsr.IpHelper.toLong(txtNodeAddress.getText()));
        Entities.Save(Node,CascadeOn);
        nTableModel.addNode(Node);
        svcTableModel.refreshView(Node);


    }
    public void saveNode(){
        if (Node!=null){
            Node.setName(txtNodeName.getText());
            Node.setIP(com.aurawin.core.rsr.IpHelper.toLong(txtNodeAddress.getText()));
            Entities.Update(Node,CascadeOn);
        }
    }
}
