package com.nvk.cinemav.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter

@NoArgsConstructor
public class PaymentUrlDTO {
  @JsonProperty("email")
  private String email;

  @JsonProperty("url")
  private String url;

  @JsonCreator
  public PaymentUrlDTO(@JsonProperty("email") String email,
      @JsonProperty("url") String url) {
    this.email = email;
    this.url = url;
  }
}
