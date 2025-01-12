package com.demo.loadbalancer.algorithm;

import com.demo.loadbalancer.model.Constant;

public enum AlgorithmType {
    ROUND_ROBIN(Constant.ROUND_ROBIN), RANDOM(Constant.RANDOM);
    private final String value;

    AlgorithmType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
