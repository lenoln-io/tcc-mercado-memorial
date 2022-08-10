package dao;

import config.Conexao;
import entity.UsuarioAcaoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class UsuarioAcaoDAO implements ICrudDAO<UsuarioAcaoEntity> {

    @Override
    public UsuarioAcaoEntity salvar(UsuarioAcaoEntity acao) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(acao.getId() == 0){
                sql = "insert into usuarioacoes(usuario, acao, horario, descricao) values(?, ?, ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, acao.getUsuario().getId());
                sqlParametro.setLong(2, acao.getAcao().getId());
                sqlParametro.setString(3, acao.getHorario());
                sqlParametro.setString(4, acao.getDescricao());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    acao.setId(resultado.getInt("Id"));
                }
                resultado.close();
            }else{
                sql = "update usuarioacoes set usuario = ?, acao = ?, horario = ?, descricao = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(5, acao.getId());
                sqlParametro.setLong(1, acao.getUsuario().getId());
                sqlParametro.setLong(2, acao.getAcao().getId());
                sqlParametro.setString(3, acao.getHorario());
                sqlParametro.setString(4, acao.getDescricao());
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
    
    public boolean exclusaoPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        boolean teste = false;
        try {
            sql = "delete from usuarioacoes where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
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
    public boolean excluir(UsuarioAcaoEntity acao) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from usuarioacoes where id = ?;";
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
    public List<UsuarioAcaoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioAcaoEntity> lista = new ArrayList<UsuarioAcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioAcaoEntity entidade = null;
        String sql;
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        AcaoDAO acaoDAO = new AcaoDAO();

        try {
            sql = "select * from usuarioacoes where id > 0 order by horario desc;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setFetchSize(20);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioAcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setUsuario(usuarioDAO.pesquisar(resultado.getLong("usuario")));
                entidade.setAcao(acaoDAO.pesquisar(resultado.getLong("acao")));
                entidade.setHorario(resultado.getString("horario"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
            
            sqlParametro.close();
            resultado.close();
            conexao.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public UsuarioAcaoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        UsuarioAcaoEntity entidade = null;
        String sql;
        try {
            sql = "select * from usuarioacoes where id = ? order by horario desc;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            
            if (resultado.next()) {
                entidade = new UsuarioAcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("usuario")));
                entidade.setAcao(new AcaoDAO().pesquisar(resultado.getLong("acao")));
                entidade.setHorario(resultado.getString("horario"));
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
    public List<UsuarioAcaoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioAcaoEntity> lista = new ArrayList<UsuarioAcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioAcaoEntity entidade = null;

        try {
            sql = "select * from usuarioacoes where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioAcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("usuario")));
                entidade.setAcao(new AcaoDAO().pesquisar(resultado.getLong("acao")));
                entidade.setHorario(resultado.getString("horario"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<UsuarioAcaoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioAcaoEntity> lista = new ArrayList<UsuarioAcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioAcaoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioAcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("usuario")));
                entidade.setAcao(new AcaoDAO().pesquisar(resultado.getLong("acao")));
                entidade.setHorario(resultado.getString("horario"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<UsuarioAcaoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioAcaoEntity> lista = new ArrayList<UsuarioAcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioAcaoEntity entidade = null;
        String sql;

        try {
            sql = "select * from usuarioacoes, usuarios, grupos, acoes where usuarioacoes.usuario = usuarios.id and usuarios.grupo = grupos.id and usuarioacoes.acao = acoes.id and usuarioacoes.id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by horario desc;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioAcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("usuario")));
                entidade.setAcao(new AcaoDAO().pesquisar(resultado.getLong("acao")));
                entidade.setHorario(resultado.getString("horario"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<UsuarioAcaoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioAcaoEntity> lista = new ArrayList<UsuarioAcaoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioAcaoEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioAcaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("usuario")));
                entidade.setAcao(new AcaoDAO().pesquisar(resultado.getLong("acao")));
                entidade.setHorario(resultado.getString("horario"));
                entidade.setDescricao(resultado.getString("descricao"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
