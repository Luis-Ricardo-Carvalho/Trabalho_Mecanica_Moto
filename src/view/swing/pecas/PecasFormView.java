package view.swing.pecas;

import controller.PecasController;
import model.Pecas;
import model.User;

import javax.swing.*;
import java.awt.*;

class PecasFormView extends JDialog implements IPecasFormView {
    private final JTextField nomeField = new JTextField(20);
    private final JTextField marcaField = new JTextField(20);
    private final JTextField categoriaField = new JTextField(20);
    private final JSpinner quantidadeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));

    private final JButton saveButton = new JButton("Salvar");
    private final JButton closeButton = new JButton("Fechar");

    private PecasController controller;
    private final boolean isNew;
    private final PecasListView parent;
    private Pecas peca;

    public PecasFormView(PecasListView parent, Pecas peca, PecasController controller) {
        super(parent, true);
        this.controller = controller;
        this.controller.setPecaFormView(this);

        this.parent = parent;
        this.peca = peca;
        this.isNew = (peca == null);

        setTitle(isNew ? "Nova Peça" : "Editar Peça");
        setSize(400, 260);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nome
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nomeField, gbc);

        // Marca
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        add(marcaField, gbc);

        // Categoria
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Categoria:"), gbc);
        gbc.gridx = 1;
        add(categoriaField, gbc);

        // Quantidade
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Quantidade:"), gbc);
        gbc.gridx = 1;
        add(quantidadeSpinner, gbc);

        // Botões
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) setPecaInForm(peca);

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    @Override
    public Pecas getPecaFromForm() {
        if (peca == null) peca = new Pecas(0);
        peca.setName(nomeField.getText());
        peca.setMarca(marcaField.getText());
        peca.setCategoria(categoriaField.getText());
        peca.setQuantidade((int) quantidadeSpinner.getValue());
        peca.setUser(new User(1));

        return peca;
    }

    @Override
    public void setPecaInForm(Pecas peca) {
        nomeField.setText(peca.getName());
        marcaField.setText(peca.getMarca());
        categoriaField.setText(peca.getCategoria());
        quantidadeSpinner.setValue(peca.getQuantidade());
    }

    @Override
    public void showInfoMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void showErrorMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Erro", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public void close() {
        parent.refresh();
        dispose();
    }
}
