package view.swing.motos;

import model.Motos;

public interface IMotosFormView {
    Motos getMotoFromForm();
    void setMotoInForm(Motos moto);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}
