package bll;

import dao.CidadeDAO;
import entity.CidadeEntity;
import java.util.List;
import utility.CriterioFiltro;

public class CidadeBLL implements ICrudBLL<CidadeEntity>{

    @Override
    public CidadeEntity salvar(CidadeEntity cidade) {
        return new CidadeDAO().salvar(cidade);
    }

    @Override
    public boolean excluir(CidadeEntity cidade) {
        return new CidadeDAO().excluir(cidade);
    }

    @Override
    public List<CidadeEntity> listarTodos() {
        return new CidadeDAO().listarTodos();
    }

    @Override
    public CidadeEntity pesquisar(long id) {
        return new CidadeDAO().pesquisar(id);
    }

    @Override
    public List<CidadeEntity> pesquisaPersonalizada(String sql) {
        return new CidadeDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<CidadeEntity> sqlDeConsultaPersonalizada(String sql) {
        return new CidadeDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<CidadeEntity> filtrar(CriterioFiltro criterio) {
        return new CidadeDAO().filtrar(criterio);
    }
    
    @Override
    public List<CidadeEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new CidadeDAO().filtrar(sql, criterio);
    }

}