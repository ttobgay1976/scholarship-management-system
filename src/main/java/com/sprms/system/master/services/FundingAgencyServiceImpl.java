package com.sprms.system.master.services;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.FundingAgencyDTO;
import com.sprms.system.hbmbeans.FundingAgency;
import com.sprms.system.master.dao.FundingAgencyRepository;
import com.sprms.system.modelMapper.FundingAgencyMapper;
import com.sprms.system.utils.DateUtil;

@Service
public class FundingAgencyServiceImpl implements FundingAgencyService {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(FundingAgencyServiceImpl.class);

	// call repository
	private final FundingAgencyRepository _fundingAgencyRepository;
	private final FundingAgencyMapper _fundingAgencyMapper;

	// constructor
	public FundingAgencyServiceImpl(FundingAgencyRepository fundingAgencyRepository,
			FundingAgencyMapper fundingAgencyMapper) {
		this._fundingAgencyRepository = fundingAgencyRepository;
		this._fundingAgencyMapper = fundingAgencyMapper;
	}

	@Override
	public FundingAgencyDTO saveFundingAgency(FundingAgencyDTO fundingAgencyDTO) {
		
		logger.info("@@@Calling this saveFundingAgency proc...............");
		
		FundingAgency entity = _fundingAgencyMapper.toEntity(fundingAgencyDTO);
		
		//set the ID and other fields
		entity.setId(Long.parseLong(DateUtil.getUniqueID()));
		entity.setCreatedAt(DateUtil.getCurrentDateTime());
		
		
		entity = _fundingAgencyRepository.save(entity);
		
		return _fundingAgencyMapper.toDTO(entity);
	}

	@Override
	public FundingAgencyDTO getFundingAgencyById(Long id) {
		
		logger.info("@@@Calling this getFundingAgencyById proc...............");
		
		return _fundingAgencyRepository.findById(id).map(_fundingAgencyMapper::toDTO)
				.orElseThrow(() -> new RuntimeException("FundingAgency not found with id: " + id));
	}

	@Override
	public List<FundingAgencyDTO> getAllFundingAgencies() {
		
		logger.info("@@@Calling this getAllFundingAgencies proc...............");
		
		return _fundingAgencyRepository.findAll().stream().map(_fundingAgencyMapper::toDTO)
				.collect(Collectors.toList());
	}

	@Override
	public FundingAgencyDTO updateFundingAgency(Long id, FundingAgencyDTO fundingAgencyDTO) {
		
		logger.info("@@@Calling this updateFundingAgency proc...............");
		
		FundingAgency existing = _fundingAgencyRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("FundingAgency not found with id: " + id));

		existing.setFundingAgencyName(fundingAgencyDTO.getFundingAgencyName());
		existing.setDescription(fundingAgencyDTO.getDescription());
		existing.setUpdateAt(fundingAgencyDTO.getUpdateAt());

		FundingAgency updated = _fundingAgencyRepository.save(existing);
		return _fundingAgencyMapper.toDTO(updated);
	}

	@Override
	public void deleteFundingAgency(Long id) {
		
		logger.info("@@@Calling this deleteFundingAgency proc...............");
		
		if (!_fundingAgencyRepository.existsById(id)) {
			throw new RuntimeException("FundingAgency not found with id: " + id);
		}
		_fundingAgencyRepository.deleteById(id);
	}

}
