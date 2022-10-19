package com.tique.dev.rest.components;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.stereotype.Component;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

@Component
public class NullAwareBeanUtilBean extends BeanUtilsBean {

    @Override
    public void copyProperty(Object bean, String name, Object value) throws IllegalAccessException, InvocationTargetException {
        if (value == null)
            return;
        super.copyProperty(bean, name, value);
    }
}
