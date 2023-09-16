package com.sjtu.rbj.bookstore.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Bojun Ren
 * @data 2023/06/21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserStatisticDTO {
    private Integer userId;

    /** Unix-style timestamp */
    private Long beginTimestamp;
    private Long endTimestamp;

    private List<BookOrderedDTO> bookOrderedList;
    private String totalCost;
}
