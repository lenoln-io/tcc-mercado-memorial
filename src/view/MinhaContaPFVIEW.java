package view;

import bll.BairroBLL;
import bll.CidadeBLL;
import bll.EstadoBLL;
import bll.PessoaFisicaBLL;
import bll.AcaoBLL;
import bll.AcessoBLL;
import bll.UsuarioAcaoBLL;
import bll.UsuarioBLL;
import entity.AcessoEntity;
import entity.EnderecoEntity;
import entity.BairroEntity;
import entity.CidadeEntity;
import entity.EstadoEntity;
import entity.PessoaFisicaEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import model.EnderecoTableModel;
import utility.DataUtility;
import utility.EmailValidator;
import static utility.GeradorDeMascaras.createMaskByIgor;
import utility.Login;

public class MinhaContaPFVIEW extends javax.swing.JFrame implements IVIEW {
    
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
                        if(this.btEditar.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btEditar.setVisible(false);
                        } else
                        if(this.btDesativarConta.getName().equalsIgnoreCase(acessosG.get(x).getRecurso())){
                            btDesativarConta.setVisible(false);
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
    
    private void insereIcones(){
        btAddtel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/add.png")));
        btDeltel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/delete.png")));
        btLimpartel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        
        btSalvarend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/add.png")));
        btExcluirend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/delete.png")));
        btCancelarend.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
    } 
    
    private void carregarTabelaEndereco() {
        if(objetoPessoaFisica != null && objetoPessoaFisica.getEnderecos() != null){
            modeloTabelaEndereco.Recarregar(objetoPessoaFisica.getEnderecos());
        }
        tbEnderecos.setModel(modeloTabelaEndereco);
    }
    
    private enum situacaoCadastro {
        scEsperando, scEditando
    }
    
    private enum situacaoCadastroEndereco {
        Esperando, Visualizando, Inserindo, Trancado
    }
    
    private enum EscolheECB {
        Estado, Cidade, Bairro, Trancado
    }
    
    private void limparCamposEnderecos() {
        objetoEndereco = null;
        txtNomerua.setText("");
        txtNumcasa.setText("");
        cbxBairros.setSelectedIndex(-1);
        cbxCidades.setSelectedIndex(-1);
        cbxEstados.setSelectedIndex(-1);
        cbControleSenha.setSelected(false);
        txtConfirmacao.setText("");
        txtSenha.setText("");
        carregarComboBoxBairros(null);
        carregarComboBoxCidades(null);
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(condicao);
        txtCpf.setEnabled(condicao);
        txtDtnasc.setEnabled(condicao);
        rbFem.setEnabled(condicao);
        rbMasc.setEnabled(condicao);
        tbEnderecos.setEnabled(condicao);
        btAddtel.setEnabled(condicao);
        btDeltel.setEnabled(condicao);
        btLimpartel.setEnabled(condicao);
        liTel.setEnabled(condicao);
        txtTel.setEnabled(condicao);
        txtEmail.setEnabled(condicao);
        cbControleSenha.setEnabled(condicao);
        if(condicao == false){
            txtConfirmacao.setEnabled(condicao);
            txtSenha.setEnabled(condicao);
        }
    }
    
    public final void controlarBotoesEnderecos(){
        if(situacao == situacaoCadastro.scEditando){
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
        btSalvar.setEnabled(situacao == situacaoCadastro.scEditando);
        btEditar.setEnabled(situacao == situacaoCadastro.scEsperando);
        btCancelar.setEnabled(situacao == situacaoCadastro.scEditando);
        btDesativarConta.setEnabled(situacao == situacaoCadastro.scEsperando);
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
    
    private void preencherCampos(){
        objetoUsuario = controleUsuario.pesquisar(Login.getLogin());
        txtEmail.setText(objetoUsuario.getEmail());
        objetoPessoaFisica = controlePessoaFisica.pesquisar(objetoUsuario.getPessoa().getId());
        txtNome.setText(objetoPessoaFisica.getNome());
        txtCpf.setText(objetoPessoaFisica.getCpf());
        txtDtnasc.setDate(objetoPessoaFisica.getDataNascimento());
        if(objetoPessoaFisica.getSexo().equalsIgnoreCase("Feminino")){
            rbFem.setSelected(true);
        }else{
            rbMasc.setSelected(true);
        }
        carregarTabelaEndereco();
        recarregarLista(objetoPessoaFisica.getTelefones());
    }
    
    private situacaoCadastro situacao;
    private situacaoCadastroEndereco situacaoEndereco;
    private EscolheECB escolheECB;
    private PessoaFisicaEntity objetoPessoaFisica;
    private UsuarioEntity objetoUsuario;
    private EnderecoEntity objetoEndereco;
    private final PessoaFisicaBLL controlePessoaFisica = new PessoaFisicaBLL();
    private final UsuarioBLL controleUsuario = new UsuarioBLL();
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
        createMaskByIgor("###.###.###-##",'_').install(txtCpf);
        createMaskByIgor("(##) ####-####",'_').install(txtTel);
    }
    
    public MinhaContaPFVIEW() {
        initComponents();
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        this.setLocationRelativeTo(null);
        criaMascaras();
        situacao = situacaoCadastro.scEsperando;
        situacaoEndereco = situacaoCadastroEndereco.Trancado;
        controlarBotoes();
        controlarBotoesEnderecos();
        controlarCampos(false);
        carregarComboBoxEstado();
        insereIcones();
        desabilitaBtFecharEAtalhos();
        carregarTabelaEndereco();
        carregarLista();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        controlaAcessos();
        preencherCampos();
    }
    
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
        if(situacao == situacaoCadastro.scEditando){
                    
            int temp = JOptionPane.showConfirmDialog(null,"Você não salvou a operação atual, se sair perderá as informações desse registro.\nDeseja mesmo sair?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

            if (temp == JOptionPane.YES_OPTION) {
                dispose();
                new MenuPrincipalVIEW().setVisible(true);
            }

        }else{
            dispose();
            new MenuPrincipalVIEW().setVisible(true);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        btSalvar = new javax.swing.JButton();
        btEditar = new javax.swing.JButton();
        btCancelar = new javax.swing.JButton();
        btFechar = new javax.swing.JButton();
        btDesativarConta = new javax.swing.JButton();
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
        rbMasc = new javax.swing.JRadioButton();
        jLabel2 = new javax.swing.JLabel();
        rbFem = new javax.swing.JRadioButton();
        txtNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        txtDtnasc = new org.jdesktop.swingx.JXDatePicker();
        txtCpf = new javax.swing.JFormattedTextField();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtEmail = new javax.swing.JTextField();
        jLabel16 = new javax.swing.JLabel();
        txtSenha = new javax.swing.JPasswordField();
        jLabel15 = new javax.swing.JLabel();
        txtConfirmacao = new javax.swing.JPasswordField();
        jLabel17 = new javax.swing.JLabel();
        cbControleSenha = new javax.swing.JCheckBox();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Meus dados - Pessoa física");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/vcard_add.png"))); // NOI18N
        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarMeusDados"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/vcard_edit.png"))); // NOI18N
        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarMeusDados"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarMeusDados"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu<br />Atalho: alt + f</html>");
        btFechar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btFecharActionPerformed(evt);
            }
        });

        btDesativarConta.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/user_cross.png"))); // NOI18N
        btDesativarConta.setMnemonic('d');
        btDesativarConta.setText("Desativar conta");
        btDesativarConta.setToolTipText("<html>Desativar conta<br />Atalho: alt + d</html>");
        btDesativarConta.setName("btDesativarMeusDados"); // NOI18N
        btDesativarConta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDesativarContaActionPerformed(evt);
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
                .add(btCancelar)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(btDesativarConta)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(btFechar)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(btSalvar)
                    .add(btEditar)
                    .add(btCancelar)
                    .add(btFechar)
                    .add(btDesativarConta))
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
        cbxEstados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbxEstadosMouseClicked(evt);
            }
        });
        cbxEstados.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cbxEstadosPropertyChange(evt);
            }
        });

        jLabel13.setText("Cidades:");

        cbxCidades.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxCidadesItemStateChanged(evt);
            }
        });
        cbxCidades.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                cbxCidadesPropertyChange(evt);
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
                    .add(cbxEstados, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(txtNumcasa)
                    .add(txtNomerua)
                    .add(cbxBairros, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(cbxCidades, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                            .add(jLabel12)
                            .add(jLabel13)
                            .add(jLabel6)
                            .add(jLabel7)
                            .add(jLabel5))
                        .add(0, 62, Short.MAX_VALUE)))
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
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
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
                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
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

        btLimpartel.setText("Cancelar");
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
                    .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 346, Short.MAX_VALUE)
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

        jLabel3.setText("CPF:");

        buttonGroup1.add(rbMasc);
        rbMasc.setText("Masculino");

        jLabel2.setText("Sexo:");

        buttonGroup1.add(rbFem);
        rbFem.setText("Feminino");

        jLabel4.setText("<html>Data de nascimento:</html>");

        jLabel1.setText("Nome:");

        jLabel11.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel11.setText("Dados de indetificação:");

        org.jdesktop.layout.GroupLayout jPanel4Layout = new org.jdesktop.layout.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtCpf)
                    .add(txtNome)
                    .add(txtDtnasc, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel4Layout.createSequentialGroup()
                        .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel1)
                            .add(jLabel11)
                            .add(jLabel2)
                            .add(jPanel4Layout.createSequentialGroup()
                                .add(rbMasc)
                                .add(18, 18, 18)
                                .add(rbFem))
                            .add(jLabel3)
                            .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(0, 197, Short.MAX_VALUE)))
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
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel4Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(rbMasc)
                    .add(rbFem))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel3)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCpf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtDtnasc, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel5.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel14.setText("Email:");

        jLabel16.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel16.setText("Dados da conta:");

        jLabel15.setText("Nova senha:");

        jLabel17.setText("Senha atual:");

        cbControleSenha.setText("Destravar");
        cbControleSenha.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbControleSenhaMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cbControleSenhaMouseReleased(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jLabel18.setText("Alterar senha");

        org.jdesktop.layout.GroupLayout jPanel5Layout = new org.jdesktop.layout.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtEmail)
                    .add(txtConfirmacao)
                    .add(txtSenha)
                    .add(jPanel5Layout.createSequentialGroup()
                        .add(jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(jLabel14)
                            .add(jLabel16)
                            .add(cbControleSenha)
                            .add(jLabel18)
                            .add(jLabel17)
                            .add(jLabel15))
                        .add(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .add(jLabel16)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel14)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtEmail, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel18)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbControleSenha)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel17)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtConfirmacao, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel15)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtSenha, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout pnCamposLayout = new org.jdesktop.layout.GroupLayout(pnCampos);
        pnCampos.setLayout(pnCamposLayout);
        pnCamposLayout.setHorizontalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        pnCamposLayout.setVerticalGroup(
            pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
            .add(org.jdesktop.layout.GroupLayout.LEADING, pnCamposLayout.createSequentialGroup()
                .addContainerGap()
                .add(pnCamposLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(pnCamposLayout.createSequentialGroup()
                        .add(jPanel4, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(jPanel5, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        tbAbas.addTab("Campos", pnCampos);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(tbAbas))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tbAbas)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btFecharActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btFecharActionPerformed
        
        acaoFechar();
        
    }//GEN-LAST:event_btFecharActionPerformed

    private void btEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEditarActionPerformed
        
        situacao = situacaoCadastro.scEditando;
        controlarBotoes();
        controlarCampos(true);
        controlaBtRemTel();
        situacaoEndereco = situacaoCadastroEndereco.Esperando;
        controlarBotoesEnderecos();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        
    }//GEN-LAST:event_btEditarActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        situacaoEndereco = situacaoCadastroEndereco.Trancado;
        controlarBotoesEnderecos();
        limparCamposEnderecos();
        escolheECB = EscolheECB.Trancado;
        controleComboboxEnderecos();
        controlarCampos(false);
        preencherCampos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

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
                    if(objetoPessoaFisica != null){
                        if(objetoPessoaFisica.getEnderecos() != null && !objetoPessoaFisica.getEnderecos().isEmpty()){
                            for(EnderecoEntity e : objetoPessoaFisica.getEnderecos()){
                                if(e.getBairro().getId() == objetoEndereco.getBairro().getId() &&
                                        e.getNomeRua().equals(objetoEndereco.getNomeRua()) &&
                                        e.getNumCasa() == objetoEndereco.getNumCasa()){
                                    salvar = false;
                                }
                            }
                        }
                    }

                    if(salvar == true){
                        if(objetoPessoaFisica == null){
                            objetoPessoaFisica = new PessoaFisicaEntity();
                        }

                        objetoPessoaFisica.addEndereco(objetoEndereco);
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
            temp = JOptionPane.showConfirmDialog(null,"Você não terminou de salvar o endereço, se continuar perderá as informações do endereco.\nProsseguir?", "AVISO",JOptionPane.YES_NO_OPTION);
        }
        
        if(temp == JOptionPane.YES_NO_OPTION){
            
            txtNome.setText(txtNome.getText().trim());
            txtCpf.setText(txtCpf.getText().trim());
            txtEmail.setText(txtEmail.getText().trim());
            
            if(!txtEmail.getText().isEmpty() && !txtNome.getText().equals("") && txtCpf.getText().length() == 14 && txtDtnasc.getDate() != null && (rbFem.isSelected() || rbMasc.isSelected())){
                EmailValidator ev = new EmailValidator();
                if(ev.validate(txtEmail.getText()) == true){
                    List<UsuarioEntity> resultadoe;
                    resultadoe = controleUsuario.pesquisaPersonalizada("email = '"+txtEmail.getText()+"' and id <> "+objetoUsuario.getId());

                    if(resultadoe == null || resultadoe.isEmpty()){
                        List<PessoaFisicaEntity> resultadoc;
                        resultadoc = controlePessoaFisica.pesquisaPersonalizada("cpf = '"+txtCpf.getText()+"' and pessoa <> "+objetoPessoaFisica.getId());

                        if(resultadoc == null || resultadoc.isEmpty()){

                            if(objetoPessoaFisica != null){
                                if(objetoPessoaFisica.getEnderecos() != null && !objetoPessoaFisica.getEnderecos().isEmpty()){

                                    if(!liModel.isEmpty()){
                                        if(objetoPessoaFisica.getTelefones() != null && !objetoPessoaFisica.getTelefones().isEmpty()){
                                            objetoPessoaFisica.getTelefones().clear();
                                        }
                                        for(int x = 0 ; x<liModel.size() ; x++){
                                            objetoPessoaFisica.addTel(liModel.getElementAt(x));
                                        }
                                        objetoPessoaFisica.setNome(txtNome.getText());
                                        objetoPessoaFisica.setCpf(txtCpf.getText());
                                        objetoPessoaFisica.setDataNascimento(new java.sql.Date(txtDtnasc.getDate().getTime()));
                                        if(rbFem.isSelected()){
                                            objetoPessoaFisica.setSexo("Feminino");
                                        }else{
                                            objetoPessoaFisica.setSexo("Masculino");
                                        }

                                        objetoUsuario.setEmail(txtEmail.getText());

                                        boolean senhaok = true;
                                        if(cbControleSenha.isSelected()){
                                            String confirmar = "";
                                            for(int x = 0 ; x < txtConfirmacao.getPassword().length ; x++){
                                                confirmar += txtConfirmacao.getPassword()[x];
                                            }
                                            confirmar = confirmar.trim();
                                            if(!confirmar.isEmpty()){
                                                List<UsuarioEntity> usuarior = controleUsuario.pesquisaPersonalizada("id = "+objetoUsuario.getId()+" and senha = md5('"+confirmar+"')");
                                                if(usuarior != null && !usuarior.isEmpty()){
                                                    String senha = "";
                                                    for(int x = 0 ; x < txtSenha.getPassword().length ; x++){
                                                        senha += txtSenha.getPassword()[x];
                                                    }
                                                    senha = senha.trim();
                                                    if(!senha.isEmpty()){
                                                        objetoUsuario.setSenha(senha);
                                                    }else{
                                                        JOptionPane.showMessageDialog(null, "Nova senha em branco.", "ERRO", JOptionPane.ERROR_MESSAGE);
                                                        senhaok = false;
                                                    }
                                                }else{
                                                    JOptionPane.showMessageDialog(null, "Senha de confirmação inválida.", "ERRO", JOptionPane.ERROR_MESSAGE);
                                                    senhaok = false;
                                                }
                                            }else{
                                                JOptionPane.showMessageDialog(null, "Senha de confirmação inválida.", "ERRO", JOptionPane.ERROR_MESSAGE);
                                                senhaok = false;
                                            }
                                        }else{
                                            objetoUsuario.setSenha(null);
                                        }
                                        
                                        if(senhaok == true){
                                            controleUsuario.salvar(objetoUsuario);
                                            controlePessoaFisica.salvar(objetoPessoaFisica);

                                            UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                                            ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Alteração de dados'").get(0));
                                            ul.setDescricao("O usuário de ID = "+objetoUsuario.getId()+" alterou seus dados.");
                                            ul.setUsuario(objetoUsuario);
                                            ul.setHorario(DataUtility.getDateTime("EN"));
                                            new UsuarioAcaoBLL().salvar(ul);

                                            JOptionPane.showMessageDialog(null,"Os dados da sua conta foram atualizados com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);

                                            situacao = situacaoCadastro.scEsperando;
                                            controlarBotoes();
                                            limparCamposEnderecos();
                                            situacaoEndereco = situacaoCadastroEndereco.Trancado;
                                            controlarBotoesEnderecos();
                                            escolheECB = EscolheECB.Trancado;
                                            controleComboboxEnderecos();
                                            controlarCampos(false);
                                            preencherCampos();
                                        }

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
                            JOptionPane.showMessageDialog(null, "CPF inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
                        }
                    }else{
                        JOptionPane.showMessageDialog(null, "Email inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
                    }
                }else{
                    JOptionPane.showMessageDialog(null, "Email inválido.", "ERRO", JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null, "Preecnha todos os campos.", "ERRO", JOptionPane.ERROR_MESSAGE);
            }
        }
        
    }//GEN-LAST:event_btSalvarActionPerformed

    private void btExcluirendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcluirendActionPerformed
        
        int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o endereco?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

        if (temp == JOptionPane.YES_OPTION) {
            objetoPessoaFisica.delEndereco(objetoEndereco);
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

    private void cbxEstadosPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cbxEstadosPropertyChange
        
    }//GEN-LAST:event_cbxEstadosPropertyChange

    private void cbxCidadesPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_cbxCidadesPropertyChange
        
    }//GEN-LAST:event_cbxCidadesPropertyChange

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

    private void cbxEstadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbxEstadosMouseClicked
        
    }//GEN-LAST:event_cbxEstadosMouseClicked

    private void btDesativarContaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDesativarContaActionPerformed
        
        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
        ul.setAcao(new AcaoBLL().pesquisaPersonalizada("nome = 'Desativação do usuário'").get(0));
        ul.setDescricao("O usuário de ID = "+objetoUsuario.getId()+" destivou sua conta.");
        ul.setUsuario(objetoUsuario);
        ul.setHorario(DataUtility.getDateTime("EN"));
        new UsuarioAcaoBLL().salvar(ul);
        
        objetoUsuario = controleUsuario.pesquisar(Login.getLogin());
        objetoUsuario.setEstatus("Desativado");
        controleUsuario.salvar(objetoUsuario);
        Login.logout();
        this.dispose();
        JOptionPane.showMessageDialog(null,"Seu usuário foi desativado!","OK",JOptionPane.PLAIN_MESSAGE);
        new LoginVIEW().setVisible(true);
        
    }//GEN-LAST:event_btDesativarContaActionPerformed

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

    private void cbControleSenhaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbControleSenhaMousePressed

    }//GEN-LAST:event_cbControleSenhaMousePressed

    private void cbControleSenhaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbControleSenhaMouseReleased

        if(cbControleSenha.isSelected()){
            txtConfirmacao.setEnabled(true);
            txtSenha.setEnabled(true);
            txtConfirmacao.setText("");
            txtSenha.setText("");
        }else{
            txtConfirmacao.setEnabled(false);
            txtSenha.setEnabled(false);
            txtConfirmacao.setText("");
            txtSenha.setText("");
        }

    }//GEN-LAST:event_cbControleSenhaMouseReleased

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
    private javax.swing.JButton btDesativarConta;
    private javax.swing.JButton btEditar;
    private javax.swing.JButton btExcluirend;
    private javax.swing.JButton btFechar;
    private javax.swing.JButton btLimpartel;
    private javax.swing.JButton btNovoend;
    private javax.swing.JButton btSalvar;
    private javax.swing.JButton btSalvarend;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox cbControleSenha;
    private javax.swing.JComboBox cbxBairros;
    private javax.swing.JComboBox cbxCidades;
    private javax.swing.JComboBox cbxEstados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JList liTel;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JRadioButton rbFem;
    private javax.swing.JRadioButton rbMasc;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbEnderecos;
    private javax.swing.JPasswordField txtConfirmacao;
    private javax.swing.JFormattedTextField txtCpf;
    private org.jdesktop.swingx.JXDatePicker txtDtnasc;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNomerua;
    private javax.swing.JTextField txtNumcasa;
    private javax.swing.JPasswordField txtSenha;
    private javax.swing.JFormattedTextField txtTel;
    // End of variables declaration//GEN-END:variables

}
