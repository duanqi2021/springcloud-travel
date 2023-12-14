package cn.dq.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class R<T> {
    public static final int CODE_SUCCESS=200;
    public static final String MSG_SUCCESS="操作成功";

    public static final int CODE_ERROR=500;
    public static final String MSG_ERROR="系统异常";

    public static final int ZDY_ERROR=501; //自定义异常
    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok(T data){return new R<>(CODE_SUCCESS,MSG_SUCCESS,data);}

    public static <T> R<T> ok(){return new R<>(CODE_SUCCESS,MSG_SUCCESS,null);}

    public static <T> R<T> error(T data){return new R<>(CODE_ERROR,MSG_ERROR,data);}

    public static <T> R<T> error(int code,String msg){return new R<>(code,msg,null);}

    public static <T> R<T> error(String msg,T data){return new R<>(ZDY_ERROR,msg,data);}

    public static <T> R<T> noPermission(){return new R<>(403,"非法访问",null);}

    public static <T> R<T> noLogin(){return new R<>(401,"请先登录",null);}
}