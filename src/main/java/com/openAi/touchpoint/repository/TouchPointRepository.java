package com.openAi.touchpoint.repository;


import com.openAi.touchpoint.model.MeetingModel;
import com.openAi.utils.NestedRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TouchPointRepository  {
    private final DataSource dataSource;
    public List<MeetingModel> getTouchpointData(Integer customerId) {
        String sql = String.format("""
                select tm.id as meetingId,tm.meeting_type as meetingType,tm.touchpoint_date as touchPointDate,tmc.content as meetingContent from playbook.touchpoint_meeting tm join playbook.touchpoint_meeting_content tmc
                on tm.content_id = tmc.id
                where tm.id not in (select touchpoint_meeting_id from playbook.touchpoint_meeting_statuses) and tm.customer_id = %d
                """, customerId);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        RowMapper<MeetingModel> rowMapper = new NestedRowMapper<>(MeetingModel.class);
        return jdbcTemplate.query(sql, rowMapper);

    }
}
