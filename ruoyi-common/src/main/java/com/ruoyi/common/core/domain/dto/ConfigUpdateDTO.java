package com.ruoyi.common.core.domain.dto;

import lombok.Data;

@Data
public class ConfigUpdateDTO<T> {

    private String key;

    private T value;
}