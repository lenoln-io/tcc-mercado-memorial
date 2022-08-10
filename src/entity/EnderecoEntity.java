package entity;

public class EnderecoEntity {

    private BairroEntity bairro;
    private String nomeRua;
    private int numCasa;

    public EnderecoEntity() {
        
    }

    public EnderecoEntity(BairroEntity bairro, String nomeRua, int numCasa) {
        this.bairro = bairro;
        this.nomeRua = nomeRua;
        this.numCasa = numCasa;
    }

    public BairroEntity getBairro() {
        return bairro;
    }

    public void setBairro(BairroEntity bairro) {
        this.bairro = bairro;
    }

    public String getNomeRua() {
        return nomeRua;
    }

    public void setNomeRua(String nomeRua) {
        this.nomeRua = nomeRua;
    }

    public int getNumCasa() {
        return numCasa;
    }

    public void setNumCasa(int numCasa) {
        this.numCasa = numCasa;
    }
    
    public EnderecoEntity getClone(){
        EnderecoEntity e = new EnderecoEntity();
        e.setBairro(this.getBairro());
        e.setNomeRua(this.getNomeRua());
        e.setNumCasa(this.getNumCasa());
        return e;
    }
    
}
