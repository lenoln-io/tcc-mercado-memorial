package view;

import bll.CidadeBLL;
import bll.EstadoBLL;
import dao.AcaoDAO;
import dao.AcessoDAO;
import dao.UsuarioAcaoDAO;
import dao.UsuarioDAO;
import entity.AcessoEntity;
import entity.CidadeEntity;
import entity.EstadoEntity;
import entity.UsuarioAcaoEntity;
import entity.UsuarioEntity;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import model.EstadoTableModel;
import utility.CampoFiltro;
import utility.CriterioFiltro;
import utility.DataUtility;
import utility.ExportarExcel;
import static utility.GeradorDeMascaras.createMaskByIgor;
import utility.Login;


public class EstadoVIEW extends javax.swing.JFrame implements IVIEW {
    
    public final void controlaAcessos(){
        if(Login.getLogin() != 0){
            
            UsuarioEntity user = new UsuarioDAO().pesquisar(Login.getLogin());
            if(user != null){
                
                    List<AcessoEntity> acessosG = new AcessoDAO().pesquisaPersonalizada("tiposistema = 'DESKTOP'");
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

        for (int i = 0; i < modeloTabelaEstado.getColumnCount(); i++) {
            cbxCampos.addItem(new CampoFiltro(
                modeloTabelaEstado.getColumnName(i), modeloTabelaEstado.getnomeTabela(i),
                modeloTabelaEstado.getNomeColunaTabela(i),
                modeloTabelaEstado.getTipoColunaTabela(i))
            );
        }

    }

    private void insereIcones(){
        btNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/new_blue.png")));
        btSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/add.png")));
        btEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/edit.png")));
        btExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/delete.png")));
        btCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cancel.png")));
        btFechar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/cross.png")));
        btExcel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/page_white_excel.png")));
        btFiltrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/magnifier.png")));
        btListar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/layout_content.png")));
    } 
    
    private void carregarTabela() {
        modeloTabelaEstado.Recarregar(controleEstado.listarTodos());
        tbEstados.setModel(modeloTabelaEstado);
    }

    private void carregarTabela(List<EstadoEntity> filtrar) {
        modeloTabelaEstado.Recarregar(filtrar);
        tbEstados.setModel(modeloTabelaEstado);
    }
    
    private enum situacaoCadastro {
        scEsperando, scVisualizando, scInserindo, scEditando
    }
    
    private void limparCampos() {
        txtNome.setText("");
        txtUf.setText("");
        tbEstados.clearSelection();
    }
    
    private void controlarCampos(boolean condicao) {
        txtNome.setEnabled(condicao);
        txtUf.setEnabled(condicao);
        tbEstados.setEnabled(!condicao);
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
        objetoEstado = modeloTabelaEstado.getObjeto(tbEstados.getSelectedRow()).getClone();
        txtNome.setText(objetoEstado.getNome());
        txtUf.setText(objetoEstado.getUf());
    }
    
    private situacaoCadastro situacao;
    private EstadoEntity objetoEstado;
    private final EstadoBLL controleEstado = new EstadoBLL();
    private final EstadoTableModel modeloTabelaEstado = new EstadoTableModel();
    
    private void criaMascaras(){
        createMaskByIgor("AA",'_').install(txtUf);
    }
    
    public EstadoVIEW() {
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
        txtUf = new javax.swing.JFormattedTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        spTabela = new javax.swing.JScrollPane();
        tbEstados = new javax.swing.JTable();
        pnFiltros = new javax.swing.JPanel();
        btExcel = new javax.swing.JButton();
        cbxCampos = new javax.swing.JComboBox();
        txtCriterio = new javax.swing.JTextField();
        btFiltrar = new javax.swing.JButton();
        btListar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Cadastro de estados");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btNovo.setMnemonic('n');
        btNovo.setText("Novo");
        btNovo.setToolTipText("<html>Registrar novo estado<br />Atalho: alt + n</html>");
        btNovo.setName("btNovoEstado"); // NOI18N
        btNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNovoActionPerformed(evt);
            }
        });

