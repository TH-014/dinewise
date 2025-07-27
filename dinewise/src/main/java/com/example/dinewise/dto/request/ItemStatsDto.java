package com.example.dinewise.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemStatsDto {
    private String itemName;
    private int lunchCount;
    private int dinnerCount;
}
