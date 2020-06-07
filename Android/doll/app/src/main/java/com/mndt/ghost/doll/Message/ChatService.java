package com.mndt.ghost.doll.Message;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.mndt.ghost.doll.UserSharedPreferences;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Ghost on 2017/5/3.
 */
public class ChatService extends Service {

    private final static int RATEINHZ = 44100;
    private final static int CHANNEL_IN = AudioFormat.CHANNEL_IN_MONO;
    private final static int CHANNEL_OUT = AudioFormat.CHANNEL_OUT_MONO;
    private final static int ENCODING_PCM = AudioFormat.ENCODING_PCM_16BIT;
    public final static byte LOGOUT = 0x00;
    public final static byte LOGIN = 0x7F;
    public final static byte GET_ALL_MESSAGE = 0x01;
    public final static byte SET_ALL_MESSAGE = 0x7E;

    public final static byte SET_AUDIO_MESSAGE = 0x02;
    public final static byte SET_END_AUDIO_MESSAGE = 0x7D;

    //    public final static byte GET_AUDIO_SUCCESS = 0x04;
//    public final static byte GET_AUDIO_FAIL = 0x05;
//
//    public final static byte GET_AUDIO_MESSAGE = 0x03;
//    public final static byte GET_END_AUDIO_MESSAGE = 0x7D;
    public final static String MSG = "message";
    private final static String TAG = "ChatService";
    private final static String IP = "mndtghost.servebeer.com";
    private final static int PORT = 1234;
    private final static int UPLOAD_PORT = 1235;
    private final static int MAX_BUFFER = 512;
    private static MediaRecordToAMR g_mediaRead = null;
    private static Socket g_socketServer = null;
    private static BufferedWriter g_buffwWrite = null;
    private static InputStream g_inputRead = null;
    private static Context g_conText = null;

    public static ArrayList<MessageData> g_alDatas = null;

    private int g_iTotalSize = 0;

    private boolean g_bRunRec = false;

    @Override
    public void onCreate() {
        super.onCreate();
        fnConnnection();
    }

    public final static void fnConnnection() {
        if (g_alDatas == null) {
            g_alDatas = new ArrayList<>();
        }
        final Thread thRunThread = new Thread(g_runSocket);
        thRunThread.start();
    }

    public final static void fnSetContent(final Context conText) {
        g_conText = conText;
    }

    //----------------------------------------------------------------------接收.上傳聲音------START-----------------------------------------------------------------------------------------------------

    public final void fnStartVoice() {
        //fnRecording();
        new Thread(new Runnable() {
            @Override
            public void run() {
                fnMediaRecord();
            }
        }).start();
    }

    public final void fnRecording() {
        new AudioRecordToAWV().execute();
    }

    private final void fnMediaRecord() {
        g_mediaRead = new MediaRecordToAMR();
        g_mediaRead.fnStartMediaRecord();
    }

    public final void fnFinishVoice() {
        // g_bRunRec = false;
        if (g_mediaRead != null) {
            g_mediaRead.fnCloseMediaRecord();
            g_mediaRead = null;
            fnUploadVoice();
        }
    }

