package persional.jobfinder_api.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import persional.jobfinder_api.dto.request.SocielRequestDTO;
import persional.jobfinder_api.model.Sociel;

@Mapper(componentModel = "spring")
public interface SocielMapper {
    SocielMapper INSTANCE = Mappers.getMapper(SocielMapper.class);


    Sociel mapToSociel(SocielRequestDTO socielDTO);

    SocielRequestDTO mapToSocielDTO(Sociel sociel);

}