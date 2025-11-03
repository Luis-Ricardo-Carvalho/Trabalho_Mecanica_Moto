package view.swing;

import javax.swing.*;
import java.awt.*;

import model.User;
import model.data.DAOFactory;
import model.data.UserDAO;
import model.ModelException;

public class LoginView extends JDialog {
    private boolean authenticated = false;
    private final JTextField emailField = new JTextField(20);
    private final JPasswordField passwordField = new JPasswordField(20);
    private final UserDAO userDAO = DAOFactory.createUserNDAO();

    public LoginView(JFrame parent) {
        super(parent, "Mecânica Motos - Login", true);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.EAST;

        gbc.gridx = 0; gbc.gridy = 0; form.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(emailField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST; form.add(new JLabel("Senha:"), gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST; form.add(passwordField, gbc);

        JPanel buttons = new JPanel();
        JButton loginBtn = new JButton("Entrar");
        //JButton registerBtn = new JButton("Cadastrar");
        JButton cancelBtn = new JButton("Cancelar");

        buttons.add(loginBtn);
        //buttons.add(registerBtn);
        buttons.add(cancelBtn);

        loginBtn.addActionListener(e -> authenticate());
        //registerBtn.addActionListener(e -> openRegister());
        cancelBtn.addActionListener(e -> { authenticated = false; dispose(); });

        add(form, BorderLayout.CENTER);
        add(buttons, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(parent);
    }

    private void authenticate() {
        String email = emailField.getText().trim();
        String senha = new String(passwordField.getPassword()).trim();

        try {
            User user = userDAO.findByEmail(email);
            if(user != null && senha.equals(user.getSenha())) {
                authenticated = true;
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ModelException me) {
            JOptionPane.showMessageDialog(this, "Erro ao acessar o banco: " + me.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openRegister() {
        RegisterView registerView = new RegisterView((JFrame) this.getParent());
        registerView.setVisible(true);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }
}
