package controller;

import bean.Personne;
import controller.util.JsfUtil;

import service.PersonneFacade;

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


@Named("personneController")
@SessionScoped
public class PersonneController implements Serializable {


    private Personne current;
    private NiveauController niveauController=new NiveauController();
    @EJB 
    private service.PersonneFacade ejbFacade;

    /*********/
    String email,pwd;
      public String connexion()throws SQLException{
          
          if(ejbFacade.connexion(email, pwd)!=null){
              current=ejbFacade.connexion(email, pwd);
              if(current.getRole().equals("professeur")){
                  System.out.println(" je ss prof");
                  return "list-niveau";
              }
              else 
                  return "";
          }
       else       
       return "";
      }

      
    public NiveauController getNiveauController() {
        return niveauController;
    }

    /********/
    public void setNiveauController(NiveauController niveauController) {
        this.niveauController = niveauController;
    }

    public Personne getCurrent() {
        return current;
    }

    public void setCurrent(Personne current) {
        this.current = current;
    }

    public PersonneFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(PersonneFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }


    public PersonneController() {
    }

    public Personne getSelected() {
        if (current == null) {
            current = new Personne();
            
        }
        return current;
    }

    private PersonneFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList() {
        return "List";
    }

    public String prepareView(Personne personne) {
        current = personne;
        return "View";
    }

    public String prepareCreate() {
        current = new Personne();
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonneCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit(Personne personne) {
        current = personne;
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonneUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy(Personne personne) {
        current = personne;
        performDestroy();
        return "List";
    }

    
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("PersonneDeleted"));
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

    public Personne getPersonne(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass=Personne.class)
    public static class PersonneControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            PersonneController controller = (PersonneController)facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "personneController");
            return controller.getPersonne(getKey(value));
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
            if (object instanceof Personne) {
                Personne o = (Personne) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: "+Personne.class.getName());
            }
        }

    }

}
