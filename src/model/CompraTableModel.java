package model;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import entity.OperacaoEntity;
import static utility.DataUtility.formataStrignDateTimeEnToBr;

public class CompraTableModel extends AbstractTableModel {

    private ArrayList<OperacaoEntity> objetos;
    private String[] colunasJTable = new String[] {"Código","Data","Usuário realizador","Fornecedor"};

    public CompraTableModel(){
        objetos = new ArrayList<OperacaoEntity>();
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

        OperacaoEntity objeto = objetos.get(rowIndex);

        switch (columnIndex) {
        case 0:
            return objeto.getId();
        case 1:
            return formataStrignDateTimeEnToBr(objeto.getData());
        case 2:
            return objeto.getUsuario().getPessoa().getNome();
        case 3:
            if(objeto.getPessoa() != null)
                return objeto.getPessoa().getNome();
            else
                return "Desconhecido";
        default:
            return null;
        }

    }

    //M�todos criados
    public void Recarregar(List<OperacaoEntity> list) {
        this.objetos.clear();
        if(list != null && !list.isEmpty()){
            this.objetos.addAll(list);
        }
        fireTableDataChanged();
    }

    public OperacaoEntity getObjeto(int rowIndex) {
        OperacaoEntity objeto = objetos.get(rowIndex);
        return objeto;
    }
        
    private String[] nomeTabela = new String[] {"O","O","PU","PP"};
    private String[] nomeColunaTabela = new String[] {"id","data","nome","nome"};
    private String[] tipoColunaTabela = new String[] {"int","datetime","string","string"};
    
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