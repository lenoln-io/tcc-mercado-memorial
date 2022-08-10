package view;

import bll.ContatoBLL;
import bll.AcessoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.ContatoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.ContatoTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.ExportarExcel;
import utility.Login;

public class ContatoVIEW extends javax.swing.JFrame implements IVIEW {
    
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
                        if(this.btEditar.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btEditar.setVisible(false);
                        } else
                        if(this.btExcluir.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btExcluir.setVisible(false);
                        }
                    }
                    
                    if(!btEditar.isVisible()){
                        btSalvar.setVisible(false);
                        btCancelar.setVisible(false);
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

        for (int i = 0; i < modeloTabelaContato.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaContato.getColumnName(i), modeloTabelaContato.getnomeTabela(i),
                modeloTabelaContato.getNomeColunaTabela(i),
                modeloTabelaContato.getTipoColunaTabela(i))
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
        modeloTabelaContato.Recarregar(controleContato.listarTodos());
        tbContatos.setModel(modeloTabelaContato);
    }

    private void carregarTabela(List<ContatoEntity> filtrar) {
        modeloTabelaContato.Recarregar(filtrar);
        tbContatos.setModel(modeloTabelaContato);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtTipo.setText("");
        txtEmail.setText("");
        txtMsg.setText("");
        txtData.setDate(null);
        tbContatos.clearSelection();
        buttonGroup1.clearSelection();
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(false/*condicao*/);
        txtTipo.setEnabled(false/*condicao*/);
        txtEmail.setEnabled(false/*condicao*/);
        txtMsg.setEnabled(false/*condicao*/);
        txtData.setEnabled(false/*condicao*/);
        //Só para garantir
        rbSim.setEnabled(condicao);
        rbNao.setEnabled(condicao);
        tbContatos.setEnabled(!condicao);
    }
    
    private void controlarBotoes() {
        btSalvar.setEnabled(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo);
        btEditar.setEnabled(situacao == situacaoCadastro.scVisualizando);
        btCancelar.setEnabled(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo);
        btExcluir.setEnabled(situacao == situacaoCadastro.scVisualizando);
        
        btExcel.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btFiltrar.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        txtCriterio.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        btListar.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
        cbxCampos.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
    }
    
    private void preencherCampos() {
        objetoContato = modeloTabelaContato.getObjeto(tbContatos.getSelectedRow()).getClone();
        txtNome.setText(objetoContato.getNome());
        txtTipo.setText(objetoContato.getTipo());
        txtMsg.setText(objetoContato.getMensagem());
        txtEmail.setText(objetoContato.getEmail());
        txtData.setDate(objetoContato.getData());
        if(objetoContato.getEstatus().equals("Sim")){
            rbSim.setSelected(true);
        }else{
            rbNao.setSelected(true);
        }
    }
    
    private situacaoCadastro situacao;
    private ContatoEntity objetoContato;
    private final ContatoBLL controleContato = new ContatoBLL();
    private final ContatoTableModel modeloTabelaContato = new ContatoTableModel();
    
    private void criaMascaras(){
        
    }
    
    public ContatoVIEW() {
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btExcluir = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        tbAbas = new javax.swing.JTabbedPane();
        pnCampos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtTipo = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();
        txtNome = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtMsg = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        txtData = new org.jdesktop.swingx.JXDatePicker();
        jLabel8 = new javax.swing.JLabel();
        rbSim = new javax.swing.JRadioButton();
        rbNao = new javax.swing.JRadioButton();
        spTabela = new javax.swing.JScrollPane();
        tbContatos = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtCriterio = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de contatos");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír contato<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirContato"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar janela de cadastro de contatos<br />Atalho: alt + f</html>");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados contato<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarContato"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png"))); // NOI18N
        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarContato"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados do contato<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarContato"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(btSalvar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btEditar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btExcluir)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btCancelar)
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
                    .add(btFechar)
                    .add(btEditar)
                    .add(btCancelar)
                    .add(btSalvar))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnCampos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nome do contato:");

        jLabel2.setText("Email do contato:");

        jLabel3.setText("Tipo da mensagem:");

        jLabel4.setText("Mensagem:");

        txtMsg.setColumns(20);
        txtMsg.setRows(5);
        jScrollPane1.setViewportView(txtMsg);

        jLabel7.setText("Data:");

        jLabel8.setText("Visualizado:");

        buttonGroup1.add(rbSim);
        rbSim.setText("Sim");

        buttonGroup1.add(rbNao);
        rbNao.setText("Não");

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel4)
                    .add(jLabel8))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(rbSim)
                        .add(18, 18, 18)
                        .add(rbNao)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(txtEmail)
                    .add(txtNome)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 658, Short.MAX_VALUE)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(txtTipo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 300, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jLabel7)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(txtData, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel2)
                    .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(txtTipo, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel7)
                    .add(txtData, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel4)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rbSim)
                    .add(jLabel8)
                    .add(rbNao))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbContatos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbContatos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbContatos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbContatosMousePressed(evt);
            }
        });
        tbContatos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbContatosKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbContatos);

        pnFiltros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setMnemonic('x');
        btExcel.setText("Excel");
        btExcel.setToolTipText("<html>Exportar dados da tabela para planilha do excel<br />Atalho: alt + x</html>");
        btExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcelActionPerformed(evt);
            }
        });

        cbxCampos.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCamposItemStateChanged(evt);
            }
        });
        cbxCampos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCamposActionPerformed(evt);
            }
        });

        btFiltrar.setMnemonic('t');
        btFiltrar.setText("Filtrar");
        btFiltrar.setToolTipText("<html>Fitrar dados da tabela pelo critério<br />Atalho: alt + t</html>");
        btFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarActionPerformed(evt);
            }
        });

        btListar.setMnemonic('l');
        btListar.setText("Listar");
        btListar.setToolTipText("<html>Listar todos os contatos na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel5.setText("Critério:");

        jLabel6.setText("Campo:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 203, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterio)
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
                    .add(btFiltrar)
                    .add(btListar)
                    .add(jLabel5)
                    .add(jLabel6)
                    .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
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
                .add(tbAbas)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
                controleContato.excluir(objetoContato);
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void tbContatosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbContatosMousePressed
        
        if(tbContatos.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbContatosMousePressed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbContatos, new File(caminho));
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
                    carregarTabela(new ContatoBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    txtCriterio.requestFocus();
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new ContatoBLL().filtrar(criterio));
                }else{
                    if("date".equals(criterio.getCampo().getTipoColuna())){
                        try{
                            criterio.setCriterio(criterio.getCriterio().replace("-", "/"));
                            criterio.setCriterio(criterio.getCriterio().replace("\\", "/"));
                            criterio.setCriterio(criterio.getCriterio().replace(" ", "/"));
                            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                            java.util.Date data = formatoData.parse(criterio.getCriterio());
                            java.sql.Date dataenvia = new java.sql.Date(data.getTime());
                            criterio.setCriterio(String.valueOf(dataenvia));
                            carregarTabela(new ContatoBLL().filtrar(criterio));
                        }
                        catch(ParseException e1){
                            JOptionPane.showMessageDialog(null, "Data inválida!!!","ERRO",JOptionPane.ERROR_MESSAGE);
                            txtCriterio.requestFocus();
                        }
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
        
        carregarTabela(new ContatoBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void cbxCamposItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCamposItemStateChanged
        
    }//GEN-LAST:event_cbxCamposItemStateChanged

    private void cbxCamposActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCamposActionPerformed
       
    }//GEN-LAST:event_cbxCamposActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        situacao = situacaoCadastro.scEditando;
        controlarBotoes();
        controlarCampos(true);
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
        if(rbNao.isSelected() || rbSim.isSelected()){
            
            if(rbSim.isSelected()){
                objetoContato.setEstatus("Sim");
            }else{
                objetoContato.setEstatus("Não");
            }
            
            controleContato.salvar(objetoContato);
            if(situacao == situacaoCadastro.scEditando){
                JOptionPane.showMessageDialog(null,"O registro foi atualizado com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                //Esse aqui nao deve ocorrer
            }
            
            carregarTabela();
            limparCampos();
            situacao = situacaoCadastro.scEsperando;
            controlarBotoes();
            controlarCampos(false);
            
        }else{
            JOptionPane.showMessageDialog(null,"Preencha todos os campos.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void tbContatosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbContatosKeyReleased
        
        if(tbContatos.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbContatosKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JRadioButton rbNao;
    private javax.swing.JRadioButton rbSim;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbContatos;
    private javax.swing.JTextField txtCriterio;
    private org.jdesktop.swingx.JXDatePicker txtData;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextArea txtMsg;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtTipo;
    // End of variables declaration//GEN-END:variables

}
