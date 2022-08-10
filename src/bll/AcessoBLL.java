package bll;

import dao.AcessoDAO;
import entity.AcessoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class AcessoBLL implements ICrudBLL<AcessoEntity>{

    @Override
    public AcessoEntity salvar(AcessoEntity acesso) {
        return new AcessoDAO().salvar(acesso);
    }

    @Override
    public boolean excluir(AcessoEntity acesso) {
        return new AcessoDAO().excluir(acesso);
    }

    @Override
    public List<AcessoEntity> listarTodos() {
        return new AcessoDAO().listarTodos();
    }

    @Override
    public AcessoEntity pesquisar(long id) {
        return new AcessoDAO().pesquisar(id);
    }

    @Override
    public List<AcessoEntity> pesquisaPersonalizada(String sql) {
        return new AcessoDAO().pesquisaPersonalizada(sql);
    }
    
    public List<AcessoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new AcessoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<AcessoEntity> filtrar(CriterioFiltro criterio) {
        return new AcessoDAO().filtrar(criterio);
    }
    
    @Override
    public List<AcessoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new AcessoDAO().filtrar(sql, criterio);
    }

}