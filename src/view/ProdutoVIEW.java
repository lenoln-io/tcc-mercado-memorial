package view;

import bll.CategoriaBLL;
import bll.OfertaBLL;
import bll.OperacaoBLL;
import bll.ProdutoBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.CategoriaEntity;
import entity.OperacaoEntity;
import entity.ProdutoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import model.ProdutoTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import utility.Login;

public class ProdutoVIEW extends javax.swing.JFrame implements IVIEW {
    
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

    private boolean imagem = false;
    
    private String getDateTime() { 
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date); 
    }
    
    private void insereIcones(){
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    }
    
    public ProdutoVIEW() {
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
        carregarComboBox();
        controlaAcessos();
    }
    
    private void criaMascaras(){
        
    }
    
    private void carregarTabela() {
        modeloTabelaProduto.Recarregar(controleProduto.listarTodos());
        tbProdutos.setModel(modeloTabelaProduto);
    }

    private void carregarTabela(List<ProdutoEntity> filtrar) {
        modeloTabelaProduto.Recarregar(filtrar);
        tbProdutos.setModel(modeloTabelaProduto);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private final DefaultComboBoxModel<CategoriaEntity> cbxModel = new DefaultComboBoxModel<>();
    
    private void carregarComboBox() {
        cbxCategorias.setModel(cbxModel);
        CategoriaBLL controleCategoria = new CategoriaBLL();
        List<CategoriaEntity> categorias = controleCategoria.listarTodos();
        for(CategoriaEntity categoria : categorias){
            cbxModel.addElement(categoria);
        }
        cbxCategorias.setSelectedIndex(-1);
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtValor.setText("");
        pnImagem.limparImagem();
        buttonGroup1.clearSelection();
        buttonGroup2.clearSelection();
        cbxCategorias.setSelectedIndex(-1);
        taDesc.setText("");
        tbProdutos.clearSelection();
        imagem = false;
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(condicao);
        txtValor.setEnabled(condicao);
        rbQuilo.setEnabled(condicao);
        rbUnidade.setEnabled(condicao);
        taDesc.setEnabled(condicao);
        cbxCategorias.setEnabled(condicao);
        pnImagem.setEnabled(condicao);
        btFoto.setEnabled(condicao);
        rbAtivado.setEnabled(condicao);
        rbDesativado.setEnabled(condicao);
        tbProdutos.setEnabled(!condicao);
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
        objetoProduto = modeloTabelaProduto.getObjeto(tbProdutos.getSelectedRow()).getClone();
        txtNome.setText(objetoProduto.getNome());
        txtValor.setText(String.valueOf(objetoProduto.getValor()));
        cbxModel.setSelectedItem(objetoProduto.getCategoria());
        pnImagem.setImagem(objetoProduto.getIcone());
        if("Unidade".equals(objetoProduto.getTipoVenda())){
            rbUnidade.setSelected(true);
        }else{
            if("Quilo".equals(objetoProduto.getTipoVenda())){
                rbQuilo.setSelected(true);
            }
        }
        if("Ativado".equals(objetoProduto.getEstatus())){
            rbAtivado.setSelected(true);
        }else{
            rbDesativado.setSelected(true);
        }
        taDesc.setText(objetoProduto.getDescricao());
        imagem = true;
    }
    
    private void preencherFiltroCampos() {

        cbxCampos.removeAllItems();

        for (int i = 0; i < modeloTabelaProduto.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaProduto.getColumnName(i), modeloTabelaProduto.getnomeTabela(i),
                modeloTabelaProduto.getNomeColunaTabela(i),
                modeloTabelaProduto.getTipoColunaTabela(i))
            );
        }

    }
    
    private situacaoCadastro situacao;
    private ProdutoEntity objetoProduto;
    private final ProdutoBLL controleProduto = new ProdutoBLL();
    private final ProdutoTableModel modeloTabelaProduto = new ProdutoTableModel();
    
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btNovo = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        tbAbas = new javax.swing.JTabbedPane();
        pnCampos = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        rbUnidade = new javax.swing.JRadioButton();
        rbQuilo = new javax.swing.JRadioButton();
        txtValor = new javax.swing.JFormattedTextField();
        txtNome = new javax.swing.JTextField();
        cbxCategorias = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDesc = new javax.swing.JTextArea();
        btFoto = new javax.swing.JButton();
        pnImagem = new utility.PanelVisualizaImagem();
        jLabel8 = new javax.swing.JLabel();
        rbAtivado = new javax.swing.JRadioButton();
        rbDesativado = new javax.swing.JRadioButton();
        spTabela = new javax.swing.JScrollPane();
        tbProdutos = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de produtos");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package.png"))); // NOI18N
        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar novo produto<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoProduto"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados do produto<br />Atalho: alt + </html>");
        btSalvar.setName("btSalvarProduto"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_link.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados do produto<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarProduto"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_delete.png"))); // NOI18N
        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír produto<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirProduto"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarProduto"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de produtos<br />Atalho: alt + f</html>");
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

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setText("Nome:");

        jLabel2.setText("Valor:");

        jLabel3.setText("Descrição:");

        jLabel4.setText("Tipo de venda:");

        jLabel5.setText("Categoria:");

        buttonGroup1.add(rbUnidade);
        rbUnidade.setText("Unidade");

        buttonGroup1.add(rbQuilo);
        rbQuilo.setText("Quilo");

        taDesc.setColumns(20);
        taDesc.setRows(5);
        jScrollPane1.setViewportView(taDesc);

        btFoto.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/picture_add.png"))); // NOI18N
        btFoto.setMnemonic('o');
        btFoto.setText("Foto do produto:");
        btFoto.setToolTipText("<html>Escolher imagem para ícone do produto<br />Atalho: alt + o</html>");
        btFoto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFotoActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnImagemLayout = new org.jdesktop.layout.GroupLayout(pnImagem);
        pnImagem.setLayout(pnImagemLayout);
        pnImagemLayout.setHorizontalGroup(
            pnImagemLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        pnImagemLayout.setVerticalGroup(
            pnImagemLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        jLabel8.setText("Situação de venda:");

        buttonGroup2.add(rbAtivado);
        rbAtivado.setText("Ativado");

        buttonGroup2.add(rbDesativado);
        rbDesativado.setText("Desativado");

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(jPanel2Layout.createSequentialGroup()
                            .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(jPanel2Layout.createSequentialGroup()
                                    .add(jLabel5)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                    .add(cbxCategorias, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .add(jPanel2Layout.createSequentialGroup()
                                    .add(jLabel3)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE))
                                .add(jPanel2Layout.createSequentialGroup()
                                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jLabel1)
                                        .add(jLabel2))
                                    .add(31, 31, 31)
                                    .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                        .add(txtNome, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 379, Short.MAX_VALUE)
                                        .add(txtValor))))
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                        .add(jPanel2Layout.createSequentialGroup()
                            .add(jLabel4)
                            .add(67, 67, 67)
                            .add(rbUnidade)
                            .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 75, Short.MAX_VALUE)
                            .add(rbQuilo)
                            .add(119, 119, 119)))
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jLabel8)
                        .add(43, 43, 43)
                        .add(rbAtivado)
                        .add(62, 62, 62)
                        .add(rbDesativado)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnImagem, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(btFoto)
                        .add(0, 434, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFoto))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel2)
                            .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel4)
                            .add(rbUnidade)
                            .add(rbQuilo))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel5)
                            .add(cbxCategorias, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(jLabel8)
                            .add(rbAtivado)
                            .add(rbDesativado))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 6, Short.MAX_VALUE)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(jLabel3)))
                    .add(pnImagem, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .add(14, 14, 14))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbProdutos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbProdutos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProdutos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbProdutosMousePressed(evt);
            }
        });
        tbProdutos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbProdutosKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbProdutos);

        pnFiltros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setMnemonic('x');
        btExcel.setText("Excel");
        btExcel.setToolTipText("<html>Exportar dados da tabela para planilha do excel<br />Atalho: alt + x</html>");
        btExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcelActionPerformed(evt);
            }
        });

        btFiltrar.setMnemonic('t');
        btFiltrar.setText("Filtrar");
        btFiltrar.setToolTipText("<html>Filtrar dados da tabela pelo critério<br />Atalho: alt + t</html>");
        btFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarActionPerformed(evt);
            }
        });

        btListar.setMnemonic('l');
        btListar.setText("Listar");
        btListar.setToolTipText("<html>Mostrar todos os produtos na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel6.setText("Critério:");

        jLabel7.setText("Campo:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 250, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
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
                    .add(jLabel6)
                    .add(jLabel7))
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
                .add(tbAbas, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 423, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 155, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
        txtValor.setText(txtValor.getText().trim().replaceAll(" ", "").replace(",", "."));
        taDesc.setText(taDesc.getText().trim());
        
        if(!"".equals(txtNome.getText()) && !"".equals(taDesc.getText()) && !"".equals(txtValor.getText())&& cbxModel.getSelectedItem() != null && imagem == true && (rbQuilo.isSelected() || rbUnidade.isSelected()) && (rbAtivado.isSelected() || rbDesativado.isSelected())){
            
            try{
                
                if(Double.parseDouble(txtValor.getText())>0){
                    if (situacao == situacaoCadastro.scInserindo) {
                        objetoProduto = new ProdutoEntity();
                        objetoProduto.setId(0);
                    }
                    
                    if(rbAtivado.isSelected()){
                        objetoProduto.setEstatus("Ativado");
                    }else{
                        objetoProduto.setEstatus("Desativado");
                    }

                    objetoProduto.setNome(txtNome.getText());
                    objetoProduto.setDescricao(taDesc.getText());
                    objetoProduto.setValor(Double.parseDouble(txtValor.getText()));
                    objetoProduto.setCategoria((CategoriaEntity) cbxCategorias.getSelectedItem());
                    if(rbQuilo.isSelected()){
                        objetoProduto.setTipoVenda("Quilo");
                    }else{
                        if(rbUnidade.isSelected()){
                            objetoProduto.setTipoVenda("Unidade");
                        }
                    }
                    objetoProduto.setIcone(pnImagem.getImagem());

                    controleProduto.salvar(objetoProduto);

                    if (situacao == situacaoCadastro.scInserindo) {
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                        ul.setDescricao("Cadastrou o produto de ID = "+objetoProduto.getId());
                        ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                        ul.setHorario(DataUtility.getDateTime("EN"));
                        new UsuarioAcaoBLL().salvar(ul);
                        
                        JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                        ul.setDescricao("Alterou o produto de ID = "+objetoProduto.getId());
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
                    JOptionPane.showMessageDialog(null,"Valor do produto inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
                
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor do produto inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
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
        
        List<OperacaoEntity> resp = new OperacaoBLL().sqlDeConsultaPersonalizada("select * from operacao, produtos, operacaoprodutos where operacao.id = operacaoprodutos.operacao and produtos.id = operacaoprodutos.produto and produtos.id = "+objetoProduto.getId());
        if(resp != null && !resp.isEmpty()){
            int temp = JOptionPane.showConfirmDialog(null,"Você está prestes a excluír o produto do sistema e perder todos os seus dados.\nDeseja mesmo excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

            if (temp == JOptionPane.YES_OPTION) {

                new OfertaBLL().excluirPorProduto(objetoProduto);

                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
                ul.setDescricao("Excluiu o produto "+objetoProduto.getNome()+" de ID = "+objetoProduto.getId());
                ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                ul.setHorario(DataUtility.getDateTime("EN"));
                new UsuarioAcaoBLL().salvar(ul);

                controleProduto.excluir(objetoProduto);
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Há compras ou vendas associadas a esse produto.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void tbProdutosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProdutosMousePressed
        
        if(tbProdutos.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbProdutosMousePressed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbProdutos, new File(caminho));
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
        
        CriterioFiltro criterio = new CriterioFiltro();

        criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
        criterio.setCriterio(txtCriterio.getText().trim());

        if("int".equals(criterio.getCampo().getTipoColuna())){
            try{
                Integer.parseInt(criterio.getCriterio());
                carregarTabela(new ProdutoBLL().filtrar(criterio));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            if("string".equals(criterio.getCampo().getTipoColuna())){
                carregarTabela(new ProdutoBLL().filtrar(criterio));
            }else{
                if("double".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        criterio.setCriterio(String.valueOf(Double.parseDouble(criterio.getCriterio().replace(",", "."))));
                        carregarTabela(new ProdutoBLL().filtrar(criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
    }//GEN-LAST:event_btFiltrarActionPerformed

    private void btListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarActionPerformed
       
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
        carregarTabela(new ProdutoBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void btFotoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFotoActionPerformed
        
        JFileChooser arquivo = new JFileChooser();
        FileNameExtensionFilter filtro = new FileNameExtensionFilter("Imagens", "gif", "jpg", "png", "jpeg", "bitmap");

        arquivo.setFileFilter(filtro);
        arquivo.setAcceptAllFileFilterUsed(false);
        arquivo.setMultiSelectionEnabled(false);
        arquivo.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int teste = arquivo.showOpenDialog(null);

        if (teste == JFileChooser.APPROVE_OPTION) {
            pnImagem.setImagem(arquivo.getSelectedFile().getPath());
            imagem = true;
        }
        
    }//GEN-LAST:event_btFotoActionPerformed

    private void tbProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbProdutosKeyReleased
        
        if(tbProdutos.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbProdutosKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btFoto;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btSalvar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxCategorias;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private utility.PanelVisualizaImagem pnImagem;
    private javax.swing.JRadioButton rbAtivado;
    private javax.swing.JRadioButton rbDesativado;
    private javax.swing.JRadioButton rbQuilo;
    private javax.swing.JRadioButton rbUnidade;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTextArea taDesc;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbProdutos;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtNome;
    private javax.swing.JFormattedTextField txtValor;
    // End of variables declaration//GEN-END:variables

}
