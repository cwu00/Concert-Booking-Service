package asg.concert.service.domain;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import asg.concert.common.types.Genre;

@Entity
@Table(name = "PERFORMERS")
public class Performer {
	
	@Column(name = "VERSION")
    private String version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;
    
    @Column(name = "NAME", nullable = false)
    private String name;

    @Column(name = "BLURB", length = 1024)
    private String blurb;

    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Enumerated(EnumType.STRING)
    @Column(name = "GENRE")
    private Genre genre;

    public Performer(){}

    public Performer(Long id, String name, String imageName, Genre genre, String blurb){
        this.id = id;
        this.name = name;
        this.imageName = imageName;
        this.genre = genre;
        this.blurb = blurb;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public Genre getGenre(){
        return genre;
    }

    public String getBlurb(){
        return blurb;
    }

    @Override
    public boolean equals(Object obj) {
        // code given in lab05
        if (!(obj instanceof Performer))
            return false;
        if (obj == this)
            return true;

        Performer rhs = (Performer) obj;
        return new EqualsBuilder()
                .append(id, rhs.id)
                .append(name, rhs.name)
                .append(imageName, rhs.imageName)
                .append(genre, rhs.genre)
                .isEquals();
    }

    @Override
    public int hashCode() {
        // code given in lab05
        return new HashCodeBuilder(17, 37)
                .append(id)
                .append(name)
                .append(imageName)
                .append(genre)
                .toHashCode();
    }

    @Override
    public String toString() {
        // code given in lab05
        StringBuffer buffer = new StringBuffer();
        buffer.append("Performer, id: ");
        buffer.append(id);
        buffer.append(", name: ");
        buffer.append(name);
        buffer.append(", s3 image: ");
        buffer.append(imageName);
        buffer.append(", genre: ");
        buffer.append(genre.toString());

        return buffer.toString();
    }
    
    public int compareTo(Performer other) {
        return other.getName().compareTo(getName());
    }

}
