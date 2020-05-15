//region 通用帮助类接口区域
//region 通用帮助类接口
/**
 * 通用帮助类接口
 */
interface dcUtil {
    /**
     * 判断是否为数组
     * @param object 需要要判断的对象
     * @returns boolean 判断结果
     */
    isArray(object: any): boolean;
    /**
     * 是否为Url路径
     * @param str 需要判断的字符串
     * @returns boolean 判断结果
     */
    isUrl(str: string): boolean;
    /**
     * 当前对象是否为字符串
     * @param object 需要要判断的对象
     * @returns boolean 判断结果
     */
    isString(object: any): boolean;
    /**
     * 日期格式化
     * @param dateOrTimestamp js日期对象或者timestamp
     * @param formatter 格式化，默认值  yyyy-MM-dd
     * @returns string 格式化后的日期
     */
    dateFormatter(dateOrTimestamp: any, formatter: string): string;
    /**
     * Guid对象
     */
    GUID: GUID;
    /**
     * 对象克隆属性，克隆对象b的属性至对象a的一个克隆
     * @param obja 基础对象
     * @param objb 需要克隆属性的对象
     * @param iProArray 克隆忽略的属性
     * @returns object 克隆出来的对象
     */
    cloneObjectPro(obja: any, objb: any, iProArray: any): Object;
    /**
     * 获取当前页面的网址，不包含页面的名称
     * @returns string 当前页面的网址
     */
    getUrl(): string;
    /**
     * 获取网站根路径（包含虚拟路径）
     * @returns string 网站根路径
     */
    getRootUrl(): string;
    /**
     * 获取Url中的参数 Json
     * @returns string Url中的参数
     */
    getRequest(): string;
    /**
     * cookie取值
     * @param key 要获取的Cookie的key值
     * @returns object 返回的Cookie键值对
     */
    getCookie(key: string): Object;
    /**
     * cookie赋值
     * @param key 要设置的Cookie的key值
     * @param value 要设置的Cookie的value值
     */
    setCookie(key: string, value: any): void;
    /**
     * Json字符串转json对象
     * @param strJson 要转换的json字符串
     * @returns object 返回解析后的Josn对象
     */
    parseJson(strJson: string): Object;
    /**
     * Json对象转json字符串
     * @param json 要转换的json对象
     * @returns string 返回解析后的Josn字符串
     */
    jsonToString(json: JSON): string;
    /**
     * html编码
     * @param value 要转码的html字符串
     * @returns string 转码之后的html字符串
     */
    encode(value: string): string;
    /**
     * html解码
     * @param value 要解码的html字符串
     * @returns string 解码之后的html字符串
     */
    decode(value: string): string;
    /**
     * Json日期值格式化
     * @param jsonDate 要格式化的Json日期对象
     * @returns Date 格式化之后的日期对象
     */
    parseJsonDate(jsonDate: JSON): Date;
    /**
     * 四舍五入
     * @param value 要进行四舍五入的值
     * @param length 要保留小数位的位数
     * @returns number 四舍五入之后的结果
     */
    round(value: number, length: number): number;
    /**
     * 向上取整
     * @param value 要进行向上取整的值
     * @param length 要保留小数位的位数
     * @returns number 向上取整之后的结果
     */
    ceiling(value: number, length: number): number;
    /**
     * 向下取整
     * @param value 要进行向下取整的值
     * @param length 要保留小数位的位数
     * @returns number 向下取整之后的结果
     */
    floor(value: number, length: number): number;
    /**
     * 获取精确结果的除法
     * @param arg1 参数1
     * @param arg2 参数2
     * @returns number 计算之后的结果
     */
    accDiv(arg1: number, arg2: number): number;
    /**
     * 获取精确结果的乘法
     * @param arg1 参数1
     * @param arg2 参数2
     * @returns number 计算之后的结果
     */
    accMul(arg1: number, arg2: number): number;
    /**
     * 获取精确结果的加法
     * @param arg1 参数1
     * @param arg2 参数2
     * @returns number 计算之后的结果
     */
    accAdd(arg1: number, arg2: number): number;
    /**
     * 获取精确结果的减法
     * @param arg1 参数1
     * @param arg2 参数2
     * @returns number 计算之后的结果
     */
    accSub(arg1: number, arg2: number): number;
    /**
     * 获取字符串的真正长度
     * @param str 要获取长度的字符串
     * @returns number 计算之后的字符串长度
     */
    strRealLength(str: string): number;
    /**
     * 字符串首尾去空
     * @returns string 首尾去空后的字符串
     */
    trim(): string;
    /**
     * 字符串前置去空
     * @returns string 前置去空后的字符串
     */
    trimLeft(): string;
    /**
     * 字符串后置去空
     * @returns string 后置去空后的字符串
     */
    trimRight(): string;
    /**
     * 判断浏览器是否是IE9以下浏览器
     * @returns true or false
     */
    IsltIE9():boolean
}
//endregion
//region 通用帮助类接口的GUID对象
interface GUID {
    /**
     * 获取一个guid对象
     */
    newGuid(): object;
}
//endregion
//endregion
//region 提示框对象接口区域
/**
 * 提示框对象接口
 */
