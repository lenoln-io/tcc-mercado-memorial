package dao;

import config.Conexao;
import entity.PessoaJuridicaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class PessoaJuridicaDAO implements ICrudDAO<PessoaJuridicaEntity> {

    @Override
    public PessoaJuridicaEntity salvar(PessoaJuridicaEntity pessoajuridica) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        try {
            if(pessoajuridica.getId() == 0){
                
                pessoajuridica.setPessoa(new PessoaDAO().salvar(pessoajuridica.getPessoa()));
                
                sql = "insert into pessoajuridicas(pessoa, cnpj) values(?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, pessoajuridica.getId());
                sqlParametro.setString(2, pessoajuridica.getCnpj());
                sqlParametro.executeUpdate();
                
            }
            else{
                
                pessoajuridica.setPessoa(new PessoaDAO().salvar(pessoajuridica.getPessoa()));
                
                sql = "update pessoajuridicas set cnpj = ? where pessoa = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(2, pessoajuridica.getId());
                sqlParametro.setString(1, pessoajuridica.getCnpj());
                sqlParametro.executeUpdate();
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                sqlParametro.close();
                conexao.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return pessoajuridica;
    }

    @Override
    public boolean excluir(PessoaJuridicaEntity pessoajuridica) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from pessoajuridicas where pessoa = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, pessoajuridica.getId());
            sqlParametro.executeUpdate();
            new PessoaDAO().excluir(pessoajuridica.getPessoa());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                sqlParametro.close();
                conexao.close();
                teste = true;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return teste;
    }

    @Override
    public List<PessoaJuridicaEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<PessoaJuridicaEntity> lista = new ArrayList<PessoaJuridicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaJuridicaEntity entidade = null;
        String sql;

        try {
            sql = "select * from pessoajuridicas, pessoas where pessoa > 0 "
                    + "and pessoas.id = pessoajuridicas.pessoa "
                    + "order by pessoas.nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaJuridicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setCnpj(resultado.getString("cnpj"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public PessoaJuridicaEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        PessoaJuridicaEntity entidade = null;
        String sql;
        try {
            sql = "select * from pessoajuridicas where pessoa = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new PessoaJuridicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setCnpj(resultado.getString("cnpj"));
            }
        } catch (SQLException e) {
                e.printStackTrace();
        } finally {
            try {
                consulta.close();
                resultado.close();
                conexao.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return entidade;
    }

    @Override
    public List<PessoaJuridicaEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaJuridicaEntity> lista = new ArrayList<PessoaJuridicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaJuridicaEntity entidade = null;

        try {
            sql = "select * from pessoajuridicas where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaJuridicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setCnpj(resultado.getString("cnpj"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public List<PessoaJuridicaEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaJuridicaEntity> lista = new ArrayList<PessoaJuridicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaJuridicaEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaJuridicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setCnpj(resultado.getString("cnpj"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<PessoaJuridicaEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaJuridicaEntity> lista = new ArrayList<PessoaJuridicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaJuridicaEntity entidade = null;
        String sql;

        try {
            sql = "select * from pessoajuridicas, pessoas where pessoajuridicas.pessoa = pessoas.id and id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by pessoas.nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaJuridicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setCnpj(resultado.getString("cnpj"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public List<PessoaJuridicaEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaJuridicaEntity> lista = new ArrayList<PessoaJuridicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaJuridicaEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaJuridicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setCnpj(resultado.getString("cnpj"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
