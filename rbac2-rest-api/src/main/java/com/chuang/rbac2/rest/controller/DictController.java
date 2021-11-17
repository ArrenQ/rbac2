package com.chuang.rbac2.rest.controller;

import com.chuang.rbac2.crud.service.IDictItemService;
import com.chuang.rbac2.crud.service.IDictTypeService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/dict")
public class DictController {

    @Resource private IDictTypeService dictTypeService;
    @Resource private IDictItemService dictItemService;


//
//    public Result<Void> enumDict() {
//
//    }

//    public Result<Void> dict() {
//        DictItem
//    }
}
