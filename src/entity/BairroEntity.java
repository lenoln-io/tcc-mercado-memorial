package entity;

public class BairroEntity {

    private long id;
    private String nome;
    private CidadeEntity cidade;

    public BairroEntity() {
    }

    public BairroEntity(String nome, CidadeEntity cidade) {
        this.nome = nome;
        this.cidade = cidade;
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

    public CidadeEntity getCidade() {
        return cidade;
    }

    public void setCidade(CidadeEntity cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return getNome();
    }
    
    public BairroEntity getClone(){
        BairroEntity e = new BairroEntity();
        e.setId(this.getId());
        e.setNome(this.getNome());
        e.setCidade(this.getCidade());
        return e;
    }
    
    public void setBairro(BairroEntity e){
        this.setId(e.getId());
        this.setNome(e.getNome());
        this.setCidade(e.getCidade());
    }
}
