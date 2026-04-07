package com.sprms.system.modelMapper;

import org.mapstruct.Mapper;

import com.sprms.system.frmbeans.FundingAgencyDTO;
import com.sprms.system.hbmbeans.FundingAgency;

@Mapper(componentModel = "spring")
public interface FundingAgencyMapper {
	
	FundingAgencyDTO toDTO(FundingAgency entity);

	FundingAgency toEntity(FundingAgencyDTO dto);
}
