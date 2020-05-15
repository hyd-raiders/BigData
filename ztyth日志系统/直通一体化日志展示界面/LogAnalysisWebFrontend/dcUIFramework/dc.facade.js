/**
 * Created by Administrator on 2017/5/26.
 * 本ｊｓ为框架的入口，框架使用的统一配置放在这里
 */
var apolloFrontend = apolloFrontend || {};
apolloFrontend.classcodes =[];
apolloFrontend.dynamicLoading={
    /*加载一批文件，_files:文件路径数组,可包括js,css,less文件,succes:加载成功回调函数*/
    LoadFileList:function(_files,succes){
        var FileArray=[];
        if(typeof _files==="object"){
            FileArray=_files;
        }else{
            /*如果文件列表是字符串，则用,切分成数组*/
            if(typeof _files==="string"){
                FileArray=_files.split(",");
            }
        }
        if(FileArray!=null && FileArray.length>0){
            var LoadedCount=0;
            for(var i=0;i< FileArray.length;i++){
                loadFile(FileArray[i],function(){
                    LoadedCount++;
                    if(LoadedCount==FileArray.length){
                        if(apolloFrontend.init)
                            apolloFrontend.init();
                        if(succes)
                        succes();
                    }
                })
            }
        }
        /*加载JS文件,url:文件路径,success:加载成功回调函数*/
        function loadFile(url, success) {
            if (!FileIsExt(apolloFrontend.classcodes,url)) {
                var ThisType=GetFileType(url);
                var fileObj=null;
                if(ThisType==".js"){
                    fileObj=document.createElement('script');
                    fileObj.src = url;
                }else if(ThisType==".css"){
                    fileObj=document.createElement('link');
                    fileObj.href = url;
                    fileObj.type = "text/css";
                    fileObj.rel="stylesheet";
                }else if(ThisType==".less"){
                    fileObj=document.createElement('link');
                    fileObj.href = url;
                    fileObj.type = "text/css";
                    fileObj.rel="stylesheet/less";
                }
                success = success || function(){};
                fileObj.onload = fileObj.onreadystatechange = function() {
                    if (!this.readyState || 'loaded' === this.readyState || 'complete' === this.readyState) {
                        success();
                        apolloFrontend.classcodes.push(url)
                    }
                }
                document.getElementsByTagName('head')[0].appendChild(fileObj);
            }else{
                success();
            }
        }
        /*获取文件类型,后缀名，小写*/
        function GetFileType(url){
            if(url!=null && url.length>0){
                return url.substr(url.lastIndexOf(".")).toLowerCase();
            }
            return "";
        }
        /*文件是否已加载*/
        function FileIsExt(FileArray,_url){
            if(FileArray!=null && FileArray.length>0){
                var len =FileArray.length;
                for (var i = 0; i < len; i++) {
                    if (FileArray[i] ==_url) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
apolloFrontend.init = apolloFrontend.init || null;
apolloFrontend.dynamicLoading.LoadFileList(["dcUIFramework/jquery-easyui/themes/default/easyui.css","dcUIFramework/jquery-1.11.3.min.js","dcUIFramework/jquery-easyui/jquery.easyui.min.js","dc.pageConfig.js","dcUIFramework/dc.frameWork-0.0.1.js"]);
var dcReady = function(fn)
{
    apolloFrontend.init=fn;
}