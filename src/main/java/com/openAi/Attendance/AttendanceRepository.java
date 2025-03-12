package com.openAi.Attendance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

    List<Attendance> findByEmailIdOrderByYearDescMonthDesc(String email);

}
