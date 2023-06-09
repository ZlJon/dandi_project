package com.back.endTest.domain.search.dao;

import com.back.endTest.domain.entity.JobBoard;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SearchDAOImpl implements SearchDAO {

  private final NamedParameterJdbcTemplate template;


  //검색-텍스트 입력
  @Override
  public List<JobBoard> searchWord(String keyword, int startRec, int endRec) {
    StringBuffer sql = new StringBuffer();
    sql.append(" select t2.* ");
    sql.append(" from(select rownum no, job_board_id_pk, ");
    sql.append(" title_job, id_job, ");
    sql.append(" salary_way, salary_amount, come_in_job, ");
    sql.append(" come_out_job, place_name, place_address ");
    sql.append(" from job_board ");
    sql.append(" where title_job like '%'||:keyword||'%'");
    sql.append(" order by job_board_id_pk DESC) t2 ");
    sql.append(" where no between :startRec and :endRec ");

    try {
      SqlParameterSource param = new MapSqlParameterSource()
        .addValue("keyword", keyword)
        .addValue("startRec", startRec)
        .addValue("endRec", endRec);

      List<JobBoard> searchWordAll = template.query(
        sql.toString(),
        param,
        BeanPropertyRowMapper.newInstance(JobBoard.class)
      );

      return searchWordAll;
    } catch (EmptyResultDataAccessException e) {

      return null;
    }
  }



}
