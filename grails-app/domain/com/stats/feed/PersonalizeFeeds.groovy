package com.stats.feed

/**
 * Created by admin on 7/13/2017.
 */
class PersonalizeFeeds {

    Date dateCreated
    Date lastUpdated

    String imeiNumber
    String modelNumber
    Feed feed
    PersonalizeType type

    enum PersonalizeType{
        LIKE,
        BOOKMARK,
        SHARE
    }

}
