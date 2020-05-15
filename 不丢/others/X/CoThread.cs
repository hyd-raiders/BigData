using Dcjet.Framework.Helpers;
using Dcjet.Framework.X.Log;
using System;
using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading;

namespace X.Utils
{
    /// <summary>
    /// 多线程调用类
    /// </summary>
    public class CoThread
    {
        static CoThread()
        {
            //读取配置值
            int workerThreads = ConfigureHelper.GetConfigureIntValue("X_CO_WORKER_THREADS", 1000);
            int completionPortThreads = ConfigureHelper.GetConfigureIntValue("X_CO_COMPLETION_PORT_THREADS", 100);
            ThreadPool.SetMaxThreads(workerThreads, completionPortThreads);
            new CoLog().Init("co.log", "");
        }

        #region 实例化实现
        private CoThread()
        {
        }
        /// <summary>
        /// 线程级缓存
        /// </summary>
        [ThreadStatic]
        private static CoThread _innerThread = new CoThread();

        /// <summary>
        /// 获取co实体
        /// </summary>
        /// <returns></returns>
        public static CoThread GetInstance()
        {
            return new CoThread();
            //return _innerThread;
        }
        #endregion

        private int count = 0;
        private ManualResetEvent resetEvent = new ManualResetEvent(false);
        private CoData _data = new CoData();

        /// <summary>
        /// 线程安全的数据帮助集合，主线程及所有子线程均能访问
        /// </summary>
        public CoData Data { get { return _data; } }

        private CoData GetThreadData() {
            return new CoData()
            {
                ExceptionQueue = this.Data.ExceptionQueue,
                Map = this.Data.Map,
                Queue = this.Data.Queue
            };
        }

        private CoAction<object> GetSwappedAction<T>()
        {
            return p => {
                CoParam cp = p as CoParam;
                DateTime pre = DateTime.Now;
                double mSec = 0;
                string exInfo = "";
                try
                {
                    cp.OneParamAction(null);
                }
                catch (Exception ex)
                {
                    CoException cex = new CoException("error", ex);
                    if (cp.objectParam != null)
                        exInfo += "异常方法参数：" + JsonHelper.SerializeObject(cp.objectParam) + "\r\n";
                    exInfo += "异常信息：" + ex.ToString();
                    Data.ExceptionQueue.Enqueue(cex);
                }
                finally
                {
                    mSec = (DateTime.Now - pre).TotalMilliseconds;
                    cp.Data.Log.Debug("方法调用时间:" + mSec + "\r\n" + exInfo);
                    if (Interlocked.Decrement(ref count) == 0)
                        resetEvent.Set();
                }
            };
        }

        /// <summary>
        /// 添加启动一个无参数的线程
        /// th.Add(coData => { //todo your code});
        /// </summary>
        /// <param name="action">执行逻辑</param>
        public void Add(CoAction<CoData> action)
        {
            if (action == null) return;
            Interlocked.Increment(ref count);
            var threadData = GetThreadData();
            ThreadPool.QueueUserWorkItem(new WaitCallback(this.GetSwappedAction<object>()), new CoParam()
            {
                OneParamAction = o => { action(threadData); },
                ParamType = CoParamType.NONE,
                Data = threadData
            });
        }

        /// <summary>
        /// 添加启动一个动态参数的线程
        /// th.Add((dynamic, coData) =>{ // your codes },new{ param1 ="p" , param2 = 1});
        /// </summary>
        /// <param name="action">执行逻辑</param>
        /// <param name="param">动态参数</param>
        public void Add(CoAction<dynamic, CoData> action, dynamic param)
        {
            if (action == null) return;
            Interlocked.Increment(ref count);
            var threadData = GetThreadData();
            ThreadPool.QueueUserWorkItem(new WaitCallback(this.GetSwappedAction<dynamic>()), new CoParam()
            {
                OneParamAction = (p => { action(param, threadData); }),
                ParamType = CoParamType.DYNAMIC,
                objectParam = param,
                Data = threadData
            });
        }

