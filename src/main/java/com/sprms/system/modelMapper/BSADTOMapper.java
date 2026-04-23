package com.sprms.system.modelMapper;

import com.sprms.system.frmbeans.BSADTO;
import com.sprms.system.frmbeans.BSAFrmBean;
import com.sprms.system.hbmbeans.BSA;
import com.sprms.system.hbmbeans.BSAStatus;

public class BSADTOMapper {

    /**
     * Convert BSA Entity to BSADTO
     */
    public static BSADTO toDTO(BSA entity) {
        if (entity == null) {
            return null;
        }

        BSADTO dto = new BSADTO();
        dto.setBsaId(entity.getBsaId());
        dto.setBsaCode(entity.getBsaCode());
        dto.setBsaName(entity.getBsaName());
        dto.setDescription(entity.getDescription());
        dto.setStatus(entity.getStatus() != null ? entity.getStatus().toString() : "");

        // Country mapping
        if (entity.getCountry() != null) {
            dto.setCountryId(entity.getCountry().getId());
            dto.setCountryName(entity.getCountry().getCountryName());
        }

        // State mapping
        if (entity.getState() != null) {
            dto.setStateId(entity.getState().getId());
            dto.setStateName(entity.getState().getStateName());
        }

        // City mapping
        if (entity.getCity() != null) {
            dto.setCityId(entity.getCity().getId());
            dto.setCityName(entity.getCity().getCityName());
        }

        // Institute (College) mapping
        if (entity.getInstitute() != null) {
            dto.setInstituteId(entity.getInstitute().getId());
            dto.setInstituteName(entity.getInstitute().getCollegeName());
        }

        dto.setCreatedDate(entity.getCreatedDate());
        dto.setUpdatedDate(entity.getUpdatedDate());
        dto.setCreatedBy(entity.getCreatedBy());
        dto.setUpdatedBy(entity.getUpdatedBy());

        return dto;
    }

    /**
     * Convert BSAFrmBean to BSA Entity
     */
    public static BSA toEntity(BSAFrmBean formBean) {
        if (formBean == null) {
            return null;
        }

        BSA entity = new BSA();
        entity.setBsaId(formBean.getBsaId());
        entity.setBsaCode(formBean.getBsaCode());
        entity.setBsaName(formBean.getBsaName());
        entity.setDescription(formBean.getDescription());

        if (formBean.getStatus() != null && !formBean.getStatus().isEmpty()) {
            try {
                entity.setStatus(BSAStatus.valueOf(formBean.getStatus()));
            } catch (IllegalArgumentException e) {
                entity.setStatus(BSAStatus.ACTIVE);
            }
        } else {
            entity.setStatus(BSAStatus.ACTIVE);
        }

        return entity;
    }

    /**
     * Update BSA Entity from BSAFrmBean
     */
    public static void updateEntityFromFormBean(BSA entity, BSAFrmBean formBean) {
        if (entity != null && formBean != null) {
            entity.setBsaCode(formBean.getBsaCode());
            entity.setBsaName(formBean.getBsaName());
            entity.setDescription(formBean.getDescription());

            if (formBean.getStatus() != null && !formBean.getStatus().isEmpty()) {
                try {
                    entity.setStatus(BSAStatus.valueOf(formBean.getStatus()));
                } catch (IllegalArgumentException e) {
                    entity.setStatus(BSAStatus.ACTIVE);
                }
            }
        }
    }

    /**
     * Convert BSADTO to BSAFrmBean
     */
    public static BSAFrmBean toFormBean(BSADTO dto) {
        if (dto == null) {
            return null;
        }

        BSAFrmBean formBean = new BSAFrmBean();
        formBean.setBsaId(dto.getBsaId());
        formBean.setBsaCode(dto.getBsaCode());
        formBean.setBsaName(dto.getBsaName());
        formBean.setDescription(dto.getDescription());
        formBean.setCountryId(dto.getCountryId());
        formBean.setStateId(dto.getStateId());
        formBean.setCityId(dto.getCityId());
        formBean.setInstituteId(dto.getInstituteId());
        formBean.setStatus(dto.getStatus());

        return formBean;
    }
}
