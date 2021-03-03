using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication4
{
    class ClientSocketData
    {
        private List<Socket> g_lsClentSokcet = new List<Socket>();
        private List<byte> g_lsStatus = new List<byte>();

        public Socket fnGetSocket(int iPos)
        {
            return g_lsClentSokcet[iPos];
        }

        public byte fnGetStatus(int iPos)
        {
            return g_lsStatus[iPos];
        }

        public void fnAdd(ref Socket skClient, byte bStatus)
        {
            g_lsClentSokcet.Add(skClient);
            g_lsStatus.Add(bStatus);
        }

        public void fnRemove(ref Socket skClient)
        {
            int iIndex = g_lsClentSokcet.IndexOf(skClient);
            g_lsClentSokcet.RemoveAt(iIndex);
            g_lsStatus.RemoveAt(iIndex);
        }

        public void fnRemove(int iIndex)
        {
            g_lsClentSokcet.RemoveAt(iIndex);
            g_lsStatus.RemoveAt(iIndex);
        }

        public int fnGetIndex(ref Socket skClient)
        {
            return g_lsClentSokcet.IndexOf(skClient);
        }
    }
}
