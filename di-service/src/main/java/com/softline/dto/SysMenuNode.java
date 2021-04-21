package com.softline.dto;

import com.softline.mbg.model.SysMenu;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 后台菜单节点封装
 * Created by dong on 2020/11/26.
 */
@Data
public class SysMenuNode extends SysMenu {
    @ApiModelProperty(value = "子级菜单")
    private List<SysMenuNode> children;
}
