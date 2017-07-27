import com.stats.feed.ImgTable
import com.stats.test.Pojo
import com.stats.util.UtilityService
import grails.converters.JSON
import sun.misc.IOUtils

import javax.imageio.ImageIO
import java.awt.image.BufferedImage
import java.sql.Blob

/**
 * Created by admin on 7/13/2017.
 */
class HelloController {

    UtilityService utilityService

    def testMethod(){

        try {
            def resp = [alertMessage : "Hello World."]
            render resp as JSON
        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def create(){

        try {
            def map = request.JSON

            Pojo pojo = new Pojo()
            pojo.name = map.name
            pojo.age = map.age
            pojo.address = map.address

            pojo.save()

            def resp = [id: pojo.id, alertMessage: "Created data successfully."]

            render resp as JSON
        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def data(){

        try {
            def id = request.JSON.id
            Pojo pojo = Pojo.findById(id)

            if(!pojo){
                throw new Exception("Unable to restore data with id $id.")
            }

            def map = [:]

            map.put("id", pojo.id)
            map.put("name", pojo.name)
            map.put("age", pojo.age)
            map.put("address", pojo.address)

            render map as JSON
        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def update(){

        try {
            def id = request.JSON.id
            Pojo pojo = Pojo.findById(id)

            if(!pojo){
                throw new Exception("Unable to restore data with id $id.")
            }

            if(request.JSON.name){
                pojo.name = request.JSON.name
            }
            if(request.JSON.age){
                pojo.age = request.JSON.age
            }
            if(request.JSON.address){
                pojo.address = request.JSON.address
            }

            pojo.save()
            def resp = [alertMessage: "Saved data successfully.", id : pojo.id]

            render resp as JSON
        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def delete(){

        try {

        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def uploadImage(){

        try {
            def imagePath = request.JSON.imagePath
            File file = new File(imagePath)
            if(!file.exists()){
                throw new Exception("Incorrect file path.")
            }
            Blob blob = new javax.sql.rowset.serial.SerialBlob(file.bytes);
            ImgTable imgTable = new ImgTable()
            imgTable.photo = blob
            imgTable.name = file.name

            imgTable.save()

            def resp = [alertMessage: "Successfully uploaded image."]

            render resp as JSON

        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }

    def getImage(){

        try {
            def imgName

            if(request.getHeader("Content-Type")?.contains("application/json") ||
                    request.getHeader("Content-Type")?.contains("text/plain")) {
                imgName = request.JSON.imageName
            } else {
                imgName = request.getParameter("imageName")
            }
            if(!imgName ){
                imgName = 'images.jpg'
            }

            ImgTable imgTable = ImgTable.findByName(imgName)
            if(!imgTable){
                throw new Exception("Image with name does not exits")
            }

//            File file = new File(imgTable.photo)

            def blob = imgTable.photo

            response.setContentType("image/jpeg")

            InputStream inputStream = imgTable.photo.getBinaryStream()
            OutputStream out = response.getOutputStream();

            byte[] buff = blob.getBytes(1,(int)blob.length());

//            BufferedImage bi = ImageIO.read(file);
//            ImageIO.write(bi, "jpg", out);
            out.write(buff)
            out.close()

            return
        } catch (Exception e){
            e.printStackTrace()
            render utilityService.createError("500", e.getMessage()) as JSON
        }
    }
}
