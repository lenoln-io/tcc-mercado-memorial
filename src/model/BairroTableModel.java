package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.BairroEntity;


public class BairroTableModel extends AbstractTableModel {

    private ArrayList<BairroEntity> objetos;
    private final String[] colunasJTable = new String[] {"Código","Bairro","Cidade","Estado"};

    public BairroTableModel(){
        objetos = new ArrayList<>();
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

        BairroEntity objeto = objetos.get(rowIndex);

        switch (columnIndex) {
        case 0:
            return objeto.getId();
        case 1:
            return objeto.getNome();
        case 2:
            return objeto.getCidade().getNome();
        case 3:
            return objeto.getCidade().getEstado().getNome();
        default:
            return null;
        }

    }
	
    //M�todos criados
    public void Recarregar(List<BairroEntity> list) {
        this.objetos.clear();
        if(list != null && !list.isEmpty()){
            this.objetos.addAll(list);
        }
        fireTableDataChanged();
    }

    public BairroEntity getObjeto(int rowIndex) {
        BairroEntity objeto = objetos.get(rowIndex);
        return objeto;
    }
    
    private final String[] nomeTabela = new String[] {"bairros","bairros","cidades","estados"};
    private final String[] nomeColunaTabela = new String[] {"id","nome","nome","nome"};
    private final String[] tipoColunaTabela = new String[] {"int","string","string","string"};
    
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










