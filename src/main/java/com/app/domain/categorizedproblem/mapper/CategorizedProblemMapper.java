package com.app.domain.categorizedproblem.mapper;

import com.app.domain.categorizedproblem.dto.CategorizedProblemDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategorizedProblemMapper {

    default CategorizedProblemDto.PostResponse categorizedProblemToPostResponse(List<Long> categorizedProblemIdList, List<Long> categoryIdList,
                                                                                Long problemId){
        CategorizedProblemDto.PostResponse postResponse = new CategorizedProblemDto.PostResponse(categorizedProblemIdList, categoryIdList
                , problemId);

        return postResponse;
    }

}
