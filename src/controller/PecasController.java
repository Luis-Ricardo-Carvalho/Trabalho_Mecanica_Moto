package controller;

import java.util.List;
import model.Pecas;
import model.ModelException;
import model.data.DAOFactory;
import model.data.PecasDAO;
import view.swing.pecas.IPecasFormView;
import view.swing.pecas.IPecasListView;

public class PecasController {

    private final PecasDAO pecaDAO = DAOFactory.createPecasDAO();
    private IPecasListView pecaListView;
    private IPecasFormView pecaFormView;

    public void loadPecas() {
        try {
            List<Pecas> pecas = pecaDAO.findAll();
            pecaListView.setPecaList(pecas);
        } catch (ModelException e) {
            pecaListView.showMessage("Erro ao carregar peças: " + e.getMessage());
        }
    }

    public void saveOrUpdate(boolean isNew) {
        Pecas peca = pecaFormView.getPecaFromForm();

        try {
            validatePeca(peca);
        } catch (IllegalArgumentException e) {
            pecaFormView.showErrorMessage("Erro de validação: " + e.getMessage());
            return;
        }

        try {
            if (isNew) {
                pecaDAO.save(peca);
            } else {
                pecaDAO.update(peca);
            }

            pecaFormView.showInfoMessage("Peça salva com sucesso!");
            pecaFormView.close();

        } catch (ModelException e) {
            pecaFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluirPeca(Pecas peca) {
        try {
            pecaDAO.delete(peca);
            pecaListView.showMessage("Peça excluída!");
            loadPecas();
        } catch (ModelException e) {
            pecaListView.showMessage("Erro ao excluir: " + e.getMessage());
        }
    }

    private void validatePeca(Pecas peca) {
        if (peca.getName() == null || peca.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("O nome da peça é obrigatório.");
        }
        if (peca.getMarca() == null || peca.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca da peça é obrigatória.");
        }
        if (peca.getQuantidade() < 0) {
            throw new IllegalArgumentException("A quantidade não pode ser negativa.");
        }
        if (peca.getUser() == null) {
            throw new IllegalArgumentException("Usuário responsável pela peça não informado.");
        }
    }

    public void setPecaFormView(IPecasFormView pecaFormView) {
        this.pecaFormView = pecaFormView;
    }

    public void setPecaListView(IPecasListView pecaListView) {
        this.pecaListView = pecaListView;
    }
}
