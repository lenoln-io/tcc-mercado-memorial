package dao;

import config.Conexao;
import entity.ContatoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class ContatoDAO implements ICrudDAO<ContatoEntity>{

    @Override
    public ContatoEntity salvar(ContatoEntity contato) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(contato.getId() == 0){
                sql = "insert into contatos(nome, email, tipo, mensagem, dataenvio, visualizado) values(?, ?, ?, ?, ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, contato.getNome());
                sqlParametro.setString(2, contato.getEmail());
                sqlParametro.setString(3, contato.getTipo());
                sqlParametro.setString(4, contato.getMensagem());
                sqlParametro.setDate(5, contato.getData());
                sqlParametro.setString(6, contato.getEstatus());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    contato.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update contatos set nome = ?, email = ?, tipo = ?, mensagem = ?, dataenvio = ?, visualizado = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(7, contato.getId());
                sqlParametro.setString(1, contato.getNome());
                sqlParametro.setString(2, contato.getEmail());
                sqlParametro.setString(3, contato.getTipo());
                sqlParametro.setString(4, contato.getMensagem());
                sqlParametro.setDate(5, contato.getData());
                sqlParametro.setString(6, contato.getEstatus());
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
        return contato;
    }

    @Override
    public boolean excluir(ContatoEntity contato) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from contatos where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, contato.getId());
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
    public List<ContatoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<ContatoEntity> lista = new ArrayList<ContatoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ContatoEntity entidade = null;
        String sql;

        try {
            sql = "select * from contatos where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ContatoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setMensagem(resultado.getString("mensagem"));
                entidade.setData(resultado.getDate("dataenvio"));
                entidade.setEstatus(resultado.getString("visualizado"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public ContatoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        ContatoEntity entidade = null;
        String sql;
        try {
            sql = "select * from contatos where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new ContatoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setMensagem(resultado.getString("mensagem"));
                entidade.setData(resultado.getDate("dataenvio"));
                entidade.setEstatus(resultado.getString("visualizado"));
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
    public List<ContatoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<ContatoEntity> lista = new ArrayList<ContatoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ContatoEntity entidade = null;

        try {
            sql = "select * from contatos where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ContatoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setMensagem(resultado.getString("mensagem"));
                entidade.setData(resultado.getDate("dataenvio"));
                entidade.setEstatus(resultado.getString("visualizado"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<ContatoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<ContatoEntity> lista = new ArrayList<ContatoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ContatoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ContatoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setMensagem(resultado.getString("mensagem"));
                entidade.setData(resultado.getDate("dataenvio"));
                entidade.setEstatus(resultado.getString("visualizado"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<ContatoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<ContatoEntity> lista = new ArrayList<ContatoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ContatoEntity entidade = null;
        String sql;

        try {
            sql = "select * from contatos";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by nome");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ContatoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setMensagem(resultado.getString("mensagem"));
                entidade.setData(resultado.getDate("dataenvio"));
                entidade.setEstatus(resultado.getString("visualizado"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<ContatoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<ContatoEntity> lista = new ArrayList<ContatoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ContatoEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ContatoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setMensagem(resultado.getString("mensagem"));
                entidade.setData(resultado.getDate("dataenvio"));
                entidade.setEstatus(resultado.getString("visualizado"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
}
