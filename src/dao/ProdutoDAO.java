package dao;

import config.Conexao;
import entity.ProdutoEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.ConverteImagem;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class ProdutoDAO implements ICrudDAO<ProdutoEntity>{

    @Override
    public ProdutoEntity salvar(ProdutoEntity entidade) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {

            if(entidade.getId()== 0){
                sql = "insert into produtos(nome, valor, categoria, tipovenda, descricao, icone, estatus) values(?,?,?,?,?,?,?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, entidade.getNome());
                sqlParametro.setDouble(2, entidade.getValor());
                sqlParametro.setLong(3, entidade.getCategoria().getId());
                sqlParametro.setString(4, entidade.getTipoVenda());
                sqlParametro.setString(5, entidade.getDescricao());
                sqlParametro.setBytes(6, ConverteImagem.imageToByte(entidade.getIcone()));
                sqlParametro.setString(7, entidade.getEstatus());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Codigo";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    entidade.setId(resultado.getLong("Codigo"));
                }
            }
            else{
                sql = "update produtos set nome = ?, valor = ?, categoria = ?, tipovenda = ?, descricao = ?, icone = ?, estatus = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, entidade.getNome());
                sqlParametro.setDouble(2, entidade.getValor());
                sqlParametro.setLong(3, entidade.getCategoria().getId());
                sqlParametro.setString(4, entidade.getTipoVenda());
                sqlParametro.setString(5, entidade.getDescricao());
                sqlParametro.setBytes(6, ConverteImagem.imageToByte(entidade.getIcone()));
                sqlParametro.setString(7, entidade.getEstatus());
                sqlParametro.setLong(8, entidade.getId());
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
        return entidade;
    }

    @Override
    public boolean excluir(ProdutoEntity entidade) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from produtos where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, entidade.getId());
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
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return teste;
    }

    @Override
    public List<ProdutoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<ProdutoEntity> lista = new ArrayList<ProdutoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ProdutoEntity entidade = null;
        String sql;
        try {
            sql = "select * from produtos where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ProdutoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setValor(resultado.getDouble("valor"));
                entidade.setCategoria(new CategoriaDAO().pesquisar(resultado.getLong("categoria")));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTipoVenda(resultado.getString("tipovenda"));
                entidade.setIcone(ConverteImagem.byteToImage(resultado.getBytes("icone")));
                entidade.setEstatus(resultado.getString("estatus"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public ProdutoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        ProdutoEntity entidade = null;
        String sql;
        try {
            sql = "select * from produtos where id = ? order by nome;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new ProdutoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setValor(resultado.getDouble("valor"));
                entidade.setCategoria(new CategoriaDAO().pesquisar(resultado.getLong("categoria")));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTipoVenda(resultado.getString("tipovenda"));
                entidade.setIcone(ConverteImagem.byteToImage(resultado.getBytes("icone")));
                entidade.setEstatus(resultado.getString("estatus"));
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
    public List<ProdutoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<ProdutoEntity> lista = new ArrayList<ProdutoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ProdutoEntity entidade = null;

        try {
            sql = "select * from produtos where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ProdutoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setValor(resultado.getDouble("valor"));
                entidade.setCategoria(new CategoriaDAO().pesquisar(resultado.getLong("categoria")));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTipoVenda(resultado.getString("tipovenda"));
                entidade.setIcone(ConverteImagem.byteToImage(resultado.getBytes("icone")));
                entidade.setEstatus(resultado.getString("estatus"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<ProdutoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<ProdutoEntity> lista = new ArrayList<ProdutoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ProdutoEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ProdutoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setValor(resultado.getDouble("valor"));
                entidade.setCategoria(new CategoriaDAO().pesquisar(resultado.getLong("categoria")));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTipoVenda(resultado.getString("tipovenda"));
                entidade.setIcone(ConverteImagem.byteToImage(resultado.getBytes("icone")));
                entidade.setEstatus(resultado.getString("estatus"));
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<ProdutoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<ProdutoEntity> lista = new ArrayList<ProdutoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ProdutoEntity entidade = null;
        String sql;
        try {
            sql = "select * from produtos, categorias where produtos.categoria = categorias.id";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by produtos.nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ProdutoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setValor(resultado.getDouble("valor"));
                entidade.setCategoria(new CategoriaDAO().pesquisar(resultado.getLong("categoria")));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTipoVenda(resultado.getString("tipovenda"));
                entidade.setIcone(ConverteImagem.byteToImage(resultado.getBytes("icone")));
                entidade.setEstatus(resultado.getString("estatus"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<ProdutoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<ProdutoEntity> lista = new ArrayList<ProdutoEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        ProdutoEntity entidade = null;
        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new ProdutoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                entidade.setValor(resultado.getDouble("valor"));
                entidade.setCategoria(new CategoriaDAO().pesquisar(resultado.getLong("categoria")));
                entidade.setDescricao(resultado.getString("descricao"));
                entidade.setTipoVenda(resultado.getString("tipovenda"));
                entidade.setIcone(ConverteImagem.byteToImage(resultado.getBytes("icone")));
                entidade.setEstatus(resultado.getString("estatus"));
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
