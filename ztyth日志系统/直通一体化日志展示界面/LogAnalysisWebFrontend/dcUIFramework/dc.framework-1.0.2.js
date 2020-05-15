/// <reference path="typings/globals/dc.framework/index.d.ts" />
(function () {
    //框架使用的全局私有对象
    var _privateObj = {
        letIE9: null
    };
    /**
     * 前端框架核心对象
     */
    function dcjet() {
        this.util = new dcUtil();
        var _util = this.util;
        this.info = new dcInfo();
        this.tabs = new dcTabs();
        this.dialog = new dcDialog();
        this.userInfo = null;//用户上下文对象
        this.token = null;//token
        //控件区************************************************************************************************************///
        //公共配置信息,congfig对象中的属性只有框架有权限进行增加/删除，开发人员不可在config对象中增加/删除属性
        this.config = {
            control: {},
            page: { pagecode: "" },
            fieldValid: {}
        };
        //私有全局配置
        var _option = {
            labelPosition: "before",
            labelAlign: 'right',
            width: "70%",
            height: 22,
            validateOnCreate: false,
            validateOnBlur: true
        };
        //控件类型map
        var _controlTypeMap = {
            textbox: "textbox",
            textarea: "textbox",
            password: "textbox",
            button: "linkbutton",
            intbox: "numberbox",
            decimalbox: "numberbox",
            intspinnerbox: "numberspinner",
            select: "combobox",
            mselect: "combobox",
            autocomplete: "combobox",
            mautocomplete: "combobox",
            datagrid: "datagrid",
            fileUpload: "filebox",
            codeautocomplete: "combobox",
            mcodeautocomplete: "combobox",
            datebox: "datebox",
            datetimebox: "datetimebox",
            tabs: "tabs",
            checkbox: "checkbox",
            mcheckbox: "checklistbox",
            radio: "radiobox"
        };
        //私有捷通控件默认配置
        var _control = {
            textbox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            textarea: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: 1000,
                height: 60,
                multiline: true,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            password: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                type: "password",
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            intbox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                validType: "int"
            },
            decimalbox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                precision: 5,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                validType: "number"
            },
            intspinnerbox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                validType: "int"
            },
            datagrid: {
                idField: "oid",
                fitColumns: true,
                rownumbers: false,
                pagination: true,
                striped: true,
                pageNumber: 1,
                fit: true,
                queryParams: {},
                pageSize: 15,
                pageList: [10, 15, 20],
                checkbox: true,
                chkBindId: "" //checkbox绑定的字段名
            },
            select: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                editable: false,
                limitToList: true,
                mode: "local",
                textField: "text",
                valueField: "value",
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            mselect: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                editable: false,
                multiple: true,
                limitToList: true,
                mode: "local",
                textField: "text",
                valueField: "value",
                panelMinHeight: 20,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            autocomplete: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                editable: true,
                multiple: false,
                limitToList: true,
                mode: "local",
                textField: "text",
                valueField: "value",
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            fileupload: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: 300,
                height: _option.height,
                buttonText: "选择文件",
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            codeautocomplete: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                editable: true,
                multiple: false,
                limitToList: true,
                mode: "local",
                valueField: "code",
                textField: "name",
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            checkbox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur
            },
            mcheckbox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                textField: "text",
                valueField: "value"
            },
            radio: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                textField: "text",
                valueField: "value"
            },
            tabs: {
                fit: true,
                border: false
            },
            datebox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                panelWidth: 200,//面板宽度默认200
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                validType: "date"
            },
            datetimebox: {
                labelPosition: _option.labelPosition,
                labelAlign: _option.labelAlign,
                width: _option.width,
                height: _option.height,
                panelWidth: 200,//面板宽度默认200
                validateOnCreate: _option.validateOnCreate,
                validateOnBlur: _option.validateOnBlur,
                validType: "datetime"
            }
        };
        //控件id-easyui控件类型map对象
        var _controlIdTypeMap = {};
        //控件编号列表
        var _controlIndexMap = { currentIndex: 0, dccList: [], ids: [] };
        /**
         * 初始化控件
         */
        this.init = function (id, option) {
            //IE9以下特殊处理            
            if (dc.project.ltIE9 && dc.util.isLtIE9()) {
                /**特殊控件类型map
                 * dctextbox：非easyui的原始textbox控件，继承部分easyui的属性和方法
                 * dcautocomplete：非easyui的特殊封装的控件，继承部分easyui的属性和方法
                 */
                _controlTypeMap.textbox = "dctextbox";
                _controlTypeMap.intbox = "dcintbox";
                _controlTypeMap.decimalbox = "dcdecimalbox";
                _controlTypeMap.autocomplete = "dcautocomplete";
                _controlTypeMap.mautocomplete = "dcautocomplete";
                _controlTypeMap.codeautocomplete = "dcautocomplete";
                _controlTypeMap.mcodeautocomplete = "dcautocomplete";
            }
            dc.config.control = dc.config.control || {};
            dc.config.page = dc.config.page || {};
            dc.config.fieldValid = dc.config.fieldValid || {};
            if (dc.project.isInitUserInfo) {
                //初始化用户上下文对象
                _initUserInfo();
            }
            if (dc.project.isInitToken) {
                //初始化token
                _initToken();
            }
            if (dc.project.isInitPermission) {
                //初始化权限
                _initpms();
            }
            //初始化控件
            if (id) {
                _initControl(id, option);
            } else {
                _initControls();
            }
            //存储pagecode和window对象的键值对
            _savePageWindow();
            //外部初始化
            if (dc.out.init) {
                dc.out.init();
            }
            //初始化回车跳转到下一个控件
            _initKeyEnterDown();
        };
        /**
         * call方法，调用某个控件的某个方法
         */
        this.call = function(id,method){
            if (!id) {
                return null;
            }
            if (id.charAt(0)=="#") {
                id = id; 
            } else {
                id = "#"+id;
            }
            var type = _controlIdTypeMap[id.replace(/#/,"")];
            try{
                var idres=$(id)[type];
            }catch(err){
                dc.util.error("参数错误")
                return false;
            }
            try{
                var res=$(id)[type](method);
            }catch(err){
                dc.util.error("该对象没有此方法")
                return false;
            }
            return res;
        }
        /**
         * 初始化用户登录信息
         */
        function _initUserInfo() {
            var userInfo = _util.getCookie("userInfo");
            if (userInfo) {
                var parseUserInfo = $.parseJSON(decodeURIComponent(userInfo));
                if (parseUserInfo.hasOwnProperty("user")) {
                    dc.userInfo = parseUserInfo.user;
                } else {
                    dc.userInfo = null;
                    if (dc.out.notLogin) {//如果没登录则执行无token的函数
                        dc.out.notLogin();
                    }
                }
            } else {
                dc.userInfo = null;
                if (dc.out.notLogin) {
                    dc.out.notLogin();
                }
            }
        }
        /**
         * 初始化token
         * */
        function _initToken() {
            var token = _util.getCookie("token");
            if (token) {
                dc.token = token;
            } else {
                if (dc.out.notToken) {//如果没有token则执行无token的函数
                    dc.out.notToken();
                }
            }
        }
        /**
         * 初始化权限
         */
        function _initpms() {
            var pmsData = getAuthority(dc.config.page.pagecode);
            if (pmsData) {
                var controls = $("[dpm],[dc-pms]");
                if (pmsData.page.show === false) { //页面不可见
                    window.location.href = dc.project.permissionInfoUrl; //跳转至无权限的提示页面
                    return false;
                }
                for (var i = 0; i < controls.length; i++) {
                    var c = $(controls[i]);
                    for (var j = 0; j < pmsData.fun.length; j++) {
                        if (pmsData.fun[j].funCode == (controls[i].getAttribute("dpm") || controls[i].getAttribute("dc-pms"))) {
                            if (pmsData.fun[j].show === false) {
                                c.remove();
                            }
                            if (pmsData.fun[j].enable === false) {
                                c.setAttribute("disabled", "disabled");//禁用
                            }
                        }
                    }
                }
            }
        }
        /**
         * 初始化控件
         */
        function _initControls() {
            //找到页面所有可见dcc控件
            var controls = $("[dcc],[dc-control],[dcc='datagrid'],[dc-control='datagrid']");
            //dc控件初始化
            for (var i = 0; i < controls.length; i++) {
                var id = controls[i].id;
                var c = $(controls[i]);
                var dcType = c.attr("dcc").replace(/-/g, "") || c.attr("dc-control").replace(/-/g, "");
                var ecType = _controlTypeMap[dcType];
                _controlIdTypeMap[id] = ecType;
                var option = _getControlOption(id, dcType, ecType);
                //执行特殊控件的初始化
                var iscontinue = _specialInit(c, dcType, option);
                if (iscontinue) {
                    //执行easyui控件的初始化
                    c[ecType](option);
                    initBlurEvent(c, option);
                }
            }
        }
        /**
        * 初始化控件
        */
        function _initControl(id, options) {
            //找到页面所有可见dcc控件
            id = id.replace(/#/,"");
            var controls = $("[id=" + id + "]");
            //dc控件初始化
            for (var i = 0; i < controls.length; i++) {
                var id = controls[i].id;
                var c = $(controls[i]);
                var dcType = c.attr("dcc").replace(/-/g, "") || c.attr("dc-control").replace(/-/g, "");
                var ecType = _controlTypeMap[dcType];
                _controlIdTypeMap[id] = ecType;
                var option = _getControlOption(id, dcType, ecType, options);
                //执行特殊控件的初始化
                var iscontinue = _specialInit(c, dcType, option);
                if (iscontinue) {
                    //执行easyui控件的初始化
                    c[ecType](option);
                    initBlurEvent(c, option);
                }
            }
        }
        /**
         * 初始化Blur事件
         */
        function initBlurEvent(jqObj, option) {
            $("input", jqObj.next("span")).blur(function () {
                if (option.onBlur) {
                    option.onBlur();
                }
            });
        }
        /**
         * 回车跳转到下一个控件
         */
        function _initKeyEnterDown() {
            var _inputs = $(".dc-data-wrapper,.dc-search-wrapper").find('input:text:enabled:not([readonly]):visible,select:visible,input[autocomplete="off"],button#btnSave');
            if (_inputs.length == 0)
                return;
            $(_inputs).on('keydown',
                function (event) {
                    //回车
                    if (event.keyCode == 13) {
                        var currentIndex = _inputs.index($(this));
                        var nextElement = _inputs.get(currentIndex + 1);
                        if (nextElement) {
                            while ($(nextElement).attr("readonly") || $(nextElement).attr("disabled")) {
                                var currentIndex = _inputs.index($(nextElement));
                                nextElement = _inputs.get(currentIndex + 1);
                            }
                            if (nextElement.className.indexOf("textbox-selectend") > 0 && nextElement.type == "text") {
                                if (nextElement.focus) {
                                    var txtValue = nextElement.value;
                                    nextElement.value = "";
                                    nextElement.focus();
                                    nextElement.value = txtValue;
                                }
                            } else {
                                if (nextElement.id == "btnSave") {
                                    if (nextElement.focus) {
                                        nextElement.focus();
                                        return false;
                                    }
                                } else {
                                    if (nextElement.select) {
                                        nextElement.select();
                                    }
                                }
                            }
                        } else {
                            $(this).parents(".dc-panel").find(".dc-btn-search:last,.dc-btn-ok:last").click();
                        }
                        return true;
                    }
                });
            $(_inputs).eq(0).focus();
        }
        /**
         * 存储pagecode和window对象的键值对
         */
        function _savePageWindow() {
            var onlywindow = _getCurrentDomianTopWindow();//当前域名下的最外层window对象
            onlywindow.dc.pages = onlywindow.dc.pages || {};
            dc.config.page = dc.config.page || {};
            if (!dc.config.page.pagecode) {
                dc.util.warn("当前页面未设置pagecode:" + location.href);
            } else {
                onlywindow.dc.pages[dc.config.page.pagecode] = window;//把当前的window对象保持至顶层window对象的dcPages对象中
            }
        }
        /**
         * 返回控件的配置信息，包含默认配置及用户自定义配置
         */
        function _getControlOption(id, type, ecType, options) {
            var option = _control[type] || {};//框架默认配置
            if (options) {
                dc.config.control[id] = options;
            }
            option = dc.util.cloneObjectPro(option, dc.config.control[id]);            
            var dcm = $("#" + id)[0].getAttribute("dcm");
            if (dcm) {
                if (dc.config.fieldValid) {
                    var validOption = dc.config.fieldValid[dcm];
                    option = dc.util.cloneObjectPro(option, validOption);
                    var novalidate = (dc.config.control[id] || {}).novalidate;
                    if (validOption != undefined && validOption.required && !novalidate) {
                        var th = $("#" + id).parent().prev();
                        if (th.length > 0) {
                            $("#" + id).parent().prev().append("<span class='dc-required'>*</span>");
                        }
                    }
                }
            }
            //判断是否跨域
            if (dc.project.isCrossdomain) {
                option.method = "get";
                option.dataType = "jsonp";
            }
            //设置请求类型
            else if (option.url && dc.util.isUrl(option.url)) {
                if (!option.method)//用户未设置请求类型
                {
                    option.method = "post";
                }
            } else {
                if (!option.method)//用户未设置请求类型
                {
                    option.method = "get";
                }
            }
            //一些控件属性的特殊处理
            switch (type) {
                case "datagrid"://dc表格控件
                    {
                        var chkColumn;
                        option.frozenColumns = option.frozenColumns || [[]];
                        //设置checkbox列
                        if (option.checkbox) {
                            chkColumn = { checkbox: true, field: "ck" };
                            option.frozenColumns[0].splice(0, 0, chkColumn);
                        }
                        if (option.columns) {
                            for (var c in option.columns[0]) {
                                option.columns[0][c].width = option.columns[0][c].width || 100;
                            }
                        }
                        //增加token验证
                        if (option.onLoadSuccess) {//如果用户设置了onLoadSuccess
                            var userOnLoadSuccess = option.onLoadSuccess;
                            option.onLoadSuccess = function (res) {
                                if (!dc.util.validateToken(res)) {
                                    if (dc.out.notToken) {
                                        dc.out.notToken();
                                    }
                                } else {
                                    userOnLoadSuccess(res);
                                }
                            }
                        } else {
                            option.onLoadSuccess = function (res) {
                                if (!dc.util.validateToken(res)) {
                                    if (dc.out.notToken) {
                                        dc.out.notToken();
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "codeautocomplete": {//dc 公共参数控件
                    if (dc.project.commonParamUrl) {
                        option.url = dc.project.commonParamUrl; //Apollo 公共参数路径
                        if (option.code) {
                            option.queryParams = {
                                "catalogCode": option.code
                            }
                        }
                    }
                }
                    break;
            }
            switch (ecType) {
                case "combobox": {
                    //增加token验证
                    if (option.onLoadSuccess) {//如果用户设置了onLoadSuccess
                        var userOnLoadSuccess = option.onLoadSuccess;
                        option.onLoadSuccess = function (res) {
                            if (!dc.util.validateToken(res)) {
                                if (dc.out.notToken) {
                                    dc.out.notToken();
                                }
                            } else {
                                userOnLoadSuccess(res);
                            }
                        }
                    } else {
                        option.onLoadSuccess = function (res) {
                            if (!dc.util.validateToken(res)) {
                                if (dc.out.notToken) {
                                    dc.out.notToken();
                                }
                            }
                        }
                    }
                    if (option.onBeforeLoad) {//如果用户设置了onBeforeLoad
                        var useronBeforeLoad = option.onBeforeLoad;
                        option.onBeforeLoad = function (param) {
                            useronBeforeLoad(param);
                        }
                    } else {
                        option.onBeforeLoad = function (param) {
                            var length = option.inputSize;
                            if (param.q) {
                                var qLength = param.q.length;
                                if (qLength < length) {
                                    return false;
                                }
                            } else {
                                return false;
                            }
                        }
                    }
                } break;
            }
            if (option.url) {
                if (!option.queryParams) {
                    option.queryParams = {};
                }
                option.queryParams.token = dc.token;
            }
            return option;
        }
        /**
         * 执行特殊控件的初始化
         */
        function _specialInit(c, dcType, option) {
            //是否继续执行easyui控件的初始化
            var iscontinue = true;
            var id;
            switch (dcType) {
                case "fileupload": {
                    var _option = {
                        onSubmit: function (param) {
                            var file = $("#" + id).filebox("getValue");
                            if (file) {
                                param = option.param;
                                if (option.onSubmit)
                                    option.onSubmit();
                                return true;
                            } else {
                                dc.info.warn("请选择需要上传的文件!");
                                return false;
                            }
                        },
                        success: function (result) {
                            var obj = (typeof result == "string" ? JSON.parse(result) : result);
                            if (result == true || result == "true") {
                                dc.info.show("上传成功!");
                            } else {
                                dc.info.show("上传失败!");
                            }
                            if (option.success)
                                option.success(obj.result);
                        }
                    };
                    id = c.prop("id");
                    var dcm = c.attr("dcm") || c.attr("dc-model") || "";
                    c.after('<form id="' + id + '_fm" method="' + option.method + '" enctype="multipart/form-data"><input name="' + id + '" id="' + id + '"  dcm="' + dcm + '" />&nbsp;<a id="' + id + '_btnupload"  >上传</a></form>').remove();
                    $("#" + id).filebox(option);
                    $("#" + id + "_btnupload").linkbutton({
                        width: 60,
                        height: 22,
                        onClick: function () {
                            $("#" + id + "_fm").form("submit",
                                {
                                    url: option.url,
                                    onSubmit: _option.onSubmit,
                                    success: _option.success
                                });
                        }
                    });
                    iscontinue = false;
                    break;
                }
                case "tabs": {
                    c.find("iframe").css({
                        height: "100%",
                        width: "100%",
                        border: "none",
                        overflow: "hidden"
                    });
                    iscontinue = true;
                    var oldSet = option.onSelect;
                    option.onSelect = function (title, index) {
                        var iframe = $(this).tabs("getTab", index).find("iframe");
                        if (!iframe.prop("src")) {
                            //显示loading界面
                            if ($("#loadingDiv").length == 0) {
                                var _PageHeight = document.documentElement.clientHeight,
                                    _PageWidth = document.documentElement.clientWidth;
                                var _LoadingTop = _PageHeight > 61 ? (_PageHeight - 61) / 2 : 0,
                                    _LoadingLeft = _PageWidth > 215 ? (_PageWidth - 215) / 2 : 0;
                                var _LoadingHtml = '<div id="loadingDiv"><div id="loadingCon" style="left: ' + _LoadingLeft + 'px; top:' + _LoadingTop + 'px; ">努力加载中...</div></div>';
                                $("body").append(_LoadingHtml);
                                $("#loadingDiv").show();
                            } else {
                                $("#loadingDiv").show();
                            }
                            setTimeout(function () {
                                iframe.prop("src", iframe.data("src"));
                            }, 1);
                        }
                        if (oldSet) {
                            oldSet();//执行用户设定的onSelect函数
                        }
                    };
                    break;
                }
                case "datebox": {
                    _specialDateInit(option);
                    iscontinue = true;
                    break;
                }
            }
            return iscontinue;
        }
        /**
         * 日期初始化，特殊处理，可变态输入日期，不加-或者/
         * @param option 控件的配置对象
         */
        function _specialDateInit(option) {
            //to do 日期回车后会默认选择今天日期
            option.formatter = function (date) {
                option.dcformatter = option.dcformatter || "yyyy-MM-dd";
                var y = date.getFullYear();
                var m = date.getMonth() + 1;
                var d = date.getDate();
                m = m < 10 ? ('0' + m) : m;
                d = d < 10 ? ('0' + d) : d;
                var value = option.dcformatter.replace("yyyy", y);
                value = value.replace("MM", m);
                value = value.replace("dd", d);
                return value;
            };
            option.parser = function (s) {
                if (!s) return new Date();
                var y = 0, m = 0, d = 0;
                if (s.indexOf('-') >= 0) {
                    var ss = s.split('-');
                    y = parseInt(ss[0], 10);
                    m = parseInt(ss[1], 10);
                    d = parseInt(ss[2], 10);
                }
                else if (s.indexOf('/') >= 0) {
                    var ss2 = s.split('/');
                    y = parseInt(ss2[0], 10);
                    m = parseInt(ss2[1], 10);
                    d = parseInt(ss2[2], 10);
                } else {
                    y = parseInt(s.substring(0, 4), 10);
                    m = parseInt(s.substring(4, 6), 10);
                    d = parseInt(s.substring(6, 8), 10);
                }
                if (isNaN(m)) {
                    m = 1;
                }
                if (isNaN(d)) {
                    d = 1;
                }
                if (!isNaN(y) && !isNaN(m) && !isNaN(d)) {
                    return new Date(y, m - 1, d);
                } else {
                    return new Date();
                }
            };
        }
        //取值赋值区************************************************************************************************************///
        /**
         *  根据控件获取
         */
        var _getControlType = function (control) {
            var cid = control.id || control[0].id;
            if (!cid && dc.project.RunMode == dc.project.Enum.dev) {
                dc.util.log("获取控件类型时遇到问题 ： 无法获取控件的id ：" + control);
            }
            //通过初始化进行优化
            if (cid) {
                var type = _controlIdTypeMap[cid];
                if (type)
                    return type;
            }
            if (!control.hasClass)
                control = $(control);
            if (control) {
                var _type = null;
                if (control.hasClass("easyui-textbox")) {
                    _type = "textbox";
                }
                else if (control.hasClass("easyui-combobox")) {
                    _type = "combobox";
                }
                else if (control.hasClass("easyui-numberbox")) {
                    _type = "numberbox";
                }
                else if (control.hasClass("datebox-f")) {
                    _type = "datebox";
                }
                else if (control.hasClass("datetimebox-f")) {
                    _type = "datetimebox";
                }
                else if (control.hasClass("easyui-tagbox")) {
                    _type = "tagbox";
                }
                else if (control.hasClass("easyui-passwordbox")) {
                    _type = "passwordbox";
                }
                else if (control.hasClass("easyui-filebox")) {
                    _type = "filebox";
                }
                else if (control.hasClass("datagrid-f")) {
                    _type = "datagrid";
                }
                else if (control.hasClass("easyui-checkbox")) {
                    _type = "checkbox";
                }
                else if (control.hasClass("easyui-checklistbox")) {
                    _type = "checklistbox";
                }
                else if (control.hasClass("easyui-radiobox")) {
                    _type = "radiobox";
                }
                return _type;
            }
            return null;
        };
        /**
         * 获取控件类型
         */
        this.getControlType = _getControlType;
        /**
         * 获取控件值
         */
        var _getValue = function (control) {
            if (!control) return null;
            //正常取值
            var type = _getControlType(control);
            /*textbox
             combobox
             numberbox
             datebox
             datetimebox
             tagbox
             passwordbox
             filebox
             checkbox
             m-checkbox
             radio*/
            var value = null;
            switch (type) {
                case "dctextbox":
                    value = control.dctextbox('getValue');
                    break;
                case "dcautocomplete":
                    value = control.dcautocomplete('getValue');
                    break;
                case "textbox":
                    value = control.textbox('getValue');
                    break;
                case "combobox":
                    value = control.combobox('getValues');
                    break;
                case "numberbox":
                    value = control.numberbox('getValue');
                    break;
                case "datebox":
                    value = control.datebox('getValue');
                    break;
                case "datetimebox":
                    value = control.datetimebox('getValue');
                    break;
                case "numberspinner":
                    value = control.numberspinner('getValue');
                case "tagbox":
                    value = control.tagbox('getValues');
                    break;
                case "passwordbox":
                    value = control.passwordbox('getValue');
                    break;
                case "datagrid":
                    value = control.datagrid('getValue');
                    break;
                case "checkbox":
                    value = control.checkbox("getValue");
                    break;
                case "checklistbox":
                    value = control.checklistbox('getValues');
                    break;
                case "radiobox":
                    value = control.radiobox('getValue');
                    break;
                case "filebox":
                    value = control.filebox('getText');
                    break;
                default: {
                    if (control.is(":input"))
                        value = control.val();
                    else
                        value = control.text();
                    break;
                }
            }
            return value;
        };
        /**
         *  内部赋值实现，只用于单纯的赋值，判断控件类型，进行赋值
         */
        var _setValue = function (controls, value) {
            var type = _getControlType(controls);
            /*textbox
             combobox
             numberbox
             datebox
             datetimebox
             tagbox
             passwordbox
             filebox
             checkbox
             m-checbox
             radio*/
            switch (type) {
                case "dctextbox":
                    controls.dctextbox('setValue', value);
                    break;
                case "dcautocomplete":
                    controls.dcautocomplete('setValue', value);
                    break;
                case "textbox": {
                    if (value) {
                        controls.textbox('setValue', value);
                    } else {
                        controls.textbox('clear');
                    }
                    break;
                }
                case "combobox":
                    if (value) {
                        if (_util.isArray(value)) {
                            controls.combobox('setValues', value);
                        }
                        else {
                            controls.combobox('setValue', value);
                        }
                    }
                    else
                        controls.combobox('clear');
                    break;
                case "numberbox":
                    if (value)
                        controls.numberbox('setValue', value);
                    else
                        controls.numberbox('clear');
                    break;
                case "numberspinner":
                    if (value)
                        controls.numberspinner('setValue', value);
                    else
                        controls.numberspinner('clear');
                    break;
                case "datebox":
                    if (value)
                        controls.datebox('setValue', _util.dateFormatter(value, dc.project.dateFormat));
                    else
                        controls.datebox('clear');
                    break;
                case "datetimebox":
                    if (value)
                        controls.datetimebox('setValue', _util.dateFormatter(value, dc.project.datetimeFormat));
                    else
                        controls.datetimebox('clear');
                    break;
                case "tagbox":
                    if (value) {
                        if (_util.isArray(value)) {
                            controls.tagbox('setValues', value);
                        }
                        else {
                            controls.tagbox('setValue', value);
                        }
                    }
                    else
                        controls.tagbox('clear');
                    break;
                case "passwordbox":
                    if (value)
                        controls.passwordbox('setValue', value);
                    else
                        controls.passwordbox('clear');
                    break;
                case "datagrid":
                    controls.datagrid('setValue', value);
                    break;
                case "checkbox":
                    controls.checkbox("setValue", value);
                    break;
                case "checklistbox":
                    controls.checklistbox('setValues', value);
                    break;
                case "radiobox":
                    controls.radiobox('setValue', value);
                    break;
                default: {
                    if (controls.is(":input")) {
                        controls.val(value);
                    } else {
                        value = value || "-";
                        controls.text(value);
                    }
                    break;
                }
                //throw  new Error("控件不能通过setValue来赋值");
            }
        };
        //获取当前域下最外层window对象
        function _getCurrentDomianTopWindow() {
            var localwindow = window;
            var fatherWindow = window.parent;
            while (true) {
                try {
                    if (!fatherWindow.dc) {
                        return localwindow;
                    }
                    var ss = fatherWindow.location.host;//无法获取父页的origin对象，因为是不同的域
                    if (ss == undefined) {
                        return localwindow;//返回当前最外层可访问的同域window对象
                    }
                    if (ss != localwindow.location.host) {
                        return localwindow;//返回当前最外层可访问的同域window对象
                    }
                }
                catch (e) {
                    return localwindow;//返回当前最外层可访问的同域window对象
                }
                if (localwindow == fatherWindow) {
                    return localwindow;//同域的外层window对象
                } else {
                    localwindow = fatherWindow;
                    fatherWindow = fatherWindow.parent;
                }
            }
        }
        /**
         * 通过页面code获取权限数据
         * @param pagecode 页面code
         */
        function getAuthority(pagecode) {
            var resData;
            $.ajax({
                url: dc.project.permissionUrl,
                type: "post",
                data: { pageCode: pagecode, token: dc.token },
                async: false,
                success: function (res) {
                    if (!dc.util.validateToken(res)) {
                        if (dc.out.notToken) {
                            dc.out.notToken();
                        }
                    } else {
                        resData = res.pms;
                    }
                }
            });
            return resData;
        }
        /**
         * 获取值（数组）
         * @param selectorOrArray 选择器或者数组,如 #id .class
         */
        this.getValues = function (selectorOrArray) {
            //数组时，返回map
            if (this.util.isArray(selectorOrArray)) {
                var map = {};
                for (var i = 0; i < selectorOrArray.length; i++) {
                    map[selectorOrArray[i]] = this.getValues(selectorOrArray[i]);
                }
                return map;
            }
            else {
                var values = new Array();
                var controls = $(selectorOrArray);
                controls.each(function () {
                    values.push(_getValue($(this)));
                });
                return values;
            }
        };
        /**
         * 获取值(多个值时，返回一个)
         */
        this.getValue = function (selectorOrArray) {
            //数组时，返回map
            if (this.util.isArray(selectorOrArray)) {
                var map = {};
                for (var i = 0; i < selectorOrArray.length; i++) {
                    map[selectorOrArray[i]] = this.getValue(selectorOrArray[i]);
                }
                return map;
            }
            else {
                if (selectorOrArray.indexOf("#") < 0 && selectorOrArray.indexOf(".") < 0) {//单词前缀没有#或者.
                    selectorOrArray = "#" + selectorOrArray;
                }
                var control = $(selectorOrArray);
                return _getValue(control);
            }
        };
        /**
         * 设置值 （可以是数组）
         */
        this.setValue = function (selectorOrMap, value) {
            if (!selectorOrMap) throw new Error("setValue 必须带参数");
            //map赋值 没有Value值认为是map方式赋值
            if (!value && selectorOrMap && typeof selectorOrMap != 'string') {
                for (var key in selectorOrMap) {
                    if (key && selectorOrMap[key])
                        this.setValue(key, selectorOrMap[key]);
                }
                return;
            }
            if (selectorOrMap.indexOf("#") < 0 && selectorOrMap.indexOf(".") < 0) {//单词前缀没有#或者.
                selectorOrMap = "#" + selectorOrMap;
            }
            //正常赋值
            var controls = $(selectorOrMap);
            //控件只有一个时，直接赋值
            if (controls.length == 1) {
                _setValue(controls, value);
            }
            else {
                //判断值是否是数组，如果是数组，如果不是数组，遍历控件赋值相同，如果是数组，进行依次赋值
                if (_util.isArray(value) == false || value == null) {
                    controls.each(function () {
                        _setValue($(this), value);
                    });
                }
                else {
                    if (controls.length != value.length) {
                        throw new Error("赋值时控件数量和数组值的数量应该一致:[" + controls.length + ":" + value.length + "]");
                    }
                    for (var i = 0; i < controls.length; i++) {
                        _setValue($(controls[i]), value[i]);
                    }
                }
            }
        };
        /**
         *  model赋值，只支持Id选择器
         */
        this.setModel = function (idSelector, model) {
            if (arguments.length !== 2) {
                throw "setModel必须传入2个参数";
            }
            //找到容器中的对象，进行赋值取值
            var controls = $(idSelector).find("[dcm],[dc-model]");
            //增加清空功能
            if (!model) {
                $.each(controls, function (n, c) {
                    c = $(c);
                    var dcmVal = c.attr("dcm") || c.attr("dc-model");
                    if (dcmVal) {
                        _setValue(c, null);
                    }
                });
            }
            var dcmControlMap = {};
            //获取map
            $.each(controls, function (n, c) {
                c = $(c);
                var dcmVal = c.attr("dcm") || c.attr("dc-model");
                if (dcmVal) {
                    dcmControlMap[dcmVal] = $(c);
                }
            });
            //遍历model，赋值
            for (var key in model) {
                var control = dcmControlMap[key];
                if (control) {
                    _setValue(control, model[key]);
                }
            }
        };
        /**
         * model 取值， 参数model可不传
         */
        this.getModel = function (idSelector, model) {
            if (!dc.util.isString(idSelector)) {
                throw "getModel函数的idSelector参数必须传入";
            }
            //找到容器中的对象，进行赋值取值
            var controls = $(idSelector).find("[dcm],[dc-model]");
            var json = new Object();
            //支持model
            if (model) {
                json = model;
            }
            //获取map
            $.each(controls, function (n, c) {
                c = $(c);
                var dcmVal = c.attr("dcm") || c.attr("dc-model");
                if (dcmVal) {
                    var val = _getValue($(c));
                    //数组自动转化
                    if (_util.isArray(val)) {
                        val = val.join(',');
                    }
                    json[dcmVal] = val;
                }
            });
            return json;
        };
        /**
         * Ajax post 封装
         * @param url 请求的url
         * @param data 请求传递的参数
         * @param type 返回的数据类型
         * @param successfn 请求成功后的回掉函数
         * @param errorfn 请求失败后的回掉函数
         */
        this.post = function (url, data, successfn, type, errorfn) {
            if (data) {
                data.token = dc.token;
            } else {
                data = { token: dc.token };
            }
            if (!dc.util.isUrl(url)) {
                throw "请传入url路径";
            }
            type = type || "json";
            $.ajax({
                url: url,
                type: "post",
                async: true,
                data: data,
                dataType: type,
                success: function (res) {
                    if (!dc.util.validateToken(res)) {
                        if (dc.out.notToken) {
                            dc.out.notToken();
                        }
                    } else {
                        successfn(res);
                    }
                },
                error: errorfn
            });
        }
        /**
         * Ajax get 封装
         * @param url 请求的url
         * @param data 请求传递的参数
         * @param type 返回的数据类型
         * @param successfn 请求成功后的回掉函数
         * @param errorfn 请求失败后的回掉函数
         */
        this.get = function (url, data, successfn, type, errorfn) {
            if (data) {
                data.token = dc.token;
            } else {
                data = { token: dc.token };
            }
            if (!dc.util.isUrl(url)) {
                throw "请传入url路径";
            }
            type = type || "json";
            $.ajax({
                url: url,
                type: "get",
                async: true,
                data: data,
                dataType: type,
                success: function (res) {
                    if (!dc.util.validateToken(res)) {
                        if (dc.out.notToken) {
                            dc.out.notToken();
                        }
                    } else {
                        successfn(res);
                    }
                },
                error: errorfn
            });
        }
        /**
         * Ajax post 同步封装
         * @param url 请求的url
         * @param data 请求传递的参数
         * @param type 返回的数据类型
         * @param successfn 请求成功后的回掉函数
         * @param errorfn 请求失败后的回掉函数
         */
        this.syncPost = function (url, data, type) {
            if (data) {
                data.token = dc.token;
            } else {
                data = { token: dc.token };
            }
            if (!dc.util.isUrl(url)) {
                throw "请传入url路径";
            }
            var res = null;
            type = type || "json";
            $.ajax({
                url: url,
                type: "post",
                async: false,
                data: data,
                dataType: type,
                success: function (data) {
                    if (!dc.util.validateToken(data)) {
                        if (dc.out.notToken) {
                            dc.out.notToken();
                        }
                    } else {
                        res = data;
                    }
                }
            });
            return res;
        }
        /**
         * Ajax get 同步封装
         * @param url 请求的url
         * @param data 请求传递的参数
         * @param successfn 请求成功后的回掉函数
         * @param errorfn 请求失败后的回掉函数
         */
        this.syncGet = function (url, data, type) {
            if (data) {
                data.token = dc.token;
            } else {
                data = { token: dc.token };
            }
            if (!dc.util.isUrl(url)) {
                throw "请传入url路径";
            }
            var res = null;
            type = type || "json";
            $.ajax({
                url: url,
                type: "get",
                async: false,
                data: data,
                dataType: type,
                success: function (data) {
                    if (!dc.util.validateToken(data)) {
                        if (dc.out.notToken) {
                            dc.out.notToken();
                        }
                    } else {
                        res = data;
                    }
                }
            });
            return res;
        }
        /**
         * 获取选项列
         * @param urlOrList url或者列表
         * @param selectedItem 选中项  可空
         * @param isHasPleaseSelect 是否有请选择  默认没有
         */
        this.getSelectItemList = function (urlOrList, selectedItem, isHasPleaseSelect) {
            if (!isHasPleaseSelect) {
                isHasPleaseSelect = false;
            }
            //是列表情况时
            if (!_util.isString(urlOrList)) {
                var list = new Array();
                var selectedCount = 0;
                for (var val in urlOrList) {
                    var text = urlOrList[val];
                    var isSelected = false;
                    //暂时只支持一个选中项
                    if (selectedItem && selectedItem == val) {
                        isSelected = true;
                        selectedCount++;
                    }
                    list.push({
                        "value": val,
                        "text": text,
                        "selected": isSelected
                    });
                }
                if (isHasPleaseSelect) {
                    list.splice(0, 0, {
                        "value": "",
                        "text": "--请选择--",
                        "selected": selectedCount == 0
                    });
                }
                return list;
            }
            else {
                //直接返回url的值
                return this.syncPost(urlOrList);
            }
            //{"selected":true,"text":"","value":""}
        };
        /**
         * 自动计算表格高度
         * @param currentPanel 表格父级标签的id或者class。默认为.dc-grid-wrapper
         * @param panel 表格父级标签的同类元素的id或者class 默认为.dc-panel
         */
        this.autoHeight = function (currentPanel, panel) {
            var datagridPanel = currentPanel || ".dc-grid-wrapper";
            var tableobj = $(datagridPanel).find("table[dcc='datagrid']:hidden");//表格标签对象
            var tableid = tableobj == undefined ? undefined : tableobj.attr("id");
            if (!$("#" + tableid).hasClass("datagrid-f")) {
                return;
            }
            if (tableid != undefined && dc.config.control[tableid] != undefined && dc.config.control[tableid].autoHeight === false) //明文设定autoHeight:false时，跳过表格高度自动计算
            {
                return;
            } else  // 不设置autoHeight，或者autoHeight:true时进行表格高度自动计算
            {
                var fullheight = $(window).outerHeight(true);
                var otherPanel = $((panel || ".dc-panel") + ":visible").not($(datagridPanel));
                $(otherPanel).each(function (i, d) {
                    fullheight = fullheight - $(d).outerHeight(true);
                });
                var margintop = $(datagridPanel).css("marginTop") == undefined ? 0 : $(datagridPanel).css("marginTop").replace("px", "")
                $(datagridPanel).outerHeight(fullheight - parseInt(margintop));
                tableobj.datagrid("resize");
            }
        };
        /**
         * html编码
         * @param value 要转码的html字符串
         * @returns output 转码之后的html字符串
         */
        this.encode = _util.encode;
        /**
         * html解码
         * @param value 要解码的html字符串
         * @returns output 解码之后的html字符串
         */
        this.decode = _util.decode;
        /**
         * 字符串首尾去空
         * @returns output 首尾去空后的字符串
         */
        this.trim = _util.trim;
        /**
         * 字符串前置去空
         * @returns output 前置去空后的字符串
         */
        this.trimLeft = _util.trimLeft;
        /**
         * 字符串后置去空
         * @returns output 后置去空后的字符串
         */
        this.trimRight = _util.trimRight;
        /**
         * 执行外部框架的函数
         * @param Function fun 执行的函数
         * @param Object argObj 函数的参数键值对象
         * @returns {}
         */
        this.invokeOut = function (fun, argObj) {
            debugger;
            var argNames = [], argValues = [], i = 0;
            if (!dc.util.isString(fun)) {
                fun = fun.toString();
            }
            var currentTopWindow = _getCurrentDomianTopWindow();//获取当前域下最外层的window对象
            currentTopWindow.currentInvokeWindow = window;//把当前调用者的window对象存储至同域最外层window对象中
            //把传递过来的参数存储至数组中
            for (var n in argObj) {
                switch (n) {
                    case "successfn": {
                        window.successfn = argObj[n];
                        argObj[n] = "window.successfn";
                    }
                        break;
                    case "cancelfn": {
                        window.cancelfn = argObj[n];
                        argObj[n] = "window.cancelfn";
                    }
                        break;
                    default: {
                        argNames[i] = n;//参数名
                        argValues.push(argObj[n]);
                        i++;
                    }
                }
            }
            argNames.push("dc_invokeUrl");
            argValues.push(dc.util.getRootUrl() + "/dcUIFramework/invoke.html");
            argNames.push("dc_pagecode");
            argValues.push(dc.config.page.pagecode);
            debugger;
            var params = {
                fun: fun,
                argNames: argNames.join(','),
                argValues: argValues.join(','),
                random: Math.random()
            }
            var paramstr = $.param(params);
            if (typeof (exec_obj) == 'undefined') {
                exec_obj = document.createElement('iframe');
                exec_obj.name = 'tmp_frame';
                exec_obj.src = dc.out.invokeUrl + "?" + paramstr;
                exec_obj.style.display = 'none';
                document.body.appendChild(exec_obj);
            } else {
                exec_obj.src = dc.out.invokeUrl + "?" + paramstr;
            }
        }
        /**
         * 获取当前域下最外层window对象
         */
        this.getCurrentDomianTopWindow = _getCurrentDomianTopWindow;
        /**
         * 同域下跨页面查找
         * @param pagecode 页面code
         * @param selecter jquery选择器
         * @returns window对象或者jquery对象
         */
        this.find = function (pagecode, selecter) {
            var pageWindow = _getCurrentDomianTopWindow().dc.pages[pagecode];
            if (!pageWindow) {
                dc.util.error("该页面[" + pagecode + "]不存在");
            } else {
                if (selecter) {
                    return pageWindow.$(selecter);
                } else {
                    return pageWindow;
                }
            }
        };
        /**
         * 同域下跨页面执行
         * @param pagecode 页面code
         * @param fn 执行的函数体
         */
        this.invoke = function (pagecode, fn) {
            var pageWindow = _getCurrentDomianTopWindow().dc.pages[pagecode];
            if (!pageWindow) {
                dc.util.error("该页面[" + pagecode + "]不存在");
            } else {
                if (fn) {
                    pageWindow["fn"] = fn;
                    pageWindow["fn"]();
                } else {
                    dc.util.error("请传入需要执行的函数");
                }
            }
        };
        /**
         * 全局禁用验证函数
         * */
        this.disableValidation = function (selector) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type)
                        $(c)[type]("disableValidation");
                }
            });
        };
        /**
         * 全局启用验证
         */
        this.enableValidation = function (selector) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type) {
                        $(c)[type]("enableValidation");
                    }
                }
            });
        };
        /**
         * 全局重置验证
         */
        this.resetValidation = function (selector) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type) {
                        $(c)[type]("resetValidation");
                    }
                }
            });
        };
        /**
         *  全局清除控件的值
         */
        this.clear = function (selector) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type)
                        $(c)[type]("clear");
                    else
                        $(c).val("");
                }
            });
        };
        /**
         * 全局禁用控件
         * */
        this.disable = function (selector) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type)
                        $(c)[type]("disable");
                    else
                        $(c).attr("disabled", true);
                }
            });
        }
        /**
         * 全局启用控件
         * */
        this.enable = function (selector) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type)
                        $(c)[type]("enable");
                    else
                        $(c).attr("disabled", false);
                }
            });
        }
        /**
         * 全局置控件为只读控件
         * */
        this.readonly = function (selector, mode) {
            //找到容器中的对象，进行赋值取值
            var controls = $(selector).find(":input");
            //获取map
            $.each(controls, function (n, c) {
                var id = $(c).attr("id");
                if (id) {
                    var type = _getControlType($(c));
                    if (type)
                        $(c)[type]("readonly", mode);
                    else
                        $(c).attr("readonly", mode);
                }
            });
        }
        /**
         * iframe加载完成后执行
         */
        this.frameLoad = function (dmo) {
            if ($(dmo).prop("src")) {
                $("#loadingDiv").hide();
            }
        }
    }
    /**
     * 通用帮助类
     */
    function dcUtil() {
        this.isArray = function (object) {
            return object && typeof object === 'object' &&
                Array == object.constructor;
        };
        this.isUrl = function (str) {
            var regex = /^.*\/?\w*\.\w*\/?$/g;
            return !regex.test(str);
        };
        var _isString = function (object) {
            if (typeof (object) == 'string')
                return true;
            else
                return false;
        };
        this.isString = _isString;
        /**
         * 日期格式化
         * @param dateOrTimestamp js日期对象或者timestamp
         * @param formatter 格式化，默认值  yyyy-MM-dd
         */
        this.dateFormatter = function (dateOrTimestamp, formatter) {
            //类型转换
            if (!dateOrTimestamp) {
                return "";
            }
            var dateObj = dateOrTimestamp;
            if (!dateObj) return "";
            if (!isNaN(dateOrTimestamp))
                dateObj = new Date(dateOrTimestamp);
            if (_isString(dateOrTimestamp)) {
                return dateOrTimestamp;
            }
            //formatter默认值
            if (!formatter)
                formatter = "yyyy-MM-dd";
            var date = {
                "M+": dateObj.getMonth() + 1,
                "d+": dateObj.getDate(),
                "h+": dateObj.getHours(),
                "m+": dateObj.getMinutes(),
                "s+": dateObj.getSeconds(),
                "q+": Math.floor((dateObj.getMonth() + 3) / 3),
                "S+": dateObj.getMilliseconds()
            };
            //主逻辑
            var format = formatter;
            if (/(y+)/i.test(format)) {
                var year = dateObj.getFullYear();
                if (year > 3000) {
                    year = dateObj.getYear();
                }
                format = format.replace(RegExp.$1, (year + '').substr(4 - RegExp.$1.length));
            }
            for (var k in date) {
                if (new RegExp("(" + k + ")").test(format)) {
                    format = format.replace(RegExp.$1, RegExp.$1.length == 1
                        ? date[k] : ("00" + date[k]).substr(("" + date[k]).length));
                }
            }
            return format;
        }
        //表示全局唯一标识符 (GUID)
        var guid = function (g) {
            var arr = new Array(); //存放32位数值的数组
            if (typeof (g) == "string") { //如果构造函数的参数为字符串
                InitByString(arr, g);
            } else {
                InitByOther(arr);
            }
            //返回一个值，该值指示 Guid 的两个实例是否表示同一个值。
            this.Equals = function (o) {
                if (o && o.IsGuid) {
                    return this.ToString() == o.ToString();
                } else {
                    return false;
                }
            };
            //Guid对象的标记
            this.IsGuid = function () {
            };
            //返回 Guid 类的此实例值的 String 表示形式。
            this.ToString = function (format) {
                if (typeof (format) == "string") {
                    if (format == "N" || format == "D" || format == "B" || format == "P") {
                        return ToStringWithFormat(arr, format);
                    } else {
                        return ToStringWithFormat(arr, "D");
                    }
                } else {
                    return ToStringWithFormat(arr, "D");
                }
            };
            //由字符串加载
            function InitByString(arr, g) {
                g = g.replace(/\{|\(|\)|\}|-/g, "");
                g = g.toLowerCase();
                if (g.length != 32 || g.search(/[^0-9,a-f]/i) != -1) {
                    InitByOther(arr);
                } else {
                    for (var i = 0; i < g.length; i++) {
                        arr.push(g[i]);
                    }
                }
            }
            //由其他类型加载
            function InitByOther(arr) {
                var i = 32;
                while (i--) {
                    arr.push("0");
                }
            }
            /*
             根据所提供的格式说明符，返回此 Guid 实例值的 String 表示形式。
             N  32 位： xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
             D  由连字符分隔的 32 位数字 xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx
             B  括在大括号中、由连字符分隔的 32 位数字：{xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx}
             P  括在圆括号中、由连字符分隔的 32 位数字：(xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx)
             */
            function ToStringWithFormat(arr, format) {
                switch (format) {
                    case "N":
                        return arr.toString().replace(/,/g, "");
                    case "D":
                        var str = arr.slice(0, 8) + "-" + arr.slice(8, 12) + "-" + arr.slice(12, 16) + "-" + arr.slice(16, 20) + "-" + arr.slice(20, 32);
                        str = str.replace(/,/g, "");
                        return str;
                    case "B":
                        var str = ToStringWithFormat(arr, "D");
                        str = "{" + str + "}";
                        return str;
                    case "P":
                        var str = ToStringWithFormat(arr, "D");
                        str = "(" + str + ")";
                        return str;
                    default:
                        return new Guid();
                }
            }
        };
        var GUID = {};
        //Guid 类的默认实例，其值保证均为零。
        GUID.empty = guid();
        //初始化 Guid 类的一个新实例。
        GUID.newGuid = function () {
            /// <summary>初始化 Guid 类的一个新实例</summary>
            var g = "";
            var i = 32;
            while (i--) {
                g += Math.floor(Math.random() * 16.0).toString(16);
            }
            return new guid(g);
        };
        this.GUID = GUID;
        /**
         * 对象克隆属性，克隆对象b的属性至对象a的一个克隆
         * @param obja 基础对象
         * @param objb 需要克隆属性的对象
         * @param iProArray 克隆忽略的属性
         */
        this.cloneObjectPro = function (obja, objb, iProArray) {
            iProArray = iProArray || [];
            iProArray = iProArray.join(',');
            var objc = $.extend({}, obja);
            if (typeof (objb) == "object") {
                for (var p in objb) {
                    if (iProArray.indexOf(p) >= 0) {
                        continue; //忽略，不拷贝
                    } else {
                        objc[p] = objb[p];
                    }
                }
            }
            return objc;
        };
        /**
         * 获取当前页面的网址，不包含页面的名称
         * @returns string
         */
        this.getUrl = function () {
            var url = window.location.href;
            return url.substring(0, url.lastIndexOf("/"));
        };
        /**
         * 获取网站根路径（包含虚拟路径）
         * @returns string
         */
        this.getRootUrl = function () {
            var strProtocol = window.location.protocol + "//";
            var strFullPath = window.location.href;
            strFullPath = strFullPath.replace(strProtocol, '');
            var strPath = window.document.location.pathname;
            var pos = strFullPath.indexOf(strPath);
            var prePath = strFullPath.substring(0, pos);
            var postPath = strPath.substring(0, strPath.substr(1).indexOf('/') + 1);
            if (postPath.toLowerCase() == "/home") {
                postPath = "";
            }
            if (postPath.toLowerCase() == "/importexport") {
                postPath = "";
            }
            return (strProtocol + prePath + postPath);
        }
        /**
         * 获取Url中的参数 Json
         */
        this.getRequest = function () {
            var url = location.search; //获取url中"?"符后的字串
            var theRequest = new Object();
            theRequest.count = 0;
            if (url.indexOf("?") != -1) {
                var str = url.substr(1);
                var strs = str.split("&");
                for (var i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = decodeURIComponent(strs[i].split("=")[1]);
                    theRequest.count++;
                }
            }
            return theRequest;
        };
        /**
         * cookie取值
         * @param key 要获取的Cookie的key值
         * @returns objb 返回的Cookie键值对
         * @returns iscurrent 是否当前
         */
        this.getCookie = function (key, iscurrent) {
            var documentObj;
            if (iscurrent) {
                documentObj = document;
            }
            else {
                documentObj = dc.getCurrentDomianTopWindow().document;
            }
            var arr, reg = new RegExp("(^| )" + key + "=([^;]*)(;|$)");
            if (arr = documentObj.cookie.match(reg))
                return unescape(arr[2]);
            else
                return null;
        };
        /**
         * cookie赋值
         * @param key 要设置的Cookie的key值
         * @param value 要设置的Cookie的value值
         * @returns objb 返回的Cookie键值对
         * @returns iscurrent 是否当前
         */
        this.setCookie = function (key, value, path, iscurrent) {
            var Days = 30;
            var exp = new Date();
            exp.setTime(exp.getTime() + Days * 24 * 60 * 60 * 1000);
            if (iscurrent) {
                document.cookie = key + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=" + (path || "/");
            }
            else {
                dc.getCurrentDomianTopWindow().document.cookie = key + "=" + escape(value) + ";expires=" + exp.toGMTString() + ";path=" + (path || "/");
            }
        };
        /**
         * Json字符串转json对象
         * @param strJson 要转换的json字符串
         * @returns objb 返回解析后的Josn对象
         */
        this.parseJson = function (strJson) {
            return JSON.parse(strJson);
        };
        /**
         * Json对象转json字符串
         * @param json 要转换的json对象
         * @returns objb 返回解析后的Josn字符串
         */
        this.jsonToString = function (json) {
            return JSON.stringify(json);
        };
        /**
         * html编码
         * @param value 要转码的html字符串
         * @returns output 转码之后的html字符串
         */
        this.encode = function (value) {
            //1.首先动态创建一个容器标签元素，如DIV
            var temp = document.createElement("div");
            //2.然后将要转换的字符串设置为这个元素的innerText(ie支持)或者textContent(火狐，google支持)
            (temp.textContent != undefined) ? (temp.textContent = value) : (temp.innerText = value);
            //3.最后返回这个元素的innerHTML，即得到经过HTML编码转换的字符串了
            var output = temp.innerHTML;
            temp = null;
            return output;
        };
        /**
         * html解码
         * @param value 要解码的html字符串
         * @returns output 解码之后的html字符串
         */
        this.decode = function (value) {
            //1.首先动态创建一个容器标签元素，如DIV
            var temp = document.createElement("div");
            //2.然后将要转换的字符串设置为这个元素的innerHTML(ie，火狐，google都支持)
            temp.innerHTML = value;
            //3.最后返回这个元素的innerText(ie支持)或者textContent(火狐，google支持)，即得到经过HTML解码的字符串了。
            var output = temp.innerText || temp.textContent;
            temp = null;
            return output;
        };
        /**
         * Json日期值格式化
         * @param jsonDate 要格式化的Json日期对象
         * @returns output 格式化之后的日期对象
         */
        this.parseJsonDate = function (jsonDate) {
            try {
                //  /Date(1410019200000+0800)/
                if (jsonDate == null || jsonDate == undefined || jsonDate == "") {
                    return null;
                }
                if ((parseInt(jsonDate.replace("/Date(", "").replace(")/", ""), 10)) == NaN) {
                    throw "传入的json日期格式不正确:" + jsonDate;
                }
                var date = new Date(parseInt(jsonDate.replace("/Date(", "").replace(")/", ""), 10));
                return date;
                //var month = date.getMonth() + 1 < 10 ? "0" + (date.getMonth() + 1) : date.getMonth() + 1;
                //var day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
                //var hours = date.getHours();
                //var minutes = date.getMinutes();
                //var seconds = date.getSeconds();
                //var milliseconds = date.getMilliseconds();
                //return date.getFullYear() + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds + "." + milliseconds;
            } catch (ex) {
                return null;
            }
        };
        /**
         * 四舍五入
         * @param value 要进行四舍五入的值
         * @param length 要保留小数位的位数
         * @returns output 四舍五入之后的结果
         */
        this.round = function (value, length) {
            if (value == null || value == undefined || value == "") {
                throw "传入数据不能为空";
            }
            if (!(typeof (value) == number)) {
                throw "传入数据[" + value + "]不是数字";
            }
            return value.toFixed(length);
        };
        /**
         * 向上取整
         * @param value 要进行向上取整的值
         * @param length 要保留小数位的位数
         * @returns output 向上取整之后的结果
         */
        this.ceiling = function (value, length) {
            length = (length == null || length == undefined || length == "") ? 0 : length;
            var floatLen = value.toString().split(".")[1].length;
            if (floatLen <= 0) {
                return value;
            } else {
                if (length == 0) {
                    return parseInt(value) + 1;
                } else if (length >= floatLen) {
                    return value;
                } else {
                    return (value + (1 / (Math.pow(10, length)))).toFixed(length);
                }
            }
        };
        /**
         * 向下取整
         * @param value 要进行向下取整的值
         * @param length 要保留小数位的位数
         * @returns output 向下取整之后的结果
         */
        this.floor = function (value, length) {
            length = (length == null || length == undefined || length == "") ? 0 : length;
            var floatLen = value.toString().split(".")[1].length;
            if (floatLen <= 0) {
                return value;
            } else {
                if (length == 0) {
                    return parseInt(value);
                }
                else if (length >= floatLen) {
                    return value;
                } else {
                    return (value - (1 / (Math.pow(10, length)))).toFixed(length);
                }
            }
        };
        /**
         * 获取精确结果的除法
         * @param arg1 参数1
         * @param arg2 参数2
         * @returns output 计算之后的结果
         */
        this.accDiv = function (arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
                t1 = 0;
            }
            try {
                t2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
                t2 = 0;
            }
            with (Math) {
                r1 = Number(arg1.toString().replace(".", ""));
                r2 = Number(arg2.toString().replace(".", ""));
                return (r1 / r2) * pow(10, t2 - t1);
            }
        };
        /**
         * 获取精确结果的乘法
         * @param arg1 参数1
         * @param arg2 参数2
         * @returns output 计算之后的结果
         */
        this.accMul = function (arg1, arg2) {
            var t1 = 0, t2 = 0, r1, r2;
            try {
                t1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
                t1 = 0;
            }
            try {
                t2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
                t2 = 0;
            }
            with (Math) {
                r1 = Number(arg1.toString().replace(".", ""));
                r2 = Number(arg2.toString().replace(".", ""));
                return (r1 * r2) / pow(10, t2 + t1);
            }
        };
        /**
         * 获取精确结果的加法
         * @param arg1 参数1
         * @param arg2 参数2
         * @returns output 计算之后的结果
         */
        this.accAdd = function (arg1, arg2) {
            var t1 = 0, t2 = 0, m;
            try {
                t1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
                t1 = 0;
            }
            try {
                t2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
                t2 = 0;
            }
            with (Math) {
                m = Math.pow(10, Math.max(t1, t2));
                return (arg1 * m + arg2 * m) / m;
            }
        };
        /**
         * 获取精确结果的减法
         * @param arg1 参数1
         * @param arg2 参数2
         * @returns output 计算之后的结果
         */
        this.accSub = function (arg1, arg2) {
            var t1 = 0, t2 = 0, m, n;
            try {
                t1 = arg1.toString().split(".")[1].length;
            }
            catch (e) {
                t1 = 0;
            }
            try {
                t2 = arg2.toString().split(".")[1].length;
            }
            catch (e) {
                t2 = 0;
            }
            with (Math) {
                //动态控制精度长度
                n = Math.max(t1, t2);
                m = Math.pow(10, n);
                //return (arg1  * m - arg2 * m) / m;
                return ((arg1 * m - arg2 * m) / m).toFixed(n);
            }
        };
        /**
         * 获取字符串的真正长度
         * @param str 要获取长度的字符串
         * @returns output 计算之后的字符串长度
         */
        this.strRealLength = function (str) {
            var byteLen = 0, len = str.length;
            if (str) {
                for (var i = 0; i < len; i++) {
                    if (str.charCodeAt(i) > 255) {
                        byteLen += 2;
                    } else {
                        byteLen++;
                    }
                }
                return byteLen;
            } else {
                return 0;
            }
        };
        /**
         * 字符串首尾去空
         * @returns output 首尾去空后的字符串
         */
        this.trim = function () {
            return this.replace(/(^\s*)|(\s*$)/g, '');
        };
        /**
         * 字符串前置去空
         * @returns output 前置去空后的字符串
         */
        this.trimLeft = function () {
            return this.replace(/(^\s*)/g, '');
        };
        /**
         * 字符串后置去空
         * @returns output 后置去空后的字符串
         */
        this.trimRight = function () {
            return this.replace(/(\s*$)/g, '');
        };
        /**
         * 判断浏览器是否是IE9以下浏览器
         * @returns true or false
         */
        this.isLtIE9 = function () {
            if (_privateObj.letIE9 == null) {
                _privateObj.letIE9 = /msie/.test(navigator.userAgent.toLowerCase());
            }
            return _privateObj.letIE9;
        };
        /**
         * 记录log日志
         * @param msg 日志内容
         */
        this.log = function (msg) {
            if (this.isLtIE9()) {
                //ingore;
            } else {
                console.log(msg);
            }
        };
        /**
         * 记录warn日志
         * @param msg 日志内容
         */
        this.warn = function (msg) {
            if (this.isLtIE9()) {
                //ingore;
            } else {
                console.warn(msg);
            }
        };
        /**
         * 记录error日志
         * @param msg 日志内容
         */
        this.error = function (msg) {
            if (this.isLtIE9()) {
                //ingore;
            } else {
                console.warn(msg)
            }
        };
        /**
         * 验证token是否通过
         */
        this.validateToken = function (res) {
            if (dc.project.isValidateToken) {
                if (res && res.hasOwnProperty("errorCode") && res.errorCode >= 1 && res.errorCode < 200) {
                    return false;
                }
                return true;
            }
            return true;
        };
    }
    /**
     * 提示框对象
     */
    function dcInfo() {
        /**
         * 普通消息框
         * @param msg 消息内容
         * @param option 额外配置对象
         */
        this.alert = function (msg, option) {
            option = option || {};
            if (!option.isOuter) { //在内页执行
                var title = "提示";
                var fn = undefined;
                if (option.title) {
                    title = option.title;
                }
                if (option.fn) {
                    fn = option.fn;
                }
                $.messager.alert(title, msg, 'info', fn);
            } else {
                dc.invokeOut(function () {
                    base.systemAlert(msg, successfn);
                }, { msg: msg, successfn: option.fn });
            }
        };
        /**
         * 错误提示框
         * @param msg 消息内容
         * @param option 额外配置对象
         */
        this.error = function (msg, option) {
            option = option || {};
            if (!option.isOuter) {  //在内页执行
                var title = "错误";
                var fn = undefined;
                if (option.title) {
                    title = option.title;
                }
                if (option.fn) {
                    fn = option.fn;
                }
                $.messager.alert(title, msg, 'error', fn);
            } else {
                dc.invokeOut(function () {
                    base.systemAlert(msg, successfn);
                }, { msg: msg, successfn: option.fn });
            }
        };
        /**
         * 警告框
         * @param msg 消息内容
         * @param option 额外配置对象
         */
        this.warn = function (msg, option) {
            option = option || {};
            if (!option.isOuter) { //在内页执行
                var title = "警告";
                var fn = undefined;
                if (option) {
                    if (option.title) {
                        title = option.title;
                    }
                    if (option.fn) {
                        fn = option.fn;
                    }
                }
                $.messager.alert(title, msg, 'warning', fn);
            } else {
                dc.invokeOut(function () {
                    base.systemAlert(msg, successfn);
                }, { msg: msg, successfn: option.fn });
            }
        };
        /**
         * 询问框
         * @param msg 消息内容
         * @param option 额外配置对象
         */
        this.confirm = function (msg, option) {
            option = option || {};
            if (!option.isOuter) { //在内页执行
                var title = "询问";
                var fn = undefined;
                if (option.title) {
                    title = option.title;
                }
                if (option.fn) {
                    fn = option.fn;
                }
                $.messager.confirm(title, msg, fn);
            } else {
                dc.invokeOut(function () {
                    base.systemConfirm(msg, successfn, cancelfn);
                }, { msg: msg, successfn: option.fn, cancelfn: option.fn });
            }
        };
        /**
         * 自动隐藏信息框
         * @param msg 消息内容
         * @param option 额外配置对象
         */
        this.show = function (msg, option) {
            option = option || {};
            if (!option.isOuter) { //在内页执行
                var _option = {
                    showType: "slide",
                    title: "提示",
                    msg: msg,
                    timeout: 1000
                }
                _option = dc.util.cloneObjectPro(_option, option);
                $.messager.show(_option);
            } else {
                dc.invokeOut(function () {
                    $.jGrowl(msg, { life: 500, beforeClose: successfn });
                }, { msg: msg, successfn: option.fn });
            }
        };
        /**
         * 加载提示框
         * @param msg 消息内容
         * @param option 额外配置对象
         */
        this.loading=function(msg,option){
            msg=msg||"正在处理，请稍等...";
            option = $.extend({img:"../../../dcUIFramework/jquery-easyui/themes/default/images/loading.gif"},option);
            if (!option.isOuter) { //在内页执行
                //获取当前页面的高框
                //创建一个遮罩
                $("body").append("<div class='dcc_loadDiv'><div class='dcc_content'><img src='"+option.img+"'/>"+msg+"</div></div>");
                $(".dcc_loadDiv").css({
                    "width":$(window).width(),
                    "height":$(window).height(),
                    "top":$(window).scrollTop(),
                    "lineHeight":$(window).height()+"px"
                });
                //锁定滚动条
                var top=$(document).scrollTop();
                $(document).on('scroll.unable',function (e){
                    $(document).scrollTop(top);
                })
            }else {
            }
        };
        /**
         * 关闭当前页面内信息提示框
         * @param option 额外配置对象
         */
        this.close=function(option){
            option = option || {};
            if (!option.isOuter) { //在内页执行
                $(".messager-body").window('close');
                $(".dcc_loadDiv").remove();
                //解除禁用滚动条
                $(document).unbind("scroll.unable");
            }else {
            }
        };
    }
    /**
     * 选项卡组对象
     */
    function dcTabs() {
        /**
         * 添加新的tab
         * @param id tabs的id
         * @param tabObj 需要添加的tab对象
         * @param windowObj 需要执行的域
         */
        this.add = function (id, tabObj, windowObj) {
            var currentTopWindow = windowObj || dc.getCurrentDomianTopWindow();
            currentTopWindow["dc_tab_add"] = function () {
                if (tabObj != undefined && typeof (tabObj) == "object") {
                    tabObj = dc.util.cloneObjectPro(tabObj, { closable: false });
                    if (tabObj.id) {
                        if ($("#" + tabObj.id).length == 0) {
                            tabObj.content = "";
                            if ($("#" + id).tabs("tabs").length == 0 || tabObj.selected) {
                                tabObj.content =
                                    "<iframe onload='dc.frameLoad(this)' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='no' style='width:100%;height:100%;border:none;overflow:hidden' data-src='" +
                                    tabObj.href +
                                    "'></iframe>";
                            } else {
                                tabObj
                                    .content =
                                    "<iframe onload='dc.frameLoad(this)' frameborder='no' border='0' marginwidth='0' marginheight='0' scrolling='no' style='width:100%;height:100%;border:none;overflow:hidden' data-src='" +
                                    tabObj.href +
                                    "'></iframe>";
                            }
                            tabObj.href = undefined;
                            $("#" + id).tabs("add", tabObj);
                        } else {
                            throw "id为[" + tabObj.id + "]的tab已经存在";
                        }
                    } else {
                        throw "请设置tab的id";
                    }
                }
            }
            currentTopWindow["dc_tab_add"]();
        }
        /**
         * 删除指定tab
         * @param id tabs的id
         * @param tabid 需要删除的选项的id
         * @param windowObj 需要执行的域
         */
        this.close = function (id, tabid, windowObj) {
            var currentTopWindow = windowObj || dc.getCurrentDomianTopWindow();
            currentTopWindow["dc_tab_delete"] = function () {
                var tab = $("#" + tabid);
                if (tab.length == 0) {
                    throw "id为[" + tabid + "]的tab不存在";
                } else {
                    var index = $("#" + id).tabs("getTabIndex", tab[0]);
                    $("#" + id).tabs("close", index);
                }
            }
            currentTopWindow["dc_tab_delete"]();
        }
        /**
         * 选中指定的tab
         * @param id tabs的id
         * @param tabid 需要选中的选项的id
         */
        this.select = function (id, tabid) {
            var tab = $("#" + tabid);
            if (tab.length == 0) {
                throw "id为[" + tabid + "]的tab不存在";
            } else {
                var index = $("#" + id).tabs("getTabIndex", tab[0]);
                $("#" + id).tabs("select", index);
            }
        }
        /**
         * 刷新指定的tab
         * @param id tabs的id
         * @param tabid 需要刷新的选项的id
         */
        this.refresh = function (id, tabid) {
            var tab = $("#" + tabid);
            if (tab.length == 0) {
                throw "id为[" + tabid + "]的tab不存在";
            } else {
                $("#" + tabid).find("iframe").attr("src", $("#" + tabid).find("iframe").attr("src"));
            }
        }
        /**
         * 更新指定的tab
         * @param id tabs的id
         * @param tabid 需要更新的选项的id
         * @param tabObj 面板更新对象，可设置三个属性，title,href,selected
         */
        this.update = function (id, tabid, tabObj) {
            var tab = $("#" + tabid);
            if (tab.length == 0) {
                dc.util.error("id为[" + tabid + "]的tab不存在");
            } else {
                if (tabObj) {
                    tabObj.selected = tabObj.selected || false;
                    if (!tabObj.title) {
                        throw "请设置更新的标题";
                    }
                    if (!tabObj.href) {
                        throw "请设置更新的地址";
                    }
                    $("#" + id).tabs("update",
                        {
                            tab: tab,
                            options: {
                                title: tabObj.title
                            }
                        });
                    $("#" + tabid).find("iframe").attr("src", tabObj.href);
                    if (tabObj.selected) {
                        dc.tabs.select(id, tabid);
                    }
                } else {
                    throw "请传入面板更新属性";
                }
            }
        }
    }
    /**
     * 弹框对象
     */
    function dcDialog() {
        /**
         * 打开弹窗
         * @param title 窗口标题
         * @param url 页面地址
         * @param dialogObj 弹窗对象
         */
        this.open = function (title, href, dialogObj) {
            dialogObj = dialogObj || {};
            var _option = {
                title: title || "新的弹窗",
                content: "<iframe style='height:100%;width:100%;border:none' src=" + href + "></ifarmae>",
                width: dialogObj.width || 1200,
                height: dialogObj.height || 600,
                closed: false,
                cache: true,
                resizable: true,
                maximizable: true,
                modal: true,
                onClose: function () {
                    var _dcDialog = window.top.dcDialog;
                    if (_dcDialog && _dcDialog.length > 0) {
                        _dcDialog.splice(_dcDialog.length - 1, 1);
                    } else {
                        throw "页面内不存在弹窗或页面内弹窗个数为0";
                    }
                    if (dialogObj && dialogObj.onClose) {
                        dialogObj.onClose();
                    }
                }
            };
            _option = dc.util.cloneObjectPro(_option, dialogObj, ["onClose"]);
            var fatherWindow = dc.getCurrentDomianTopWindow();
            //弹窗对象全局唯一
            fatherWindow.dcDialog = fatherWindow.dcDialog || [];
            fatherWindow.dcDialog.push(fatherWindow.$("<div></div>").dialog(_option));
        };
        /**
         * 关闭弹窗
         */
        this.close = function () {
            var fatherWindow = dc.getCurrentDomianTopWindow();
            var _dcDialog = fatherWindow.dcDialog;
            if (_dcDialog && _dcDialog.length > 0) {
                _dcDialog[_dcDialog.length - 1].dialog('close');
            } else {
                throw "页面内不存在弹窗或页面内弹窗个数为0";
            }
        };
    }
    window.dc = new dcjet();
    window.dcFramework = window.dc;
}(window));
/**
 * 系统初始化ok后执行
 * @type {null|Function}
 */
