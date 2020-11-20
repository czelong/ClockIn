const formatTime = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()
  const hour = date.getHours()
  const minute = date.getMinutes()
  const second = date.getSeconds()

  return [year, month, day].map(formatNumber).join('/') + ' ' + [hour, minute, second].map(formatNumber).join(':')
}

const formatDate = date => {
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  //当前日期
  const year1 = new Date().getFullYear();
  const month1 = new Date().getMonth() + 1;
  const day1 = new Date().getDate();

  // return [year1, month1, day1].map(formatNumber).join('/')
  if(year == year1 && month == month1 && day == day1){
    return "今天";
  }else if(year == year1 && month == month1 && day == (day1-1)){
    return "昨天";
  }else{
    return month+"月"+day+"日"
  }

  // return year1+"年"+month1+"月"+day1+"日"
}


//时间戳获取具体时分
const formatTimeTwo = date => {
  const hour = date.getHours()
  const minute = date.getMinutes()

  if(hour < 10 && minute < 10){
    return "0"+hour+":0"+minute;
  }else if(hour < 10){
    return "0"+hour+":"+minute;
  }else if(minute < 10){
    return hour+":0"+minute;
  }else{
    return hour+":"+minute;
  }
}

//时间戳转时间段
const formatTimeThree = (date1,date2) =>{
  const hour1 = date1.getHours()
  const minute1 = date1.getMinutes()
  const hour2 = date2.getHours()
  const minute2 = date2.getMinutes()
  

  var hour = hour1-hour2;
  var minute = minute1-minute2;
  if(minute < 0){
    hour--;
    minute = minute1+60-minute2;
  }

  var msg;

  if(hour == 0){
    msg = minute+"分钟";
  }else if(minute == 0){
    msg = hour+"小时";
  }else{
    msg = hour+"小时"+minute+"分钟"
  }
  return msg;
}

//时间戳转时间段
const formatTimeFour = (time) =>{
  var hour = Math.floor(time/(3600*1000));
  var minute = Math.floor((time%(3600*1000))/(60*1000));
  var msg;
  if(hour == 0){
    msg = minute+"分钟";
  }else if(minute == 0){
    msg = hour+"小时";
  }else{
    msg = hour+"小时"+minute+"分钟"
  }
  return msg;
}

const formatNumber = n => {
  n = n.toString()
  return n[1] ? n : '0' + n
}

module.exports = {
  formatTime: formatTime,
  formatDate:formatDate,
  formatTimeTwo:formatTimeTwo,
  formatTimeThree:formatTimeThree,
  formatTimeFour:formatTimeFour
}
