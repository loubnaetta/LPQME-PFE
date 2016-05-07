package controller;

import bean.QuestionQCM;
import controller.util.JsfUtil;
import service.QuestionQCMFacade;

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

@Named("questionQCMController")
@SessionScoped
public class QuestionQCMController implements Serializable {

    private QuestionQCM current;
    @EJB
    private service.QuestionQCMFacade ejbFacade;


    public QuestionQCMController() {
    }

    public QuestionQCM getSelected() {
        if (current == null) {
            current = new QuestionQCM();
        }
        return current;
    }

    private QuestionQCMFacade getFacade() {
        return ejbFacade;
    }

  
    public String prepareList() {
        return "List";
    }

    public String prepareView(QuestionQCM questionQCM) {
        current = questionQCM;
       
        return "View";
    }

    public String prepareCreate() {
        current = new QuestionQCM();
      
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("QuestionQCMCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(QuestionQCM questionQCM) {
        current = questionQCM;
   
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("QuestionQCMUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(QuestionQCM questionQCM) {
        current = questionQCM;
       
        performDestroy();
        return "List";
    }


    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("QuestionQCMDeleted"));
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

    public QuestionQCM getQuestionQCM(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = QuestionQCM.class)
    public static class QuestionQCMControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            QuestionQCMController controller = (QuestionQCMController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "questionQCMController");
            return controller.getQuestionQCM(getKey(value));
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
            if (object instanceof QuestionQCM) {
                QuestionQCM o = (QuestionQCM) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + QuestionQCM.class.getName());
            }
        }

    }

}
