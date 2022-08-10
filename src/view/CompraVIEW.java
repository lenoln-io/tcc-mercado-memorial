package view;

import bll.OperacaoBLL;
import bll.PessoaJuridicaBLL;
import bll.ProdutoBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.OperacaoEntity;
import entity.OperacaoProdutosEntity;
import entity.PessoaJuridicaEntity;
import entity.ProdutoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.CompraTableModel;
import model.PessoaJuridicaTableModel;
import model.ProdutoCompraCarrinoATableModel;
import model.ProdutoCompraCarrinoTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import utility.Login;


public class CompraVIEW extends javax.swing.JFrame implements IVIEW {

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
    
    public CompraVIEW() {
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        carregarTabela();
        carregaTabelasProdutos(null);
        controlarBotoes();
        controlarCampos(false);
        insereIcones();
        preencherFiltroCampos();
        desabilitaBtFecharEAtalhos();
        situacaoPS = situacaoProdutoS.nadaS;
        controleBotoesProduto();
        situacaoF = situacaoFornecedor.fornecedorT;
        controlaBotoesFornecedor();
        preencherFiltroCamposF();
        carregarTabelaFornecedor();
        mostraPDproduto(null);
        preencherFiltroCamposP();
        controlaAcessos();
    }
    
    private String getDateTime() { 
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); 
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date); 
    }
    
    public void carregarTabelaFornecedor(List<PessoaJuridicaEntity> lista){
        modeloTabelaFornecedor.Recarregar(lista);
        tbFornecedores.setModel(modeloTabelaFornecedor);
    }
    
    public final void carregarTabelaFornecedor(){
        modeloTabelaFornecedor.Recarregar(new PessoaJuridicaBLL().listarTodos());
        tbFornecedores.setModel(modeloTabelaFornecedor);
    }
    
    private void insereIcones(){
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    }
    
    private void criaMascaras(){
        
    }
    
    private void carregarTabela() {
        modeloTabelaCompra.Recarregar(controleCompra.pesquisaPersonalizada("tipo = 'Compra'"));
        tbCompras.setModel(modeloTabelaCompra);
    }

    private void carregarTabela(List<OperacaoEntity> filtrar) {
        modeloTabelaCompra.Recarregar(filtrar);
        tbCompras.setModel(modeloTabelaCompra);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private situacaoProdutoS situacaoPS;
    
    private enum situacaoProdutoS {
        produtoS, produtoAS, nadaS
    }
    
    private situacaoFornecedor situacaoF;
    
    private enum situacaoFornecedor{
        fornecedorS, fornecedorD, fornecedorT
    }
    
    private void limparCampos() {
        carregaTabelasProdutos(null);
        tbCompras.clearSelection();
        tbFornecedores.clearSelection();
        carregarTabelaFornecedor();
        mostraPDproduto(null);
        txtCriterioF.setText("");
        txtCriterioP.setText("");
        cbxCamposF.setSelectedIndex(-1);
        cbxCamposP.setSelectedIndex(-1);
    }
    
    public void controlaCamposFiltroP(){
        
        if(true == (modeloTabelaProdutoCarrinoA.getRowCount() == 0 || (situacao != situacaoCadastro.scEditando && situacao != situacaoCadastro.scInserindo))){

            cbxCamposP.setEnabled(false);
            txtCriterioP.setEnabled(false);
            btFiltrarP.setEnabled(false);
            btPesquisarP.setEnabled(false);

        }else{

            cbxCamposP.setEnabled(true);
            txtCriterioP.setEnabled(true);
            btFiltrarP.setEnabled(true);
            btPesquisarP.setEnabled(true);

        }
        
    }
    
    private void controlarCampos(boolean condicao) {
        tbProdutos.setEnabled(condicao);
        tbProdutosA.setEnabled(condicao);
        tbCompras.setEnabled(!condicao);
        controlaCamposFiltroP();
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
        objetoCompra = modeloTabelaCompra.getObjeto(tbCompras.getSelectedRow()).getClone();
        carregaTabelasProdutos(objetoCompra.getProdutos());
        PessoaJuridicaEntity f = new PessoaJuridicaBLL().pesquisar(objetoCompra.getPessoa().getId());
        List<PessoaJuridicaEntity> fl = new ArrayList<>();
        fl.add(f);
        carregarTabelaFornecedor(fl);
    }
    
    private void preencherFiltroCampos() {

        cbxCampos.removeAllItems();

        for (int i = 0; i < modeloTabelaCompra.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaCompra.getColumnName(i), modeloTabelaCompra.getnomeTabela(i),
                modeloTabelaCompra.getNomeColunaTabela(i),
                modeloTabelaCompra.getTipoColunaTabela(i))
            );
        }

    }
    
    private void preencherFiltroCamposF() {

        cbxCamposF.removeAllItems();

        for (int i = 0; i < modeloTabelaFornecedor.getColumnCount(); i++) {
            cbxCamposF.addItem(new CampoFiltro(
                modeloTabelaFornecedor.getColumnName(i), modeloTabelaFornecedor.getnomeTabela(i),
                modeloTabelaFornecedor.getNomeColunaTabela(i),
                modeloTabelaFornecedor.getTipoColunaTabela(i))
            );
        }

    }
    
    private void preencherFiltroCamposP() {

        cbxCamposP.removeAllItems();

        for (int i = 0; i < modeloTabelaProdutoCarrinoA.getColumnCount(); i++) {
            cbxCamposP.addItem(new CampoFiltro(
                modeloTabelaProdutoCarrinoA.getColumnName(i), modeloTabelaProdutoCarrinoA.getnomeTabela(i),
                modeloTabelaProdutoCarrinoA.getNomeColunaTabela(i),
                modeloTabelaProdutoCarrinoA.getTipoColunaTabela(i))
            );
        }

    }
    
    private situacaoCadastro situacao;
    private OperacaoEntity objetoCompra;
    private final OperacaoBLL controleCompra = new OperacaoBLL();
    private final CompraTableModel modeloTabelaCompra = new CompraTableModel();
    private final ProdutoCompraCarrinoTableModel modeloTabelaProdutoCarrino = new ProdutoCompraCarrinoTableModel();
    private final ProdutoCompraCarrinoATableModel modeloTabelaProdutoCarrinoA = new ProdutoCompraCarrinoATableModel();
    private final PessoaJuridicaTableModel modeloTabelaFornecedor = new PessoaJuridicaTableModel();
    private List<OperacaoProdutosEntity> produtosTB;
    private List<ProdutoEntity> produtosATB;
    
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
        jPanel2 = new javax.swing.JPanel();
        btAddP = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbProdutosA = new javax.swing.JTable();
        btAltQTD = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbProdutos = new javax.swing.JTable();
        btDelP = new javax.swing.JButton();
        btInfoP = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        btAltvl = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        cbxCamposP = new javax.swing.JComboBox();
        btFiltrarP = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtCriterioP = new javax.swing.JTextField();
        btPesquisarP = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btDeselecionarF = new javax.swing.JButton();
        spTbf = new javax.swing.JScrollPane();
        tbFornecedores = new javax.swing.JTable();
        btSelecionarF = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btFiltrarF = new javax.swing.JButton();
        txtCriterioF = new javax.swing.JTextField();
        cbxCamposF = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        btListarF = new javax.swing.JButton();
        pnDadosCompra = new javax.swing.JPanel();
        lbTitDC = new javax.swing.JLabel();
        lbVTDC = new javax.swing.JLabel();
        lbQTDC = new javax.swing.JLabel();
        lbVTDCI = new javax.swing.JLabel();
        lbQTDCI = new javax.swing.JLabel();
        pnDP = new javax.swing.JPanel();
        pnIMG = new utility.PanelVisualizaImagem();
        lbTPP = new javax.swing.JLabel();
        lbTPPI = new javax.swing.JLabel();
        spTabela = new javax.swing.JScrollPane();
        tbCompras = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Operação de compra");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package.png"))); // NOI18N
        btNovo.setText("Novo");
        btNovo.setName("btNovoCompra"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_add.png"))); // NOI18N
        btSalvar.setText("Salvar");
        btSalvar.setName("btSalvarCompra"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_link.png"))); // NOI18N
        btEditar.setText("Editar");
        btEditar.setName("btEditarCompra"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_delete.png"))); // NOI18N
        btExcluir.setText("Excluir");
        btExcluir.setName("btExcluirCompra"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setText("Cancelar");
        btCancelar.setName("btCancelarCompra"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
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

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(0));

        btAddP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/arrow_left.png"))); // NOI18N
        btAddP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddPActionPerformed(evt);
            }
        });

        tbProdutosA.setModel(new javax.swing.table.DefaultTableModel(
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
        tbProdutosA.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbProdutosA.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbProdutosAMousePressed(evt);
            }
        });
        tbProdutosA.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbProdutosAKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbProdutosA);

        btAltQTD.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit.png"))); // NOI18N
        btAltQTD.setText("Alterar quantidade");
        btAltQTD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAltQTDActionPerformed(evt);
            }
        });

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
        jScrollPane2.setViewportView(tbProdutos);

        btDelP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/arrow_right.png"))); // NOI18N
        btDelP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDelPActionPerformed(evt);
            }
        });

        btInfoP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btInfoP.setText("Info");
        btInfoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btInfoPActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel2.setText("Produtos da compra:");

        btAltvl.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit.png"))); // NOI18N
        btAltvl.setText("Alterar preço");
        btAltvl.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAltvlActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btFiltrarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png"))); // NOI18N
        btFiltrarP.setText("Filtrar");
        btFiltrarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarPActionPerformed(evt);
            }
        });

        jLabel4.setText("Critério:");

        btPesquisarP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btPesquisarP.setText("Pesquisar");
        btPesquisarP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPesquisarPActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(cbxCamposP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterioP)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btPesquisarP)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btFiltrarP)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbxCamposP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4)
                    .add(txtCriterioP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFiltrarP)
                    .add(btPesquisarP))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 490, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btDelP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btAltQTD, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btInfoP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btAddP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btAltvl, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 544, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(btAddP)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btDelP)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btAltQTD)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btAltvl)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btInfoP)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(0));

        btDeselecionarF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/decline.png"))); // NOI18N
        btDeselecionarF.setText("Deselecionar");
        btDeselecionarF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeselecionarFActionPerformed(evt);
            }
        });

        tbFornecedores.setModel(new javax.swing.table.DefaultTableModel(
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
        tbFornecedores.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbFornecedoresMousePressed(evt);
            }
        });
        tbFornecedores.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbFornecedoresKeyReleased(evt);
            }
        });
        spTbf.setViewportView(tbFornecedores);

        btSelecionarF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/tick.png"))); // NOI18N
        btSelecionarF.setText("Selecionar");
        btSelecionarF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelecionarFActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Fornecedor dos produtos:");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btFiltrarF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btFiltrarF.setText("Filtrar");
        btFiltrarF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFiltrarFActionPerformed(evt);
            }
        });

        jLabel5.setText("Crtitério:");

        btListarF.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png"))); // NOI18N
        btListarF.setText("Listar");
        btListarF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarFActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(cbxCamposF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 151, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterioF)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btFiltrarF)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btListarF)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbxCamposF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCriterioF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btFiltrarF)
                    .add(jLabel5)
                    .add(btListarF))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(spTbf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 442, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btSelecionarF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(btDeselecionarF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel3)
                        .add(0, 0, Short.MAX_VALUE))
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(btSelecionarF)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btDeselecionarF))
                    .add(spTbf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnDadosCompra.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTitDC.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbTitDC.setText("Dados da compra:");

        lbVTDC.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbVTDC.setText("Valor total:");

        lbQTDC.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbQTDC.setText("Quantidade total de produtos:");

        lbVTDCI.setBackground(new java.awt.Color(255, 255, 255));
        lbVTDCI.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbVTDCI.setForeground(new java.awt.Color(0, 0, 255));
        lbVTDCI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbVTDCI.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lbVTDCI.setOpaque(true);

        lbQTDCI.setBackground(new java.awt.Color(255, 255, 255));
        lbQTDCI.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        lbQTDCI.setForeground(new java.awt.Color(0, 0, 255));
        lbQTDCI.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbQTDCI.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));
        lbQTDCI.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        lbQTDCI.setOpaque(true);

        org.jdesktop.layout.GroupLayout pnDadosCompraLayout = new org.jdesktop.layout.GroupLayout(pnDadosCompra);
        pnDadosCompra.setLayout(pnDadosCompraLayout);
        pnDadosCompraLayout.setHorizontalGroup(
            pnDadosCompraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnDadosCompraLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnDadosCompraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lbVTDCI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lbTitDC)
                    .add(lbVTDC)
                    .add(lbQTDC)
                    .add(lbQTDCI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnDadosCompraLayout.setVerticalGroup(
            pnDadosCompraLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnDadosCompraLayout.createSequentialGroup()
                .addContainerGap()
                .add(lbTitDC)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbVTDC)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbVTDCI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbQTDC)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(lbQTDCI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 55, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pnDP.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnDP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnDPMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                pnDPMousePressed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout pnIMGLayout = new org.jdesktop.layout.GroupLayout(pnIMG);
        pnIMG.setLayout(pnIMGLayout);
        pnIMGLayout.setHorizontalGroup(
            pnIMGLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );
        pnIMGLayout.setVerticalGroup(
            pnIMGLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 0, Short.MAX_VALUE)
        );

        lbTPP.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lbTPP.setText("Tipo de venda:");

        lbTPPI.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N

        org.jdesktop.layout.GroupLayout pnDPLayout = new org.jdesktop.layout.GroupLayout(pnDP);
        pnDP.setLayout(pnDPLayout);
        pnDPLayout.setHorizontalGroup(
            pnDPLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnDPLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnDPLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnIMG, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnDPLayout.createSequentialGroup()
                        .add(lbTPP)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(lbTPPI, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 163, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnDPLayout.setVerticalGroup(
            pnDPLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnDPLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnIMG, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnDPLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(lbTPP)
                    .add(lbTPPI))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(pnDadosCompra, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                        .add(pnDP, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnDadosCompra, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnDP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(33, 33, 33))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbCompras.setModel(new javax.swing.table.DefaultTableModel(
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
        tbCompras.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbCompras.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbComprasMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbComprasMouseReleased(evt);
            }
        });
        tbCompras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbComprasKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbCompras);

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
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 376, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 496, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 91, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnFiltros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void tbProdutosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProdutosMousePressed
        
        if(tbProdutos.isEnabled()){
            tbProdutosA.clearSelection();
            situacaoPS = situacaoProdutoS.produtoS;
            controleBotoesProduto();
            if(tbProdutos.getSelectedRow() != -1){
                OperacaoProdutosEntity op = modeloTabelaProdutoCarrino.getObjeto(tbProdutos.getSelectedRow());
                ProdutoEntity p = op.getProduto();
                mostraPDproduto(p);
            }
        }
        
    }//GEN-LAST:event_tbProdutosMousePressed

    private void tbProdutosAMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProdutosAMousePressed
        
        if(tbProdutosA.isEnabled()){
            tbProdutos.clearSelection();
            situacaoPS = situacaoProdutoS.produtoAS;
            controleBotoesProduto();
            if(tbProdutosA.getSelectedRow() != -1){
                ProdutoEntity p = modeloTabelaProdutoCarrinoA.getObjeto(tbProdutosA.getSelectedRow());
                mostraPDproduto(p);
            }
        }
        
    }//GEN-LAST:event_tbProdutosAMousePressed

    private void btInfoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btInfoPActionPerformed
        
        if(tbProdutosA.getSelectedRow() != -1){
            
            ProdutoEntity p = modeloTabelaProdutoCarrinoA.getObjeto(tbProdutosA.getSelectedRow());
            String inf = "Nome: "+p.getNome()
                    + "\nTipo de venda: "+p.getTipoVenda()
                    + "\nCategoria: "+p.getCategoria().getNome()
                    + "\nValor: R$ "+p.getValor()
                    + "\nDescrição: "+p.getDescricao();
            JOptionPane.showMessageDialog(null, inf, "Informações do poduto selecionado", JOptionPane.INFORMATION_MESSAGE);
            
        }else{
            if(tbProdutos.getSelectedRow() != -1){
                
                OperacaoProdutosEntity op = modeloTabelaProdutoCarrino.getObjeto(tbProdutos.getSelectedRow());
                ProdutoEntity p = op.getProduto();
                String inf = "Nome: "+p.getNome()
                        + "\nTipo de venda: "+p.getTipoVenda()
                        + "\nCategoria: "+p.getCategoria().getNome()
                        + "\nValor: R$ "+p.getValor()
                        + "\nDescrição: "+p.getDescricao();
                JOptionPane.showMessageDialog(null, inf, "Informações do poduto selecionado", JOptionPane.INFORMATION_MESSAGE);
                
            }else{
                JOptionPane.showMessageDialog(null, "Nenhum produto selecionado.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_btInfoPActionPerformed

    private void btAddPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddPActionPerformed
        
        if(tbProdutosA.getSelectedRow() != -1){
            
            ProdutoEntity p = modeloTabelaProdutoCarrinoA.getObjeto(tbProdutosA.getSelectedRow());
            
            try{
                if("Unidade".equals(p.getTipoVenda())){
                    int q = Integer.parseInt(JOptionPane.showInputDialog(null,"Quantidade de unidades do produto que será comprada:","INFORE A QUANTIDADE",JOptionPane.QUESTION_MESSAGE));
                    if(q > 0){
                        double v = Double.parseDouble(JOptionPane.showInputDialog(null,"Valor do produto que será comprado:","INFORE O PREÇO",JOptionPane.QUESTION_MESSAGE));
                        if(v > 0){
                            OperacaoProdutosEntity op = new OperacaoProdutosEntity();
                            op.setProduto(p);
                            op.setQtd(q);
                            op.setTipovenda(p.getTipoVenda());
                            op.setValorUnitario(v);
                            op.setValorOferta(0);
                            produtosTB.add(op);
                            carregaTabelasProdutos(produtosTB);
                            mostraPDproduto(null);
                            controlaCamposFiltroP();
                        }else{
                            JOptionPane.showMessageDialog(null,"Preço inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Quantidade inválida.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    double q = Double.parseDouble(JOptionPane.showInputDialog(null,"Quantidade de quilos do produto que será comprada:","INFORE A QUANTIDADE",JOptionPane.QUESTION_MESSAGE));
                    if(q > 0){
                        double v = Double.parseDouble(JOptionPane.showInputDialog(null,"Valor do produto que será comprado:","INFORE O PREÇO",JOptionPane.QUESTION_MESSAGE));
                        if(v > 0){
                            OperacaoProdutosEntity op = new OperacaoProdutosEntity();
                            op.setProduto(p);
                            op.setQtd(q);
                            op.setTipovenda(p.getTipoVenda());
                            op.setValorUnitario(v);
                            op.setValorOferta(0);
                            produtosTB.add(op);
                            carregaTabelasProdutos(produtosTB);
                            mostraPDproduto(null);
                            controlaCamposFiltroP();
                        }else{
                            JOptionPane.showMessageDialog(null,"Preço inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Quantidade inválida.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }
                JOptionPane.showMessageDialog(null, "Produto adicionado.", "OK", JOptionPane.PLAIN_MESSAGE);
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Dado inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Nenhum produto selecionado.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btAddPActionPerformed

    private void btAltQTDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAltQTDActionPerformed
        
        if(tbProdutos.getSelectedRow() != -1){
            
            int x = tbProdutos.getSelectedRow();
            
            try{
                if("Unidade".equals(produtosTB.get(x).getTipovenda())){

                    String resp = JOptionPane.showInputDialog(null,"Quantidade de unidades do produto que será comprada:","INFORME A QUANTIDADE",JOptionPane.QUESTION_MESSAGE);
                    if(resp != null){
                        int q = Integer.parseInt(resp);
                        if(q > 0){
                            produtosTB.get(x).setQtd(q);
                            carregaTabelasProdutos(produtosTB);
                            JOptionPane.showMessageDialog(null, "Quantidade alterada.", "OK", JOptionPane.PLAIN_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, "Quantidade inválida.", "ERRO", JOptionPane.ERROR_MESSAGE); 
                        }
                    }
                    
                }else{

                    String resp = JOptionPane.showInputDialog(null,"Quantidade de quilos do produto que será comprada:","INFORME A QUANTIDADE",JOptionPane.QUESTION_MESSAGE);
                    if(resp != null){
                        double q = Double.parseDouble(resp);
                        if(q > 0){
                            produtosTB.get(x).setQtd(q);
                            carregaTabelasProdutos(produtosTB);
                            JOptionPane.showMessageDialog(null, "Quantidade alterada.", "OK", JOptionPane.PLAIN_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null, "Quantidade inválida.", "ERRO", JOptionPane.ERROR_MESSAGE); 
                        }
                    }
                    
                }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Quantidade inválida.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Nenhum produto selecionado.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btAltQTDActionPerformed

    private void btDelPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDelPActionPerformed
        
        if(tbProdutos.getSelectedRow() != -1){
            
            int x = tbProdutos.getSelectedRow();
            
            produtosTB.remove(x);
            carregaTabelasProdutos(produtosTB);
            JOptionPane.showMessageDialog(null, "Produto removido.", "OK", JOptionPane.PLAIN_MESSAGE);
            controlaCamposFiltroP();
            
        }else{
            JOptionPane.showMessageDialog(null, "Nenhum produto selecionado.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btDelPActionPerformed

    private void btNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoActionPerformed
        
        limparCampos();
        situacao = situacaoCadastro.scInserindo;
        controlarBotoes();
        controlarCampos(true);
        situacaoF = situacaoFornecedor.fornecedorD;
        controlaBotoesFornecedor();
        objetoCompra = new OperacaoEntity();
        objetoCompra.setId(0);
        
    }//GEN-LAST:event_btNovoActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        situacao = situacaoCadastro.scEditando;
        controlarBotoes();
        controlarCampos(true);
        situacaoF = situacaoFornecedor.fornecedorS;
        controlaBotoesFornecedor();
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Você está prestes a excluír o produto do sistema e perder todos os seus dados.\nDeseja mesmo excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
            
            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu uma operação'").get(0));
            ul.setDescricao("Excluiu a compra de ID = "+objetoCompra.getId());
            ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
            ul.setHorario(DataUtility.getDateTime("EN"));
            new UsuarioAcaoBLL().salvar(ul);
            
            controleCompra.excluir(objetoCompra);
            carregarTabela();
            limparCampos();
            situacao = situacaoCadastro.scEsperando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        situacaoF = situacaoFornecedor.fornecedorT;
        controlaBotoesFornecedor();
        objetoCompra = null;
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btFiltrarFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFiltrarFActionPerformed
        
        CriterioFiltro criterio = new CriterioFiltro();

        criterio.setCampo((CampoFiltro) cbxCamposF.getSelectedItem());
        criterio.setCriterio(txtCriterioF.getText().trim());

        if("int".equals(criterio.getCampo().getTipoColuna())){
            try{
                Integer.parseInt(criterio.getCriterio());
                carregarTabelaFornecedor(new PessoaJuridicaBLL().filtrar(criterio));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            if("string".equals(criterio.getCampo().getTipoColuna())){
                carregarTabelaFornecedor(new PessoaJuridicaBLL().filtrar(criterio));
            }else{
                if("double".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        criterio.setCriterio(String.valueOf(Double.parseDouble(criterio.getCriterio().replace(",", "."))));
                        carregarTabelaFornecedor(new PessoaJuridicaBLL().filtrar(criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
        controlaBotoesFornecedor();
        
    }//GEN-LAST:event_btFiltrarFActionPerformed

    private void btListarFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarFActionPerformed
        
        carregarTabelaFornecedor();
        controlaBotoesFornecedor();
        
    }//GEN-LAST:event_btListarFActionPerformed

    private void btDeselecionarFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeselecionarFActionPerformed
        
        situacaoF = situacaoFornecedor.fornecedorD;
        controlaBotoesFornecedor();
        carregarTabelaFornecedor();
        objetoCompra.setPessoa(null);
        
    }//GEN-LAST:event_btDeselecionarFActionPerformed

    private void btSelecionarFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelecionarFActionPerformed
        
        if(tbFornecedores.getSelectedRow() != -1){
            
            PessoaJuridicaEntity fs = modeloTabelaFornecedor.getObjeto(tbFornecedores.getSelectedRow());
            List<PessoaJuridicaEntity> fl = new ArrayList<>();
            fl.add(fs);
            objetoCompra.setPessoa(fs.getPessoa());
            carregarTabelaFornecedor(fl);
            situacaoF = situacaoFornecedor.fornecedorS;
            controlaBotoesFornecedor();
            JOptionPane.showMessageDialog(null,"Fornecedor selecionado.","OK",JOptionPane.PLAIN_MESSAGE);
            
        }else{
            JOptionPane.showMessageDialog(null,"Selecione um fornecedor na tabela.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSelecionarFActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
        if(objetoCompra.getPessoa() != null){
            
            if(!produtosTB.isEmpty()){
                
                objetoCompra.setProdutos(produtosTB);
                
                if(situacao == situacaoCadastro.scInserindo){
                    objetoCompra.setData(DataUtility.getDateTime("EN"));
                }
                
                if(objetoCompra.getData() != null){
                    
                    objetoCompra.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                    objetoCompra.setTipo("Compra");
                    
                    controleCompra.salvar(objetoCompra);
                    
                    if(situacao == situacaoCadastro.scInserindo){
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou uma operação'").get(0));
                        ul.setDescricao("Realizou a compra de ID = "+objetoCompra.getId());
                        ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                        ul.setHorario(DataUtility.getDateTime("EN"));
                        new UsuarioAcaoBLL().salvar(ul);
                        
                        JOptionPane.showMessageDialog(null,"Registro salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou uma operação'").get(0));
                        ul.setDescricao("Alterou a compra de ID = "+objetoCompra.getId());
                        ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                        ul.setHorario(DataUtility.getDateTime("EN"));
                        new UsuarioAcaoBLL().salvar(ul);
                        
                        JOptionPane.showMessageDialog(null,"Registro atualizado com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                    }
                    
                    carregarTabela();
                    situacao = situacaoCadastro.scEsperando;
                    controlarBotoes();
                    controlarCampos(false);
                    limparCampos();
                    situacaoF = situacaoFornecedor.fornecedorT;
                    controlaBotoesFornecedor();
                    objetoCompra = null;
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Erro ao obter a data atual do sistema.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
                
            }else{
                JOptionPane.showMessageDialog(null,"Adicione produtos a compra.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Escolha um fornecedor para a compra.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void tbComprasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbComprasMouseReleased
        
    }//GEN-LAST:event_tbComprasMouseReleased

    private void tbComprasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbComprasMousePressed
        
        if(tbCompras.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbComprasMousePressed

    private void pnDPMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnDPMousePressed
        
        
        
    }//GEN-LAST:event_pnDPMousePressed

    private void pnDPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnDPMouseClicked
        
        
        
    }//GEN-LAST:event_pnDPMouseClicked

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbCompras, new File(caminho));
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
        situacaoF = situacaoFornecedor.fornecedorT;
        controlaBotoesFornecedor();
        objetoCompra = null;
        
        CriterioFiltro criterio = new CriterioFiltro();

        criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
        criterio.setCriterio(txtCriterio.getText().trim());
        String sql = "select * from operacao as O, usuarios as U, pessoas as PU , pessoas as PP "
                + "where O.idpessoa = PP.id "
                + "and O.idusuario = U.id "
                + "and U.pessoa = PU.id "
                + "and O.tipo = 'Compra'";

        if("int".equals(criterio.getCampo().getTipoColuna())){
            try{
                Integer.parseInt(criterio.getCriterio());
                carregarTabela(new OperacaoBLL().filtrar(sql,criterio));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            if("string".equals(criterio.getCampo().getTipoColuna())){
                carregarTabela(new OperacaoBLL().filtrar(sql,criterio));
            }else{
                if("double".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        criterio.setCriterio(String.valueOf(Double.parseDouble(criterio.getCriterio().replace(",", "."))));
                        carregarTabela(new OperacaoBLL().filtrar(sql,criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if("datetime".equals(criterio.getCampo().getTipoColuna())){
                        carregarTabela(new OperacaoBLL().filtrar(sql,criterio));
                    }
                }
            }
        }
        
    }//GEN-LAST:event_btFiltrarActionPerformed

    private void btListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarActionPerformed
        
        carregarTabela();
        
    }//GEN-LAST:event_btListarActionPerformed

    private void btAltvlActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAltvlActionPerformed
        
        if(tbProdutos.getSelectedRow() != -1){
            
            int x = tbProdutos.getSelectedRow();
            
            try{
                if("Unidade".equals(produtosTB.get(x).getTipovenda())){

                    String resp = JOptionPane.showInputDialog(null,"Valor do produto que será comprado:","INFORME O VALOR",JOptionPane.QUESTION_MESSAGE);
                    if(resp != null){
                        double v = Double.parseDouble(resp);
                        if(v > 0){
                            produtosTB.get(x).setValorUnitario(v);
                            carregaTabelasProdutos(produtosTB);
                            JOptionPane.showMessageDialog(null,"Valor alterado.","OK",JOptionPane.PLAIN_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null,"Valor inválido.","ERRO",JOptionPane.ERROR_MESSAGE); 
                        }
                    }
                    
                }else{

                    String resp = JOptionPane.showInputDialog(null,"Valor do produto que será comprado:","INFORME O VALOR",JOptionPane.QUESTION_MESSAGE);
                    if(resp != null){
                        double v = Double.parseDouble(resp);
                        if(v > 0){
                            produtosTB.get(x).setValorUnitario(v);
                            carregaTabelasProdutos(produtosTB);
                            JOptionPane.showMessageDialog(null,"Valor alterado.","OK",JOptionPane.PLAIN_MESSAGE);
                        }else{
                            JOptionPane.showMessageDialog(null,"Valor inválido.","ERRO",JOptionPane.ERROR_MESSAGE); 
                        }
                    }
                    
                }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Nenhum produto selecionado.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btAltvlActionPerformed

    private void btPesquisarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPesquisarPActionPerformed
        
        CriterioFiltro criterio = new CriterioFiltro();

        criterio.setCampo((CampoFiltro) cbxCamposP.getSelectedItem());
        criterio.setCriterio(txtCriterioP.getText().trim());

        if("int".equals(criterio.getCampo().getTipoColuna())){
            try{
                Integer.parseInt(criterio.getCriterio());
                filtrarTabelaProdutoA(new ProdutoBLL().filtrar(criterio));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            if("string".equals(criterio.getCampo().getTipoColuna())){
                filtrarTabelaProdutoA(new ProdutoBLL().filtrar(criterio));
            }else{
                if("double".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        criterio.setCriterio(String.valueOf(Double.parseDouble(criterio.getCriterio().replace(",", "."))));
                        filtrarTabelaProdutoA(new ProdutoBLL().filtrar(criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
        
    }//GEN-LAST:event_btPesquisarPActionPerformed

    private void btFiltrarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFiltrarPActionPerformed
        
        filtrarTabelaProdutoA(new ProdutoBLL().listarTodos());
        
    }//GEN-LAST:event_btFiltrarPActionPerformed

    private void tbFornecedoresMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbFornecedoresMousePressed
        
        controlaBotoesFornecedor();
        
    }//GEN-LAST:event_tbFornecedoresMousePressed

    private void tbComprasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbComprasKeyReleased
        
        if(tbCompras.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbComprasKeyReleased

    private void tbProdutosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbProdutosKeyReleased
        
        if(tbProdutos.isEnabled()){
            tbProdutosA.clearSelection();
            situacaoPS = situacaoProdutoS.produtoS;
            controleBotoesProduto();
            if(tbProdutos.getSelectedRow() != -1){
                OperacaoProdutosEntity op = modeloTabelaProdutoCarrino.getObjeto(tbProdutos.getSelectedRow());
                ProdutoEntity p = op.getProduto();
                mostraPDproduto(p);
            }
        }
        
    }//GEN-LAST:event_tbProdutosKeyReleased

    private void tbProdutosAKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbProdutosAKeyReleased
        
        if(tbProdutosA.isEnabled()){
            tbProdutos.clearSelection();
            situacaoPS = situacaoProdutoS.produtoAS;
            controleBotoesProduto();
            if(tbProdutosA.getSelectedRow() != -1){
                ProdutoEntity p = modeloTabelaProdutoCarrinoA.getObjeto(tbProdutosA.getSelectedRow());
                mostraPDproduto(p);
            }
        }
        
    }//GEN-LAST:event_tbProdutosAKeyReleased

    private void tbFornecedoresKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbFornecedoresKeyReleased
       
        controlaBotoesFornecedor();
        
    }//GEN-LAST:event_tbFornecedoresKeyReleased
    
    public final void controlaBotoesFornecedor(){
        
        if(new PessoaJuridicaBLL().listarTodos().size() > 0){
            btListarF.setEnabled(situacaoF == situacaoFornecedor.fornecedorD);
            btFiltrarF.setEnabled(situacaoF == situacaoFornecedor.fornecedorD);
            cbxCamposF.setEnabled(situacaoF == situacaoFornecedor.fornecedorD);
            txtCriterioF.setEnabled(situacaoF == situacaoFornecedor.fornecedorD);
        }else{
            btListarF.setEnabled(false);
            btFiltrarF.setEnabled(false);
            cbxCamposF.setEnabled(false);
            txtCriterioF.setEnabled(false);
        }
        
        if(tbFornecedores.getSelectedRow() != -1)
            btSelecionarF.setEnabled(situacaoF == situacaoFornecedor.fornecedorD);
        else
            btSelecionarF.setEnabled(false);
            
        btDeselecionarF.setEnabled(situacaoF == situacaoFornecedor.fornecedorS);
        tbFornecedores.setEnabled(situacaoF == situacaoFornecedor.fornecedorD);
        
    }
    
    public final void controleBotoesProduto(){
        btDelP.setEnabled(situacaoPS == situacaoProdutoS.produtoS);
        btAddP.setEnabled(situacaoPS == situacaoProdutoS.produtoAS);
        btAltQTD.setEnabled(situacaoPS == situacaoProdutoS.produtoS);
        btAltvl.setEnabled(situacaoPS == situacaoProdutoS.produtoS);
        btInfoP.setEnabled(situacaoPS == situacaoProdutoS.produtoS || situacaoPS == situacaoProdutoS.produtoAS);
    }
    
    public final void filtrarTabelaProdutoA(List<ProdutoEntity> produtos){
        
        if(modeloTabelaProdutoCarrino.getRowCount() == 0){
            
            modeloTabelaProdutoCarrinoA.Recarregar(produtos);
            tbProdutosA.setModel(modeloTabelaProdutoCarrinoA);
            produtosATB = new ArrayList<>();
            produtosATB.addAll(produtos);
            
        }else{
            
            int ti = modeloTabelaProdutoCarrino.getRowCount();
            
            for(int x = (produtos.size()-1) ; x >= 0 ; x--){
                for(int y = (ti-1) ; y >= 0 ; y--){
                    if(modeloTabelaProdutoCarrino.getObjeto(y).getProduto().getId() == produtos.get(x).getId()){
                        produtos.remove(x);
                        break;
                    }
                }
            }
            
            modeloTabelaProdutoCarrinoA.Recarregar(produtos);
            tbProdutosA.setModel(modeloTabelaProdutoCarrinoA);
            produtosATB = new ArrayList<>();
            produtosATB.addAll(produtos);
            
        }
        
    }
    
    public final void carregaTabelasProdutos(List<OperacaoProdutosEntity> produtos){
        
        if(produtos == null || produtos.isEmpty()){
            modeloTabelaProdutoCarrino.Recarregar(null);
            tbProdutos.setModel(modeloTabelaProdutoCarrino);
            modeloTabelaProdutoCarrinoA.Recarregar(new ProdutoBLL().listarTodos());
            tbProdutosA.setModel(modeloTabelaProdutoCarrinoA);
            produtosATB = new ArrayList<>();
            produtosATB.addAll(new ProdutoBLL().listarTodos());
            produtosTB = new ArrayList<>();
        }else{
            modeloTabelaProdutoCarrino.Recarregar(produtos);
            tbProdutos.setModel(modeloTabelaProdutoCarrino);
            
            List<ProdutoEntity> p = new ProdutoBLL().listarTodos();
            for(int x = (p.size()-1) ; x >= 0 ; x--){
                for(int y = (produtos.size()-1) ; y >= 0  ; y--){
                    if(produtos.get(y).getProduto().getId() == p.get(x).getId()){
                        p.remove(x);
                        break;
                    }
                }
            }
            
            modeloTabelaProdutoCarrinoA.Recarregar(p);
            tbProdutosA.setModel(modeloTabelaProdutoCarrinoA);
            produtosATB = new ArrayList<>();
            produtosATB.addAll(p);
            produtosTB = new ArrayList<>();
            produtosTB.addAll(produtos);
        }
        
        situacaoPS = situacaoProdutoS.nadaS;
        controleBotoesProduto();
        mostraPDCompra();
        mostraPDproduto(null);
        
    }
    
    public void mostraPDCompra(){
        
        if(modeloTabelaProdutoCarrino.getRowCount() > 0){
            
            pnDadosCompra.setVisible(true);
            lbQTDC.setVisible(true);
            lbQTDCI.setVisible(true);
            lbTitDC.setVisible(true);
            lbVTDC.setVisible(true);
            lbVTDCI.setVisible(true);
            
            pnDadosCompra.setEnabled(true);
            lbQTDC.setEnabled(true);
            lbQTDCI.setEnabled(true);
            lbTitDC.setEnabled(true);
            lbVTDC.setEnabled(true);
            lbVTDCI.setEnabled(true);
            
            lbQTDCI.setText(String.valueOf(modeloTabelaProdutoCarrino.getRowCount()));
            
            double vlt = 0;
            for(int x = 0 ; x<modeloTabelaProdutoCarrino.getRowCount() ; x++){
                OperacaoProdutosEntity op = modeloTabelaProdutoCarrino.getObjeto(x);
                double vlr = op.getValorUnitario()*op.getQtd();
                vlt += vlr;
            }
            
            lbVTDCI.setText("R$ "+vlt);
            
        }else{
            
            pnDadosCompra.setVisible(false);
            lbQTDC.setVisible(false);
            lbQTDCI.setVisible(false);
            lbTitDC.setVisible(false);
            lbVTDC.setVisible(false);
            lbVTDCI.setVisible(false);
            
            pnDadosCompra.setEnabled(false);
            lbQTDC.setEnabled(false);
            lbQTDCI.setEnabled(false);
            lbTitDC.setEnabled(false);
            lbVTDC.setEnabled(false);
            lbVTDCI.setEnabled(false);
            
        }
        
    }
    
    public final void mostraPDproduto(ProdutoEntity p){
        if(p != null){
            
            pnDP.setVisible(true);
            lbTPP.setVisible(true);
            lbTPPI.setVisible(true);
            pnIMG.setVisible(true);
            
            pnDP.setEnabled(true);
            lbTPP.setEnabled(true);
            lbTPPI.setEnabled(true);
            pnIMG.setEnabled(true);
            
            lbTPPI.setText(p.getTipoVenda());
            pnIMG.setImagem(p.getIcone());
            
        }else{
            
            pnDP.setVisible(false);
            lbTPP.setVisible(false);
            lbTPPI.setVisible(false);
            pnIMG.setVisible(false);
            
            pnDP.setEnabled(false);
            lbTPP.setEnabled(false);
            lbTPPI.setEnabled(false);
            pnIMG.setEnabled(false);
            
        }
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddP;
    private javax.swing.JButton btAltQTD;
    private javax.swing.JButton btAltvl;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btDelP;
    private javax.swing.JButton btDeselecionarF;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btFiltrarF;
    private javax.swing.JButton btFiltrarP;
    private javax.swing.JButton btInfoP;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btListarF;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btPesquisarP;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSelecionarF;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxCamposF;
    private javax.swing.JComboBox cbxCamposP;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lbQTDC;
    private javax.swing.JLabel lbQTDCI;
    private javax.swing.JLabel lbTPP;
    private javax.swing.JLabel lbTPPI;
    private javax.swing.JLabel lbTitDC;
    private javax.swing.JLabel lbVTDC;
    private javax.swing.JLabel lbVTDCI;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnDP;
    private javax.swing.JPanel pnDadosCompra;
    private javax.swing.JPanel pnFiltros;
    private utility.PanelVisualizaImagem pnIMG;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JScrollPane spTbf;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbCompras;
    private javax.swing.JTable tbFornecedores;
    private javax.swing.JTable tbProdutos;
    private javax.swing.JTable tbProdutosA;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtCriterioF;
    private javax.swing.JTextField txtCriterioP;
    // End of variables declaration//GEN-END:variables

}
