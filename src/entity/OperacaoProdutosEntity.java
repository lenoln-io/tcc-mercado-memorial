package entity;

public class OperacaoProdutosEntity {

    private ProdutoEntity produto;
    private double qtd;
    private double valorUnitario;
    private String tipovenda;
    private int valorOferta;
    
    public OperacaoProdutosEntity(){
        
    }

    public OperacaoProdutosEntity(ProdutoEntity produto, double qtd, double valorUnitario, String tipovenda, int valorOferta) {
        this.produto = produto;
        this.qtd = qtd;
        this.valorUnitario = valorUnitario;
        this.tipovenda = tipovenda;
        this.valorOferta = valorOferta;
    }

    public ProdutoEntity getProduto() {
        return produto;
    }

    public void setProduto(ProdutoEntity produto) {
        this.produto = produto;
    }

    public double getQtd() {
        return qtd;
    }

    public void setQtd(double qtd) {
        this.qtd = qtd;
    }

    public double getValorUnitario() {
        return valorUnitario;
    }

    public void setValorUnitario(double valorUnitario) {
        this.valorUnitario = valorUnitario;
    }

    public String getTipovenda() {
        return tipovenda;
    }

    public void setTipovenda(String tipovenda) {
        this.tipovenda = tipovenda;
    }

    public int getValorOferta() {
        return valorOferta;
    }

    public void setValorOferta(int valorOferta) {
        this.valorOferta = valorOferta;
    }
    
}
