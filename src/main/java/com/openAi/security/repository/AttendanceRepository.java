package com.openAi.security.repository;


import com.openAi.security.entity.Attendance;
import com.openAi.security.model.MonthValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {

  @Query(value = """
            select distinct concat(a.year,"-",a.month) from playbook.attendance a
          order by a.year desc,a.month desc limit 1
          """, nativeQuery = true)
  String getLatestMonthAndYear();

  @Query(value = """
          SELECT * FROM attendance where 
          month in :month AND year=:year AND 
          (project_name like %:projectName% or project_name = :customerName)
          AND employee_grade != 'Contract' """, nativeQuery = true)
  List<Attendance> findAllByProjectName(Integer year, List<Integer> month, String projectName, String customerName);

    @Query(value = """
          SELECT * FROM attendance a where 
          STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') <=:endDate
          and (a.project_name like %:projectName% or a.project_name = :customerName)
          AND a.employee_grade != 'Contract' """, nativeQuery = true)
    List<Attendance> findAllByProjectNameAndDate(LocalDate startDate,LocalDate endDate, String projectName, String customerName);


    @Query(value = """
             select distinct a.* FROM playbook.attendance a           
             where a.month = :month and a.year = :year
             and a.employee_function not in ('IT','Administration','Account','Accounts','Account Management')
             AND a.email_id NOT IN (SELECT user_email_id FROM playbook.kra_user_survey WHERE kra_cycle_id= :configId)              
            """, nativeQuery = true)
  List<Attendance> findAllAttendanceSurvey(Integer year, Integer month,Integer configId);
    @Query(value = """
          SELECT * FROM attendance where 
          month in :month AND year=:year
          AND employee_grade != 'Contract' """, nativeQuery = true)
    List<Attendance> findAllByYearAndMonth(Integer year, List<Integer> month);


  @Query(value = """
          SELECT * FROM attendance where
          (project_name like %:projectName% or project_name = :customerName)
          AND employee_grade != 'Contract' """, nativeQuery = true)
  List<Attendance> findAllByProjectName(String projectName, String customerName);


  @Query(value = """
          SELECT * FROM attendance where 
          month in :month AND year=:year AND 
          (employee_function like %:function%)
          AND employee_grade != 'Contract' """, nativeQuery = true)
  List<Attendance> findAllByEmployeeFunction(Integer year, List<Integer> month, String function);

  @Query(value = """
            select a2.* FROM attendance a1 join attendance a2
            on a1.employee_name = a2.reporting_manager and a1.month = a2.month and a1.year = a2.year
            join (select * from attendance where employee_grade = 'M2' and employee_function like %:function% and month in (:month) and year=:year) as a3
            on a1.reporting_manager = a3.employee_name and a1.month = a3.month and a1.year = a3.year
            where a1.month in (:month) AND a1.year=:year
            union
            select a1.* FROM attendance a1
            join (select * from attendance where employee_grade = 'M2' and employee_function like %:function% and month in (:month) and year=:year) a2
            on a1.reporting_manager = a2.employee_name and a1.month = a2.month and a1.year = a2.year
            where a1.month in (:month) AND a1.year=:year        
          """, nativeQuery = true)
  List<Attendance> findAllByReportingManager(Integer year, List<Integer> month, String function);


 @Query(value= """
          select(
                    (select sum(story_points) from playbook.attendance_team at join playbook.attendance a
                    on at.attendance_id = a.id and a.email_id= :email
                    and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') <=:endDate and at.team_id in :teamId)
                    /
                    (select sum(story_points) from playbook.attendance_team at join playbook.attendance a
                    on at.attendance_id = a.id and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') <=:endDate and at.team_id in :teamId and a.employee_function
                    in (select a.employee_function from playbook.attendance_team at join playbook.attendance a
                    on at.attendance_id = a.id and a.email_id=:email
                    and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') <=:endDate )))*100 as Result
         """,nativeQuery = true)
  Double getOwnerShipByEffort(List<Integer> teamId, String email, LocalDate startDate, LocalDate endDate);


    @Query(value = """
        select sum(present_days) from attendance a
        where STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') <=:endDate\s
        and a.email_id =:email
        """,nativeQuery = true)
  Double getDevelopmentDays(String email, LocalDate startDate, LocalDate endDate);

  @Query(value= """
         select sum(logged_hours)/avg(working_hours)*100 from playbook.attendance_team at join playbook.attendance a
         on at.attendance_id = a.id and a.email_id= :email
         and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')<=:endDate and at.team_id in (:teamId)
         group by a.month,a.year
         """,nativeQuery = true)
 List<Double> getProductivity(List<Integer> teamId,String email, LocalDate startDate,LocalDate endDate);


    @Query(value= """
         select sum(logged_hours)/(8) from playbook.attendance_team at join playbook.attendance a
         on at.attendance_id = a.id and a.email_id= :email
         and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')<=:endDate and at.team_id in (:teamId)
         group by a.month,a.year
         """,nativeQuery = true)
    List<Double> getProductivityInDays(List<Integer> teamId,String email, LocalDate startDate,LocalDate endDate);

    @Query(value= """
         select sum(logged_hours)/avg(working_hours)*100 as value,a.month,a.year from playbook.attendance_team at join playbook.attendance a
         on at.attendance_id = a.id and a.email_id= :email
         and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')<=:endDate and at.team_id in (:teamId)
         group by a.month,a.year
         """,nativeQuery = true)
    List<MonthValue> getProductivityValues(List<Integer> teamId, String email, LocalDate startDate, LocalDate endDate);


    @Query(value= """
         select sum(story_points) * 22 /sum(a.present_days)  from playbook.attendance_team at join playbook.attendance a
         on at.attendance_id = a.id and a.email_id= :email
         and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')<=:endDate and at.team_id in (:teamId)
         group by a.month,a.year
         """,nativeQuery = true)
    List<Double> getStoryPoints(List<Integer> teamId,String email, LocalDate startDate,LocalDate endDate);


    @Query(value= """
         select sum(story_points) from playbook.attendance_team at join playbook.attendance a
         on at.attendance_id = a.id and a.email_id= :email
         and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')>=:startDate and STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d')<=:endDate and at.team_id in (:teamId)
         group by a.month,a.year
         """,nativeQuery = true)
    List<Double> getTotalStoryPoints(List<Integer> teamId,String email, LocalDate startDate,LocalDate endDate);

    @Query(value= """
         select (sum(COALESCE(bug_fixes_story_points, 0) + COALESCE(enhancements_story_points, 0) + COALESCE(new_features_story_points, 0))/(sum(at.working_hours)/8))*22 sp
         from playbook.attendance_team at
         join playbook.attendance a on at.attendance_id = a.id
         and a.month = :month and a.year = :year and at.team_id = :teamId
         """,nativeQuery = true)
    Double getVelocityByTeamAndMonthAndYear(Integer teamId, Integer month, Integer year);

  @Query(value = """
            SELECT * FROM attendance where 
            month in :month AND year=:year AND 
            (employee_grade like 'A%')
            AND employee_grade != 'Contract' """, nativeQuery = true)
    List<Attendance> findAllByEmployeeGrade(Integer year, List<Integer> month);

    @Query(value = "SELECT at FROM Attendance at where at.month=:month AND at.year=:year AND at.employeeName IN :employeeNames")
    List<Attendance> getByMonthAndYearAndEmployeeNameIn(Integer month, Integer year, List<String> employeeNames);

  @Query(value = "select * from attendance where email_id= :emailId and ((month >= :startMonth and year= :startYear) or (month <= :endMonth and year= :endYear)) order by year,month desc", nativeQuery = true)
  List<Attendance> findByEmployeeEmailAndYearMonth(String emailId, Integer startMonth, Integer startYear, Integer endMonth, Integer endYear);

  @Query(value = "select * from attendance where email_id= :emailId and month >= :startMonth and month <= :endMonth and year= :year order by year,month desc", nativeQuery = true)
  List<Attendance> findByEmployeeEmailAndYearMonth(String emailId, Integer startMonth, Integer endMonth, Integer year);

  List<Attendance> findByMonthAndYearAndFirstNameAndLastName(Integer month, Integer year, String firstName, String lastName);

  @Query(value = """
          SELECT * FROM attendance where
          (project_name like %:projectName% or project_name = :customerName)
          AND first_name = :firstName
          AND last_name = :lastName
          AND employee_grade != 'Contract'
          """, nativeQuery = true)
  List<Attendance> findByProjectNameAndFirstNameAndLastName(String projectName, String customerName, String firstName, String lastName);

  List<Attendance> findByMonthAndYearAndLastName(Integer month, Integer year, String lastName);

  List<Attendance> findByMonthAndYearAndLastNameAndProjectName(Integer month, Integer year, String lastName,String projectName);

  List<Attendance> findByMonthAndYearAndFirstNameAndProjectName(Integer month, Integer year, String firstName,String projectName);


  List<Attendance> findByEmailIdOrderByYearDescMonthDesc(String email);

    @Query(value = "select * from attendance where reporting_manager= :name and month = :month and year= :year", nativeQuery = true)
    List<Attendance> findByReportingManagerOrderByYearDescMonthDesc(String name,Integer year,Integer month);

  @Query(value = "select * from attendance where email_id= :email and month = :month and year= :year Limit 1", nativeQuery = true)
  Attendance findByEmailAndYearAndMonth(String email, Integer year, Integer month);

  @Query(value = "select * from attendance where month = :month and year= :year Limit 5", nativeQuery = true)
  List<Attendance> findByYearAndMonth(Integer year, Integer month);

  @Modifying
  @Transactional
  @Query(value = "delete FROM attendance where month = :month and year = :year", nativeQuery = true)
  void deleteByMonthAndYear(Integer month, Integer year);

  @Modifying
  @Transactional
  @Query(value = "delete FROM attendance_team where attendance_id in (select id from attendance where month = :month and year = :year)", nativeQuery = true)
  void deleteByMonthAndYearAttendanceTeam(Integer month, Integer year);

  @Query(value = "SELECT DISTINCT a.* FROM attendance AS a JOIN customers AS c ON a.project_name LIKE CONCAT('%', SUBSTRING(c.project_name, 1, LENGTH(a.project_name)), '%') WHERE c.id = :customerId AND ((a.year = :startYear AND a.month >= :startMonth) AND (a.year = :endYear AND a.month <= :endMonth));", nativeQuery = true)
  List<Attendance> findByCustomerIdAndDateRanges(Long customerId, int startYear, int startMonth, int endYear,
                                                 int endMonth);

  @Query(value = "SELECT DISTINCT a.* FROM attendance AS a WHERE a.id IN( SELECT attendance_id FROM attendance_team WHERE team_id in :teamId ) AND ((month >= :startMonth and year= :startYear) or (month <= :endMonth and year= :endYear));", nativeQuery = true)
  List<Attendance> findByTeamIdAndDateRanges(List<Integer> teamId, int startYear, int startMonth, int endYear, int endMonth);

  @Query(value = "SELECT DISTINCT a.* FROM attendance AS a WHERE a.id IN( SELECT attendance_id FROM attendance_team WHERE team_id in :teamId ) AND (month >= :startMonth and year= :startYear and month <= :endMonth);", nativeQuery = true)
  List<Attendance> findByTeamIdAndDateRanges(List<Integer> teamId, int startYear, int startMonth, int endMonth);

  @Query(value = "SELECT DISTINCT a.employee_code,a.employee_name, a.employee_grade, a.employee_function ,at.team_id FROM attendance a join attendance_team at on a.id = at.attendance_id join team t on at.team_id = t.id WHERE ((month >= :startMonth and year= :startYear) or (month <= :endMonth and year= :endYear));", nativeQuery = true)
  List<Map<String, Object>> findByDateRanges(int startYear, int startMonth, int endYear, int endMonth);

  @Query(value = "SELECT DISTINCT a.employee_code,a.employee_name, a.employee_grade, a.employee_function ,at.team_id FROM attendance a join attendance_team at on a.id = at.attendance_id join team t on at.team_id = t.id WHERE (month >= :startMonth and year= :startYear and month <= :endMonth);", nativeQuery = true)
  List<Map<String, Object>> findByDateRanges(int startYear, int startMonth, int endMonth);

  @Query(value = "SELECT COALESCE(SUM(new_features),0) FROM attendance_team at JOIN attendance a ON at.attendance_id = a.id WHERE at.team_id = :teamId AND ((a.year = :year AND a.month = :month))", nativeQuery = true)
  Integer findNewsFeaturesCountForDate(Long teamId, int year, int month);

  @Query(value = "SELECT COALESCE(SUM(enhancements),0) FROM attendance_team at JOIN attendance a ON at.attendance_id = a.id WHERE at.team_id = :teamId and ((a.year = :year AND a.month = :month))", nativeQuery = true)
  Integer findEnhancementCountForDate(Long teamId, int year, int month);

  @Query(value = "SELECT COALESCE(SUM(bug_fixes),0) FROM attendance_team at JOIN attendance a ON at.attendance_id = a.id WHERE at.team_id = :teamId AND ((a.year = :year AND a.month = :month))", nativeQuery = true)
  Integer findBugsFixesCountForDate(Long teamId, int year, int month);

  @Query(value = """
          select
                    (select sum(bug_fixes+enhancements+new_features) from playbook.attendance_team at
                            join playbook.attendance a on at.attendance_id = a.id
                            where a.month >= :startMonth and a.month < :endMonth and year = :year)/
                      ((select count(distinct(a.month)) from playbook.attendance_team at
                            join playbook.attendance a on at.attendance_id = a.id
                            where a.month >= :startMonth and a.month < :endMonth and year = :year)*
                        (select count(distinct(at.team_id)) from playbook.attendance_team at
                            join playbook.attendance a on at.attendance_id = a.id
                            where a.month >= :startMonth and a.month < :endMonth and year = :year)
                      ) as talenticaAvg
      """, nativeQuery = true)
  Double findAverageTrendCount(int year, int startMonth, int endMonth);

  @Query(value = """
               select
              (select avg(story_points) from (select sum(bug_fixes_story_points + enhancements_story_points + new_features_story_points) as story_points from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where ((a.month >= :startMonth and year= :startYear) or (a.month < :endMonth and year = :endYear))) as storyPoints)/
              ((select  count(distinct(a.month)) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where ((a.month >= :startMonth and year= :startYear) or (a.month < :endMonth and year = :endYear))
              )*(select  count(distinct(at.team_id)) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where ((a.month >= :startMonth and year= :startYear) or (a.month < :endMonth and year = :endYear)))) as teamAvg
          """, nativeQuery = true)
  Double findAverageTrendCount(int startYear, int endYear, int startMonth, int endMonth);

  @Query(value = """
              select
              (select avg(story_points) from (select sum(bug_fixes_story_points + enhancements_story_points + new_features_story_points) as story_points from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where a.month >= :startMonth and a.month < :endMonth and year = :year and at.team_id in :teamIds 
              ) as storyPoints )/
              ((select  count(distinct(a.month)) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where a.month >= :startMonth and a.month < :endMonth and year = :year and at.team_id in :teamIds
              )*(select  count(distinct(a.email_id)) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where a.month >= :startMonth and a.month < :endMonth and year = :year and at.team_id in :teamIds)) as teamAvg
          """, nativeQuery = true)
  Double findAverageTrendCountByTeam(int year, int startMonth, int endMonth, List<Integer> teamIds);

  @Query(value = """
              select
              (select sum(bug_fixes_story_points+enhancements_story_points+new_features_story_points) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where ((a.month >= :startMonth and year= :startYear) or (a.month < :endMonth and year = :endYear)) and at.team_id in :teamIds
              )/
              ((select  count(distinct(a.month)) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where ((a.month >= :startMonth and year= :startYear) or (a.month < :endMonth and year = :endYear)) and at.team_id in :teamIds
              )*(select  count(distinct(a.email_id)) from playbook.attendance_team at
              join playbook.attendance a on at.attendance_id = a.id
              where ((a.month >= :startMonth and year= :startYear) or (a.month < :endMonth and year = :endYear)) and at.team_id in :teamIds)) as teamAvg
          """, nativeQuery = true)
  Double findAverageTrendCountByTeam(int startYear, int endYear, int startMonth, int endMonth, List<Integer> teamIds);

  @Query(value = """
      SELECT DISTINCT a.* FROM attendance AS a
      where a.project_name = :projectName and month >= :startMonth and month<= :endMonth and year = :year 
      and employee_name in :employee 
      """, nativeQuery = true)
  List<Attendance> findByProjectNameAndDateRangesAndEmployee(String projectName, Integer startMonth, Integer endMonth, Integer year, List<String> employee);

    @Query(value = """
            SELECT DISTINCT a.* FROM attendance AS a
                where month >= :startMonth and month<= :endMonth and year = :year
                and email_id in :email and (employee_grade in ('M1','M2','M3') or employee_grade like 'A%')
                """, nativeQuery = true)
    List<Attendance> findByProjectNameAndDateRangesAndEmail(Integer startMonth, Integer endMonth, Integer year, List<String> email);

    List<Attendance> findByMonthAndYearAndEmailIdIn(Integer month, Integer year, List<String> emailIds);

    @Query(value = """
            SELECT a.year, a.month, COALESCE(SUM(new_features_story_points), 0) AS new_features,
            COALESCE(SUM(enhancements_story_points), 0) AS enhancements,
            COALESCE(SUM(bug_fixes_story_points), 0) AS bug_fixes,
            Avg(new_features_story_points+enhancements_story_points+bug_fixes_story_points) as normalizedCount
            FROM attendance_team at JOIN attendance a
            ON at.attendance_id = a.id WHERE at.team_id in (:teamId)
            AND STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') >= STR_TO_DATE(CONCAT(:startYear,'-',:startMonth,'-',1), '%Y-%m-%d')\s
            AND STR_TO_DATE(CONCAT(a.year,'-',a.month,'-',1), '%Y-%m-%d') <= STR_TO_DATE(CONCAT(:endYear,'-',:endMonth,'-',1), '%Y-%m-%d')
            GROUP BY a.year, a.month
            """
            , nativeQuery = true)
    List<Object[]> findCountsForDateRange(List<Integer> teamId, int startYear, int startMonth, int endYear, int endMonth);


    @Query(value = """
             SELECT a.year, a.month, COALESCE(SUM(new_features_story_points), 0) AS new_features,
             COALESCE(SUM(enhancements_story_points), 0) AS enhancements,
             COALESCE(SUM(bug_fixes_story_points), 0) AS bug_fixes,
             Avg(new_features_story_points+enhancements_story_points+bug_fixes_story_points) as normalizedCount
             FROM attendance_team at JOIN attendance a
             ON at.attendance_id = a.id WHERE a.employee_function like %:customerName%
             AND (((:startYear = :endYear) AND ((a.month >= :startMonth AND a.month <= :endMonth) 
             AND a.year = :startYear)) OR ((:startYear != :endYear) AND ((a.year >= :startYear AND a.month >= :startMonth) OR (a.year <= :endYear AND a.month <= :endMonth)))) GROUP BY a.year, a.month;"""
            , nativeQuery = true)
    List<Object[]> findCountsForDateRangeTegCustomer(String customerName, int startYear, int startMonth, int endYear, int endMonth);
    @Query(value = """
            SELECT a.year, a.month, COALESCE(SUM(new_features_story_points), 0) AS new_features,
                                                                         COALESCE(SUM(enhancements_story_points), 0) AS enhancements,
                                                                         COALESCE(SUM(bug_fixes_story_points), 0) AS bug_fixes FROM attendance_team at\s
              JOIN
              (select a2.* FROM attendance a1 join attendance a2
              on a1.employee_name = a2.reporting_manager
              and a1.reporting_manager in (select distinct employee_name from attendance where employee_grade = 'M2' and employee_function like %:customerName%)
              where STR_TO_DATE(CONCAT('01-', a1.month,'-',a1.year), '%d-%m-%Y') >= STR_TO_DATE(CONCAT('01-', :startMonth,'-',:startYear), '%d-%m-%Y')
              AND STR_TO_DATE(CONCAT('01-', a1.month,'-',a1.year), '%d-%m-%Y') <= STR_TO_DATE(CONCAT('01-', :endMonth,'-',:endYear), '%d-%m-%Y')
              AND STR_TO_DATE(CONCAT('01-', a2.month,'-',a2.year), '%d-%m-%Y') >= STR_TO_DATE(CONCAT('01-', :startMonth,'-',:startYear), '%d-%m-%Y')
              AND STR_TO_DATE(CONCAT('01-', a2.month,'-',a2.year), '%d-%m-%Y') <= STR_TO_DATE(CONCAT('01-', :endMonth,'-',:endYear), '%d-%m-%Y')           \s
              union
              select a1.* FROM attendance a1
              where STR_TO_DATE(CONCAT('01-', a1.month,'-',a1.year), '%d-%m-%Y') >= STR_TO_DATE(CONCAT('01-', :startMonth,'-',:startYear), '%d-%m-%Y')
              AND STR_TO_DATE(CONCAT('01-', a1.month,'-',a1.year), '%d-%m-%Y') <= STR_TO_DATE(CONCAT('01-', :endMonth,'-',:endYear), '%d-%m-%Y')
              AND a1.reporting_manager in (select distinct employee_name from attendance where employee_grade = 'M2' and employee_function like %:customerName%))as a                 \s
            ON at.attendance_id = a.id
            GROUP BY a.year, a.month ;                    
            """
            , nativeQuery = true)
    List<Object[]> findCountsForDateRangeTegOpsCustomer(String customerName, int startYear, int startMonth, int endYear, int endMonth);

    @Query(value = """
            SELECT a.year, a.month, COALESCE(SUM(new_features_story_points), 0) AS new_features,
             COALESCE(SUM(enhancements_story_points), 0) AS enhancements, 
             COALESCE(SUM(bug_fixes_story_points), 0) AS bug_fixes,
             Avg(new_features_story_points+enhancements_story_points+bug_fixes_story_points) as normalizedCount
             FROM attendance_team at JOIN attendance a 
             ON at.attendance_id = a.id WHERE a.employee_grade like 'A%'
             AND (((:startYear = :endYear) AND ((a.month >= :startMonth AND a.month <= :endMonth) 
             AND a.year = :startYear)) OR ((:startYear != :endYear) AND ((a.year >= :startYear AND a.month >= :startMonth) OR (a.year <= :endYear AND a.month <= :endMonth)))) GROUP BY a.year, a.month;"""
            , nativeQuery = true)
    List<Object[]> findCountsForDateRangeTegCustomerArchitects(int startYear, int startMonth, int endYear, int endMonth);

    @Query(value = "select a.year, a.month, at.team_id from attendance a join attendance_team at on at.attendance_id = a.id", nativeQuery = true)
    List<Map<String, Object>> getRecentRecord();


  @Query(value = """
            SELECT SUM(bug_fixes_story_points+enhancements_story_points+new_features_story_points)
            FROM attendance_team atm JOIN attendance a
            ON atm.attendance_id = a.id
            WHERE atm.team_id in :teamId AND month = :month AND year = :year
            GROUP BY a.month, a.year
            """
          , nativeQuery = true)
  Double getAvgStoryPoints(List<Integer> teamId, int month, int year);

    @Query(value = """
            SELECT distinct t.team_name
            FROM attendance_team atm JOIN attendance a
            ON atm.attendance_id = a.id
            JOIN team t on atm.team_id = t.id
            WHERE a.email_id = :email and (year, month) >= (:startYear, :startMonth)
            and (year, month) <= (:endYear, :endMonth)
            """
            , nativeQuery = true)
  List<String> getTeamHistory(String email, int startMonth, int startYear, int endMonth, int endYear);
}
