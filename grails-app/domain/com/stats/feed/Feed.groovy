package com.stats.feed

import com.stats.util.Status

/**
 * Created by admin on 7/13/2017.
 */
class Feed {

    Date dateCreated
    Date lastUpdated

    Integer likes=0
    Integer shares=0
    Integer bookmarks=0

    String data
    String title
    FeedType type
    Date date
    String country
    Integer sequence
    Category category
    Language language
    Status status

    List<Resource> resourceList
    List<FeedTag> feedTagList
    static hasMany = [resourceList : Resource, feedTagList : FeedTag]

    static enum Language{
        ENGLISH,
        HINDI
    }

    static constraints = {
        data nullable: true
        country nullable: true
        category nullable: true
    }

    enum FeedType{
        IMAGE,
        TEXT_IMAGE,
        VIDEO,
        ANIMATION,
        INFO,
        ADVERTISEMENT
    }

}
