package com.epam.rd.autocode.spring.project.mapper;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BaseMapper<Obj, Dto>{

    Obj mapFromDto (Dto dto);

    Dto mapToDto (Obj obj);

    default List<Obj> mapFromDtoList(List<Dto> listDto){
        return listDto.stream().map(this::mapFromDto).toList();
    }

    default List<Dto> mapToDtoList(List<Obj> listObj){
        return listObj.stream().map(this::mapToDto).toList();
    }
}
