package bll;

import dao.GrupoDAO;
import entity.GrupoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class GrupoBLL implements ICrudBLL<GrupoEntity>{

    @Override
    public GrupoEntity salvar(GrupoEntity grupo) {
        return new GrupoDAO().salvar(grupo);
    }

    @Override
    public boolean excluir(GrupoEntity grupo) {
        return new GrupoDAO().excluir(grupo);
    }

    @Override
    public List<GrupoEntity> listarTodos() {
        return new GrupoDAO().listarTodos();
    }

    @Override
    public GrupoEntity pesquisar(long id) {
        return new GrupoDAO().pesquisar(id);
    }

    @Override
    public List<GrupoEntity> pesquisaPersonalizada(String sql) {
        return new GrupoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<GrupoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new GrupoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<GrupoEntity> filtrar(CriterioFiltro criterio) {
        return new GrupoDAO().filtrar(criterio);
    }
    
    @Override
    public List<GrupoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new GrupoDAO().filtrar(sql, criterio);
    }

}