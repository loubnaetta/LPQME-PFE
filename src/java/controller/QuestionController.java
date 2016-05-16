package controller;

import bean.Question;
import bean.Reponse;
import bean.Test;
import controller.util.JsfUtil;
import service.QuestionFacade;

import java.io.Serializable;
import java.util.ArrayList;
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

@Named("questionController")
@SessionScoped
public class QuestionController implements Serializable {

    private Question current;
    
    private int nrb_choix;
    @EJB
    private service.QuestionFacade ejbFacade;
    @EJB
    private service.TestFacade testFacade;
     @EJB
     private service.ReponseFacade reponseFacade;

  
    private  Reponse reponse=new Reponse();
    
   public void ajouter_reponse(){
       current.getReponses().add(reponse);
       reponse=new Reponse();
   }
   
   public void supprimer_reponse(Reponse reponse){
       current.getReponses().remove(reponse);
   }
   
   
    public String reponse_question(){
        return "Create_reponse";
    }
   
    public Reponse getReponse() {
        return reponse;
    }

    public void setReponse(Reponse reponse) {
        this.reponse = reponse;
    }
   
    public QuestionController() {
    }

    public Question getCurrent() {
        return current;
    }

    public void setCurrent(Question current) {
        this.current = current;
    }

    
    public int getNrb_choix() {
        return nrb_choix;
    }

    public void setNrb_choix(int nrb_choix) {
        this.nrb_choix = nrb_choix;
    }
    

    public Question getSelected() {
        if (current == null) {
            current = new Question();
        }
        return current;
    }

    private QuestionFacade getFacade() {
        return ejbFacade;
    }

  
    public String prepareList() {
        return "List";
    }

    public String prepareView(Question questionQCM) {
        current = questionQCM;
       
        return "View";
    }

    public String prepareCreate() {
        current = new Question();
         
        return "Create";
    }

    public String create(Test test) {
        try {
             current.setTest(test);
              for(Reponse rep:current.getReponses()){
                //rep.setQuestion(current);
                reponseFacade.create(rep);
                //current.getReponses().add(rep);
            }
           
             getFacade().create(current);
             for(Reponse rep:current.getReponses()){
                rep.setQuestion(current);
                reponseFacade.edit(rep);
               
            }
             current.getTest().getQuestions().add(current);
             testFacade.edit(current.getTest());

             return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return "";
        }
    }

    public String prepareEdit(Question questionQCM) {
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

    public String destroy(Question question) {
        current = question;
        performDestroy();
        return "List";
    }

      
    private void performDestroy() {
        try {
            if(current.getTest()!=null){
             current.getTest().getQuestions().remove(current);
             testFacade.edit(current.getTest());
             current.setTest(null);
             ejbFacade.edit(current);
            }
             List<Reponse> reps=current.getReponses();
             for(Reponse rep:reps){
                 current.getReponses().remove(rep);
                 ejbFacade.edit(current);
                 rep.setQuestion(null);
                 reponseFacade.edit(rep);
                 reponseFacade.remove(rep);
             }
             
             if(current.getReponses().size()==0)
                 ejbFacade.remove(current);
             else
                 performDestroy();
             
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

    public Question getQuestionQCM(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Question.class)
    public static class QuestionQCMControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            QuestionController controller = (QuestionController) facesContext.getApplication().getELResolver().
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
            if (object instanceof Question) {
                Question o = (Question) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Question.class.getName());
            }
        }

    }

}
