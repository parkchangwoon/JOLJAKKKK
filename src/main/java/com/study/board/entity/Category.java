package com.study.board.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    private String rating;

    private String store; // 각 카테고리의 점포 정보를 저장하는 필드


    // 생성자, getter 및 setter 메서드 생략

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }
}