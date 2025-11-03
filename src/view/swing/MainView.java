package view.swing;

import view.swing.motos.MotosListView;
import view.swing.pecas.PecasListView;
import view.swing.user.UserListView;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainView extends JFrame {
    private static final long serialVersionUID = 1L;

    public MainView() {
        setTitle("Sistema CRUD - Swing");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JMenuBar menuBar = new JMenuBar();

        JMenu menu = new JMenu("Usuários");
        JMenuItem userListItem = new JMenuItem("Listar Usuários");
        userListItem.addActionListener(e -> new UserListView(this).setVisible(true));
        menu.add(userListItem);
        menuBar.add(menu);

        JMenu motoMenu = new JMenu("Motos");
        JMenuItem motoListItem = new JMenuItem("Listar Motos");
        motoListItem.addActionListener(e -> new MotosListView(this).setVisible(true));
        motoMenu.add(motoListItem);
        menuBar.add(motoMenu);

        JMenu pecasMenu = new JMenu("Peças");
        JMenuItem pecasListItem = new JMenuItem("Listar Peças");
        pecasListItem.addActionListener(e -> new PecasListView(this).setVisible(true));
        pecasMenu.add(pecasListItem);
        menuBar.add(pecasMenu);

        menuBar.add(Box.createHorizontalGlue());

        JMenu menuSair = new JMenu("...");
        JMenuItem sairItem = new JMenuItem("Fechar o sistema");
        sairItem.addActionListener(e -> System.exit(0));
        menuSair.add(sairItem);
        menuBar.add(menuSair);

        setJMenuBar(menuBar);

        JLabel label = new JLabel("Seja bem-vindo!", SwingConstants.CENTER);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(32, 32, 32, 32));
        contentPanel.add(label, BorderLayout.CENTER);

        setContentPane(contentPanel);
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            MainView dummyMain = new MainView(); 
            
            LoginView login = new LoginView(dummyMain);
            login.setVisible(true);

            if (login.isAuthenticated()) {
                MainView mainView = new MainView();
                mainView.setVisible(true);
                mainView.setExtendedState(JFrame.MAXIMIZED_BOTH);
            } else {
                System.exit(0);
            }
        });
    }
}
