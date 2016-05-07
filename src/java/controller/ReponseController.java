package controller;

import bean.Reponse;
import controller.util.JsfUtil;
import service.ReponseFacade;

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

@Named("reponseController")
@SessionScoped
public class ReponseController implements Serializable {

    private Reponse current;
 
    @EJB
    private service.ReponseFacade ejbFacade;
    
    public ReponseController() {
    }

    public Reponse getSelected() {
        if (current == null) {
            current = new Reponse();
            
        }
        return current;
    }

    private ReponseFacade getFacade() {
        return ejbFacade;
    }


    public String prepareList() {
        
        return "List";
    }

    public String prepareView(Reponse reponse) {
        current = reponse;
        return "View";
    }

    public String prepareCreate() {
        current = new Reponse();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReponseCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Reponse reponse) {
        current = reponse;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReponseUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Reponse reponse) {
        current = reponse;
        performDestroy();
        return "List";
    }


    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ReponseDeleted"));
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

    public Reponse getReponse(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Reponse.class)
    public static class ReponseControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            ReponseController controller = (ReponseController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "reponseController");
            return controller.getReponse(getKey(value));
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
            if (object instanceof Reponse) {
                Reponse o = (Reponse) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Reponse.class.getName());
            }
        }

    }

}
