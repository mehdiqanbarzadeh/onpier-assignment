package de.onpier.assignment.api.web.user.mapper;

import java.util.List;

import de.onpier.assignment.dto.user.UserResponse;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.util.Utils;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface UserControllerMapper {


	@IterableMapping(qualifiedByName = "toUserResponse")
	List<UserResponse> toUserResponse(List<User> activeUsers);

	@Named("toUserResponse")
	@Mapping(source = "name", target = "name")
	@Mapping(source = "firstName", target = "firstName")
	@Mapping(source = "memberSince", target = "memberFrom", qualifiedByName = "localDateToStringFormat")
	@Mapping(source = "memberTill", target = "memberTill", qualifiedByName = "localDateToStringFormat")
	@Mapping(source = "gender", target = "gender")
	@Mapping(source = "userId", target = "userId")
	UserResponse toUserResponse(User user);

	@Named("localDateToStringFormat")
	default String localDateToStringFormat(Long localDate) {
		return Utils.localDateToString(localDate);
	}
}
