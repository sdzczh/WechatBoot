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
