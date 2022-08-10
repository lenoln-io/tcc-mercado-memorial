package entity;

public class CidadeEntity {

    private long id;
    private String nome;
    private EstadoEntity estado;

    public CidadeEntity() {
    }

    public CidadeEntity(String nome, EstadoEntity estado) {
        this.nome = nome;
        this.estado = estado;
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

    public EstadoEntity getEstado() {
        return estado;
    }

    public void setEstado(EstadoEntity estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        return getNome();
    }
    
    public CidadeEntity getClone(){
        CidadeEntity c = new CidadeEntity();
        c.setId(this.getId());
        c.setNome(this.getNome());
        c.setEstado(this.getEstado());
        return c;
    }
    
    public void setCidade(CidadeEntity c){
        this.setId(c.getId());
        this.setNome(c.getNome());
        this.setEstado(c.getEstado());
    }
}
