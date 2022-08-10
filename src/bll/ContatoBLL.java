package bll;

import dao.ContatoDAO;
import entity.ContatoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class ContatoBLL implements ICrudBLL<ContatoEntity>{

    @Override
    public ContatoEntity salvar(ContatoEntity contato) {
        return new ContatoDAO().salvar(contato);
    }

    @Override
    public boolean excluir(ContatoEntity contato) {
        return new ContatoDAO().excluir(contato);
    }

    @Override
    public List<ContatoEntity> listarTodos() {
        return new ContatoDAO().listarTodos();
    }

    @Override
    public ContatoEntity pesquisar(long id) {
        return new ContatoDAO().pesquisar(id);
    }

    @Override
    public List<ContatoEntity> pesquisaPersonalizada(String sql) {
        return new ContatoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<ContatoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new ContatoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<ContatoEntity> filtrar(CriterioFiltro criterio) {
        return new ContatoDAO().filtrar(criterio);
    }
    
    @Override
    public List<ContatoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new ContatoDAO().filtrar(sql, criterio);
    }

}
