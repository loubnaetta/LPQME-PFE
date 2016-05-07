package controller;

import bean.FichierEtud;
import controller.util.JsfUtil;
import service.FichierEtudFacade;

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

@Named("fichierEtudController")
@SessionScoped
public class FichierEtudController implements Serializable {

    private FichierEtud current;
    @EJB
    private service.FichierEtudFacade ejbFacade;
  

    public FichierEtudController() {
    }

    public FichierEtud getSelected() {
        if (current == null) {
            current = new FichierEtud();
        }
        return current;
    }

    private FichierEtudFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        return "List";
    }

    public String prepareView(FichierEtud fichierEtud) {
        current = fichierEtud;
        return "View";
    }

    public String prepareCreate() {
        current = new FichierEtud();
      
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FichierEtudCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(FichierEtud fichierEtud) {
        current = fichierEtud;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FichierEtudUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(FichierEtud fichierEtud) {
        current = fichierEtud;
        performDestroy();
        return "List";
    }


    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FichierEtudDeleted"));
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

    public FichierEtud getFichierEtud(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = FichierEtud.class)
    public static class FichierEtudControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            FichierEtudController controller = (FichierEtudController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fichierEtudController");
            return controller.getFichierEtud(getKey(value));
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
            if (object instanceof FichierEtud) {
                FichierEtud o = (FichierEtud) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + FichierEtud.class.getName());
            }
        }

    }

}
