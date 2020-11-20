package cn.iotclub.clockin.controller;

import cn.iotclub.clockin.domain.Admin;
import cn.iotclub.clockin.domain.Code2Session;
import cn.iotclub.clockin.domain.Member;
import cn.iotclub.clockin.domain.Res;
import cn.iotclub.clockin.service.MemberService;
import cn.iotclub.clockin.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("member")
public class MemberController {

    //小程序appid
    @Value("${wxapp.appid}")
    private String appid;
    //小程序appSecret
    @Value("${wxapp.secret}")
    private String secret;
    //授权类型
    @Value("${wxapp.grant_type}")
    private String grant_type;

    @Autowired
    private MemberService memberService;

    @Autowired
    private RecordService recordService;

    /***
     * 获取openid
     * @param js_code
     * @return
     */
    @GetMapping("getOpenid")
    public Res getOpenid(@RequestParam("code") String js_code){
        Res res = new Res();
        Code2Session code2Session = memberService.getOpenid(js_code,appid,secret,grant_type);
        if(code2Session.getErrcode() == null){
            res.setCode(0);
            res.setMsg("获取成功");
            res.setData(code2Session);
        }else{
            res.setCode(1);
            res.setMsg(code2Session.getErrmsg());
        }
        return res;
    }

    /**
     * 新增Member
     * @param openid
     * @param name
     * @param portrait
     * @param state
     * @return
     */
    @RequestMapping(value = "addMember",method = RequestMethod.PUT)
    public Res addMember(@RequestParam("openid") String openid,
                         @RequestParam("name") String name,
                         @RequestParam("portrait") String portrait,
                         @RequestParam("state") String state){
        Res res = new Res();
        if(memberService.getMember(openid) == null){
            if(memberService.addMember(openid,name,portrait,state) == 1){
                res.setCode(0);
                res.setMsg("新增成功");
            }else{
                res.setCode(1);
                res.setMsg("新增失败");
            }
        }else{
            res.setCode(1);
            res.setMsg("该用户已存在");
        }
        return res;
    }

    /**
     * 删除Member
     * @param openid
     * @return
     */
    @RequestMapping(value = "deleteMember",method = RequestMethod.DELETE)
    public Res deleteMember(@RequestParam("openid") String openid){
        Res res = new Res();
        if(memberService.getMember(openid) != null){
            if(memberService.deleteMember(openid) == 1){
                recordService.deleteRecordByOpenid(openid);
                res.setCode(0);
                res.setMsg("删除成功");
            }else{
                res.setCode(1);
                res.setMsg("删除失败");
            }
        }else{
            res.setCode(1);
            res.setMsg("该用户不存在");
        }
        return res;
    }

    /**
     * 修改Member信息
     * @param openid
     * @param name
     * @param portrait
     * @return
     */
    @RequestMapping(value = "updateMember",method = RequestMethod.POST)
    public Res updateMember(@RequestParam("openid") String openid,
                         @RequestParam("name") String name,
                         @RequestParam("portrait") String portrait){
        Res res = new Res();
        if(memberService.getMember(openid) != null){
            if(memberService.updateMember(openid,name,portrait) == 1){
                res.setCode(0);
                res.setMsg("修改成功");
            }else{
                res.setCode(1);
                res.setMsg("修改失败");
            }
        }else{
            res.setCode(1);
            res.setMsg("该用户不存在");
        }
        return res;
    }


    /**
     * 修改Member状态
     * @param openid
     * @param state
     * @return
     */
    @RequestMapping(value = "updateMemberState",method = RequestMethod.POST)
    public Res updateMemberState(@RequestParam("openid") String openid,
                            @RequestParam("state") String state){
        Res res = new Res();
        if(memberService.getMember(openid) != null){
            if(memberService.updateMember(openid,state) == 1){
                res.setCode(0);
                res.setMsg("修改成功");
            }else{
                res.setCode(1);
                res.setMsg("修改失败");
            }
        }else{
            res.setCode(1);
            res.setMsg("该用户不存在");
        }
        return res;
    }

    /***
     * 查询Member
     */
    @GetMapping("getMembers")
    public Res getMembers(@RequestParam("page") int page,
                         @RequestParam("limit") int limit,
                          @RequestParam(value = "state",required = false) String state){
        Res res = new Res();
        res.setCode(0);
        res.setMsg("查询成功");
//        if(state == ""){
//
//        }
        PageInfo<Member> pageInfo = memberService.getMembers(page,limit);
        res.setData(pageInfo.getList());
        res.setCount((int) pageInfo.getTotal());
        return res;
    }

    /***
     * 根据openid查询Member
     */
    @GetMapping("getMemberByOpenid")
    public Res getMemberByOpenid(@RequestParam("openid") String openid){
        Res res = new Res();
        Member member = memberService.getMember(openid);
        res.setCode(0);
        res.setMsg("查询成功");
        res.setData(member);
        return res;
    }

}
