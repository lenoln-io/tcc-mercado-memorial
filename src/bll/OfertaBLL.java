package bll;

import dao.OfertaDAO;
import entity.OfertaEntity;
import entity.ProdutoEntity;
import java.util.List;
import utility.CriterioFiltro;

public class OfertaBLL implements ICrudBLL<OfertaEntity>{

    @Override
    public OfertaEntity salvar(OfertaEntity oferta) {
        return new OfertaDAO().salvar(oferta);
    }
    
    public boolean excluirPorProduto(ProdutoEntity produto) {
        return new OfertaDAO().excluirPorProduto(produto);
    }

    @Override
    public boolean excluir(OfertaEntity oferta) {
        return new OfertaDAO().excluir(oferta);
    }

    @Override
    public List<OfertaEntity> listarTodos() {
        return new OfertaDAO().listarTodos();
    }

    @Override
    public OfertaEntity pesquisar(long id) {
        return new OfertaDAO().pesquisar(id);
    }

    @Override
    public List<OfertaEntity> pesquisaPersonalizada(String sql) {
        return new OfertaDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<OfertaEntity> sqlDeConsultaPersonalizada(String sql) {
        return new OfertaDAO().sqlDeConsultaPersonalizada(sql);
    }
    
    @Override
    public List<OfertaEntity> filtrar(CriterioFiltro criterio) {
        return new OfertaDAO().filtrar(criterio);
    }
    
    @Override
    public List<OfertaEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new OfertaDAO().filtrar(sql, criterio);
    }

}