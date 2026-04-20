package com.sprms.system.modelMapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.SchoolDTO;
import com.sprms.system.hbmbeans.Schools;

@Component
public class SchoolDTOMapper {

	// DTO → Entity (for insert)
    public Schools toEntity(SchoolDTO dto) {

        if (dto == null) return null;

        Schools entity = new Schools();

        entity.setId(dto.getId()); // important for update
        entity.setSchoolName(dto.getSchoolName());
        entity.setSchoolLocation(dto.getSchoolLocation());
        entity.setSchoolCode(dto.getSchoolCode());
        entity.setSchoolType(dto.getSchoolType());
        entity.setStatus(entity.getStatus());
        entity.setCreatedat(entity.getCreatedat());
        entity.setUpdateat(entity.getUpdateat());

        return entity;
    }

    // Entity → DTO
    public SchoolDTO toDTO(Schools entity) {

        if (entity == null) return null;

        SchoolDTO dto = new SchoolDTO();

        dto.setId(entity.getId());
        dto.setSchoolName(entity.getSchoolName());
        dto.setSchoolLocation(entity.getSchoolLocation());
        dto.setSchoolCode(entity.getSchoolCode());
        dto.setSchoolType(entity.getSchoolType());
        entity.setStatus(entity.getStatus());
        entity.setCreatedat(entity.getCreatedat());
        entity.setUpdateat(entity.getUpdateat());

        return dto;
    }

    // 🔥 Update existing entity (BEST PRACTICE)
    public void updateEntityFromDTO(SchoolDTO dto, Schools entity) {

        if (dto == null || entity == null) return;

        entity.setSchoolName(dto.getSchoolName());
        entity.setSchoolLocation(dto.getSchoolLocation());
        entity.setSchoolCode(dto.getSchoolCode());
        entity.setSchoolType(dto.getSchoolType());
        entity.setStatus(entity.getStatus());
        entity.setCreatedat(entity.getCreatedat());
        entity.setUpdateat(entity.getUpdateat());
    }
}
