package entity;

import java.sql.Date;

public class UsuarioEntity {

    private long id;
    private PessoaEntity pessoa;
    private String email;
    private String senha;
    private String ultimoLogout;
    private String ultimoLogin;
    private String estatus;
    private GrupoEntity grupo;

    public UsuarioEntity() {
        
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public PessoaEntity getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaEntity pessoa) {
        this.pessoa = pessoa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getUltimoLogout() {
        return ultimoLogout;
    }

    public void setUltimoLogout(String ultimoLogout) {
        this.ultimoLogout = ultimoLogout;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public GrupoEntity getGrupo() {
        return grupo;
    }

    public void setGrupo(GrupoEntity grupo) {
        this.grupo = grupo;
    }
    
    public UsuarioEntity getClone(){
        UsuarioEntity u = new UsuarioEntity();
        u.setId(this.getId());
        u.setPessoa(this.getPessoa());
        u.setEmail(this.getEmail());
        u.setSenha(this.getSenha());
        u.setGrupo(this.getGrupo());
        u.setEstatus(this.getEstatus());
        u.setUltimoLogout(this.getUltimoLogout());
        u.setUltimoLogin(this.getUltimoLogin());
        return u;
    }

    public String getUltimoLogin() {
        return ultimoLogin;
    }

    public void setUltimoLogin(String ultimoLogin) {
        this.ultimoLogin = ultimoLogin;
    }
    
}
