package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.CategoriaEntity;


public class CategoriaTableModel extends AbstractTableModel {

    private ArrayList<CategoriaEntity> objetos;
    private String[] colunasJTable = new String[] {"Código","Categoria","Descrição"};

    public CategoriaTableModel(){
        objetos = new ArrayList<CategoriaEntity>();
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

        CategoriaEntity objeto = objetos.get(rowIndex);

        switch (columnIndex) {
        case 0:
            return objeto.getId();
        case 1:
            return objeto.getNome();
        case 2:
            return objeto.getDescricao();		
        default:
            return null;
        }

    }

    //M�todos criados
    public void Recarregar(List<CategoriaEntity> list) {
        this.objetos.clear();
        if(list != null && !list.isEmpty()){
            this.objetos.addAll(list);
        }
        fireTableDataChanged();
    }

    public CategoriaEntity getObjeto(int rowIndex) {
        CategoriaEntity objeto = objetos.get(rowIndex);
        return objeto;
    }
        
    private String[] nomeTabela = new String[] {"categorias","categorias","categorias"};
    private String[] nomeColunaTabela = new String[] {"id","nome","descricao"};
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