interface dcInfo {
    /**
     * 提示框对象
     */
    alert(msg: string, option: any): void;
    /**
     * 错误提示框
     * @param msg 消息内容
     * @param option 额外配置对象
     */
    error(msg: string, option: any): void;
    /**
     * 警告框
     * @param msg 消息内容
     * @param option 额外配置对象
     */
    warn(msg: string, option: any): void;
    /**
     * 询问框
     * @param msg 消息内容
     * @param option 额外配置对象
     */
    confirm(msg: string, option: any): void;
    /**
     * 自动隐藏信息框
     * @param msg 消息内容
     * @param option 额外配置对象
     */
    show(msg: string, option: any): void;
}
//endregion
//region 选项卡组对象接口区域
/**
 * 选项卡组对象接口
 */
interface dcTabs {
    /**
     * 添加新的tab
     * @param id tabs的id
     * @param tabObj 需要添加的tab对象
     */
    add(id: string, tabObj: any): void;
    /**
     * 删除指定tab
     * @param id tabs的id
     * @param tabid 需要删除的选项的id
     */
    close(id: string, tabid: string): void;
    /**
     * 选中指定的tab
     * @param id tabs的id
     * @param tabid 需要选中的选项的id
     */
    select(id: string, tabid: string): void;
    /**
     * 刷新指定的tab
     * @param id tabs的id
     * @param tabid 需要刷新的选项的id
     */
    refresh(id: string, tabid: string): void;
    /**
     * 更新指定的tab
     * @param id tabs的id
     * @param tabid 需要更新的选项的id
     * @param tabObj 面板更新对象，可设置三个属性，title,href,selected
     */
    update(id: string, tabid: string, tabObj: any): void;
}
//endregion
//region 弹框对象接口区域
/**
 * 弹框对象接口
 */
interface dcDialog {
    /**
     * 打开弹窗
     * @param title 窗口标题
     * @param url 页面地址
     * @param dialogObj 弹窗对象
     */
    open(title: string, href: string, dialogObj: any): void;
    /**
     * 关闭弹窗
     */
    close(result: any): void;
}
//endregion
/**
 * 前端框架核心对象接口
 */
