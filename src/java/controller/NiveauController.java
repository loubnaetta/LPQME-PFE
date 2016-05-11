package controller;

import bean.Test;
import bean.Cours;
import bean.FichierEleve;
import bean.Matiere;
import bean.Niveau;
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
       List<Niveau> list=ejbFacade.all(matiere.getId());
 
       return list;
   }   
     
      public String Ajouter_cours() {
          if(fichier_cours.getSize()!=0){
                Cours cours= new Cours();
                cours.setChemin(fichier_cours.getFileName());
               
                cours.setNiveau(current);
                coursController.Create(cours);
               current.getCours().add(cours);
              
                 update();
                 
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
              if(s.getType().equals("serie"))
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
              if(t.getType().equals("examen"))
                  return t;
          }
              
          return null;
      }
      public String prochain_examen(Niveau niveau){
          
        
          if(examen(niveau)!=null){
              
              Date d=niveau.getTests().get(niveau.getTests().size()-1).getDate_examen();
              
              if(d.after(new Date())){
                  System.out.println(" date exam "+d.toString());
                  return d.getDay()+"/"+d.getMonth()+"/"+(d.getYear()+1900)+" à "+d.getHours()+":"+d.getMinutes();
              }
              else{
                  System.out.println(" AA");
                  return "non disponible";}
          }
          
          else 
              return "non disponible";

      }


    
   public List<Test> examen_niveau(){
       return ejbFacade.examen_niveau(current);
   }
   
   public List<Cours> correction_niveau(){
       return ejbFacade.correction_niveau(current);
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
        return "List";
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

    public String update() {
        try {
            getFacade().edit(current);
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
                
            for(Cours c:current.getCours()){
                current.getCours().remove(c);
                coursController.destroy(c);
            }
            
            for(Test t:current.getTests()){
                
                for(FichierEleve fe:t.getFichiers_Eleves()){
                    
                }
            }
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NiveauDeleted"));
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
