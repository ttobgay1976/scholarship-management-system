package com.sprms.system.core.services;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sprms.system.frmbeans.StudentMarksDTO;

@Service
public class StudentMarksServices {

	// this is used for the logging the error
	private static final Logger logger = LoggerFactory.getLogger(StudentMarksServices.class);
	
	//call the repository

	//this will calculate the average
	public Map<String, Double> calculateAverage(List<StudentMarksDTO> list) {

        return list.stream()
                .collect(Collectors.groupingBy(
                        StudentMarksDTO::getIndexNumber,
                        Collectors.averagingDouble(StudentMarksDTO::getMarks)
                ));
    }
	
}
