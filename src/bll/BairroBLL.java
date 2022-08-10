package bll;

import dao.BairroDAO;
import entity.BairroEntity;
import java.util.List;
import utility.CriterioFiltro;

public class BairroBLL implements ICrudBLL<BairroEntity>{

    @Override
    public BairroEntity salvar(BairroEntity bairro) {
        return new BairroDAO().salvar(bairro);
    }

    @Override
    public boolean excluir(BairroEntity bairro) {
        return new BairroDAO().excluir(bairro);
    }

    @Override
    public List<BairroEntity> listarTodos() {
        return new BairroDAO().listarTodos();
    }

    @Override
    public BairroEntity pesquisar(long id) {
        return new BairroDAO().pesquisar(id);
    }

    @Override
    public List<BairroEntity> pesquisaPersonalizada(String sql) {
        return new BairroDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<BairroEntity> sqlDeConsultaPersonalizada(String sql) {
        return new BairroDAO().sqlDeConsultaPersonalizada(sql);
    }
    
    @Override
    public List<BairroEntity> filtrar(CriterioFiltro criterio) {
        return new BairroDAO().filtrar(criterio);
    }
    
    @Override
    public List<BairroEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new BairroDAO().filtrar(sql, criterio);
    }

}