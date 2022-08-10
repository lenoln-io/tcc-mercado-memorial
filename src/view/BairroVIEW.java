package view;

import bll.BairroBLL;
import bll.CidadeBLL;
import bll.PessoaBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.BairroEntity;
import entity.CidadeEntity;
import entity.PessoaEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.BairroTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import utility.Login;


public class BairroVIEW extends javax.swing.JFrame implements IVIEW{

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
                        if(this.btNovo.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btNovo.setVisible(false);
                        } else
                        if(this.btEditar.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btEditar.setVisible(false);
                        } else
                        if(this.btExcluir.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btExcluir.setVisible(false);
                        }
                    }
                    
                    if(!btNovo.isVisible() && !btEditar.isVisible()){
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

        for (int i = 0; i < modeloTabelaBairro.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaBairro.getColumnName(i), modeloTabelaBairro.getnomeTabela(i),
                modeloTabelaBairro.getNomeColunaTabela(i),
                modeloTabelaBairro.getTipoColunaTabela(i))
            );
        }

    }
    
    private void carregarTabela() {
        modeloTabelaBairro.Recarregar(controleBairro.listarTodos());
        tbBairros.setModel(modeloTabelaBairro);
    }

    private void carregarTabela(List<BairroEntity> listarTodos) {
        modeloTabelaBairro.Recarregar(listarTodos);
        tbBairros.setModel(modeloTabelaBairro);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private void limparCampos() {
        txtNome.setText("");
        cbxCidades.setSelectedIndex(-1);
        tbBairros.clearSelection();
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(condicao);
        cbxCidades.setEnabled(condicao);
        tbBairros.setEnabled(!condicao);
    }
    
    private void controlarBotoes() {
        btNovo.setEnabled(situacao == situacaoCadastro.scEsperando || situacao == situacaoCadastro.scVisualizando);
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
        objetoBairro = modeloTabelaBairro.getObjeto(tbBairros.getSelectedRow()).getClone();
        txtNome.setText(objetoBairro.getNome());
        cbxModel.setSelectedItem(objetoBairro.getCidade());
    }
    
    private situacaoCadastro situacao;
    private BairroEntity objetoBairro;
    private final BairroBLL controleBairro = new BairroBLL();
    private final BairroTableModel modeloTabelaBairro = new BairroTableModel();
    private final DefaultComboBoxModel<CidadeEntity> cbxModel = new DefaultComboBoxModel<>();
    
    private void carregarComboBox() {
        cbxCidades.setModel(cbxModel);
        CidadeBLL controleCidade = new CidadeBLL();
        List<CidadeEntity> estados = controleCidade.listarTodos();
        for(CidadeEntity estado : estados){
            cbxModel.addElement(estado);
        }
        cbxCidades.setSelectedIndex(-1);
    }
    
    private void criaMascaras(){
        
    }
    
    public BairroVIEW() {
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        carregarTabela();
        controlarBotoes();
        controlarCampos(false);
        carregarComboBox();
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
        btNovo = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        tbAbas = new javax.swing.JTabbedPane();
        pnCampos = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        txtNome = new javax.swing.JTextField();
        cbxCidades = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        spTabela = new javax.swing.JScrollPane();
        tbBairros = new javax.swing.JTable();
        pnFiltros2 = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de bairros");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/new_blue.png"))); // NOI18N
        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar novo bairro<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoBairro"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados do bairro<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarBairro"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados do bairro<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarBairro"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/delete.png"))); // NOI18N
        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír bairro<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirBairro"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png"))); // NOI18N
        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarBairro"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png"))); // NOI18N
        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de categorias<br />Atalho: alt + f</html>");
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
                .add(btNovo)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
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
                    .add(btNovo)
                    .add(btSalvar)
                    .add(btEditar)
                    .add(btExcluir)
                    .add(btCancelar)
                    .add(btFechar))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setText("Nome:");

        jLabel4.setText("Cidade:");

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel3)
                    .add(jLabel4))
                .add(0, 0, 0)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(txtNome)
                    .add(cbxCidades, 0, 678, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel3))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbxCidades, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbBairros.setModel(new javax.swing.table.DefaultTableModel(
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
        tbBairros.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbBairros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbBairrosMousePressed(evt);
            }
        });
        tbBairros.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbBairrosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbBairrosKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbBairros);

        pnFiltros2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png"))); // NOI18N
        btExcel.setMnemonic('x');
        btExcel.setText("Excel");
        btExcel.setToolTipText("<html>Exportar dados da tabela para planilha excel<br />Atalho: alt + x</html>");
        btExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcelActionPerformed(evt);
            }
        });

        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btFiltrar.setMnemonic('t');
        btFiltrar.setText("Filtrar");
        btFiltrar.setToolTipText("<html>Filtrar dados da tabela pelo critério<br />Atalho: alt + t</html>");
        btFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarActionPerformed(evt);
            }
        });

        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png"))); // NOI18N
        btListar.setMnemonic('l');
        btListar.setText("Listar");
        btListar.setToolTipText("<html>Mostrar todos os bairros na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel1.setText("Critério:");

        jLabel2.setText("Campo:");

        org.jdesktop.layout.GroupLayout pnFiltros2Layout = new org.jdesktop.layout.GroupLayout(pnFiltros2);
        pnFiltros2.setLayout(pnFiltros2Layout);
        pnFiltros2Layout.setHorizontalGroup(
            pnFiltros2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltros2Layout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 191, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterio)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btFiltrar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btListar)
                .addContainerGap())
        );
        pnFiltros2Layout.setVerticalGroup(
            pnFiltros2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltros2Layout.createSequentialGroup()
                .addContainerGap()
                .add(pnFiltros2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btExcel)
                    .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFiltrar)
                    .add(btListar)
                    .add(jLabel1)
                    .add(jLabel2))
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
                    .add(pnFiltros2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 124, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 267, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
        
        limparCampos();
        situacao = situacaoCadastro.scInserindo;
        controlarBotoes();
        controlarCampos(true);
        
    }//GEN-LAST:event_btNovoActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
        txtNome.setText(txtNome.getText().trim());
        
        if(!txtNome.getText().equals("") && cbxModel.getSelectedItem() != null){
            if (situacao == situacaoCadastro.scInserindo) {
                    objetoBairro = new BairroEntity();
            }

            objetoBairro.setNome(txtNome.getText());
            objetoBairro.setCidade((CidadeEntity) cbxModel.getSelectedItem());

            boolean salvar = true;
            List<BairroEntity> bairros = controleBairro.listarTodos();
            for(BairroEntity bairro : bairros){
                if(bairro.getId() == 0){
                    if(bairro.getNome().equals(objetoBairro.getNome()) && bairro.getCidade().getId() == objetoBairro.getCidade().getId()){
                        salvar = false;
                        break;
                    }
                }else{
                    if(bairro.getNome().equals(objetoBairro.getNome()) && bairro.getCidade().getId() == objetoBairro.getCidade().getId() && bairro.getId() != objetoBairro.getId()){
                        salvar = false;
                        break;
                    }
                }
            }
            if(salvar == true){
                objetoBairro = controleBairro.salvar(objetoBairro);

                if (situacao == situacaoCadastro.scInserindo) {
                    
                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                    ul.setDescricao("Cadastrou o bairro de ID = "+objetoBairro.getId());
                    ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                    ul.setHorario(DataUtility.getDateTime("EN"));
                    new UsuarioAcaoBLL().salvar(ul);
                    
                    JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                }else{
                    
                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                    ul.setDescricao("Alterou o bairro de ID = "+objetoBairro.getId());
                    ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                    ul.setHorario(DataUtility.getDateTime("EN"));
                    new UsuarioAcaoBLL().salvar(ul);
                    
                    JOptionPane.showMessageDialog(null,"O registro foi atualizado com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                }
                
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
                
            }else{
                JOptionPane.showMessageDialog(null,"Já existe um registro com esse nome na cidade escolhida.","ERRO",JOptionPane.ERROR_MESSAGE);
            }

        }else{
            JOptionPane.showMessageDialog(null,"Preencha todos os campos.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        situacao = situacaoCadastro.scEditando;
        controlarBotoes();
        controlarCampos(true);
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        PessoaBLL pessoaBLL = new PessoaBLL();
        List<PessoaEntity> resp = pessoaBLL.sqlDeConsultaPersonalizada("select * from pessoas, pessoaenderecos where pessoas.id = pessoaenderecos.pessoa and pessoaenderecos.bairro = "+objetoBairro.getId());
        if(resp == null || resp.isEmpty()){
            
            int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

            if (temp == JOptionPane.YES_OPTION) {
                
                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
                ul.setDescricao("Excluiu o bairro "+objetoBairro.getNome()+" de ID = "+objetoBairro.getId());
                ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                ul.setHorario(DataUtility.getDateTime("EN"));
                new UsuarioAcaoBLL().salvar(ul);
                
                controleBairro.excluir(objetoBairro);
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Há pessoas associadas a esse bairro.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void tbBairrosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbBairrosMousePressed
        
        if(tbBairros.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbBairrosMousePressed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbBairros, new File(caminho));
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
                    carregarTabela(new BairroBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new BairroBLL().filtrar(criterio));
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
        
        carregarTabela(new BairroBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void tbBairrosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBairrosKeyPressed
        
    }//GEN-LAST:event_tbBairrosKeyPressed

    private void tbBairrosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbBairrosKeyReleased
        
        if(tbBairros.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbBairrosKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btSalvar;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxCidades;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros2;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbBairros;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
