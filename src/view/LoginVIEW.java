package view;

import bll.AcaoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.DateFormat;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JOptionPane;
import utility.DataUtility;
import utility.EmailValidator;
import utility.Login;


public class LoginVIEW extends javax.swing.JFrame implements IVIEW {

    private javax.swing.Timer timer;
    ActionListener rel;
    
    public LoginVIEW() {
        initComponents();
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        desabilitaBtFecharEAtalhos();
        rel = new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                Date hora = new Date();
                SimpleDateFormat hora_formato = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                lbHorario.setText(hora_formato.format(hora));
            }
        };
        disparaRelogio();
    }
    
    public final void disparaRelogio() {
        if (timer == null) {
            timer = new javax.swing.Timer(1000, rel);
            timer.setInitialDelay(0);
            timer.start();
        } else if (!timer.isRunning()) {
            timer.restart();
        }
    }
    
    public final void desabilitaBtFecharEAtalhos(){
        this.addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent evt) {
                acaoFechar();
            }  
        });
    }
    
    public void acaoFechar(){
                    
        int temp = JOptionPane.showConfirmDialog(null,"Quer mesmo sair?", "CONFIRMAR",JOptionPane.YES_NO_OPTION);

        if(temp == JOptionPane.YES_OPTION){
            System.exit(0);
        }
        
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        txtSenha = new javax.swing.JPasswordField();
        jLabel2 = new javax.swing.JLabel();
        btLogin = new javax.swing.JButton();
        btLimpar = new javax.swing.JButton();
        lbHorario = new javax.swing.JLabel();
        btFechar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel1.setText("Login");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Senha:");

        jLabel2.setText("Email:");

        btLogin.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/laptop_start.png"))); // NOI18N
        btLogin.setText("Login");
        btLogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLoginActionPerformed(evt);
            }
        });

        btLimpar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_paint.png"))); // NOI18N
        btLimpar.setText("Limpar");
        btLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimparActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtEmail)
                    .add(txtSenha)
                    .add(btLogin, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(btLimpar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel1Layout.createSequentialGroup()
                        .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(jLabel3))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btLogin)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btLimpar)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lbHorario.setBackground(new java.awt.Color(255, 255, 255));
        lbHorario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbHorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbHorario.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lbHorario.setOpaque(true);

        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png"))); // NOI18N
        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(jLabel1)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 45, Short.MAX_VALUE)
                        .add(lbHorario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 180, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btFechar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, lbHorario, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(org.jdesktop.layout.GroupLayout.LEADING, btFechar, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void btLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimparActionPerformed
        
        limpar();
        
    }//GEN-LAST:event_btLimparActionPerformed

    public void limpar(){
        txtEmail.setText("");
        txtSenha.setText("");
    }
    
    private void btLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLoginActionPerformed
        
        txtEmail.setText(txtEmail.getText().toLowerCase().trim());
        
        if(!txtEmail.getText().equals("") && txtSenha.getPassword().length > 0){
            
                EmailValidator em = new EmailValidator();
                if(em.validate(txtEmail.getText())){
                    
                    String email = txtEmail.getText();
                    String senha = "";
                    for(int x = 0 ; x < txtSenha.getPassword().length ; x++){
                        senha += txtSenha.getPassword()[x];
                    }
                    
                    List<UsuarioEntity> resp = new UsuarioBLL().pesquisaPersonalizada("email = '"+email+"' AND senha = md5('"+senha+"') AND estatus = 'Ativado'");
                    if(resp != null && resp.size() == 1){
                        
                        if("Sim".equals(resp.get(0).getGrupo().getLogindesktop())){
                            
                            JOptionPane.showMessageDialog(null,"Logon realizado, seja bem vindo "+resp.get(0).getPessoa().getNome()+".","OK",JOptionPane.PLAIN_MESSAGE);
                            Login.setLogin(resp.get(0).getId());
                            this.dispose();
                            
                            resp.get(0).setUltimoLogin(DataUtility.getDateTime("EN"));
                            new UsuarioBLL().salvar(resp.get(0));
                            
                            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Login'").get(0));
                            ul.setDescricao("Realizou login.");
                            ul.setUsuario(resp.get(0));
                            ul.setHorario(getDateTime("EN"));
                            new UsuarioAcaoBLL().salvar(ul);
                            
                            new MenuPrincipalVIEW().setVisible(true);
                            
                        }else{
                            
                            JOptionPane.showMessageDialog(null,"Dados inválidos.","ERRO",JOptionPane.ERROR_MESSAGE);
                            limpar();
                            
                        }
                        
                    }else{
                        if(resp == null || resp.isEmpty()){
                            JOptionPane.showMessageDialog(null,"Dados inválidos.","ERRO",JOptionPane.ERROR_MESSAGE);
                            limpar();
                        }else{
                            JOptionPane.showMessageDialog(null,"Erro ao realizar logon.","ERRO",JOptionPane.ERROR_MESSAGE);
                            limpar();
                        }
                    }
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Email inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                    limpar();
                }
                
        }else{
            JOptionPane.showMessageDialog(null,"Preencha todos os campos.","ERRO",JOptionPane.ERROR_MESSAGE);
            limpar();
        }
        
    }//GEN-LAST:event_btLoginActionPerformed

    private String getDateTime(String lg) {
        if(lg.equals("PT")){
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
            java.util.Date date = new java.util.Date();
            return dateFormat.format(date);
        }else{
            if(lg.equals("EN")){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
                java.util.Date date = new java.util.Date();
                return dateFormat.format(date);
            }else{
                return null;
            }
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btLimpar;
    private javax.swing.JButton btLogin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lbHorario;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JPasswordField txtSenha;
    // End of variables declaration//GEN-END:variables

    @Override
    public void controlaAcessos() {
        // desnecessario, so pra aproveitar os outros odis métodos da interface
    }

}
