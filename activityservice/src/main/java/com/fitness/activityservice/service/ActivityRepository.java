package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ActivityRepository  extends MongoRepository<Activity, String> {
    List<Activity> findByUserId(String userId);
}
