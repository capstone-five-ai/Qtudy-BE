package com.app.domain.summary.mapper;

import com.app.domain.summary.dto.SummaryDto;
import com.app.domain.summary.entity.Summary;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SummaryMapper {

    Summary summaryPatchDtoToSummary(SummaryDto.Patch summaryPatchDto);
}
