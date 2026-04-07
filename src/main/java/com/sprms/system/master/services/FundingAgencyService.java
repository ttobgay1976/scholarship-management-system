package com.sprms.system.master.services;

import java.util.List;

import com.sprms.system.frmbeans.FundingAgencyDTO;

public interface FundingAgencyService {

	FundingAgencyDTO saveFundingAgency(FundingAgencyDTO fundingAgencyDTO);

	FundingAgencyDTO getFundingAgencyById(Long id);

	List<FundingAgencyDTO> getAllFundingAgencies();

	FundingAgencyDTO updateFundingAgency(Long id, FundingAgencyDTO fundingAgencyDTO);

	void deleteFundingAgency(Long id);
}
