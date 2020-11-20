//app.js
App({
  onLaunch: function() {
    var that = this;
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // // 登录
    // wx.login({
    //   success: res => {
    //     // 发送 res.code 到后台换取 openId, sessionKey, unionId
    //     console.log(res.code);
    //   }
    // })
    
    // wx.showLoading({
    //   title: '',
    //   mask:'true'
    // })

    // // 获取用户信息
    // wx.getSetting({
    //   success: res => {
    //     if (res.authSetting['scope.userInfo']) {
    //       console.log("已授权");
    //       that.globalData.infoflag = 1;
    //       // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
    //       wx.getUserInfo({
    //         success: res => {
    //           var userInfo = res.userInfo;
    //           // 可以将 res 发送给后台解码出 unionId
    //           that.globalData.userInfo = res.userInfo
    //           //获取openid
    //           wx.login({
    //             success(res){
    //               var code = res.code;
    //               wx.request({
    //                 url: that.globalData.urlConfig+'/member/getOpenid',
    //                 method:"GET",
    //                 data:{
    //                   code:code
    //                 },
    //                 success(res){
    //                   if(res.data.code == 0){
    //                     var openid = res.data.data.openid;
    //                     that.globalData.openid = openid;
    //                     //通过openid查询member
    //                     wx.request({
    //                       url: that.globalData.urlConfig+'/member/getMemberByOpenid',
    //                       data:{
    //                         openid:openid
    //                       },
    //                       method:"GET",
    //                       success(res){
    //                         //未查询到Member
    //                         if(res.data.data == null){
    //                           console.log("未注册");
    //                           that.globalData.name = userInfo.nickName;
    //                           that.globalData.portrait = userInfo.avatarUrl;
    //                         }else{
    //                           console.log("已注册");
    //                           that.globalData.name = res.data.data.name;
    //                           that.globalData.portrait = res.data.data.portrait;
    //                           that.globalData.state = res.data.data.state;
    //                           var state = res.data.data.state;
    //                           //判断签到状态
    //                           if(state != 1){
    //                             that.globalData.clockflag = 0;
    //                           }else{
    //                             wx.request({
    //                               url: that.globalData.urlConfig+'/record/getLastRecordByOpenid',
    //                               method:'GET',
    //                               data:{
    //                                 openid:openid,
    //                                 page:1,
    //                                 limit:1
    //                               },
    //                               success(res){
    //                                 var record = res.data.data[0];
    //                                 // console.log(record);
    //                                 if(record == null || record.outtime != null){
    //                                   that.globalData.clockflag = 0;
    //                                   console.log("未签到");
    //                                 }else{
    //                                   that.globalData.clockflag = 1;
    //                                   console.log("未签退");
    //                                 }
    //                               }
    //                             })
    //                           }
    //                         }
    //                         wx.hideLoading({
    //                           success: (res) => {
    //                             wx.switchTab({
    //                               url: '/pages/clock/index/index',
    //                             })
    //                           },
    //                         })
    //                       }
    //                     })
    //                   }
    //                 }
    //               })
    //             }
    //           })

    //           // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
    //           // 所以此处加入 callback 以防止这种情况
    //           if (that.userInfoReadyCallback) {
    //             that.userInfoReadyCallback(res)
    //           }
    //         }
    //       })
    //     }else{
    //       console.log("未授权");
    //       wx.hideLoading({
    //         success: (res) => {
    //           wx.switchTab({
    //             url: '/pages/clock/index/index',
    //           })
    //         },
    //       })
    //     }
    //   }
    // })
    // 获取系统状态栏信息
    wx.getSystemInfo({
      success: e => {
        that.globalData.StatusBar = e.statusBarHeight;
        let capsule = wx.getMenuButtonBoundingClientRect();
        if (capsule) {
          that.globalData.Custom = capsule;
        	that.globalData.CustomBar = capsule.bottom + capsule.top - e.statusBarHeight;
        } else {
        	that.globalData.CustomBar = e.statusBarHeight + 50;
        }
      }
    })
  },
  globalData: {
    // urlConfig:"http://localhost:8787/clockin",
    urlConfig:"https://www.qoiop.cn/clockin",
    userInfo: null,
    infoflag:0, //0未授权  1已授权
    openid:"",
    name:"",
    portrait:"",
    state:-1,// -1未认证 0审核中 1已认证,
    clockflag:0 //0未签到 1未签退
  }
})