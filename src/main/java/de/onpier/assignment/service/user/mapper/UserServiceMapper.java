package de.onpier.assignment.service.user.mapper;

import java.time.LocalDate;

import de.onpier.assignment.dto.user.UserDto;
import de.onpier.assignment.model.user.User;
import de.onpier.assignment.util.Utils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", imports = { Utils.class })
public interface UserServiceMapper {

	@Mapping(source = "name", target = "name")
	@Mapping(source = "firstName", target = "firstName")
	@Mapping(source = "gender", target = "gender")
	@Mapping(target = "userId", expression = "java(Utils.generateUniqueUserId())")
	@Mapping(source = "memberSince", target = "memberSince", qualifiedByName = "localDateToLong")
	@Mapping(source = "memberTill", target = "memberTill", qualifiedByName = "localDateToLong")
	User toUser(UserDto userDto);

	@Named("localDateToLong")
	default Long localDateToLong(LocalDate localDate) {
		return Utils.localDateToLong(localDate);
	}

}
