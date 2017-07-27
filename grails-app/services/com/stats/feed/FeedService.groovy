package com.stats.feed

import com.stats.dto.DateUtil
import com.stats.tag.TagService
import com.stats.util.Status
import com.stats.util.UtilityService

import java.text.SimpleDateFormat

/**
 * Created by admin on 7/20/2017.
 */
class FeedService {

    UtilityService utilityService
    TagService tagService
    def dataSource

    def create(def params) throws Exception{

        Feed feed = new Feed()
        feed.data = params.data
        feed.title = params.title
        feed.language = getLanguage(params.language)
        feed.country = params.country
        feed.sequence = utilityService.getSequenceNumber()

        Category category = Category.findById(params.categoryId)

        if(!category){
            throw new Exception("Incorrect category id.")
        }
        feed.category = category
        if(params.date){
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(params.date)
            feed.date = date
        } else {
            feed.date = DateUtil.today()
        }
        feed.status = Status.ACTIVE
        feed.resourceList = createResourceList(params.resourceUrl, feed)
        feed.feedTagList = createFeedTagList(params.tags, feed)
        feed.type = Feed.FeedType.TEXT_IMAGE

        feed.lastUpdated = new Date()
        feed.dateCreated = new Date()

        if(feed.validate()){
            feed.save()
        } else {
            feed.errors.allErrors.each { log.error it}
            throw new Exception("Error saving object Feed.")
        }

        def resp = ['status':"Success", 'alertMessage' : "Feed created successfully."]

        return resp
    }

    def getLanguage(def language){

        if('hindi'.equalsIgnoreCase(language)){
            return Feed.Language.HINDI
        }

        return Feed.Language.ENGLISH
    }

    def createResourceList(def urlList, Feed feed) throws Exception{

        List<Resource> resourceList = new ArrayList<Resource>()

        if(urlList){
            urlList?.each{
                it->
                    Resource resource = new Resource()
                    resource.lastUpdated = new Date()
                    resource.dateCreated = new Date()
                    resource.resourceUrl = it
                    resource.feed = feed

                    resourceList << resource
            }
        }

        return resourceList
    }

    def createFeedTagList(def tagList, Feed feed) throws Exception{

        List<FeedTag> feedList = new ArrayList<FeedTag>()
        tagList?.each{
            it->
                Tag tag = tagService.addTag(it)
                FeedTag feedTag = new FeedTag()
                feedTag.feed = feed
                feedTag.tag = tag
                feedTag.dateCreated = new Date()
                feedTag.lastUpdated = new Date()

                feedList << feedTag
        }

        return feedList
    }

    def tagFeed(Map params) throws Exception{

        def tagList = params.tagList
        def feedId = params.feedId

        Feed feed = Feed.findById(feedId)
        if(!feed){
            throw new Exception("Unable to restore feed with id $feedId ")
        }

        return tagFeed(tagList, feed)
    }

    def tagFeed(List tagList, Feed feed) throws Exception{

        List<FeedTag> feedList = feed.feedTagList

        if(tagList) {
            tagList?.each {
                it ->
                    Tag tag = tagService.addTag(it)

                    FeedTag feedTag = new FeedTag()
                    feedTag.feed = feed
                    feedTag.tag = tag
                    feedTag.dateCreated = new Date()
                    feedTag.lastUpdated = new Date()

                    feedList << feedTag
            }

            feed.feedTagList = feedList
            feed.save()
        }
        return [alertMessage: "Successfully tagged."]
    }

    def searchFeed(def filters) throws Exception{

        def data = []
        if(!filters){
//            return data
        }
        def options = null
        def categoryType
        def feedTags
        def start = filters.start
        def limit = filters.limit
        if(filters && filters.sport){
            if(filters.sportType){
                categoryType = Category.findAllBySportAndType(filters.sport.toUpperCase(), filters.sportType.toUpperCase())
            } else {
                categoryType = Category.findAllBySport(filters.sport.toUpperCase())
            }
        }

        if(filters && filters.tags){
            def tags = Tag.findAllByNameInList(filters.tags)
            if(tags){
                feedTags = FeedTag.findAllByTagInList(tags)
            }
        }


        def query = Feed.where {
            status in Status.ACTIVE
        }
        if(categoryType){
            query = query.where {
                category in categoryType
            }
        }
        if(feedTags){
            query.where {
                feedTagList in feedTags
            }
        }
        if(filters && filters.mostShared){
            if(!options){
                options = [:]
            }
            options.put("order", "desc")
            options.put("sort", "shares")
        }
        if(filters && filters.mostLiked){
            if(!options){
                options = [:]
            }
            options.put("order", "desc")
            options.put("sort", "likes")
        }
        if(filters && filters.mostMarked){
            if(!options){
                options = [:]
            }
            options.put("order", "desc")
            options.put("sort", "bookmarks")
        }
        if(limit){
            options = [
                    max: limit as Integer, offset: ((start ?: '0') as Integer)
            ]
        }

        data = options ? query.list(options) : query.list()
        def totalCount = query.count()

        def result = []

        data?.each {
            it->
                Map map = [:]
                map.put("feedId", it.id)
                map.put("data", it.data)
                map.put("title", it.title)
                map.put("date", it.date)
                map.put("sequence", it.sequence)
                map.put("country", it.country)
                map.put("language", it.language)
                map.put("sport", it.category.sport)
                map.put("specialization", it.category.type)
                map.put("likes", it.likes)
                map.put("shares", it.shares)
                map.put("bookmarks", it.bookmarks)
                map.put("resources", it.resourceList.resourceUrl)
                map.put("feedTagList", it.feedTagList.tag.name)
                map.put("status", it.status.toString())

                result << map
        }

        if(filters && !(filters.mostLiked || filters.mostMarked || filters.mostShared)){
            result = result.sort{  it.sequence }.reverse()
        }

        def response = [data : result, totalCount : totalCount]

        return response
    }

    def personalizeFeed(def params) throws Exception{

        def feedId = params.feedId
        def imeiNumber = params.imeiNumber
        def modelNumber = params.modelNumber

        Feed feed = Feed.findById(params.feedId)

        if(!feed){
            throw new Exception("Unable to restore feed with id $feedId.")
        }

        PersonalizeFeeds personalizeFeeds = new PersonalizeFeeds()

        personalizeFeeds.feed = feed
        personalizeFeeds.modelNumber = modelNumber
        personalizeFeeds.imeiNumber = imeiNumber
        personalizeFeeds.type = PersonalizeFeeds.PersonalizeType.valueOf(params.type.toUpperCase())
        if(personalizeFeeds.validate()){
            personalizeFeeds.save()
        } else {
            personalizeFeeds.errors.allErrors.each { log.error it}
            throw new Exception("Error saving object PersonalizeFeeds.")
        }

        if("share".equalsIgnoreCase(params.type)){
            feed.shares = feed.shares + 1
        } else if("like".equalsIgnoreCase(params.type)){
            feed.likes = feed.likes + 1
        } else if("bookmark".equalsIgnoreCase(params.type)){
            feed.bookmarks = feed.bookmarks + 1
        }
        feed.lastUpdated = new Date()
        if(params.type){
            feed.save()
        }

        def resp = [status: "success", alertMessage: "Activity successfully synced."]
        return resp
    }
}
