package com.sprms.system.modelMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.StudentMarksDTO;
import com.sprms.system.hbmbeans.StudentMarks;

@Component
public class StudentMarksDTOMapper {

    private StudentMarksDTOMapper() {
        // prevent instantiation
    }

    // ==============================
    // ENTITY → DTO
    // ==============================
    public static StudentMarksDTO toDTO(StudentMarks entity) {

        if (entity == null) return null;

        StudentMarksDTO dto = new StudentMarksDTO();

        dto.setId(entity.getId());
        dto.setIndexNumber(entity.getIndexNumber());
        dto.setCid(entity.getCid());
        dto.setSubject(entity.getSubject());
        dto.setMarks(entity.getMarks());
        dto.setStream(entity.getStream());

        // Year → Integer
        dto.setExamYear(
                entity.getExamYear() != null
                        ? entity.getExamYear().getValue()
                        : null
        );

        return dto;
    }

    // ==============================
    // DTO → ENTITY
    // ==============================
    public static StudentMarks toEntity(StudentMarksDTO dto) {

        if (dto == null) return null;

        StudentMarks entity = new StudentMarks();

        entity.setId(dto.getId());
        entity.setIndexNumber(dto.getIndexNumber());
        entity.setCid(dto.getCid());
        entity.setSubject(dto.getSubject());
        entity.setMarks(dto.getMarks());
        entity.setStream(dto.getStream());

        // Integer → Year
        entity.setExamYear(
                dto.getExamYear() != null
                        ? java.time.Year.of(dto.getExamYear())
                        : null
        );

        return entity;
    }

    // ==============================
    // LIST ENTITY → DTO
    // ==============================
    public static List<StudentMarksDTO> toDTOList(List<StudentMarks> entities) {

        if (entities == null) return null;

        return entities.stream()
                .map(StudentMarksDTOMapper::toDTO)
                .collect(Collectors.toList());
    }

    // ==============================
    // LIST DTO → ENTITY
    // ==============================
    public static List<StudentMarks> toEntityList(List<StudentMarksDTO> dtos) {

        if (dtos == null) return null;

        return dtos.stream()
                .map(StudentMarksDTOMapper::toEntity)
                .collect(Collectors.toList());
    }
}
