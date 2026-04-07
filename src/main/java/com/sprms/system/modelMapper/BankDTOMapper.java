package com.sprms.system.modelMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.BankDTO;
import com.sprms.system.hbmbeans.Bank;
import com.sprms.system.hbmbeans.College;

@Component
public class BankDTOMapper {

	 // Convert Bank entity → BankDTO
    public static BankDTO toDTO(Bank entity) {
        if (entity == null) return null;

        BankDTO dto = new BankDTO();
        dto.setId(entity.getId());
        dto.setAccountHolderName(entity.getAccountHolderName());
        dto.setBankName(entity.getBankName());
        dto.setBranchName(entity.getBranchName());
        dto.setAccountNo(entity.getAccountNo());
        dto.setIfscSwiftCode(entity.getIfscSwiftCode());
        dto.setCreatedAt(entity.getCreatedat());
        dto.setUpdatedAt(entity.getUpdatedat());

        // Include College info
        if (entity.getCollege() != null) {
            dto.setCollegeId(entity.getCollege().getId());
            dto.setCollegeName(entity.getCollege().getCollegeName());
        }

        return dto;
    }

    // Convert list of entities → list of DTOs
    public static List<BankDTO> toDTOList(List<Bank> entities) {
        if (entities == null) return null;
        return entities.stream().map(BankDTOMapper::toDTO).collect(Collectors.toList());
    }

    // Convert BankDTO → Bank entity
    public static Bank toEntity(BankDTO dto, College college) {
        if (dto == null) return null;

        Bank entity = new Bank();
        entity.setId(dto.getId());
        entity.setAccountHolderName(dto.getAccountHolderName());
        entity.setBankName(dto.getBankName());
        entity.setBranchName(dto.getBranchName());
        entity.setAccountNo(dto.getAccountNo() != null ? dto.getAccountNo() : 0L);
        entity.setIfscSwiftCode(dto.getIfscSwiftCode());
        entity.setCreatedat(dto.getCreatedAt());
        entity.setUpdatedat(dto.getUpdatedAt());

        // Assign College entity
        entity.setCollege(college);

        return entity;
    }
}
