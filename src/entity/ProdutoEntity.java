package entity;

import java.awt.Image;

public class ProdutoEntity {

    private long id;
    private String nome;
    private double valor;
    private CategoriaEntity categoria;
    private String tipoVenda;
    private String descricao;
    private Image icone;
    private String estatus;

    public ProdutoEntity(String nome, double valor, CategoriaEntity categoria, String tipoVenda, String descricao, Image icone, String estatus) {
        this.nome = nome;
        this.valor = valor;
        this.categoria = categoria;
        this.tipoVenda = tipoVenda;
        this.descricao = descricao;
        this.icone = icone;
        this.estatus = estatus;
    }

    public ProdutoEntity() {
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public CategoriaEntity getCategoria() {
        return categoria;
    }

    public void setCategoria(CategoriaEntity categoria) {
        this.categoria = categoria;
    }

    public String getTipoVenda() {
        return tipoVenda;
    }

    public void setTipoVenda(String tipoVenda) {
        this.tipoVenda = tipoVenda;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Image getIcone() {
        return icone;
    }

    public void setIcone(Image icone) {
        this.icone = icone;
    }

    @Override
    public String toString() {
        return getNome();
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }
    
    public ProdutoEntity getClone(){
        ProdutoEntity p = new ProdutoEntity();
        p.setId(this.getId());
        p.setNome(this.getNome());
        p.setDescricao(this.getDescricao());
        p.setCategoria(this.getCategoria());
        p.setTipoVenda(this.getTipoVenda());
        p.setValor(this.getValor());
        p.setIcone(this.getIcone());
        p.setEstatus(this.getEstatus());
        return p;
    }
    
    public void setProduto(ProdutoEntity p){
        this.setId(p.getId());
        this.setNome(p.getNome());
        this.setCategoria(p.getCategoria());
        this.setDescricao(p.getDescricao());
        this.setTipoVenda(p.getTipoVenda());
        this.setValor(p.getValor());
        this.setEstatus(p.getEstatus());
        this.setIcone(p.getIcone());
    }
}
