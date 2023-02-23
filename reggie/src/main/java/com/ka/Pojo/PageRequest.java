package com.ka.Pojo;

import lombok.Data;

@Data
public class PageRequest {
   private int page;
   private int pageSize;
   private String name;
}
