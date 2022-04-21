package com.springtest.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page<T> {
    public int currentPage;
    public int maxPage;
    public int pageSize;
    public List<T> data;
}
