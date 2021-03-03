using System;
using System.Collections.Generic;
using System.Text;

namespace ConsoleApplication4
{
    class Program
    {
        static void Main(string[] args)
        {
            MessageServer skMessageServer = new MessageServer();
            UploadServer skUploadServer = new UploadServer();
            skMessageServer.fnStartListen();
            skUploadServer.fnStartListen();
            while (true)
            {
                string input = Console.ReadLine();
                if (input == "exit")
                {
                    break;
                }
                if (input != string.Empty)
                {
                    skMessageServer.fnSendAll(input);
                }
            }
        }
    }
}
