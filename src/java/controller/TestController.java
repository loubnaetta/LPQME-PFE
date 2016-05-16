package controller;

import bean.Niveau;
import bean.Test;
import controller.util.JsfUtil;
import service.TestFacade;

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

@Named("testController")
@SessionScoped
public class TestController implements Serializable {

    private Test current;
    @EJB
    private service.TestFacade ejbFacade;
    @EJB
    private service.NiveauFacade niveauFacade;
    
    private NiveauController  niveauController;
    
    /**********************/
    public String title_create(){
        if(current.getType().equals("serie"))
            return "Nouvelle Série";
        else
            return "Nouveau Examen";
    }
    
  
 
    /**********************/

    public TestController() {
    }

    public Test getSelected() {
        if (current == null) {
            current = new Test();
            
        }
        return current;
    }

    private TestFacade getFacade() {
        return ejbFacade;
    }

    public String prepareList(Niveau niveau, String type) {
       
        if(type.equals("serie"))
            return "List_serie";
        else
           return "List_examen";
    }

    public String prepareView(Test examen) {
        current = examen;
       
        return "View";
    }

    public String prepareCreate(Niveau niveau,String type) {
        current = new Test();
        current.setType(type);
        current.setNiveau(niveau);
        return "Create";
    }

    public String create() {
        try {
            current.setEtat(true);
            getFacade().create(current);
            current.getNiveau().getTests().add(current);
            niveauFacade.edit(current.getNiveau());
            return "/question/List";
        
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
          return "";
        }
    }

    public String prepareEdit(Test examen) {
       
        current = examen;
        
        return "/question/List";
    }

    public void  update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ExamenUpdated"));
   
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            
        }
    }

    public String destroy(Test examen) {
        current = examen;
        performDestroy();
        return "List";
    }

    private void performDestroy() {
        try {
            current.setEtat(false);
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("ExamenDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    public Test getCurrent() {
        return current;
    }

    public void setCurrent(Test current) {
        this.current = current;
    }
     
    

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Test getExamen(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Test.class)
    public static class ExamenControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TestController controller = (TestController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "examenController");
            return controller.getExamen(getKey(value));
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
            if (object instanceof Test) {
                Test o = (Test) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Test.class.getName());
            }
        }

    }

}
