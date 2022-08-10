package bll;

import dao.PessoaJuridicaDAO;
import entity.PessoaJuridicaEntity;
import java.util.List;
import utility.CriterioFiltro;

public class PessoaJuridicaBLL implements ICrudBLL<PessoaJuridicaEntity>{

    @Override
    public PessoaJuridicaEntity salvar(PessoaJuridicaEntity categoria) {
        return new PessoaJuridicaDAO().salvar(categoria);
    }

    @Override
    public boolean excluir(PessoaJuridicaEntity categoria) {
        return new PessoaJuridicaDAO().excluir(categoria);
    }

    @Override
    public List<PessoaJuridicaEntity> listarTodos() {
        return new PessoaJuridicaDAO().listarTodos();
    }

    @Override
    public PessoaJuridicaEntity pesquisar(long id) {
        return new PessoaJuridicaDAO().pesquisar(id);
    }

    @Override
    public List<PessoaJuridicaEntity> pesquisaPersonalizada(String sql) {
        return new PessoaJuridicaDAO().pesquisaPersonalizada(sql);
    }
    
    @Override
    public List<PessoaJuridicaEntity> sqlDeConsultaPersonalizada(String sql) {
        return new PessoaJuridicaDAO().sqlDeConsultaPersonalizada(sql);
    }

    @Override
    public List<PessoaJuridicaEntity> filtrar(CriterioFiltro criterio) {
        return new PessoaJuridicaDAO().filtrar(criterio);
    }
    
    @Override
    public List<PessoaJuridicaEntity> filtrar(String sql, CriterioFiltro criterio) {
        return new PessoaJuridicaDAO().filtrar(sql, criterio);
    }

}