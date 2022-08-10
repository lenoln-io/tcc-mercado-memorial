package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.CidadeEntity;


public class CidadeTableModel extends AbstractTableModel {

    private ArrayList<CidadeEntity> objetos;
    private String[] colunasJTable = new String[] {"Código","Cidade","Estado"};

    public CidadeTableModel(){
        objetos = new ArrayList<CidadeEntity>();
    }

    //M�todos abstratos 
    @Override
    public int getColumnCount() {
        return colunasJTable.length;
    }

    @Override
    public int getRowCount() {
        return objetos.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colunasJTable[columnIndex];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        CidadeEntity objeto = objetos.get(rowIndex);

        switch (columnIndex) {
        case 0:
            return objeto.getId();
        case 1:
            return objeto.getNome();
        case 2:
            return objeto.getEstado().getNome();		
        default:
            return null;
        }

    }

    //M�todos criados
    public void Recarregar(List<CidadeEntity> list) {
        this.objetos.clear();
        if(list != null && !list.isEmpty()){
            this.objetos.addAll(list);
        }
        fireTableDataChanged();
    }

    public CidadeEntity getObjeto(int rowIndex) {
        CidadeEntity objeto = objetos.get(rowIndex);
        return objeto;
    }
        
    private String[] nomeTabela = new String[] {"cidades","cidades","estados"};
    private String[] nomeColunaTabela = new String[] {"id","nome","nome"};
    private String[] tipoColunaTabela = new String[] {"int","string","string"};
    
    public String getTipoColunaTabela(int columnIndex) {
        return tipoColunaTabela[columnIndex];
    }

    public String getNomeColunaTabela(int columnIndex) {
        return nomeColunaTabela[columnIndex];
    }

    public String getnomeTabela(int columnIndex) {
        return nomeTabela[columnIndex];
    }

}










