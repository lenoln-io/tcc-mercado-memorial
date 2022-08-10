package model;

import entity.ContatoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class ContatoTableModel extends AbstractTableModel {

    private ArrayList<ContatoEntity> objetos;
    private String[] colunasJTable = new String[] {"Código","Nome","Email","Tipo","Data","Visualizado"};

    public ContatoTableModel(){
        objetos = new ArrayList<ContatoEntity>();
    }

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

        ContatoEntity objeto = objetos.get(rowIndex);

        switch (columnIndex) {
        case 0:
            return objeto.getId();
        case 1:
            return objeto.getNome();
        case 2:
            return objeto.getEmail();		
        case 3:
            return objeto.getTipo();
        case 4:
            return utility.DataUtility.formatarDataTexto(objeto.getData());
        case 5:
            return objeto.getEstatus();
        default:
            return null;
        }

    }

    public void Recarregar(List<ContatoEntity> list) {
        this.objetos.clear();
        if(list != null && !list.isEmpty()){
            this.objetos.addAll(list);
        }
        fireTableDataChanged();
    }

    public ContatoEntity getObjeto(int rowIndex) {
        ContatoEntity objeto = objetos.get(rowIndex);
        return objeto;
    }
    
    /*private String[] colunasJTable = new String[] {"Código","Nome","Email","Tipo"};*/
    private String[] nomeTabela = new String[] {"contatos","contatos","contatos","contatos","contatos","contatos"};
    private String[] nomeColunaTabela = new String[] {"id","nome","email","tipo","dataenvio","visualizado"};
    private String[] tipoColunaTabela = new String[] {"int","string","string","string","date","string"};
    
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
