import com.stats.feed.Feed
import com.stats.feed.FeedService
import com.stats.tag.TagService
import com.stats.util.UtilityService
import grails.converters.JSON


/**
 * Created by nkunturkar on 7/20/2017.
 */
class FeedController {

    UtilityService utilityService
    FeedService feedService
    NotificationService notificationService
    TagService tagService

    def create(){

        try {

            def result = feedService.create(request.JSON)
            render result as JSON
        } catch (Exception e){
            log.info(e.printStackTrace())
            response.status = 500
            render utilityService.createError("500", e.getMessage()) as JSON
        }

    }

    def feedNotification(){

        try{
            def feedId = request.JSON.feedId

            Feed feed = Feed.findById(feedId)

            def resp = notificationService.sendNotification(feed, ["dwWl3xWMuN4:APA91bFKZaElu8Eeg9EbXU2xlK8L-zbKAdqQdqGx8m2vmcWXuUk7AZTuL1MGX8q-0S0VoyTSjdtG1FCxVsqo8fIjbgQr6CvO6XGvjYUY4JC3NOtHelkSmrqwSDAkuKJUidB3WgEI1d0s"],
                "Hi Mahesh", "If you see this message, let me know.", "")
            render resp as JSON
        } catch (Exception e){
            log.info(e.printStackTrace())
            response.status = 500
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def addTag(){

        try{
            def resp = feedService.tagFeed(request.JSON)
            render resp as JSON
        }catch (Exception e){
            log.info(e.printStackTrace())
            response.status = 500
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def searchFeed(){

        try {
            def resp = feedService.searchFeed(request.JSON)
            render resp as JSON
        }catch (Exception e){
            log.info(e.printStackTrace())
            response.status = 500
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def personalizeFeed(){

        try {
            def resp = feedService.personalizeFeed(request.JSON)
            render resp as JSON
        }catch (Exception e){
            log.info(e.printStackTrace())
            response.status = 500
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }
}
