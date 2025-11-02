package view.swing.motos;

import java.util.List;
import model.Motos;

public interface IMotosListView {
    void setMotoList(List<Motos> motos);
    void showMessage(String msg);
}
