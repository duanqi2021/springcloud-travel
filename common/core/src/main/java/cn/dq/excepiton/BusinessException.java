package cn.dq.excepiton;

import cn.dq.utils.R;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusinessException extends  RuntimeException{
    private Integer code= R.CODE_ERROR;

    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException(Integer code,String msg) {
        super(msg);
        this.code=code;
    }
}
