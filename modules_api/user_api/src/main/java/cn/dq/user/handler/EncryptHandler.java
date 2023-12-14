package cn.dq.user.handler;

import com.baomidou.mybatisplus.core.toolkit.AES;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;



public class EncryptHandler extends BaseTypeHandler<String> {
    private final static String KEY="2132h8se8i902756";
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, String s, JdbcType jdbcType) throws SQLException {
         preparedStatement.setString(i, AES.encrypt(s,KEY));
    }

    @Override
    public String getNullableResult(ResultSet resultSet, String s) throws SQLException {
        String c=resultSet.getString(s);
        return AES.decrypt(c,KEY);
    }

    @Override
    public String getNullableResult(ResultSet resultSet, int i) throws SQLException {
        String c=resultSet.getString(i);
        return AES.decrypt(c,KEY);
    }

    @Override
    public String getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String c=callableStatement.getString(i);
        return AES.decrypt(c,KEY);
    }
}
