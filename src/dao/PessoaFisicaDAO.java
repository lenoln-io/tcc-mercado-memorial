package dao;

import config.Conexao;
import entity.PessoaFisicaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class PessoaFisicaDAO implements ICrudDAO<PessoaFisicaEntity> {
    
    @Override
    public PessoaFisicaEntity salvar(PessoaFisicaEntity pessoafisica) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        try {
            if(pessoafisica.getId() == 0){
                
                pessoafisica.setPessoa(new PessoaDAO().salvar(pessoafisica.getPessoa()));
                
                sql = "insert into pessoafisicas(pessoa, sexo, cpf, datanasce) values(?, ?, ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, pessoafisica.getId());
                sqlParametro.setString(2, pessoafisica.getSexo());
                sqlParametro.setString(3, pessoafisica.getCpf());
                sqlParametro.setDate(4, pessoafisica.getDataNascimento());
                sqlParametro.executeUpdate();
                
            }
            else{
                
                pessoafisica.setPessoa(new PessoaDAO().salvar(pessoafisica.getPessoa()));
                
                sql = "update pessoafisicas set sexo = ?, cpf = ?, datanasce = ? where pessoa = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(4, pessoafisica.getId());
                sqlParametro.setString(1, pessoafisica.getSexo());
                sqlParametro.setString(2, pessoafisica.getCpf());
                sqlParametro.setDate(3, pessoafisica.getDataNascimento());
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
        return pessoafisica;
    }

    @Override
    public boolean excluir(PessoaFisicaEntity pessoafisica) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from pessoafisicas where pessoa = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, pessoafisica.getId());
            sqlParametro.executeUpdate();
            new PessoaDAO().excluir(pessoafisica.getPessoa());
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
    public List<PessoaFisicaEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<PessoaFisicaEntity> lista = new ArrayList<PessoaFisicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaFisicaEntity entidade = null;
        String sql;

        try {
            sql = "select * from pessoafisicas, pessoas where pessoa > 0 "
                    + "and pessoas.id = pessoafisicas.pessoa "
                    + "order by pessoas.nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaFisicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setSexo(resultado.getString("sexo"));
                entidade.setCpf(resultado.getString("cpf"));
                entidade.setDataNascimento(resultado.getDate("datanasce"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public PessoaFisicaEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        PessoaFisicaEntity entidade = null;
        String sql;
        try {
            sql = "select * from pessoafisicas where pessoa = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new PessoaFisicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setSexo(resultado.getString("sexo"));
                entidade.setCpf(resultado.getString("cpf"));
                entidade.setDataNascimento(resultado.getDate("datanasce"));
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
    public List<PessoaFisicaEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaFisicaEntity> lista = new ArrayList<PessoaFisicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaFisicaEntity entidade = null;

        try {
            sql = "select * from pessoafisicas where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaFisicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setSexo(resultado.getString("sexo"));
                entidade.setCpf(resultado.getString("cpf"));
                entidade.setDataNascimento(resultado.getDate("datanasce"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public List<PessoaFisicaEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaFisicaEntity> lista = new ArrayList<PessoaFisicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaFisicaEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaFisicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setSexo(resultado.getString("sexo"));
                entidade.setCpf(resultado.getString("cpf"));
                entidade.setDataNascimento(resultado.getDate("datanasce"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<PessoaFisicaEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaFisicaEntity> lista = new ArrayList<PessoaFisicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaFisicaEntity entidade = null;
        String sql;

        try {
            sql = "select * from pessoafisicas, pessoas where pessoafisicas.pessoa = pessoas.id and id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by pessoas.nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaFisicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setSexo(resultado.getString("sexo"));
                entidade.setCpf(resultado.getString("cpf"));
                entidade.setDataNascimento(resultado.getDate("datanasce"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    public List<PessoaFisicaEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaFisicaEntity> lista = new ArrayList<PessoaFisicaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaFisicaEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaFisicaEntity();
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getInt("pessoa")));
                entidade.setSexo(resultado.getString("sexo"));
                entidade.setCpf(resultado.getString("cpf"));
                entidade.setDataNascimento(resultado.getDate("datanasce"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
