package controller;

import bean.Cours;
import bean.Niveau;
import bean.Personne;
import controller.util.JsfUtil;
import service.CoursFacade;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.SelectItem;

@Named("coursController")
@SessionScoped
public class CoursController implements Serializable {

    private Cours current;
    @EJB
    private service.CoursFacade ejbFacade;
    
    private NiveauController niveauController;
  

    public Cours getCurrent() {
        return current;
    }

    public void setCurrent(Cours current) {
        this.current = current;
    }
  
    
    public CoursController() {
    }

    public Cours getSelected() {
        if (current == null) {
            current = new Cours();
        }
        return current;
    }

    private CoursFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        return "List";
    }

    public String prepareView(Cours fichierProf) {
        current = fichierProf;
        return "View";
    }

    public String prepareCreate() {
        current = new Cours();
        return "Create";
    }

    public void Create(Cours fp){
        System.out.println("create FP");
        current=fp;
        System.out.println(" chemin : "+current.getChemin());
         create();
    }
    
    public void create() {
        try {
            System.out.println(" create cours");
            getFacade().create(current);
            System.out.println(" cours crée");
            JsfUtil.addSuccessMessage("fichier crée");
           
        } catch (Exception e) {
          
          
        }
    }

    public String prepareEdit(Cours fichierProf) {
        current = fichierProf;
        return "Edit";
    }

    public void update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage("cours est modifier");
      
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            
        }
    }

    public void destroy(Cours cours) {
        current = cours;
        performDestroy();
        
    }

    private void performDestroy() {
        try {
            System.out.println(" cours à detruire : "+current.getChemin());
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("FichierProfDeleted"));
        } catch (Exception e) {
            System.out.println("erreur");
        }
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Cours getFichierProf(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Cours.class)
    public static class FichierProfControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CoursController controller = (CoursController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "fichierProfController");
            return controller.getFichierProf(getKey(value));
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
            if (object instanceof Cours) {
                Cours o = (Cours) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cours.class.getName());
            }
        }

    }

}
