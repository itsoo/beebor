package org.beeborframework.core.converter;

import lombok.Data;

import java.util.List;

/**
 * FooObject
 *
 * @author zxy
 * @version 0.0.1
 * @date 2021/5/18 13:31
 */
@Data
public class FooObject {

    private String name;

    private Integer age;

    private String phone;

    private List<String> alias;

    private Integer[] hats;

}
