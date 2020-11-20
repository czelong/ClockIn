package cn.iotclub.clockin.serviceImpl;

import cn.iotclub.clockin.domain.Admin;
import cn.iotclub.clockin.domain.Code2Session;
import cn.iotclub.clockin.domain.Member;
import cn.iotclub.clockin.mapper.MemberMapper;
import cn.iotclub.clockin.service.MemberService;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sun.org.apache.bcel.internal.classfile.Code;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {

    @Autowired
    private MemberMapper memberMapper;

    @Override
    public Code2Session getOpenid(String js_code, String appid, String secret, String grant_type) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid="+appid+"&secret="+secret+"&js_code="+js_code+"&grant_type="+grant_type;
        String str = restTemplate.getForObject(url,String.class);
        Code2Session code2Session = JSONObject.parseObject(str,Code2Session.class);
        return code2Session;
    }

    @Override
    public int addMember(String openid, String name, String portrait, String state) {
        Member member = new Member();
        member.setOpenid(openid);
        member.setName(name);
        member.setPortrait(portrait);
        member.setState(state);
        return memberMapper.insert(member);
    }

    @Override
    public Member getMember(String openid) {
        Member member = new Member();
        member.setOpenid(openid);
        return memberMapper.selectOne(member);
    }

    @Override
    public int deleteMember(String openid) {
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        return memberMapper.deleteByExample(example);
    }

    @Override
    public int updateMember(String openid, String name, String portrait) {
        Member member = new Member();
        member.setName(name);
        member.setPortrait(portrait);
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        return memberMapper.updateByExampleSelective(member,example);
    }

    @Override
    public int updateMember(String openid, String state) {
        Member member = new Member();
        member.setState(state);
        Example example = new Example(Member.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        return memberMapper.updateByExampleSelective(member,example);
    }

    @Override
    public PageInfo<Member> getMembers(int page, int limit) {
        PageHelper.startPage(page,limit);
        List<Member> members = memberMapper.selectAll();
        //解析分页结果
        PageInfo<Member> info = new PageInfo<>(members);
        return info;
    }
}
