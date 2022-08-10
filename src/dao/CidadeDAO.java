package dao;

import config.Conexao;
import entity.CidadeEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class CidadeDAO implements ICrudDAO<CidadeEntity>{

    @Override
    public CidadeEntity salvar(CidadeEntity cidade) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(cidade.getId() == 0){
                sql = "insert into cidades(nome, estado) values(?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, cidade.getNome());
                sqlParametro.setLong(2, cidade.getEstado().getId());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    cidade.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update cidades set nome = ?, estado = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(3, cidade.getId());
                sqlParametro.setString(1, cidade.getNome());
                sqlParametro.setLong(2, cidade.getEstado().getId());
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
        return cidade;
    }

    @Override
    public boolean excluir(CidadeEntity cidade) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from cidades where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, cidade.getId());
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
    public List<CidadeEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<CidadeEntity> lista = new ArrayList<CidadeEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CidadeEntity entidade = null;
        String sql;

        try {
            sql = "select * from cidades where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CidadeEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEstado(new EstadoDAO().pesquisar(resultado.getInt("estado")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public CidadeEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        CidadeEntity entidade = null;
        String sql;
        try {
            sql = "select * from cidades where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new CidadeEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEstado(new EstadoDAO().pesquisar(resultado.getInt("estado")));
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
    public List<CidadeEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<CidadeEntity> lista = new ArrayList<CidadeEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CidadeEntity entidade = null;

        try {
            sql = "select * from cidades where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CidadeEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEstado(new EstadoDAO().pesquisar(resultado.getInt("estado")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<CidadeEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<CidadeEntity> lista = new ArrayList<CidadeEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CidadeEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CidadeEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEstado(new EstadoDAO().pesquisar(resultado.getInt("estado")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<CidadeEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<CidadeEntity> lista = new ArrayList<CidadeEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CidadeEntity entidade = null;
        String sql;

        try {
            sql = "select * from cidades, estados where cidades.estado = estados.id and id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by cidades.nome");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CidadeEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEstado(new EstadoDAO().pesquisar(resultado.getInt("estado")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<CidadeEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<CidadeEntity> lista = new ArrayList<CidadeEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CidadeEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CidadeEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEstado(new EstadoDAO().pesquisar(resultado.getInt("estado")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}