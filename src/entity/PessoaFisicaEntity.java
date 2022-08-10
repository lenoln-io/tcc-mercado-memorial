package entity;

import java.sql.Date;

public class PessoaFisicaEntity extends PessoaEntity {

    private String sexo;
    private String cpf;
    private Date dataNascimento;

    public PessoaFisicaEntity() {
        super();
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
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
    
    public PessoaFisicaEntity getClone(){
        PessoaFisicaEntity p = new PessoaFisicaEntity();
        p.setPessoa(this.getPessoa());
        p.setCpf(this.getCpf());
        p.setSexo(this.getSexo());
        p.setDataNascimento(this.getDataNascimento());
        return p;
    }

    @Override
    public String toString() {
        return super.getNome();
    }
    
}
