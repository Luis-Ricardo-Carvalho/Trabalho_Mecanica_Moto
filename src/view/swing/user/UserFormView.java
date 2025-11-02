package view.swing.user;

import controller.UserController;
import model.User;
import model.UserGender;

import javax.swing.*;
import java.awt.*;

class UserFormView extends JDialog implements IUserFormView {
    private final JTextField nomeField = new JTextField(20); 
    private final JComboBox<String> sexoBox = new JComboBox<>(new String[]{"M", "F"}); 
    private final JTextField emailField = new JTextField(20); 
    private final JPasswordField senhaField = new JPasswordField(20);
    private final JButton saveButton = new JButton("Salvar");
    private final JButton closeButton = new JButton("Fechar");

    private UserController controller;
    private final boolean isNew;
    private final UserListView parent;
    private User user;

    public UserFormView(UserListView parent, User user, UserController controller) {
        super(parent, true);
        this.controller = controller;
        this.controller.setUserFormView(this);

        this.parent = parent;
        this.user = user;
        this.isNew = (user == null);

        setTitle(isNew ? "Novo Usuário" : "Editar Usuário");
        setSize(350, 270); 
        setLocationRelativeTo(parent);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1;
        add(nomeField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1;
        add(sexoBox, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1;
        add(senhaField, gbc);

        // Botões
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveButton);
        btnPanel.add(closeButton);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2;
        add(btnPanel, gbc);

        if (!isNew) setUserInForm(user);

        saveButton.addActionListener(e -> controller.saveOrUpdate(isNew));
        closeButton.addActionListener(e -> close());
    }

    @Override
    public User getUserFromForm() {
        if (user == null) user = new User(0);

        user.setName(nomeField.getText());
        user.setGender(sexoBox.getSelectedItem().toString().equals("M") ? UserGender.M : UserGender.F);
        user.setEmail(emailField.getText());
        user.setSenha(new String(senhaField.getPassword()));

        return user;
    }

    @Override
    public void setUserInForm(User user) {
        nomeField.setText(user.getName());
        sexoBox.setSelectedItem(user.getGender().toString());
        emailField.setText(user.getEmail());
        senhaField.setText(user.getSenha());
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
