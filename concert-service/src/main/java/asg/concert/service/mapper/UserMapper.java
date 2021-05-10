package asg.concert.service.mapper;

import asg.concert.common.dto.UserDTO;
import asg.concert.service.domain.User;

public class UserMapper {
	public static User toDomainModel(UserDTO userDTO) {
		User user = new User(userDTO.getUsername(), userDTO.getPassword());
		return user;
	}
	
	public static UserDTO toDto(User user){
		UserDTO userDTO = new UserDTO(user.getUsername(), user.getPassword());
		return userDTO;
	}
}
