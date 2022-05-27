package com.hj.platform.uac.auth.respository;

import com.hj.platform.domain.entity.sys.SysUser;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author <a href="mailto:hjm0928@sina.cn">韩金明</a>
 */
@Repository
public interface UserRepository extends ReactiveCrudRepository<SysUser, Long> {
    Mono<SysUser> getSysUserByUsername(String username);

    Flux<SysUser> queryAllBy();
}
