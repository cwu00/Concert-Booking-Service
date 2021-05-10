package asg.concert.service.mapper;

import asg.concert.common.dto.PerformerDTO;
import asg.concert.service.domain.Performer;

public class PerformerMapper {

	public static Performer toDomainModel(PerformerDTO dto) {
		
		//convert performer DTO to performer
		Performer p = new Performer(dto.getId(), dto.getName(), dto.getImageName(), dto.getGenre(), dto.getBlurb());
		return p;
	}
	
    public static PerformerDTO toDto(Performer performer){
    	
    	//convert performer to performer DTO
        PerformerDTO performerDTO = new PerformerDTO(
            performer.getId(), 
            performer.getName(), 
            performer.getImageName(),
            performer.getGenre(),
            performer.getBlurb());

        return performerDTO;
    }
}
