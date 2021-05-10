package asg.concert.service.mapper;

import java.util.ArrayList;
import java.util.List;

import asg.concert.common.dto.BookingDTO;
import asg.concert.service.domain.Booking;
import asg.concert.common.dto.SeatDTO;
import asg.concert.service.domain.Seat;

public class BookingMapper {
	
	public static Booking toDomainModel(BookingDTO dto) {
		
		//get list of DTO seats of the booking and convert to seat
		List<Seat> seats = new ArrayList<>();
		List<SeatDTO> seatsDTO = dto.getSeats();
		for (SeatDTO seatdto: seatsDTO) {
			seats.add(SeatMapper.toDomainModel(seatdto));
		}
		
		//convert booking DTO to booking
		Booking b = new Booking(dto.getConcertId(), dto.getDate(), seats);
		return b;
	}
	
	public static BookingDTO toDto(Booking b) {
		
		//get list of seats and convert to DTO seat
		List<SeatDTO> seatsdto = new ArrayList<>();
		List<Seat> seats = b.getSeats();
		for (Seat seat: seats) {
			seatsdto.add(SeatMapper.toDto(seat));
		}
		
		//convert booking to booking DTO
		BookingDTO dto = new BookingDTO(b.getConcertId(), b.getDate(), seatsdto);
		return dto;
	}
}
