package com.app.domain.categorizedproblem.mapper;

import com.app.domain.categorizedproblem.dto.CategorizedProblemDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategorizedProblemMapper {

//    CategorizedProblem categoryProblemPostdtoToCategoryProblem(CategorizedProblemDto.Post categorizedProblemPostDto);

    default CategorizedProblemDto.PostResponse categorizedProblemToPostResponse(List<Long> categorizedProblemIdList, List<Long> categoryIdList,
                                                                                Long memberSavedProblemId, Integer aiGeneratedProblemId){
        CategorizedProblemDto.PostResponse postResponse = new CategorizedProblemDto.PostResponse(categorizedProblemIdList, categoryIdList
                , memberSavedProblemId, aiGeneratedProblemId);

        return postResponse;
    }

}