    public final void fnUploadVoice() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final InetAddress netAddIp = InetAddress.getByName(IP);
                    final Socket skUploadServer = new Socket(netAddIp, UPLOAD_PORT);
                    final OutputStream outStream = skUploadServer.getOutputStream();
                    Log.e(TAG, "Open Upload Socket Connected");
                    fnWriteVoice(outStream);
                    outStream.close();
                    skUploadServer.close();
                    Log.e(TAG, "Close Upload  Socket Connected");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private final void fnWriteVoice(final OutputStream outStream) {

        try {
            final FileInputStream fileiReadStream = new FileInputStream("/sdcard/ARMRecord.amr");
            final byte[] bData = new byte[MAX_BUFFER];
            int iSize = 0;
            outStream.write(SET_AUDIO_MESSAGE);
            while ((iSize = fileiReadStream.read(bData)) > 0) {
                outStream.write(bData, 0, iSize);
            }
            outStream.write(SET_END_AUDIO_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //----------------------------------------------------------------------接收.上傳聲音------END-----------------------------------------------------------------------------------------------------


//----------------------------------------------------------------------接收.傳送訊息--------START---------------------------------------------------------------------------------------------

    private static Runnable g_runSocket = new Runnable() {
        @Override
        public void run() {
            try {
                final InetAddress netAddIp = InetAddress.getByName(IP);
                g_socketServer = new Socket(netAddIp, PORT);
                g_buffwWrite = new BufferedWriter(new OutputStreamWriter(g_socketServer.getOutputStream()));
                g_inputRead = g_socketServer.getInputStream();
                Log.e(TAG, "Socket Connected");
                if (fnSendAccount()) {
                    while (g_socketServer.isConnected()) {
                        final byte[] bData = new byte[2048];
                        final int iSize = g_inputRead.read(bData);
                        if (iSize > 0) {
                            fnActionHeader(bData, iSize - 1);
                            Log.i(TAG, new String(bData, 1, iSize));
                        }
                    }
                } else {
                    fnClose();
                }
            } catch (Exception e) {
                //e.printStackTrace();
                fnClose();
                Log.e(TAG, "Socket Disconnect=" + e.toString());
            }
        }
    };


    public final static boolean fnWrite(final byte bHeader, final String sData) {
        try {
            g_buffwWrite.write(bHeader);
            g_buffwWrite.write(sData);
            Log.e(TAG, "Handler:" + bHeader + " Socket Send:" + sData);
            g_buffwWrite.flush();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private final static void fnBroadcastMessage(final String sAction, final String sMsg) {
        final MessageData msgData = MessageData.fnSplitMsgToMessageData(sMsg);
        if (msgData != null && !msgData.fnGetAccount().equals(UserSharedPreferences.g_sAccount)) {
            g_alDatas.add(msgData);
            if (g_conText != null) {
                final Intent itAction = new Intent(sAction);
                itAction.putExtra(MSG, "Y");
                LocalBroadcastManager.getInstance(g_conText).sendBroadcast(itAction);
            }
        }
    }

    private final static void fnActionHeader(final byte[] bData, final int iSize) {
        final byte bAction = bData[0];
        switch (bAction) {
            case GET_ALL_MESSAGE:
                fnBroadcastMessage(BroadcastMessages.GET_ALL_MESSAGE, new String(bData, 1, iSize));
                break;
            default:
                break;
        }
    }

//----------------------------------------------------------------------接收.傳送訊息--------END------------------------------------------------------------------------------------------

    private final static boolean fnSendAccount() {
        String sAccountDatas = "";
        sAccountDatas += UserSharedPreferences.g_sIMEI + "_______";
        sAccountDatas += UserSharedPreferences.g_sAccount + "_______";
        sAccountDatas += UserSharedPreferences.g_sPwd + "_______";
        sAccountDatas += UserSharedPreferences.g_sName;
        return fnWrite(LOGIN, sAccountDatas);
    }


    public final static void fnClose() {
        try {
            g_alDatas = null;
            if (g_socketServer != null) {
                g_socketServer.getOutputStream().write(LOGOUT);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "Close Error=" + e.toString());
        }
        finally {
            try {
                if (g_socketServer != null) {
                    g_socketServer.close();
                    g_socketServer = null;
                    g_inputRead.close();
                    g_buffwWrite.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fnClose();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    //----------------------------------------------------------------------接收聲音存AMRClass--------START-----------------------------------------------------------------------------------------------------
    private class MediaRecordToAMR {
        MediaRecorder g_mediaRead;

        public final void fnStartMediaRecord() {
            final String fileName = "ARMRecord.amr";
            try {
                g_mediaRead = new MediaRecorder();
                g_mediaRead.setAudioSource(MediaRecorder.AudioSource.MIC);
                g_mediaRead.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
                g_mediaRead.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                g_mediaRead.setOutputFile("/sdcard/" + fileName);
                g_mediaRead.prepare();
                g_mediaRead.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public final void fnCloseMediaRecord() {
            g_mediaRead.stop();
            g_mediaRead.release();
            g_mediaRead = null;
        }
    }

    //----------------------------------------------------------------------接收聲音存AMRClass--------END-----------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------------接收聲音存AWVClass--------START-----------------------------------------------------------------------------------------------------

    private class AudioRecordToAWV extends AsyncTask<Void, Integer, ArrayList> {
        private RandomAccessFile g_radaFile = null;
        private byte[] g_bReadByte = null;
        private int g_iRecBuffer = 0;
        private ArrayList g_lsReadByte = null;
        private AudioRecord g_audioRead = null;

        private final void fnInit() {
            g_lsReadByte = new ArrayList<>();
            g_bRunRec = true;
            g_iRecBuffer = AudioRecord.getMinBufferSize(RATEINHZ,
                    CHANNEL_IN,
                    ENCODING_PCM);

            g_audioRead = new AudioRecord(MediaRecorder.AudioSource.MIC,
                    RATEINHZ,
                    CHANNEL_IN,
                    ENCODING_PCM,
                    g_iRecBuffer);
            g_bReadByte = new byte[g_iRecBuffer];

            try {
                g_radaFile = new RandomAccessFile(Environment.getExternalStorageDirectory().getPath() + "/my.wav", "rw");
                fnWriteWAVHealder();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        private final void fnWriteWAVHealder() throws IOException {
            g_radaFile.setLength(0);
            g_radaFile.writeBytes("RIFF");
            g_radaFile.writeInt(0); // Final file size not known yet, write 0
            g_radaFile.writeBytes("WAVE");
            g_radaFile.writeBytes("fmt ");
            g_radaFile.writeInt(Integer.reverseBytes(16)); // Sub-chunk size, 16 for PCM
            g_radaFile.writeShort(Short.reverseBytes((short) 1)); // AudioFormat, 1 for PCM
            g_radaFile.writeShort(Short.reverseBytes((short) 1));// Number of channels, 1 for mono, 2 for stereo
            g_radaFile.writeInt(Integer.reverseBytes(RATEINHZ)); // Sample rate
            g_radaFile.writeInt(Integer.reverseBytes(RATEINHZ * CHANNEL_IN * ENCODING_PCM / 8)); // Byte rate
            g_radaFile.writeShort(Short.reverseBytes((short) (CHANNEL_IN * ENCODING_PCM / 8))); // Block align
            g_radaFile.writeShort(Short.reverseBytes((short) 16)); // Bits per sample
            g_radaFile.writeBytes("data");
            g_radaFile.writeInt(0); // Data chunk size not known yet, write 0
        }

        private final void fnWriteWAVEnd() throws IOException {
            g_radaFile.seek(4);
            g_radaFile.writeInt(Integer.reverseBytes(36 + g_iTotalSize));
            g_radaFile.seek(40); // Write size to Subchunk2Size field
            g_radaFile.writeInt(Integer.reverseBytes(g_iTotalSize));
            g_radaFile.close();
        }

        @Override
        protected void onPreExecute() {
            fnInit();
            super.onPreExecute();
        }

        @Override
        protected ArrayList doInBackground(Void... params) {
            try {
                g_audioRead.startRecording();

                while (g_bRunRec) {
                    g_audioRead.read(g_bReadByte, 0, g_iRecBuffer);
                    if (g_bReadByte.length >= 5) {
                        g_radaFile.write(g_bReadByte);
                        g_lsReadByte.add(g_bReadByte);
                        g_iTotalSize += g_iRecBuffer;
                    }
                }
                g_audioRead.stop();
                g_audioRead = null;
                fnWriteWAVEnd();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return g_lsReadByte;
        }
    }

    //----------------------------------------------------------------------接收聲音存AWVClass--------END------------------------------------------------------------------------------------------------------
}
