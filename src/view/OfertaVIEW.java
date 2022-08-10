package view;

import bll.OfertaBLL;
import bll.ProdutoBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.OfertaEntity;
import entity.ProdutoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.OfertaTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import utility.Login;


public class OfertaVIEW extends javax.swing.JFrame implements IVIEW {
    
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

    private void insereIcones(){
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    }
    
    public OfertaVIEW() {
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
        insereIcones();
        controlaAcessos();
    }
    
    private void preencherFiltroCampos() {

        cbxCampos.removeAllItems();

        for (int i = 0; i < modeloTabelaOferta.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaOferta.getColumnName(i), modeloTabelaOferta.getnomeTabela(i),
                modeloTabelaOferta.getNomeColunaTabela(i),
                modeloTabelaOferta.getTipoColunaTabela(i))
            );
        }

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
    
    private void criaMascaras(){
        
    }
    
    private void preencherCampos() {
        objetoOferta = modeloTabelaOferta.getObjeto(tbOfertas.getSelectedRow()).getClone();
        txtValor.setText(String.valueOf(objetoOferta.getValor()));
        txtDtinicio.setDate(objetoOferta.getDtinicio());
        txtDtfim.setDate(objetoOferta.getDtfim());
        cbxModel.setSelectedItem(objetoOferta.getProduto());
        if(objetoOferta.getTipo().equals("Vendas online")){
            rbW.setSelected(true);
        }else{
            if(objetoOferta.getTipo().equals("Vendas físicas")){
                rbD.setSelected(true);
            }else{
                rbWD.setSelected(true);
            }
        }
    }
    
    private void carregarComboBox() {
        cbxProdutos.setModel(cbxModel);
        ProdutoBLL controleProduto = new ProdutoBLL();
        List<ProdutoEntity> estados = controleProduto.listarTodos();
        for(ProdutoEntity estado : estados){
            cbxModel.addElement(estado);
        }
        cbxProdutos.setSelectedIndex(-1);
    }
    
    private void limparCampos() {
        txtValor.setText("");
        txtDtfim.setDate(null);
        txtDtinicio.setDate(null);
        cbxProdutos.setSelectedIndex(-1);
        buttonGroup1.clearSelection();
        tbOfertas.clearSelection();
    }
    
    private void controlarCampos(boolean condicao) {
        txtValor.setEnabled(condicao);
        txtDtfim.setEnabled(condicao);
        txtDtinicio.setEnabled(condicao);
        cbxProdutos.setEnabled(condicao);
        rbD.setEnabled(condicao);
        rbW.setEnabled(condicao);
        rbWD.setEnabled(condicao);
        tbOfertas.setEnabled(!condicao);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private void carregarTabela() {
        modeloTabelaOferta.Recarregar(controleOferta.listarTodos());
        tbOfertas.setModel(modeloTabelaOferta);
    }

    private void carregarTabela(List<OfertaEntity> listarTodos) {
        modeloTabelaOferta.Recarregar(listarTodos);
        tbOfertas.setModel(modeloTabelaOferta);
    }
    
    private situacaoCadastro situacao;
    private OfertaEntity objetoOferta;
    private final OfertaBLL controleOferta = new OfertaBLL();
    private final OfertaTableModel modeloTabelaOferta = new OfertaTableModel();
    private final DefaultComboBoxModel<ProdutoEntity> cbxModel = new DefaultComboBoxModel<>();
    
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
        btNovo = new javax.swing.JButton();
        btSalvar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btExcluir = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        tbAbas = new javax.swing.JTabbedPane();
        pnCampos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        cbxProdutos = new javax.swing.JComboBox();
        txtDtinicio = new org.jdesktop.swingx.JXDatePicker();
        txtDtfim = new org.jdesktop.swingx.JXDatePicker();
        txtValor = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        rbW = new javax.swing.JRadioButton();
        rbD = new javax.swing.JRadioButton();
        rbWD = new javax.swing.JRadioButton();
        spTabela = new javax.swing.JScrollPane();
        tbOfertas = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/note.png"))); // NOI18N
        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar nova oferta<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoOferta"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/note_add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados da oferta<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarOferta"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/note_edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados da oferta<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarOferta"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/note_delete.png"))); // NOI18N
        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír oferta<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirOferta"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarOferta"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de ofertas<br />Atalho: alt + f</html>");
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

        jLabel1.setText("Produto:");

        jLabel2.setText("Data de início:");

        jLabel3.setText("Data de fim:");

        jLabel4.setText("Valor de desconto: (%)");

        jLabel5.setText("Alvo:");

        buttonGroup1.add(rbW);
        rbW.setText("Vendas online");

        buttonGroup1.add(rbD);
        rbD.setText("Vendas físicas");

        buttonGroup1.add(rbWD);
        rbWD.setText("Vendas online e físicas");

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel4))
                        .add(14, 14, 14)
                        .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(cbxProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 246, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 246, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(18, 18, 18))
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(jLabel5)
                        .add(8, 8, 8)
                        .add(rbW)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(rbD)
                        .add(18, 18, 18)
                        .add(rbWD)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)))
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jLabel2)
                    .add(jLabel3))
                .add(14, 14, 14)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtDtfim, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .add(txtDtinicio, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel1)
                    .add(cbxProdutos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel2)
                    .add(txtDtinicio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel3)
                    .add(txtDtfim, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jLabel4)
                    .add(txtValor, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(jLabel5)
                    .add(rbW)
                    .add(rbD)
                    .add(rbWD))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        tbAbas.addTab("Campos", pnCampos);

        tbOfertas.setModel(new javax.swing.table.DefaultTableModel(
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
        tbOfertas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbOfertas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbOfertasMousePressed(evt);
            }
        });
        tbOfertas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbOfertasKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbOfertas);

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
        btListar.setToolTipText("<html>Mostrar todas as ofertas na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel6.setText("Critério:");

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
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 289, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                    .add(jLabel6))
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
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 134, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 197, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        situacao = situacaoCadastro.scEditando;
        controlarBotoes();
        controlarCampos(true);
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
            
            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
            ul.setDescricao("Excluiu a oferta de ID = "+objetoOferta.getId());
            ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
            ul.setHorario(DataUtility.getDateTime("EN"));
            new UsuarioAcaoBLL().salvar(ul);

            controleOferta.excluir(objetoOferta);
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
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbOfertas, new File(caminho));
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
                    carregarTabela(new OfertaBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new OfertaBLL().filtrar(criterio));
                }else{
                    if("date".equals(criterio.getCampo().getTipoColuna())){
                        try{
                            SimpleDateFormat formatoData = new SimpleDateFormat("dd/MM/yyyy");
                            java.util.Date data = formatoData.parse(criterio.getCriterio());
                            java.sql.Date dataenvia = new java.sql.Date(data.getTime());
                            criterio.setCriterio(String.valueOf(dataenvia));
                            carregarTabela(new OfertaBLL().filtrar(criterio));
                        }
                        catch(ParseException e){
                            JOptionPane.showMessageDialog(null,"Data inválida.","ERRO",JOptionPane.ERROR_MESSAGE);
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
        
        carregarTabela(new OfertaBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void tbOfertasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbOfertasMousePressed
        
        if(tbOfertas.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbOfertasMousePressed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
        txtValor.setText(txtValor.getText().trim());
        
        if(!"".equals(txtValor.getText()) && txtDtfim.getDate() != null && txtDtinicio.getDate() != null && cbxModel.getSelectedItem() != null && (rbW.isSelected() || rbD.isSelected() || rbWD.isSelected())){
            
            try{
                int valor = Integer.parseInt(txtValor.getText());
                if(valor > 0 && valor < 100){
                    if(txtDtinicio.getDate().before(txtDtfim.getDate())){
                        if (situacao == situacaoCadastro.scInserindo){
                            objetoOferta = new OfertaEntity();
                            objetoOferta.setId(0);
                        }
                        
                        if(rbW.isSelected()){
                            objetoOferta.setTipo("Vendas online");
                        }else{
                            if(rbD.isSelected()){
                                objetoOferta.setTipo("Vendas físicas");
                            }else{
                                objetoOferta.setTipo("Vendas online e físicas");
                            }
                        }
                        
                        objetoOferta.setValor(valor);
                        objetoOferta.setProduto((ProdutoEntity) cbxModel.getSelectedItem());
                        objetoOferta.setDtinicio(new java.sql.Date(txtDtinicio.getDate().getTime()));
                        objetoOferta.setDtfim(new java.sql.Date(txtDtfim.getDate().getTime()));
                        
                        List<OfertaEntity> r1 = null;
                        List<OfertaEntity> r2 = null;
                        List<OfertaEntity> r3 = null;
                        List<OfertaEntity> r4 = null;
                        
                        if(situacao == situacaoCadastro.scInserindo){
                            String primeiraSql = "produto = "+objetoOferta.getProduto().getId()+" and datafim >= '"+objetoOferta.getDtinicio()+"' and datainicio <= '"+objetoOferta.getDtinicio()+"'";
                            String segundaSql = "produto = "+objetoOferta.getProduto().getId()+" and datafim >= '"+objetoOferta.getDtfim()+"' and datainicio <= '"+objetoOferta.getDtfim()+"'";
                            String terceiraSql = "produto = "+objetoOferta.getProduto().getId()+" and datainicio >= '"+objetoOferta.getDtinicio()+"' and datafim <= '"+objetoOferta.getDtfim()+"'";
                            String quartaSql = "produto = "+objetoOferta.getProduto().getId()+" and datainicio <= '"+objetoOferta.getDtinicio()+"' and datafim >= '"+objetoOferta.getDtfim()+"'";
                            
                            r1 = controleOferta.pesquisaPersonalizada(primeiraSql);
                            r2 = controleOferta.pesquisaPersonalizada(segundaSql);
                            r3 = controleOferta.pesquisaPersonalizada(terceiraSql);
                            r4 = controleOferta.pesquisaPersonalizada(quartaSql);
                        }else{
                            String primeiraSql = "id <> "+objetoOferta.getId()+" and produto = "+objetoOferta.getProduto().getId()+" and datafim >= '"+objetoOferta.getDtinicio()+"' and datainicio <= '"+objetoOferta.getDtinicio()+"'";
                            String segundaSql = "id <> "+objetoOferta.getId()+" and produto = "+objetoOferta.getProduto().getId()+" and datafim >= '"+objetoOferta.getDtfim()+"' and datainicio <= '"+objetoOferta.getDtfim()+"'";
                            String terceiraSql = "id <> "+objetoOferta.getId()+" and produto = "+objetoOferta.getProduto().getId()+" and datainicio >= '"+objetoOferta.getDtinicio()+"' and datafim <= '"+objetoOferta.getDtfim()+"'";
                            String quartaSql = "id <> "+objetoOferta.getId()+" and produto = "+objetoOferta.getProduto().getId()+" and datainicio <= '"+objetoOferta.getDtinicio()+"' and datafim >= '"+objetoOferta.getDtfim()+"'";
                            
                            r1 = controleOferta.pesquisaPersonalizada(primeiraSql);
                            r2 = controleOferta.pesquisaPersonalizada(segundaSql);
                            r3 = controleOferta.pesquisaPersonalizada(terceiraSql);
                            r4 = controleOferta.pesquisaPersonalizada(quartaSql);
                        }
                        
                        if(r1 == null || r1.isEmpty()){
                            
                            if(r2 == null || r2.isEmpty()){
                                
                                if(r3 == null || r3.isEmpty()){
                                    
                                    if(r4 == null || r4.isEmpty()){
                                    
                                        controleOferta.salvar(objetoOferta);
                                        if(situacao == situacaoCadastro.scInserindo){
                                            
                                            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                                            ul.setDescricao("Cadastrou a oferta de ID = "+objetoOferta.getId());
                                            ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                                            ul.setHorario(DataUtility.getDateTime("EN"));
                                            new UsuarioAcaoBLL().salvar(ul);
                                            
                                            JOptionPane.showMessageDialog(null, "O registro foi cadastrado com sucesso!", "OK", JOptionPane.PLAIN_MESSAGE);
                                        }else{
                                            
                                            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                                            ul.setDescricao("Alterou a oferta de ID = "+objetoOferta.getId());
                                            ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                                            ul.setHorario(DataUtility.getDateTime("EN"));
                                            new UsuarioAcaoBLL().salvar(ul);
                                            
                                            JOptionPane.showMessageDialog(null, "O registro foi atualizado com sucesso!", "OK", JOptionPane.PLAIN_MESSAGE);
                                        }

                                        carregarTabela();
                                        limparCampos();
                                        situacao = situacaoCadastro.scEsperando;
                                        controlarBotoes();
                                        controlarCampos(false);
                                        
                                    }else{
                                        JOptionPane.showMessageDialog(null,"3Já existe uma oferta cadastrada para esse produto, nesse intervalo de tempo.","ERRO",JOptionPane.ERROR_MESSAGE);
                                    }
                                    
                                }else{
                                    JOptionPane.showMessageDialog(null,"3Já existe uma oferta cadastrada para esse produto, nesse intervalo de tempo.","ERRO",JOptionPane.ERROR_MESSAGE);
                                }
                                
                            }else{
                                JOptionPane.showMessageDialog(null,"2Já existe uma oferta cadastrada para esse produto, nesse intervalo de tempo.","ERRO",JOptionPane.ERROR_MESSAGE);
                            }
                            
                        }else{
                            JOptionPane.showMessageDialog(null,"1Já existe uma oferta cadastrada para esse produto, nesse intervalo de tempo.","ERRO",JOptionPane.ERROR_MESSAGE);
                        }
                        
                    }else{
                        JOptionPane.showMessageDialog(null,"Data de inínio da oferta, maior que a data de fim.","ERRO",JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Valor de desconto inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null,"Valor de desconto inválido.","ERRO",JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Preencha todos os campos.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void tbOfertasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbOfertasKeyReleased
        
        if(tbOfertas.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbOfertasKeyReleased

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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxProdutos;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JRadioButton rbD;
    private javax.swing.JRadioButton rbW;
    private javax.swing.JRadioButton rbWD;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbOfertas;
    private javax.swing.JTextField txtCriterio;
    private org.jdesktop.swingx.JXDatePicker txtDtfim;
    private org.jdesktop.swingx.JXDatePicker txtDtinicio;
    private javax.swing.JTextField txtValor;
    // End of variables declaration//GEN-END:variables

}
