package cn.iotclub.clockin.controller;

import cn.iotclub.clockin.domain.Admin;
import cn.iotclub.clockin.domain.Res;
import cn.iotclub.clockin.service.AdminService;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@Api(tags = "管理员接口")
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /***
     * 查询管理员账号
     */
    @ApiOperation("查询管理员账号")
    @GetMapping("getAdmins")
    public Res getAdmins(@RequestParam("page") int page,
                           @RequestParam("limit") int limit){
        Res res = new Res();
        res.setCode(0);
        res.setMsg("查询成功");
        PageInfo<Admin> pageInfo = adminService.queryAdmins(page,limit);
        res.setData(pageInfo.getList());
        res.setCount((int) pageInfo.getTotal());
        return res;
    }

    /**
     * 登录接口
     * @param account
     * @param password
     * @return
     */
    @GetMapping("login")
    public Res login(@RequestParam("account") String account,
                     @RequestParam("password") String password){
        Res res = new Res();
        Admin admin = new Admin();
        admin = adminService.queryAdmin(account,password);
        if(admin != null){
            res.setCode(0);
            res.setMsg("登录成功");
            res.setData(admin);
        }else {
            res.setCode(1);
            res.setMsg("登录失败，账号或密码错误");
        }
        return res;
    }

    /***
     * 新增管理员账号
     * @param name
     * @param account
     * @param password
     * @return
     */
    @RequestMapping(value = "addAdmin",method = RequestMethod.PUT)
    public Res addAdmin(@RequestParam("name") String name,
                        @RequestParam("account") String account,
                        @RequestParam("password") String password){
        Res res = new Res();
        if(adminService.queryAdmin(account) != null){
            res.setCode(1);
            res.setMsg("账号重复，请修改后重试");
        }else{
            int flag = adminService.addAdmin(name,account,password);
            if(flag == 1){
                res.setCode(0);
                res.setMsg("新增账号成功！");
            }else{
                res.setCode(1);
                res.setMsg("新增账号失败！");
            }
        }
        return res;
    }

    /***
     * 删除管理员账号
     * @param id
     * @return
     */
    @RequestMapping(value = "deleteAdmin",method = RequestMethod.DELETE)
    public Res deleteAdmin(@RequestParam("id") int id){
        Res res = new Res();
        if( adminService.queryAdmin(id) != null){
            int flag = adminService.deleteAdmin(id);
            if(flag == 1){
                res.setCode(0);
                res.setMsg("删除成功");
            }else{
                res.setCode(1);
                res.setMsg("删除失败");
            }
        }else{
            res.setCode(1);
            res.setMsg("删除失败，此账号不存在");
        }
        return res;
    }

    /***
     * 修改管理员账号信息
     * @param id
     * @return
     */
    @RequestMapping(value = "updateAdmin",method = RequestMethod.POST)
    public Res updateAdmin(@RequestParam("id") int id,
                           @RequestParam("name") String name,
                           @RequestParam("account") String account){
        Res res = new Res();
        if(adminService.queryAdmin(id) != null){
            Admin admin = adminService.queryAdmin(account);
            if(admin == null || admin.getId() == id){
                if(adminService.updateAdmin(id,name,account) == 1){
                    res.setCode(0);
                    res.setMsg("修改成功");
                }else{
                    res.setCode(1);
                    res.setMsg("修改失败");
                }
            }else{
                res.setCode(1);
                res.setMsg("账号重复，请修改后重试");
            }
        }else{
            res.setCode(1);
            res.setMsg("修改失败，此账号不存在");
        }
        return res;
    }

    /***
     * 根据id修改管理员密码
     * @param id
     * @param oldpassword
     * @param newpassword
     * @return
     */
    @RequestMapping(value = "updateAdminPassword",method = RequestMethod.POST)
    public Res updateAdminPassword(@RequestParam("id") int id,
                                   @RequestParam("oldpassword") String oldpassword,
                                   @RequestParam("newpassword") String newpassword){
        Res res = new Res();
        Admin admin = adminService.queryAdmin(id);
        if(admin != null){
            if(adminService.queryAdmin(admin.getAccount(),oldpassword) != null){
                if(adminService.updateAdminPassword(id,newpassword) == 1){
                    res.setCode(0);
                    res.setMsg("修改成功");
                }else{
                    res.setCode(1);
                    res.setMsg("修改失败");
                }
            }else{
                res.setCode(1);
                res.setMsg("旧密码错误");
            }
        }else{
            res.setCode(1);
            res.setMsg("修改失败，此账号不存在");
        }
        return res;
    }
}
