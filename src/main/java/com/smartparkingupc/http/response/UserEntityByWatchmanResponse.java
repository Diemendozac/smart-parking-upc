package com.smartparkingupc.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntityByWatchmanResponse {
  private String name;
  private String email;
  private String phoneNumber;
  private String photoUrl;
}
