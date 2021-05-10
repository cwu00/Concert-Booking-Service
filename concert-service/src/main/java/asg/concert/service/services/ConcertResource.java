package asg.concert.service.services;

import asg.concert.service.domain.*;

import asg.concert.service.jaxrs.LocalDateTimeParam;

import asg.concert.common.dto.*;
import asg.concert.common.types.BookingStatus;
import asg.concert.service.mapper.*;

import javax.ws.rs.*;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
import javax.ws.rs.core.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javax.persistence.*;

import org.slf4j.*;

@Path("/concert-service")
public class ConcertResource {
	
	private static Logger LOGGER = LoggerFactory.getLogger(ConcertResource.class);	
	
	private static List<AsyncResponse> subs = new ArrayList<AsyncResponse>();

	/**
	 * Retrieves a particular concert.
	 * @param id	unique identifier of the retrieval target concert
	 * @return 		a response containing a ConertDTO object which represents the target concert.
	 */
    @GET
    @Path("/concerts/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveConcert(@PathParam("id") long id) {
    	
    	LOGGER.info("Retrieving concert with id: " + id);
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {
        	em.getTransaction().begin();
        	
        	Concert concert = em.find(Concert.class, id);
        	
        	em.getTransaction().commit();
        	
        	if (concert == null)
        		return Response.status(Response.Status.NOT_FOUND).build();
        	
        	ConcertDTO dtoConcert = ConcertMapper.toDto(concert);
        	
        	return Response.ok(dtoConcert).build();
        	
    	} finally {
    		em.close();
    	}
    }
    
    /**
	 * Retrieve all concerts currently in the database.
	 * @return a list of concertDTO objeects that represents all concerts currently in the application database
	 */
	@GET
    @Path("/concerts")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllConcerts() {
    	
    	LOGGER.info("Retrieving all concerts");
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {
        	em.getTransaction().begin();
        	
        	// Getting concerts
        	TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
        	List<Concert> concerts = concertQuery.getResultList();
        	
        	em.getTransaction().commit();
        	
        	if (concerts == null)
        		return Response.status(Response.Status.NOT_FOUND).build();
        	
        	// Converting to concertsDTO
        	List<ConcertDTO> concertsDTO = new ArrayList<ConcertDTO>();
        	
        	for (Concert concert : concerts) 
        		concertsDTO.add(ConcertMapper.toDto(concert));
        	
        	GenericEntity<List<ConcertDTO>> entity = new GenericEntity<List<ConcertDTO>>(concertsDTO) {};
        	
        	return Response.ok(entity).build();
        	
    	} finally {
    		em.close();
    	}
    }
    

    /**
	 * Retrieve a particular performer
	 * @param id 	unique identififer of the retrival target performer
	 * @return		a performerDTO object that represents the target performer
	 */
	@GET
    @Path("performers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrievePerformer(@PathParam("id") long id) {
    	
    	LOGGER.info("Retrieving performer with id: " + id);
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {
        	em.getTransaction().begin();
        	
        	Performer performer = em.find(Performer.class, id);
        	
        	em.getTransaction().commit();
        	
        	if (performer == null)
        		return Response.status(Response.Status.NOT_FOUND).build();
        	
        	PerformerDTO dtoPerformer = PerformerMapper.toDto(performer);
        	
        	return Response.ok(dtoPerformer).build();
        	
    	} finally {
    		em.close();
    	}
    }
    
	/**
	 * Retrieve all performers currently in the application database
	 * @return a list of PerformerDTO objects
	 */
    @GET
    @Path("/performers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveAllPerformers() {
    	
    	LOGGER.info("Retrieving all performers");
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {
        	em.getTransaction().begin();
        	
        	// Getting performers
        	TypedQuery<Performer> performerQuery = em.createQuery("select p from Performer p", Performer.class);
        	List<Performer> performers = performerQuery.getResultList();
        	
        	em.getTransaction().commit();
        	
        	if (performers == null)
        		return Response.status(Response.Status.NOT_FOUND).build();
        	
        	// Converting to performersDTO
        	List<PerformerDTO> performersDTO = new ArrayList<PerformerDTO>();
        	
        	for (Performer performer : performers) 
        		performersDTO.add(PerformerMapper.toDto(performer));
        	
        	GenericEntity<List<PerformerDTO>> entity = new GenericEntity<List<PerformerDTO>>(performersDTO) {};
        	
        	return Response.ok(entity).build();
        	
    	} finally {
    		em.close();
    	}
    }
    
