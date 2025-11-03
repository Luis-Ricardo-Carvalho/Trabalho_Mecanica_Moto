package view.swing.motos;

import controller.MotosController;
import model.Motos;
import model.Pecas;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class MotosFormView extends JDialog implements IMotosFormView {
    private final JTextField modeloField = new JTextField(20);
    private final JTextField marcaField = new JTextField(20);
    private final JTextArea descricaoArea = new JTextArea(4, 20);
    private final JComboBox<Pecas> pecaComboBox = new JComboBox<>();

    private final JButton saveButton = new JButton("Salvar");
    private final JButton closeButton = new JButton("Fechar");

    private MotosController controller;
    private final boolean isNew;
    private final MotosListView parent;
    private Motos moto;

    public MotosFormView(MotosListView parent, Motos moto, MotosController controller, List<Pecas> pecasList) {
        super(parent, true);
        this.controller = controller;
        this.controller.setMotoFormView(this);

        this.parent = parent;
        this.moto = moto;
        this.isNew = (moto == null);

        setTitle(isNew ? "Nova Moto" : "Editar Moto");
        setSize(400, 300);
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Modelo:"), gbc);
        gbc.gridx = 1;
        add(modeloField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Marca:"), gbc);
        gbc.gridx = 1;
        add(marcaField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Descrição:"), gbc);
        gbc.gridx = 1;
        add(new JScrollPane(descricaoArea), gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Peça associada:"), gbc);
        gbc.gridx = 1;
        pecaComboBox.setModel(new DefaultComboBoxModel<>(pecasList.toArray(new Pecas[0])));
        add(pecaComboBox, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) setMotoInForm(moto);

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    @Override
    public Motos getMotoFromForm() {
        if (moto == null) moto = new Motos(0);
        moto.setModelo(modeloField.getText());
        moto.setMarca(marcaField.getText());
        moto.setDescricao(descricaoArea.getText());
        moto.setPeca((Pecas) pecaComboBox.getSelectedItem());
        return moto;
    }

    @Override
    public void setMotoInForm(Motos moto) {
        modeloField.setText(moto.getModelo());
        marcaField.setText(moto.getMarca());
        descricaoArea.setText(moto.getDescricao());
        pecaComboBox.setSelectedItem(moto.getPeca());
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
