package cn.iotclub.clockin.service;


import cn.iotclub.clockin.domain.Record;
import com.github.pagehelper.PageInfo;

public interface RecordService {

    /**
     * 新增记录
     * @param openid
     * @param name
     * @param intime
     * @return
     */
    int addRecord(String openid, String name, String intime);

    /**
     * 修改记录
     * @param id
     * @param outtime
     * @return
     */
    int updateRecord(int id, String outtime, String time);

    /**
     * 根据openid查询最近一次的记录
     * @param openid
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Record> getLastRecordByOpenid(String openid, int page, int limit);

    /**
     * 根据openid进行分页查询记录
     * @param openid
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Record> getRecordByOpenid(String openid, int page, int limit);

    /**
     * 根据id删除记录
     * @param id
     * @return
     */
    int deleteRecordById(int id);


    /**
     * 根据openid和时间分页查询
     * @param openid
     * @param startdate
     * @param enddate
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Record> getRecordByOpenidAndDate(String openid, String startdate, String enddate, int page, int limit);

    /**
     * 根据时间分页查询
     * @param startdate
     * @param enddate
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Record> getRecordByDate(String startdate, String enddate, int page, int limit);

    /**
     * 分页查询
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Record> getRecord(int page, int limit);

    /**
     * 根据openid删除记录
     * @param openid
     * @return
     */
    int deleteRecordByOpenid(String openid);


    /**
     * 定时删除未签退的记录
     */
    void deleteRecord();

}
