package dao;

import config.Conexao;
import entity.AcaoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class AcaoDAO implements ICrudDAO<AcaoEntity> {

    @Override
    public AcaoEntity salvar(AcaoEntity acao) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(acao.getId() == 0){
                sql = "insert into acoes(nome) values(?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, acao.getNome());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    acao.setId(resultado.getInt("Id"));
                }
                resultado.close();
            }
            else{
                sql = "update acoes set nome = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(3, acao.getId());
                sqlParametro.setString(1, acao.getNome());
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
        return acao;
    }

    @Override
    public boolean excluir(AcaoEntity acao) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from acoes where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, acao.getId());
            sqlParametro.executeUpdate();
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
    public List<AcaoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<AcaoEntity> lista = new ArrayList<AcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcaoEntity entidade = null;
        String sql;

        try {
            sql = "select * from acoes;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setFetchSize(20);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public AcaoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        AcaoEntity entidade = null;
        String sql;
        try {
            
            sql = "select * from acoes where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            
            if (resultado.next()) {
                entidade = new AcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
            }
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
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
    public List<AcaoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<AcaoEntity> lista = new ArrayList<AcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcaoEntity entidade = null;

        try {
            sql = "select * from acoes where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<AcaoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<AcaoEntity> lista = new ArrayList<AcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcaoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<AcaoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<AcaoEntity> lista = new ArrayList<AcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcaoEntity entidade = null;
        String sql;

        try {
            sql = "select * from acoes";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<AcaoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<AcaoEntity> lista = new ArrayList<AcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcaoEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
