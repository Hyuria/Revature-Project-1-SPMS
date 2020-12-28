package com.revature.service;

import com.revature.beans.Story;

import java.util.Set;

public interface StoryService {

    public Integer addStory(Story story);

    public Story getStoryById(Integer id);

    public Set<Story> getStories();


}
