package dao;

import config.Conexao;
import entity.UsuarioEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class UsuarioDAO implements ICrudDAO<UsuarioEntity>{

    @Override
    public UsuarioEntity salvar(UsuarioEntity usuario) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        try {
            
            if(usuario.getId() == 0){
                
                sql = "insert into usuarios(pessoa, email, senha, estatus, grupo) values(?, ?, md5(?), ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, usuario.getPessoa().getId());
                sqlParametro.setString(2, usuario.getEmail());
                sqlParametro.setString(3, usuario.getSenha());
                sqlParametro.setString(4, usuario.getEstatus());
                sqlParametro.setLong(5, usuario.getGrupo().getId());
                sqlParametro.executeUpdate();
                
            }else{
                
                sql = "update usuarios set pessoa = ?, email = ?, estatus = ?, grupo = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(5, usuario.getId());
                sqlParametro.setLong(1, usuario.getPessoa().getId());
                sqlParametro.setString(2, usuario.getEmail());
                sqlParametro.setString(3, usuario.getEstatus());
                sqlParametro.setLong(4, usuario.getGrupo().getId());
                sqlParametro.executeUpdate();
                
                if(usuario.getSenha() != null){
                    List<UsuarioEntity> usuarios = pesquisaPersonalizada("id = "+usuario.getId()+" AND senha = md5('"+usuario.getSenha()+"')");
                    if(usuarios == null || usuarios.isEmpty()){
                        sql = "update usuarios set senha = md5(?) where id = ?;";
                        sqlParametro = conexao.prepareStatement(sql);
                        sqlParametro.setLong(2, usuario.getId());
                        sqlParametro.setString(1, usuario.getSenha());
                        sqlParametro.executeUpdate();
                    }
                }
                
                if(usuario.getUltimoLogin() != null){
                    sql = "update usuarios set ultimologin = ? where id = ?;";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(2, usuario.getId());
                    sqlParametro.setString(1, usuario.getUltimoLogin());
                    sqlParametro.executeUpdate();
                }
                
                if(usuario.getUltimoLogout() != null){
                    sql = "update usuarios set ultimologout = ? where id = ?;";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(2, usuario.getId());
                    sqlParametro.setString(1, usuario.getUltimoLogout());
                    sqlParametro.executeUpdate();
                }
                
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
        return usuario;
    }

    @Override
    public boolean excluir(UsuarioEntity usuario) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from usuarios where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, usuario.getId());
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
    public List<UsuarioEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioEntity> lista = new ArrayList<UsuarioEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioEntity entidade = null;
        String sql;

        try {
            
            sql = "select * from usuarios where id > 0 order by email;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            
            while (resultado.next()) {
                entidade = new UsuarioEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("pessoa")));
                //entidade.setSenha(resultado.getString("senha"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setEstatus(resultado.getString("estatus"));
                entidade.setGrupo(new GrupoDAO().pesquisar(resultado.getLong("grupo")));
                entidade.setUltimoLogin(resultado.getString("ultimologin"));
                entidade.setUltimoLogout(resultado.getString("ultimologout"));
                lista.add(entidade);
            }
            
            resultado.close();
            sqlParametro.close();
            conexao.close();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public UsuarioEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        UsuarioEntity entidade = null;
        String sql;
        PessoaDAO pessoaDAO = new PessoaDAO();
        GrupoDAO grupoDAO = new GrupoDAO();
        
        try {
            sql = "select * from usuarios where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new UsuarioEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setPessoa(pessoaDAO.pesquisar(resultado.getLong("pessoa")));
                //entidade.setSenha(resultado.getString("senha"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setEstatus(resultado.getString("estatus"));
                entidade.setGrupo(grupoDAO.pesquisar(resultado.getLong("grupo")));
                entidade.setUltimoLogin(resultado.getString("ultimologin"));
                entidade.setUltimoLogout(resultado.getString("ultimologout"));
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
    public List<UsuarioEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioEntity> lista = new ArrayList<UsuarioEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioEntity entidade = null;

        try {
            sql = "select * from usuarios where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("pessoa")));
                //entidade.setSenha(resultado.getString("senha"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setEstatus(resultado.getString("estatus"));
                entidade.setGrupo(new GrupoDAO().pesquisar(resultado.getLong("grupo")));
                entidade.setUltimoLogin(resultado.getString("ultimologin"));
                entidade.setUltimoLogout(resultado.getString("ultimologout"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<UsuarioEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioEntity> lista = new ArrayList<UsuarioEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("pessoa")));
                //entidade.setSenha(resultado.getString("senha"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setEstatus(resultado.getString("estatus"));
                entidade.setGrupo(new GrupoDAO().pesquisar(resultado.getLong("grupo")));
                entidade.setUltimoLogin(resultado.getString("ultimologin"));
                entidade.setUltimoLogout(resultado.getString("ultimologout"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<UsuarioEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioEntity> lista = new ArrayList<UsuarioEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioEntity entidade = null;
        String sql;

        try {
            sql = "select * from usuarios, grupos, pessoas where pessoas.id = usuarios.pessoa and usuarios.grupo = grupos.id and usuarios.id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order usuario.email;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("pessoa")));
                //entidade.setSenha(resultado.getString("senha"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setEstatus(resultado.getString("estatus"));
                entidade.setGrupo(new GrupoDAO().pesquisar(resultado.getLong("grupo")));
                entidade.setUltimoLogin(resultado.getString("ultimologin"));
                entidade.setUltimoLogout(resultado.getString("ultimologout"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<UsuarioEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<UsuarioEntity> lista = new ArrayList<UsuarioEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        UsuarioEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new UsuarioEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("pessoa")));
                //entidade.setSenha(resultado.getString("senha"));
                entidade.setEmail(resultado.getString("email"));
                entidade.setEstatus(resultado.getString("estatus"));
                entidade.setGrupo(new GrupoDAO().pesquisar(resultado.getLong("grupo")));
                entidade.setUltimoLogin(resultado.getString("ultimologin"));
                entidade.setUltimoLogout(resultado.getString("ultimologout"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
