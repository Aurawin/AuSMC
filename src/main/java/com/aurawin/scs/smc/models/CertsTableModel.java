package com.aurawin.scs.smc.models;

import com.aurawin.core.stored.entities.security.Certificate;
import com.aurawin.scs.stored.Entities;
import com.aurawin.scs.stored.cloud.Service;
import com.aurawin.scs.stored.domain.Domain;
import com.aurawin.scs.stored.domain.user.Account;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

import static com.aurawin.core.stored.entities.Entities.CascadeOff;

public class CertsTableModel extends AbstractTableModel {
    public static ArrayList<Certificate> Certs = new ArrayList<>();


    private String[] columnHeadings = {"Id","Keys" ,"Request", "Status", "Level", "Expires"};
    @Override
    public int getRowCount() {
        return Certs.size();
    }
    @Override
    public int getColumnCount() {
        return columnHeadings.length;
    }
    @Override
    public String getColumnName(int col) {
        return columnHeadings[col];
    }
    @Override
    public boolean isCellEditable(int row, int col) {
        return false;
    }
    @Override
    public Object getValueAt(int row, int col) {
        Certificate cert = (Certificate) Certs.get(row);
        switch (col) {
            case 0:
                return String.valueOf(cert.getId());
            case 1:
                return Certificate.keysPresent(cert);
            case 2:
                return Certificate.requestPresent(cert);
            case 3:
                return Certificate.isIssued(cert);
            case 4:
                return String.valueOf(cert.ChainCount);
            case 5:
                return cert.Expires.toString();
            default:
                return null;
        }
    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    public void addCert(Certificate cert) {
        Certs.add(cert);
        fireTableDataChanged();
    }

    public void removeCert(Certificate cert) {
        Certs.remove(cert);
        fireTableDataChanged();
    }
    public Certificate getCertificate(int index){
        return (index!=-1)?Certs.get(index) : null;
    }
    public void loadCertificates(Domain d){
        Certs = Entities.Domains.Certificates.listAll(d);
    }
    public void setCertsList(List l) {
        Certs.clear();
        Certs.addAll(l);
        fireTableDataChanged();
    }

    public void Clear(){
        Certs.clear();
        fireTableDataChanged();
    }
    public void certChanged(Certificate cert){
        Entities.Update(cert,CascadeOff);
    }


}