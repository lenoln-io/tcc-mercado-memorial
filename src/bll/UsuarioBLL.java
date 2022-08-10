package bll;

import dao.UsuarioDAO;
import entity.UsuarioEntity;
import java.util.List;
import utility.CriterioFiltro;

public class UsuarioBLL implements ICrudBLL<UsuarioEntity>{

    @Override
    public UsuarioEntity salvar(UsuarioEntity usuario) {
        return new UsuarioDAO().salvar(usuario);
    }

    @Override
    public boolean excluir(UsuarioEntity usuario) {
        return new UsuarioDAO().excluir(usuario);
    }

    @Override
    public List<UsuarioEntity> listarTodos() {
        return new UsuarioDAO().listarTodos();
    }

    @Override
    public UsuarioEntity pesquisar(long id) {
        return new UsuarioDAO().pesquisar(id);
    }

    @Override
    public List<UsuarioEntity> pesquisaPersonalizada(String sql) {
        return new UsuarioDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<UsuarioEntity> sqlDeConsultaPersonalizada(String sql) {
        return new UsuarioDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<UsuarioEntity> filtrar(CriterioFiltro criterio) {
        return new UsuarioDAO().filtrar(criterio);
    }
    
    @Override
    public List<UsuarioEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new UsuarioDAO().filtrar(sql, criterio);
    }

}