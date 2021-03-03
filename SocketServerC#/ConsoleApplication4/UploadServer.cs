using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ConsoleApplication4
{
    class UploadServer
    {
        private Socket g_skServerSokcet;
        private List<Socket> g_lsClentSokcet = new List<Socket>();
        private object g_objLocker = new object();
        private const string IP = "127.0.0.1";
        private const int PORT = 7661;
        private const int DATA_LEN = 2048;
        public void fnStartListen()
        {
            g_skServerSokcet = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            Thread thRunListen = new Thread(new ParameterizedThreadStart((socket) =>
            {
                Socket skServer = (Socket)socket;
                skServer.Bind(new IPEndPoint(IPAddress.Parse(IP), PORT));
                skServer.Listen(1000);
                while (true)
                {
                    try
                    {
                        Socket skClientSocket = skServer.Accept();
                        lock (g_objLocker)
                        {
                            SocketAsyncEventArgs skaEventArg = new SocketAsyncEventArgs();
                            byte[] bHeaderBuff = new byte[5];  //緩衝接收大小
                            skaEventArg.SetBuffer(bHeaderBuff, 0, bHeaderBuff.Length);
                            skaEventArg.Completed += fnSendOrReceiveAsync;
                            skClientSocket.ReceiveAsync(skaEventArg);// 異步接收
                            g_lsClentSokcet.Add(skClientSocket);
                            Console.WriteLine("Connection Client:" + skClientSocket.RemoteEndPoint);
                        }
                    }
                    catch { }
                }
            }));
            thRunListen.IsBackground = true;
            thRunListen.Start(g_skServerSokcet);
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
                            Console.WriteLine("UpLoad Get Header:" + bHeaderBuff[0]);
                            Task.Run(() => fnActionMove(ref skClient, bHeaderBuff[0], e));
                        }
                        break;
                    case SocketAsyncOperation.Send:
                        break;
                }
            }
        }

        private void fnActionMove(ref Socket skClient, byte bAction, SocketAsyncEventArgs e)
        {
            try
            {
                switch (bAction)
                {
                    case RecHeader.GET_AUDIO_MESSAGE:
                        fnGetDataStream(ref skClient);
                        break;
                    case RecHeader.GET_END_AUDIO_MESSAGE:
                        fnCloseSokcet(ref skClient);
                        break;
                }
                //skClient.ReceiveAsync(e); // 非同步接收
            }
            catch
            {
                fnPrintSokcetStatus(ref skClient);
            }
        }

        private void fnGetDataStream(ref Socket skClient)
        {
            byte[] bClientData = null;
            int iAcceptLen = 0;
            bClientData = new byte[DATA_LEN];

            try
            {
                FileStream fsStream = new FileStream("測試.amr", FileMode.OpenOrCreate, FileAccess.ReadWrite);
                while ((iAcceptLen = skClient.Receive(bClientData)) > 1)
                {
                    fsStream.Write(bClientData, 0, iAcceptLen);
                }

                if (iAcceptLen == 1 && RecHeader.GET_END_AUDIO_MESSAGE != bClientData[0])
                {
                    fsStream.Write(bClientData, 0, iAcceptLen);
                }
                fsStream.Flush();
                fsStream.Close();
                fnCloseSokcet(ref skClient);
            }
            catch
            {

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
            g_lsClentSokcet.Remove(skClient);
            skClient = null;
        }
    }
}
