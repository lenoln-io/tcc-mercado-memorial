package bll;

import dao.OperacaoDAO;
import entity.OperacaoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class OperacaoBLL implements ICrudBLL<OperacaoEntity>{

    @Override
    public OperacaoEntity salvar(OperacaoEntity operacao) {
        return new OperacaoDAO().salvar(operacao);
    }

    @Override
    public boolean excluir(OperacaoEntity operacao) {
        return new OperacaoDAO().excluir(operacao);
    }

    @Override
    public List<OperacaoEntity> listarTodos() {
        return new OperacaoDAO().listarTodos();
    }

    @Override
    public OperacaoEntity pesquisar(long id) {
        return new OperacaoDAO().pesquisar(id);
    }

    @Override
    public List<OperacaoEntity> pesquisaPersonalizada(String sql) {
        return new OperacaoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<OperacaoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new OperacaoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<OperacaoEntity> filtrar(CriterioFiltro criterio) {
        return new OperacaoDAO().filtrar(criterio);
    }
    
    @Override
    public List<OperacaoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new OperacaoDAO().filtrar(sql, criterio);
    }

}