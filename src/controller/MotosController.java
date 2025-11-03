package controller;

import java.util.List;
import model.Motos;
import model.Pecas;
import model.ModelException;
import model.data.DAOFactory;
import model.data.MotosDAO;
import view.swing.motos.IMotosFormView;
import view.swing.motos.IMotosListView;

public class MotosController {

    private final MotosDAO motoDAO = DAOFactory.createMotosDAO();
    private IMotosListView motoListView;
    private IMotosFormView motoFormView;

    public void loadMotos() {
        try {
            List<Motos> motos = motoDAO.findAll();
            motoListView.setMotoList(motos);
        } catch (ModelException e) {
            motoListView.showMessage("Erro ao carregar motos: " + e.getMessage());
        }
    }

    public void saveOrUpdate(boolean isNew) {
        Motos moto = motoFormView.getMotoFromForm();

        try {
            validateMoto(moto);
        } catch (IllegalArgumentException e) {
            motoFormView.showErrorMessage("Erro de validação: " + e.getMessage());
            return;
        }

        try {
            if (isNew) {
                motoDAO.save(moto);
            } else {
                motoDAO.update(moto);
            }

            motoFormView.showInfoMessage("Moto salva com sucesso!");
            motoFormView.close();

        } catch (ModelException e) {
            motoFormView.showErrorMessage("Erro ao salvar: " + e.getMessage());
        }
    }

    public void excluirMoto(Motos moto) {
        try {
            motoDAO.delete(moto);
            motoListView.showMessage("Moto excluída!");
            loadMotos();
        } catch (ModelException e) {
            motoListView.showMessage("Erro ao excluir: " + e.getMessage());
        }
    }

    private void validateMoto(Motos moto) {
        if (moto.getModelo() == null || moto.getModelo().trim().isEmpty()) {
            throw new IllegalArgumentException("O modelo da moto é obrigatório.");
        }
        if (moto.getMarca() == null || moto.getMarca().trim().isEmpty()) {
            throw new IllegalArgumentException("A marca da moto é obrigatória.");
        }
        if (moto.getDescricao() == null || moto.getDescricao().trim().isEmpty()) {
            throw new IllegalArgumentException("A descrição da moto é obrigatória.");
        }
        if (moto.getPeca() == null) {
            throw new IllegalArgumentException("A peça associada à moto é obrigatória.");
        }
    }

    public void setMotoFormView(IMotosFormView motoFormView) {
        this.motoFormView = motoFormView;
    }

    public void setMotoListView(IMotosListView motoListView) {
        this.motoListView = motoListView;
    }

    public List<Pecas> loadPecasList() {
        try {
            return DAOFactory.createPecasDAO().findAll();
        } catch (ModelException e) {
            if (motoFormView != null) {
                motoFormView.showErrorMessage("Erro ao carregar peças: " + e.getMessage());
            }
            return List.of();
        }
    }
}
