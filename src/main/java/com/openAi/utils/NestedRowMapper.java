package com.openAi.utils;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

public class NestedRowMapper<T> implements RowMapper<T> {

  private final Class<T> mappedClass;

  public NestedRowMapper(Class<T> mappedClass) {
    this.mappedClass = mappedClass;
  }

  @Override
  @SneakyThrows
  public T mapRow(ResultSet rs, int rowNum) {

    T mappedObject = BeanUtils.instantiateClass(this.mappedClass);
    BeanWrapper beanWrapper = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);

    beanWrapper.setAutoGrowNestedPaths(true);

    ResultSetMetaData metaData = rs.getMetaData();
    int columnCount = metaData.getColumnCount();

    for (int index = 1; index <= columnCount; index++) {
      String column = JdbcUtils.lookupColumnName(metaData, index);
      Object value =
              JdbcUtils.getResultSetValue(rs, index, Class.forName(metaData.getColumnClassName(index)));

      beanWrapper.setPropertyValue(column, value);
    }
    return mappedObject;
  }
}
