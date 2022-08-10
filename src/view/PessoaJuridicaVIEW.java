package view;

import bll.BairroBLL;
import bll.CidadeBLL;
import bll.EstadoBLL;
import bll.OperacaoBLL;
import bll.PessoaJuridicaBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.EnderecoEntity;
import entity.BairroEntity;
import entity.CidadeEntity;
import entity.EstadoEntity;
import entity.OperacaoEntity;
import entity.PessoaJuridicaEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.EnderecoTableModel;
import model.PessoaJuridicaTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import static utility.GeradorDeMascaras.createMaskByIgor;
import utility.Login;


public class PessoaJuridicaVIEW extends javax.swing.JFrame implements IVIEW {
    
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

        for (int i = 0; i < modeloTabelaPessoaJuridica.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaPessoaJuridica.getColumnName(i), modeloTabelaPessoaJuridica.getnomeTabela(i),
                modeloTabelaPessoaJuridica.getNomeColunaTabela(i),
                modeloTabelaPessoaJuridica.getTipoColunaTabela(i))
            );
        }

    }
    
    private void insereIcones(){
        btAddtel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/smartphone_add.png")));
        btDeltel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/smartphone_delete.png")));
        btLimpartel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_paint.png")));
        
        btSalvarend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/add.png")));
        btExcluirend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/delete.png")));
        btCancelarend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    } 
    
    private void carregarTabelaEndereco() {
        if(objetoPessoaJuridica != null && objetoPessoaJuridica.getEnderecos() != null){
            modeloTabelaEndereco.Recarregar(objetoPessoaJuridica.getEnderecos());
        }
        tbEnderecos.setModel(modeloTabelaEndereco);
    }
    
    private void carregarTabelaEndereco(List<EnderecoEntity> e) {
        if(e != null)
            modeloTabelaEndereco.Recarregar(e);
        else
            modeloTabelaEndereco.limpar();
        tbEnderecos.setModel(modeloTabelaEndereco);
    }
    
    private void carregarTabela() {
        UsuarioEntity ul = new UsuarioBLL().pesquisar(Login.getLogin());
        modeloTabelaPessoaJuridica.Recarregar(controlePessoaJuridica.pesquisaPersonalizada("pessoa > 0 and pessoa <>"+ul.getPessoa().getId()));
        tbPessoaJuridicas.setModel(modeloTabelaPessoaJuridica);
    }

    private void carregarTabela(List<PessoaJuridicaEntity> filtrar) {
        UsuarioEntity ul = new UsuarioBLL().pesquisar(Login.getLogin());
        for(int x = (filtrar.size()-1) ; x >= 0 ; x--){
            if(filtrar.get(x).getId() == ul.getPessoa().getId()){
                filtrar.remove(x);
            }
        }
        modeloTabelaPessoaJuridica.Recarregar(filtrar);
        tbPessoaJuridicas.setModel(modeloTabelaPessoaJuridica);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private enum situacaoCadastroEndereco {
        Esperando, Visualizando, Inserindo, Trancado
    }
    
    private void limparCampos() {
        objetoPessoaJuridica = null;
        txtNome.setText("");
        txtCnpj.setText("");
        tbPessoaJuridicas.clearSelection();
        tbEnderecos.clearSelection();
        liTel.clearSelection();
        txtTel.setText("");
        limparLista();
    }
    
    private void limparCamposEnderecos() {
        objetoEndereco = null;
        txtNomerua.setText("");
        txtNumcasa.setText("");
        cbxBairros.setSelectedIndex(-1);
        cbxCidades.setSelectedIndex(-1);
        cbxEstados.setSelectedIndex(-1);
        carregarComboBoxBairros(null);
        carregarComboBoxCidades(null);
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(condicao);
        txtCnpj.setEnabled(condicao);
        tbEnderecos.setEnabled(condicao);
        btAddtel.setEnabled(condicao);
        btDeltel.setEnabled(condicao);
        btLimpartel.setEnabled(condicao);
        liTel.setEnabled(condicao);
        txtTel.setEnabled(condicao);
        tbPessoaJuridicas.setEnabled(!condicao);
    }
    
    public final void controlarBotoesEnderecos(){
        if(situacao == situacaoCadastro.scEditando || situacao == situacaoCadastro.scInserindo){
            btNovoend.setEnabled(situacaoEndereco == situacaoCadastroEndereco.Esperando || situacaoEndereco == situacaoCadastroEndereco.Visualizando);
            btSalvarend.setEnabled(situacaoEndereco == situacaoCadastroEndereco.Inserindo);
            btCancelarend.setEnabled(situacaoEndereco == situacaoCadastroEndereco.Inserindo);
            btExcluirend.setEnabled(situacaoEndereco == situacaoCadastroEndereco.Visualizando);
        }else{
            btNovoend.setEnabled(false);
            btSalvarend.setEnabled(false);
            btCancelarend.setEnabled(false);
            btExcluirend.setEnabled(false);
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
    
    private void preencherCamposEndereco(){
        objetoEndereco = modeloTabelaEndereco.getObjeto(tbEnderecos.getSelectedRow()).getClone();
        cbxModel3.setSelectedItem(objetoEndereco.getBairro().getCidade().getEstado());
        long codE = objetoEndereco.getBairro().getCidade().getEstado().getId();
        carregarComboBoxCidades(new CidadeBLL().pesquisaPersonalizada("estado = "+codE));
        cbxModel2.setSelectedItem(objetoEndereco.getBairro().getCidade());
        long codB = objetoEndereco.getBairro().getCidade().getId();
        carregarComboBoxBairros(new BairroBLL().pesquisaPersonalizada("cidade = "+codB));
        cbxModel.setSelectedItem(objetoEndereco.getBairro());
        txtNomerua.setText(objetoEndereco.getNomeRua());
        txtNumcasa.setText(String.valueOf(objetoEndereco.getNumCasa()));
    }
    
    private void preencherCampos() {
        objetoPessoaJuridica = modeloTabelaPessoaJuridica.getObjeto(tbPessoaJuridicas.getSelectedRow()).getClone();
        txtNome.setText(objetoPessoaJuridica.getNome());
        txtCnpj.setText(objetoPessoaJuridica.getCnpj());
        carregarTabelaEndereco();
        recarregarLista(objetoPessoaJuridica.getTelefones());
    }
    
    private situacaoCadastro situacao;
    private situacaoCadastroEndereco situacaoEndereco;
    private PessoaJuridicaEntity objetoPessoaJuridica;
    private EnderecoEntity objetoEndereco;
    private final PessoaJuridicaBLL controlePessoaJuridica = new PessoaJuridicaBLL();
    private final PessoaJuridicaTableModel modeloTabelaPessoaJuridica = new PessoaJuridicaTableModel();
    private final EnderecoTableModel modeloTabelaEndereco = new EnderecoTableModel();
    private final DefaultComboBoxModel<BairroEntity> cbxModel = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<CidadeEntity> cbxModel2 = new DefaultComboBoxModel<>();
    private final DefaultComboBoxModel<EstadoEntity> cbxModel3 = new DefaultComboBoxModel<>();
    private final DefaultListModel<String> liModel = new DefaultListModel<>();
    
    private void carregarLista(){
        liTel.setModel(liModel);
    }
    
    private void recarregarLista(List<String> tels){
        liModel.clear();
        if(tels != null && !tels.isEmpty()){
            for(int x = 0; x<tels.size() ; x++){
                liModel.addElement(tels.get(x));
            }
        }
        liTel.setModel(liModel);
    }
    
    private void limparLista(){
        liModel.clear();
        liTel.setModel(liModel);
    }
    
    private void carregarComboBoxEstado() {
        cbxModel3.removeAllElements();
        cbxEstados.setModel(cbxModel3);
        EstadoBLL controleEstado = new EstadoBLL();
        List<EstadoEntity> estados = controleEstado.listarTodos();
        for(EstadoEntity estado : estados){
            cbxModel3.addElement(estado);
        }
        cbxEstados.setSelectedIndex(-1);
    }
    
    private void criaMascaras(){
        createMaskByIgor("##.###.###/####-##",'_').install(txtCnpj);
        createMaskByIgor("(##) ####-####",'_').install(txtTel);
    }
    
    public PessoaJuridicaVIEW() {
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        situacaoEndereco = situacaoCadastroEndereco.Trancado;
        carregarTabela();
        controlarBotoes();
        controlarBotoesEnderecos();
        controlarCampos(false);
        carregarComboBoxEstado();
        insereIcones();
        preencherFiltroCampos();
        desabilitaBtFecharEAtalhos();
        carregarTabelaEndereco();
        carregarLista();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
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
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        btNovoend = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        btExcluirend = new javax.swing.JButton();
        btCancelarend = new javax.swing.JButton();
        txtNumcasa = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        btSalvarend = new javax.swing.JButton();
        cbxBairros = new javax.swing.JComboBox();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbEnderecos = new javax.swing.JTable();
        txtNomerua = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        cbxEstados = new javax.swing.JComboBox();
        jLabel13 = new javax.swing.JLabel();
        cbxCidades = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        btAddtel = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        liTel = new javax.swing.JList();
        btDeltel = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        txtTel = new javax.swing.JFormattedTextField();
        btLimpartel = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtCnpj = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        spTabela = new javax.swing.JScrollPane();
        tbPessoaJuridicas = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de pessoas jurídicas");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user.png"))); // NOI18N
        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar nova pessoa<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoPessoaJ"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados da pessoa<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarPessoaJ"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados da pessoa<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarPessoaJ"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_delete.png"))); // NOI18N
        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír pessoa<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirPessoaJ"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarPessoaJ"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de pessoas<br />Atalho: alt + f</html>");
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

        jPanel2.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel6.setText("Nome rua:");

        btNovoend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/map.png"))); // NOI18N
        btNovoend.setText("Novo");
        btNovoend.setToolTipText("Registrar novo endereço");
        btNovoend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoendActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel8.setText("Endereços:");

        btExcluirend.setText("Excluir");
        btExcluirend.setToolTipText("Excluír endereço");
        btExcluirend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirendActionPerformed(evt);
            }
        });

        btCancelarend.setText("Cancelar");
        btCancelarend.setToolTipText("Cancelar operação");
        btCancelarend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarendActionPerformed(evt);
            }
        });

        jLabel5.setText("Bairro:");

        jLabel7.setText("Número da casa:");

        btSalvarend.setText("Salvar");
        btSalvarend.setToolTipText("Salvar dados do novo endereço");
        btSalvarend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarendActionPerformed(evt);
            }
        });

        tbEnderecos.setModel(new javax.swing.table.DefaultTableModel(
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
        tbEnderecos.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbEnderecos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbEnderecosMousePressed(evt);
            }
        });
        tbEnderecos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbEnderecosKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(tbEnderecos);

        jLabel12.setText("Estados:");

        cbxEstados.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxEstadosItemStateChanged(evt);
            }
        });

        jLabel13.setText("Cidades:");

        cbxCidades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCidadesItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .add(txtNumcasa)
                    .add(txtNomerua)
                    .add(cbxBairros, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2Layout.createSequentialGroup()
                        .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel8)
                            .add(jPanel2Layout.createSequentialGroup()
                                .add(btNovoend)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btSalvarend)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btExcluirend)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btCancelarend))
                            .add(jLabel6)
                            .add(jLabel7)
                            .add(jLabel5)
                            .add(jLabel12)
                            .add(jLabel13))
                        .add(0, 78, Short.MAX_VALUE))
                    .add(cbxEstados, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cbxCidades, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel8)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel12)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxEstados, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel13)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                .add(cbxCidades, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel5)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxBairros, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel6)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtNomerua, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(4, 4, 4)
                .add(jLabel7)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtNumcasa, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btNovoend)
                    .add(btSalvarend)
                    .add(btExcluirend)
                    .add(btCancelarend))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 84, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel3.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btAddtel.setText("Adcionar");
        btAddtel.setToolTipText("Adicionar telefone a lista");
        btAddtel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btAddtelActionPerformed(evt);
            }
        });

        jLabel10.setText("Número:");

        liTel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                liTelMousePressed(evt);
            }
        });
        jScrollPane2.setViewportView(liTel);

        btDeltel.setText("Remover");
        btDeltel.setToolTipText("Remover telefone da lista");
        btDeltel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDeltelActionPerformed(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel9.setText("Telefones:");

        btLimpartel.setText("Limpar");
        btLimpartel.setToolTipText("Limpar campo do telefone");
        btLimpartel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btLimpartelActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout jPanel3Layout = new org.jdesktop.layout.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 299, Short.MAX_VALUE)
                    .add(txtTel)
                    .add(jPanel3Layout.createSequentialGroup()
                        .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(btDeltel)
                            .add(jLabel9)
                            .add(jLabel10)
                            .add(jPanel3Layout.createSequentialGroup()
                                .add(btAddtel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(btLimpartel)))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel9)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btDeltel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 142, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(jLabel10)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtTel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btAddtel)
                    .add(btLimpartel))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel4.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel3.setText("CNPJ:");

        jLabel1.setText("Nome:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("Dados de indetificação:");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/frigocenter-cadastro-cliente-pj.jpg"))); // NOI18N

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtNome)
                    .add(jLabel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 411, Short.MAX_VALUE)
                    .add(txtCnpj)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel11)
                            .add(jLabel3))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel11)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtNome, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCnpj, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(95, 95, 95)
                .add(jLabel14, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tbAbas.addTab("Campos", pnCampos);

        tbPessoaJuridicas.setModel(new javax.swing.table.DefaultTableModel(
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
        tbPessoaJuridicas.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbPessoaJuridicas.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbPessoaJuridicasMousePressed(evt);
            }
        });
        tbPessoaJuridicas.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPessoaJuridicasKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbPessoaJuridicas);

        pnFiltros.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btExcel.setMnemonic('x');
        btExcel.setText("Excel");
        btExcel.setToolTipText("<html>Exportar dados da tabela para planilha excel<br />Atalho: alt + x</html>");
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
        btListar.setToolTipText("<html>Mostrar todas as pessoas na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel15.setText("Critério:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 306, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel15)
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
                    .add(jLabel15))
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
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 111, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
        controlaBtRemTel();
        
        situacaoEndereco = situacaoCadastroEndereco.Esperando;
        controlarBotoesEnderecos();
        limparCamposEnderecos();
        carregarTabelaEndereco(null);
        
    }//GEN-LAST:event_btNovoActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        UsuarioEntity ul = new UsuarioBLL().pesquisar(Login.getLogin());
        List<UsuarioEntity> usuario = new UsuarioBLL().pesquisaPersonalizada("pessoa = "+objetoPessoaJuridica.getId());
        if("Controlador mestre".equalsIgnoreCase(ul.getGrupo().getNome()) || usuario == null || (!"Administradores".equalsIgnoreCase(usuario.get(0).getGrupo().getNome()) && !"Controlador mestre".equalsIgnoreCase(usuario.get(0).getGrupo().getNome()))){
            situacao = situacaoCadastro.scEditando;
            controlarBotoes();
            controlarCampos(true);
            controlaBtRemTel();
            situacaoEndereco = situacaoCadastroEndereco.Esperando;
            controlarBotoesEnderecos();
            escolheECB = EscolheECB.Trancado;
            controleComboboxEnderecos();
        }else{
            JOptionPane.showMessageDialog(null, "Você não têm permissão para editar esssa pessoa.","AVISO",JOptionPane.WARNING_MESSAGE);
        }
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirActionPerformed
        
        String sql = "select distinct usuarios.* from usuarios, pessoas where usuarios.pessoa = pessoas.id "
                + "and pessoas.id = "+objetoPessoaJuridica.getId()+";";
        List<UsuarioEntity> resultado = new UsuarioBLL().sqlDeConsultaPersonalizada(sql);
        if(resultado == null || resultado.isEmpty()){
            
            boolean perguntar = false;
            List<OperacaoEntity> resp = new OperacaoBLL().pesquisaPersonalizada("idpessoa = "+objetoPessoaJuridica.getId());
            if(resp != null && !resp.isEmpty()){
                perguntar = true;
            }
            
            int temp = JOptionPane.YES_OPTION;
            
            if(perguntar == false){
                temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
            }else{
                temp = JOptionPane.showConfirmDialog(null,"Essa pessoa está associada há uma ou mais vendas ou compras.\n"
                        + "Deseja mesmo exclui-la?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
            }
                
            if (temp == JOptionPane.YES_OPTION) {
                
                if(resp != null && !resp.isEmpty()){
                    for(int x = 0 ; x < resp.size() ; x++){
                        new OperacaoBLL().excluir(resp.get(x));
                    }
                }
                
                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
                ul.setDescricao("Excluiu a pessoa juridica de nome = "+objetoPessoaJuridica.getNome()+" e de ID = "+objetoPessoaJuridica.getId());
                ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                ul.setHorario(DataUtility.getDateTime("EN"));
                new UsuarioAcaoBLL().salvar(ul);

                controlePessoaJuridica.excluir(objetoPessoaJuridica);
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
                situacaoEndereco = situacaoCadastroEndereco.Trancado;
                controlarBotoesEnderecos();
                limparCamposEnderecos();
                carregarTabelaEndereco(null);
                escolheECB = EscolheECB.Trancado;
                controleComboboxEnderecos();

            }
            
        }else{
            JOptionPane.showMessageDialog(null,"Essa pesoa está associada a um usário.\nSeus dados não podem ser deletados","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        carregarTabela();
        carregarTabelaEndereco(null);
        situacaoEndereco = situacaoCadastroEndereco.Trancado;
        controlarBotoesEnderecos();
        limparCamposEnderecos();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbPessoaJuridicas, new File(caminho));
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
        carregarTabela();
        
        carregarTabelaEndereco(null);
        situacaoEndereco = situacaoCadastroEndereco.Trancado;
        controlarBotoesEnderecos();
        limparCamposEnderecos();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        
        txtCriterio.setText(txtCriterio.getText().trim());
        
        if(!txtCriterio.getText().equals("")){
            CriterioFiltro criterio = new CriterioFiltro();

            criterio.setCampo((CampoFiltro) cbxCampos.getSelectedItem());
            criterio.setCriterio(txtCriterio.getText());

            if("int".equals(criterio.getCampo().getTipoColuna())){
                try{
                    Integer.parseInt(criterio.getCriterio());
                    carregarTabela(new PessoaJuridicaBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new PessoaJuridicaBLL().filtrar(criterio));
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
                            carregarTabela(new PessoaJuridicaBLL().filtrar(criterio));
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
        
    }//GEN-LAST:event_btFiltrarActionPerformed

    private void btListarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btListarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        carregarTabela();
        carregarTabelaEndereco(null);
        situacaoEndereco = situacaoCadastroEndereco.Trancado;
        controlarBotoesEnderecos();
        limparCamposEnderecos();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        
        carregarTabela(new PessoaJuridicaBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void tbPessoaJuridicasMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPessoaJuridicasMousePressed
        
        if(tbPessoaJuridicas.isEnabled()){
            carregarTabelaEndereco(null);

            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);

            situacaoEndereco = situacaoCadastroEndereco.Trancado;
            limparCamposEnderecos();
            controlarBotoesEnderecos();
            escolheECB = EscolheECB.Trancado;
            controleComboboxEnderecos();
        }
        
    }//GEN-LAST:event_tbPessoaJuridicasMousePressed

    private void btNovoendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btNovoendActionPerformed
       
        limparCamposEnderecos();
        situacaoEndereco = situacaoCadastroEndereco.Inserindo;
        controlarBotoesEnderecos();
        escolheECB = EscolheECB.Estado;
        controleComboboxEnderecos();
        
    }//GEN-LAST:event_btNovoendActionPerformed

    private void btSalvarendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarendActionPerformed
        
        txtNomerua.setText(txtNomerua.getText().trim());
        txtNumcasa.setText(txtNumcasa.getText().trim());
        
        if(!txtNomerua.getText().equals("") && !txtNumcasa.getText().equals("") && cbxModel.getSelectedItem() != null){
           
            if(situacaoEndereco == situacaoCadastroEndereco.Inserindo){
                objetoEndereco = new EnderecoEntity();
            }
            
            objetoEndereco.setBairro((BairroEntity) cbxModel.getSelectedItem());
            objetoEndereco.setNomeRua(txtNomerua.getText());
            
            try{
                
                objetoEndereco.setNumCasa(Integer.parseInt(txtNumcasa.getText()));
                
                if(objetoEndereco.getNumCasa() >= 0){
                    boolean salvar = true;
                    if(objetoPessoaJuridica != null){
                        if(objetoPessoaJuridica.getEnderecos() != null && !objetoPessoaJuridica.getEnderecos().isEmpty()){
                            for(EnderecoEntity e : objetoPessoaJuridica.getEnderecos()){
                                if(e.getBairro().getId() == objetoEndereco.getBairro().getId() &&
                                        e.getNomeRua().equals(objetoEndereco.getNomeRua()) &&
                                        e.getNumCasa() == objetoEndereco.getNumCasa()){
                                    salvar = false;
                                }
                            }
                        }
                    }

                    if(salvar == true){
                        if(objetoPessoaJuridica == null){
                            objetoPessoaJuridica = new PessoaJuridicaEntity();
                        }

                        objetoPessoaJuridica.addEndereco(objetoEndereco);
                        JOptionPane.showMessageDialog(null,"O endereco foi adicionado com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);

                        carregarTabelaEndereco();
                        limparCamposEnderecos();
                        situacaoEndereco = situacaoCadastroEndereco.Esperando;
                        controlarBotoesEnderecos();
                        escolheECB = EscolheECB.Trancado;
                        controleComboboxEnderecos();
                        
                    }else{
                        JOptionPane.showMessageDialog(null, "Esse endereco ja existe.", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Informe um numero valido para a casa.", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
                
            }
            catch(NumberFormatException e){
                JOptionPane.showMessageDialog(null, "Informe um numero valido para a casa.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Preecnha todos os campos.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btSalvarendActionPerformed

    private void btSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btSalvarActionPerformed
        
        int temp = JOptionPane.YES_NO_OPTION;
        if(situacaoEndereco == situacaoCadastroEndereco.Inserindo){
            temp = JOptionPane.showConfirmDialog(null,"Você não terminou a operação atual, se continuar perderá as informações do endereco.\nProsseguir?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
        }
        
        if(temp == JOptionPane.YES_NO_OPTION){
            
            txtNome.setText(txtNome.getText().trim());
            txtCnpj.setText(txtCnpj.getText().trim());
            
            List<PessoaJuridicaEntity> resultado;
            
            if(situacao == situacaoCadastro.scInserindo){
                resultado = controlePessoaJuridica.pesquisaPersonalizada("cnpj = '"+txtCnpj.getText()+"'");
            }else{
                resultado = controlePessoaJuridica.pesquisaPersonalizada("cnpj = '"+txtCnpj.getText()+"' and pessoa <> "+objetoPessoaJuridica.getId());
            }
            
            if(resultado == null || resultado.isEmpty()){
                if(!txtNome.getText().equals("") && txtCnpj.getText().length() == 18){

                    if(objetoPessoaJuridica != null){
                        if(objetoPessoaJuridica.getEnderecos() != null && !objetoPessoaJuridica.getEnderecos().isEmpty()){

                            if(!liModel.isEmpty()){
                                if(objetoPessoaJuridica.getTelefones() != null && !objetoPessoaJuridica.getTelefones().isEmpty()){
                                    objetoPessoaJuridica.getTelefones().clear();
                                }
                                for(int x = 0 ; x<liModel.size() ; x++){
                                    objetoPessoaJuridica.addTel(liModel.getElementAt(x));
                                }
                                objetoPessoaJuridica.setNome(txtNome.getText());
                                objetoPessoaJuridica.setCnpj(txtCnpj.getText());

                                controlePessoaJuridica.salvar(objetoPessoaJuridica);
                                if (situacao == situacaoCadastro.scInserindo) {
                                    
                                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                                    ul.setDescricao("Cadastrou a pessoa jurídica de nome = "+objetoPessoaJuridica.getNome()+" e de ID = "+objetoPessoaJuridica.getId());
                                    ul.setUsuario(new UsuarioBLL().pesquisar(Login.getLogin()));
                                    ul.setHorario(DataUtility.getDateTime("EN"));
                                    new UsuarioAcaoBLL().salvar(ul);
                                    
                                    JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                                }else{
                                    
                                    UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                    ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                                    ul.setDescricao("Alterou a pessoa jurídica de ID = "+objetoPessoaJuridica.getId());
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
                                carregarTabelaEndereco(null);
                                limparCamposEnderecos();
                                situacaoEndereco = situacaoCadastroEndereco.Trancado;
                                controlarBotoesEnderecos();
                                escolheECB = EscolheECB.Trancado;
                                controleComboboxEnderecos();
                                
                            }else{
                                JOptionPane.showMessageDialog(null, "Adicione um telefone.", "ERRO", JOptionPane.ERROR_MESSAGE);
                            }

                        }else{
                            JOptionPane.showMessageDialog(null, "Adicione um endereco.", "ERRO", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Adicione um endereco.", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }

                }else{
                    JOptionPane.showMessageDialog(null, "Preecnha todos os campos.", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "CNPJ inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
            
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btExcluirendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirendActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o endereco?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
            objetoPessoaJuridica.delEndereco(objetoEndereco);
            carregarTabelaEndereco();
            limparCamposEnderecos();
            situacaoEndereco = situacaoCadastroEndereco.Esperando;
            controlarBotoesEnderecos();
            escolheECB = EscolheECB.Trancado;
            controleComboboxEnderecos();
        }
        
    }//GEN-LAST:event_btExcluirendActionPerformed

    private void btCancelarendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarendActionPerformed
        
        situacaoEndereco = situacaoCadastroEndereco.Esperando;
        controlarBotoesEnderecos();
        limparCamposEnderecos();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        
    }//GEN-LAST:event_btCancelarendActionPerformed

    private void tbEnderecosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEnderecosMousePressed
        
        if(tbEnderecos.isEnabled()){
            int temp = JOptionPane.YES_NO_OPTION;
            if(situacaoEndereco == situacaoCadastroEndereco.Inserindo){
                temp = JOptionPane.showConfirmDialog(null,"Você não terminou a operação atual, se continuar perderá as informações do endereco.\nProsseguir?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
            }

            if(temp == JOptionPane.YES_NO_OPTION){
                preencherCamposEndereco();
                situacaoEndereco = situacaoCadastroEndereco.Visualizando;
                controlarBotoesEnderecos();
                escolheECB = EscolheECB.Trancado;
                controleComboboxEnderecos();
            }
        }
        
    }//GEN-LAST:event_tbEnderecosMousePressed

    private void btDeltelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDeltelActionPerformed
        
        if(!liTel.isSelectionEmpty()){
            int[] pos = liTel.getSelectedIndices();
            for(int x = (pos.length-1); x>=0; x--){
                liModel.remove(pos[x]);
            }
            JOptionPane.showMessageDialog(null,"Telefone(s) removido(s).","OK",JOptionPane.PLAIN_MESSAGE);
            liTel.setModel(liModel);
        }else{
            JOptionPane.showMessageDialog(null, "Selecione o(s) número(s) que deseja remover.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
        controlaBtRemTel();
        
    }//GEN-LAST:event_btDeltelActionPerformed

    private void btAddtelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btAddtelActionPerformed
        
        String tel = txtTel.getText().trim();
        tel = tel.replaceAll("_", "");
        
        if(tel.length() == 14){
            
            boolean salvar = true;
            for(int x = 0 ; x<liModel.getSize() ; x++){
                if(liModel.getElementAt(x).equals(tel)){
                    salvar = false;
                    break;
                }
            }
            
            if(salvar == true){
                liModel.addElement(tel);
                JOptionPane.showMessageDialog(null,"Telefone adicionado.","OK",JOptionPane.PLAIN_MESSAGE);
            }else{
                JOptionPane.showMessageDialog(null, "O telefone já existe na lista.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
            
        }else{
            JOptionPane.showMessageDialog(null, "Telefone inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        
        txtTel.setText("");
        controlaBtRemTel();
        
    }//GEN-LAST:event_btAddtelActionPerformed

    private void btLimpartelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btLimpartelActionPerformed
       
        txtTel.setText("");
        liTel.clearSelection();
        controlaBtRemTel();
        
    }//GEN-LAST:event_btLimpartelActionPerformed

    private void liTelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_liTelMousePressed
        
        controlaBtRemTel();
        
    }//GEN-LAST:event_liTelMousePressed

    private void cbxEstadosItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxEstadosItemStateChanged
        
        if(cbxModel3.getSelectedItem() != null){
            
            long codE = ((EstadoEntity) cbxModel3.getSelectedItem()).getId();
            List<CidadeEntity> resultado = new CidadeBLL().pesquisaPersonalizada("estado = "+codE);
            carregarComboBoxCidades(resultado);
            escolheECB = EscolheECB.Cidade;
            controleComboboxEnderecos();
            
        }
        
    }//GEN-LAST:event_cbxEstadosItemStateChanged

    private void cbxCidadesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxCidadesItemStateChanged
        
        if(cbxModel2.getSelectedItem() != null){
            
            long codC = ((CidadeEntity) cbxModel2.getSelectedItem()).getId();
            List<BairroEntity> resultado = new BairroBLL().pesquisaPersonalizada("cidade = "+codC);
            carregarComboBoxBairros(resultado);
            escolheECB = EscolheECB.Bairro;
            controleComboboxEnderecos();
            
        }
        
    }//GEN-LAST:event_cbxCidadesItemStateChanged

    private void tbPessoaJuridicasKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPessoaJuridicasKeyReleased
        
        if(tbPessoaJuridicas.isEnabled()){
            carregarTabelaEndereco(null);

            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);

            situacaoEndereco = situacaoCadastroEndereco.Trancado;
            limparCamposEnderecos();
            controlarBotoesEnderecos();
            escolheECB = EscolheECB.Trancado;
            controleComboboxEnderecos();
        }
        
    }//GEN-LAST:event_tbPessoaJuridicasKeyReleased

    private void tbEnderecosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbEnderecosKeyReleased
        
        if(tbEnderecos.isEnabled()){
            int temp = JOptionPane.YES_NO_OPTION;
            if(situacaoEndereco == situacaoCadastroEndereco.Inserindo){
                temp = JOptionPane.showConfirmDialog(null,"Você não terminou a operação atual, se continuar perderá as informações do endereco.\nProsseguir?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);
            }

            if(temp == JOptionPane.YES_NO_OPTION){
                preencherCamposEndereco();
                situacaoEndereco = situacaoCadastroEndereco.Visualizando;
                controlarBotoesEnderecos();
                escolheECB = EscolheECB.Trancado;
                controleComboboxEnderecos();
            }
        }
        
    }//GEN-LAST:event_tbEnderecosKeyReleased

    private void carregarComboBoxCidades(List<CidadeEntity> filtro) {
        cbxModel2.removeAllElements();
        cbxCidades.setModel(cbxModel2);
        if(filtro != null && !filtro.isEmpty()){
            for(CidadeEntity cidade : filtro){
                cbxModel2.addElement(cidade);
            }
        }
        cbxCidades.setSelectedIndex(-1);
    }
    
    private void carregarComboBoxBairros(List<BairroEntity> filtro) {
        cbxModel.removeAllElements();
        cbxBairros.setModel(cbxModel);
        if(filtro != null && !filtro.isEmpty()){
            for(BairroEntity bairro : filtro){
                cbxModel.addElement(bairro);
            }
        }
        cbxBairros.setSelectedIndex(-1);
    }
    
    public void controlaBtRemTel(){
        
        if(liModel.isEmpty()){
            btDeltel.setEnabled(false);
        }else{
            if(!liTel.isSelectionEmpty()){
                btDeltel.setEnabled(true);
            }else{
                btDeltel.setEnabled(false);
            }
        }
        
    }
    
    private enum EscolheECB {
        Estado, Cidade, Bairro, Trancado
    }
    
    private EscolheECB escolheECB;
    
    public final void controleComboboxEnderecos(){
        
        cbxEstados.setEnabled(escolheECB == EscolheECB.Estado || escolheECB == EscolheECB.Cidade || escolheECB == EscolheECB.Bairro);
        cbxCidades.setEnabled(escolheECB == EscolheECB.Cidade || escolheECB == EscolheECB.Bairro);
        cbxBairros.setEnabled(escolheECB == EscolheECB.Bairro);
        txtNomerua.setEnabled(escolheECB == EscolheECB.Bairro);
        txtNumcasa.setEnabled(escolheECB == EscolheECB.Bairro);
        
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btAddtel;
    private javax.swing.JButton btCancelar;
    private javax.swing.JButton btCancelarend;
    private javax.swing.JButton btDeltel;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcel;
    private javax.swing.JButton btExcluir;
    private javax.swing.JButton btExcluirend;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btFiltrar;
    private javax.swing.JButton btLimpartel;
    private javax.swing.JButton btListar;
    private javax.swing.JButton btNovo;
    private javax.swing.JButton btNovoend;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSalvarend;
    private javax.swing.JComboBox cbxBairros;
    private javax.swing.JComboBox cbxCampos;
    private javax.swing.JComboBox cbxCidades;
    private javax.swing.JComboBox cbxEstados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList liTel;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbEnderecos;
    private javax.swing.JTable tbPessoaJuridicas;
    private javax.swing.JFormattedTextField txtCnpj;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNomerua;
    private javax.swing.JTextField txtNumcasa;
    private javax.swing.JFormattedTextField txtTel;
    // End of variables declaration//GEN-END:variables

}
