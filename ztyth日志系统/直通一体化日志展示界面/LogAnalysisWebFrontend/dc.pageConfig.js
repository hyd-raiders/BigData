dc.project = dc.project || {};
//+++++++++++++++++++++++++++++++修改区域++++++++++++++++++++++++++++++++++++++++++
//后端Url，如http://192.168.10.128:8080/javaBackendDemo
dc.project.backendUrl = "http://localhost:8080/logAnalysis"; //todo:需要根据实际地址修改
//前端根地址Url, 如http://localhost:8080/ApolloFrontend
dc.project.frontendUrl = "http://localhost:8080/LogAnalysisWebFrontend"; //需要根据实际地址修改
//+++++++++++++++++++++++++++++++较固定区域++++++++++++++++++++++++++++++++++++++++++
//公共参数控件url
dc.project.commonParamUrl = dc.project.backendUrl+"/publicCode/getPublicCode";
//于跨域调用的外层框架Invoke页面地址，如 http://192.168.10.128/apollo/invoke.html
dc.out.invokeUrl = "";
//权限数据获取地址
dc.project.permissionUrl = dc.project.backendUrl+"/permission/getListPageFun";
//页面无权限时的提示页面
dc.project.permissionInfoUrl = dc.project.frontendUrl + "/dcUIFramework/ApolloFrontend/noPermission.html";
//未登陆后跳转的页面
dc.project.loginUrl="http://192.168.12.6:8088/cruiser/views/dcDefault/system/login.html";
//没有token的提示页面
dc.project.noTokenUrl="http://192.168.12.6:8088/cruiser/views/dcDefault/system/login.html";
//是否跨域
dc.project.isCrossdomain = false;
//验证登录
dc.project.notLogin=false;
//验证token
dc.project.notToken=false;
//外部扩展
dc.out = dc.out || {};
//重写跳转
dc.out.redirect = dc.out.redirect || function (url, title) {
 location.href = url;
}
//运行模式 临时修改为开发模式
dc.project.RunMode = dc.project.Enum.dev;
//外部初始化，无需修改，项目扩展用
dc.out.init = function () {
    $(".dc-searchHidden").click(function () {
        var button = $(this);
        var dc_panel_body = button.parent().next(".dc-panel-body");
        var display = dc_panel_body.css("display");
        if (display == "none") {
            dc_panel_body.show();
            button.find(".l-btn-text").text("隐藏查询");
            dc.autoHeight();
            $(".dc-panel-head").css({
                "borderBottomStyle": "dotted",
                "borderBottomColor": "#dadada"
            });
        } else {
            dc_panel_body.hide();
            button.find(".l-btn-text").text("显示查询");
            dc.autoHeight();
            $(".dc-panel-head").css({
                "borderBottomStyle": "solid",
                "borderBottomColor": "rgb(230,231,235)"
            });
        }
    });
};
//执行登录未验证通过的动作
dc.out.notLogin=function(){
    console.log("未登录系统");
    if(dc.project.notLogin){
        //跳转至登录页面
        window.location.href=dc.project.loginUrl;
    }
}
//执行token未验证通过的动作
dc.out.notToken=function(){
    console.log("token未验证通过");
    if(dc.project.notToken){
        //跳转至登录页面
        window.location.href=dc.project.noTokenUrl;
    }
}
//+++++++++++++++++++++++++++++++全局枚举区域++++++++++++++++++++++++++++++++++++++++++
var Enum = Enum || {};
Enum.PageNameList = [{"value":"","text":"--请选择--","selected":true},
{"value":"登录","text":"登录","selected":false},
{"value":"修改密码","text":"修改密码","selected":false},
{"value":"备案资料库","text":"备案资料库","selected":false},
{"value":"一体化备案","text":"一体化备案","selected":false},
{"value":"手册管理","text":"手册管理","selected":false},
{"value":"报关单确认","text":"报关单确认","selected":false},
{"value":"进口报关单","text":"进口报关单","selected":false},
{"value":"出口报关单","text":"出口报关单","selected":false},
{"value":"一体化核销","text":"一体化核销","selected":false},
{"value":"权限设置","text":"权限设置","selected":false}
];

//Enum.YesNo = [{"value":"1","text":"是","selected":false},{"value":"2","text":"否","selected":false}];
//Enum.hbBaseNo = [{"value":"hs1","text":"hs11111111","selected":false},{"value":"hs2","text":"hs2222222","selected":false},{"value":"hs333333","text":"hs33333333","selected":false},{"value":"HS2326400888","text":"HS2326400888","selected":false}];