// pages/mine/index/index.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    infoflag:0,
    openid:"",
    name:"",
    portrait:"",
    state:""
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    that.setData({
      infoflag : app.globalData.infoflag,
      openid : app.globalData.openid,
      name : app.globalData.name,
      portrait : app.globalData.portrait,
      state : app.globalData.state
    })
  },

  bindgetUserInfo(){
    var that = this;
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
                      }
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

  //页面跳转
  jump(e){
    var location = e.currentTarget.dataset.location;
    wx.navigateTo({
      url: '../'+location+'/'+location,
    })
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
      infoflag : app.globalData.infoflag,
      openid : app.globalData.openid,
      name : app.globalData.name,
      portrait : app.globalData.portrait,
      state : app.globalData.state
    })

    if(that.data.infoflag == 1){
      //获取用户状态
      //通过openid查询member
      wx.request({
        url: app.globalData.urlConfig+'/member/getMemberByOpenid',
        data:{
          openid:that.data.openid
        },
        method:"GET",
        success(res){
          if(res.data.data != null){
            app.globalData.state = res.data.data.state;
            that.setData({
              state:res.data.data.state
            })
          }
        }
      })
    }
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

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})