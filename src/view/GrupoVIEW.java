package view;

import bll.GrupoBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.GrupoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.GrupoTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import utility.Login;


public final class GrupoVIEW extends javax.swing.JFrame implements IVIEW {

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

        for (int i = 0; i < modeloTabelaGrupo.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaGrupo.getColumnName(i), modeloTabelaGrupo.getnomeTabela(i),
                modeloTabelaGrupo.getNomeColunaTabela(i),
                modeloTabelaGrupo.getTipoColunaTabela(i))
            );
        }

    }
    
    private void carregarTabela() {
        modeloTabelaGrupo.Recarregar(controleGrupo.listarTodos());
        tbGrupos.setModel(modeloTabelaGrupo);
    }

    private void carregarTabela(List<GrupoEntity> filtrar) {
        modeloTabelaGrupo.Recarregar(filtrar);
        tbGrupos.setModel(modeloTabelaGrupo);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtDesc.setText("");
        ckbWeb.setSelected(false);
        ckbDesk.setSelected(false);
        carregaListas(null);
        tbGrupos.clearSelection();
        controleBtFunc();
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(condicao);
        liFunc.setEnabled(condicao);
        liFuncAll.setEnabled(condicao);
        btAddFunc.setEnabled(condicao);
        btDelFunc.setEnabled(condicao);
        txtDesc.setEnabled(condicao);
        ckbDesk.setEnabled(condicao);
        ckbWeb.setEnabled(condicao);
        tbGrupos.setEnabled(!condicao);
        controleBtFunc();
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
        objetoGrupo = modeloTabelaGrupo.getObjeto(tbGrupos.getSelectedRow()).getClone();
        txtNome.setText(objetoGrupo.getNome());
        txtDesc.setText(objetoGrupo.getDescricao());
        carregaListas(objetoGrupo.getAcessos());
        if(objetoGrupo.getLogindesktop().equalsIgnoreCase("Sim")){
            ckbDesk.setSelected(true);
        }else{
            ckbDesk.setSelected(false);
        }
        if(objetoGrupo.getLoginweb().equalsIgnoreCase("Sim")){
            ckbWeb.setSelected(true);
        }else{
            ckbWeb.setSelected(false);
        }
    }
    
    private situacaoCadastro situacao;
    private GrupoEntity objetoGrupo;
    private final GrupoBLL controleGrupo = new GrupoBLL();
    private final GrupoTableModel modeloTabelaGrupo = new GrupoTableModel();
    private final DefaultListModel<AcessoEntity> modeloLista1 = new DefaultListModel<>();
    private final DefaultListModel<AcessoEntity> modeloLista2 = new DefaultListModel<>();
    
    public void carregaListas(List<AcessoEntity> acessos){
        
        modeloLista1.removeAllElements();
        modeloLista2.removeAllElements();
        
        if(acessos != null && !acessos.isEmpty()){
            
            for(AcessoEntity acesso : acessos){
                
                modeloLista1.addElement(acesso);
                
            }
            
        }
        
        liFunc.setModel(modeloLista1);
        
        List<AcessoEntity> acessosAdd = new AcessoBLL().listarTodos();
        
        if(acessos != null && !acessos.isEmpty()){
            for(int x = (acessosAdd.size()-1) ; x >= 0 ; x-- ){
                for(int y = (acessos.size()-1) ; y >= 0 ; y-- ){
                    if(acessosAdd.get(x) != null)
                        if(acessosAdd.get(x).getId() == acessos.get(y).getId()){
                            acessosAdd.set(x, null);
                        }
                }
            } 
        }
        
        for(AcessoEntity acessoAdd : acessosAdd){
            
            if(acessoAdd != null)
                modeloLista2.addElement(acessoAdd);
            
        }
        
        liFuncAll.setModel(modeloLista2);
        
    }
    
    private void criaMascaras(){
        
    }
    
    public GrupoVIEW() {
        
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        carregarTabela();
        controlarBotoes();
        controlarCampos(false);
        preencherFiltroCampos();
        desabilitaBtFecharEAtalhos();
        carregaListas(null);
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
        jLabel1 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        liFunc = new javax.swing.JList();
        jLabel2 = new javax.swing.JLabel();
        btAddFunc = new javax.swing.JButton();
        btDelFunc = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        liFuncAll = new javax.swing.JList();
        jScrollPane4 = new javax.swing.JScrollPane();
        txtDesc = new javax.swing.JTextArea();
        jLabel5 = new javax.swing.JLabel();
        ckbWeb = new javax.swing.JCheckBox();
        ckbDesk = new javax.swing.JCheckBox();
        jLabel3 = new javax.swing.JLabel();
        spTabela = new javax.swing.JScrollPane();
        tbGrupos = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/group.png"))); // NOI18N
        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar novo grupo<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoGrupo"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/group_add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados do grupo<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarGrupo"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/group_edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados do grupo<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarGrupo"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/group_delete.png"))); // NOI18N
        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír grupo<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirGrupo"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png"))); // NOI18N
        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação atual<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarGrupo"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png"))); // NOI18N
        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de grupos<br />Atalho: alt + f</html>");
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

        jLabel1.setText("Nome:");

        liFunc.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                liFuncMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(liFunc);

        jLabel2.setText("Acessos:");

        btAddFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/arrow_left.png"))); // NOI18N
        btAddFunc.setMnemonic('+');
        btAddFunc.setToolTipText("<html>Adicionar função ao grupo<br>Atalho: alt e +</html>");
        btAddFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddFuncActionPerformed(evt);
            }
        });

        btDelFunc.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/arrow_right.png"))); // NOI18N
        btDelFunc.setMnemonic('-');
        btDelFunc.setToolTipText("<html>Remover função do grupo<br>Atalho: alt e +</html>");
        btDelFunc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelFuncActionPerformed(evt);
            }
        });

        liFuncAll.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                liFuncAllMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(liFuncAll);

        txtDesc.setColumns(20);
        txtDesc.setRows(5);
        jScrollPane4.setViewportView(txtDesc);

        jLabel5.setText("Descrição:");

        ckbWeb.setText("Login WEB");

        ckbDesk.setText("Login DESKTOP");

        jLabel3.setText("Login:");

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel1)
                    .add(jLabel2)
                    .add(jLabel5)
                    .add(jLabel3))
                .add(11, 11, 11)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtNome)
                    .add(jScrollPane4)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(ckbWeb)
                        .add(18, 18, 18)
                        .add(ckbDesk)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 476, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(btAddFunc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btDelFunc))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 474, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel1))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel5)
                    .add(jScrollPane4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jScrollPane2)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel2)
                            .add(btAddFunc))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btDelFunc))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 171, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(ckbWeb)
                    .add(ckbDesk)
                    .add(jLabel3))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbGrupos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbGrupos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbGrupos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbGruposMousePressed(evt);
            }
        });
        tbGrupos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbGruposKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbGruposKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbGrupos);

        pnFiltros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png"))); // NOI18N
        btExcel.setMnemonic('e');
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
        btListar.setToolTipText("<html>Mostrar todos os grupos na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel4.setText("Critério:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 191, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
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
                    .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFiltrar)
                    .add(btListar)
                    .add(jLabel4))
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
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 383, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        if(!objetoGrupo.getNome().equalsIgnoreCase("Administradores") && !objetoGrupo.getNome().equalsIgnoreCase("Controlador mestre")){
        
            situacao = situacaoCadastro.scEditando;
            controlarBotoes();
            controlarCampos(true);
            
        }else{
            JOptionPane.showMessageDialog(null,"Esse grupo é padrão do sistema não pode ser alterado.","ERRO",JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        UsuarioBLL controleUsuario = new UsuarioBLL();
        List<UsuarioEntity> produtos = controleUsuario.pesquisaPersonalizada("grupo = "+objetoGrupo.getId());
        if(produtos == null || produtos.isEmpty()){
            
            if(!objetoGrupo.getNome().equalsIgnoreCase("Administradores") && !objetoGrupo.getNome().equalsIgnoreCase("Controlador mestre")){
                int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

                if (temp == JOptionPane.YES_OPTION) {
                    
                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
                    ul.setDescricao("Excluiu o grupo "+objetoGrupo.getNome()+" de ID = "+objetoGrupo.getId());
                    ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                    ul.setHorario(DataUtility.getDateTime("EN"));
                    new UsuarioAcaoBLL().salvar(ul);
                    
                    controleGrupo.excluir(objetoGrupo);
                    carregarTabela();
                    limparCampos();
                    situacao = situacaoCadastro.scEsperando;
                    controlarBotoes();
                    controlarCampos(false);
                }
            }else{
                JOptionPane.showMessageDialog(null,"Esse grupo é padrão do sistema não pode ser remvido.","ERRO",JOptionPane.WARNING_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Há usuários associados a esse grupo.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbGrupos, new File(caminho));
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
        limparCampos();
        
        txtCriterio.setText(txtCriterio.getText().trim());
        
        if(!txtCriterio.getText().equals("")){
            CriterioFiltro criterio = new CriterioFiltro();

            criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
            criterio.setCriterio(txtCriterio.getText());

            if("int".equals(criterio.getCampo().getTipoColuna())){
                try{
                    Integer.parseInt(criterio.getCriterio());
                    carregarTabela(new GrupoBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new GrupoBLL().filtrar(criterio));
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
        limparCampos();
        
        carregarTabela(new GrupoBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void btAddFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddFuncActionPerformed
        
        if(!liFuncAll.isSelectionEmpty()){
            
            int[] indices = liFuncAll.getSelectedIndices();
            for(int x = (indices.length-1) ; x>=0 ; x--){
                AcessoEntity a = modeloLista2.getElementAt(indices[x]);
                modeloLista2.remove(indices[x]);
                modeloLista1.addElement(a);
            }
            
            liFunc.clearSelection();
            liFuncAll.clearSelection();
            
        }else{
            JOptionPane.showMessageDialog(null,"Escolha uma funcionalidade para adicioná-la.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
        controleBtFunc();
        
    }//GEN-LAST:event_btAddFuncActionPerformed

    private void btDelFuncActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelFuncActionPerformed
        
        if(!liFunc.isSelectionEmpty()){
            
            int[] indices = liFunc.getSelectedIndices();
            for(int x = (indices.length-1) ; x>=0 ; x--){
                AcessoEntity a = modeloLista1.getElementAt(indices[x]);
                modeloLista1.remove(indices[x]);
                modeloLista2.addElement(a);
            }
            
            liFunc.clearSelection();
            liFuncAll.clearSelection();
            
        }else{
            JOptionPane.showMessageDialog(null,"Escolha uma funcionalidade para remove-la.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
        controleBtFunc();
        
    }//GEN-LAST:event_btDelFuncActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
        txtNome.setText(txtNome.getText().trim());
        txtDesc.setText(txtDesc.getText().trim());
        
        if(!txtNome.getText().equals("") && !txtDesc.getText().equals("")){
            
            if(ckbWeb.isSelected() || ckbDesk.isSelected()){
            
                List<GrupoEntity> resultado = null;
                if(situacao == situacaoCadastro.scInserindo){
                    resultado = controleGrupo.pesquisaPersonalizada("nome = '"+txtNome.getText()+"'");
                }else{
                    resultado = controleGrupo.pesquisaPersonalizada("nome = '"+txtNome.getText()+"' and id <> "+objetoGrupo.getId());
                }
                if(resultado == null || resultado.isEmpty()){

                    if(situacao == situacaoCadastro.scInserindo){
                        objetoGrupo = new GrupoEntity();
                    }

                    if(ckbWeb.isSelected()){
                        objetoGrupo.setLoginweb("Sim");
                    }else{
                        objetoGrupo.setLoginweb("Não");
                    }

                    if(ckbDesk.isSelected()){
                        objetoGrupo.setLogindesktop("Sim");
                    }else{
                        objetoGrupo.setLogindesktop("Não");
                    }

                    objetoGrupo.setNome(txtNome.getText());
                    objetoGrupo.setDescricao(txtDesc.getText());
                    
                    objetoGrupo.clearAcessos();
                    
                    boolean continuar1 = true;
                    boolean continuar2 = true;
                    for(int x = 0 ; x<modeloLista1.getSize() ; x++){
                        objetoGrupo.addAcesso(modeloLista1.get(x));
                        if(!ckbDesk.isSelected() && modeloLista1.get(x).getTiposistema().equals("DESKTOP")){
                            continuar1 = false;
                            break;
                        }else{
                            if(!ckbWeb.isSelected() && modeloLista1.get(x).getTiposistema().equals("WEB")){
                                continuar2 = false;
                                break;
                            }
                        }
                    }
                    
                    if(continuar1 == true && continuar2 == true){
                        
                        boolean salvar = false;
                        if(ckbDesk.isSelected()){
                            for(int x = 0 ; x<modeloLista1.getSize() ; x++){
                                if("DESKTOP".equals(modeloLista1.get(x).getTiposistema())){
                                    salvar = true;
                                    break;
                                }
                            }
                        }else{
                            salvar = true;
                        }

                        if(salvar == false){
                            JOptionPane.showMessageDialog(null,"Adicione funcionalidades DESKTOP para o grupo!","ERRO",JOptionPane.ERROR_MESSAGE);
                        }

                        if(salvar == true){
                            
                            boolean algumaFuncionalidadeWeb = false;
                            if(ckbWeb.isSelected()){
                                for(int x = 0 ; x<modeloLista1.getSize() ; x++){
                                    if("WEB".equals(modeloLista1.get(x).getTiposistema())){
                                        algumaFuncionalidadeWeb = true;
                                        break;
                                    }
                                }
                            }else{
                                algumaFuncionalidadeWeb = true;
                            }
                            
                            int temp = JOptionPane.YES_NO_OPTION;
                            if(algumaFuncionalidadeWeb == false){
                                temp = JOptionPane.showConfirmDialog(null,"Esse grupo não posssuí funcionalidades web, mas possuí login web.\n"
                                    + "Qualquer usuário desse grupo poderá ter acesso as funcionalidades básicas, como carrinho de compras\n"
                                    + "e alteração dos dados da sua conta. Prosseguir?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
                            }
                            
                            if(temp == JOptionPane.YES_NO_OPTION){
                                controleGrupo.salvar(objetoGrupo);

                                if(situacao == situacaoCadastro.scInserindo){
                                    
                                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                                    ul.setDescricao("Cadastrou o grupo de ID = "+objetoGrupo.getId());
                                    ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                                    ul.setHorario(DataUtility.getDateTime("EN"));
                                    new UsuarioAcaoBLL().salvar(ul);
                                    
                                    JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                                }else{
                                    
                                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                                    ul.setDescricao("Alterou o grupo de ID = "+objetoGrupo.getId());
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
                            }
                        }
                        
                    }else{
                        if(continuar1 == false){
                            JOptionPane.showMessageDialog(null,"Você adicionou funcionalidades desktop ao grupo, mas não permitiu login desktop!","ERRO",JOptionPane.ERROR_MESSAGE);
                        }else{
                            if(continuar2 == false){
                                JOptionPane.showMessageDialog(null,"Você adicionou funcionalidades web ao grupo, mas não permitiu login web!","ERRO",JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    }

                }else{
                    JOptionPane.showMessageDialog(null,"Já existe um grupo com esse nome!","ERRO",JOptionPane.ERROR_MESSAGE);
                }
                
            }else{
                JOptionPane.showMessageDialog(null,"Selecione pelo menos uma modalidade de login para o grupo.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Preencha todos os campos!","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void tbGruposMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbGruposMousePressed
        
        if(tbGrupos.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }

    }//GEN-LAST:event_tbGruposMousePressed

    private void liFuncAllMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_liFuncAllMousePressed
        
        liFunc.clearSelection();
        controleBtFunc();
        
    }//GEN-LAST:event_liFuncAllMousePressed

    private void liFuncMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_liFuncMousePressed
        
        liFuncAll.clearSelection();
        controleBtFunc();
        
    }//GEN-LAST:event_liFuncMousePressed

    private void tbGruposKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbGruposKeyPressed
        
    }//GEN-LAST:event_tbGruposKeyPressed

    private void tbGruposKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbGruposKeyReleased
        
        if(tbGrupos.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbGruposKeyReleased

    public void controleBtFunc(){
        
        if(liFuncAll.isSelectionEmpty()){
            btAddFunc.setEnabled(false);
        }else{
            btAddFunc.setEnabled(true);
        }

        if(liFunc.isSelectionEmpty()){
            btDelFunc.setEnabled(false);
        }else{
            btDelFunc.setEnabled(true);
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddFunc;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btDelFunc;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btSalvar;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JCheckBox ckbDesk;
    private javax.swing.JCheckBox ckbWeb;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList liFunc;
    private javax.swing.JList liFuncAll;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbGrupos;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextArea txtDesc;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables

}
