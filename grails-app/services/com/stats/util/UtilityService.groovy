package com.stats.util

import com.stats.dto.ErrorDTO
import com.stats.feed.Feed
import groovy.sql.Sql

/**
 * Created by admin on 7/13/2017.
 */
class UtilityService {

    def dataSource

    def ErrorDTO createError(String status, String error){
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setCode(status);
        errorDTO.setMsg(error);

        return errorDTO;
    }

    def getSequenceNumber(){

        return Feed.count()
    }
}
