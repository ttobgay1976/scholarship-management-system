package com.sprms.system.modelMapper;

import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.FundingAgencyDTO;
import com.sprms.system.frmbeans.ScholarshipProgramDTO;
import com.sprms.system.hbmbeans.FundingAgency;
import com.sprms.system.hbmbeans.ScholarshipProgram;

@Component
public class ScholarshipProgramMapper {

	 // Convert entity → DTO (for listing)
    public ScholarshipProgramDTO toDTO(ScholarshipProgram entity) {
        if (entity == null) return null;

        ScholarshipProgramDTO dto = new ScholarshipProgramDTO();
        dto.setId(entity.getId());
        dto.setScholarshipProgramName(entity.getScholarshipProgramName());
        dto.setScholarshipAvailableSlots(entity.getScholarshipAvailableSlots());
        dto.setCreatedAt(entity.getCreatedat());
        dto.setUpdateAt(entity.getUpdateat());

        // ⚡ include FundingAgency info
        if (entity.getFundingAgency() != null) {
            dto.setFundingAgencyId(entity.getFundingAgency().getId());
            dto.setFundingAgencyName(entity.getFundingAgency().getFundingAgencyName());
        }

        return dto;
    }

    // Convert DTO → entity (for save/update)
    public ScholarshipProgram toEntity(ScholarshipProgramDTO dto, FundingAgency agency) {
        if (dto == null) return null;

        ScholarshipProgram entity = new ScholarshipProgram();
        entity.setId(dto.getId());
        entity.setScholarshipProgramName(dto.getScholarshipProgramName());
        entity.setScholarshipAvailableSlots(dto.getScholarshipAvailableSlots());
        entity.setCreatedat(dto.getCreatedAt());
        entity.setUpdateat(dto.getUpdateAt());

        // ⚡ assign FundingAgency entity
        entity.setFundingAgency(agency);

        return entity;
    }
}
