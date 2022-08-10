package dao;

import config.Conexao;
import entity.EnderecoEntity;
import entity.PessoaEntity;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utility.CriterioFiltro;
import utility.GeraFiltro;

public class PessoaDAO implements ICrudDAO<PessoaEntity> {
    
    @Override
    public PessoaEntity salvar(PessoaEntity pessoa) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        String sql;
        try {
            if(pessoa.getId() == 0){
                
                sql = "insert into pessoas(nome) values(?);";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setString(1, pessoa.getNome());
                sqlParametro.executeUpdate();
                sql = "select last_insert_id() as Id";
                sqlParametro = conexao.prepareStatement(sql);
                resultado = sqlParametro.executeQuery();
                if (resultado.next()) {
                    pessoa.setId(resultado.getInt("Id"));
                }
                
                for(int x = 0 ; x < pessoa.getEnderecos().size() ; x++){
                    sql = "insert into pessoaenderecos(pessoa,bairro,nomerua,numcasa) values(?,?,?,?);";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(1, pessoa.getId());
                    sqlParametro.setLong(2, pessoa.getEnderecos().get(x).getBairro().getId());
                    sqlParametro.setString(3, pessoa.getEnderecos().get(x).getNomeRua());
                    sqlParametro.setInt(4, pessoa.getEnderecos().get(x).getNumCasa());
                    sqlParametro.executeUpdate();
                }
                
                for(int x = 0 ; x < pessoa.getTelefones().size() ; x++){
                    sql = "insert into pessoatelefone(pessoa,telefone) values(?,?);";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(1, pessoa.getId());
                    sqlParametro.setString(2, pessoa.getTelefones().get(x));
                    sqlParametro.executeUpdate();
                }
                
            }
            else{
                
                sql = "delete from pessoaenderecos where pessoa = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, pessoa.getId());
                sqlParametro.executeUpdate();
                
                sql = "delete from pessoatelefone where pessoa = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(1, pessoa.getId());
                sqlParametro.executeUpdate();
                
                sql = "update pessoas set nome = ? where id = ?;";
                sqlParametro = conexao.prepareStatement(sql);
                sqlParametro.setLong(2, pessoa.getId());
                sqlParametro.setString(1, pessoa.getNome());
                sqlParametro.executeUpdate();
                
                for(int x = 0 ; x < pessoa.getEnderecos().size() ; x++){
                    sql = "insert into pessoaenderecos(pessoa,bairro,nomerua,numcasa) values(?,?,?,?);";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(1, pessoa.getId());
                    sqlParametro.setLong(2, pessoa.getEnderecos().get(x).getBairro().getId());
                    sqlParametro.setString(3, pessoa.getEnderecos().get(x).getNomeRua());
                    sqlParametro.setInt(4, pessoa.getEnderecos().get(x).getNumCasa());
                    sqlParametro.executeUpdate();
                }
                
                for(int x = 0 ; x < pessoa.getTelefones().size() ; x++){
                    sql = "insert into pessoatelefone(pessoa,telefone) values(?,?);";
                    sqlParametro = conexao.prepareStatement(sql);
                    sqlParametro.setLong(1, pessoa.getId());
                    sqlParametro.setString(2, pessoa.getTelefones().get(x));
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
        return pessoa;
    }

    @Override
    public boolean excluir(PessoaEntity pessoa) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement sqlParametro = null;
        String sql;
        boolean teste = false;
        try {
            
            sql = "delete from pessoaenderecos where pessoa = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, pessoa.getId());
            sqlParametro.executeUpdate();
            
            sql = "delete from pessoatelefone where pessoa = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, pessoa.getId());
            sqlParametro.executeUpdate();
            
            sql = "delete from pessoas where id = ?;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setLong(1, pessoa.getId());
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
    public List<PessoaEntity> listarTodos() {
        Connection conexao = new Conexao().getConexao();
        List<PessoaEntity> lista = new ArrayList<PessoaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaEntity entidade = null;
        String sql;

        try {
            sql = "select * from pessoas where id > 0 order by nome;";
            sqlParametro = conexao.prepareStatement(sql);
            sqlParametro.setFetchSize(20);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                entidade = new PessoaEntity();
                entidade.setId(resultado.getLong("id"));
                entidade.setNome(resultado.getString("nome"));
                
                String sqle = "select * from pessoaenderecos where pessoa = ?;";
                PreparedStatement sqlParametroe = conexao.prepareStatement(sqle);
                sqlParametroe.setLong(1, entidade.getId());
                ResultSet resultadoe = sqlParametroe.executeQuery();
                while (resultadoe.next()) {
                    EnderecoEntity e = new EnderecoEntity();
                    e.setBairro(new BairroDAO().pesquisar(resultadoe.getLong("bairro")));
                    e.setNomeRua(resultadoe.getString("nomerua"));
                    e.setNumCasa(resultadoe.getInt("numcasa"));
                    entidade.addEndereco(e);
                }
                
                sqlParametroe.close();
                resultadoe.close();
                
                String sqlt = "select * from pessoatelefone where pessoa = ?;";
                PreparedStatement sqlParametrot = conexao.prepareStatement(sqlt);
                sqlParametrot.setLong(1, entidade.getId());
                ResultSet resultadot = sqlParametrot.executeQuery();
                while (resultadot.next()) {
                    entidade.addTel(resultadot.getString("telefone"));
                }
                
                sqlParametrot.close();
                resultadot.close();
                
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
    public PessoaEntity pesquisar(long id) {
        Connection conexao = new Conexao().getConexao();
        PreparedStatement consulta = null;
        ResultSet resultado = null;
        PessoaEntity entidade = null;
        String sql;
        try {
            sql = "select * from pessoas where id = ?;";
            consulta = conexao.prepareStatement(sql);
            consulta.setLong(1, id);
            resultado = consulta.executeQuery();
            if (resultado.next()) {
                
                entidade = new PessoaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                
                String sqle = "select * from pessoaenderecos where pessoa = ?;";
                PreparedStatement sqlParametroe = conexao.prepareStatement(sqle);
                sqlParametroe.setLong(1, entidade.getId());
                ResultSet resultadoe = sqlParametroe.executeQuery();
                while (resultadoe.next()) {
                    EnderecoEntity e = new EnderecoEntity();
                    e.setBairro(new BairroDAO().pesquisar(resultadoe.getLong("bairro")));
                    e.setNomeRua(resultadoe.getString("nomerua"));
                    e.setNumCasa(resultadoe.getInt("numcasa"));
                    entidade.addEndereco(e);
                }
                
                String sqlt = "select * from pessoatelefone where pessoa = ?;";
                PreparedStatement sqlParametrot = conexao.prepareStatement(sqlt);
                sqlParametrot.setLong(1, entidade.getId());
                ResultSet resultadot = sqlParametrot.executeQuery();
                while (resultadot.next()) {
                    entidade.addTel(resultadot.getString("telefone"));
                }
                
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
    public List<PessoaEntity> pesquisaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaEntity> lista = new ArrayList<PessoaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaEntity entidade = null;

        try {
            sql = "select * from pessoas where " + sql + ";";
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                
                entidade = new PessoaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                
                String sqle = "select * from pessoaenderecos where pessoa = ?;";
                PreparedStatement sqlParametroe = conexao.prepareStatement(sqle);
                sqlParametroe.setLong(1, entidade.getId());
                ResultSet resultadoe = sqlParametroe.executeQuery();
                while (resultadoe.next()) {
                    EnderecoEntity e = new EnderecoEntity();
                    e.setBairro(new BairroDAO().pesquisar(resultadoe.getLong("bairro")));
                    e.setNomeRua(resultadoe.getString("nomerua"));
                    e.setNumCasa(resultadoe.getInt("numcasa"));
                    entidade.addEndereco(e);
                }
                
                String sqlt = "select * from pessoatelefone where pessoa = ?;";
                PreparedStatement sqlParametrot = conexao.prepareStatement(sqlt);
                sqlParametrot.setLong(1, entidade.getId());
                ResultSet resultadot = sqlParametrot.executeQuery();
                while (resultadot.next()) {
                    entidade.addTel(resultadot.getString("telefone"));
                }
                
                lista.add(entidade);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<PessoaEntity> sqlDeConsultaPersonalizada(String sql) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaEntity> lista = new ArrayList<PessoaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaEntity entidade = null;

        try {
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                
                entidade = new PessoaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                
                String sqle = "select * from pessoaenderecos where pessoa = ?;";
                PreparedStatement sqlParametroe = conexao.prepareStatement(sqle);
                sqlParametroe.setLong(1, entidade.getId());
                ResultSet resultadoe = sqlParametroe.executeQuery();
                while (resultadoe.next()) {
                    EnderecoEntity e = new EnderecoEntity();
                    e.setBairro(new BairroDAO().pesquisar(resultadoe.getLong("bairro")));
                    e.setNomeRua(resultadoe.getString("nomerua"));
                    e.setNumCasa(resultadoe.getInt("numcasa"));
                    entidade.addEndereco(e);
                }
                
                String sqlt = "select * from pessoatelefone where pessoa = ?;";
                PreparedStatement sqlParametrot = conexao.prepareStatement(sqlt);
                sqlParametrot.setLong(1, entidade.getId());
                ResultSet resultadot = sqlParametrot.executeQuery();
                while (resultadot.next()) {
                    entidade.addTel(resultadot.getString("telefone"));
                }
                
                lista.add(entidade);
                
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

    @Override
    public List<PessoaEntity> filtrar(CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaEntity> lista = new ArrayList<PessoaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaEntity entidade = null;
        String sql;

        try {
            sql = "select * from pessoas";
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql + "order by nome;");
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                
                entidade = new PessoaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                
                String sqle = "select * from pessoaenderecos where pessoa = ?;";
                PreparedStatement sqlParametroe = conexao.prepareStatement(sqle);
                sqlParametroe.setLong(1, entidade.getId());
                ResultSet resultadoe = sqlParametroe.executeQuery();
                while (resultadoe.next()) {
                    EnderecoEntity e = new EnderecoEntity();
                    e.setBairro(new BairroDAO().pesquisar(resultadoe.getLong("bairro")));
                    e.setNomeRua(resultadoe.getString("nomerua"));
                    e.setNumCasa(resultadoe.getInt("numcasa"));
                    entidade.addEndereco(e);
                }
                
                String sqlt = "select * from pessoatelefone where pessoa = ?;";
                PreparedStatement sqlParametrot = conexao.prepareStatement(sqlt);
                sqlParametrot.setLong(1, entidade.getId());
                ResultSet resultadot = sqlParametrot.executeQuery();
                while (resultadot.next()) {
                    entidade.addTel(resultadot.getString("telefone"));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }
    
    @Override
    public List<PessoaEntity> filtrar(String sql, CriterioFiltro criterio) {
        Connection conexao = new Conexao().getConexao();
        List<PessoaEntity> lista = new ArrayList<PessoaEntity>();
        PreparedStatement sqlParametro = null;
        ResultSet resultado = null;
        PessoaEntity entidade = null;

        try {
            sql = GeraFiltro.gerarSentenca(sql, criterio);
            sqlParametro = conexao.prepareStatement(sql);
            resultado = sqlParametro.executeQuery();
            while (resultado.next()) {
                
                entidade = new PessoaEntity();
                entidade.setId(resultado.getInt("id"));
                entidade.setNome(resultado.getString("nome"));
                
                String sqle = "select * from pessoaenderecos where pessoa = ?;";
                PreparedStatement sqlParametroe = conexao.prepareStatement(sqle);
                sqlParametroe.setLong(1, entidade.getId());
                ResultSet resultadoe = sqlParametroe.executeQuery();
                while (resultadoe.next()) {
                    EnderecoEntity e = new EnderecoEntity();
                    e.setBairro(new BairroDAO().pesquisar(resultadoe.getLong("bairro")));
                    e.setNomeRua(resultadoe.getString("nomerua"));
                    e.setNumCasa(resultadoe.getInt("numcasa"));
                    entidade.addEndereco(e);
                }
                
                String sqlt = "select * from pessoatelefone where pessoa = ?;";
                PreparedStatement sqlParametrot = conexao.prepareStatement(sqlt);
                sqlParametrot.setLong(1, entidade.getId());
                ResultSet resultadot = sqlParametrot.executeQuery();
                while (resultadot.next()) {
                    entidade.addTel(resultadot.getString("telefone"));
                }
                
                lista.add(entidade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lista;
    }

}
