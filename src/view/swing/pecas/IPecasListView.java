package view.swing.pecas;

import java.util.List;
import model.Pecas;

public interface IPecasListView {
    void setPecaList(List<Pecas> pecas);
    void showMessage(String msg);
}