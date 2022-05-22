package com.devsuperior.bds02.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.devsuperior.bds02.dto.EventDTO;
import com.devsuperior.bds02.entities.City;
import com.devsuperior.bds02.entities.Event;
import com.devsuperior.bds02.repositories.EventRepository;
import com.devsuperior.bds02.services.exceptions.ResourceNotFoundException;

@Service
public class EventService {
	
	@Autowired
	EventRepository eventRepository;
	
	@Transactional(readOnly = true)
	public List<EventDTO> findAll() {
		List<Event> events = eventRepository.findAll(Sort.by("name"));
		return events.stream().map(x -> new EventDTO(x)).collect(Collectors.toList());
	}
	
	@Transactional
	public EventDTO insert(EventDTO dto) {
		Event entity = new Event();
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(new City(dto.getCityId(), null));
		entity = eventRepository.save(entity);
		return new EventDTO(entity);
	}

	@Transactional
	public EventDTO update(Long id, EventDTO dto) {
		Event entity = eventRepository.getOne(id);
		entity.setName(dto.getName());
		entity.setDate(dto.getDate());
		entity.setUrl(dto.getUrl());
		entity.setCity(new City(dto.getCityId(), null));
		entity = eventRepository.save(entity);
		return new EventDTO(entity);
	}
	
	public void delete(Long id) {
		try {
			eventRepository.deleteById(id);
		}
		catch(EmptyResultDataAccessException e) {
			throw new ResourceNotFoundException("ID : " + id + " not found for Delete");
		}
		
	}
}
