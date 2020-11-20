package cn.iotclub.clockin.mapper;

import cn.iotclub.clockin.domain.Member;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

@Component
@org.apache.ibatis.annotations.Mapper
public interface MemberMapper extends Mapper<Member> {
}
