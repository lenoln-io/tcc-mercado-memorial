package entity;

import java.util.ArrayList;
import java.util.List;

public class OperacaoEntity {

    private long id;
    private String data;
    private String tipo;
    private UsuarioEntity usuario;
    private PessoaEntity pessoa;
    private List<OperacaoProdutosEntity> produtos;

    public OperacaoEntity() {
    }

    public OperacaoEntity(long id, String data, String tipo, UsuarioEntity usuario, PessoaEntity pessoa, List<OperacaoProdutosEntity> produtos) {
        this.id = id;
        this.data = data;
        this.tipo = tipo;
        this.usuario = usuario;
        this.pessoa = pessoa;
        this.produtos = produtos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<OperacaoProdutosEntity> getProdutos() {
        return produtos;
    }

    public void setProdutos(List<OperacaoProdutosEntity> produtos) {
        this.produtos = produtos;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public UsuarioEntity getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.usuario = usuario;
    }

    public PessoaEntity getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaEntity pessoa) {
        this.pessoa = pessoa;
    }
    
    public void addProduto(OperacaoProdutosEntity p){
        if(produtos == null)
            produtos = new ArrayList<>();
        if(p != null)
            produtos.add(p);
    }
    
    public void delProduto(int index){
        if(produtos != null){
            if(index >= 0 && index<produtos.size())
                produtos.remove(index);
        }
    }
    
    public OperacaoEntity getClone(){
        OperacaoEntity o = new OperacaoEntity();
        o.setData(this.getData());
        o.setId(this.getId());
        o.setPessoa(this.getPessoa());
        o.setUsuario(this.getUsuario());
        o.setProdutos(this.getProdutos());
        o.setTipo(o.getTipo());
        return o;
    }
    
}
