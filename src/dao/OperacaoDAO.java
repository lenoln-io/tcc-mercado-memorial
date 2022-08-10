package dao;

import config.Conexao;
import entity.OperacaoEntity;
import entity.OperacaoProdutosEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class OperacaoDAO implements ICrudDAO<OperacaoEntity>{

    @Override
    public OperacaoEntity salvar(OperacaoEntity entity) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(entity.getId() == 0){
                
                sql = "insert into operacao(data, tipo, idusuario, idpessoa) values(?, ?, ?, ?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, entity.getData());
                sqlParametro.setString(2, entity.getTipo());
                sqlParametro.setLong(3, entity.getUsuario().getId());
                sqlParametro.setLong(4, entity.getPessoa().getId());
                sqlParametro.executeUpdate();
                
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    entity.setId(resultado.getInt("Id"));
                }
                
                for(int x = 0 ; x < entity.getProdutos().size() ; x++){
                    sql = "insert into operacaoprodutos(operacao, produto, qtd, valorunitario, tipovenda, valoroferta) values(?, ?, ?, ?, ?, ?);";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(1, entity.getId());
                    sqlParametro.setLong(2, entity.getProdutos().get(x).getProduto().getId());
                    sqlParametro.setDouble(3, entity.getProdutos().get(x).getQtd());
                    sqlParametro.setDouble(4, entity.getProdutos().get(x).getValorUnitario());
                    sqlParametro.setString(5, entity.getProdutos().get(x).getTipovenda());
                    sqlParametro.setInt(6, entity.getProdutos().get(x).getValorOferta());
                    sqlParametro.executeUpdate();
                }
                
            }else{
                
                sql = "delete from operacaoprodutos where operacao = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, entity.getId());
                sqlParametro.executeUpdate();
                
                sql = "update operacao set data = ?, tipo = ?, idusuario = ?, idpessoa = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, entity.getData());
                sqlParametro.setString(2, entity.getTipo());
                sqlParametro.setLong(3, entity.getUsuario().getId());
                sqlParametro.setLong(4, entity.getPessoa().getId());
                sqlParametro.setLong(5, entity.getId());
                sqlParametro.executeUpdate();
                
                for(int x = 0 ; x < entity.getProdutos().size() ; x++){
                    sql = "insert into operacaoprodutos(operacao, produto, qtd, valorunitario, tipovenda, valoroferta) values(?, ?, ?, ?, ?, ?);";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(1, entity.getId());
                    sqlParametro.setLong(2, entity.getProdutos().get(x).getProduto().getId());
                    sqlParametro.setDouble(3, entity.getProdutos().get(x).getQtd());
                    sqlParametro.setDouble(4, entity.getProdutos().get(x).getValorUnitario());
                    sqlParametro.setString(5, entity.getProdutos().get(x).getTipovenda());
                    sqlParametro.setInt(6, entity.getProdutos().get(x).getValorOferta());
                    sqlParametro.executeUpdate();
                }
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
        return entity;
    }

    @Override
    public boolean excluir(OperacaoEntity entity) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            sql = "delete from operacaoprodutos where operacao = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, entity.getId());
            sqlParametro.executeUpdate();
            
            sql = "delete from operacao where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, entity.getId());
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
    public List<OperacaoEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<OperacaoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OperacaoEntity entidade = null;
        String sql;
        PreparedStatement sqlParametro2 = null;
        ResultSet resultado2 = null;
        String sql2;

        try {
            sql = "select * from operacao where id > 0 order by data;";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OperacaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setData(resultado.getString("data"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("idusuario")));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("idpessoa")));
                
                sql2 = "select * from operacaoprodutos where operacao = ?;";
                sqlParametro2 = conexao.prepareStatement(sql2);
                sqlParametro2.setLong(1, entidade.getId());
                resultado2 = sqlParametro2.executeQuery();
                while (resultado2.next()) {
                    OperacaoProdutosEntity produto = new OperacaoProdutosEntity();
                    produto.setProduto(new ProdutoDAO().pesquisar(resultado2.getLong("produto")));
                    produto.setQtd(resultado2.getDouble("qtd"));
                    produto.setValorUnitario(resultado2.getDouble("valorunitario"));
                    produto.setTipovenda(resultado2.getString("tipovenda"));
                    produto.setValorOferta(resultado2.getInt("valoroferta"));
                    entidade.addProduto(produto);
                }
                
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public OperacaoEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        OperacaoEntity entidade = null;
        String sql;
        PreparedStatement sqlParametro2 = null;
        ResultSet resultado2 = null;
        String sql2;
        
        try {
            sql = "select * from operacao where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                entidade = new OperacaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setData(resultado.getString("data"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("idusuario")));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("idpessoa")));
                
                sql2 = "select * from operacaoprodutos where operacao = ?;";
                sqlParametro2 = conexao.prepareStatement(sql2);
                sqlParametro2.setLong(1, entidade.getId());
                resultado2 = sqlParametro2.executeQuery();
                while (resultado2.next()) {
                    OperacaoProdutosEntity produto = new OperacaoProdutosEntity();
                    produto.setProduto(new ProdutoDAO().pesquisar(resultado2.getLong("produto")));
                    produto.setQtd(resultado2.getDouble("qtd"));
                    produto.setValorUnitario(resultado2.getDouble("valorunitario"));
                    produto.setTipovenda(resultado2.getString("tipovenda"));
                    produto.setValorOferta(resultado2.getInt("valoroferta"));
                    entidade.addProduto(produto);
                }
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
    public List<OperacaoEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<OperacaoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OperacaoEntity entidade = null;
        PreparedStatement sqlParametro2 = null;
        ResultSet resultado2 = null;
        String sql2;

        try {
            sql = "select * from operacao where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OperacaoEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setData(resultado.getString("data"));
                entidade.setTipo(resultado.getString("tipo"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("idusuario")));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("idpessoa")));
                
                sql2 = "select * from operacaoprodutos where operacao = ?;";
                sqlParametro2 = conexao.prepareStatement(sql2);
                sqlParametro2.setLong(1, entidade.getId());
                resultado2 = sqlParametro2.executeQuery();
                while (resultado2.next()) {
                    OperacaoProdutosEntity produto = new OperacaoProdutosEntity();
                    produto.setProduto(new ProdutoDAO().pesquisar(resultado2.getLong("produto")));
                    produto.setQtd(resultado2.getDouble("qtd"));
                    produto.setValorUnitario(resultado2.getDouble("valorunitario"));
                    produto.setTipovenda(resultado2.getString("tipovenda"));
                    produto.setValorOferta(resultado2.getInt("valoroferta"));
                    entidade.addProduto(produto);
                }
                
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<OperacaoEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<OperacaoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OperacaoEntity entidade = null;
        String sql;
        PreparedStatement sqlParametro2 = null;
        ResultSet resultado2 = null;
        String sql2;

        try {
            sql = "select * from operacao as O, usuarios as U, pessoas as PU , pessoas as PP "
                + "where O.idpessoa = PP.id and O.idusuario = U.id and U.pessoa = PU.id";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by O.data desc;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OperacaoEntity();
                entidade.setId(resultado.getInt("O.id"));
                entidade.setData(resultado.getString("O.data"));
                entidade.setTipo(resultado.getString("O.tipo"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("O.idusuario")));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("O.idpessoa")));
                
                sql2 = "select * from operacaoprodutos where operacao = ?;";
                sqlParametro2 = conexao.prepareStatement(sql2);
                sqlParametro2.setLong(1, entidade.getId());
                resultado2 = sqlParametro2.executeQuery();
                while (resultado2.next()) {
                    OperacaoProdutosEntity produto = new OperacaoProdutosEntity();
                    produto.setProduto(new ProdutoDAO().pesquisar(resultado2.getLong("produto")));
                    produto.setQtd(resultado2.getDouble("qtd"));
                    produto.setValorUnitario(resultado2.getDouble("valorunitario"));
                    produto.setTipovenda(resultado2.getString("tipovenda"));
                    produto.setValorOferta(resultado2.getInt("valoroferta"));
                    entidade.addProduto(produto);
                }
                
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<OperacaoEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<OperacaoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OperacaoEntity entidade = null;
        PreparedStatement sqlParametro2 = null;
        ResultSet resultado2 = null;
        String sql2;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OperacaoEntity();
                entidade.setId(resultado.getInt("O.id"));
                entidade.setData(resultado.getString("O.data"));
                entidade.setTipo(resultado.getString("O.tipo"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("O.idusuario")));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("O.idpessoa")));
                
                sql2 = "select * from operacaoprodutos where operacao = ?;";
                sqlParametro2 = conexao.prepareStatement(sql2);
                sqlParametro2.setLong(1, entidade.getId());
                resultado2 = sqlParametro2.executeQuery();
                while (resultado2.next()) {
                    OperacaoProdutosEntity produto = new OperacaoProdutosEntity();
                    produto.setProduto(new ProdutoDAO().pesquisar(resultado2.getLong("produto")));
                    produto.setQtd(resultado2.getDouble("qtd"));
                    produto.setValorUnitario(resultado2.getDouble("valorunitario"));
                    produto.setTipovenda(resultado2.getString("tipovenda"));
                    produto.setValorOferta(resultado2.getInt("valoroferta"));
                    entidade.addProduto(produto);
                }
                
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<OperacaoEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<OperacaoEntity> lista = new ArrayList<>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        OperacaoEntity entidade = null;
        PreparedStatement sqlParametro2 = null;
        ResultSet resultado2 = null;
        String sql2;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new OperacaoEntity();
                entidade.setId(resultado.getInt("O.id"));
                entidade.setData(resultado.getString("O.data"));
                entidade.setTipo(resultado.getString("O.tipo"));
                entidade.setUsuario(new UsuarioDAO().pesquisar(resultado.getLong("O.idusuario")));
                entidade.setPessoa(new PessoaDAO().pesquisar(resultado.getLong("O.idpessoa")));
                
                sql2 = "select * from operacaoprodutos where operacao = ?;";
                sqlParametro2 = conexao.prepareStatement(sql2);
                sqlParametro2.setLong(1, entidade.getId());
                resultado2 = sqlParametro2.executeQuery();
                while (resultado2.next()) {
                    OperacaoProdutosEntity produto = new OperacaoProdutosEntity();
                    produto.setProduto(new ProdutoDAO().pesquisar(resultado2.getLong("produto")));
                    produto.setQtd(resultado2.getDouble("qtd"));
                    produto.setValorUnitario(resultado2.getDouble("valorunitario"));
                    produto.setTipovenda(resultado2.getString("tipovenda"));
                    produto.setValorOferta(resultado2.getInt("valoroferta"));
                    entidade.addProduto(produto);
                }
                
                lista.add(entidade);
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
