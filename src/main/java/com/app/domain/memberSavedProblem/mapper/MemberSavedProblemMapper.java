package com.app.domain.memberSavedProblem.mapper;

import com.app.domain.memberSavedProblem.dto.MemberSavedProblemDto;
import com.app.domain.memberSavedProblem.entity.MemberSavedProblem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberSavedProblemMapper {

    MemberSavedProblem problemPostDtoToProblem(MemberSavedProblemDto.Post problemPostDto);

    MemberSavedProblem problemPatchDtoToProblem(MemberSavedProblemDto.Patch problemPatchDto);

    MemberSavedProblemDto.Response problemToResponse(MemberSavedProblem memberSavedProblem);
}
