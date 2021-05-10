package asg.concert.service.mapper;

import java.time.LocalDateTime;

import asg.concert.common.dto.SeatDTO;
import asg.concert.service.domain.Seat;

public class SeatMapper {
	public static Seat toDomainModel(SeatDTO dto) {
		
		//convert seat DTO to seat
		Seat s = new Seat(dto.getLabel(), false, LocalDateTime.now(), dto.getPrice());
		return s;
	}
	
	public static SeatDTO toDto(Seat s) {
		
		//convert seat to seat DTO
		SeatDTO dto = new SeatDTO(s.getLabel(), s.getPrice());
		return dto;
	}
}
