using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication4
{
    class RecHeader
    {
        public const byte LOGOUT = 0x00;
        public const byte LOGIN = 0x7F;

        public const byte GET_ALL_MESSAGE = 0x7E;
        public const byte GET_AUDIO_MESSAGE = 0x02;
        public const byte GET_END_AUDIO_MESSAGE = 0x7D;
    }
}
