package cz.muni.fi.sdipr.visualizationofozone.converter;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;

public class GenericConverter<T1, T2> {

	private Mapper mapper;
	private Class<T1> entityClass;
	private Class<T2> dtoClass;

	public GenericConverter(Class<T1> entityClass, Class<T2> dtoClass) {
		this.mapper = DozerBeanMapperSingletonWrapper.getInstance();
		this.entityClass = entityClass;
		this.dtoClass = dtoClass;
	}

	public T2 entityToDto(T1 entity) {
		return mapper.map(entity, dtoClass);
	}

	public T1 dtoToEntity(T2 dto) {
		return mapper.map(dto, entityClass);
	}

	public List<T2> entitiesToDtos(List<T1> entities) {
		List<T2> dtos = new ArrayList<T2>();
		for (T1 entity : entities) {
			dtos.add(this.entityToDto(entity));
		}
		return dtos;
	}

	public List<T1> dtosToEntities(List<T2> dtos) {
		List<T1> entities = new ArrayList<T1>();
		for (T2 dto : dtos) {
			entities.add(this.dtoToEntity(dto));
		}
		return entities;
	}
}