	/**
	 * Retrive summaries of all concerts currently in the application database
	 * @return	summeries of all concerts
	 */
    @GET
    @Path("/concerts/summaries")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getConcertSummaries() {
    	
    	LOGGER.info("Getting all summaries");
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {
        	em.getTransaction().begin();
        	
        	// Getting concerts
        	TypedQuery<Concert> concertQuery = em.createQuery("select c from Concert c", Concert.class);
        	List<Concert> concerts = concertQuery.getResultList();
        	
        	em.getTransaction().commit();
        	
        	// Converting to concertSummaryDTO
        	List<ConcertSummaryDTO> concertSummariesDTO = new ArrayList<ConcertSummaryDTO>();
        	
    	    for (Concert concert: concerts) {
    	    	ConcertSummaryDTO concertSummaryDTO = new ConcertSummaryDTO(concert.getId(), concert.getTitle(), concert.getImageName()); 
    	    	concertSummariesDTO.add(concertSummaryDTO);
    	    }
        	
        	GenericEntity<List<ConcertSummaryDTO>> entity = new GenericEntity<List<ConcertSummaryDTO>>(concertSummariesDTO) {};
        	
        	return Response.ok(entity).build();
        	
    	} finally {
    		em.close();
    	}
    }
    
	/**
	 * Authenticate user by giving them an authenticatoin token as a cookie,
	 * when they verify their username and password and log in
	 * @param userDTO	contains user information, including username and password
	 * @return 			cookie with authentication token
	 */
    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserDTO userDTO) {
        	
	    LOGGER.info("Trying to log in");
	    
		Response response;
	    EntityManager em = PersistenceManager.instance().createEntityManager();
        	
	    try {
	    	
	    	em.getTransaction().begin();
	    	
	    	TypedQuery<User> userQuery = em.createQuery("select u from User u where u.username = :inputUsername AND u.password = :inputPassword", User.class)
			.setParameter("inputUsername", userDTO.getUsername())
			.setParameter("inputPassword", userDTO.getPassword());
	    	
	    	User user = userQuery.getResultList().stream().findFirst().orElse(null);
	    
	    	if (user == null){
				response= Response.status(Response.Status.UNAUTHORIZED).build();
			} else {
				NewCookie cookie = new NewCookie("auth", UUID.randomUUID().toString());
				user.setCookie(cookie.getValue());
				em.merge(user);
				response = Response.ok().cookie(cookie).build();
			}
			em.getTransaction().commit();
		} finally {
			em.close();
		}
		return response;
    }
    

	/**
	 *	Make a booking for a particular user
	 * @param bReq		contains information about the booking to be made
	 * @param cookie	contains authentication token obtained by user
	 * @return			URI link to the new booking that was made
	 */
    @POST
    @Path("/bookings")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response booking(BookingRequestDTO bReq, @CookieParam("auth") Cookie cookie) {
    	
    	if (cookie == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}

    	LOGGER.info("Trying to book a seat");
        
    	// variables that we get from the request
    	long concertId = bReq.getConcertId();
    	LocalDateTime date = bReq.getDate();
    	List<String> seatLabels = bReq.getSeatLabels();
	
	    EntityManager em = PersistenceManager.instance().createEntityManager();
	        	
	    try {
	    	
	    	em.getTransaction().begin();
	    	
			User user = em.createQuery("select u from User u where u.cookie = :cookie", User.class)
			.setParameter("cookie", cookie.getValue()).getResultList().stream().findFirst().orElse(null);

			em.getTransaction().commit();
			
			if (user ==  null) {
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}

			em.getTransaction().begin();
	    	// retrieving concert and checking whether request is a good request 
	    	Concert concert = em.find(Concert.class, concertId);
	    	
	    	em.getTransaction().commit();
	    	
	    	if (concert == null)
	    		return Response.status(Response.Status.BAD_REQUEST).build();
	    	
	    	Set<LocalDateTime> dates = concert.getDates();
	    	if(!dates.contains(date))
	    		return Response.status(Response.Status.BAD_REQUEST).build();
	    	
	    	
	    	// retrieving seats with specified labels to book    	
	    	em.getTransaction().begin();
	    	
	    	List<Seat> seats = new ArrayList<Seat>();
	    	
	    	for (String label: seatLabels) {
	    		TypedQuery<Seat> seatQuery = em.createQuery("select s from Seat s where s.label = :label AND s.date = :date", Seat.class);
		    	seatQuery.setParameter("label", label);
		    	seatQuery.setParameter("date", date);
		    	seats.add(seatQuery.getSingleResult());
	    	}
	    	
	    	em.getTransaction().commit();
	    	
	    	for (Seat seat: seats) {
	    		if (!seat.getIsBooked()) {
	    			seat.setIsBooked(true);
	    		}else {
	    			return Response.status(Response.Status.FORBIDDEN).build();
	    		}
	    	}

	    	Booking booking = new Booking(concertId, date, seats);
	    	booking.setUser(user);
	    	
	    	em.getTransaction().begin();
	    	
	    	for (Seat seat: seats)
	    		em.persist(seat);
	    	
	    	em.persist(booking);
	    	
	    	List<Seat> seatsInfo = new ArrayList<Seat>();
	    	
	    	TypedQuery<Seat> seatQuery = em.createQuery("select s from Seat s where s.date = :date", Seat.class);
		    seatQuery.setParameter("date", date);
		    seatsInfo = seatQuery.getResultList();
	    	
	    	
	    	em.getTransaction().commit();
	    	
	    	int totalBooked = 0;
	    	
	    	for (Seat seat: seatsInfo) {
        		if (seat.getIsBooked())
        			totalBooked += 1;
        	}
    	

	    	processConcertInfo(concertId, date, totalBooked, seatsInfo.size());
	    	
	    	return Response.created(URI.create("/concert-service/bookings/" + user.getId().toString())).build();
	    	
	    } finally {
	    	em.close();
		}
    }
    
	/**
	 * Retrieves a list of seats that are booked for a particular date
	 * @param dateParam	the date that the seats are available on
	 * @param status	status of the seat
	 * @return			a list of seats depending on status and date
	 */
    @GET
    @Path("seats/{date}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkForSeats(@PathParam("date") LocalDateTimeParam dateParam, @QueryParam("status") BookingStatus status) {
    	
    	LocalDateTime date = dateParam.getLocalDateTime();
    	
    	LOGGER.info("Checking for success seat booking");
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {
    		
    		List<Seat> seats = new ArrayList<Seat>();
    		
        	em.getTransaction().begin();
        	
        	TypedQuery<Seat> seatQuery = em.createQuery("select s from Seat s where s.date = :date", Seat.class);
	    	seatQuery.setParameter("date", date);
	    	seats = seatQuery.getResultList();
        	
        	em.getTransaction().commit();
        	List<SeatDTO> bookedSeats = new ArrayList<SeatDTO>();
        	if(status == BookingStatus.Booked) {
            	for (Seat seat: seats) {
            		if (seat.getIsBooked())
            			bookedSeats.add(SeatMapper.toDto(seat));
            	}
        	}else if (status == BookingStatus.Unbooked){
            	for (Seat seat: seats) {
            		if (!seat.getIsBooked())
            			bookedSeats.add(SeatMapper.toDto(seat));
            	}
        	}else {
            	for (Seat seat: seats) 
            			bookedSeats.add(SeatMapper.toDto(seat));
        	}
      
        	
        	GenericEntity<List<SeatDTO>> entity = new GenericEntity<List<SeatDTO>>(bookedSeats) {};
        	
        	return Response.ok(entity).build();
        	
    	} finally {
    		em.close();
    	}
    }
    
    

    /**
	 * Retrieve all bookings made by a particular user
	 * @param cookie	contains authentication token obtained by user
	 * @return			a list of bookings made by user
	 */
    @GET
    @Path("/bookings")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookings(@CookieParam("auth") Cookie cookie) {
    	
    	LOGGER.info("Retrieving bookings");
    	
    	if (cookie == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
        
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {

        	em.getTransaction().begin();

        	//retrieving user
	    	TypedQuery<User> userQuery = em.createQuery("select u from User u where u.cookie = :cookie", User.class);
	    	userQuery.setParameter("cookie", cookie.getValue());
	    	User user = userQuery.getResultList().stream().findFirst().orElse(null);

			em.getTransaction().commit();
			
			em.getTransaction().begin();
			if (user == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
        	
	    	TypedQuery<Booking> bookingQuery = em.createQuery("SELECT b FROM Booking b WHERE b.user = :user", Booking.class);
	    	bookingQuery.setParameter("user", user);
	    	List<Booking> bookings = bookingQuery.getResultList();
        
        	em.getTransaction().commit();
        	
        	List<BookingDTO> bookingsDTO = new ArrayList<BookingDTO>();
        	for (Booking booking: bookings) {
        		bookingsDTO.add(BookingMapper.toDto(booking));
        	}
        	
        	GenericEntity<List<BookingDTO>> entity = new GenericEntity<List<BookingDTO>>(bookingsDTO) {};
        	
        	return Response.ok(entity).build();
        	
    	} finally {
    		em.close();
    	}
    }

    
    /**
	 * Get a particular booking by its unique identifier
	 * @param id		unique identifier for retrival target booking
	 * @param cookie	contains authentication token obtained by user
	 * @return			the target booking
	 */
    @GET
    @Path("/bookings/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookingsById(@PathParam("id") long id, @CookieParam("auth") Cookie cookie) {
    	
		if (cookie == null){
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
    	
		LOGGER.info("Retrieving bookings");
    	
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {

        	em.getTransaction().begin();

        	//retrieving user
	    	TypedQuery<User> userQuery = em.createQuery("select u from User u where u.cookie = :cookie", User.class);
	    	userQuery.setParameter("cookie", cookie.getValue());
	    	User user = userQuery.getResultList().stream().findFirst().orElse(null);

			if (user == null){
				return Response.status(Response.Status.UNAUTHORIZED).build();
			}
	    	
	    	em.getTransaction().commit();
	    	
	    	if (user.getId() != id) {
	    		return Response.status(Response.Status.FORBIDDEN).build();
	    	}
	    	
	    	em.getTransaction().begin();
	    	
	    	TypedQuery<Booking> bookingQuery = em.createQuery("SELECT b FROM Booking b WHERE b.user = :user", Booking.class);
	    	bookingQuery.setParameter("user", user);
	    	List<Booking> bookings = bookingQuery.getResultList();
        
        	em.getTransaction().commit();
        	
        	BookingDTO booking = BookingMapper.toDto(bookings.get(bookings.size() - 1));
        	
        	return Response.ok(booking).build();
        	
    	} finally {
    		em.close();
    	}
    }
 
    /**
	 * Add a subscriber to observe booking changes
	 * @param sub		subscriber response instance of the client
	 * @param subInfo	information user had subscribed to
	 * @param cookie	contains authentication token obtained by user, user must be authenticated to subscribe
	 */
    @POST
    @Path("/subscribe/concertInfo")
    @Consumes(MediaType.APPLICATION_JSON)
    public void postMessage(@Suspended AsyncResponse sub, ConcertInfoSubscriptionDTO subInfo, @CookieParam("auth") Cookie cookie) {
    	
    	if (cookie == null){
			sub.resume(Response.status(Response.Status.UNAUTHORIZED).build());
			return;
		}

    		
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	try {
    		
        	long concertId = subInfo.getConcertId();
        	LocalDateTime date = subInfo.getDate();
        	
        	em.getTransaction().begin();
        	
    		// retrieving concert and checking whether request is a good request 
	    	Concert concert = em.find(Concert.class, concertId);
	    	
	    	em.getTransaction().commit();
	    	
	    	
	    	if (concert == null) {
	    		sub.resume(Response.status(Response.Status.BAD_REQUEST).build());
	    		return;
	    	}
	    	
	    	Set<LocalDateTime> dates = concert.getDates();
	    	
	    	if(!dates.contains(date)) {
	    		sub.resume(Response.status(Response.Status.BAD_REQUEST).build());
	    		return;
	    	}
	    	
	    	em.getTransaction().begin();
	    	em.persist(ConcertInfoSubscriptionMapper.toDomainModel(subInfo));
	    	em.getTransaction().commit();

	    	subs.add(sub);
	    	
	    } finally {
	    	em.close();
		}
    }
    
	/**
	 * Helper method for updating subscription
	 * @param concertId		unique identifier for concert of subscription
	 * @param date			date of concert of subscription
	 * @param seatsBooked	number of seats that had been booked
	 * @param seats			number of available seats
	 */
    @Produces(MediaType.APPLICATION_JSON)
    private void processConcertInfo(long concertId, LocalDateTime date, int seatsBooked, int seats) {
    	EntityManager em = PersistenceManager.instance().createEntityManager();
    	
    	try {

	    	em.getTransaction().begin();
	    	
	    	TypedQuery<ConcertInfoSubscription> concertInfoSubscriptionQuery = em.createQuery("SELECT cs FROM ConcertInfoSubscription cs WHERE cs.concertId =: concertId", ConcertInfoSubscription.class);
	    	concertInfoSubscriptionQuery.setParameter("concertId", concertId);
	    	List<ConcertInfoSubscription> concertInfoSubscriptions = concertInfoSubscriptionQuery.getResultList();
	    	
	    	em.getTransaction().commit();
	    	for (ConcertInfoSubscription subscription: concertInfoSubscriptions) {
	    		
	    		if( (int)((float)seatsBooked / (float)seats * 100) >= subscription.getPercentageBooked() & subscription.getConcertId() == concertId & subscription.getDate().equals(date)) {
	    			
			    	ConcertInfoNotificationDTO notif = new ConcertInfoNotificationDTO(seats - seatsBooked);
			    	
			    	synchronized (subs) {
					for (AsyncResponse sub : subs) {
						
						sub.resume(Response.ok(notif, MediaType.APPLICATION_JSON).build());
					    }
				    subs.clear();
			    	}
	    		}
	    			
	    	}
	    } finally {
	    	em.close();
		}
	}
    		
}

