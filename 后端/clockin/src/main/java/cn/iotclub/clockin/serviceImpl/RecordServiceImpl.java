package cn.iotclub.clockin.serviceImpl;

import cn.iotclub.clockin.domain.Member;
import cn.iotclub.clockin.domain.Record;
import cn.iotclub.clockin.mapper.RecordMapper;
import cn.iotclub.clockin.service.RecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    private RecordMapper recordMapper;

    @Override
    public int addRecord(String openid, String name, String intime) {
        Record record = new Record();
        record.setOpenid(openid);
        record.setName(name);
        record.setIntime(intime);
        return recordMapper.insert(record);
    }

    @Override
    public int updateRecord(int id, String outtime, String time) {
        Record record = new Record();
        record.setOuttime(outtime);
        record.setTime(time);
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        return recordMapper.updateByExampleSelective(record,example);
    }

    @Override
    public PageInfo<Record> getLastRecordByOpenid(String openid, int page, int limit) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        example.orderBy("id").desc();
        PageHelper.startPage(page,limit);
        List<Record> records = recordMapper.selectByExample(example);
        //解析分页结果
        PageInfo<Record> info = new PageInfo<>(records);
        return info;
    }

    @Override
    public PageInfo<Record> getRecordByOpenid(String openid, int page, int limit) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        example.orderBy("intime").desc();
        PageHelper.startPage(page,limit);
        List<Record> records = recordMapper.selectByExample(example);
        //解析分页结果
        PageInfo<Record> info = new PageInfo<>(records);
        return info;
    }

    @Override
    public int deleteRecordById(int id) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        return recordMapper.deleteByExample(example);
    }

    @Override
    public PageInfo<Record> getRecordByOpenidAndDate(String openid, String startdate, String enddate, int page, int limit) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        criteria.andGreaterThan("intime",startdate);
        criteria.andLessThan("intime",enddate);
        example.orderBy("intime").desc();
        PageHelper.startPage(page,limit);
        List<Record> records = recordMapper.selectByExample(example);
        //解析分页结果
        PageInfo<Record> info = new PageInfo<>(records);
        return info;
    }

    @Override
    public PageInfo<Record> getRecordByDate(String startdate, String enddate, int page, int limit) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andGreaterThan("intime",startdate);
        criteria.andLessThan("intime",enddate);
        example.orderBy("intime").desc();
        PageHelper.startPage(page,limit);
        List<Record> records = recordMapper.selectByExample(example);
        //解析分页结果
        PageInfo<Record> info = new PageInfo<>(records);
        return info;
    }

    @Override
    public PageInfo<Record> getRecord(int page, int limit) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("intime").desc();
        PageHelper.startPage(page,limit);
        List<Record> records = recordMapper.selectByExample(example);
        //解析分页结果
        PageInfo<Record> info = new PageInfo<>(records);
        return info;
    }

    @Override
    public int deleteRecordByOpenid(String openid) {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openid",openid);
        return recordMapper.deleteByExample(example);
    }


    @Override
    @Scheduled(cron = "0 0 23 * * ?")
    public void deleteRecord() {
        Example example = new Example(Record.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andIsNull("outtime");
        recordMapper.deleteByExample(example);
    }


}
