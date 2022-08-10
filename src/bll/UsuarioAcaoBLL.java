package bll;

import dao.UsuarioAcaoDAO;
import entity.UsuarioAcaoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class UsuarioAcaoBLL implements ICrudBLL<UsuarioAcaoEntity>{

    @Override
    public UsuarioAcaoEntity salvar(UsuarioAcaoEntity usuarioAcao) {
        return new UsuarioAcaoDAO().salvar(usuarioAcao);
    }
    
    public boolean exclusaoPersonalizada(String sql) {
        return new UsuarioAcaoDAO().exclusaoPersonalizada(sql);
    }

    @Override
    public boolean excluir(UsuarioAcaoEntity usuarioAcao) {
        return new UsuarioAcaoDAO().excluir(usuarioAcao);
    }

    @Override
    public List<UsuarioAcaoEntity> listarTodos() {
        return new UsuarioAcaoDAO().listarTodos();
    }

    @Override
    public UsuarioAcaoEntity pesquisar(long id) {
        return new UsuarioAcaoDAO().pesquisar(id);
    }

    @Override
    public List<UsuarioAcaoEntity> pesquisaPersonalizada(String sql) {
        return new UsuarioAcaoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<UsuarioAcaoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new UsuarioAcaoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<UsuarioAcaoEntity> filtrar(CriterioFiltro criterio) {
        return new UsuarioAcaoDAO().filtrar(criterio);
    }
    
    @Override
    public List<UsuarioAcaoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new UsuarioAcaoDAO().filtrar(sql, criterio);
    }

}