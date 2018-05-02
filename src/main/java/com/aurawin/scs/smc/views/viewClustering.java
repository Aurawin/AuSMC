package com.aurawin.scs.smc.views;

import com.aurawin.core.gui.JTextFieldListener;
import com.aurawin.scs.smc.Controller;
import com.aurawin.scs.smc.JTableHelper;
import com.aurawin.scs.smc.models.ClusterTimer;
import com.aurawin.scs.smc.models.ClusterTreeModel;
import com.aurawin.scs.smc.models.ServiceModel;
import com.aurawin.scs.smc.models.ServiceTableModel;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.*;
import com.aurawin.scs.stored.domain.Domain;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;
import static com.aurawin.core.stored.entities.Entities.CascadeOn;
import static com.aurawin.scs.solution.Table.Stored.Cloud.Service.Kind.svcAUDISK;

public class viewClustering {
    private static boolean Loading = true;
    private final ClusterTimer AutosaveTimer = new ClusterTimer();
    private Group Cluster;
    private Resource Resource;
    private Node Node;
    private Service Service;
    private Location Location;
    private ArrayList<Domain>Domains;


    private JComboBox cbDomainAllocation;
    private JButton btnDomainAllocate;
    private JButton btnDomainRelease;
    public JTree tvClusters;
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
    private JPanel pnlScroller;
    private JScrollPane spServices;
    private JTextField txtMountPoint;
    private JPanel pnlMountPoint;

    protected ArrayList<Group> Clusters;

    public static ClusterTreeModel tvClusterModel;
    public static ServiceTableModel svcTableModel;

    private DefaultTableCellRenderer svcTableRenderer;

    public viewClustering() {

        ServiceModel.init();

        svcTableRenderer = new DefaultTableCellRenderer();
        svcTableModel = new ServiceTableModel();

        tblServices.setModel(svcTableModel);
        tblServices.setRowSelectionAllowed(true);
        tblServices.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JTableHelper.setColumnWidth(tblServices,svcTableRenderer,0,75);
        JTableHelper.setColumnWidth(tblServices,svcTableRenderer,1,75);
        JTableHelper.setColumnWidth(tblServices,svcTableRenderer,2,55);
        JTableHelper.setColumnWidth(tblServices,svcTableRenderer,3,140);

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
        //JTableHelper.setColumnAlignment(tblServices,svcTableRenderer,JLabel.Left);


        Cluster=null;
        tpClustering.setEnabledAt(0,true);
        tpClustering.setEnabledAt(1,false);
        tpClustering.setEnabledAt(2,false);
        tvClusterModel = new ClusterTreeModel(tvClusters);
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

                refreshView();


            }
        });
        txtClusterName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterRack.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterRow.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterDescription.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterTown.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterCity.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtClusterState.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterCountry.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterPostal.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterBuilding.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterStreet.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });
        txtClusterFloor.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtClusterRack.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtClusterRoom.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
                AutosaveTimer.Enable(ClusterTimer.Mode.cmGroup);
            }
        });

        txtResourceName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
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
                    if (Controller.dialogView.showDialog(
                            DialogMode.dmConfirmation,
                            Controller.Lang.Dialog.getString("title.cluster.resource.confirm.delete"),
                            Controller.Lang.Dialog.getString("message.cluster.resource.confirm.delete")
                    ) && Controller.dialogView.Confirmed){
                        Entities.Delete(Resource,CascadeOn);
                        tvClusterModel.reload();
                    }
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

                tvClusterModel.refreshView();
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
                    if (Controller.dialogView.showDialog(
                            DialogMode.dmConfirmation,
                            Controller.Lang.Dialog.getString("title.cluster.confirm.delete"),
                            Controller.Lang.Dialog.getString("message.cluster.confirm.delete")
                    ) && Controller.dialogView.Confirmed){
                        Entities.Delete(Cluster,CascadeOn);
                        tvClusterModel.reload();
                    }
                }
            }
        });

        txtNodeName.getDocument().addDocumentListener(new JTextFieldListener() {
            @Override
            public void update(DocumentEvent e) {
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
                if (Service!=null) {
                    Service.setMountPoint(txtMountPoint.getText());
                    svcTableModel.serviceChanged(Service);
                }

            }
        });
        btnNodeRemove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Node!=null){
                    if (Controller.dialogView.showDialog(
                            DialogMode.dmConfirmation,
                            Controller.Lang.Dialog.getString("title.cluster.node.confirm.delete"),
                            Controller.Lang.Dialog.getString("message.cluster.node.confirm.delete")
                    ) && Controller.dialogView.Confirmed){
                        Entities.Delete(Node,CascadeOn);
                        tvClusterModel.reload();
                    }
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
    }


    public void selectGroup(Group group, boolean switchTab){
        resetServiceView();
        tpClustering.setEnabledAt(0,true);
        tpClustering.setEnabledAt(1,true);
        tpClustering.setEnabledAt(2,false);

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
        if (switchTab) tpClustering.setSelectedIndex(0);

    }
    public void selectResource(Resource resource, boolean switchTab){

        Resource = resource;
        if (resource!=null) {
            selectGroup(Resource.getGroup(), false);

            tpClustering.setEnabledAt(0, true);
            tpClustering.setEnabledAt(1, true);
            tpClustering.setEnabledAt(2, true);

            if (switchTab) tpClustering.setSelectedIndex(1);

            btnResourceDelete.setEnabled(true);
            btnResourceSave.setEnabled(true);


            txtResourceName.setText(Resource.getName());
            Resource=resource;
        }
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


            svcTableModel.load(node);

            Node = node;

        } else {
            btnNodeNew.setEnabled(true);
            txtNodeName.setText("");
            txtNodeAddress.setText("");
            lblNodeStatus.setText("");
            cbDomainAllocation.setSelectedIndex(-1);
        }


    }
    public void refreshView(){
        Loading = true;
        resetServiceView();
        Domains = Entities.Domains.listAll();
        cbDomainAllocation.removeAllItems();
        //svcTableModel.Clear();
        for (Domain d : Domains){
            cbDomainAllocation.addItem(d);
        }

        tvClusterModel.refreshView();
        tvClusters.setModel(tvClusterModel);
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

            tvClusterModel.reload();
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
        } finally {
            Loading=false;
        }
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
            Entities.Update(Resource,CascadeOn);
            tvClusterModel.reload();
        }
    }
    public void createNode(){
        Node= new Node();
        Node.setResource(Resource);
        Node.setName(txtNodeName.getText());
        Node.setIP(com.aurawin.core.rsr.IpHelper.toLong(txtNodeAddress.getText()));
        Entities.Save(Node,CascadeOn);
        Resource.Nodes.add(Node);
        tvClusterModel.reload();


    }
    public void saveNode(){
        if (Node!=null){
            Node.setName(txtNodeName.getText());
            Node.setIP(com.aurawin.core.rsr.IpHelper.toLong(txtNodeAddress.getText()));
            Entities.Update(Node,CascadeOn);
            tvClusterModel.reload();
        }
    }
}
