package com.sprms.system.modelMapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.sprms.system.frmbeans.StreamDTO;
import com.sprms.system.hbmbeans.Stream;

@Component
public class StreamDTOMapper {

	// Entity → DTO
	public StreamDTO toDTO(Stream stream) {
		if (stream == null)
			return null;
		StreamDTO dto = new StreamDTO();
		dto.setId(stream.getId());
		dto.setStreamName(stream.getStreamName());
		dto.setDescriptions(stream.getDescriptions());
		return dto;
	}

	// DTO → Entity
	public Stream toEntity(StreamDTO dto) {
		if (dto == null)
			return null;
		Stream stream = new Stream();
		stream.setId(dto.getId());
		stream.setStreamName(dto.getStreamName());
		stream.setDescriptions(dto.getDescriptions());
		return stream;
	}

	// List<Entity> → List<DTO>
	public List<StreamDTO> toDTOList(List<Stream> streams) {
		return streams.stream().map(this::toDTO).collect(Collectors.toList());
	}

	// List<DTO> → List<Entity>
	public List<Stream> toEntityList(List<StreamDTO> dtos) {
		return dtos.stream().map(this::toEntity).collect(Collectors.toList());
	}
}
