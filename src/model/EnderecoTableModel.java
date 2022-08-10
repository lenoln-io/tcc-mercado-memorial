package model;

import entity.EnderecoEntity;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

public class EnderecoTableModel extends AbstractTableModel {

    private final ArrayList<EnderecoEntity> objetos;
    private final String[] colunasJTable = new String[] {"Estado","Cidade","Bairro","Nome da rua","Número da casa"};

    public EnderecoTableModel(){
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

        EnderecoEntity objeto = objetos.get(rowIndex);

        switch (columnIndex) {
        case 0:
            return objeto.getBairro().getCidade().getEstado().getUf();
        case 1:
            return objeto.getBairro().getCidade().getNome();
        case 2:
            return objeto.getBairro().getNome();
        case 3:
            return objeto.getNomeRua();
        case 4:
            return objeto.getNumCasa();		
        default:
            return null;
        }

    }

    //M�todos criados
    public void limpar(){
        this.objetos.clear();
        fireTableDataChanged();
    }
    
    public void Recarregar(List<EnderecoEntity> list) {
        this.objetos.clear();
        this.objetos.addAll(list);
        fireTableDataChanged();
    }

    public EnderecoEntity getObjeto(int rowIndex) {
        EnderecoEntity objeto = objetos.get(rowIndex);
        return objeto;
    }
    
    public List<EnderecoEntity> getObjetos(){
        return objetos;
    }
    
}
