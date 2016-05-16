/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Cours;
import bean.Niveau;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author loubna
 */
@Stateless
public class CoursFacade extends AbstractFacade<Cours> {

    @PersistenceContext(unitName = "Web-PedagogiquePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CoursFacade() {
        super(Cours.class);
    }
     
    public List<Cours> cours_niveau(Niveau niveau){
        Query q=em.createQuery("select c from Cours c where c.niveau.id="+niveau.getId());
        List<Cours> lista= (List<Cours>)q.getResultList();
        if(lista!=null)
            return lista;
        else return null;
    }
    

}
