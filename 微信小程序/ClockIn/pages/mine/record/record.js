// pages/mine/record/record.js
const util = require("../../../utils/util");
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    records:[]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

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
    var records=[];
    that.setData({
      openid:app.globalData.openid
    })
    //获取打卡记录
    wx.request({
      url: app.globalData.urlConfig+'/record/getRecords',
      method:'GET',
      data:{
        page:0,
        limit:99999,
        openid:that.data.openid
      },
      success(res){
        console.log(res);
        if(res.data.data != null){
          for(var i = 0; i<res.data.data.length;i++){
            var item = {};
            item['date'] = util.formatDate(new Date(res.data.data[i].intime*1));
            item['intime'] = util.formatTimeTwo(new Date(res.data.data[i].intime*1));
            if(res.data.data[i].outtime != null){
              item['outtime'] = util.formatTimeTwo(new Date(res.data.data[i].outtime*1));
              item['time'] = util.formatTimeThree(new Date(res.data.data[i].outtime*1),new Date(res.data.data[i].intime*1));
            }else{
              item['outtime'] = null;
              item['time'] = util.formatTimeThree(new Date(),new Date(res.data.data[i].intime*1));
            }
            records.push(item);
          }
        }
        that.setData({
          records:records
        })
        console.log(that.data.records);
      }
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