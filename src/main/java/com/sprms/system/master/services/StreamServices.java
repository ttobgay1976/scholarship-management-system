package com.sprms.system.master.services;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.StreamDTO;
import com.sprms.system.hbmbeans.Stream;
import com.sprms.system.master.dao.StreamRepository;
import com.sprms.system.modelMapper.StreamDTOMapper;
import com.sprms.system.utils.DateUtil;

@Service
public class StreamServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(StreamServices.class);

	// get repository
	private final StreamRepository _streamRepository;
	private final StreamDTOMapper _streamDTOMapper;

	// constructor
	public StreamServices(StreamRepository streamRepository, StreamDTOMapper streamDTOMapper) {
		this._streamRepository = streamRepository;
		this._streamDTOMapper = streamDTOMapper;
	}

	// Save or Update
	public StreamDTO saveOrUpdate(StreamDTO dto) {

		Stream entity = new Stream();
		entity.setId(Long.parseLong(DateUtil.getUniqueID()));
		/* entity.set(DateUtil.getCurrentDateTime()); */

		_streamRepository.save(entity);
		return _streamDTOMapper.toDTO(entity);
	}

	// Find by ID
	public StreamDTO findById(Long id) {
		Optional<Stream> stream = _streamRepository.findById(id);
		
	    // If present, map to DTO, else return null (or handle as needed)
	    return stream.map(_streamDTOMapper::toDTO).orElse(null);
	}

	// Find all
    public List<StreamDTO> findAll() {
        return _streamDTOMapper.toDTOList(_streamRepository.findAll());
    }

	// Find by Name
	public StreamDTO findByName(String name) {
		Stream stream = _streamRepository.findByStreamName(name);
		return stream != null ? _streamDTOMapper.toDTO(stream) : null;
	}
}
