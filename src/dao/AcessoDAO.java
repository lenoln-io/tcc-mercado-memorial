package dao;

import config.Conexao;
import entity.AcessoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class AcessoDAO implements ICrudDAO<AcessoEntity> {

    @Override
    public AcessoEntity salvar(AcessoEntity acesso) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(acesso.getId() == 0){
                sql = "insert into acessos(nome, descricao, tiposistema, recurso) values(?, ?, ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, acesso.getNome());
                sqlParametro.setString(2, acesso.getDescricao());
                sqlParametro.setString(3, acesso.getTiposistema());
                sqlParametro.setString(4, acesso.getDescricao());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    acesso.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update acessos set nome = ?, descricao = ?, tiposistema = ?, recurso = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(5, acesso.getId());
                sqlParametro.setString(1, acesso.getNome());
                sqlParametro.setString(2, acesso.getDescricao());
                sqlParametro.setString(3, acesso.getTiposistema());
                sqlParametro.setString(4, acesso.getDescricao());
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
        return acesso;
    }

    @Override
    public boolean excluir(AcessoEntity acesso) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from acessos where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, acesso.getId());
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
    public List<AcessoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<AcessoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcessoEntity entidade = null;
        String sql;

        try {
            sql = "select * from acessos order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcessoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTiposistema(resultado.getString("tiposistema"));
                entidade.setRecurso(resultado.getString("recurso"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public AcessoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        AcessoEntity entidade = null;
        String sql;
        try {
            sql = "select * from acessos where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new AcessoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTiposistema(resultado.getString("tiposistema"));
                entidade.setRecurso(resultado.getString("recurso"));
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
    public List<AcessoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<AcessoEntity> lista = new ArrayList<AcessoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcessoEntity entidade = null;

        try {
            sql = "select * from acessos where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcessoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTiposistema(resultado.getString("tiposistema"));
                entidade.setRecurso(resultado.getString("recurso"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<AcessoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<AcessoEntity> lista = new ArrayList<AcessoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcessoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcessoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTiposistema(resultado.getString("tiposistema"));
                entidade.setRecurso(resultado.getString("recurso"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<AcessoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<AcessoEntity> lista = new ArrayList<AcessoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcessoEntity entidade = null;
        String sql;

        try {
            sql = "select * from acessos";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcessoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTiposistema(resultado.getString("tiposistema"));
                entidade.setRecurso(resultado.getString("recurso"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<AcessoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<AcessoEntity> lista = new ArrayList<AcessoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        AcessoEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new AcessoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTiposistema(resultado.getString("tiposistema"));
                entidade.setRecurso(resultado.getString("recurso"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}