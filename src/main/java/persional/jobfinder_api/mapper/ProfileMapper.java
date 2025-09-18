package persional.jobfinder_api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import persional.jobfinder_api.dto.request.ProfileUpdateRequest;
import persional.jobfinder_api.dto.respones.ProfileRespone;
import persional.jobfinder_api.model.UserProfile;

@Mapper(componentModel = "spring")
public interface ProfileMapper {

    ProfileMapper INSTANCE = Mappers.getMapper(ProfileMapper.class);

    UserProfile mapToUserProfile(ProfileUpdateRequest profileUpdateRequest);

    ProfileRespone mapToProfileRespone(UserProfile userProfile);
}
