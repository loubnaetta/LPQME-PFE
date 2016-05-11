/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Matiere;
import bean.Personne;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author Omaima
 */
@Stateless
public class MatiereFacade extends AbstractFacade<Matiere> {

    @PersistenceContext(unitName = "Web-PedagogiquePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MatiereFacade() {
        super(Matiere.class);
    }
    
    public Matiere findByProf(Personne prof){
        Query q= em.createQuery("select m from Matiere m where m.professeur.id="+prof.getId());
        Matiere m=(Matiere)q.getResultList().get(0);
        if(m!=null)
            return  m;
                    else return null;
    }
}
