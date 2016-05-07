package controller;

import bean.QuestionOuverte;
import controller.util.JsfUtil;
import service.QuestionOuverteFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@Named("questionOuverteController")
@SessionScoped
public class QuestionOuverteController implements Serializable {

    private QuestionOuverte current;
    @EJB
    private service.QuestionOuverteFacade ejbFacade;

    public QuestionOuverteController() {
    }

    public QuestionOuverte getSelected() {
        if (current == null) {
            current = new QuestionOuverte();
        }
        return current;
    }

    private QuestionOuverteFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        return "List";
    }

    public String prepareView(QuestionOuverte questionOuverte) {
        current = questionOuverte;
        return "View";
    }

    public String prepareCreate() {
        current = new QuestionOuverte();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("QuestionOuverteCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(QuestionOuverte questionOuverte) {
        current = questionOuverte;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("QuestionOuverteUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(QuestionOuverte questionOuverte) {
        current = questionOuverte;
        performDestroy();
        return "List";
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("QuestionOuverteDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public QuestionOuverte getQuestionOuverte(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = QuestionOuverte.class)
    public static class QuestionOuverteControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            QuestionOuverteController controller = (QuestionOuverteController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "questionOuverteController");
            return controller.getQuestionOuverte(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof QuestionOuverte) {
                QuestionOuverte o = (QuestionOuverte) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + QuestionOuverte.class.getName());
            }
        }

    }

}
