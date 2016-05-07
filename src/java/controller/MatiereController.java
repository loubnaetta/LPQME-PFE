package controller;

import bean.Matiere;
import controller.util.JsfUtil;
import service.MatiereFacade;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@Named("matiereController")
@SessionScoped
public class MatiereController implements Serializable {

    private Matiere current;

    @EJB
    private service.MatiereFacade ejbFacade;

    /*************/
    public List<Matiere> all()throws SQLException{
        return ejbFacade.findAll();
    }
    

    /*************/
    
    
    

    public MatiereController() {
    }

    public Matiere getSelected() {
        if (current == null) {
            current = new Matiere();
           
        }
        return current;
    }

    private MatiereFacade getFacade() {
        return ejbFacade;
    }

  

    public String prepareList() {
   
        return "List";
    }

    public String prepareView(Matiere matiere) {
        current = matiere;
        
        return "View";
    }

    public String prepareCreate() {
        current = new Matiere();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MatiereCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Matiere matiere) {
        current = matiere;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MatiereUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Matiere matiere) {
        current = matiere;
        performDestroy();
   
        return "List";
    }

 
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("MatiereDeleted"));
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

    public Matiere getMatiere(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Matiere.class)
    public static class MatiereControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            MatiereController controller = (MatiereController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "matiereController");
            return controller.getMatiere(getKey(value));
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
            if (object instanceof Matiere) {
                Matiere o = (Matiere) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Matiere.class.getName());
            }
        }

    }

}
