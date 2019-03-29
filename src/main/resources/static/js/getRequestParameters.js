function GetUrlParam(paraName) {
    var url = document.location.toString();
    var arrObj = url.split("&state=");

    if (arrObj.length > 1) {
        var arrPara = arrObj[1].split("%26");
        var arr;

        for (var i = 0; i < arrPara.length; i++) {
            arr = arrPara[i].split("%3D");

            if (arr != null && arr[0] == paraName) {
                return arr[1];
            }
        }
        return "";
    }
    else {
        return "";
    }
}

function withdrawal(id, state) {
    if(state == 1){
        layer.msg('状态不可用！', {icon: 2});
        return;
    }
    layer.msg('确定申请提现吗？', {
        time: 0
        ,btn: ['确定', '取消']
        ,yes: function(index){
            $.ajax("http://tlhe.cn/web/changeReferInfo", {
                data:{
                    id : id
                },
                type:"POST",
                success: function(data) {
                    if(data == "success"){
                        layer.msg('申请成功！', {icon: 1});
                    }else{
                        layer.msg('未知错误，请联系系统管理员', {icon: 2});
                    }
                }
            });
        }
    });
}

function getWxConfig(code, openid) {
    $.ajax("http://tlhe.cn/web/getSignInfo", {
        data:{
            'code' : code, 'openid' : openid, scenePage : 1
        },
        type:"GET",
        dataType: 'jsonp',
        jsonp:"callback",
        crossDomain: true,
        success: function(data) {
            var ret = JSON.parse(data.ret);
            var timestamp = ret.timestamp;
            var nonceStr = ret.nonceStr;
            var signature = ret.signature;
            console.info(ret);
            wx.config({
                debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: 'wx7968b1618f078cc4', // 必填，公众号的唯一标识
                timestamp: timestamp, // 必填，生成签名的时间戳
                nonceStr: nonceStr, // 必填，生成签名的随机串
                signature: signature,// 必填，签名
                jsApiList: [
                    'chooseImage',
                    'uploadImage',
                    'getLocalImgData',
                    'downloadImage'] // 必填，需要使用的JS接口列表
            });

        }
    });
}
function chooseImage() {
    var request =
        {
            QueryString : function(val)
            {
                var uri = window.location.search;
                var re = new RegExp("" +val+ "=([^&?]*)", "ig");
                return ((uri.match(re))?(uri.match(re)[0].substr(val.length+1)):null);
            }
        };
    var code = request.QueryString("code");
    var storage=window.localStorage;
    var openid = storage.getItem("openid");
    $.ajax("http://tlhe.cn/web/getSignInfo", {
        data:{
            'code' : code, 'openid' : openid, scenePage : 1
        },
        type:"GET",
        dataType: 'jsonp',
        jsonp:"callback",
        crossDomain: true,
        success: function(data) {
            var ret = data.ret;
            var timestamp = ret.timestamp;
            var nonceStr = ret.noncestr;
            var signature = ret.signature;
            wx.config({
                debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                appId: 'wx7968b1618f078cc4', // 必填，公众号的唯一标识
                timestamp: timestamp, // 必填，生成签名的时间戳
                nonceStr: nonceStr, // 必填，生成签名的随机串
                signature: signature,// 必填，签名
                jsApiList: ['chooseImage','uploadImage','getLocalImgData','downloadImage'],
                success:function(){
                    alert("配置成功")
                },
                fail:function(){
                    alert("配置失败")
                }
            });
            wx.chooseImage({
                count: 1, // 默认9
                sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
                success: function (res) {
                    var localIds = res.localIds; // 返回选定照片的本地ID列表，localId可以作为img标签的src属性显示图片
                    wx.uploadImage({
                        localId: localIds, // 需要上传的图片的本地ID，由chooseImage接口获得
                        isShowProgressTips: 1, // 默认为1，显示进度提示
                        success: function (res) {
                            var serverId = res.serverId; // 返回图片的服务器端ID
                            wx.downloadImage({
                                serverId: serverId, // 需要下载的图片的服务器端ID，由uploadImage接口获得
                                isShowProgressTips: 1, // 默认为1，显示进度提示
                                success: function (res) {
                                    var localId = res.localId; // 返回图片下载后的本地ID
                                    wx.getLocalImgData({
                                        localId: localId, // 图片的localID
                                        success: function (res) {
                                            var localData = res.localData; // localData是图片的base64数据，可以用img标签显示
                                            saveImg(localData, openid); //保存图片到本地
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            });
        }
    });

}

function saveImg(localData, openid) {
    $.ajax("http://tlhe.cn/web/saveImg", {
        data:{
            'localData' : localData, 'openid' : openid
        },
        type:"GET",
        success: function(data) {
            layer.msg('保存成功！', {icon: 1});
        }
    });
}