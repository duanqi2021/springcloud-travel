package cn.dq.article.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@TableName("region")
public class Region implements Serializable {
    public static final int STATE_HOT = 1;
    public static final int STATE_NORMAL = 0;

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;
    private String sn;
    private String refIds; //关联的ID  多个，以逗号分隔
    private Integer ishot = STATE_NORMAL;
    private Integer seq;
    private String info;

    //86,87,88,89        86   87   88   99
    public List<Long> parseRefIds() {
        //创建一个集合LIST
        List<Long> ids = new ArrayList<>();
        //看看是否有关联的目的地
        if (StringUtils.hasLength(refIds)) {
            //拆分。以什么作为拆分点
            String[] split = refIds.split(",");
            if (split != null && split.length > 0) {
                for (int i = 0; i < split.length; i++) {
                    //存到LIST集合中
                    ids.add(Long.parseLong(split[i]));
                }
            }
        }
        //返回
        return ids;
    }

}