package bll;

import dao.CategoriaDAO;
import entity.CategoriaEntity;
import java.util.List;
import utility.CriterioFiltro;

public class CategoriaBLL implements ICrudBLL<CategoriaEntity>{

    @Override
    public CategoriaEntity salvar(CategoriaEntity categoria) {
        return new CategoriaDAO().salvar(categoria);
    }

    @Override
    public boolean excluir(CategoriaEntity categoria) {
        return new CategoriaDAO().excluir(categoria);
    }

    @Override
    public List<CategoriaEntity> listarTodos() {
        return new CategoriaDAO().listarTodos();
    }

    @Override
    public CategoriaEntity pesquisar(long id) {
        return new CategoriaDAO().pesquisar(id);
    }

    @Override
    public List<CategoriaEntity> pesquisaPersonalizada(String sql) {
        return new CategoriaDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<CategoriaEntity> sqlDeConsultaPersonalizada(String sql) {
        return new CategoriaDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<CategoriaEntity> filtrar(CriterioFiltro criterio) {
        return new CategoriaDAO().filtrar(criterio);
    }
    
    @Override
    public List<CategoriaEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new CategoriaDAO().filtrar(sql, criterio);
    }

}