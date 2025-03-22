package com.smartparkingupc.http.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@AllArgsConstructor
@Data
@Builder
public class RelatedUsersResponse {
	List<UserEntityByWatchmanResponse> users;
	Boolean isParked;
}

