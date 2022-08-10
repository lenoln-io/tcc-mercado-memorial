package entity;

import java.util.ArrayList;
import java.util.List;

public class GrupoEntity {

    private long id;
    private String nome;
    private List<AcessoEntity> acessos;
    private String descricao;
    private String loginweb;
    private String logindesktop;

    public String getLoginweb() {
        return loginweb;
    }

    public void setLoginweb(String loginweb) {
        this.loginweb = loginweb;
    }

    public String getLogindesktop() {
        return logindesktop;
    }

    public void setLogindesktop(String logindesktop) {
        this.logindesktop = logindesktop;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public GrupoEntity() {
        
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
    
    public GrupoEntity getClone(){
        GrupoEntity g = new GrupoEntity();
        g.setId(this.getId());
        g.setNome(this.getNome());
        g.setAcessos(this.getAcessos());
        g.setDescricao(this.getDescricao());
        g.setLogindesktop(this.getLogindesktop());
        g.setLoginweb(this.getLoginweb());
        return g;
    }
    
    public void setGrupo(GrupoEntity g){
        this.setId(g.getId());
        this.setNome(g.getNome());
        this.setAcessos(g.getAcessos());
        this.setDescricao(g.getDescricao());
        this.setLogindesktop(g.getLogindesktop());
        this.setLoginweb(g.getLoginweb());
    }
    
    public List<AcessoEntity> getAcessos() {
        return acessos;
    }

    public void setAcessos(List<AcessoEntity> acessos) {
        this.acessos = acessos;
    }
    
    public void clearAcessos(){
        if(acessos != null)
            this.acessos.clear();
    }
    
    public void addAcesso(AcessoEntity a){
        if(this.acessos == null || this.acessos.isEmpty()){
            this.acessos = new ArrayList<>();
        }
        this.acessos.add(a);
    }
    
    public void removeAcesso(AcessoEntity a){
        if(this.acessos != null && !this.acessos.isEmpty()){
            for(int x = 0 ; x<this.acessos.size() ; x++){
                if(this.acessos.get(x).getId() == a.getId()){
                    this.acessos.remove(x);
                    break;
                }
            }
        }
    }

    @Override
    public String toString() {
        return getNome();
    }
    
}
