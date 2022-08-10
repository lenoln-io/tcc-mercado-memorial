package dao;

import config.Conexao;
import entity.CategoriaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class CategoriaDAO implements ICrudDAO<CategoriaEntity> {

    @Override
    public CategoriaEntity salvar(CategoriaEntity categoria) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(categoria.getId() == 0){
                sql = "insert into categorias(nome, descricao) values(?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, categoria.getNome());
                sqlParametro.setString(2, categoria.getDescricao());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    categoria.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update categorias set nome = ?, descricao = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(3, categoria.getId());
                sqlParametro.setString(1, categoria.getNome());
                sqlParametro.setString(2, categoria.getDescricao());
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
        return categoria;
    }

    @Override
    public boolean excluir(CategoriaEntity categoria) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from categorias where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, categoria.getId());
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
    public List<CategoriaEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<CategoriaEntity> lista = new ArrayList<CategoriaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CategoriaEntity entidade = null;
        String sql;

        try {
            sql = "select * from categorias where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CategoriaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public CategoriaEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        CategoriaEntity entidade = null;
        String sql;
        try {
            sql = "select * from categorias where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new CategoriaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
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
    public List<CategoriaEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<CategoriaEntity> lista = new ArrayList<CategoriaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CategoriaEntity entidade = null;

        try {
            sql = "select * from categorias where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CategoriaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<CategoriaEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<CategoriaEntity> lista = new ArrayList<CategoriaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CategoriaEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CategoriaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<CategoriaEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<CategoriaEntity> lista = new ArrayList<CategoriaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CategoriaEntity entidade = null;
        String sql;

        try {
            sql = "select * from categorias";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by categorias.nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CategoriaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<CategoriaEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<CategoriaEntity> lista = new ArrayList<CategoriaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        CategoriaEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new CategoriaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
