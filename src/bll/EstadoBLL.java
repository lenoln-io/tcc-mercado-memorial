package bll;

import dao.EstadoDAO;
import entity.EstadoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class EstadoBLL implements ICrudBLL<EstadoEntity>{

    @Override
    public EstadoEntity salvar(EstadoEntity estado) {
        return new EstadoDAO().salvar(estado);
    }

    @Override
    public boolean excluir(EstadoEntity estado) {
        return new EstadoDAO().excluir(estado);
    }

    @Override
    public List<EstadoEntity> listarTodos() {
        return new EstadoDAO().listarTodos();
    }

    @Override
    public EstadoEntity pesquisar(long id) {
        return new EstadoDAO().pesquisar(id);
    }

    @Override
    public List<EstadoEntity> pesquisaPersonalizada(String sql) {
        return new EstadoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<EstadoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new EstadoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<EstadoEntity> filtrar(CriterioFiltro criterio) {
        return new EstadoDAO().filtrar(criterio);
    }
    
    @Override
    public List<EstadoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new EstadoDAO().filtrar(sql, criterio);
    }

}