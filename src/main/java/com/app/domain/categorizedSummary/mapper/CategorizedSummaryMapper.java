package com.app.domain.categorizedSummary.mapper;

import com.app.domain.categorizedSummary.dto.CategorizedSummaryDto;
import com.app.domain.categorizedSummary.entity.CategorizedSummary;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategorizedSummaryMapper {

    default CategorizedSummaryDto.PostResponse categorizedSummaryToPostResponse(List<Long> categorizedSummaryIdList, CategorizedSummaryDto.Post categorizedSummaryPostDto){
        return CategorizedSummaryDto.PostResponse.of(categorizedSummaryIdList, categorizedSummaryPostDto);
    }

    default CategorizedSummaryDto.Response categorizedSummaryToResponse(CategorizedSummary categorizedSummary) {
        return CategorizedSummaryDto.Response.of(categorizedSummary);
    }

    default CategorizedSummaryDto.LinkedSharedResponse categorizedSummaryToLinkedSharedResponse(CategorizedSummary categorizedSummary, Boolean isWriter){
        CategorizedSummaryDto.Response response = categorizedSummaryToResponse(categorizedSummary);
        CategorizedSummaryDto.LinkedSharedResponse linkedSharedResponse = new CategorizedSummaryDto.LinkedSharedResponse(response, isWriter);

        return linkedSharedResponse;
    }
}
