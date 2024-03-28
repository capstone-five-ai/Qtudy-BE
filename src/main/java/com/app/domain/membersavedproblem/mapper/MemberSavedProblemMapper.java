package com.app.domain.membersavedproblem.mapper;

import com.app.domain.membersavedproblem.dto.MemberSavedProblemDto;
import com.app.domain.membersavedproblem.entity.MemberSavedProblem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberSavedProblemMapper {

    MemberSavedProblem problemPostDtoToProblem(MemberSavedProblemDto.Post problemPostDto);

    MemberSavedProblem problemPatchDtoToProblem(MemberSavedProblemDto.Patch problemPatchDto);

    MemberSavedProblemDto.Response problemToResponse(MemberSavedProblem memberSavedProblem);

    default MemberSavedProblemDto.LinkSharedResponse problemToLinkSharedResponse(MemberSavedProblem memberSavedProblem, boolean isWriter){
        MemberSavedProblemDto.Response response = problemToResponse(memberSavedProblem);

        MemberSavedProblemDto.LinkSharedResponse linkSharedResponse = new MemberSavedProblemDto.LinkSharedResponse(response, isWriter);

        return linkSharedResponse;
    }
}
