package com.hj.platform.uac.oauth;

import com.hj.platform.domain.entity.sys.SysUser;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
public interface UserRepository extends Repository<SysUser, Long> {
    @Query(value = "select * from sys_user where username=:name")
    Flux<SysUser> testQuery(@Param("name") String name);
}
