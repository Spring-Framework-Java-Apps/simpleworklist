package org.woehlke.beachbox.entities;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Created by Fert on 27.03.2014.
 */
@Entity
public class Vinyl implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Rubrik rubrik;

    @Enumerated(EnumType.STRING)
    private Tontraeger tontraeger;

    @Column
    private String interpret;

    @Column
    private String song;

    @Column
    private String name;

    @Column
    private String seite;

    @Column
    private String jahr;

    @Column
    private String genre;

    @Column
    private String label;

    @Column
    private String bemerkung;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Rubrik getRubrik() {
        return rubrik;
    }

    public void setRubrik(Rubrik rubrik) {
        this.rubrik = rubrik;
    }

    public Tontraeger getTontraeger() {
        return tontraeger;
    }

    public void setTontraeger(Tontraeger tontraeger) {
        this.tontraeger = tontraeger;
    }

    public String getInterpret() {
        return interpret;
    }

    public void setInterpret(String interpret) {
        this.interpret = interpret;
    }

    public String getSong() {
        return song;
    }

    public void setSong(String song) {
        this.song = song;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeite() {
        return seite;
    }

    public void setSeite(String seite) {
        this.seite = seite;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(String jahr) {
        this.jahr = jahr;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBemerkung() {
        return bemerkung;
    }

    public void setBemerkung(String bemerkung) {
        this.bemerkung = bemerkung;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Vinyl vinyl = (Vinyl) o;

        if (bemerkung != null ? !bemerkung.equals(vinyl.bemerkung) : vinyl.bemerkung != null) return false;
        if (genre != null ? !genre.equals(vinyl.genre) : vinyl.genre != null) return false;
        if (id != null ? !id.equals(vinyl.id) : vinyl.id != null) return false;
        if (interpret != null ? !interpret.equals(vinyl.interpret) : vinyl.interpret != null) return false;
        if (jahr != null ? !jahr.equals(vinyl.jahr) : vinyl.jahr != null) return false;
        if (label != null ? !label.equals(vinyl.label) : vinyl.label != null) return false;
        if (name != null ? !name.equals(vinyl.name) : vinyl.name != null) return false;
        if (rubrik != vinyl.rubrik) return false;
        if (seite != null ? !seite.equals(vinyl.seite) : vinyl.seite != null) return false;
        if (song != null ? !song.equals(vinyl.song) : vinyl.song != null) return false;
        if (tontraeger != vinyl.tontraeger) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (rubrik != null ? rubrik.hashCode() : 0);
        result = 31 * result + (tontraeger != null ? tontraeger.hashCode() : 0);
        result = 31 * result + (interpret != null ? interpret.hashCode() : 0);
        result = 31 * result + (song != null ? song.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (seite != null ? seite.hashCode() : 0);
        result = 31 * result + (jahr != null ? jahr.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (bemerkung != null ? bemerkung.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Vinyl{" +
                "id=" + id +
                ", rubrik=" + rubrik +
                ", tontraeger=" + tontraeger +
                ", interpret='" + interpret + '\'' +
                ", song='" + song + '\'' +
                ", name='" + name + '\'' +
                ", seite='" + seite + '\'' +
                ", jahr='" + jahr + '\'' +
                ", genre='" + genre + '\'' +
                ", label='" + label + '\'' +
                ", bemerkung='" + bemerkung + '\'' +
                '}';
    }
}
