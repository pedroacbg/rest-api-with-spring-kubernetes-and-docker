package com.pedroacbg.rest_with_spring_boot.controllers;

import com.pedroacbg.rest_with_spring_boot.exception.UnsupportedMathOperationException;
import com.pedroacbg.rest_with_spring_boot.math.SimpleMath;
import com.pedroacbg.rest_with_spring_boot.request.converters.NumberConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/math")
public class MathController {

    private SimpleMath math = new SimpleMath();

    @RequestMapping(path = "/sum/{x}/{y}")
    public Double sum(@PathVariable(name = "x") String x, @PathVariable(name = "y") String y){
        if(!NumberConverter.isNumeric(x) || !NumberConverter.isNumeric(y)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.sum(NumberConverter.convertToDouble(x), NumberConverter.convertToDouble(y));
    }

    @RequestMapping(path = "/sub/{x}/{y}")
    public Double sub(@PathVariable(name = "x") String x, @PathVariable(name = "y") String y){
        if(!NumberConverter.isNumeric(x) || !NumberConverter.isNumeric(y)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.sub(NumberConverter.convertToDouble(x), NumberConverter.convertToDouble(y));
    }

    @RequestMapping(path = "/multiply/{x}/{y}")
    public Double multiply(@PathVariable(name = "x") String x, @PathVariable(name = "y") String y){
        if(!NumberConverter.isNumeric(x) || !NumberConverter.isNumeric(y)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.multiply(NumberConverter.convertToDouble(x), NumberConverter.convertToDouble(y));
    }

    @RequestMapping(path = "/divide/{x}/{y}")
    public Double divide(@PathVariable(name = "x") String x, @PathVariable(name = "y") String y){
        if(!NumberConverter.isNumeric(x) || !NumberConverter.isNumeric(y)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.divide(NumberConverter.convertToDouble(x), NumberConverter.convertToDouble(y));
    }

    @RequestMapping(path = "/average/{x}/{y}")
    public Double average(@PathVariable(name = "x") String x, @PathVariable(name = "y") String y){
        if(!NumberConverter.isNumeric(x) || !NumberConverter.isNumeric(y)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.average(NumberConverter.convertToDouble(x), NumberConverter.convertToDouble(y));
    }

    @RequestMapping(path = "/root/{x}")
    public Double root(@PathVariable(name = "x") String x){
        if(!NumberConverter.isNumeric(x)) throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.root(NumberConverter.convertToDouble(x));
    }
}