var dcReady = dcReady || null;
/**
 * 当没有apolloFrontend 时，进行初始化
 */
if (!apolloFrontend) {
    var apolloFrontend = apolloFrontend || {};
    apolloFrontend.init = apolloFrontend.init || null;
    var dcReady = dcReady || function (fn) {
        apolloFrontend.init = fn;
    };
    //调用read
    $(function () {
        if (apolloFrontend.init) {
            apolloFrontend.init();
            dc.autoHeight();
        }
    });
}
//out 的框架定义
dc.out = dc.out || {};
/**
 * 默认外部跳转，可重写
 * @param url
 * @param title
 */
dc.out.redirect = function (url, title) {
    location.href = url;
};
/**
 * 日期格式器
 * @param value
 * @param row
 * @param index
 * @returns {*}
 */
dc.out.datetimeFormatter = function (value, row, index) {
    return dc.util.dateFormatter(value, dc.project.datetimeFormat);
};
dc.out.dateFormatter = function (value, row, index) {
    return dc.util.dateFormatter(value, dc.project.dateFormat);
};
dc.out.getEnumText = function (value, Enum) {
    for (var e in Enum) {
        Enum[e].value = Enum[e].value == undefined ? "" : Enum[e].value;
        value = value == undefined ? "" : value;
        if (Enum[e].value.toString() === value.toString()) {
            return Enum[e].text;
        }
    }
};
//项目默认值
dc.project = dc.project || {};
dc.project.dateFormat = "yyyy-MM-dd";
dc.project.datetimeFormat = "yyyy-MM-dd hh:mm:ss";
//项目枚举项
dc.project.Enum = dc.project.Enum || {};
dc.project.Enum = {
    dev: "devMode",
    develop: "devMode",
    produce: "produceMode",
    defaut: "produceMode",
    run: "produceMode"
};
//运行模式，默认为生产模式
dc.project.RunMode = dc.project.Enum.defaut;
if ($.fn.datagrid) {
    /**
     * easyui datagrid控件扩展
     */
    $.extend($.fn.datagrid.methods, {
        getValue: function (jq) {
            var selects = jq.datagrid("getSelections");
            var result = [];
            if (selects.length > 0) {
                var field = jq.datagrid('options').idField;
                if (field) {
                    $.each(selects, function (index, value) {
                        var val = value[field];
                        if (val == undefined)
                            throw new Error("获取datagrid表格数据出错，没有字段：" + field);
                        result.push(val);
                    });
                }
                else
                    return null;
            }
            return result;
        },
        setValue: function (jq, value) {
            var array = value;
            //判断是否字符串
            if (dcFramework.util.isString(value)) {
                array = value.split(",");
            }
            //清空选择
            jq.datagrid("clearSelections");
            //赋值
            $.each(array, function (index, v) {
                var i = jq.datagrid("getRowIndex", v);
                if (i > -1) {
                    jq.datagrid("selectRow", i);
                }
            });
        }
    });
    $.extend($.fn.textbox.methods,
        {
            focus: function (jq) {
                jq.next("span").find("input:visible,textarea:visible").focus();
            }
        });
    /**
     * easyui-验证扩展
     */
    $.extend($.fn.validatebox.defaults.rules,
        {
            length: {
                validator: function (value, param) {
                    if (param.length == 1) {
                        param.splice(0, 0, 0);
                    }
                    if (param[0] <= value.length && value.length <= param[1]) {
                        return true;
                    } else {
                        return false;
                    }
                },
                message: '输入内容长度必须介于{0}和{1}之间'
            },
            int: {
                validator: function (value, param) {
                    var regex = new RegExp(/^[\-\+]?\d+$/);
                    var res = regex.test(value);
                    if (res && param) {
                        if (value.length > param[0]) {
                            $.fn.validatebox.defaults.rules["int"].message = "整数最大" + param[0] + "位";
                            return false;
                        } else {
                            return true;
                        }
                    } else {
                        $.fn.validatebox.defaults.rules["int"].message = "不是有效的整数";
                        return res;
                    }
                },
                message: '不是有效的整数'
            },
            number: {
                validator: function (value, param) {
                    var regex = new RegExp(/^[\-\+]?(([0-9]+)([\.,]([0-9]+))?|([\.,]([0-9]+))?)$/);
                    var res = regex.test(value);
                    if (res && param) {
                        var intlength = value.length;
                        var decimalLength = 0;
                        if (value.indexOf('.') >= 0) {
                            intlength = value.indexOf('.');
                            decimalLength = value.length - 1 - intlength;
                        }
                        if (intlength <= param[0] && decimalLength <= param[1]) {
                            return true;
                        } else {
                            $.fn.validatebox.defaults.rules["number"]
                                .message = "整数位最大" + param[0] + "位，小数位最大" + param[1] + "位";
                            return false;
                        }
                    } else {
                        $.fn.validatebox.defaults.rules["number"].message = "不是有效的数字";
                        return res;
                    }
                },
                message: "不是有效的数字"
            },
            date: {
                validator: function (value, param) {
                    if (value.indexOf('-') >= 0) {
                        var regex = new
                            RegExp(/^[0-9]{4}-(((0[13578]|(10|12))-(0[1-9]|[1-2][0-9]|3[0-1]))|(02-(0[1-9]|[1-2][0-9]))|((0[469]|11)-(0[1-9]|[1-2][0-9]|30)))$/);
                        return regex.test(value);
                    } else if (value.indexOf('/') >= 0) {
                        var regex2 = new
                            RegExp(/^[0-9]{4}\/(((0[13578]|(10|12))\/(0[1-9]|[1-2][0-9]|3[0-1]))|(02\/(0[1-9]|[1-2][0-9]))|((0[469]|11)\/(0[1-9]|[1-2][0-9]|30)))$/);
                        return regex2.test(value);
                    } else {
                        var regex3 = new RegExp(/^\d{8}$/);
                        return regex3.test(value);
                    }
                },
                message: "不是有效的日期"
            },
            datetime: {
                validator: function (value, param) {
                    var regex = /^(\d{1,4})(-|\/)(\d{1,2})\2(\d{1,2}) (\d{1,2}):(\d{1,2}):(\d{1,2})$/g;
                    return regex.test(value);
                },
                message: "不是有效的时间"
            },
            checklistbox: {
                validator: function (value, param) {
                    return false;
                },
                message: "xxx"
            }
        });
    /**
     * easyui扩展-checkbox控件
     */
    (function ($) {
        $.fn.checkbox = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.checkbox.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return this.textbox(options, param);
                }
            } else {
                options = options || { value: false };//默认不选中
                return this.each(function () {
                    var state = $.data(this, 'checkbox');
                    if (state) {
                        $.extend(state.options, options);
                    } else {
                        $.data(this, 'checkbox', {
                            options: $.extend({}, $.fn.checkbox.defaults, options)//用户配置覆盖默认配置
                        });
                    }
                    createbox(this);
                });
            }
        };
        /**
         * 默认配置
         */
        $.fn.checkbox.defaults = $.extend({}, $.fn.textbox.defaults, $.fn.validatebox.defaults, {
            value: false
        });
        $.fn.checkbox.methods = {
            options: function (jq) {
                var copts = jq.combo('options');
                return $.extend($.data(jq[0], 'Checkbox').options, {
                    width: copts.width,
                    height: copts.height,
                    originalValue: copts.originalValue,
                    disabled: copts.disabled,
                    readonly: copts.readonly
                });
            },
            setValue: function (jq, value) {
                return jq.each(function () {
                    setValue(this, value);
                });
            },
            getValue: function (jq) {
                return getValue(jq);
            },
            reset: function (jq) {
                return jq.each(function () {
                    var opts = $(this).Checkbox('options');
                    $(this).Checkbox('setValue', opts.originalValue);
                });
            },
            clear: function (jq) {
                clear(jq);
            },
            disable: function (jq) {
                var arrInput = $(jq).next(".easyui-checkbox").find(":checkbox");
                $(arrInput).each(function (n, d) {
                    $(d).attr("disabled", true);
                });
            },
            enable: function (jq) {
                var arrInput = $(jq).next(".easyui-checkbox").find(":checkbox");
                $(arrInput).each(function (n, d) {
                    $(d).attr("disabled", false);
                });
            },
            readonly: function (jq, mode) {
                var arrInput = $(jq).next(".easyui-checkbox").find(":checkbox");
                $(arrInput).each(function (n, d) {
                    $(d).attr("readonly", mode);
                });
            },
            disableValidation: function () {
            },
            enableValidation: function () {
            },
            resetValidation: function () {
            }
        };
        /**
         * 控件初始化函数
         * @param target 控件对象
         */
        function createbox(target) {
            var opts = $.data(target, 'checkbox').options;//获取配置对象
            var chk = $(target);
            var id = chk.attr("id") || "chk";
            var checked = "";
            chk.hide();
            if (opts.value === true) {
                checked = "checked='checked'";
            } else {
                checked = "";
            }
            if (opts.text) {
                var html = "<sapn class='textbox easyui-checkbox'><input id='chk_" + id + "' type='checkbox' " + checked + "/><label for='chk_" + id + "'>" + opts.text + "<label></sapn>";
                $(target).nextAll().remove();
                $(target).after(html);
            }
        }
        function getValue(target, remainText) {
            var chk = $(target).next(".easyui-checkbox").find(":checkbox");
            return chk.prop("checked");
        }
        function setValue(target, value, remainText) {
            var chk = $(target).next(".easyui-checkbox").find(":checkbox");
            if (value === true) {
                chk.prop("checked", true);
            } else {
                chk.prop("checked", false);
            }
        }
        function clear(target) {
            var chk = $(target).next(".easyui-checkbox").find(":checkbox");
            $(target).val();
            $(chk).each(function (n, d) {
                d.checked = false;
            });
        }
    })(jQuery);
    /**
     * easyui扩展-checklistbox控件
     */
    (function ($) {
        $.fn.checklistbox = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.checklistbox.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return this.combobox(options, param);
                }
            }
            options = options || {};
            return this.each(function () {
                var state = $.data(this, 'checklistbox');
                if (state) {
                    $.extend(state.options, options);
                } else {
                    $.data(this, 'checklistbox', {
                        options: $.extend({}, $.fn.checklistbox.defaults, options)
                    });
                }
                checklistbox(this);
            });
        };
        /**
         * 默认配置
         */
        $.fn.checklistbox.defaults = $.extend({}, $.fn.combobox.defaults, {
            textField: "text",
            valueField: "value",
            method: "post",
            dataType: "json"
        });
        $.fn.checklistbox.methods = {
            getValues: function (jq) {
                return getValues(jq);
            },
            getValue: function (jq) {
                var res = getValues(jq);
                if (res.length == 0) {
                    return undefined;
                } else {
                    return res.join(',');
                }
            },
            setValues: function (jq, value) {
                return jq.each(function () {
                    clear(jq);
                    setValues(this, value);
                });
            },
            setValue: function (jq, value) {
                return jq.each(function () {
                    clear(jq);
                    setValue(this, value);
                });
            },
            clear: function (jq) {
                clear(jq);
            },
            disable: function (jq) {
                var arrInput = $(jq).next(".easyui-checklistbox").find(":checkbox");
                $(arrInput).each(function (n, d) {
                    $(d).attr("disabled", true);
                });
            },
            enable: function (jq) {
                var arrInput = $(jq).next(".easyui-checklistbox").find(":checkbox");
                $(arrInput).each(function (n, d) {
                    $(d).attr("disabled", false);
                });
            },
            readonly: function (jq, mode) {
                var arrInput = $(jq).next(".easyui-checklistbox").find(":checkbox");
                $(arrInput).each(function (n, d) {
                    $(d).attr("readonly", mode);
                });
            },
            disableValidation: function (jq) {
                jq.validatebox("disableValidation");
            },
            enableValidation: function (jq) {
                jq.validatebox("enableValidation");
            },
            resetValidation: function (jq) {
                jq.validatebox("resetValidation");
            }
        };
        /**
         * 控件初始化函数
         * @param target 控件对象
         */
        function checklistbox(target) {
            var state = $.data(target, 'checklistbox');
            var opts = state.options;
            if (opts.url) {
                $.ajax({
                    type: opts.method,
                    url: opts.url,
                    data: opts.queryParams,
                    dataType: opts.dataType,
                    beforeSend: function () {
                        if (opts.onBeforeLoad) {
                            opts.onBeforeLoad();
                        }
                    },
                    success: function (data) {
                        if (!dc.util.validateToken(data)) {
                            if (dc.out.notToken) {
                                dc.out.notToken();
                            }
                        } else {
                            if (opts.onLoadSuccess) {
                                opts.onLoadSuccess(data);
                            }
                            opts.data = data;
                            init(target, opts);
                        }
                    },
                    error: function () {
                        if (opts.onLoadError) {
                            opts.onLoadError();
                        }
                    }
                });
            } else {
                init(target, opts);
            }
        }
        function init(target, opts) {
            var html = "";
            var checked = "";
            var chkl = $(target);
            chkl.addClass("easy-checklistbox").attr("readonly", true);
            var id = chkl.prop("id") || "chkl";
            if (opts.data) {
                for (var i = 0; i < opts.data.length; i++) {
                    var nid = id + "_" + i;
                    if (opts.value) {//如果设置了默认值，则选中不选中以默认值为准
                        if ((opts.value + ",").indexOf(opts.data[i][opts.valueField] + ",") >= 0) {
                            checked = "checked='checked'";
                        } else {
                            checked = "";
                        }
                    } else {//没有默认值，选中不选中以数据源为准
                        if (opts.data[i].selected === true) {
                            checked = "checked='checked'";
                        } else {
                            checked = "";
                        }
                    }
                    html += "<input id='" + nid + "' type='checkbox' value='" + opts.data[i][opts.valueField] + "' " + checked + "/>" +
                        "<label class='easy-checklistbox-label' for='" + nid + "'>" + opts.data[i][opts.textField] + "</label>";
                }
                chkl.nextAll().remove();
                chkl.after('<span class="textbox easyui-checklistbox">' + html + "</span>");
                var span = chkl.nextAll(".easyui-checklistbox");
                var spanwidth = span.outerWidth(true) + 10;
                var spanheight = span.outerHeight(true);
                chkl.width(spanwidth);
                var parentWidth = chkl.parent().width();
                if (parentWidth >= spanwidth * 2 - 10) {
                    span.css("left", -spanwidth);
                } else {
                    span.css("top", -spanheight / 2 - 4);
                    chkl.css("top", spanheight / 2);
                }
                chkl.validatebox({
                    required: opts.required || false,
                    validateOnCreate: opts.validateOnCreate,
                    validateOnBlur: opts.validateOnBlur,
                    validType: opts.validType
                });
                chkl.val(dc.getValues("#" + id));
                chkl.nextAll(".easyui-checklistbox").find("input[type='checkbox']").change(function () {
                    chkl.val(dc.getValues("#" + id));
                    chkl.validatebox("validate").focus();
                });
            }
        }
        function setValues(target, value, remainText) {
            var id = $(target).prop("id") || "chkl";
            var arrInput = $(target).next(".easyui-checklistbox");
            if (dc.util.isString(value)) {
                value = value.split(',');
            }
            if (value) {
                $(value).each(function (n, d) {
                    arrInput.find("input[value='" + d + "']").prop("checked", true);
                });
            }
            $(target).val(dc.getValues("#" + id));
            $(target).validatebox("validate").focus();
        }
        function setValue(target, value, remainText) {
            var id = $(target).prop("id") || "chkl";
            var arrInput = $(target).next(".easyui-checklistbox").find(":checkbox");
            $(arrInput).each(function (n, d) {
                if (d.value == value) {
                    d.checked = true;
                }
            });
            $(target).val(dc.getValues("#" + id));
            $(target).validatebox("validate").focus();
        }
        function getValues(target, remainText) {
            var value = new Array();
            var arrInput = $(target).next(".easyui-checklistbox").find(":checked");
            $(arrInput).each(function () {
                value.push($(this).val());
            });
            return value;
        }
        function clear(target) {
            var arrInput = $(target).next(".easyui-checklistbox").find(":checked");
            $(target).val();
            $(arrInput).each(function (n, d) {
                d.checked = false;
            });
        }
    })(jQuery);
    /**
     * easyui扩展-radiobox控件
     */
    (function ($) {
        $.fn.radiobox = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.radiobox.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return this.combobox(options, param);
                }
            }
            options = options || {};
            return this.each(function () {
                var state = $.data(this, 'radiobox');
                if (state) {
                    $.extend(state.options, options);
                } else {
                    $.data(this, 'radiobox', {
                        options: $.extend({}, $.fn.radiobox.defaults, options)
                    });
                }
                radiobox(this);
            });
        };
        /**
         * 默认配置
         */
        $.fn.radiobox.defaults = $.extend({}, $.fn.combobox.defaults, {
            textField: "text",
            valueField: "value"
        });
        $.fn.radiobox.methods = {
            getValue: function (jq) {
                return getValue(jq);
            },
            setValue: function (jq, value) {
                return jq.each(function () {
                    clear(jq);
                    setValue(this, value);
                });
            },
            clear: function (jq) {
                clear(jq);
            },
            disable: function (jq) {
                var arrInput = $(jq).next(".easyui-radiobox").find(":radio");
                $(arrInput).each(function (n, d) {
                    $(d).attr("disabled", true);
                });
            },
            enable: function (jq) {
                var arrInput = $(jq).next(".easyui-radiobox").find(":radio");
                $(arrInput).each(function (n, d) {
                    $(d).attr("disabled", false);
                });
            },
            readonly: function (jq, mode) {
                var arrInput = $(jq).next(".easyui-radiobox").find(":radio");
                $(arrInput).each(function (n, d) {
                    $(d).attr("readonly", mode);
                });
            },
            disableValidation: function (jq) {
                jq.validatebox("disableValidation");
            },
            enableValidation: function (jq) {
                jq.validatebox("enableValidation");
            },
            resetValidation: function (jq) {
                jq.validatebox("resetValidation");
            }
        };
        /**
         * 控件初始化函数
         * @param target 控件对象
         */
        function radiobox(target) {
            var state = $.data(target, 'radiobox');
            var opts = state.options;
            if (opts.url) {
                $.ajax({
                    type: opts.method,
                    url: opts.url,
                    async: false,
                    data: opts.queryParams,
                    dataType: opts.dataType,
                    beforeSend: function () {
                        if (opts.onBeforeLoad) {
                            opts.onBeforeLoad();
                        }
                    },
                    success: function (data) {
                        if (!dc.util.validateToken(data)) {
                            if (dc.out.notToken) {
                                dc.out.notToken();
                            }
                        } else {
                            if (opts.onLoadSuccess) {
                                opts.onLoadSuccess();
                            }
                            opts.data = data;
                            init(target, opts);
                        }
                    },
                    error: function () {
                        if (opts.onLoadError) {
                            opts.onLoadError();
                        }
                    }
                });
            } else {
                init(target, opts);
            }
        }
        function init(target, opts) {
            var radio = $(target);
            radio.addClass("easy-radiobox").attr("readonly", true);
            radio.removeAttr("style");
            var id = radio.prop("id") || "rad";
            var name = id;
            var html = "";
            var checked = "";
            if (opts.data) {
                for (var i = 0; i < opts.data.length; i++) {
                    var nid = id + "_" + i;
                    if (opts.value) {//如果设置了默认值，则选中不选中以默认值为准
                        if ((opts.value + ",").indexOf(opts.data[i][opts.valueField] + ",") >= 0) {
                            checked = "checked='checked'";
                        } else {
                            checked = "";
                        }
                    }
                    else {//没有默认值，选中不选中以数据源为准
                        if (opts.data[i].selected === true) {
                            checked = "checked='checked'";
                        } else {
                            checked = "";
                        }
                    }
                    html += "<input id='" + nid + "' name=" + name + "  type='radio' value='" + opts.data[i][opts.valueField] + "' " + checked + "/>" +
                        "<label class='easy-radiobox-label' for='" + nid + "'>" + opts.data[i][opts.textField] + "</label>";
                }
                radio.css("width", 1).nextAll().remove();
                radio.after('<span class="textbox easyui-radiobox">' + html + "</span>");
                radio.nextAll(".easyui-radiobox").css("left", -3);
                // var span = radio.nextAll(".easyui-radiobox");
                // var spanwidth = span.outerWidth(true) + 10;
                // var spanheight = span.outerHeight(true);
                // var parentWidth = radio.parent().width();
                // if (parentWidth >= spanwidth * 2 - 10) {
                //     span.css("left", -spanwidth);
                // } else {
                //     span.css("top", -spanheight / 2 - 4);
                //     radio.css("top", spanheight / 2);
                // }
                radio.validatebox({
                    required: opts.required || false,
                    validateOnCreate: opts.validateOnCreate,
                    validateOnBlur: opts.validateOnBlur,
                    validType: opts.validType
                });
                radio.val(dc.getValues("#" + id));
                radio.nextAll(".easyui-radiobox").find("input[type='radio']").change(function () {
                    radio.val(dc.getValues("#" + id));
                    radio.validatebox("validate").focus();
                });
            }
        }
        function getValue(target, value, remainText) {
            var arrInput = $(target).next(".easyui-radiobox").find(":checked");
            if (arrInput.length > 0) {
                return arrInput[0].value;
            } else {
                return undefined;
            }
        }
        function setValue(target, value, remainText) {
            var id = $(target).prop("id") || "rad";
            var arrInput = $(target).next(".easyui-radiobox").find(":radio");
            $(arrInput).each(function (n, d) {
                if (d.value == value) {
                    d.checked = true;
                }
            });
            $(target).val(dc.getValue("#" + id));
            $(target).validatebox("validate").focus();
        }
        function clear(target) {
            var arrInput = $(target).next(".easyui-radiobox").find(":checked");
            $(target).val();
            $(arrInput).each(function (n, d) {
                d.checked = false;
            });
        }
    })(jQuery);
    /**
     * easyui扩展-dctextbox控件,继承自easyui-textbox
     */
    (function ($) {
        $.fn.dctextbox = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.dctextbox.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return undefined;
                }
            } else {
                options = $.extend({}, $.fn.dctextbox.defaults, options)
                return initdctextbox(this, options);
            }
        };
        /**
         * 默认配置
         */
        $.fn.dctextbox.defaults = $.extend({}, $.fn.textbox.defaults);
        /**
         * 控件的事件
         */
        $.fn.dctextbox.methods = {
            getValue: function (jq) {
                return jq.val();
            },
            setValue: function (jq, value) {
                jq.val(value);
            },
            clear: function (jq) {
                jq.val("");
            },
            disable: function (jq) {
                jq.attr("disabled", true);
            },
            enable: function (jq) {
                jq.attr("disabled", false);
            },
            readonly: function (jq, mode) {
                if (mode) {
                    jq.attr("readonly", true);
                } else {
                    jq.attr("readonly", false);
                }
            },
            disableValidation: function (jq) {
                jq.validatebox("disableValidation");
            },
            enableValidation: function (jq) {
                jq.validatebox("enableValidation");
            },
            resetValidation: function (jq) {
                jq.validatebox("resetValidation");
            }
        };
        /**
         * 控件的初始化函数
         * */
        function initdctextbox(target, options) {
            if (options.disabled) {
                target.attr("disabled", true);
            }
            target.addClass("textbox easyui-dctextbox");
            target.css({
                width: options.width,
                height: options.height
            });
            target.on({
                change: function () {
                    if (options.onChange) {
                        options.onChange();
                    }
                }
            });
            if (!options.novalidate) {
                target.validatebox({
                    required: options.required || false,
                    validateOnCreate: false,
                    validateOnBlur: false,
                    validType: options.validType
                });
            }
        }
    })(jQuery);
    /**
     * easyui扩展-dcdcintbox控件,继承自easyui-textbox
     * */
    (function ($) {
        $.fn.dcintbox = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.dctextbox.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return undefined;
                }
            } else {
                options = $.extend({}, $.fn.dctextbox.defaults, options)
                return initdcintbox(this, options);
            }
        };
        /**
         * 默认配置
         */
        $.fn.dcintbox.defaults = $.extend({}, $.fn.textbox.defaults);
        /**
         * 控件的事件
         */
        $.fn.dcintbox.methods = {
            getValue: function (jq) {
                return jq.val();
            },
            setValue: function (jq, value) {
                jq.val(value);
            },
            clear: function (jq) {
                jq.val("");
            },
            disable: function (jq) {
                jq.attr("disabled", true);
            },
            enable: function (jq) {
                jq.attr("disabled", false);
            },
            readonly: function (jq, mode) {
                if (mode) {
                    jq.attr("readonly", true);
                } else {
                    jq.attr("readonly", false);
                }
            },
            disableValidation: function (jq) {
                jq.validatebox("disableValidation");
            },
            enableValidation: function (jq) {
                jq.validatebox("enableValidation");
            },
            resetValidation: function (jq) {
                jq.validatebox("resetValidation");
            }
        };
        /**
         * 控件的初始化函数
         * */
        function initdcintbox(target, options) {
            if (options.disabled) {
                target.attr("disabled", true);
            }
            target.addClass("textbox easyui-dcnumberbox");
            target.css({
                width: options.width,
                height: options.height
            });
            target.on({
                "keydown": function (event) {
                    if (event.keyCode >= 48 && event.keyCode <= 57) {
                        return true;
                    }
                    if (event.keyCode >= 96 && event.keyCode <= 105) {
                        return true;
                    }
                    if (event.keyCode == 189 || event.keyCode == 13 || event.keyCode == 8 || event.keyCode == 109) {
                        return true;
                    }
                    return false;
                },
                "keyup": function () {
                    var re = /^-?\d*$/;
                    if (!re.test(this.value)) {
                        this.value = this.value.match(/-?\d*/);
                    }
                },
                "onchange": function () {
                    if (e.keyCode == 13) {
                        var re = /^-?\d*$/;
                        if (!re.test(this.value)) {
                            this.value = this.value.match(/-?\d*/);
                        }
                    }
                },
                "blur": function () {
                    var re = /^-?\d*$/;
                    if (!re.test(this.value)) {
                        this.value = this.value.match(/-?\d*/);
                    }
                }
            });
            if (!options.novalidate) {
                target.validatebox({
                    required: options.required || false,
                    validateOnCreate: false,
                    validateOnBlur: false,
                    validType: options.validType
                });
            }
        }
    })(jQuery);
    /**
     * easyui扩展-dcdecimalbox控件,继承自easyui-textbox
     * */
    (function ($) {
        $.fn.dcdecimalbox = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.dctextbox.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return undefined;
                }
            } else {
                options = $.extend({}, $.fn.dctextbox.defaults, options)
                return initdcdecimalbox(this, options);
            }
        };
        /**
         * 默认配置
         */
        $.fn.dcdecimalbox.defaults = $.extend({}, $.fn.textbox.defaults);
        /**
         * 控件的事件
         */
        $.fn.dcdecimalbox.methods = {
            getValue: function (jq) {
                return jq.val();
            },
            setValue: function (jq, value) {
                jq.val(value);
            },
            clear: function (jq) {
                jq.val("");
            },
            disable: function (jq) {
                jq.attr("disabled", true);
            },
            enable: function (jq) {
                jq.attr("disabled", false);
            },
            readonly: function (jq, mode) {
                if (mode) {
                    jq.attr("readonly", true);
                } else {
                    jq.attr("readonly", false);
                }
            },
            disableValidation: function (jq) {
                jq.validatebox("disableValidation");
            },
            enableValidation: function (jq) {
                jq.validatebox("enableValidation");
            },
            resetValidation: function (jq) {
                jq.validatebox("resetValidation");
            }
        };
        /**
         * 控件的初始化函数
         * */
        function initdcdecimalbox(target, options) {
            if (options.disabled) {
                target.attr("disabled", true);
            }
            target.addClass("textbox easyui-dcnumberbox");
            target.css({
                width: options.width,
                height: options.height
            });
            target.on({
                "keyup": function () {
                    var re = /[^-?\d.]/g;
                    if (re.test(this.value)) {
                        this.value = "";
                    }
                },
                "keypress": function (e) {
                    if (e.keyCode >= 48 && event.keyCode <= 57 || event.keyCode == 46 || event.keyCode == 45) {
                        if (this.value.length > 0 && event.keyCode == 45 && this.value.split('')[0] == '-') {
                            return false;
                        }
                        if (this.value.length > 0 && event.keyCode == 46 && this.value.split('.').length > 1) {
                            return false;
                        }
                        return true;
                    } else {
                        return false;
                    }
                },
                "blur": function () {
                    var re = /[^-?\d.]/g;
                    if (re.test(this.value)) {
                        this.value = "";
                    }
                }
            });
            if (!options.novalidate) {
                target.validatebox({
                    required: options.required || false,
                    validateOnCreate: false,
                    validateOnBlur: false,
                    validType: options.validType
                });
            }
        }
    })(jQuery);
    /**
     * easyui扩展-dcautocomplete控件,继承自easyui-textbox
     *
     */
    (function ($) {
        $.fn.dcautocomplete = function (options, param) {
            if (typeof options == 'string') {
                var method = $.fn.dcautocomplete.methods[options];
                if (method) {
                    return method(this, param);
                } else {
                    return undefined;
                }
            } else {
                options = $.extend({}, $.fn.dctextbox.defaults, options)
                return initdcautocomplete(this, options);
            }
        };
        /**
         * 默认配置
         */
        $.fn.dcautocomplete.defaults = $.extend({}, $.fn.textbox.defaults);
        /**
         * 控件的事件
         */
        $.fn.dcautocomplete.methods = {
            getValue: function (jq) {
                return jq.val();
            },
            setValue: function (jq, value) {
                $("#" + jq.attr("dcjet-validate-text")).selectlist("setValue", value);
            },
            clear: function (jq) {
                $("#" + jq.attr("dcjet-validate-text")).selectlist("setValue", "");
            },
            disable: function (jq) {
                $("#" + jq.attr("dcjet-validate-text")).attr("disabled", true);
                jq.next().find(".combo-arrow").addClass("combo-arrow-disabled");
            },
            enable: function (jq) {
                $("#" + jq.attr("dcjet-validate-text")).attr("disabled", false);
                jq.next().find(".combo-arrow").removeClass("combo-arrow-disabled");
            },
            readonly: function (jq, mode) {
                if (mode) {
                    $("#" + jq.attr("dcjet-validate-text")).attr("readonly", true);
                    jq.next().find(".combo-arrow").addClass("combo-arrow-readonly");
                } else {
                    $("#" + jq.attr("dcjet-validate-text")).attr("readonly", false);
                    jq.next().find(".combo-arrow").removeClass("combo-arrow-readonly");
                }
            },
            disableValidation: function (jq) {
                $("#" + jq.attr("dcjet-validate-text")).validatebox("disableValidation");
            },
            enableValidation: function (jq) {
                $("#" + jq.attr("dcjet-validate-text")).validatebox("enableValidation");
            },
            resetValidation: function (jq) {
                $("#" + jq.attr("dcjet-validate-text")).validatebox("resetValidation");
            }
        };
        /**
         * 控件的初始化函数，获取数据源
         * */
        function initdcautocomplete(target, options) {
            var valID = target.attr('id');
            var txtID = valID + "_text";
            var valName = target.attr('name') || "";
            var txtName = (valName == "" ? "" : (valName + "_text"));
            var iId = txtID + "_selectlist";
            var arrow = '<span class="textbox-addon textbox-addon-right" style="right: 0px; top: 0px;"><a href="javascript:;" id="' + iId + '" class="textbox-icon combo-arrow" icon-index="0" tabindex="-1" style="width: 18px; height: 22px;"></a></span>';
            var showTargetHtml = '<input type="text" id="' + txtID + '" name="' + txtName + '" class="textbox-text" dcjet-validate-val="' + valID + '"/>';
            target.nextAll().remove();
            target.after('<span class="textbox easyui-fluid combo easyui-dcautocomplete">' + showTargetHtml + arrow + '</span>');
            target.attr("dcjet-validate-text", txtID).hide();
            var showTarget = $("#" + txtID);
            showTarget.parent().css({
                "width": options.width,
                "height": options.height
            });
            if (options.url) {//远程数据源
                dc.post(options.url, options.queryParams, function (res) {
                    options.data = res;
                    init(target, showTarget, options, iId);
                });
            } else//本地数据源
            {
                init(target, showTarget, options, iId);
            }
        }
        /**
         * 控件的初始化底层实现函数
         * @param target
         * @param showTarget
         * @param options
         * @param iId
         */
        function init(target, showTarget, options, iId) {
            if (!options.data) return;
            var option = {};
            var maxHeight = options.panelMaxHeight || 150;
            option.lookup = options.data;
            option.minChars = 1;
            option.textField = options.textField;//设置显示值得属性名
            option.autoSelectFirst = true;
            option.maxHeight = maxHeight;
            var changeHideInputValue = function (suggestion) {
                target.val(suggestion[options.valueField]);
                showTarget.val(suggestion[options.textField]);
                if (!options.novalidate)
                    showTarget.validatebox("validate");
            };
            option.onSelect = function (suggestion) {
                changeHideInputValue(suggestion);
                if (options.onSelect) {
                    options.onSelect();
                }
            };
            option.onBlur = function (sender, data) {
                if (!data || data.length == 0) {
                    var valObj = {};
                    valObj[options.valueField] = "";
                    valObj[options.textField] = "";
                    changeHideInputValue(valObj);
                    return;
                }
            };
            option.onSetValue = function (val, data) {
                if (data && data.length > 0 && val != "") {
                    changeHideInputValue(data[0]);
                } else {
                    var valObj = {};
                    valObj[options.valueField] = "";
                    valObj[options.textField] = "";
                    changeHideInputValue(valObj);
                }
            };
            showTarget.selectlist(option);
            if (!options.novalidate) {
                showTarget.validatebox({
                    required: options.required || false,
                    validateOnCreate: false,
                    validateOnBlur: false,
                    validType: options.validType,
                    onValidate: function (res) {
                        if (res) {
                            showTarget.parent().removeClass("textbox-invalid");
                        } else {
                            showTarget.parent().addClass("textbox-invalid");
                        }
                    }
                });
            }
            $("#" + iId).click(function () {
                if (!$(this).hasClass("combo-arrow-disabled") && !$(this).hasClass("combo-arrow-readonly")) {
                    var obj = showTarget.selectlist();
                    if (!obj.containerEventInitialized) {
                        $(obj.suggestionsContainer).focusin(function () {
                            obj.shouldHide = false;
                        });
                        $(obj.suggestionsContainer).focusout(function () {
                            obj.shouldHide = true;
                            obj.onBlur()
                        });
                        obj.containerEventInitialized = true;
                    }
                    if (obj.visible) {
                        obj.shouldHide = true;
                        obj.hide();
                    } else {
                        obj.shouldHide = false;
                        showTarget.focus();
                        setTimeout(function () {
                            obj.shouldHide = true;
                        }, 250);
                        option.lookupFilter = function () {
                            return true;
                        };
                        obj.setOptions(option);
                        var query = obj.getQuery("");
                        obj.getSuggestions(query);
                        option.lookupFilter = function (suggestion, originalQuery, queryLowerCase) {
                            return originalQuery[options.textField].toLowerCase().indexOf(queryLowerCase) !== -1;
                        };
                        obj.setOptions(option);
                    }
                }
            });
        }
    })(jQuery);
}