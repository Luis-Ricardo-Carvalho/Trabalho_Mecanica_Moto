package view.swing;

import javax.swing.*;
import java.awt.*;

import model.User;
import model.UserGender;
import model.data.DAOFactory;
import model.data.UserDAO;
import model.ModelException;

public class RegisterView extends JDialog {

    private final JTextField nameField = new JTextField(20);
    private final JComboBox<String> genderCombo = new JComboBox<>(new String[]{"M", "F"});
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final UserDAO userDAO = DAOFactory.createUserNDAO();

    public RegisterView(JFrame parent) {
        super(parent, "Cadastro de Usuário", true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8,8,8,8);
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Nome:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; form.add(new JLabel("Sexo:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(genderCombo, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST; form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton saveBtn = new JButton("Salvar");
        JButton cancelBtn = new JButton("Cancelar");
        buttons.add(saveBtn);
        buttons.add(cancelBtn);

        saveBtn.addActionListener(e -> saveUser());
        cancelBtn.addActionListener(e -> dispose());

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private void saveUser() {
        String name = nameField.getText().trim();
        String genderStr = (String) genderCombo.getSelectedItem();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            if(userDAO.findByEmail(email) != null){
                JOptionPane.showMessageDialog(this, "Email já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            User user = new User();
            user.setName(name);
            user.setGender(genderStr.equals("M") ? UserGender.M : UserGender.F);
            user.setEmail(email);
            user.setSenha(password);

            userDAO.save(user);

            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            dispose();

        } catch (ModelException me) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar usuário: " + me.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}
