package cn.iotclub.clockin.service;

import cn.iotclub.clockin.domain.Admin;
import cn.iotclub.clockin.domain.Code2Session;
import cn.iotclub.clockin.domain.Member;
import com.github.pagehelper.PageInfo;

public interface MemberService {

    /**
     * 获取用户openid
     * @param js_code
     * @param appid
     * @param secret
     * @param grant_type
     * @return
     */
    Code2Session getOpenid(String js_code, String appid, String secret, String grant_type);

    /**
     *新增Member
     * @return
     * @param openid
     * @param name
     * @param portrait
     * @param state
     */
    int addMember(String openid, String name, String portrait, String state);

    /**
     * 根据openid查询Member
     * @param openid
     * @return
     */
    Member getMember(String openid);


    /**
     * 根据openid删除Member
     * @param openid
     * @return
     */
    int deleteMember(String openid);

    /**
     * 修改Member信息
     * @param openid
     * @param name
     * @param portrait
     * @return
     */
    int updateMember(String openid, String name, String portrait);

    /**
     * 修改Member状态
     * @param openid
     * @param state
     * @return
     */
    int updateMember(String openid, String state);

    /**
     * 分页查询Member
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Member> getMembers(int page, int limit);

}
