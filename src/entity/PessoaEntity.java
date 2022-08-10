package entity;

import java.util.ArrayList;
import java.util.List;

public class PessoaEntity {

    private long id;
    private String nome;
    private List<EnderecoEntity> enderecos;
    private List<String> telefones;

    public PessoaEntity() {
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getTelefones() {
        return telefones;
    }

    public void setTelefones(List<String> telefones) {
        this.telefones = telefones;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<EnderecoEntity> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<EnderecoEntity> enderecos) {
        this.enderecos = enderecos;
    }
    
    public void addEndereco(EnderecoEntity e){
        if(enderecos == null)
            enderecos = new ArrayList<>();
        enderecos.add(e);
    }
    
    public void delEndereco(EnderecoEntity e){
        for(int x = 0 ; x < enderecos.size() ; x++){
            if(e.getBairro().getId() == enderecos.get(x).getBairro().getId() &&
                    e.getNomeRua().equals(enderecos.get(x).getNomeRua()) &&
                    e.getNumCasa() == enderecos.get(x).getNumCasa()){
                enderecos.remove(x);
            }
        }
    }
    
    public void addTel(String tel){
        if(telefones == null)
            telefones = new ArrayList<>();
        telefones.add(tel);
    }
    
    public void delTel(String tel){
        for(int x = 0 ; x <telefones.size() ; x++){
            if(telefones.get(x).equals(tel)){
                telefones.remove(x);
            }
        }
    }
    
}
