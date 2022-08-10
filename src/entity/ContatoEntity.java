package entity;

import java.sql.Date;

public class ContatoEntity {

    private long id;
    private String nome;
    private String email;
    private String tipo;
    private String mensagem;
    private Date data;
    private String estatus;

    public ContatoEntity(long id, String nome, String email, String tipo, String mensagem, Date data, String estatus) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.tipo = tipo;
        this.mensagem = mensagem;
        this.data = data;
        this.estatus = estatus;
    }

    public ContatoEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return getNome();
    }
    
    public ContatoEntity getClone(){
        ContatoEntity c = new ContatoEntity();
        c.setId(this.getId());
        c.setNome(this.getNome());
        c.setEmail(this.getEmail());
        c.setTipo(this.getTipo());
        c.setMensagem(this.getMensagem());
        c.setData(this.getData());
        c.setEstatus(this.getEstatus());
        return c;
    }
    
    public void setContato(ContatoEntity c){
        this.setId(c.getId());
        this.setNome(c.getNome());
        this.setEmail(c.getEmail());
        this.setTipo(c.getTipo());
        this.setMensagem(c.getMensagem());
        this.setData(c.getData());
        this.setEstatus(c.getEstatus());
    }
}
