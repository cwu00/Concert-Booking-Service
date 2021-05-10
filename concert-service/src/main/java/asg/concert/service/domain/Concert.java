package asg.concert.service.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import asg.concert.common.jackson.LocalDateTimeDeserializer;
import asg.concert.common.jackson.LocalDateTimeSerializer;

@Entity
@Table(name = "CONCERTS")
public class Concert {
	
	@Column(name = "VERSION")
    private String version;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;
    
    @Column(name = "IMAGE_NAME")
    private String imageName;

    @Column(name = "BLURB", length = 1024)
    private String blurb;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "CONCERT_PERFORMER",
        joinColumns = @JoinColumn(name = "CONCERT_ID"),
        inverseJoinColumns = @JoinColumn(name = "PERFORMER_ID"))
    private List<Performer> performers;

    @ElementCollection
    @CollectionTable(name = "CONCERT_DATES")
    @Column(name = "DATE")
    private Set<LocalDateTime> dates;

    public Concert(){}

    public Concert(String title, String imageName){
        this.title = title;
        this.imageName = imageName;
    }

    public Concert(Long id, String title, String imageName, String blurb){
        this.id = id;
        this.title = title;
        this.imageName = imageName;
        this.blurb = blurb;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    @JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
    @JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
    public Set<LocalDateTime> getDates(){
        return dates;
    }
    
    @JsonSerialize(contentUsing = LocalDateTimeSerializer.class)
    @JsonDeserialize(contentUsing = LocalDateTimeDeserializer.class)
    public void setDates(Set<LocalDateTime> dates){
        this.dates = dates;
    }

    public List<Performer> getPerformers(){
        return performers;
    }

    public void setPerformers(List<Performer> performers){
        this.performers = performers;
    }

    public String getImageName(){
        return imageName;
    }

    public void setImageName(String imageName){
        this.imageName = imageName;
    }

    public String getBlurb(){
        return blurb;
    }

    public void setBlurb(String blurb){
        this.blurb = blurb;
    }

    @Override
    public boolean equals(Object obj) {
        // code given in lab05
        // Implement value-equality based on a Concert's title alone. ID isn't
        // included in the equality check because two Concert objects could
        // represent the same real-world Concert, where one is stored in the
        // database (and therefore has an ID - a primary key) and the other
        // doesn't (it exists only in memory).
        if (!(obj instanceof Concert))
            return false;
        if (obj == this)
            return true;

        Concert rhs = (Concert) obj;
        return new EqualsBuilder().
                append(title, rhs.title).
                isEquals();
    }

    @Override
    public int hashCode() {
        // code given in lab05
        // Hash-code value is derived from the value of the title field. It's
        // good practice for the hash code to be generated based on a value
        // that doesn't change.
        return new HashCodeBuilder(17, 31).
                append(title).hashCode();
    }

    @Override
    public String toString() {
        // code given in lab05
        StringBuffer buffer = new StringBuffer();
        buffer.append("Concert, id: ");
        buffer.append(id);
        buffer.append(", title: ");
        buffer.append(title);
        buffer.append(", date: ");
        buffer.append(dates.toString());
        buffer.append(", featuring: ");
        buffer.append(performers);

        return buffer.toString();
    }
    
    public int compareTo(Concert concert) {
        return title.compareTo(concert.getTitle());
    }
}