package com.sprms.system.modelMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.ScholarshipRegistrationDTO;
import com.sprms.system.hbmbeans.ScholarshipRegistration;
import com.sprms.system.hbmbeans.Stream;

@Component
public class ScholarshipRegistrationDTOMapper {

	// 🔹 Entity → DTO
	public static ScholarshipRegistrationDTO toDTO(ScholarshipRegistration entity) {
		if (entity == null) {
			return null;
		}

		ScholarshipRegistrationDTO dto = new ScholarshipRegistrationDTO();

		dto.setId(entity.getId());
		dto.setCitizenId(entity.getCitizenId());
		dto.setFirstName(entity.getFirstName());
		dto.setMiddleName(entity.getMiddleName());
		dto.setLastName(entity.getLastName());

		dto.setDateOfBirth(entity.getDateOfBirth());
		dto.setGender(entity.getGender());

		dto.setFatherName(entity.getFatherName());
		dto.setMotherName(entity.getMotherName());

		dto.setPermanentAddress(entity.getPermanentAddress());
		dto.setGuardianDetail(entity.getGuardianDetail());

		dto.setIndexNumber(entity.getIndexNumber());

		dto.setEmailAddress(entity.getEmailAddress());
		dto.setContactNo(entity.getContactNo());

	    // ✅ FIX STREAM MAPPING (IMPORTANT)
	    if (entity.getStream() != null) {
	        dto.setStreamId(entity.getStream().getId());
	        dto.setStreamName(entity.getStream().getStreamName());
	    }
		
		dto.setCountryOfCompletion(entity.getCountryOfCompletion());

		dto.setRemarks(entity.getRemarks());

		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());
		
		//new added fields
		/* dto.setStatus(entity.getStatus()); */
		dto.setVerified_at(entity.getVerified_at());
		dto.setVerified_by(entity.getVerified_by());
		dto.setApproved_by(entity.getApproved_by());
		dto.setApproved_at(entity.getApproved_at());
		
		dto.setVerifier_remarks(entity.getVerifier_remarks());
		dto.setApproval_remarks(entity.getApproval_remarks());
		
		dto.setStatus(entity.getStatus());
		
		return dto;
	}

	// 🔹 List<Entity> → List<DTO>
	public static List<ScholarshipRegistrationDTO> toDTOList(List<ScholarshipRegistration> entities) {
		if (entities == null) {
			return null;
		}

		return entities.stream().map(ScholarshipRegistrationDTOMapper::toDTO).collect(Collectors.toList());
	}

	// 🔹 DTO → Entity (for insert)
	public static ScholarshipRegistration toEntity(ScholarshipRegistrationDTO dto) {
		if (dto == null) {
			return null;
		}

		ScholarshipRegistration entity = new ScholarshipRegistration();

		entity.setId(dto.getId());
		entity.setCitizenId(dto.getCitizenId());
		entity.setFirstName(dto.getFirstName());
		entity.setMiddleName(dto.getMiddleName());
		entity.setLastName(dto.getLastName());

		entity.setDateOfBirth(dto.getDateOfBirth());
		entity.setGender(dto.getGender());

		entity.setFatherName(dto.getFatherName());
		entity.setMotherName(dto.getMotherName());

		entity.setPermanentAddress(dto.getPermanentAddress());
		entity.setGuardianDetail(dto.getGuardianDetail());

		entity.setIndexNumber(dto.getIndexNumber());

		entity.setEmailAddress(dto.getEmailAddress());
		entity.setContactNo(dto.getContactNo());

	    // ✅ FIXED STREAM MAPPING (VERY IMPORTANT)
	    if (dto.getStreamId() != null) {
	        Stream stream = new Stream();
	        stream.setId(dto.getStreamId());
	        entity.setStream(stream);
	    }


		entity.setCountryOfCompletion(dto.getCountryOfCompletion());

		entity.setRemarks(dto.getRemarks());

		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());
		
		//new added fields
		 entity.setStatus(dto.getStatus()); 
		entity.setVerified_by(dto.getVerified_by());
		entity.setVerified_at(dto.getVerified_at());
		entity.setApproved_by(dto.getApproved_by());
		entity.setApproved_at(dto.getApproved_at());
		
		entity.setVerifier_remarks(dto.getVerifier_remarks());
		entity.setApproval_remarks(dto.getApproval_remarks());
		

		return entity;
	}

	// 🔹 Update existing entity (for edit)
	public static void updateEntityFromDTO(ScholarshipRegistrationDTO dto, ScholarshipRegistration entity) {
		if (dto == null || entity == null) {
			return;
		}

		entity.setCitizenId(dto.getCitizenId());
		entity.setFirstName(dto.getFirstName());
		entity.setMiddleName(dto.getMiddleName());
		entity.setLastName(dto.getLastName());

		entity.setDateOfBirth(dto.getDateOfBirth());
		entity.setGender(dto.getGender());

		entity.setFatherName(dto.getFatherName());
		entity.setMotherName(dto.getMotherName());

		entity.setPermanentAddress(dto.getPermanentAddress());
		entity.setGuardianDetail(dto.getGuardianDetail());

		entity.setIndexNumber(dto.getIndexNumber());

		entity.setEmailAddress(dto.getEmailAddress());
		entity.setContactNo(dto.getContactNo());

	    // ✅ IMPORTANT: FIX STREAM RELATION
	    if (dto.getStreamId() != null) {
	        Stream stream = new Stream();
	        stream.setId(dto.getStreamId());
	        entity.setStream(stream);
	    } else {
	        entity.setStream(null); // optional safety
	    }
		entity.setCountryOfCompletion(dto.getCountryOfCompletion());

		entity.setRemarks(dto.getRemarks());

		entity.setUpdatedAt(dto.getUpdatedAt());
		
		//new added fields
		/* entity.setStatus(dto.getStatus()); */
		entity.setVerified_by(dto.getVerified_by());
		entity.setVerified_at(dto.getVerified_at());
		entity.setApproved_by(dto.getApproved_by());
		entity.setApproved_at(dto.getApproved_at());
		
		entity.setVerifier_remarks(dto.getVerifier_remarks());
		entity.setApproval_remarks(dto.getApproval_remarks());
		
	}

}
