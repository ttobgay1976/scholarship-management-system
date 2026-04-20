package com.sprms.system.modelMapper;

import java.util.List;
import java.util.stream.Collectors;

import com.sprms.system.frmbeans.SupportingFilesDTO;
import com.sprms.system.hbmbeans.ScholarshipRegistration;
import com.sprms.system.hbmbeans.SupportingFiles;


public class SupportingFilesDTOMapper {

	public static SupportingFilesDTO toDTO(SupportingFiles entity) {

		if (entity == null)
			return null;

		SupportingFilesDTO dto = new SupportingFilesDTO();

		dto.setId(entity.getId());
		dto.setFileName(entity.getFileName());
		dto.setFileLocation(entity.getFileLocation());
		dto.setCreatedAt(entity.getCreatedAt());
		dto.setUpdatedAt(entity.getUpdatedAt());

		if (entity.getScholarshipRegistration() != null) {
			dto.setScholarshipRegistrationId(entity.getScholarshipRegistration().getId());
		}

		return dto;
	}

	// =========================
	// DTO → ENTITY
	// =========================
	public static SupportingFiles toEntity(SupportingFilesDTO dto) {

		if (dto == null)
			return null;

		SupportingFiles entity = new SupportingFiles();

		entity.setId(dto.getId());
		entity.setFileName(dto.getFileName());
		entity.setFileLocation(dto.getFileLocation());
		entity.setCreatedAt(dto.getCreatedAt());
		entity.setUpdatedAt(dto.getUpdatedAt());

		// IMPORTANT: only set ID reference (avoid full fetch here)
		if (dto.getScholarshipRegistrationId() != null) {
			ScholarshipRegistration reg = new ScholarshipRegistration();
			reg.setId(dto.getScholarshipRegistrationId());

			entity.setScholarshipRegistration(reg);
		}

		return entity;
	}

	// =========================
	// LIST ENTITY → DTO LIST
	// =========================
	public static List<SupportingFilesDTO> toDTOList(List<SupportingFiles> entities) {

		if (entities == null)
			return null;

		return entities.stream().map(SupportingFilesDTOMapper::toDTO).collect(Collectors.toList());
	}

	// =========================
	// LIST DTO → ENTITY LIST
	// =========================
	public static List<SupportingFiles> toEntityList(List<SupportingFilesDTO> dtos) {

		if (dtos == null)
			return null;

		return dtos.stream().map(SupportingFilesDTOMapper::toEntity).collect(Collectors.toList());
	}
}
