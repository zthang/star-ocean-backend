package com.tju.myproject.entity;

import lombok.Data;

@Data
public class ResultEntity
{
    private Integer state;
    private String message;
    private Object data;
    public ResultEntity(){}
    public ResultEntity(Integer state,String message,Object data)
    {
        this.state=state;
        this.message=message;
        this.data=data;
    }
}
