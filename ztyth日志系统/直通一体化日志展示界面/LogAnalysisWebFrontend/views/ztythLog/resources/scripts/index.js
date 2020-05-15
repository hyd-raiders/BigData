var moduleUrl = dc.project.backendUrl + "/logHome";
var pageNum=dc.util.getRequest().pageNum;        
if (pageNum == undefined) {
    pageNum = 1;
} else {
    pageNum = pageNum;            
}
dcReady(function () {
    dc.config={
        pagecode: "db-ztyth-log-index",
        control:{
            "s_corpCode": {
                width:'95%'
            },
            "s_username": {
                width: '95%'
            },
            "s_createDateBegin": {
                width: '45%'
            },
            "s_createDateEnd": {
                width: '45%'
            },
            "s_pageName": {  width:'95%',data: Enum.PageNameList,valueField: "value", textField: "text"},
            "s_functionName": { width:'95%' },
            "s_message": {
                width: '95%'
            },
            "s_quicksearch":{onClick:dc_quicksearch},
            "s_search":{onClick:dc_search},
            "t_export": { "iconCls": "icon-dc-export", onClick: dc_export },
            "g_table":{
                idField: 'oid',
                url: moduleUrl + "/getList",
                pageSize:20,
                columns: [[
                    {field: 'corpcode', title: '企业编码',width:100},
                    {field: 'username', title: '用户名',width:100},
                    {field: 'pagename', title: '模块名称',width:150},
                    {field: 'functionname', title: '操作类型',width:150},
                    {field: 'ip', title: 'IP地址',width:100},
                    {field: 'createdate', title: '操作时间',formatter: dc.out.datetimeJsonFormatter,width:150},
                    {
                        title: "日志详情", field: 'message',width:600, formatter: function (value, row, index) {
                            value = value.replace(/'/g, "&apos;");
                            value = value.replace(/"/g, "&quot;");
                            value = value.replace(/>/g, "&gt;");
                            value = value.replace(/</g, "&lt;");
                            value = value.replace(/\s+/g, "");

                            return "<div class='txt-overflow imps' data-id='"+row.oid+"' style='width:100%;cursor:pointer;' title='" + value+ "'>" + value + "</div>";
                        }
                    },
					{field:"oid",hidden:true}
                ]],
                onLoadSuccess: function () {
                    $(".imps").click(function () {
                        _showMessage($(this).data("id"));
                    });
                }
            }
        }

    };

   dc.init();
});

//查询
function dc_quicksearch() {
    var searchModel = dc.getModel("form");
    searchModel.quick = "1";
    $('#g_table').datagrid('load', searchModel);
};

function dc_search() {
    var searchModel = dc.getModel("form");
    searchModel.quick = "0";
    $('#g_table').datagrid('load', searchModel);
};

function _showMessage(oid)
{
    var detailUrl = dc.util.getUrl() + ("/detail.html?oid=" + encodeURIComponent(oid));
    //detailUrl = url
    dc.dialog.open("查看详情", detailUrl, {
        onClose: function () {
        },
        height: 500
    });
}
function dc_export() {
    var href = moduleUrl + "/export?";
    href = href + jQuery.param(dc.getModel("form"));
    $("#linkExport").attr("href", href);
    $("#linkExport").get(0).click();
}