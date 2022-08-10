package view;

import bll.UsuarioAcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.HistoricoTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import static utility.DataUtility.formataStrignDateTimeEnToBr;
import utility.ExportarExcel;
import utility.Login;


public class HistoricoVIEW extends javax.swing.JFrame implements IVIEW {
    
    public final void controlaAcessos(){
        if(Login.getLogin() != 0){
            
            UsuarioEntity user = new UsuarioBLL().pesquisar(Login.getLogin());
            if(user != null){
                
                    List<AcessoEntity> acessosG = new AcessoBLL().pesquisaPersonalizada("tiposistema = 'DESKTOP'");
                    List<AcessoEntity> acessosU = user.getGrupo().getAcessos();
                    for(int x = (acessosG.size()-1) ; x >= 0 ; x--){
                        for(int y = (acessosU.size()-1) ; y >= 0 ; y--){
                            if(acessosG.get(x).getId() == acessosU.get(y).getId()){
                                acessosG.remove(x);
                                break;
                            }
                        }
                    }

                    for(int x = (acessosG.size()-1) ; x >= 0 ; x--){
                        if(this.btExcluir.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btExcluir.setVisible(false);
                        }
                    }
                
            }else{
                System.exit(0);
            }
            
        }else{
            System.exit(0);
        }
    }

