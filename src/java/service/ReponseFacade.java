/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import bean.Reponse;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Omaima
 */
@Stateless
public class ReponseFacade extends AbstractFacade<Reponse> {

    @PersistenceContext(unitName = "Web-PedagogiquePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ReponseFacade() {
        super(Reponse.class);
    }
    
}
