package cn.iotclub.clockin.controller;

import cn.iotclub.clockin.domain.Member;
import cn.iotclub.clockin.domain.Rank;
import cn.iotclub.clockin.domain.Record;
import cn.iotclub.clockin.domain.Res;
import cn.iotclub.clockin.service.MemberService;
import cn.iotclub.clockin.service.RecordService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private MemberService memberService;


    /**
     * 新增打卡记录（打卡）
     * @param openid
     * @param name
     * @param intime
     * @return
     */
    @RequestMapping(value = "addRecord",method = RequestMethod.PUT)
    public Res addRecord(@RequestParam("openid") String openid,
                         @RequestParam("name") String name,
                         @RequestParam("intime") String intime){
        Res res = new Res();
        if(recordService.addRecord(openid,name,intime) == 1){
            res.setCode(0);
            res.setMsg("打卡成功");
        }else{
            res.setCode(1);
            res.setMsg("打卡失败");
        }
        return res;
    }

    /**
     * 修改打卡记录（签退）
     * @param id
     * @param outtime
     * @return
     */
    @RequestMapping(value = "updateRecord",method = RequestMethod.POST)
    public Res updateRecord(@RequestParam("id") int id,
                            @RequestParam("outtime") String outtime,
                            @RequestParam("intime") String intime){
        Res res = new Res();
        long time = Long.parseLong(outtime)-Long.parseLong(intime);
        if(recordService.updateRecord(id,outtime,Long.toString(time)) == 1){
            res.setCode(0);
            res.setMsg("签退成功");
        }else{
            res.setCode(1);
            res.setMsg("签退失败");
        }
        return res;
    }

    /**
     * 根据openid查询最近一次的打卡记录
     * @param openid
     * @param page
     * @param limit
     * @return
     */
    @RequestMapping(value = "getLastRecordByOpenid",method = RequestMethod.GET)
    public Res getLastRecordByOpenid(@RequestParam("openid") String openid,
                                 @RequestParam("page") int page,
                                 @RequestParam("limit") int limit){
        Res res = new Res();
        PageInfo<Record> recordPageInfo = recordService.getLastRecordByOpenid(openid,page,limit);
        res.setCode(0);
        res.setMsg("查询成功");
        res.setData(recordPageInfo.getList());
        res.setCount((int) recordPageInfo.getTotal());
        return res;
    }

    /**
     * 根据id删除记录
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteRecordById",method = RequestMethod.DELETE)
    public Res deleteRecordById(@RequestParam("id") int id){
        Res res = new Res();
        int flag = recordService.deleteRecordById(id);
        if(flag == 1){
            res.setCode(0);
            res.setMsg("删除成功");
        }else{
            res.setCode(1);
            res.setMsg("删除失败");
        }
        return res;
    }

    /**
     * 条件查询记录
     * @param page
     * @param limit
     * @param openid
     * @param startdate
     * @param enddate
     * @return
     */
    @RequestMapping(value = "getRecords",method = RequestMethod.GET)
    public Res getRecords(@RequestParam("page") int page,
                          @RequestParam("limit") int limit,
                          @RequestParam(value = "openid",required = false) String openid,
                          @RequestParam(value = "startdate",required = false) String startdate,
                          @RequestParam(value = "enddate",required = false) String enddate){
        Res res = new Res();
        PageInfo<Record> recordPageInfo = null;
        if(startdate != null && openid != null){
            recordPageInfo = recordService.getRecordByOpenidAndDate(openid,startdate,enddate,page,limit);
        }else if(startdate == null && openid != null){
            recordPageInfo = recordService.getRecordByOpenid(openid,page,limit);
        }else if(startdate != null && openid == null){
            recordPageInfo = recordService.getRecordByDate(startdate,enddate,page,limit);
        }else if(startdate == null && openid == null){
            recordPageInfo = recordService.getRecord(page,limit);
        }
        res.setCode(0);
        res.setData(recordPageInfo.getList());
        res.setCount((int) recordPageInfo.getTotal());
        res.setMsg("查询成功");
        return res;
    }

    /**
     * 获取指定时间内的排名
     * @param startdate 开始时间
     * @param enddate 结束时间
     * @return
     */
    @RequestMapping(value = "getRank",method = RequestMethod.GET)
    public Res getRank(@RequestParam("startdate") String startdate,
                       @RequestParam("enddate") String enddate){
        Res res = new Res();
        PageInfo<Member> memberPageInfo = memberService.getMembers(0,99999);
        PageInfo<Record> recordPageInfo = null;
        //得到所有member
        List<Member> members = memberPageInfo.getList();
        List<Rank> ranks = new ArrayList<>();
        for (Member member : members) {
            //查询每一个member的记录
            recordPageInfo = recordService.getRecordByOpenidAndDate(member.getOpenid(),startdate,enddate,0,99999);
            List<Record> records = recordPageInfo.getList();
            long time = 0;
            //遍历每一条记录，计算总时长
            for (Record record : records) {
                time+=Long.parseLong(record.getOuttime())-Long.parseLong(record.getIntime());
            }
            Rank rank = new Rank();
            rank.setPortrait(member.getPortrait());
            rank.setName(member.getName());
            rank.setTime(time);
            ranks.add(rank);
        }
        ranks.sort(Comparator.comparing(Rank::getTime).reversed());
        res.setCode(0);
        res.setMsg("查询成功");
        res.setData(ranks);
        return res;
    }

}
