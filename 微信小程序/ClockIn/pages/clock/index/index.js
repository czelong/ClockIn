// pages/clock/index/index.js
const amapFile = require('../../../libs/amap-wx');
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    openid:'',
    infoflag:'',
    state:'',
    name:'',
    clockflag:''
  },

  //签到
  clockIn(){
    var that = this;

    //获取用户认证状态
    

    //判断用户
    var state = that.data.state;
    if(state == -1){//未审核用户，跳转到审核界面
      wx.navigateTo({
        url: '../../mine/register/register',
      })
    }else if(state == 0){//正在审核
      wx.showToast({
        title: '等待管理员审核中，审核完成方可打卡',
      })
    }else{//审核通过

      //高德地图
      //获取定位
      // var myAmapFun = new amapFile.AMapWX({key:'fac9e923df74d6f5fe8a127ed371a726'});
      // myAmapFun.getRegeo({
      //   success(res){
      //     console.log(res);
      //   }
      // })

      wx.showLoading({
        title: '定位中',
        mask:true
      }),

      wx.getLocation({
        type:'gcj02',
        isHighAccuracy:true,
        highAccuracyExpireTime:10000,
        success(res){
          console.log(res);
          var latitude = res.latitude;
          var longitude = res.longitude;
          var s = that.getDistance(latitude,longitude,37.97872,114.52481);
          console.log("距离：",s);
          // 显示地图
          // wx.chooseLocation({
          //   latitude: latitude,
          //   longitude:longitude,
          //   success(res){
          //     console.log(res);
          //   }
          // })
          wx.hideLoading({
            success: (res) => {
              if(s < 150){
                wx.showToast({
                  title:'定位成功',
                  duration:3000
                });

                //获取当前时间
                var time = Date.parse(new Date());

                wx.showLoading({
                  title: '正在提交',
                  mask:true
                })
                // 调用签到接口
                wx.request({
                  url: app.globalData.urlConfig+'/record/addRecord',
                  method:'PUT',
                  data:{
                    openid:that.data.openid,
                    name:that.data.name,
                    intime:time
                  },
                  header: {
                    "content-type":"application/x-www-form-urlencoded"
                  },    
                  success(res){
                    wx.hideLoading({
                      success: (res) => {},
                    })
                    if(res.data.code == 0){
                      wx.showToast({
                        title: '签到成功',
                      })
                      that.setData({
                        clockflag:1
                      })
                      app.globalData.clockflag=1
                    }else{
                      wx.showToast({
                        title: '签到失败',
                        icon:'none'
                      })
                    }
                  }
                })
              }else{
                wx.showModal({
                  content:'未在打卡范围内或定位错误，请开启定位后重试。',
                  showCancel:false
                })
              }
            },
          })
        }
      })

      //腾讯地图
      //     // 引入SDK核心类
      //     var QQMapWX = require('../../../libs/qqmap-wx-jssdk');
           
      //     // 实例化API核心类
      //     var qqmapsdk = new QQMapWX({
      //         key: 'DB7BZ-G4N6W-IKBRY-OOILT-MHOLO-IKBJ6' // 必填
      //     });  
      //     qqmapsdk.reverseGeocoder({
      //       location: {
      //         latitude: latitude,
      //         longitude: longitude
      //       },
      //       // location: e.detail.value.reverseGeo || '', //获取表单传入的位置坐标,不填默认当前位置,示例为string格式
      //       success: function(res) {//成功后的回调
      //         console.log(res);
      //         wx.showToast({
      //           title: res.result.formatted_addresses.recommend,
      //           icon:'none'
      //         });
      //         var s = that.getDistance(latitude,longitude,37.97836832682292,114.5270738389757);
      //         // var s = that.getDistance(latitude,longitude,latitude,longitude);
      //         console.log(s);
            
      //       },
      //       fail: function (res) {
      //          console.log("获取地址失败",res);
      //       }
      //     })
      //   }
      // })
    }
  },


  /**
  * 计算两个经纬度的距离(米)
  */
  getDistance(lat1, lng1, lat2, lng2){
    var radLat1 = lat1*Math.PI / 180.0;
    var radLat2 = lat2*Math.PI / 180.0;
    var a = radLat1 - radLat2;
    var b = lng1*Math.PI / 180.0 - lng2*Math.PI / 180.0;
    var s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2) +
    Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
    s = s *6378.137 ;// EARTH_RADIUS;
    s = Math.round(s * 10000) / 10000;
    s=s*1000;
    return s;
  },

  //签退
  clockOut(){

    wx.showLoading({
      title: '正在提交',
      mask:true
    })
    var that = this;
    //获取当前时间
    var time = Date.parse(new Date());
    
    //查询最后一次签到记录
    wx.request({
      url: app.globalData.urlConfig+'/record/getLastRecordByOpenid',
      method:'GET',
      data:{
        openid:that.data.openid,
        page:1,
        limit:1
      },
      success(res){
        wx.hideLoading({
          success: (res) => {},
        })
        var id = res.data.data[0].id;
        var intime = res.data.data[0].intime;
        console.log(time-intime);
        if(time-intime < 1800000){ 
          wx.showModal({
            title:'警告',
            content:'本次学习时长不足30分钟，将不计入历史记录中，是否继续？',
            cancelColor:'#576B95',
            confirmColor:'#ff0000',
            success(res){
              if(res.confirm){
                //删除本次记录
                wx.request({
                  url: app.globalData.urlConfig+'/record/deleteRecordById',
                  method:'DELETE',
                  data:{
                    id:id
                  },
                  header: {
                    "content-type":"application/x-www-form-urlencoded"
                  }, 
                  success(res){
                    if(res.data.code == 0){
                      that.setData({
                        clockflag:0
                      })
                      app.globalData.clockflag=0
                    }
                  }
                })
              }
            }
          })
        }else{
          wx.showLoading({
            title: '定位中',
            mask:true
          }),
      
          wx.getLocation({
            type:'gcj02',
            isHighAccuracy:true,
            highAccuracyExpireTime:10000,
            success(res){
              console.log(res);
              var latitude = res.latitude;
              var longitude = res.longitude;
              var s = that.getDistance(latitude,longitude,37.97872,114.52481);
              console.log("距离：",s);
              wx.hideLoading({
                success: (res) => {
                  if(s < 150){
                    wx.showToast({
                      title:'定位成功',
                      duration:3000
                    });
      
                    wx.showLoading({
                      title: '正在提交',
                      mask:true
                    })
                    // 调用签退接口
                    wx.request({
                      url: app.globalData.urlConfig+'/record/updateRecord',
                      method:'POST',
                      data:{
                        id:id,
                        outtime:time,
                        intime:intime
                      },
                      header: {
                        "content-type":"application/x-www-form-urlencoded"
                      },    
                      success(res){
                        console.log(res);
                        wx.hideLoading({
                          success: (res) => {},
                        })
                        if(res.data.code == 0){
                          wx.showToast({
                            title: '签退成功',
                          })
                          that.setData({
                            clockflag:0
                          })
                          app.globalData.clockflag=0
                        }
                      }
                    })
                  }else{
                    wx.showModal({
                      content:'未在打卡范围内或定位错误，请开启定位后重试。',
                      showCancel:false
                    })
                  }
                },
              })
            }
          })
        }

      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    
    wx.showLoading({
      mask:'true'
    })

    //判断用户授权状态
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          console.log("已授权");
          app.globalData.infoflag = 1;
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              var userInfo = res.userInfo;
              // 可以将 res 发送给后台解码出 unionId
              app.globalData.userInfo = res.userInfo
              //获取openid
              wx.login({
                success(res){
                  var code = res.code;
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
                              console.log("未注册");
                              app.globalData.name = userInfo.nickName;
                              that.setData({
                                name:userInfo.nickName
                              })
                              app.globalData.portrait = userInfo.avatarUrl;
                            }else{
                              console.log("已注册");
                              app.globalData.name = res.data.data.name;
                              app.globalData.portrait = res.data.data.portrait;
                              app.globalData.state = res.data.data.state;
                              that.setData({
                                state:res.data.data.state,
                                name:res.data.data.name
                              })
                              var state = res.data.data.state;
                              //判断签到状态
                              if(state != 1){
                                app.globalData.clockflag = 0;
                              }else{
                                wx.request({
                                  url: app.globalData.urlConfig+'/record/getLastRecordByOpenid',
                                  method:'GET',
                                  data:{
                                    openid:openid,
                                    page:1,
                                    limit:1
                                  },
                                  success(res){
                                    var record = res.data.data[0];
                                    // console.log(record);
                                    if(record == null || record.outtime != null){
                                      app.globalData.clockflag = 0;
                                      that.setData({
                                        clockflag:0
                                      })
                                      console.log("未签到");
                                    }else{
                                      app.globalData.clockflag = 1;
                                      that.setData({
                                        clockflag:1
                                      })
                                      console.log("未签退");
                                    }
                                  }
                                })
                              }
                            }
                            wx.hideLoading({
                              success: (res) => {
                                
                              },
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
        }else{
          console.log("未授权");
          wx.hideLoading({
            success: (res) => {
              
            },
          })
        }
      }
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
      infoflag:app.globalData.infoflag,
      state:app.globalData.state,
      openid:app.globalData.openid,
      name:app.globalData.name,
      clockflag:app.globalData.clockflag
    })
    //获取签到状态
    if(that.data.state == 1){
      wx.request({
        url: app.globalData.urlConfig+'/record/getLastRecordByOpenid',
        method:'GET',
        data:{
          openid:that.data.openid,
          page:1,
          limit:1
        },
        success(res){
          var record = res.data.data[0];
          if(record == null || record.outtime != null){
            app.globalData.clockflag = 0;
            that.setData({
              clockflag:0
            })
          }else{
            app.globalData.clockflag = 1;
            that.setData({
              clockflag:1
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