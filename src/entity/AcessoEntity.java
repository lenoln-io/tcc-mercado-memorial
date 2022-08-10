package entity;

public class AcessoEntity {

    private long id;
    private String nome;
    private String descricao;
    private String tiposistema;
    private String recurso;

    public AcessoEntity() {
    }

    public AcessoEntity(long id, String nome, String descricao, String recurso, String tiposistema) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.tiposistema = tiposistema;
        this.recurso = recurso;
    }

    public AcessoEntity(String nome, String descricao, String recurso, String tiposistema) {
        this.nome = nome;
        this.descricao = descricao;
        this.tiposistema = tiposistema;
        this.recurso = recurso;
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

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTiposistema() {
        return tiposistema;
    }

    public void setTiposistema(String tiposistema) {
        this.tiposistema = tiposistema;
    }

    public String getRecurso() {
        return recurso;
    }

    public void setRecurso(String recurso) {
        this.recurso = recurso;
    }

    @Override
    public String toString() {
        return getNome() + " - " + getTiposistema();
    }
    
}