        /// <summary>
        /// 添加启动指定参数类型的线程
        /// YourClass yourParam = new YourClass();
        /// th.Add<YourClass>((param, coData) => { //your codes },yourParam);
        /// </summary>
        /// <typeparam name="T">类型</typeparam>
        /// <param name="action">执行逻辑</param>
        /// <param name="param">动态参数</param>
        public void Add<T>(CoAction<T, CoData> action, T param)
        {
            if (action == null) return;
            Interlocked.Increment(ref count);
            var threadData = GetThreadData();
            ThreadPool.QueueUserWorkItem(new WaitCallback(this.GetSwappedAction<T>()), new CoParam()
            {
                OneParamAction = (p => { action((T)param, threadData); }),
                ParamType = CoParamType.TYPE,
                objectParam = param,
                Data = threadData
            });
        }

        /// <summary>
        /// 根据参数列表批量启动执行线程
        /// </summary>
        /// <typeparam name="T">类型</typeparam>
        /// <param name="action">执行逻辑</param>
        /// <param name="paramList">参数列表</param>
        public void BatchAdd<T>(CoAction<T, CoData> action, IEnumerable<T> paramList)
        {
            if (paramList == null) return;
            foreach (T p in paramList)
                Add(action, p);
        }

        /// <summary>
        /// 根据参数列表批量启动执行线程
        /// </summary>
        /// <param name="action">执行逻辑</param>
        /// <param name="paramList">参数列表</param>
        public void BatchAdd(CoAction<dynamic, CoData> action, IEnumerable<dynamic> paramList)
        {
            if (paramList == null) return;
            foreach (dynamic p in paramList)
                Add(action, p);
        }

        /// <summary>
        /// 批量启动执行count数量的线程
        /// </summary>
        /// <param name="action">执行逻辑</param>
        /// <param name="count">参数列表</param>
        public void BatchAdd(CoAction<CoData> action, int count)
        {
            for (int i = 0; i < count; i++)
                Add(action);
        }

        /// <summary>
        /// 等待所有添加的线程执行完
        /// </summary>
        public void Wait()
        {
            try
            {
                if (count != 0)
                    resetEvent.WaitOne();
            }
            catch (Exception ex)
            {
                Data.Log.Error("CoThread Error: resetEvent.WaitOne :" + ex.ToString());
            }
            finally
            {
                resetEvent.Close();
                resetEvent = new ManualResetEvent(false);
            }
        }

        /// <summary>
        ///  清除所有的内部数据
        /// </summary>
        public void ClearAll()
        {
            _data = new CoData();
        }
    }

    /// <summary>
    /// 内部数据类，用于多线程数据处理
    /// </summary>
    public class CoData
    {
        internal CoData() {
            this.Log = new CoLog();
        }
        /// <summary>
        /// 线程安全map集合
        /// </summary>
        public ConcurrentDictionary<object, object> Map = new ConcurrentDictionary<object, object>();

        /// <summary>
        /// 线程安全的队列
        /// </summary>
        public ConcurrentQueue<object> Queue = new ConcurrentQueue<object>();

        /// <summary>
        /// 线程安全的异常队列，多线程中出现的异常自动加载到此队列
        /// </summary>
        public ConcurrentQueue<object> ExceptionQueue = new ConcurrentQueue<object>();

        /// <summary>
        /// 日志对象
        /// </summary>
        public IDLog Log { get; private set; }
    }

    public class CoException : Exception
    {
        public CoException() : base() { }

        public CoException(string message, Exception innerException) : base(message, innerException) { }
    }

    class CoParam
    {
        internal CoParamType ParamType { get; set; }
        internal object objectParam { get; set; }
        internal CoAction<object> OneParamAction { get; set; }
        internal CoData Data { get; set; }
    }

    enum CoParamType
    {
        NONE = 1,
        DYNAMIC = 2,
        TYPE = 3
    }

    public delegate void CoAction<in T>(T obj);
    public delegate void CoAction<in T1, in T2>(T1 arg1, T2 arg2);
}
