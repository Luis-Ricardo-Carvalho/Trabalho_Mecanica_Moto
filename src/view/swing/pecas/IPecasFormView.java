package view.swing.pecas;

import model.Pecas;

public interface IPecasFormView {
    Pecas getPecaFromForm();
    void setPecaInForm(Pecas peca);
    void showInfoMessage(String msg);
    void showErrorMessage(String msg);
    void close();
}