package etu1979.framework.model;

import etu1979.framework.Annotation.URL;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import etu1979.framework.ModelView;
import etu1979.framework.FileUpload;

public class Emp {
    String nom;
    FileUpload profilPic;

    public Emp(FileUpload profilPic) {
        this.profilPic = profilPic;
    }

    public Emp(String nom) {
        this.nom = nom;
    }

    public Emp(String nom, FileUpload profilPic) {
        this.nom = nom;
        this.profilPic = profilPic;
    }

    @URL(value = "TestUpload")
    public ModelView pic() {
        return new ModelView("Upload.jsp");
    }

    @URL(value = "Hello")
    public ModelView great() {
        return new ModelView("Greating.jsp");
    }

    @URL(value = "Test", parameters = { "annee", "mois", "jour" })
    public ModelView calclAge(String annee, String mois, String jour) {
        ModelView result = new ModelView("Age.jsp");

        int anneeDeNaissance = Integer.valueOf(annee);
        int moisDeNaissance = Integer.valueOf(mois);
        int jourDeNaissance = Integer.valueOf(jour);

        LocalDate dateDeNaissance = LocalDate.of(anneeDeNaissance, moisDeNaissance, jourDeNaissance);
        LocalDate actual = LocalDate.of(2023, 07, 01);

        long yearsDiff = ChronoUnit.YEARS.between(dateDeNaissance, actual);
        result.addItem("difference", String.valueOf(yearsDiff));

        return result;
    }

    public FileUpload getProfilPic() {
        return profilPic;
    }

    public void setProfilPic(FileUpload profilPic) {
        this.profilPic = profilPic;
    }

    public Emp() {
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    // public int getAge() {
    //     return age;
    // }

    // public void setAge(int age) {
    //     this.age = age;
    // }

    // public void setAge(String age) {
    //     int toBeUsed = Integer.valueOf(age);
    //     this.age = toBeUsed;
    // }
}
