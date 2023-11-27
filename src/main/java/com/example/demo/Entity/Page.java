package com.example.demo.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class Page {
    @Id
    @GeneratedValue
    private Integer pageId;

    private String pageName;

    private String pageDescription;

    private String actionIds;
}
