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
public class Personne implements Serializable  {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
     private String nom;
    private String prenom;
    private String email;
    private String pwd;
    private String etablissement;
    private Date derniere_connexion=new Date();
    private boolean etat=false;
    private String role;
    /****    attribut pour l'etudiant  **/
    @ManyToMany
    private List<Niveau> niveaux=new ArrayList<>();
    @OneToMany
    private List<FichierEtud> fichiersEtuds=new ArrayList<>();
   /****    attribut pour Professeur  **/
      @ManyToOne
    private Matiere matiere;
    @OneToMany
    private List<FichierProf> fichiersProfs=new ArrayList<>();
     @OneToMany
    private List<Serie> series=new ArrayList<>();
      @OneToMany
     private List<Examen> examens=new ArrayList<>();

    public boolean isEtat() {
        return etat;
    }

    public void setEtat(boolean etat) {
        this.etat = etat;
    }

    public List<Niveau> getNiveaux() {
        return niveaux;
    }

    public void setNiveaux(List<Niveau> niveaux) {
        this.niveaux = niveaux;
    }

    public List<Examen> getExamens() {
        return examens;
    }

    public void setExamens(List<Examen> examens) {
        this.examens = examens;
    }

      
      
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<FichierEtud> getFichiersEtuds() {
        return fichiersEtuds;
    }

    public void setFichiersEtuds(List<FichierEtud> fichiersEtuds) {
        this.fichiersEtuds = fichiersEtuds;
    }

    public Matiere getMatiere() {
        return matiere;
    }

    public void setMatiere(Matiere matiere) {
        this.matiere = matiere;
    }

    public List<FichierProf> getFichiersProfs() {
        return fichiersProfs;
    }

    public void setFichiersProfs(List<FichierProf> fichiersProfs) {
        this.fichiersProfs = fichiersProfs;
    }

    public List<Serie> getSeries() {
        return series;
    }

    public void setSeries(List<Serie> series) {
        this.series = series;
    }
/***************/
    
    
    public Long getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getEtablissement() {
        return etablissement;
    }

    public void setEtablissement(String etablissement) {
        this.etablissement = etablissement;
    }

    public Date getDerniere_connexion() {
        return derniere_connexion;
    }

    public void setDerniere_connexion(Date derniere_connexion) {
        this.derniere_connexion = derniere_connexion;
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
        if (!(object instanceof Personne)) {
            return false;
        }
        Personne other = (Personne) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "bean.Personne[ id=" + id + " ]";
    }
    
}
