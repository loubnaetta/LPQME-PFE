/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Test;
import bean.Cours;
import bean.Niveau;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Omaima
 */
@Stateless
public class NiveauFacade extends AbstractFacade<Niveau> {

    @PersistenceContext(unitName = "Web-PedagogiquePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public NiveauFacade() {
        super(Niveau.class);
    }
    public List<Niveau> all(Long id){
        Query q=em.createQuery("select n from Niveau n where n.matiere.id="+id);
        List<Niveau> list=(List<Niveau>)q.getResultList();
        return list;
    }

    public boolean findByNom(String nom) {
        Query q=em.createQuery("select n from Niveau n where n.nom='"+nom+"'");
      if(!q.getResultList().isEmpty())
          return true;
        return false;
    }
    
  
       public List<Cours> correction_niveau(Niveau niveau){
        Query q=em.createQuery("select f from FichierProf f where f.niveau.id="+niveau.getId()+" and f.type='correction'");
        List<Cours> cours=(List<Cours>)q.getResultList();
        if(cours!=null)
            return cours;
        else 
            return null;
    }
       
    public List<Test> examen_niveau(Niveau niveau){
        Query q=em.createQuery("select e from Examen e where e.niveau.id="+niveau.getId());
        List<Test> examen=(List<Test>)q.getResultList();
        if(examen!=null)
            return examen;
        else 
            return null;
    }
}
