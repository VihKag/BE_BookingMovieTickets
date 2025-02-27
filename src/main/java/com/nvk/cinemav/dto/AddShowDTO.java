package com.nvk.cinemav.dto;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AddShowDTO {
  private UUID movieId;
  private Integer screenId;
  private Date time;
}
