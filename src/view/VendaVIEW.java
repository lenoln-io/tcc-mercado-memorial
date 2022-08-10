package view;

import bll.OfertaBLL;
import bll.OperacaoBLL;
import bll.PessoaFisicaBLL;
import bll.PessoaJuridicaBLL;
import bll.ProdutoBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.OfertaEntity;
import entity.OperacaoEntity;
import entity.OperacaoProdutosEntity;
import entity.PessoaFisicaEntity;
import entity.PessoaJuridicaEntity;
import entity.ProdutoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.PessoaFisicaTableModel;
import model.PessoaJuridicaTableModel;
import model.ProdutoVendaCarrinoATableModel;
import model.ProdutoVendaCarrinoTableModel;
import model.VendaTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import utility.Login;


public class VendaVIEW extends javax.swing.JFrame implements IVIEW {
    
    @Override
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

    public VendaVIEW() {
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
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        carregarCbxTpP();
        mostraPDproduto(null);
        preencherFiltroCamposP();
        controlaAcessos();
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
        modeloTabelaVenda.Recarregar(controleVenda.pesquisaPersonalizada("tipo = 'Venda'"));
        tbVendas.setModel(modeloTabelaVenda);
    }

    private void carregarTabela(List<OperacaoEntity> filtrar) {
        modeloTabelaVenda.Recarregar(filtrar);
        tbVendas.setModel(modeloTabelaVenda);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private situacaoProdutoS situacaoPS;
    
    private enum situacaoProdutoS {
        produtoS, produtoAS, nadaS
    }
    
    private void limparCampos() {
        modelPf.Recarregar(null);
        modelPj.Recarregar(null);
        carregaTabelasProdutos(null);
        tbVendas.clearSelection();
        tbClientes.clearSelection();
        mostraPDproduto(null);
        txtCri.setText("");
        txtCriterioP.setText("");
        cbxCam.setSelectedIndex(-1);
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
        tbVendas.setEnabled(!condicao);
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
        objetoVenda = modeloTabelaVenda.getObjeto(tbVendas.getSelectedRow()).getClone();
        carregaTabelasProdutos(objetoVenda.getProdutos());
        if(objetoVenda.getPessoa() != null){
            if(objetoVenda.getPessoa().getId()!= -1){
                List<PessoaJuridicaEntity> res1 = new PessoaJuridicaBLL().pesquisaPersonalizada("pessoa = "+objetoVenda.getPessoa().getId());
                if(res1 != null && !res1.isEmpty()){
                    modelPj.Recarregar(res1);
                    tbClientes.setModel(modelPj);
                    cbxTp.setSelectedItem("Jurídicas");
                    sp = situacaoPessoa.escolhido;
                }else{
                    List<PessoaFisicaEntity> res2 = new PessoaFisicaBLL().pesquisaPersonalizada("pessoa = "+objetoVenda.getPessoa().getId());
                    modelPf.Recarregar(res2);
                    tbClientes.setModel(modelPf);
                    cbxTp.setSelectedItem("Físicas");
                    sp = situacaoPessoa.escolhido;
                }
            }else{
                modelPf.Recarregar(null);
                tbClientes.setModel(modelPf);
                modelPj.Recarregar(null);
                tbClientes.setModel(modelPj);
                cbxTp.setSelectedIndex(-1);
                sp = situacaoPessoa.filtrar;
            }
        }else{
            modelPf.Recarregar(null);
            modelPj.Recarregar(null);
        }
    }
    
    private void preencherFiltroCampos() {

        cbxCampos.removeAllItems();

        for (int i = 0; i < modeloTabelaVenda.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaVenda.getColumnName(i), modeloTabelaVenda.getnomeTabela(i),
                modeloTabelaVenda.getNomeColunaTabela(i),
                modeloTabelaVenda.getTipoColunaTabela(i))
            );
        }

    }
    
    private void preencherFiltroCamposP() {

        cbxCamposP.removeAllItems();

        for (int i = 0; i < (modeloTabelaProdutoCarrinoA.getColumnCount()-1); i++) {
            cbxCamposP.addItem(new CampoFiltro(
                modeloTabelaProdutoCarrinoA.getColumnName(i), modeloTabelaProdutoCarrinoA.getnomeTabela(i),
                modeloTabelaProdutoCarrinoA.getNomeColunaTabela(i),
                modeloTabelaProdutoCarrinoA.getTipoColunaTabela(i))
            );
        }

    }
    
    private situacaoCadastro situacao;
    private OperacaoEntity objetoVenda;
    private final OperacaoBLL controleVenda = new OperacaoBLL();
    private final VendaTableModel modeloTabelaVenda = new VendaTableModel();
    private final ProdutoVendaCarrinoTableModel modeloTabelaProdutoCarrino = new ProdutoVendaCarrinoTableModel();
    private final ProdutoVendaCarrinoATableModel modeloTabelaProdutoCarrinoA = new ProdutoVendaCarrinoATableModel();
    private List<OperacaoProdutosEntity> produtosTB;
    private List<ProdutoEntity> produtosATB;
    
    @Override
    public final void desabilitaBtFecharEAtalhos(){
        this.addWindowListener(new WindowAdapter() {  
            @Override
            public void windowClosing(WindowEvent evt) {
                acaoFechar();
            }  
        });
    }
    
    @Override
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
        jPanel5 = new javax.swing.JPanel();
        cbxCamposP = new javax.swing.JComboBox();
        btFiltrarP = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        txtCriterioP = new javax.swing.JTextField();
        btPesquisarP = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        btDes = new javax.swing.JButton();
        spTbf = new javax.swing.JScrollPane();
        tbClientes = new javax.swing.JTable();
        btSel = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        btPes = new javax.swing.JButton();
        txtCri = new javax.swing.JTextField();
        cbxCam = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        btLis = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        cbxTp = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        btBus = new javax.swing.JButton();
        btCan = new javax.swing.JButton();
        pnDadosVenda = new javax.swing.JPanel();
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
        tbVendas = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Operação de venda");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package.png"))); // NOI18N
        btNovo.setText("Novo");
        btNovo.setName("btNovoVenda"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_add.png"))); // NOI18N
        btSalvar.setText("Salvar");
        btSalvar.setName("btSalvarVenda"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_link.png"))); // NOI18N
        btEditar.setText("Editar");
        btEditar.setName("btEditarVenda"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/package_delete.png"))); // NOI18N
        btExcluir.setText("Excluir");
        btExcluir.setName("btExcluirVenda"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setText("Cancelar");
        btCancelar.setName("btCancelarVenda"); // NOI18N
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
        jLabel2.setText("Produtos da venda:");

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
                            .add(btAddP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jLabel2))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 584, Short.MAX_VALUE)
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
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel2Layout.createSequentialGroup()
                        .add(btAddP)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btDelP)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btAltQTD)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btInfoP))
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(0));

        btDes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/decline.png"))); // NOI18N
        btDes.setText("Deselecionar");
        btDes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDesActionPerformed(evt);
            }
        });

        tbClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tbClientes.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbClientesMousePressed(evt);
            }
        });
        tbClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbClientesKeyReleased(evt);
            }
        });
        spTbf.setViewportView(tbClientes);

        btSel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/tick.png"))); // NOI18N
        btSel.setText("Selecionar");
        btSel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSelActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel3.setText("Cliente da venda:");

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btPes.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btPes.setText("Filtrar");
        btPes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPesActionPerformed(evt);
            }
        });

        jLabel5.setText("Crtitério:");

        btLis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png"))); // NOI18N
        btLis.setText("Listar");
        btLis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLisActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(cbxCam, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 140, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCri)
                .add(4, 4, 4)
                .add(btPes)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btLis)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(cbxCam, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(txtCri, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btPes)
                    .add(jLabel5)
                    .add(btLis))
                .addContainerGap())
        );

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel6.setText("Tipo de pessoa:");

        btBus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png"))); // NOI18N
        btBus.setText("Buscar");
        btBus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btBusActionPerformed(evt);
            }
        });

        btCan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png"))); // NOI18N
        btCan.setText("Cancelar");
        btCan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCanActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel6Layout = new org.jdesktop.layout.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxTp, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btBus, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btCan, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 113, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel6Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel6)
                    .add(cbxTp, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(btBus)
                    .add(btCan))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel6, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(spTbf, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 490, Short.MAX_VALUE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btSel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, btDes, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jLabel3)
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel6, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(btSel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(btDes))
                    .add(spTbf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(34, 34, 34))
        );

        pnDadosVenda.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbTitDC.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lbTitDC.setText("Dados da venda:");

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

        org.jdesktop.layout.GroupLayout pnDadosVendaLayout = new org.jdesktop.layout.GroupLayout(pnDadosVenda);
        pnDadosVenda.setLayout(pnDadosVendaLayout);
        pnDadosVendaLayout.setHorizontalGroup(
            pnDadosVendaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnDadosVendaLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnDadosVendaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(lbVTDCI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(lbTitDC)
                    .add(lbVTDC)
                    .add(lbQTDC)
                    .add(lbQTDCI, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 280, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnDadosVendaLayout.setVerticalGroup(
            pnDadosVendaLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnDadosVendaLayout.createSequentialGroup()
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
                        .add(pnDadosVenda, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
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
                    .add(pnDadosVenda, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnDP, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 230, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(33, 33, 33))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbVendas.setModel(new javax.swing.table.DefaultTableModel(
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
        tbVendas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbVendas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbVendasMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                tbVendasMouseReleased(evt);
            }
        });
        tbVendas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbVendasKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbVendas);

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
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 491, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 96, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                        OperacaoProdutosEntity op = new OperacaoProdutosEntity();
                        op.setProduto(p);
                        op.setQtd(q);
                        op.setTipovenda(p.getTipoVenda());
                        op.setValorUnitario(p.getValor());
                        List<OfertaEntity> resp = new OfertaBLL().pesquisaPersonalizada("produto = "+p.getId()+" and datainicio <= '"+getDate()+"' and datafim >= '"+getDate()+"'");
                        if(resp != null && !resp.isEmpty()){
                            if(resp.get(0).getTipo().equalsIgnoreCase("Vendas online e físicas") || resp.get(0).getTipo().equalsIgnoreCase("Vendas físicas"))
                                op.setValorOferta(resp.get(0).getValor());
                            else
                                op.setValorOferta(0);
                        }else{
                            op.setValorOferta(0);
                        }
                        produtosTB.add(op);
                        carregaTabelasProdutos(produtosTB);
                        mostraPDproduto(null);
                        controlaCamposFiltroP();
                    }else{
                        JOptionPane.showMessageDialog(null,"Quantidade inválida.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    double q = Double.parseDouble(JOptionPane.showInputDialog(null,"Quantidade de quilos do produto que será comprada:","INFORE A QUANTIDADE",JOptionPane.QUESTION_MESSAGE));
                    if(q > 0){
                        OperacaoProdutosEntity op = new OperacaoProdutosEntity();
                        op.setProduto(p);
                        op.setQtd(q);
                        op.setTipovenda(p.getTipoVenda());
                        op.setValorUnitario(p.getValor());
                        List<OfertaEntity> resp = new OfertaBLL().pesquisaPersonalizada("produto = "+p.getId()+" and datainicio <= '"+getDate()+"' and datafim >= '"+getDate()+"'");
                        if(resp != null && !resp.isEmpty()){
                            if(resp.get(0).getTipo().equalsIgnoreCase("Vendas online e físicas") || resp.get(0).getTipo().equalsIgnoreCase("Vendas físicas"))
                                op.setValorOferta(resp.get(0).getValor());
                            else
                                op.setValorOferta(0);
                        }else{
                            op.setValorOferta(0);
                        }
                        produtosTB.add(op);
                        carregaTabelasProdutos(produtosTB);
                        mostraPDproduto(null);
                        controlaCamposFiltroP();
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
        sp = situacaoPessoa.filtrar;
        controlarBotoesP();
        objetoVenda = new OperacaoEntity();
        objetoVenda.setId(0);
        
    }//GEN-LAST:event_btNovoActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        situacao = situacaoCadastro.scEditando;
        controlarBotoes();
        controlarCampos(true);
        controlarBotoesP();
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Você está prestes a excluír o produto do sistema e perder todos os seus dados.\nDeseja mesmo excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
            
            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu uma operação'").get(0));
            ul.setDescricao("Excluiu a venda de ID = "+objetoVenda.getId());
            ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
            ul.setHorario(DataUtility.getDateTime("EN"));
            new UsuarioAcaoBLL().salvar(ul);
            
            controleVenda.excluir(objetoVenda);
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
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        objetoVenda = null;
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btPesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPesActionPerformed
        
        if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
            String sql = "SELECT DISTINCT * FROM pessoafisicas, pessoas WHERE pessoas.id = pessoafisicas.pessoa";
            txtCri.setText(txtCri.getText().trim());
            
            if(!txtCri.getText().equals("")){
                CriterioFiltro criterio = new CriterioFiltro();

                criterio.setCampo((CampoFiltro) cbxCam.getSelectedItem());
                criterio.setCriterio(txtCri.getText());

                if("int".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        Integer.parseInt(criterio.getCriterio());
                        modelPf.Recarregar(new PessoaFisicaBLL().filtrar(sql,criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if("string".equals(criterio.getCampo().getTipoColuna())){
                        modelPf.Recarregar(new PessoaFisicaBLL().filtrar(sql,criterio));
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
                                modelPf.Recarregar(new PessoaFisicaBLL().filtrar(sql,criterio));
                            }
                            catch(ParseException e){
                                JOptionPane.showMessageDialog(null,"Data inválida!!!","ERRO",JOptionPane.ERROR_MESSAGE);
                                txtCri.requestFocus();
                            }
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Informe o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            String sql = "SELECT DISTINCT * FROM pessoajuridicas, pessoas WHERE pessoas.id = pessoajuridicas.pessoa";
            txtCri.setText(txtCri.getText().trim());

            if(!txtCri.getText().equals("")){
                CriterioFiltro criterio = new CriterioFiltro();

                criterio.setCampo((CampoFiltro) cbxCam.getSelectedItem());
                criterio.setCriterio(txtCri.getText());

                if("int".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        Integer.parseInt(criterio.getCriterio());
                        modelPj.Recarregar(new PessoaJuridicaBLL().filtrar(sql, criterio));
                    }
                    catch(NumberFormatException e){
                        JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    if("string".equals(criterio.getCampo().getTipoColuna())){
                        modelPj.Recarregar(new PessoaJuridicaBLL().filtrar(sql, criterio));
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
                                modelPj.Recarregar(new PessoaJuridicaBLL().filtrar(sql, criterio));
                            }
                            catch(ParseException e){
                                JOptionPane.showMessageDialog(null,"Data inválida!!!","ERRO",JOptionPane.ERROR_MESSAGE);
                                txtCriterio.requestFocus();
                            }
                        }
                    }
                }
            }else{
                JOptionPane.showMessageDialog(null,"Informe o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }
        
        controlarBotoesP();
        
    }//GEN-LAST:event_btPesActionPerformed

    private void btLisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLisActionPerformed
        
        if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
            String sql = "SELECT DISTINCT * FROM pessoafisicas, pessoas WHERE pessoafisicas.pessoa = pessoas.id;";
            modelPf.Recarregar(new PessoaFisicaBLL().sqlDeConsultaPersonalizada(sql));
            preencherFiltroCamposF();
        }else{
            String sql = "SELECT DISTINCT * FROM pessoajuridicas, pessoas WHERE pessoas.id = pessoajuridicas.pessoa;";
            modelPj.Recarregar(new PessoaJuridicaBLL().sqlDeConsultaPersonalizada(sql));
            preencherFiltroCamposJ();
        }
        
        controlarBotoesP();
        
    }//GEN-LAST:event_btLisActionPerformed

    private void btDesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDesActionPerformed
        
        modelPj.Recarregar(null);
        modelPf.Recarregar(null);
        if(objetoVenda.getPessoa() != null){
            objetoVenda.setPessoa(null);
        }
        sp = situacaoPessoa.filtrar;
        controlarBotoesP();
        
    }//GEN-LAST:event_btDesActionPerformed

    private void btSelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSelActionPerformed
        
        if(tbClientes.getSelectedRow() != -1){
            if(situacao == situacaoCadastro.scInserindo){
                objetoVenda = new OperacaoEntity();
            }

            if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
                objetoVenda.setPessoa(modelPf.getObjeto(tbClientes.getSelectedRow()).getPessoa());
                List<PessoaFisicaEntity> one = new ArrayList<>();
                one.add(modelPf.getObjeto(tbClientes.getSelectedRow()));
                modelPf.Recarregar(one);
            }else{
                objetoVenda.setPessoa(modelPj.getObjeto(tbClientes.getSelectedRow()).getPessoa());
                List<PessoaJuridicaEntity> one = new ArrayList<>();
                one.add(modelPj.getObjeto(tbClientes.getSelectedRow()));
                modelPj.Recarregar(one);
            }

            sp = situacaoPessoa.escolhido;
            controlarBotoesP();
        }else{
            JOptionPane.showMessageDialog(null, "Selecione uma pesssoa.", "ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSelActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
            
        if(!produtosTB.isEmpty()){

            objetoVenda.setProdutos(produtosTB);

            if(situacao == situacaoCadastro.scInserindo){
                objetoVenda.setData(DataUtility.getDateTime("EN"));
            }

            if(objetoVenda.getData() != null){

                objetoVenda.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                objetoVenda.setTipo("Venda");
                
                int temp = JOptionPane.YES_NO_OPTION;
                if(objetoVenda.getPessoa() == null){
                    temp = JOptionPane.showConfirmDialog(null,"Essa venda está sem cliente associado a ela.\nDeseja continuar a operação mesmo assim?.","AVISO",JOptionPane.WARNING_MESSAGE);
                }

                if(temp == JOptionPane.YES_NO_OPTION){
                    
                    if(objetoVenda.getPessoa() == null){
                        objetoVenda.setPessoa(new PessoaFisicaBLL().pesquisar(-1));
                    }
                    
                    controleVenda.salvar(objetoVenda);

                    if(situacao == situacaoCadastro.scInserindo){
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou uma operação'").get(0));
                        ul.setDescricao("Realizou a venda de ID = "+objetoVenda.getId());
                        ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                        ul.setHorario(DataUtility.getDateTime("EN"));
                        new UsuarioAcaoBLL().salvar(ul);
                        
                        JOptionPane.showMessageDialog(null,"Registro salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou uma operação'").get(0));
                        ul.setDescricao("Alterou a venda de ID = "+objetoVenda.getId());
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
                    sp = situacaoPessoa.trancado;
                    controlarBotoesP();
                    objetoVenda = null;
                }

            }else{
                JOptionPane.showMessageDialog(null,"Erro ao obter a data atual do sistema.","ERRO",JOptionPane.ERROR_MESSAGE);
            }

        }else{
            JOptionPane.showMessageDialog(null,"Adicione produtos a compra.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void tbVendasMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVendasMouseReleased
        
    }//GEN-LAST:event_tbVendasMouseReleased

    private void tbVendasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbVendasMousePressed
        
        if(tbVendas.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbVendasMousePressed

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
                exportarExcel.GerarArquivo(tbVendas, new File(caminho));
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
        sp = situacaoPessoa.trancado;
        controlarBotoesP();
        objetoVenda = null;
        
        CriterioFiltro criterio = new CriterioFiltro();

        criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
        criterio.setCriterio(txtCriterio.getText().trim());
        String sql;
            
        sql = "select * from operacao as O, usuarios as U, pessoas as PU , pessoas as PP "
            + "where O.idpessoa = PP.id "
            + "and O.idusuario = U.id "
            + "and U.pessoa = PU.id "
            + "and O.tipo = 'Venda'";

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

    private void btPesquisarPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btPesquisarPActionPerformed
        
        CriterioFiltro criterio = new CriterioFiltro();

        criterio.setCampo((CampoFiltro) cbxCamposP.getSelectedItem());
        criterio.setCriterio(txtCriterioP.getText().trim());

        String sql = "select * from produtos, categorias where produtos.categoria = categorias.id and produtos.estatus = 'Ativado'";
        
        if("int".equals(criterio.getCampo().getTipoColuna())){
            try{
                Integer.parseInt(criterio.getCriterio());
                filtrarTabelaProdutoA(new ProdutoBLL().filtrar(sql,criterio));
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
        }else{
            if("string".equals(criterio.getCampo().getTipoColuna())){
                filtrarTabelaProdutoA(new ProdutoBLL().filtrar(sql,criterio));
            }else{
                if("double".equals(criterio.getCampo().getTipoColuna())){
                    try{
                        criterio.setCriterio(String.valueOf(Double.parseDouble(criterio.getCriterio().replace(",", "."))));
                        filtrarTabelaProdutoA(new ProdutoBLL().filtrar(sql,criterio));
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

    private void tbClientesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbClientesMousePressed
        
        controlarBotoesP();
        
    }//GEN-LAST:event_tbClientesMousePressed

    private void btBusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btBusActionPerformed
        
        if(modelTpp.getSelectedItem() != null){
            if(((String)modelTpp.getSelectedItem()).equals("Físicas")){
                tbClientes.setModel(modelPf);
                String sql = "SELECT * FROM pessoafisicas, pessoas WHERE pessoafisicas.pessoa = pessoas.id AND pessoafisicas.pessoa > 0;";
                List<PessoaFisicaEntity> list = new PessoaFisicaBLL().sqlDeConsultaPersonalizada(sql);
                if(list != null && !list.isEmpty()){
                    modelPf.Recarregar(list);
                    preencherFiltroCamposF();
                    sp = situacaoPessoa.escolher;
                    controlarBotoesP();
                }else{
                    JOptionPane.showMessageDialog(null, "Todas as pessoas físicas já são usários.\n"
                            + "Ou não há pessoas físicas cadastradas no sistema.", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
            }else{
                tbClientes.setModel(modelPj);
                String sql = "SELECT * FROM pessoajuridicas, pessoas WHERE pessoas.id = pessoajuridicas.pessoa AND pessoajuridicas.pessoa > 0;";
                List<PessoaJuridicaEntity> list = new PessoaJuridicaBLL().sqlDeConsultaPersonalizada(sql);
                if(list != null && !list.isEmpty()){
                    modelPj.Recarregar(list);
                    preencherFiltroCamposJ();
                    sp = situacaoPessoa.escolher;
                    controlarBotoesP();
                }else{
                    JOptionPane.showMessageDialog(null, "Todas as pessoas jurídicas já são usários.\n"
                            + "Ou não há pessoas jurídicas cadastradas no sistema.", "AVISO", JOptionPane.WARNING_MESSAGE);
                }
            }
        }else{
            JOptionPane.showMessageDialog(null, "Selecione um tipo de pesssoa.", "ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btBusActionPerformed

    private void btCanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCanActionPerformed
        
        modelPf.Recarregar(null);
        modelPj.Recarregar(null);
        sp = situacaoPessoa.filtrar;
        controlarBotoesP();
        
    }//GEN-LAST:event_btCanActionPerformed

    private void tbVendasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbVendasKeyReleased
        
        if(tbVendas.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbVendasKeyReleased

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

    private void tbClientesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbClientesKeyReleased
        
        controlarBotoesP();
        
    }//GEN-LAST:event_tbClientesKeyReleased

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

    private String getDate() { 
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new java.util.Date();
        return dateFormat.format(date); 
    }
    
    public final void controleBotoesProduto(){
        btDelP.setEnabled(situacaoPS == situacaoProdutoS.produtoS);
        btAddP.setEnabled(situacaoPS == situacaoProdutoS.produtoAS);
        btAltQTD.setEnabled(situacaoPS == situacaoProdutoS.produtoS);
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
            modeloTabelaProdutoCarrinoA.Recarregar(new ProdutoBLL().pesquisaPersonalizada("estatus = 'Ativado'"));
            tbProdutosA.setModel(modeloTabelaProdutoCarrinoA);
            produtosATB = new ArrayList<>();
            produtosATB.addAll(new ProdutoBLL().listarTodos());
            produtosTB = new ArrayList<>();
        }else{
            modeloTabelaProdutoCarrino.Recarregar(produtos);
            tbProdutos.setModel(modeloTabelaProdutoCarrino);
            
            List<ProdutoEntity> p = new ProdutoBLL().pesquisaPersonalizada("estatus = 'Ativado'");
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
        mostraPDVenda();
        mostraPDproduto(null);
        
    }
    
    public void mostraPDVenda(){
        
        if(modeloTabelaProdutoCarrino.getRowCount() > 0){
            
            pnDadosVenda.setVisible(true);
            lbQTDC.setVisible(true);
            lbQTDCI.setVisible(true);
            lbTitDC.setVisible(true);
            lbVTDC.setVisible(true);
            lbVTDCI.setVisible(true);
            
            pnDadosVenda.setEnabled(true);
            lbQTDC.setEnabled(true);
            lbQTDCI.setEnabled(true);
            lbTitDC.setEnabled(true);
            lbVTDC.setEnabled(true);
            lbVTDCI.setEnabled(true);
            
            lbQTDCI.setText(String.valueOf(modeloTabelaProdutoCarrino.getRowCount()));
            
            double vlt = 0;
            for(int x = 0 ; x<modeloTabelaProdutoCarrino.getRowCount() ; x++){
                OperacaoProdutosEntity op = modeloTabelaProdutoCarrino.getObjeto(x);
                if(op.getValorOferta() != 0){
                    
                    double nv = op.getProduto().getValor() - op.getProduto().getValor()*op.getValorOferta()/100;
                    double vlr = nv*op.getQtd();
                    vlt += vlr;
                    
                }else{
                    
                    double vlr = op.getProduto().getValor()*op.getQtd();
                    vlt += vlr;
                    
                }
            }
            
            lbVTDCI.setText("R$ "+vlt);
            
        }else{
            
            pnDadosVenda.setVisible(false);
            lbQTDC.setVisible(false);
            lbQTDCI.setVisible(false);
            lbTitDC.setVisible(false);
            lbVTDC.setVisible(false);
            lbVTDCI.setVisible(false);
            
            pnDadosVenda.setEnabled(false);
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
    
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    private void controlarBotoesP(){
        
        cbxTp.setEnabled(sp == situacaoPessoa.filtrar);
        btBus.setEnabled(sp == situacaoPessoa.filtrar);
        
        if(tbClientes.getSelectedRow() != -1)
            btSel.setEnabled(sp == situacaoPessoa.escolher);
        else
            btSel.setEnabled(false);
        
        btDes.setEnabled(sp == situacaoPessoa.escolhido);
        btCan.setEnabled(sp == situacaoPessoa.escolher);
        
        cbxCam.setEnabled(sp == situacaoPessoa.escolher);
        txtCri.setEnabled(sp == situacaoPessoa.escolher);
        btPes.setEnabled(sp == situacaoPessoa.escolher);
        btLis.setEnabled(sp == situacaoPessoa.escolher);
        
        tbClientes.setEnabled(sp == situacaoPessoa.escolher);
    }
    
    private situacaoPessoa sp;
    
    private enum situacaoPessoa {
        filtrar, escolher, escolhido, trancado
    }
    
    private void preencherFiltroCamposJ() {

        cbxCam.removeAllItems();

        for (int i = 0; i < modelPj.getColumnCount(); i++) {
            cbxCam.addItem(new CampoFiltro(
                modelPj.getColumnName(i), modelPj.getnomeTabela(i),
                modelPj.getNomeColunaTabela(i),
                modelPj.getTipoColunaTabela(i))
            );
        }

    }
    
    private void preencherFiltroCamposF() {

        cbxCam.removeAllItems();

        for (int i = 0; i < modelPf.getColumnCount(); i++) {
            cbxCam.addItem(new CampoFiltro(
                modelPf.getColumnName(i), modelPf.getnomeTabela(i),
                modelPf.getNomeColunaTabela(i),
                modelPf.getTipoColunaTabela(i))
            );
        }

    }
    
    private PessoaFisicaTableModel modelPf = new PessoaFisicaTableModel();
    private PessoaJuridicaTableModel modelPj = new PessoaJuridicaTableModel();
    
    private DefaultComboBoxModel<String> modelTpp = new DefaultComboBoxModel<>();
    
    public final void carregarCbxTpP(){
        cbxTp.setModel(modelTpp);
        modelTpp.addElement("Físicas");
        modelTpp.addElement("Jurídicas");
        cbxTp.setSelectedIndex(-1);
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddP;
    private javax.swing.JButton btAltQTD;
    private javax.swing.JButton btBus;
    private javax.swing.JButton btCan;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btDelP;
    private javax.swing.JButton btDes;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btFiltrarP;
    private javax.swing.JButton btInfoP;
    private javax.swing.JButton btLis;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btPes;
    private javax.swing.JButton btPesquisarP;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSel;
    private javax.swing.JComboBox cbxCam;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxCamposP;
    private javax.swing.JComboBox cbxTp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
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
    private javax.swing.JPanel pnDadosVenda;
    private javax.swing.JPanel pnFiltros;
    private utility.PanelVisualizaImagem pnIMG;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JScrollPane spTbf;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbClientes;
    private javax.swing.JTable tbProdutos;
    private javax.swing.JTable tbProdutosA;
    private javax.swing.JTable tbVendas;
    private javax.swing.JTextField txtCri;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtCriterioP;
    // End of variables declaration//GEN-END:variables

}
