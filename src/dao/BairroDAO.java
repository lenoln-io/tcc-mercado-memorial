package dao;

import config.Conexao;
import entity.BairroEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class BairroDAO implements ICrudDAO<BairroEntity>{

    @Override
    public BairroEntity salvar(BairroEntity bairro) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(bairro.getId() == 0){
                sql = "insert into bairros(nome, cidade) values(?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, bairro.getNome());
                sqlParametro.setLong(2, bairro.getCidade().getId());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    bairro.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update bairros set nome = ?, cidade = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(3, bairro.getId());
                sqlParametro.setString(1, bairro.getNome());
                sqlParametro.setLong(2, bairro.getCidade().getId());
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
        return bairro;
    }

    @Override
    public boolean excluir(BairroEntity bairro) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from bairros where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, bairro.getId());
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
    public List<BairroEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<BairroEntity> lista = new ArrayList<BairroEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        BairroEntity entidade = null;
        String sql;

        try {
            sql = "select * from bairros where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setFetchSize(20);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new BairroEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setCidade(new CidadeDAO().pesquisar(resultado.getInt("cidade")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public BairroEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        BairroEntity entidade = null;
        String sql;
        try {
            sql = "select * from bairros where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new BairroEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setCidade(new CidadeDAO().pesquisar(resultado.getInt("cidade")));
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
    public List<BairroEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<BairroEntity> lista = new ArrayList<BairroEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        BairroEntity entidade = null;

        try {
            sql = "select * from bairros where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new BairroEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setCidade(new CidadeDAO().pesquisar(resultado.getInt("cidade")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<BairroEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<BairroEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        BairroEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new BairroEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setCidade(new CidadeDAO().pesquisar(resultado.getInt("cidade")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<BairroEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<BairroEntity> lista = new ArrayList<BairroEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        BairroEntity entidade = null;
        String sql;

        try {
            sql = "select * from bairros, cidades, estados where bairros.cidade = cidades.id "
                    + "and cidades.estado = estados.id and id > 0";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by bairros.nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new BairroEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setCidade(new CidadeDAO().pesquisar(resultado.getInt("cidade")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<BairroEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<BairroEntity> lista = new ArrayList<BairroEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        BairroEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new BairroEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setCidade(new CidadeDAO().pesquisar(resultado.getInt("cidade")));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}