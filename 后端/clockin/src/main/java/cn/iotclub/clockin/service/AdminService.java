package cn.iotclub.clockin.service;

import cn.iotclub.clockin.domain.Admin;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AdminService {

    /***
     * 查询所有管理员账号
     */
    List<Admin> queryAllAdmin();

    /***
     * 分页查询管理员账号
     * @return
     */
    PageInfo<Admin> queryAdmins(int page, int limit);

    /***
     * 根据账号密码查询管理员账号
     * @param account
     * @param password
     * @return
     */
    Admin queryAdmin(String account,String password);

    /***
     * 根据账号查询管理员账号
     * @param account
     * @return
     */
    Admin queryAdmin(String account);

    /***
     * 根据id查询管理员账号
     * @param id
     * @return
     */
    Admin queryAdmin(int id);

    /***
     * 新增管理员账号
     * @param name
     * @param account
     * @param password
     * @return
     */
    int addAdmin(String name,String account,String password);

    /***
     * 根据id删除管理员账号
     * @param id
     * @return
     */
    int deleteAdmin(int id);

    /***
     * 根据id修改管理员账号信息
     * @param id
     * @param name
     * @param account
     * @return
     */
    int updateAdmin(int id,String name,String account);

    /***
     * 根据id修改管理员密码
     * @param id
     * @param newpassword
     * @return
     */
    int updateAdminPassword(int id,String newpassword);
}
