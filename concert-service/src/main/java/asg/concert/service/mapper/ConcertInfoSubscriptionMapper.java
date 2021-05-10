package asg.concert.service.mapper;

import asg.concert.common.dto.ConcertInfoSubscriptionDTO;
import asg.concert.service.domain.ConcertInfoSubscription;

public class ConcertInfoSubscriptionMapper {
	public static ConcertInfoSubscription toDomainModel(ConcertInfoSubscriptionDTO dto) {
		
		//convert concertinfosubscription DTO to concertinfosubscription
		ConcertInfoSubscription concertInfoSubsription = new ConcertInfoSubscription(dto.getConcertId(), dto.getDate(), dto.getPercentageBooked());
		return concertInfoSubsription;
	}
	
	public static ConcertInfoSubscriptionDTO toDto(ConcertInfoSubscription cis) {
		//convert concertinfosubscription to concertinfosubscription DTO
		ConcertInfoSubscriptionDTO dtoConcertInfoSubsription = new ConcertInfoSubscriptionDTO(cis.getConcertId(), cis.getDate(), cis.getPercentageBooked());
		return dtoConcertInfoSubsription;
	}
}
