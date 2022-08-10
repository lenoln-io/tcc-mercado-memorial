package dao;

import config.Conexao;
import entity.OfertaEntity;
import entity.ProdutoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class OfertaDAO implements ICrudDAO<OfertaEntity>{

    @Override
    public OfertaEntity salvar(OfertaEntity oferta) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(oferta.getId() == 0){
                sql = "insert into ofertas(produto, valordesconto, datainicio, datafim, tipo) values(?, ?, ?, ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, oferta.getProduto().getId());
                sqlParametro.setInt(2, oferta.getValor());
                sqlParametro.setDate(3, oferta.getDtinicio());
                sqlParametro.setDate(4, oferta.getDtfim());
                sqlParametro.setString(5, oferta.getTipo());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    oferta.setId(resultado.getInt("Id"));
                }
            }
            else{
                sql = "update ofertas set produto = ?, valordesconto = ?, datainicio = ?, datafim = ?, tipo = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, oferta.getProduto().getId());
                sqlParametro.setInt(2, oferta.getValor());
                sqlParametro.setDate(3, oferta.getDtinicio());
                sqlParametro.setDate(4, oferta.getDtfim());
                sqlParametro.setString(5, oferta.getTipo());
                sqlParametro.setLong(6, oferta.getId());
                sqlParametro.executeUpdate();
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                sqlParametro.close();
                conexao.close();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return oferta;
    }

    public boolean excluirPorProduto(ProdutoEntity produto) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from ofertas where produto = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, produto.getId());
            sqlParametro.executeUpdate();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                sqlParametro.close();
                conexao.close();
                teste = true;
            } 
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return teste;
    }
    
    @Override
    public boolean excluir(OfertaEntity oferta) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from ofertas where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, oferta.getId());
            sqlParametro.executeUpdate();
        } 
        catch (SQLException e) {
            e.printStackTrace();
        } 
        finally {
            try {
                sqlParametro.close();
                conexao.close();
                teste = true;
            } 
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return teste;
    }

    @Override
    public List<OfertaEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<OfertaEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OfertaEntity entidade = null;
        String sql;

        try {
            sql = "select * from ofertas where id > 0 order by datainicio desc;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OfertaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setProduto(new ProdutoDAO().pesquisar(resultado.getInt("produto")));
                entidade.setValor(resultado.getInt("valordesconto"));
                entidade.setDtinicio(resultado.getDate("datainicio"));
                entidade.setDtfim(resultado.getDate("datafim"));
                entidade.setTipo(resultado.getString("tipo"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public OfertaEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        OfertaEntity entidade = null;
        String sql;
        try {
            sql = "select * from ofertas where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new OfertaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setProduto(new ProdutoDAO().pesquisar(resultado.getInt("produto")));
                entidade.setValor(resultado.getInt("valordesconto"));
                entidade.setDtinicio(resultado.getDate("datainicio"));
                entidade.setDtfim(resultado.getDate("datafim"));
                entidade.setTipo(resultado.getString("tipo"));
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
            } 
            catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return entidade;
    }

    @Override
    public List<OfertaEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<OfertaEntity> lista = new ArrayList<OfertaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OfertaEntity entidade = null;

        try {
            sql = "select * from ofertas where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OfertaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setProduto(new ProdutoDAO().pesquisar(resultado.getInt("produto")));
                entidade.setValor(resultado.getInt("valordesconto"));
                entidade.setDtinicio(resultado.getDate("datainicio"));
                entidade.setDtfim(resultado.getDate("datafim"));
                entidade.setTipo(resultado.getString("tipo"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<OfertaEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<OfertaEntity> lista = new ArrayList<OfertaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OfertaEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OfertaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setProduto(new ProdutoDAO().pesquisar(resultado.getInt("produto")));
                entidade.setValor(resultado.getInt("valordesconto"));
                entidade.setDtinicio(resultado.getDate("datainicio"));
                entidade.setDtfim(resultado.getDate("datafim"));
                entidade.setTipo(resultado.getString("tipo"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<OfertaEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<OfertaEntity> lista = new ArrayList<OfertaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OfertaEntity entidade = null;
        String sql;

        try {
            sql = "select * from ofertas, produtos, categorias "
                    + "where ofertas.produto = produtos.id and produtos.categoria = categorias.id";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by datainicio desc;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OfertaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setProduto(new ProdutoDAO().pesquisar(resultado.getInt("produto")));
                entidade.setValor(resultado.getInt("valordesconto"));
                entidade.setDtinicio(resultado.getDate("datainicio"));
                entidade.setDtfim(resultado.getDate("datafim"));
                entidade.setTipo(resultado.getString("tipo"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<OfertaEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<OfertaEntity> lista = new ArrayList<OfertaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OfertaEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OfertaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setProduto(new ProdutoDAO().pesquisar(resultado.getInt("produto")));
                entidade.setValor(resultado.getInt("valordesconto"));
                entidade.setDtinicio(resultado.getDate("datainicio"));
                entidade.setDtfim(resultado.getDate("datafim"));
                entidade.setTipo(resultado.getString("tipo"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
