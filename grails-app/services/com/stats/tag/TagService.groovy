package com.stats.tag

import com.stats.feed.Tag

/**
 * Created by admin on 7/20/2017.
 */
class TagService {

    def addTag(def tag) throws Exception{

        Tag entry = Tag.findByName(tag)

        if(!entry){
            entry = new Tag()
            entry.name = tag
            entry.dateCreated = new Date()
            entry.lastUpdated = new Date()

            entry.save()
        }

        return entry
    }

    def addTagList(def tagList) throws Exception{

        def newTagList = [:]

        tagList?.each{
            it->
                Tag entry = Tag.findByName(it)

                if(!entry){
                    entry = new Tag()
                    entry.name = it
                    entry.dateCreated = new Date()
                    entry.lastUpdated = new Date()

                    newTagList << entry
                }
        }

        Tag.saveAll(newTagList)
    }
}
