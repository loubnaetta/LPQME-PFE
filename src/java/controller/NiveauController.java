package controller;

import bean.Test;
import bean.Cours;
import bean.FichierEleve;
import bean.Matiere;
import bean.Niveau;
import bean.Question;
import bean.Reponse;
import controller.util.JsfUtil;
import service.NiveauFacade;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
import org.primefaces.model.UploadedFile;

@Named("niveauController")
@SessionScoped
public class NiveauController implements Serializable {

    private Niveau current;
    @EJB
    private service.NiveauFacade ejbFacade;
    @EJB
    private service.CoursFacade coursFacade;
    @EJB
    private service.TestFacade testFacade;
    @EJB
    private service.QuestionFacade questionFacade;
    @EJB
    private service.ReponseFacade reponseFacade;
    private CoursController coursController =new  CoursController();
    private MatiereController matiereController= new MatiereController() ;
/****************/
    
    
    private UploadedFile  fichier_cours;
  
    public UploadedFile  getFichier_cours() {
     
        return fichier_cours;
    }
    
    public void setFichier_cours(UploadedFile  fichier_cours) {
        this.fichier_cours=fichier_cours;
    }
    
    
      public List<Niveau> all(Matiere matiere){
       List<Niveau> list=new ArrayList<>();
           list=ejbFacade.all(matiere.getId());
 
       return list;
   }   
     
      public String Ajouter_cours() {
          if(fichier_cours.getSize()!=0){
                Cours cours= new Cours();
                cours.setChemin(fichier_cours.getFileName());
               current.getCours().add(cours);
                 
                 return "";
                  }
      else
         return "";
             
      }
      public List<Cours> cours(Niveau niveau){
           return niveau.getCours();
      }
      public String cours_disponible(Niveau niveau){

          if(niveau.getCours().isEmpty())
              return "non disponible";
          else
              return ""+niveau.getCours().size();
      }
      public List<Test> serie(Niveau niveau){
            List<Test> serie=new ArrayList<>();
          for(Test s:niveau.getTests()){
              if(s.getType().equals("serie") && s.isEtat()==true)
              serie.add(s);
                  }
          return serie;
      }
      public String serie_disponible(Niveau niveau){
            List<Test> serie=new ArrayList<>();
          for(Test s:niveau.getTests()){
              if(s.getType().equals("serie"))
              serie.add(s);
                  }
          if(serie.isEmpty())
              return "non disponible";
          else
              return ""+serie.size();
      }
      
      public Test examen(Niveau niveau){
          for(Test t:niveau.getTests()){
              if(t.getType().equals("examen") && t.isEtat()==true)
                  return t;
          }
              
          return null;
      }
      public String prochain_examen(Niveau niveau){
  
          if(examen(niveau)!=null){
              
              Date d=examen(niveau).getDate_examen();
              
              if(d.after(new Date())){ 
                  return d.getDay()+"/"+d.getMonth()+"/"+(d.getYear()+1900)+" à "+d.getHours()+":"+d.getMinutes();
              }
              else{
                  return "non disponible";}
          }
          
          else 
              return "non disponible";

      }
      
         public String prepareList_test(Niveau niveau, String type) {
         current=niveau;
        if(type.equals("serie"))
            return "/test/List_serie";
        else
           return "List_examen";
    }

    /**************/
      
   public Niveau getCurrent() {
        return current;
    }

    public void setCurrent(Niveau current) {
        this.current = current;
    }

    public NiveauFacade getEjbFacade() {
        return ejbFacade;
    }

    public void setEjbFacade(NiveauFacade ejbFacade) {
        this.ejbFacade = ejbFacade;
    }
    
    public NiveauController() {
 
    

    }

    public Niveau getSelected() {
        if (current == null) {
            current = new Niveau();
           
        }
        return current;
    }

    private NiveauFacade getFacade() {
        return ejbFacade;
    }

  

    public String prepareList() { 
        return "/niveau/List";
    }

    public String prepareView(Niveau niveau) {
        current =niveau;
        return "View";
    }

    public String prepareCreate() {
        current = new Niveau();
        return "Create";
    }

    public String create(Matiere matiere) {
        try {
            
            if( ejbFacade.findByNom(current.getNom())==false && !current.getNom().equals("")){
              
              current.setMatiere(matiere);
              current.setEtat(true);
              getFacade().create(current); 
            JsfUtil.addSuccessMessage("L'opération est s'effectuée avec succès");
            
            if(fichier_cours.getSize()!=0){
                Cours cours=new Cours();
                cours.setChemin(fichier_cours.getFileName());
              
                cours.setNiveau(current);
                coursController.Create(cours);
               current.getCours().add(cours);
                 update();
            }
              return prepareList();
            }
            else{
                JsfUtil.addErrorMessage("le nom de niveau déja existe");
                return prepareCreate();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage("erreur de création de niveau");
            return null;
        }
    }

    public String prepareEdit(Niveau niveau) {
        current = niveau;
        return "Edit";
    }
   public void destroy_cours(Cours cours){
       current.getCours().remove(cours);
       
   }
   public void destroy_test(Test test){
       current.getTests().remove(test);
       
   }

    public String update() {
        try {

            // creation des nouveaux cours ajouter
            for(Cours c :current.getCours()){
                if(c.getNiveau()==null){
                       c.setNiveau(current);
                    coursController.Create(c);
                }
            }
            getFacade().edit(current);
               
               // remove test(examen, serie)
           List<Test> list_test=testFacade.test_niveau(current);
            System.out.println(" 1");
                        if(list_test!=null){
                             System.out.println(" 12");
                          for(Test t:list_test){
                               System.out.println(" 13");
                              if(t.getType().equals("serie")){
                                   System.out.println(" 14");
                              if(current.getTests().contains(t)==false){
                                   System.out.println(" 15");
                                   System.out.println(" 22");
                                   t.setEtat(false);
                                  testFacade.edit(t);
                              }
                              }
                          }
                        }
               //pour effacer carrément le cours que ne fait plus partie de niveau 
            List<Cours> list_cours=coursFacade.cours_niveau(current);
            if(list_cours!=null && list_cours.size()!=current.getCours().size()){
                System.out.println(" size :"+list_cours.size()+" , "+current.getCours().size());
                   System.out.println("4");
           for(Cours c:list_cours){
                
                if(current.getCours().contains(c)==false){
                    coursFacade.remove(c);
                }
            }
            }
            return "List";
        } catch (Exception e) {
            return null;
        }
    }

    public String destroy(Niveau niveau) {
        current = niveau;
        performDestroy();
        return "List";
    }


    private void performDestroy() {
        try {
            if(current.getEleves().isEmpty()){
                current.setEtat(false);
                ejbFacade.edit(current);
            }

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

    public Niveau getNiveau(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Niveau.class)
    public static class NiveauControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            NiveauController controller = (NiveauController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "niveauController");
            return controller.getNiveau(getKey(value));
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
            if (object instanceof Niveau) {
                Niveau o = (Niveau) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Niveau.class.getName());
            }
        }

    }

}
