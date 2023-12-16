package cn.dq.article.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@TableName("destination")
public class Destination implements Serializable {
    private String name;
    private String english;
    private Long parentId;
    private String parentName;
    private String info;
    private String coverUrl;

    //吐司导航功能会用到
    @TableField(exist = false)
    private List<Destination> children = new ArrayList<>();


}