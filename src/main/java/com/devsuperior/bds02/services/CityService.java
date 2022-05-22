package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.CityDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.CityRepository;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class CityService {
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@Transactional
	public List<CityDTO> findAll() {
		List<City> cities = cityRepository.findAll(Sort.by("name"));
		return cities.stream().map(x -> new CityDTO(x)).collect(Collectors.toList());
	}

	@Transactional
	public CityDTO insert(CityDTO dto) {
		City entity = new City();
		entity.setName(dto.getName());
		entity = cityRepository.save(entity);
		return new CityDTO(entity);
	}
	
	@Transactional
	public CityDTO update(Long id, CityDTO dto) {
		try {
			City entity = cityRepository.getOne(id);
			entity.setName(dto.getName());
			entity.getEvents().clear();
			for(Event evtDTO : dto.getEvents()) {
				Event event = eventRepository.getOne(evtDTO.getId());
				entity.getEvents().add(event);
			}
			entity = cityRepository.save(entity);
			return new CityDTO(entity);
		}
		catch(ResourceNotFoundException enfe) {
			throw new ResourceNotFoundException("Error Update Category: Id not found = " + id);
		}
	}
	
	public void delete(Long id) {
		try {
			cityRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("Codigo " + id + " not found for Delete");
		}
		
	}
}
