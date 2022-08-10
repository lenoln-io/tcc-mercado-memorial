package entity;

import java.sql.Date;

public class OfertaEntity {

    private long id;
    private ProdutoEntity produto;
    private int valor;
    private Date dtinicio;
    private Date dtfim;
    private String tipo;

    public OfertaEntity() {
    }

    public OfertaEntity(long id, ProdutoEntity produto, int valor, Date dtinicio, Date dtfim, String tipo) {
        this.id = id;
        this.produto = produto;
        this.valor = valor;
        this.dtinicio = dtinicio;
        this.dtfim = dtfim;
        this.tipo = tipo;
    }

    public OfertaEntity(ProdutoEntity produto, int valor, Date dtinicio, Date dtfim, String tipo) {
        this.produto = produto;
        this.valor = valor;
        this.dtinicio = dtinicio;
        this.dtfim = dtfim;
        this.tipo = tipo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ProdutoEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutoEntity produto) {
        this.produto = produto;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public Date getDtinicio() {
        return dtinicio;
    }

    public void setDtinicio(Date dtinicio) {
        this.dtinicio = dtinicio;
    }

    public Date getDtfim() {
        return dtfim;
    }

    public void setDtfim(Date dtfim) {
        this.dtfim = dtfim;
    }

    @Override
    public String toString() {
        return getProduto().getNome();
    }
    
    public OfertaEntity getClone(){
        OfertaEntity o = new OfertaEntity();
        o.setId(this.getId());
        o.setProduto(this.getProduto());
        o.setValor(this.getValor());
        o.setDtinicio(this.getDtinicio());
        o.setDtfim(this.getDtfim());
        o.setTipo(this.getTipo());
        return o;
    }
    
    public void setOferta(OfertaEntity o){
        this.setId(o.getId());
        this.setProduto(o.getProduto());
        this.setValor(o.getValor());
        this.setDtinicio(o.getDtinicio());
        this.setDtfim(o.getDtfim());
        this.setTipo(o.getTipo());
    }
}
