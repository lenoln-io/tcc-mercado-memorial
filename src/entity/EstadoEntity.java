package entity;

public class EstadoEntity {

    private long id;
    private String nome;
    private String uf;

    public EstadoEntity() {
    }

    public EstadoEntity(String nome, String uf) {
        this.nome = nome;
        this.uf = uf;
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

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }
    
    @Override
    public String toString() {
        return getUf()+" - "+getNome();
    }
    
    public EstadoEntity getClone(){
        EstadoEntity e = new EstadoEntity();
        e.setId(this.getId());
        e.setNome(this.getNome());
        e.setUf(this.getUf());
        return e;
    }
    
    public void setEstado(EstadoEntity e){
        this.setId(e.getId());
        this.setNome(e.getNome());
        this.setUf(e.getUf());
    }
}
