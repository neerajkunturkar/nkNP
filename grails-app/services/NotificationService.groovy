import com.stats.feed.Feed
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.util.EntityUtils
import org.codehaus.groovy.grails.web.json.JSONArray
import org.codehaus.groovy.grails.web.json.JSONObject

/**
 * Created by admin on 7/20/2017.
 */
class NotificationService {

    def sendNotification(Feed feed, List regIds, def title, def text, def type = '') throws Exception {

        String url = "https://fcm.googleapis.com/fcm/send"
        HttpClient client = new DefaultHttpClient(); // HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(url);
        post.setHeader("Content-Type","application/json")
        def authKey = "AIzaSyASzfs_PeZx_0DJpRKHciCMt8NErvKxtE4"
        if(authKey) {
            post.setHeader("Authorization", "key=" + authKey)

            def data = new JSONObject();
            data.put('title', title)
            data.put('body' , text)
            data.put('sound', 'default')

            def newData = new JSONObject();
            newData.put('type', type)
            JSONObject jsonObject = new JSONObject()
            jsonObject.put("registration_ids", new JSONArray(regIds))
            jsonObject.put("notification", data)
            //jsonObject.put("data", newData)

            post.setEntity(new StringEntity(jsonObject.toString()))

            HttpResponse response = client.execute(post);
            def resp = ['respCode': response.getStatusLine().getStatusCode(), 'responseText': "SentMessage"/*EntityUtils.toString(response.getEntity())*/]
            log.info(" : " + EntityUtils.toString(response.getEntity()))
            return resp
        }
        else {
            return ['respCode': 'Cannot send notification.']
        }
    }

}
