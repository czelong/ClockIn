// pages/mine/rank/rank.js
const app = getApp()
const util = require("../../../utils/util");

Page({

  /**
   * 页面的初始数据
   */
  data: {
    lastweekstarttime:'',
    lastweekendtime:'',
    ranks:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    var ranks=[];
    that.getDate();
    // console.log(that.data.lastweekstarttime);
    // console.log(that.data.lastweekendtime);
    wx.request({
      url: app.globalData.urlConfig+'/record/getRank',
      method:'GET',
      data:{
        startdate:that.data.lastweekstarttime,
        enddate:that.data.lastweekendtime
      },
      success(res){
        if(res.data.data != null){
          for(var i = 0; i<res.data.data.length;i++){
            var item = {};
            var time = res.data.data[i].time;
            if(res.data.data[i].time > 39600000){
              item['flag'] = 1;
            }else{
              item['flag'] = 0;
            }
            item['name'] = res.data.data[i].name;
            item['portrait'] = res.data.data[i].portrait;
            item['time'] = util.formatTimeFour(time);
            ranks.push(item);
          }
        }
        that.setData({
          ranks:ranks
        })
        console.log(that.data.ranks);
      }
    })
  },

  //获取上周的起始时间
  getDate(){
    var that = this;
    var now = new Date();
    var week = now.getDay();//今天是星期几（0代表星期日）
    var hour = now.getHours();
    var minute = now.getMinutes();
    var second = now.getSeconds();
    var todaystartime = Date.parse(now)-hour*60*60*1000-minute*60*1000-second*1000;//今天的开始时间
    var lastweekstarttime;//上周的开始时间
    var lastweekendtime;//上周的结束时间
    if(week == 0){
      lastweekstarttime = todaystartime-13*24*60*60*1000;
      lastweekendtime = todaystartime-6*24*60*60*1000;
    }else{
      lastweekstarttime = todaystartime-(week-1+7)*24*60*60*1000;
      lastweekendtime = todaystartime-(week-1)*24*60*60*1000;
    }
    that.setData({
      lastweekstarttime:lastweekstarttime,
      lastweekendtime:lastweekendtime
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