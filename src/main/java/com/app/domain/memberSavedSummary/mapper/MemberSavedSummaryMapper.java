package com.app.domain.memberSavedSummary.mapper;

import com.app.domain.memberSavedSummary.dto.MemberSavedSummaryDto;
import com.app.domain.memberSavedSummary.entity.MemberSavedSummary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberSavedSummaryMapper {

    MemberSavedSummary summaryPostDtoToSummary(MemberSavedSummaryDto.Post summaryPostDto);

    MemberSavedSummary summaryPatchDtoToSummary(MemberSavedSummaryDto.Patch summaryPatchDto);

    MemberSavedSummaryDto.Response summaryToResponse(MemberSavedSummary memberSavedSummary);
}
