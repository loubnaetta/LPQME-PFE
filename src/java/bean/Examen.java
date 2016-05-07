/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

/**
 *
 * @author loubna
 */
@Entity
public class Examen implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date date;
    private String type;
    private int temps;
    @OneToMany
    private List<QuestionQCM> questionQCMs=new ArrayList<>();
     @OneToMany
    private List<QuestionOuverte> questionOuvertes=new ArrayList<>();
    @OneToMany
    private List<FichierEtud> fichiers_Etudiant=new ArrayList<>();
    @ManyToOne
    private Niveau niveau;
    @ManyToOne
    private Personne personne;

    public Personne getPersonne() {
        return personne;
    }

    public void setPersonne(Personne personne) {
        this.personne = personne;
    }
    
    
    
    

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTemps() {
        return temps;
    }

    public void setTemps(int temps) {
        this.temps = temps;
    }

    public List<QuestionQCM> getQuestionQCMs() {
        return questionQCMs;
    }

    public void setQuestionQCMs(List<QuestionQCM> questionQCMs) {
        this.questionQCMs = questionQCMs;
    }

    public List<QuestionOuverte> getQuestionOuvertes() {
        return questionOuvertes;
    }

    public void setQuestionOuvertes(List<QuestionOuverte> questionOuvertes) {
        this.questionOuvertes = questionOuvertes;
    }

    public List<FichierEtud> getFichiers_Etudiant() {
        return fichiers_Etudiant;
    }

    public void setFichiers_Etudiant(List<FichierEtud> fichiers_Etudiant) {
        this.fichiers_Etudiant = fichiers_Etudiant;
    }

    public Niveau getNiveau() {
        return niveau;
    }

    public void setNiveau(Niveau niveau) {
        this.niveau = niveau;
    }
    
    
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Examen)) {
            return false;
        }
        Examen other = (Examen) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Examen[ id=" + id + " ]";
    }
    
}
