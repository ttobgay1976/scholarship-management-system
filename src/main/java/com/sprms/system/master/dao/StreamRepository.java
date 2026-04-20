package com.sprms.system.master.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sprms.system.hbmbeans.Stream;

@Repository
public interface StreamRepository extends JpaRepository<Stream, Long> {

    // You can add custom finder if needed
    Stream findByStreamName(String streamName);
}
