package controller;

import bean.FichierProf;
import bean.Matiere;
import bean.Niveau;
import controller.util.JsfUtil;
import service.NiveauFacade;

import java.io.Serializable;
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
    private FichierProfController fichierProfController =new  FichierProfController();
    
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
      public List<FichierProf> documentation(Niveau niveau){
          List<FichierProf> doc=new ArrayList<>();
          for(FichierProf fichier:niveau.getFichiers_Prof()){
          if(fichier.getType().equals("cours"))
              doc.add(fichier);
                  }
          return doc;
      }
      
      public String prochain_examen(Niveau niveau){
          
          System.out.println("  --"+niveau.getExamens().size());
          if(niveau.getExamens().size()>0){
              System.out.println(" A");
              Date d=niveau.getExamens().get(niveau.getExamens().size()-1).getDate();
              
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
                 System.out.println("1.2");
              getFacade().create(current);
            JsfUtil.addSuccessMessage("L'opération est s'effectuée avec succès");
           // JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NiveauCreated"));
            if(fichier_cours.getSize()!=0){
                System.out.println("2");
                FichierProf fichierProf=new FichierProf();
                System.out.println("3");
                fichierProf.setType("cours");
                System.out.println("4");
                fichierProf.setChemin(fichier_cours.getFileName());
                System.out.println("5");
                fichierProf.setProfesseur(current.getMatiere().getProfesseur());
                System.out.println("6");
                fichierProf.setNiveau(current);
                System.out.println("7");
                fichierProfController.Create(fichierProf);
                System.out.println("8");
               current.getFichiers_Prof().add(fichierProf);

                System.out.println("9");
           
                 update();
            }
              return prepareList();
            }
            else{
                JsfUtil.addErrorMessage("le nom de niveau déja existe");
                return prepareCreate();
            }
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NiveauUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
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
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("NiveauDeleted"));
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
