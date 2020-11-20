
//获取应用实例
const app = getApp()

Page({

  /**
   * 页面的初始数据
   */
  data: {
    infoflag:"",
    class:'',
    name:''
  },

  setClass(e){
    var that = this;
    that.setData({
      class : e.detail.value
    })
  },

  setName(e){
    var that = this;
    that.setData({
      name : e.detail.value
    })
  },

  //监听注册按钮单击事件
  register(){
    var that = this;
    if((that.data.class != "") && (that.data.name != "")){
      //判断是否填写班级和姓名
      //提交申请接口s
      wx.showLoading({
        title: '正在提交',
        mask:true
      })
      wx.request({
        url: app.globalData.urlConfig+'/member/addMember',
        data:{
          openid:app.globalData.openid,
          name:that.data.class+that.data.name,
          portrait:app.globalData.portrait,
          state:0
        },
        method:'PUT',
        header: {
          'content-type': 'application/x-www-form-urlencoded',
        },
        success:function(res){
          wx.hideLoading({
            success: (res) => {},
          })
          if(res.data.code == 0){
            app.globalData.state = 0;
            app.globalData.name = that.data.class+that.data.name;
            wx.showToast({
              icon:'none',
              title: "请等待管理员审核",
              duration:1500,
              success:function(){
                setTimeout(function (){
                  wx.navigateBack({
                    delta: 1,
                  })
                },1500);
              }
            })
          }
        }
      })
    }else if(that.data.class == ""){
      wx.showToast({
        icon:'none',
        title: '请输入班级',
      })
    }else if(that.data.name == ""){
      wx.showToast({
        icon:'none',
        title: '请输入姓名',
      })
    }
  },

  bindgetUserInfo(){
    var that = this;
    wx.showLoading({
    })
    wx.getUserInfo({
      success(res){
        var userInfo = res.userInfo;
        app.globalData.infoflag = 1;
        that.setData({
          infoflag : 1
        })
        wx.login({
          success(res){
            var code = res.code;
            //获取openid
            wx.request({
              url: app.globalData.urlConfig+'/member/getOpenid',
              method:"GET",
              data:{
                code:code
              },
              success(res){
                if(res.data.code == 0){
                  var openid = res.data.data.openid;
                  app.globalData.openid = openid;
                  that.setData({
                    openid:openid
                  })
                  //通过openid查询member
                  wx.request({
                    url: app.globalData.urlConfig+'/member/getMemberByOpenid',
                    data:{
                      openid:openid
                    },
                    method:"GET",
                    success(res){
                      //未查询到Member
                      if(res.data.data == null){
                        app.globalData.name = userInfo.nickName;
                        app.globalData.portrait = userInfo.avatarUrl;
                        that.setData({
                          name:userInfo.nickName,
                          portrait:userInfo.avatarUrl
                        })
                      }else{
                        app.globalData.name = res.data.data.name;
                        app.globalData.portrait = res.data.data.portrait;
                        app.globalData.state = res.data.data.state;
                        that.setData({
                          name:res.data.data.name,
                          portrait:res.data.data.portrait,
                          state:res.data.data.state
                        })
                        if(res.data.data.state != 0){
                          wx.navigateBack({
                            delta: 1,
                          })
                        }
                      }
                      wx.hideLoading({
                        success: (res) => {},
                      })
                    }
                  })
                }
              }
            })
          }
        })
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      infoflag:app.globalData.infoflag
    })

    // wx.login({
    //   success(res){
    //     var code = res.code;
    //     //用code换取openid
    //     wx.request({
    //       url: app.globalData.urlConfig+'/member/getOpenid',
    //       data:{
    //         code:code
    //       },
    //       method:'GET',
    //       success:function(res){
    //         // console.log(res.data);
    //         var openid = res.data.openid;
    //         //将openid存入内存
    //         app.globalData.openid = openid;
    //       }
    //     })
    //   }
    // })

  },


  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {
    var that = this;
    that.setData({
      infoflag : app.globalData.infoflag
    })
  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

})
