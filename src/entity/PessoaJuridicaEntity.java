package entity;

public class PessoaJuridicaEntity extends PessoaEntity {

    private String cnpj;

    public PessoaJuridicaEntity() {
        super();
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
    
    public PessoaEntity getPessoa(){
        PessoaEntity p = new PessoaEntity();
        p.setTelefones(super.getTelefones());
        p.setEnderecos(super.getEnderecos());
        p.setId(super.getId());
        p.setNome(super.getNome());
        return p;
    }
    
    public void setPessoa(PessoaEntity p){
        super.setId(p.getId());
        super.setNome(p.getNome());
        super.setEnderecos(p.getEnderecos());
        super.setTelefones(p.getTelefones());
    }
    
    public PessoaJuridicaEntity getClone(){
        PessoaJuridicaEntity p = new PessoaJuridicaEntity();
        p.setPessoa(this.getPessoa());
        p.setCnpj(cnpj);
        return p;
    }
    
    @Override
    public String toString() {
        return super.getNome();
    }
    
}
