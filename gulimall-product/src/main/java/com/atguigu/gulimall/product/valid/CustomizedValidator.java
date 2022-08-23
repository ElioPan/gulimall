package com.atguigu.gulimall.product.valid;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;

/**
 * @author
 * @create 2022-08-13-21:03
 */
public class CustomizedValidator implements ConstraintValidator<CustomizedList,Integer>{
    private Set<Integer> set = new HashSet<>();
    @Override
    public void initialize(CustomizedList constraintAnnotation) {
        int[] value = constraintAnnotation.value();
        for(int v : value){
            set.add(v);
        }
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        return set.contains(value);
    }
}
