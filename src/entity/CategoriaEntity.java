package entity;

public class CategoriaEntity {

    private long id;
    private String nome;
    private String descricao;

    public CategoriaEntity(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public CategoriaEntity() {
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

    @Override
    public String toString() {
        return getNome();
    }
    
    public CategoriaEntity getClone(){
        CategoriaEntity c = new CategoriaEntity();
        c.setId(this.getId());
        c.setNome(this.getNome());
        c.setDescricao(this.getDescricao());
        return c;
    }
    
    public void setCategoria(CategoriaEntity c){
        this.setId(c.getId());
        this.setNome(c.getNome());
        this.setDescricao(c.getDescricao());
    }
}
