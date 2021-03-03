using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ConsoleApplication4
{
    public class MessageServer
    {
        private Socket g_skServerSokcet;
        private List<Socket> g_lsClentSokcet = new List<Socket>();
        private List<String> g_lsIMEI = new List<String>();
        private List<String> g_lsName = new List<String>();
        private object g_objLocker = new object();
        private const string IP = "127.0.0.1";
        private const int PORT = 7665;
        private const int DATA_LEN = 1024;
        public void fnStartListen()
        {
            g_skServerSokcet = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            Thread thRunListen = new Thread(new ParameterizedThreadStart((socket) =>
            {
                Socket skServer = (Socket)socket;
                skServer.Bind(new IPEndPoint(IPAddress.Parse(IP), PORT));
                skServer.Listen(11000);
                while (true)
                {
                    try
                    {
                        Socket skClientSocket = skServer.Accept();
                        lock (g_objLocker)
                        {
                            SocketAsyncEventArgs skaEventArg = new SocketAsyncEventArgs();
                            byte[] bHeaderBuff = new byte[DATA_LEN];  //緩衝接收大小
                            skaEventArg.SetBuffer(bHeaderBuff, 0, bHeaderBuff.Length);
                            skaEventArg.Completed += fnSendOrReceiveAsync;
                            skClientSocket.ReceiveAsync(skaEventArg);// 異步接收
                            fnAdd(ref skClientSocket);
                            Console.WriteLine("Connection Client:" + skClientSocket.RemoteEndPoint);
                        }
                    }
                    catch { }
                }
            }));
            thRunListen.IsBackground = true;
            thRunListen.Start(g_skServerSokcet);
        }

        private void fnAdd(ref Socket skClientSocket)
        {
            g_lsClentSokcet.Add(skClientSocket);
            g_lsIMEI.Add("");
            g_lsName.Add("");
        }
        private void fnSendOrReceiveAsync(object sender, SocketAsyncEventArgs e)
        {
            Socket skClient = ((Socket)sender);
            if (fnPrintSokcetStatus(ref skClient))
            {
                switch (e.LastOperation)
                {
                    case SocketAsyncOperation.Receive:
                        byte[] bHeaderBuff = new byte[e.BytesTransferred];
                        Array.Copy(e.Buffer, bHeaderBuff, bHeaderBuff.Length);
                        //string sData = System.Text.UTF8Encoding.UTF8.GetString(bHeaderBuff);
                        if (bHeaderBuff.Length > 0)
                        {
                            Console.WriteLine("Get Header:" + bHeaderBuff[0]);
                            Task.Run(() => fnActionMove(ref skClient, ref bHeaderBuff, e));
                        }
                        break;
                    case SocketAsyncOperation.Send:
                        break;
                }
            }
        }

        private void fnActionMove(ref Socket skClient, ref byte[] bData, SocketAsyncEventArgs e)
        {
            byte bAction = bData[0];
            try
            {
                switch (bAction)
                {
                    case RecHeader.GET_ALL_MESSAGE:
                        fnSendAll(ref skClient, System.Text.UTF8Encoding.UTF8.GetString(bData, 1, bData.Length - 1));
                        break;
                    case RecHeader.LOGIN:
                        fnCheckAccount(ref skClient, System.Text.UTF8Encoding.UTF8.GetString(bData, 1, bData.Length - 1));
                        break;
                    case RecHeader.LOGOUT:
                        fnCloseSokcet(ref skClient);
                        break;
                }
                skClient.ReceiveAsync(e); // 非同步接收
            }
            catch
            {
                fnPrintSokcetStatus(ref skClient);
            }
        }

        private bool fnPrintSokcetStatus(ref Socket skClient)
        {
            if (skClient == null) return false;
            if (!skClient.Connected)
            {
                fnCloseSokcet(ref skClient);
                return false;
            }
            return true;
        }

        private void fnCloseSokcet(ref Socket skClient)
        {
            Console.WriteLine("Disconnect Client:" + skClient.RemoteEndPoint);
            skClient.Close();
            int iIndex = g_lsClentSokcet.IndexOf(skClient);
            g_lsClentSokcet.RemoveAt(iIndex);
            g_lsIMEI.RemoveAt(iIndex);
            g_lsName.RemoveAt(iIndex);
            skClient = null;
        }

        private void fnCloseSokcet(Socket skClient)
        {
            Console.WriteLine("Disconnect Client:" + skClient.RemoteEndPoint);
            skClient.Close();
            int iIndex = g_lsClentSokcet.IndexOf(skClient);
            g_lsClentSokcet.RemoveAt(iIndex);
            g_lsIMEI.RemoveAt(iIndex);
            g_lsName.RemoveAt(iIndex);
            skClient = null;
        }

        private void fnCloseSokcet(int iIndex)
        {
            Console.WriteLine("Disconnect Client:" + g_lsClentSokcet[iIndex].RemoteEndPoint);
            g_lsClentSokcet[iIndex].Close();
            g_lsClentSokcet.RemoveAt(iIndex);
            g_lsIMEI.RemoveAt(iIndex);
            g_lsName.RemoveAt(iIndex);
        }

        public void fnSendAll(ref Socket skClient, string data)
        {
            Console.WriteLine("Data:" + data);
            lock (g_objLocker)
            {
                for (int iIndex = 0; iIndex < g_lsClentSokcet.Count; iIndex++)
                {
                    if (g_lsIMEI[iIndex].Length > 14 
                        && !g_lsClentSokcet[iIndex].Equals(skClient))
                    {
                        try
                        {
                            char cHedler = ((char)SendHeader.SET_ALL_MESSAGE);
                            g_lsClentSokcet[iIndex].Send(System.Text.UTF8Encoding.UTF8.GetBytes(cHedler + data));
                        }
                        catch
                        {
                            fnCloseSokcet(g_lsClentSokcet[iIndex]);
                            iIndex--;
                        }
                    }
                }
            }
        }

        public void fnSendAll(string data)
        {
            Console.WriteLine("Data:" + data);
            lock (g_objLocker)
            {
                for (int iIndex = 0; iIndex < g_lsClentSokcet.Count; iIndex++)
                {
                    if (g_lsIMEI[iIndex].Length > 14)
                    {
                        try
                        {
                            char cHedler = ((char)SendHeader.SET_ALL_MESSAGE);
                            g_lsClentSokcet[iIndex].Send(System.Text.UTF8Encoding.UTF8.GetBytes(cHedler + data));
                        }
                        catch
                        {
                            fnCloseSokcet(g_lsClentSokcet[iIndex]);
                            iIndex--;
                        }
                    }
                }
            }
        }

        public void fnCheckAccount(ref Socket skClient, string data)
        {
            Console.WriteLine("Data:" + data);
            lock (g_objLocker)
            {
                string[] sDatas = data.Split(new string[] { "***___***" }, StringSplitOptions.RemoveEmptyEntries);

                if (sDatas.Length == 4)
                {
                    int iIndex = g_lsClentSokcet.IndexOf(skClient);
                    g_lsIMEI[iIndex] = sDatas[0];
                    g_lsName[iIndex] = sDatas[3];
            
                }
            }
        }
    }
}
