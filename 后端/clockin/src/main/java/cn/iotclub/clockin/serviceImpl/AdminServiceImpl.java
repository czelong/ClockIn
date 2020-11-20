package cn.iotclub.clockin.serviceImpl;

import cn.iotclub.clockin.domain.Admin;
import cn.iotclub.clockin.mapper.AdminMapper;
import cn.iotclub.clockin.service.AdminService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public List<Admin> queryAllAdmin() {
        return adminMapper.selectAll();
    }

    @Override
    public PageInfo<Admin> queryAdmins(int page, int limit) {
        PageHelper.startPage(page,limit);
        List<Admin> admins = adminMapper.selectAll();
        //解析分页结果
        PageInfo<Admin> info = new PageInfo<>(admins);
        return info;
    }

    @Override
    public Admin queryAdmin(String account, String password) {
        Admin admin = new Admin();
        admin.setAccount(account);
        admin.setPassword(password);
        return adminMapper.selectOne(admin);
    }

    @Override
    public Admin queryAdmin(String account) {
        Admin admin = new Admin();
        admin.setAccount(account);
        return adminMapper.selectOne(admin);
    }

    @Override
    public Admin queryAdmin(int id) {
        Admin admin = new Admin();
        admin.setId(id);
        return adminMapper.selectOne(admin);
    }

    @Override
    public int addAdmin(String name, String account, String password) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setAccount(account);
        admin.setPassword(password);
        return adminMapper.insert(admin);
    }

    @Override
    public int deleteAdmin(int id) {
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        return adminMapper.deleteByExample(example);
    }

    @Override
    public int updateAdmin(int id, String name, String account) {
        Admin admin = new Admin();
        admin.setName(name);
        admin.setAccount(account);
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        return adminMapper.updateByExampleSelective(admin,example);
    }

    @Override
    public int updateAdminPassword(int id, String newpassword) {
        Admin admin = new Admin();
        admin.setPassword(newpassword);
        Example example = new Example(Admin.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("id",id);
        return adminMapper.updateByExampleSelective(admin,example);
    }
}
