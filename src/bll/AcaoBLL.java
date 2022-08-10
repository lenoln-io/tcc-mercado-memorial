package bll;

import dao.AcaoDAO;
import entity.AcaoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class AcaoBLL implements ICrudBLL<AcaoEntity>{

    @Override
    public AcaoEntity salvar(AcaoEntity acao) {
        return new AcaoDAO().salvar(acao);
    }

    @Override
    public boolean excluir(AcaoEntity acao) {
        return new AcaoDAO().excluir(acao);
    }

    @Override
    public List<AcaoEntity> listarTodos() {
        return new AcaoDAO().listarTodos();
    }

    @Override
    public AcaoEntity pesquisar(long id) {
        return new AcaoDAO().pesquisar(id);
    }

    @Override
    public List<AcaoEntity> pesquisaPersonalizada(String sql) {
        return new AcaoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<AcaoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new AcaoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<AcaoEntity> filtrar(CriterioFiltro criterio) {
        return new AcaoDAO().filtrar(criterio);
    }
    
    @Override
    public List<AcaoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new AcaoDAO().filtrar(sql, criterio);
    }

}