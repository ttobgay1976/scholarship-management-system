package com.sprms.system.modelMapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.CollegesDTO;
import com.sprms.system.hbmbeans.Cities;
import com.sprms.system.hbmbeans.College;
import com.sprms.system.hbmbeans.Country;
import com.sprms.system.hbmbeans.State;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CollegesDTOMapper {

	// HBMBean → DTO
	public static CollegesDTO toDTO(College entity) {
		if (entity == null)
			return null;

		CollegesDTO dto = new CollegesDTO();

		dto.setId(entity.getId());
		dto.setCollegeName(entity.getCollegeName());
		dto.setShortName(entity.getShortName());
		dto.setEmail(entity.getEmail());
		dto.setPhoneNo(entity.getPhoneNo());
		dto.setAddress(entity.getAddress());
		dto.setWebsite(entity.getWebsite());
		dto.setEstablishedYear(entity.getEstablishedYear());
		dto.setStatus(entity.getStatus());
		dto.setCreatedDate(entity.getCreatedDate());
		dto.setUpdatedDate(entity.getUpdatedDate());

		// 🔹 Country
		if (entity.getCountry() != null) {
			dto.setCountryId(entity.getCountry().getId());
			dto.setCountryName(entity.getCountry().getCountryName());
		}

		// 🔹 State
		if (entity.getState() != null) {
			dto.setStateId(entity.getState().getId());
			dto.setStateName(entity.getState().getStateName());
		}

		// 🔹 City
		if (entity.getCity() != null) {
			dto.setCityId(entity.getCity().getId());
			dto.setCityName(entity.getCity().getCityName());
		}

		return dto;
	}

	// List of entities → List of DTOs
	public static List<CollegesDTO> toDTOList(List<College> entities) {
		if (entities == null)
			return null;
		return entities.stream().map(CollegesDTOMapper::toDTO).collect(Collectors.toList());
	}

	// DTO → Entity (for save/update)
	public static College toEntity(CollegesDTO dto, Country country, State state, Cities city) {
		if (dto == null)
			return null;

		College entity = new College();

		entity.setId(dto.getId());
		entity.setCollegeName(dto.getCollegeName());
		entity.setShortName(dto.getShortName());
		entity.setEmail(dto.getEmail());
		entity.setPhoneNo(dto.getPhoneNo());
		entity.setAddress(dto.getAddress());
		entity.setWebsite(dto.getWebsite());
		entity.setEstablishedYear(dto.getEstablishedYear());
		entity.setStatus(dto.getStatus());
		entity.setCreatedDate(dto.getCreatedDate());
		entity.setUpdatedDate(dto.getUpdatedDate());

		// ⚡ Assign entities
		entity.setCountry(country);
		entity.setState(state);
		entity.setCity(city);

		return entity;
	}
}
