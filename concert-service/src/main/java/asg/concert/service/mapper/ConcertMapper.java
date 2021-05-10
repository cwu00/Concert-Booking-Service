package asg.concert.service.mapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import asg.concert.common.dto.ConcertDTO;
import asg.concert.common.dto.PerformerDTO;
import asg.concert.service.domain.Concert;
import asg.concert.service.domain.Performer;

public class ConcertMapper {
	public static ConcertDTO toDto(Concert concert){
		
		//get performers from concert and convert to DTO performer
    	List<Performer> performers = concert.getPerformers();
    	List<PerformerDTO> performersDTO = new ArrayList<>();
    	
        for (Performer p : performers){
            performersDTO.add(PerformerMapper.toDto(p));
        }
        
        //convert concert to DTO
        ConcertDTO concertDTO = new ConcertDTO(
        		concert.getId(),
        		concert.getTitle(),
        		concert.getImageName(),
        		concert.getBlurb()
        );
        
        //get dates and set it for DTO concert
        Set<LocalDateTime> dates = concert.getDates();
        List<LocalDateTime> listDates = new ArrayList<>();
        
        for (LocalDateTime d: dates) {
        	listDates.add(d);
        }
        
        concertDTO.setDates(listDates);
        concertDTO.setPerformers(performersDTO);
       
		return concertDTO;
	}
    
    public static Concert toDomainModel(ConcertDTO dto) {
    	
    	//get list of DTO performers and convert to performers
    	List<PerformerDTO> performersDTO = dto.getPerformers();
    	List<Performer> performers = new ArrayList<>();
        for (PerformerDTO p : performersDTO){
            performers.add(PerformerMapper.toDomainModel(p));
        }
        
        //convert DTO concert to concert
		Concert concert = new Concert(dto.getId(), dto.getTitle(),dto.getImageName(), dto.getBlurb());
		
		//get dates for concert
		Set<LocalDateTime> setDates = new HashSet<>();
        List<LocalDateTime> dates = dto.getDates();
        
        for (LocalDateTime d: dates) {
        	if (!(setDates.contains(d))) {
        		setDates.add(d);
        	}
        }
        
        concert.setDates(setDates);
        concert.setPerformers(performers);
        
		return concert;
	}
}
