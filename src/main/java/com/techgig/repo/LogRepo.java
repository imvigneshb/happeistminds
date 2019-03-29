package com.techgig.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.techgig.model.Log;

public interface LogRepo extends JpaRepository<Log, Long> {

}
