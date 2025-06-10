package com.pedroacbg.rest_with_spring_boot.math;

import com.pedroacbg.rest_with_spring_boot.exception.UnsupportedMathOperationException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

public class SimpleMath {


    public Double sum(Double x, Double y){
        return x + y;
    }

    public Double sub(Double x, Double y){
        return x - y;
    }

    public Double multiply(Double x, Double y){
        return x * y;
    }

    public Double divide(Double x, Double y){
        return x / y;
    }

    public Double average(Double x, Double y){
        return (x + y) / 2;
    }

    public Double root(Double x){
        return Math.sqrt(x);
    }
}
