package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.sprms.system.frmbeans.CollegesDTO;
import com.sprms.system.hbmbeans.College;

@Mapper(componentModel = "spring")
public interface CollegesDTOMapper {

    // Map HBMBean → DTO
    @Mapping(target = "countryName", source = "country.countryName")
    @Mapping(target = "stateName", source = "state.stateName")
    @Mapping(target = "cityName", source = "city.cityName")
    CollegesDTO toDTO(College entity);

    default List<CollegesDTO> toDTO(List<College> entities) {
        if (entities == null) return null;
        return entities.stream().map(this::toDTO).toList();
    }
}
