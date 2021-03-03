using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication4
{
    class SocketData
    {
        private Socket g_skSokcet = null;
        private string g_sIMEI = "";
        private string g_sName = "";
        private string g_sAccount = "";
        private string g_sPassword = "";

        public SocketData(ref Socket skServerSocket)
        {
            g_skSokcet = skServerSocket.Accept();
        }

        public bool fnCheckAccount(string g_sIMEI, string g_sName, string g_sAccount, string g_sPassword)
        {
            return true;
        }

        public Socket fnGetSocket()
        {
            return g_skSokcet;
        }

        public int fnSend(byte[] bData)
        {
            return g_skSokcet.Send(bData);
        }

        public bool fnReceiveAsync(ref SocketAsyncEventArgs skaEventArg)
        {
            return g_skSokcet.ReceiveAsync(skaEventArg);
        }
    }
}
