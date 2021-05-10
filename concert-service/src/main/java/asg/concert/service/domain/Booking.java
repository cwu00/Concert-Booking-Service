package asg.concert.service.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asg.concert.common.jackson.LocalDateTimeDeserializer;
import asg.concert.common.jackson.LocalDateTimeSerializer;

@Entity
public class Booking {
	
	@Column(name = "VERSION")
    private String version;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private long concertId;
    private LocalDateTime date;

    // many seats can be associated with one booking
    @OneToMany(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    private List<Seat> seats;


    @ManyToOne(fetch = FetchType.LAZY)
    // each booking must be associated with a user
    private User user = null;

    public Booking(){}

    public Booking(long concertId, LocalDateTime date, List<Seat> seats){
        this.concertId = concertId;
        this.date = date;
        this.seats = seats;
    }

    public Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id = id;
    }

    public long getConcertId(){
        return concertId;
    }

    public void setConcertId(long concertId){
        this.concertId = concertId;
    }

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public LocalDateTime getDate(){
        return date;
    }
    
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    public void setDate(LocalDateTime date){
        this.date = date;
    }

    public List<Seat> getSeats(){
        return seats;
    }

    public void setSeats(List<Seat> seats){
        this.seats = seats;
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }
}
