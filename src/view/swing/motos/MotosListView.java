package view.swing.motos;

import controller.MotosController;
import model.Motos;
import model.Pecas;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class MotosListView extends JDialog implements IMotosListView {
    private MotosController controller;
    private final MotoTableModel tableModel = new MotoTableModel();
    private final JTable table = new JTable(tableModel);

    public MotosListView(JFrame parent) {
        super(parent, "Motos", true);
        this.controller = new MotosController();
        this.controller.setMotoListView(this);

        setSize(800, 450);
        setLocationRelativeTo(null);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setRowHeight(36);
        table.setShowGrid(true);
        table.setGridColor(Color.LIGHT_GRAY);

        JButton addButton = new JButton("Adicionar Moto");
        addButton.addActionListener(e -> {
            List<Pecas> pecasList = controller.loadPecasList(); // carregar peças disponíveis
            MotosFormView form = new MotosFormView(this, null, controller, pecasList);
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
                Motos moto = tableModel.getMotoAt(row);
                List<Pecas> pecasList = controller.loadPecasList();
                MotosFormView form = new MotosFormView(this, moto, controller, pecasList);
                form.setVisible(true);
            }
        });

        deleteItem.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                Motos moto = tableModel.getMotoAt(row);
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Excluir moto?",
                        "Confirmação",
                        JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    controller.excluirMoto(moto);
                }
            }
        });

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(addButton, BorderLayout.EAST);

        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        controller.loadMotos();
    }

    @Override
    public void setMotoList(List<Motos> motos) {
        tableModel.setMotos(motos);
    }

    @Override
    public void showMessage(String msg) {
        JOptionPane.showMessageDialog(this, msg);
    }

    public void refresh() {
        controller.loadMotos();
    }

    static class MotoTableModel extends AbstractTableModel {
        private final String[] columns = {"ID", "Modelo", "Marca", "Descrição", "Peça Associada"};
        private List<Motos> motos = new ArrayList<>();

        public void setMotos(List<Motos> motos) {
            this.motos = motos;
            fireTableDataChanged();
        }

        public Motos getMotoAt(int row) {
            return motos.get(row);
        }

        @Override
        public int getRowCount() {
            return motos.size();
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
            Motos m = motos.get(row);
            switch (col) {
                case 0: return m.getId();
                case 1: return m.getModelo();
                case 2: return m.getMarca();
                case 3: return m.getDescricao();
                case 4: return (m.getPeca() != null ? m.getPeca().getName() : "—");
                default: return null;
            }
        }

        @Override
        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }
}
