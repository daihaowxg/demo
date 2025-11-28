package io.github.daihaowxg.event.traditional;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 积分服务（传统方式）
 *
 * @author wxg
 */
@Slf4j
@Service
public class PointsService {

    /**
     * 赠送新用户积分
     */
    public void grantNewUserPoints(String username) {
        log.info("【积分服务】赠送新人积分给: {}", username);
        System.out.println(">> 赠送 100 积分给新用户: " + username);
    }
}
