package dao;

import config.Conexao;
import entity.AcessoEntity;
import entity.GrupoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class GrupoDAO implements ICrudDAO<GrupoEntity> {

    @Override
    public GrupoEntity salvar(GrupoEntity grupo) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(grupo.getId() == 0){
                sql = "insert into grupos(nome,descricao,loginweb,logindesktop) values(?,?,?,?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, grupo.getNome());
                sqlParametro.setString(2, grupo.getDescricao());
                sqlParametro.setString(3, grupo.getLoginweb());
                sqlParametro.setString(4, grupo.getLogindesktop());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    grupo.setId(resultado.getInt("Id"));
                }
                resultado.close();
                if(grupo.getAcessos() != null)
                    for(int x = 0; x<grupo.getAcessos().size(); x++){
                        AcessoEntity a = grupo.getAcessos().get(x);
                        sql = "insert into grupoacessos(grupo, acesso) values(?, ?);";
                        sqlParametro = conexao.prepareStatement(sql);
                        sqlParametro.setLong(1, grupo.getId());
                        sqlParametro.setLong(2, a.getId());
                        sqlParametro.executeUpdate();
                    }
            }
            else{
                
                sql = "delete from grupoacessos where grupo = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, grupo.getId());
                sqlParametro.executeUpdate();
                
                sql = "update grupos set nome = ?, descricao = ?, loginweb = ?, logindesktop = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(5, grupo.getId());
                sqlParametro.setString(1, grupo.getNome());
                sqlParametro.setString(2, grupo.getDescricao());
                sqlParametro.setString(3, grupo.getLoginweb());
                sqlParametro.setString(4, grupo.getLogindesktop());
                sqlParametro.executeUpdate();
                
                if(grupo.getAcessos() != null){
                    for(int x = 0; x<grupo.getAcessos().size(); x++){
                        AcessoEntity a = grupo.getAcessos().get(x);
                        sql = "insert into grupoacessos(grupo, acesso) values(?, ?);";
                        sqlParametro = conexao.prepareStatement(sql);
                        sqlParametro.setLong(1, grupo.getId());
                        sqlParametro.setLong(2, a.getId());
                        sqlParametro.executeUpdate();
                    }
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
        return grupo;
    }

    @Override
    public boolean excluir(GrupoEntity grupo) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from grupoacessos where grupo = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, grupo.getId());
            sqlParametro.executeUpdate();
            sql = "delete from grupos where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, grupo.getId());
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
    public List<GrupoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<GrupoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        GrupoEntity entidade = null;
        String sql;

        try {
            sql = "select * from grupos where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new GrupoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setLogindesktop(resultado.getString("logindesktop"));
                entidade.setLoginweb(resultado.getString("loginweb"));
                
                String sqla = "select acesso from grupoAcessos where grupo = ?;";
                PreparedStatement sqlParametroa = null;
                ResultSet resultadoa = null;
                sqlParametroa = conexao.prepareStatement(sqla);
                sqlParametroa.setLong(1, entidade.getId());
                resultadoa = sqlParametroa.executeQuery();
                while (resultadoa.next()) {
                    entidade.addAcesso(new AcessoDAO().pesquisar(resultadoa.getInt("acesso")));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public GrupoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        GrupoEntity entidade = null;
        String sql;
        try {
            sql = "select * from grupos where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new GrupoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setLogindesktop(resultado.getString("logindesktop"));
                entidade.setLoginweb(resultado.getString("loginweb"));
                
                String sqla = "select acesso from grupoAcessos where grupo = ?;";
                PreparedStatement sqlParametroa = null;
                ResultSet resultadoa = null;
                sqlParametroa = conexao.prepareStatement(sqla);
                sqlParametroa.setLong(1, entidade.getId());
                resultadoa = sqlParametroa.executeQuery();
                while (resultadoa.next()) {
                    entidade.addAcesso(new AcessoDAO().pesquisar(resultadoa.getInt("acesso")));
                }
                resultadoa.close();
                sqlParametroa.close();
                
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                consulta.close();
                resultado.close();
                conexao.close();
            } 
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return entidade;
    }

    @Override
    public List<GrupoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<GrupoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        GrupoEntity entidade = null;

        try {
            sql = "select * from grupos where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new GrupoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setLogindesktop(resultado.getString("logindesktop"));
                entidade.setLoginweb(resultado.getString("loginweb"));
                
                String sqla = "select acesso from grupoAcessos where grupo = ?;";
                PreparedStatement sqlParametroa = null;
                ResultSet resultadoa = null;
                sqlParametroa = conexao.prepareStatement(sqla);
                sqlParametroa.setLong(1, entidade.getId());
                resultadoa = sqlParametroa.executeQuery();
                while (resultadoa.next()) {
                    entidade.addAcesso(new AcessoDAO().pesquisar(resultadoa.getInt("acesso")));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<GrupoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<GrupoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        GrupoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new GrupoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setLogindesktop(resultado.getString("logindesktop"));
                entidade.setLoginweb(resultado.getString("loginweb"));
                
                String sqla = "select acesso from grupoAcessos where grupo = ?;";
                PreparedStatement sqlParametroa = null;
                ResultSet resultadoa = null;
                sqlParametroa = conexao.prepareStatement(sqla);
                sqlParametroa.setLong(1, entidade.getId());
                resultadoa = sqlParametroa.executeQuery();
                while (resultadoa.next()) {
                    entidade.addAcesso(new AcessoDAO().pesquisar(resultadoa.getInt("acesso")));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<GrupoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<GrupoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        GrupoEntity entidade = null;
        String sql;

        try {
            sql = "select * from grupos";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new GrupoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setLogindesktop(resultado.getString("logindesktop"));
                entidade.setLoginweb(resultado.getString("loginweb"));
                
                String sqla = "select acesso from grupoAcessos where grupo = ?;";
                PreparedStatement sqlParametroa = null;
                ResultSet resultadoa = null;
                sqlParametroa = conexao.prepareStatement(sqla);
                sqlParametroa.setLong(1, entidade.getId());
                resultadoa = sqlParametroa.executeQuery();
                while (resultadoa.next()) {
                    entidade.addAcesso(new AcessoDAO().pesquisar(resultadoa.getInt("acesso")));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<GrupoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<GrupoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        GrupoEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new GrupoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setLogindesktop(resultado.getString("logindesktop"));
                entidade.setLoginweb(resultado.getString("loginweb"));
                
                String sqla = "select acesso from grupoAcessos where grupo = ?;";
                PreparedStatement sqlParametroa = null;
                ResultSet resultadoa = null;
                sqlParametroa = conexao.prepareStatement(sqla);
                sqlParametroa.setLong(1, entidade.getId());
                resultadoa = sqlParametroa.executeQuery();
                while (resultadoa.next()) {
                    entidade.addAcesso(new AcessoDAO().pesquisar(resultadoa.getInt("acesso")));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}