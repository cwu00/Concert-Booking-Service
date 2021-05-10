package asg.concert.service.domain;

import java.time.LocalDateTime;

import javax.persistence.*;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import asg.concert.common.jackson.LocalDateTimeDeserializer;
import asg.concert.common.jackson.LocalDateTimeSerializer;

@Entity
public class ConcertInfoSubscription {
	
	@Column(name = "VERSION")
    private String version;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;
	
	private long concertId;
    private LocalDateTime date;
    private int percentageBooked;

    public ConcertInfoSubscription() {
    }

    public ConcertInfoSubscription(long concertId, LocalDateTime date, int percentageBooked) {
        this.concertId = concertId;
        this.date = date;
        this.percentageBooked = percentageBooked;
    }

    public long getConcertId() {
        return concertId;
    }

    public void setConcertId(long concertId) {
        this.concertId = concertId;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public LocalDateTime getDate() {
        return date;
    }

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public int getPercentageBooked() {
        return percentageBooked;
    }

    public void setPercentageBooked(int percentageBooked) {
        this.percentageBooked = percentageBooked;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        ConcertInfoSubscription that = (ConcertInfoSubscription) o;

        return new EqualsBuilder()
                .append(concertId, that.concertId)
                .append(percentageBooked, that.percentageBooked)
                .append(date, that.date)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(concertId)
                .append(date)
                .append(percentageBooked)
                .toHashCode();
    }
}
