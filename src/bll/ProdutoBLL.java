package bll;

import dao.ProdutoDAO;
import entity.ProdutoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class ProdutoBLL implements ICrudBLL<ProdutoEntity>{

    @Override
    public ProdutoEntity salvar(ProdutoEntity usuario) {
        return new ProdutoDAO().salvar(usuario);
    }

    @Override
    public boolean excluir(ProdutoEntity usuario) {
        return new ProdutoDAO().excluir(usuario);
    }

    @Override
    public List<ProdutoEntity> listarTodos() {
        return new ProdutoDAO().listarTodos();
    }

    @Override
    public ProdutoEntity pesquisar(long id) {
        return new ProdutoDAO().pesquisar(id);
    }

    @Override
    public List<ProdutoEntity> pesquisaPersonalizada(String sql) {
        return new ProdutoDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<ProdutoEntity> sqlDeConsultaPersonalizada(String sql) {
        return new ProdutoDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<ProdutoEntity> filtrar(CriterioFiltro criterio) {
        return new ProdutoDAO().filtrar(criterio);
    }
    
    @Override
    public List<ProdutoEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new ProdutoDAO().filtrar(sql, criterio);
    }

}
