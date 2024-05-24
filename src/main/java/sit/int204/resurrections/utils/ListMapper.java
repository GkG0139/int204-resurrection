package sit.int204.resurrections.utils;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import sit.int204.resurrections.dtos.PageDTO;

import java.util.List;
import java.util.stream.StreamSupport;

@Component
@Scope("singleton")
public class ListMapper {
    private final ModelMapper modelMapper;

    @Autowired
    public ListMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <S, T> List<T> convertList(Iterable<S> source, Class<T> targetClass) {
        return StreamSupport.stream(source.spliterator(), false)
                .map(entity -> modelMapper.map(entity, targetClass))
                .toList();
    }

    public <S, T> List<T> convertList(List<S> source, Class<T> targetClass) {
        return source.stream()
                .map(entity -> modelMapper.map(entity, targetClass))
                .toList();
    }

    public <S, T> PageDTO<T> toPageDTO(Page<S> source, Class<T> targetClass) {
        PageDTO<T> page = modelMapper.map(source, PageDTO.class);
        page.setContent(convertList(source.getContent(), targetClass));
        return page;
    }
}