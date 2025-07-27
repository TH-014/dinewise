package com.example.dinewise.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.dinewise.dto.request.ItemStatsDto;
import com.example.dinewise.model.Menu;
import com.example.dinewise.repo.MenuRepository;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;
    public List<String> getDistinctItemsSince(LocalDate fromDate) {
    List<Menu> menus = menuRepository.findByMenuDateAfter(fromDate.minusDays(1));
    Set<String> allItems = new HashSet<>();
    for (Menu menu : menus) {
        allItems.addAll(menu.getLunchItems());
        allItems.addAll(menu.getDinnerItems());
    }
    return new ArrayList<>(allItems);
}

public List<ItemStatsDto> getItemStatsSince(LocalDate fromDate, List<String> itemNames) {
    List<Menu> menus = menuRepository.findByMenuDateAfter(fromDate.minusDays(1));
    List<ItemStatsDto> stats = new ArrayList<>();

    for (String item : itemNames) {
        int lunchCount = 0;
        int dinnerCount = 0;
        for (Menu menu : menus) {
            if (menu.getLunchItems().contains(item)) lunchCount++;
            if (menu.getDinnerItems().contains(item)) dinnerCount++;
        }
        stats.add(new ItemStatsDto(item, lunchCount, dinnerCount));
    }

    return stats;
}

    
}