interface dcjet {
    /**
     * 通用帮助类
     */
    util: dcUtil;
    /**
     * 提示框对象
     */
    info: dcInfo;
    /**
     * 选项卡组对象
     */
    tabs: dcTabs;
    /**
     * 弹框对象
     */
    dialog: dcDialog;
    /**
     * 公共配置信息
     */
    config: Object;
    /**
     * 初始化函数，包括控件初始化
     */
    init(): void;
    /**
     * 根据控件获取控件类型
     * @param control 控件对象
     */
    getControlType(control: any): string;
    /**
     * 获取值（数组）
     * @param selectorOrArray 选择器或者数组,如 #id .class
     */
    getValues(selectorOrArray: any): Object;
    /**
     * 获取值(多个值时，返回一个)
     * @param selectorOrArray 选择器或者数组,如 #id .class
     */
    getValue(selectorOrArray: any): Object;
    /**
     * 设置值 （可以是数组）
     * @param selectorOrMap 选择器或者数组,如 #id .class
     * @param value 要设置的值
     */
    setValue(selectorOrMap: any, value: any): void;
    /**
     * model赋值，只支持Id选择器
     * @param idSelector Id选择器
     * @param model model值
     */
    setModel(idSelector: any, model: any): void;
    /**
     * model 取值， 参数model可不传
     * @param idSelector  Id选择器
     * @param model model值
     */
    getModel(idSelector: any, model: any): void;
    /**
     * Ajax post 封装
     * @param url 请求的url
     * @param data 请求传递的参数
     * @param type 返回的数据类型
     * @param successfn 请求成功后的回掉函数
     * @param errorfn 请求失败后的回掉函数
     */
    post(url: string, data: any, successfn: any, type: any, errorfn: any): void;
    /**
     * Ajax get 封装
     * @param url 请求的url
     * @param data 请求传递的参数
     * @param type 返回的数据类型
     * @param successfn 请求成功后的回掉函数
     * @param errorfn 请求失败后的回掉函数
     */
    get(url: string, data: any, successfn: any, type: any, errorfn: any): void;
    /**
     * Ajax post 同步封装
     * @param url 请求的url
     * @param data 请求传递的参数
     * @param type 返回的数据类型
     */
    syncPost(url: string, data: any, type: any): void;
    /**
     * Ajax get 同步封装
     * @param url 请求的url
     * @param data 请求传递的参数
     */
    syncGet(url: string, data: any, type: any): void;
    /**
     * 获取选项列
     * @param urlOrList url或者列表
     * @param selectedItem 选中项  可空
     * @param isHasPleaseSelect 是否有请选择  默认没有
     * @returns object 返回结果值
     */
    getSelectItemList(urlOrList: any, selectedItem: any, isHasPleaseSelect: boolean): Object;
    /**
     * html编码
     * @param value 要转码的html字符串
     * @returns string 转码之后的html字符串
     */
    encode(value: string): string;
    /**
     * html解码
     * @param value 要解码的html字符串
     * @returns string 解码之后的html字符串
     */
    decode(value: string): string;
    /**
     * 字符串首尾去空
     * @returns string 首尾去空后的字符串
     */
    trim(): string;
    /**
     * 字符串前置去空
     * @returns string 前置去空后的字符串
     */
    trimLeft(): string;
    /**
     * 字符串后置去空
     * @returns string 后置去空后的字符串
     */
    trimRight(): string;
    /**
     * 自动计算表格高度
     * @param string 表格父级标签的id或者class。默认为.dc-grid-wrapper
     * @param string 表格父级标签的同类元素的id或者class 默认为.dc-panel
     */
    autoHeight(currentPanel: string, panel: string): void;
    /**
     * 执行外部框架的函数，可跨域
     * @param Function 执行的函数
     * @param Object argObj 函数的参数键值对象
     */
    invokeOut(fun, argObj);
    /**
     * 获取当前域下最外层window对象
     * @returns window对象
     */
    getCurrentDomianTopWindow(): Window;
    /**
     * 同域下跨页面查找
     * @param string 页面code
     * @param string 选择器
     * @returns window对象或者jquery对象 
     */
    find(pagecode, selecter): JQuery | Window;
    /**
     * 同域下跨页面执行
     * @param string 页面code
     * @param Function 执行的函数体
     */
    invoke(pagecode, fn);
    /**
     * 全局禁用验证
     * @param jquery选择器
     * */
    disableValidation(selector);
    /**
     * 全局启用验证
     * @param jquery选择器
     * */
    enableValidation(selector);
    /**
     * 全局重置验证
     * @param jquery选择器
     * */
    resetValidation(selector);
    /**
     * 全局清除控件的值
     * @param jquery选择器
     * */
    clear(selector);
    /**
     * 全局禁用控件
     * @param jquery选择器
     * */
    disable(selector);
    /**
     * 全局启用控件
     * @param jquery选择器
     * */
    enable(selector);
    /**
     * 全局设置控件为只读模式
     * @param jquery选择器
     * @param mode true or false
     * */
    readonly(selector,mode);
    /**
     * 执行外部框架的函数
     * @param fun 需要执行的函数
     * @param argObj 函数的参数键值对象
     * */
    invokeOut(fun:object, argObj:object):void;
    /**
     * 同域下跨页面查找
     * @param pagecode 页面code
     * @param selecter jquery选择器
     * @returns window对象或者jquery对象
     */
    find (pagecode:string, selecter:string):object
    /**
     * 同域下跨页面执行
     * @param pagecode 页面code
     * @param fn 执行的函数体
     */
    invoke (pagecode:string, fn:object):void
}
declare module "dcjet" {
    export = dc;
}
/**
 * 前端框架核心对象
 */
declare var dc: dcjet;
/**
 * 系统初始化ok后执行
 * @param fn 匿名函数
 */
declare function dcReady(fn): void;