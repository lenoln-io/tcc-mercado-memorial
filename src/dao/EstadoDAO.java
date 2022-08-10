package dao;

import config.Conexao;
import entity.EstadoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class EstadoDAO implements ICrudDAO<EstadoEntity>{

    @Override
    public EstadoEntity salvar(EstadoEntity estado) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(estado.getId() == 0){
                sql = "insert into estados(nome, uf) values(?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, estado.getNome());
                sqlParametro.setString(2, estado.getUf());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    estado.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update estados set nome = ?, uf = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(3, estado.getId());
                sqlParametro.setString(1, estado.getNome());
                sqlParametro.setString(2, estado.getUf());
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
        return estado;
    }

    @Override
    public boolean excluir(EstadoEntity estado) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from estados where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, estado.getId());
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
    public List<EstadoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<EstadoEntity> lista = new ArrayList<EstadoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        EstadoEntity entidade = null;
        String sql;

        try {
            sql = "select * from estados where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new EstadoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setUf(resultado.getString("uf"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public EstadoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        EstadoEntity entidade = null;
        String sql;
        try {
            sql = "select * from estados where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new EstadoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setUf(resultado.getString("uf"));
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
    public List<EstadoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<EstadoEntity> lista = new ArrayList<EstadoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        EstadoEntity entidade = null;

        try {
            sql = "select * from estados where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new EstadoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setUf(resultado.getString("uf"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<EstadoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<EstadoEntity> lista = new ArrayList<EstadoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        EstadoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new EstadoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setUf(resultado.getString("uf"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<EstadoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<EstadoEntity> lista = new ArrayList<EstadoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        EstadoEntity entidade = null;
        String sql;

        try {
            sql = "select * from estados where id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by nome");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new EstadoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setUf(resultado.getString("uf"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<EstadoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<EstadoEntity> lista = new ArrayList<EstadoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        EstadoEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new EstadoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setUf(resultado.getString("uf"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
