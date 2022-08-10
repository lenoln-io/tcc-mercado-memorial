package bll;

import dao.PessoaDAO;
import entity.PessoaEntity;
import java.util.List;
import utility.CriterioFiltro;

public class PessoaBLL implements ICrudBLL<PessoaEntity>{
    
    @Override
    public PessoaEntity salvar(PessoaEntity pessoa) {
        return new PessoaDAO().salvar(pessoa);
    }

    @Override
    public boolean excluir(PessoaEntity pessoa) {
        return new PessoaDAO().excluir(pessoa);
    }

    @Override
    public List<PessoaEntity> listarTodos() {
        return new PessoaDAO().listarTodos();
    }

    @Override
    public PessoaEntity pesquisar(long id) {
        return new PessoaDAO().pesquisar(id);
    }

    @Override
    public List<PessoaEntity> pesquisaPersonalizada(String sql) {
        return new PessoaDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<PessoaEntity> sqlDeConsultaPersonalizada(String sql) {
        return new PessoaDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<PessoaEntity> filtrar(CriterioFiltro criterio) {
        return new PessoaDAO().filtrar(criterio);
    }
    
    @Override
    public List<PessoaEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new PessoaDAO().filtrar(sql, criterio);
    }

}