package com.nvk.cinemav.dto;

import com.nvk.cinemav.entity.Province;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProvinceDTO {
  private int id;
  private String name;

  public ProvinceDTO(Province province) {
    this.id = province.getId();
    this.name = province.getName();
  }
}

