package com.app.domain.summary.membersavedsummary.mapper;

import com.app.domain.summary.membersavedsummary.entity.MemberSavedSummary;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto.LinkedSharedResponse;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto.Patch;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto.Post;
import com.app.domain.summary.membersavedsummary.dto.MemberSavedSummaryDto.Response;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberSavedSummaryMapper {

    MemberSavedSummary summaryPostDtoToSummary(Post summaryPostDto);

    MemberSavedSummary summaryPatchDtoToSummary(Patch summaryPatchDto);

    Response summaryToResponse(MemberSavedSummary memberSavedSummary);

    default LinkedSharedResponse summaryToLinkedSharedResponse(MemberSavedSummary memberSavedSummary, boolean isWriter) {
        Response response = summaryToResponse(memberSavedSummary);
        LinkedSharedResponse linkedSharedResponse = new LinkedSharedResponse(response, isWriter);

        return linkedSharedResponse;
    }
}
