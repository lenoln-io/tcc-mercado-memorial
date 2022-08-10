package bll;

import dao.PessoaFisicaDAO;
import entity.PessoaFisicaEntity;
import java.util.List;
import utility.CriterioFiltro;

public class PessoaFisicaBLL implements ICrudBLL<PessoaFisicaEntity>{
    
    @Override
    public PessoaFisicaEntity salvar(PessoaFisicaEntity categoria) {
        return new PessoaFisicaDAO().salvar(categoria);
    }

    @Override
    public boolean excluir(PessoaFisicaEntity categoria) {
        return new PessoaFisicaDAO().excluir(categoria);
    }

    @Override
    public List<PessoaFisicaEntity> listarTodos() {
        return new PessoaFisicaDAO().listarTodos();
    }

    @Override
    public PessoaFisicaEntity pesquisar(long id) {
        return new PessoaFisicaDAO().pesquisar(id);
    }

    @Override
    public List<PessoaFisicaEntity> pesquisaPersonalizada(String sql) {
        return new PessoaFisicaDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<PessoaFisicaEntity> sqlDeConsultaPersonalizada(String sql) {
        return new PessoaFisicaDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<PessoaFisicaEntity> filtrar(CriterioFiltro criterio) {
        return new PessoaFisicaDAO().filtrar(criterio);
    }
    
    @Override
    public List<PessoaFisicaEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new PessoaFisicaDAO().filtrar(sql, criterio);
    }

}