    private void preencherFiltroCampos() {

        cbxCampos.removeAllItems();

        for (int i = 0; i < modeloTabelaUsuarioAcao.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaUsuarioAcao.getColumnName(i), modeloTabelaUsuarioAcao.getnomeTabela(i),
                modeloTabelaUsuarioAcao.getNomeColunaTabela(i),
                modeloTabelaUsuarioAcao.getTipoColunaTabela(i))
            );
        }

    }
    
    private void insereIcones(){
        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/delete.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    }    
    
    private void carregarTabela() {
        modeloTabelaUsuarioAcao.Recarregar(controleUsuarioAcao.listarTodos());
        tbUsuarioAcao.setModel(modeloTabelaUsuarioAcao);
    }

    private void carregarTabela(List<UsuarioAcaoEntity> filtrar) {
        modeloTabelaUsuarioAcao.Recarregar(filtrar);
        tbUsuarioAcao.setModel(modeloTabelaUsuarioAcao);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private void limparCampos() {
        txtNmpessoa.setText("");
        txtDesc.setText("");
        txtAcao.setText("");
        txtHorario.setText("");
        txtUser.setText("");
        txtGrupo.setText("");
        tbUsuarioAcao.clearSelection();
    }
    
    private void controlarCampos(boolean condicao) {
        txtNmpessoa.setEnabled(condicao);
        txtDesc.setEnabled(condicao);
        txtAcao.setEnabled(condicao);
        txtHorario.setEnabled(condicao);
        txtUser.setEnabled(condicao);
        txtGrupo.setEnabled(condicao);
        tbUsuarioAcao.setEnabled(!condicao);
    }
    
    private void controlarBotoes() {
        btExcluir.setEnabled(situacao == situacaoCadastro.scVisualizando);
        
        btExcel.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btFiltrar.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        txtCriterio.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btListar.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        cbxCampos.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
    }
    
    private void preencherCampos() {
        objetoUsuarioAcao = modeloTabelaUsuarioAcao.getObjeto(tbUsuarioAcao.getSelectedRow()).getClone();
        txtUser.setText(objetoUsuarioAcao.getUsuario().getEmail());
        txtDesc.setText(objetoUsuarioAcao.getDescricao());
        txtHorario.setText(formataStrignDateTimeEnToBr(objetoUsuarioAcao.getHorario()));
        txtAcao.setText(objetoUsuarioAcao.getAcao().getNome());
        txtNmpessoa.setText(objetoUsuarioAcao.getUsuario().getPessoa().getNome());
        txtGrupo.setText(objetoUsuarioAcao.getUsuario().getGrupo().getNome());
    }
    
    private situacaoCadastro situacao;
    private UsuarioAcaoEntity objetoUsuarioAcao;
    private final UsuarioAcaoBLL controleUsuarioAcao = new UsuarioAcaoBLL();
    private final HistoricoTableModel modeloTabelaUsuarioAcao = new HistoricoTableModel();
    
    private void criaMascaras(){
        
    }
    
    public HistoricoVIEW() {
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        carregarTabela();
        controlarBotoes();
        controlarCampos(false);
        insereIcones();
        preencherFiltroCampos();
        desabilitaBtFecharEAtalhos();
        controlaAcessos();
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
        if(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo){
                    
            int temp = JOptionPane.showConfirmDialog(null,"Você não salvou a operação atual, se sair perderá as informações desse registro.\nDeseja mesmo sair?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

            if (temp == JOptionPane.YES_OPTION) {
                dispose();
            }

        }else{
            dispose();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        btExcluir = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        tbAbas = new javax.swing.JTabbedPane();
        pnCampos = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        txtHorario = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtUser = new javax.swing.JTextField();
        txtNmpessoa = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtGrupo = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtAcao = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        spTabela = new javax.swing.JScrollPane();
        tbUsuarioAcao = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcluir.setText("Excluir");
        btExcluir.setName("btExcluirHistorico"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btFechar.setText("Fechar");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(btExcluir)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btFechar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btExcluir)
                    .add(btFechar))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel7.setText("Horario:");

        txtUser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUserActionPerformed(evt);
            }
        });

        txtNmpessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNmpessoaActionPerformed(evt);
            }
        });

        jLabel2.setText("Usuário:");

        jLabel3.setText("<html>Nome da<br>pessoa:</html>");

        jLabel4.setText("Grupo:");

        txtAcao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAcaoActionPerformed(evt);
            }
        });

        jLabel6.setText("Descrição:");

        txtDesc.setColumns(20);
        txtDesc.setRows(5);
        jScrollPane1.setViewportView(txtDesc);

        jLabel5.setText("Acao:");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(0, 0, Short.MAX_VALUE)
                        .add(jLabel7)
                        .add(298, 298, 298))
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(txtNmpessoa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 276, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jLabel2)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(txtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 276, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jLabel5)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(txtAcao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 276, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                                .add(jLabel4)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(txtGrupo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 276, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(0, 0, Short.MAX_VALUE)
                                .add(txtHorario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 276, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel6)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 404, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(txtUser, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel6))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtNmpessoa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(txtGrupo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(txtAcao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel7)
                            .add(txtHorario, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jScrollPane1))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbAbas.addTab("Campos", pnCampos);

        tbUsuarioAcao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tbUsuarioAcao.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbUsuarioAcao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbUsuarioAcaoMousePressed(evt);
            }
        });
        tbUsuarioAcao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbUsuarioAcaoKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbUsuarioAcao);

        pnFiltros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setText("Excel");
        btExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcelActionPerformed(evt);
            }
        });

        btFiltrar.setText("Filtrar");
        btFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarActionPerformed(evt);
            }
        });

        btListar.setText("Listar");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel1.setText("Critério:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 286, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btFiltrar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btListar)
                .addContainerGap())
        );
        pnFiltrosLayout.setVerticalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btExcel)
                    .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFiltrar)
                    .add(btListar)
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(tbAbas)
                    .add(spTabela)
                    .add(pnFiltros, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 220, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void txtUserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUserActionPerformed
        
        
        
    }//GEN-LAST:event_txtUserActionPerformed

    private void txtAcaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAcaoActionPerformed
        
        
        
    }//GEN-LAST:event_txtAcaoActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
            controleUsuarioAcao.excluir(objetoUsuarioAcao);
            carregarTabela();
            limparCampos();
            situacao = situacaoCadastro.scEsperando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void tbUsuarioAcaoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUsuarioAcaoMousePressed
        
        if(tbUsuarioAcao.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbUsuarioAcaoMousePressed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbUsuarioAcao, new File(caminho));
                JOptionPane.showMessageDialog(null, "Arquivo: '" + caminho + "' exportado com sucesso.","Mensagem", JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_btExcelActionPerformed

    private void btFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFiltrarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
        txtCriterio.setText(txtCriterio.getText().trim());
        
        if(!txtCriterio.getText().equals("")){
            CriterioFiltro criterio = new CriterioFiltro();

            criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
            criterio.setCriterio(txtCriterio.getText());

            if("int".equals(criterio.getCampo().getTipoColuna())){
                try{
                    Integer.parseInt(criterio.getCriterio());
                    carregarTabela(new UsuarioAcaoBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new UsuarioAcaoBLL().filtrar(criterio));
                }else{
                    if("datetime".equals(criterio.getCampo().getTipoColuna())){
                        carregarTabela(new UsuarioAcaoBLL().filtrar(criterio));
                    }
                }
            }
        }else{
            JOptionPane.showMessageDialog(null,"Informe o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btFiltrarActionPerformed

    private void btListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
        carregarTabela(new UsuarioAcaoBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void txtNmpessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNmpessoaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNmpessoaActionPerformed

    private void tbUsuarioAcaoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbUsuarioAcaoKeyReleased
        
        if(tbUsuarioAcao.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbUsuarioAcaoKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btListar;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbUsuarioAcao;
    private javax.swing.JTextField txtAcao;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtGrupo;
    private javax.swing.JTextField txtHorario;
    private javax.swing.JTextField txtNmpessoa;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables

}
