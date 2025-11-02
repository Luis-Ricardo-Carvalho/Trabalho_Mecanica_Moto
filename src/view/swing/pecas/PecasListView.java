package view.swing.pecas;

import controller.PecasController;
import model.Pecas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class PecasListView extends JDialog implements IPecasListView {
    private PecasController controller;
    private final PecaTableModel tableModel = new PecaTableModel();
    private final JTable table = new JTable(tableModel);

    public PecasListView(JFrame parent) {
        super(parent, "Peças", true);
        this.controller = new PecasController();
        this.controller.setPecaListView(this);

        setSize(750, 420);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Adicionar Peça");
        addButton.addActionListener(e -> {
            PecasFormView form = new PecasFormView(this, null, controller);
            form.setVisible(true);
        });

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem editItem = new JMenuItem("Editar");
        JMenuItem deleteItem = new JMenuItem("Excluir");
        popupMenu.add(editItem);
        popupMenu.add(deleteItem);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) { showPopup(e); }
            @Override
            public void mouseReleased(MouseEvent e) { showPopup(e); }
            private void showPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0 && row < table.getRowCount()) {
                        table.setRowSelectionInterval(row, row);
                        popupMenu.show(table, e.getX(), e.getY());
                    }
                }
            }
        });

        editItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Pecas peca = tableModel.getPecaAt(row);
                PecasFormView form = new PecasFormView(this, peca, controller);
                form.setVisible(true);
            }
        });

        deleteItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Pecas peca = tableModel.getPecaAt(row);
                int confirm = JOptionPane.showConfirmDialog(this, 
                        "Excluir peça?", 
                        "Confirmação", 
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.excluirPeca(peca);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        controller.loadPecas();
    }

    @Override
    public void setPecaList(List<Pecas> pecas) {
        tableModel.setPecas(pecas);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadPecas();
    }

    static class PecaTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Nome", "Marca", "Categoria", "Quantidade", "Usuário"};
        private List<Pecas> pecas = new ArrayList<>();

        public void setPecas(List<Pecas> pecas) {
            this.pecas = pecas;
            fireTableDataChanged();
        }

        public Pecas getPecaAt(int row) {
            return pecas.get(row);
        }

        @Override
        public int getRowCount() {
            return pecas.size();
        }

        @Override
        public int getColumnCount() {
            return columns.length;
        }

        @Override
        public String getColumnName(int col) {
            return columns[col];
        }

        @Override
        public Object getValueAt(int row, int col) {
            Pecas p = pecas.get(row);
            switch (col) {
                case 0: return p.getId();
                case 1: return p.getName();
                case 2: return p.getMarca();
                case 3: return p.getCategoria();
                case 4: return p.getQuantidade();
                case 5: return (p.getUser() != null ? p.getUser().getName() : "—");
                default: return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}