        btSalvar.setMnemonic('s');
        btSalvar.setText("Salvar");
        btSalvar.setToolTipText("<html>Salvar dados do estado<br />Atalho: alt + s</html>");
        btSalvar.setName("btSalvarEstado"); // NOI18N
        btSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btSalvarActionPerformed(evt);
            }
        });

        btEditar.setMnemonic('e');
        btEditar.setText("Editar");
        btEditar.setToolTipText("<html>Editar dados do estado<br />Atalho: alt + e</html>");
        btEditar.setName("btEditarEstado"); // NOI18N
        btEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEditarActionPerformed(evt);
            }
        });

        btExcluir.setMnemonic('r');
        btExcluir.setText("Excluir");
        btExcluir.setToolTipText("<html>Excluír estado<br />Atalho: alt + r</html>");
        btExcluir.setName("btExcluirEstado"); // NOI18N
        btExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btExcluirActionPerformed(evt);
            }
        });

        btCancelar.setMnemonic('c');
        btCancelar.setText("Cancelar");
        btCancelar.setToolTipText("<html>Cancelar operação atual<br />Atalho: alt + c</html>");
        btCancelar.setName("btCancelarEstado"); // NOI18N
        btCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btCancelarActionPerformed(evt);
            }
        });

        btFechar.setMnemonic('f');
        btFechar.setText("Fechar");
        btFechar.setToolTipText("<html>Fechar menu de cadastro de estados<br />Atalho: alt + f</html>");
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

        jLabel4.setText("UF:");

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
                .add(jPanel3Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(txtUf, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 685, Short.MAX_VALUE)
                    .add(txtNome))
                .addContainerGap())
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
                    .add(txtUf, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                .add(jPanel3, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tbAbas.addTab("Campos", pnCampos);

        tbEstados.setModel(new javax.swing.table.DefaultTableModel(
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
        tbEstados.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tbEstados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tbEstadosMousePressed(evt);
            }
        });
        tbEstados.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbEstadosKeyReleased(evt);
            }
        });
        spTabela.setViewportView(tbEstados);

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
        btListar.setToolTipText("<html>Mostrar todas as categorias na tabela<br />Atalho: alt + l</html>");
        btListar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btListarActionPerformed(evt);
            }
        });

        jLabel1.setText("Critério:");

        jLabel2.setText("Campo:");

        org.jdesktop.layout.GroupLayout pnFiltrosLayout = new org.jdesktop.layout.GroupLayout(pnFiltros);
        pnFiltros.setLayout(pnFiltrosLayout);
        pnFiltrosLayout.setHorizontalGroup(
            pnFiltrosLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(pnFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .add(btExcel)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(cbxCampos, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(txtCriterio, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 219, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
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
                    .add(pnFiltros, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(tbAbas, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 119, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(spTabela, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 158, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        
        String nome = txtNome.getText().trim();
        String uf = txtUf.getText().trim();
        
        uf = uf.replace("_", "");
        
        if(!nome.equals("") && !uf.equals("")){
            if(uf.length() == 2){
                if (situacao == situacaoCadastro.scInserindo) {
                    objetoEstado = new EstadoEntity();
                    objetoEstado.setId(0);
                }

                objetoEstado.setNome(nome);
                objetoEstado.setUf(uf);

                boolean salvar = true;
                List<EstadoEntity> estados = controleEstado.listarTodos();
                for(EstadoEntity estado : estados){
                    if(objetoEstado.getId() == 0){
                        if(objetoEstado.getNome().equals(estado.getNome()) || objetoEstado.getUf().equals(estado.getUf())){
                            salvar = false;
                            break;
                        }
                    }else{
                        if((objetoEstado.getNome().equals(estado.getNome()) || objetoEstado.getUf().equals(estado.getUf())) && objetoEstado.getId() != estado.getId()){
                            salvar = false;
                            break;
                        }
                    }
                }
                if(salvar == true){

                    controleEstado.salvar(objetoEstado);

                    if (situacao == situacaoCadastro.scInserindo) {
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoDAO().pesquisaPersonalizada("nome = 'Realizou um cadastro'").get(0));
                        ul.setDescricao("Cadastrou o estado de ID = "+objetoEstado.getId());
                        ul.setUsuario(new UsuarioDAO().pesquisar(Login.getLogin()));
                        ul.setHorario(DataUtility.getDateTime("EN"));
                        new UsuarioAcaoDAO().salvar(ul);
                        
                        JOptionPane.showMessageDialog(null,"O registro foi salvo com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                    }else{
                        
                        UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                        ul.setAcao(new AcaoDAO().pesquisaPersonalizada("nome = 'Alterou um registro'").get(0));
                        ul.setDescricao("Alterou o estado de ID = "+objetoEstado.getId());
                        ul.setUsuario(new UsuarioDAO().pesquisar(Login.getLogin()));
                        ul.setHorario(DataUtility.getDateTime("EN"));
                        new UsuarioAcaoDAO().salvar(ul);
                        
                        JOptionPane.showMessageDialog(null,"O registro foi atualizado com sucesso.","OK",JOptionPane.PLAIN_MESSAGE);
                    }
                    
                    carregarTabela();
                    limparCampos();
                    situacao = situacaoCadastro.scEsperando;
                    controlarBotoes();
                    controlarCampos(false);
                    
                }else{
                    JOptionPane.showMessageDialog(null,"Já existe um registro com esse nome ou UF.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                JOptionPane.showMessageDialog(null,"Unidade federativa inválida.","ERRO",JOptionPane.ERROR_MESSAGE);
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
        
        boolean deletar = true;
        CidadeBLL controleCidade = new CidadeBLL();
        List<CidadeEntity> cidades = controleCidade.listarTodos();
        for(CidadeEntity cidade : cidades){
            if(cidade.getEstado().getId() == objetoEstado.getId()){
                deletar = false;
                break;
            }
        }
        
        if(deletar == true){
            int temp = JOptionPane.showConfirmDialog(null,"Deseja excluir o registro?", "Alerta do Sistema",JOptionPane.YES_NO_OPTION);

            if (temp == JOptionPane.YES_OPTION) {
                
                UsuarioAcaoEntity ul = new UsuarioAcaoEntity();
                ul.setAcao(new AcaoDAO().pesquisaPersonalizada("nome = 'Excluiu um registro'").get(0));
                ul.setDescricao("Excluiu o estado "+objetoEstado.getNome()+" de ID = "+objetoEstado.getId());
                ul.setUsuario(new UsuarioDAO().pesquisar(Login.getLogin()));
                ul.setHorario(DataUtility.getDateTime("EN"));
                new UsuarioAcaoDAO().salvar(ul);
                
                controleEstado.excluir(objetoEstado);
                carregarTabela();
                limparCampos();
                situacao = situacaoCadastro.scEsperando;
                controlarBotoes();
                controlarCampos(false);
            }
        }else{
            JOptionPane.showMessageDialog(null,"Há registros associados a esse estado.","ERRO",JOptionPane.ERROR_MESSAGE);
        }
        
    }//GEN-LAST:event_btExcluirActionPerformed

    private void btCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btCancelarActionPerformed
        
        situacao = situacaoCadastro.scEsperando;
        controlarBotoes();
        controlarCampos(false);
        limparCampos();
        
    }//GEN-LAST:event_btCancelarActionPerformed

    private void tbEstadosMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbEstadosMousePressed
        
        if(tbEstados.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbEstadosMousePressed

    private void btExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btExcelActionPerformed
        
        try {

            JFileChooser arquivoSalvar = new JFileChooser();
            int retorno = arquivoSalvar.showSaveDialog(null);

            if (retorno == JFileChooser.APPROVE_OPTION) {

                String caminho = arquivoSalvar.getSelectedFile().getAbsolutePath() + ".xls";
                ExportarExcel exportarExcel = new ExportarExcel();
                exportarExcel.GerarArquivo(tbEstados, new File(caminho));
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
                    carregarTabela(new EstadoBLL().filtrar(criterio));
                }
                catch(NumberFormatException e){
                    JOptionPane.showMessageDialog(null,"Valor inválido para o critério.","ERRO",JOptionPane.ERROR_MESSAGE);
                }
            }else{
                if("string".equals(criterio.getCampo().getTipoColuna())){
                    carregarTabela(new EstadoBLL().filtrar(criterio));
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
        
        carregarTabela(new EstadoBLL().listarTodos());
        
    }//GEN-LAST:event_btListarActionPerformed

    private void tbEstadosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbEstadosKeyReleased
        
        if(tbEstados.isEnabled()){
            preencherCampos();
            situacao = situacaoCadastro.scVisualizando;
            controlarBotoes();
            controlarCampos(false);
        }
        
    }//GEN-LAST:event_tbEstadosKeyReleased

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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel pnCampos;
    private javax.swing.JPanel pnFiltros;
    private javax.swing.JScrollPane spTabela;
    private javax.swing.JTabbedPane tbAbas;
    private javax.swing.JTable tbEstados;
    private javax.swing.JTextField txtCriterio;
    private javax.swing.JTextField txtNome;
    private javax.swing.JFormattedTextField txtUf;
    // End of variables declaration//GEN-END:variables

}
