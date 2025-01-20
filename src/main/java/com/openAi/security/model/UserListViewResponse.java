package com.openAi.security.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/** UserListViewResponse. */
@Getter
@Setter
@Accessors(chain = true)
public class UserListViewResponse {

  private List<UserModel> userListViews = new ArrayList<>();
  private Long totalRecords;
  private Integer totalPages;
  private boolean hasNext = false;
  private boolean hasPrevious = false;
  private Integer page;
  private Integer size;
}
