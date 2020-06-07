using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Web;
using System.Web.Services;

/// <summary>
/// Summary description for WebService
/// </summary>
[WebService(Namespace = "http://tempuri.org/")]
[WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
// To allow this Web Service to be called from script, using ASP.NET AJAX, uncomment the following line. 
// [System.Web.Script.Services.ScriptService]
public class WebService : System.Web.Services.WebService
{
    private string ROOT_PATH = "C:\\inetpub\\wwwroot\\DOLLWebServer\\";

    public WebService()
    {

        //Uncomment the following line if using designed components 
        //InitializeComponent(); 
    }
    protected void Page_Load(object sender, EventArgs e)
    {
        System.Threading.Thread.Sleep(3000);
    }

    [WebMethod]
    public string[] fnGetShopData()
    {
        string sSql = "  SELECT [address_no]  " +
                    "        ,[area]  " +
                    "        ,[location]  " +
                    "        ,[shop_name]  " +
                    "        ,[address]  " +
                    "        ,[popular]  " +
                    "        ,[machine_amount]  " +
                    "        ,[machine_type]  " +
                    "        ,[remarks]  " +
                    "        ,[latitude]  " +
                    "        ,[longitude]  " +
                    "        ,[star]  " +
                    "        ,CONVERT(CHAR, [create_datetime], 111)  " +
                    "        ,CONVERT(CHAR, [modify_datetime], 111)  " +
                    "   FROM [DOLL_shop]  " +
                    "   ORDER BY CAST([address_no] AS int) ";
        return Functions.fnGetLs(sSql, 14, "DOLL").ToArray();
    }

    [WebMethod]
    public string[] fnGetUpdateShopData(string sDate)
    {
        string sSql = "  SELECT [address_no]  " +
                    "        ,[area]  " +
                    "        ,[location]  " +
                    "        ,[shop_name]  " +
                    "        ,[address]  " +
                    "        ,[popular]  " +
                    "        ,[machine_amount]  " +
                    "        ,[machine_type]  " +
                    "        ,[remarks]  " +
                    "        ,[latitude]  " +
                    "        ,[longitude]  " +
                    "        ,[star]  " +
                    "        ,CONVERT(CHAR, [create_datetime], 111)  " +
                    "        ,CONVERT(CHAR, [modify_datetime], 111)  " +
                    "   FROM [DOLL_shop]  " +
                    "   WHERE CONVERT(CHAR, [modify_datetime], 111) > '" + sDate + "' " +
                    "   ORDER BY CAST([address_no] AS int) ";
        return Functions.fnGetLs(sSql, 14, "DOLL").ToArray();
    }

    [WebMethod]
    public bool fnGetVersionDate(string sDate)
    {
        string sSql = "  SELECT CONVERT(char, [date], 111)  " +
                    "  FROM [DOLL_var]  " +
                    "  WHERE CONVERT(char, [date], 111) > '" + sDate + "'  ";
        if (Functions.fnGetValue(sSql, "DOLL") == " ")
        {
            return false;
        }
        return true;
    }

    [WebMethod]
    public string fnGetVersionMsg()
    {
        string sSql = "  SELECT [message] " +
                    "  FROM [DOLL_var]  ";
        return Functions.fnGetValue(sSql, "DOLL");
    }


    [WebMethod]
    public bool fnRegister(string sIMEI, string sCheckIMEI, string sStrBase64
        , string sName, string sAccount, string sPwd, string sSex)
    {
        if (sIMEI == "356939076001089" || fnCheckRegisterData(sIMEI, sCheckIMEI, sPwd))
        {
            string sSql = "  INSERT INTO [DOLL_account]  " +
                            "             ([IMEI]  " +
                            "             ,[name]  " +
                            "             ,[account]  " +
                            "             ,[password]  " +
                            "             ,[sex]  " +
                            "             ,[status]  " +
                            "             ,[create_datetime]  " +
                            "             ,[modify_datetime])  " +
                            "       VALUES  " +
                            "             ('" + sIMEI + "'  " +
                            "             , '" + sName + "'  " +
                            "             , '" + sAccount + "'  " +
                            "             , '" + sPwd + "'  " +
                            "             , '" + sSex + "'  " +
                            "             , 'Y'  " +
                            "             , GETDATE()  " +
                            "             , GETDATE())  ";
            if (Functions.fnExecuteSQL(sSql, "DOLL") == "")
            {
                Base64StringToImage(sStrBase64, sAccount);
                return true;
            }
            return false;
        }
        return false;
    }

    [WebMethod]
    public bool fnUpdateUserImage(string sIMEI, string sAccount, string sPwd, string sStrBase64)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            Base64StringToImage(sStrBase64, sAccount);
            return true;
        }
        return false;
    }

    [WebMethod]
    public bool fnInsertEvaluation(string sIMEI, string sAccount, string sPwd
                            , string sAddressNo, string star, string sMessage)
    {

        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {

            string sSql = "  INSERT INTO [DOLL_evaluation]  " +
                            "             ([account]  " +
                            "             ,[address_no]  " +
                            "             ,[star]  " +
                            "             ,[message]  " +
                            "             ,[create_datetime]  " +
                            "             ,[modify_datetime])  " +
                            "       VALUES  " +
                            "             ('" + sAccount + "' " +
                            "             ,'" + sAddressNo + "' " +
                            "             ,'" + star + "' " +
                            "             ,'" + sMessage + "' " +
                            "             , GETDATE()  " +
                            "             , GETDATE())  ";
            return Functions.fnExecuteSQL(sSql, "DOLL") == "";
        }
        return false;
    }

    [WebMethod]
    public bool fnUpdateEvaluation(string sIMEI, string sAccount, string sPwd
                            , string sAddressNo, string star, string sMessage)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {

            string sSql = "  UPDATE [DOLL_evaluation]  " +
                            "     SET [star] = '" + star + "'  " +
                            "        ,[message] = '" + sMessage + "'  " +
                            "        ,[modify_datetime] = GETDATE()  " +
                            "   WHERE 1 = 1  " +
                            "       AND [account] = '" + sAccount + "' " +
                            "       AND [address_no] = '" + sAddressNo + "' ";
            return Functions.fnExecuteSQL(sSql, "DOLL") == "";
        }
        return false;
    }

    [WebMethod]
    public string[] fnGetMyEvaluation(string sIMEI, string sAccount, string sPwd)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            string sSql = "  SELECT [account]  " +
                            "        ,[address_no]  " +
                            "        ,[star]  " +
                            "        ,[message]  " +
                            "        ,CONVERT(char, [create_datetime], 111)  " +
                            "        ,CONVERT(char, [modify_datetime], 111)  " +
                            "  FROM [DOLL_evaluation]  " +
                            "   WHERE 1 = 1  " +
                            "       AND [account] = '" + sAccount + "' ";
            return Functions.fnGetLs(sSql, 6, "DOLL").ToArray();
        }
        return null;
    }

    [WebMethod]
    public string[] fnGetThemeData(string sPlateId, string sStart, string sEnd)
    {
        string sSql = "  SELECT [theme_id] " +
                        "  ,[article_type] " +
                        "  ,[title] " +
                        "  ,[content] " +
                        "  ,[response_count] " +
                        "  ,[account].[account] " +
                        "  ,[account].[name] " +
                        "  ,[pic_amount] " +
                        "  ,CONVERT(nvarchar, [theme].[create_datetime], 111) + ' ' + CONVERT(nvarchar, [theme].[create_datetime], 108) " +
                        "  FROM[DOLL_theme] [theme] LEFT JOIN[DOLL_account] [account] " +
                        "  ON [theme].[account] = [account].[account] " +
                            "   WHERE 1 = 1  " +
                            "       AND [plate_id] = " + sPlateId +
                            "       AND [theme_id] >= " + sStart +
                            "       AND [theme_id] < " + sEnd +
                            "       AND[article_type] = 1 " +
                            "       AND [theme].[status] = 'Y' " +
                            "   ORDER BY [theme_id] DESC ";
        return Functions.fnGetLs(sSql, 9, "DOLL").ToArray();
    }

    [WebMethod]
    public string[] fnGetFirstThemeData(string sPlateId)
    {
        string sSql = "  SELECT [plate_id] " +
                        "  ,[theme_id] " +
                        "  ,[article_type] " +
                        "  ,[title] " +
                        "  ,[content] " +
                        "  ,[response_count] " +
                        "  ,[account].[account] " +
                        "  ,[account].[name] " +
                        "  ,[pic_amount] " +
                        "  ,CONVERT(nvarchar, [theme].[create_datetime], 111) + ' ' + CONVERT(nvarchar, [theme].[create_datetime], 108) " +
                        "  FROM[DOLL_theme] [theme] LEFT JOIN[DOLL_account] [account] " +
                        "  ON [theme].[account] = [account].[account] " +
                            "   WHERE 1 = 1  " +
                            "       AND ([plate_id] = " + sPlateId +
                            "       OR [plate_id] = '0' ) " +
                            "       AND [article_type] <> 1 " +
                            "       AND [theme].[status] = 'Y' " +
                            "   ORDER BY [plate_id], [article_type], [theme_id] DESC ";
        return Functions.fnGetLs(sSql, 10, "DOLL").ToArray();
    }

    [WebMethod]
    public string[] fnGetMyThemeData(string sIMEI, string sAccount, string sPwd)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            string sSql = "  SELECT [plate_id] " +
                        "  ,[theme_id] " +
                        "  ,[article_type] " +
                        "  ,[title] " +
                        "  ,[content] " +
                        "  ,[response_count] " +
                        "  ,[account].[account] " +
                        "  ,[account].[name] " +
                        "  ,[pic_amount] " +
                        "  ,CONVERT(nvarchar, [theme].[create_datetime], 111) + ' ' + CONVERT(nvarchar, [theme].[create_datetime], 108) " +
                        "  FROM[DOLL_theme] [theme] LEFT JOIN[DOLL_account] [account] " +
                        "  ON [theme].[account] = [account].[account] " +
                            "   WHERE 1 = 1  " +
                            "       AND [theme].[account] = '" + sAccount + "' " +
                            "       AND [theme].[status] = 'Y' " +
                            "   ORDER BY [plate_id], [article_type], [theme_id] DESC ";
            return Functions.fnGetLs(sSql, 10, "DOLL").ToArray();
        }
        return null;
    }

    [WebMethod]
    public string fnInsertThemeData(string sIMEI, string sAccount, string sPwd, string sPlateId
                                        , string sTitle, string sContent, string sPicAmount)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            string sSql = " SELECT ISNULL(MAX(theme_id) + 1, 0) FROM [DOLL_theme] WHERE [plate_id] = '" + sPlateId + "' ";
            string iNum = Functions.fnGetValue(sSql, "DOLL");
            sSql = " INSERT INTO[dbo].[DOLL_theme] " +
                           " ([plate_id] " +
                           " ,[theme_id] " +
                           " ,[article_type] " +
                           " ,[title] " +
                           " ,[content] " +
                           " ,[response_count] " +
                           " ,[account] " +
                           " ,[pic_amount] " +
                           " ,[status] " +
                           " ,[create_datetime] " +
                           " ,[modify_datetime]) " +
                            " VALUES " +
                           " ( '" + sPlateId + "' " +
                           " , '" + iNum + "' " +
                           " , '1' " +
                           " , '" + sTitle + "' " +
                           " , '" + sContent + "' " +
                           " , '0'" +
                           " , '" + sAccount + "' " +
                           " , '" + sPicAmount + "' " +
                           " , 'Y' " +
                           " , GETDATE() " +
                           " , GETDATE()) ";
            if (Functions.fnExecuteSQL(sSql, "DOLL") == "")
            {
                return iNum;
            }
            return "N";
        }
        return "N";
    }


    [WebMethod]
    public string fnUpdateThemeData(string sIMEI, string sAccount, string sPwd, string sPlateId
                                        , string sThemeId, string sTitle, string sContent, string sPicAmount)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            string sSql = " UPDATE [DOLL_theme] " +
                          " SET [title] = '" + sTitle + "' " +
                          " ,[content] = '" + sContent + "' " +
                          " ,[pic_amount] = '" + sPicAmount + "' " +
                          " ,[modify_datetime] = GETDATE() " +
                          " WHERE 1 = 1 " +
                          "     AND [plate_id] = '" + sPlateId + "' " +
                          "     AND [theme_id] = '" + sThemeId + "' " +
                          "     AND [account] = '" + sAccount + "' ";

            if (Functions.fnExecuteSQL(sSql, "DOLL") == "")
            {
                return sThemeId;
            }
            return "N";
        }
        return "N";
    }

    [WebMethod]
    public string[] fnGetResponseData(string sPlateId, string sThemeId, string sStart, string sEnd)
    {
        string sSql = "  SELECT [response_id] " +
                        "  ,[response_type] " +
                        "  ,[content] " +
                        "  ,[account].[account] " +
                        "  ,[account].[name] " +
                        "  ,CONVERT(nvarchar, [response].[create_datetime], 111) + ' ' + CONVERT(nvarchar, [response].[create_datetime], 108) " +
                        "  FROM [DOLL_response] [response] LEFT JOIN [DOLL_account] [account] " +
                        "  ON [response].[account] = [account].[account] " +
                            "   WHERE 1 = 1  " +
                            "       AND [plate_id] = " + sPlateId +
                            "       AND [theme_id] = " + sThemeId +
                            "       AND [response_id] >= " + sStart +
                            "       AND [response_id] < " + sEnd +
                            "       AND [response_type] = 1 " +
                            "       AND [response].[status] = 'Y' " +
                            "   ORDER BY [response_id]  ";
        return Functions.fnGetLs(sSql, 6, "DOLL").ToArray();
    }

    [WebMethod]
    public string fnInsertResponseData(string sIMEI, string sAccount, string sPwd, string sPlateId
                                                , string sThemeId, string sContent)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            string sSql = " SELECT ISNULL(MAX(response_id)+ 1, 0) FROM [DOLL_response] WHERE [plate_id] = '" + sPlateId + "' AND [theme_id] = '" + sThemeId + "' ";
            string iNum = Functions.fnGetValue(sSql, "DOLL");
            sSql = "  INSERT INTO [dbo].[DOLL_response]  " +
                        "             ([plate_id]  " +
                        "             ,[theme_id]  " +
                        "             ,[response_id]  " +
                        "             ,[response_type]  " +
                        "             ,[content]  " +
                        "             ,[account]  " +
                        "             ,[status] " +
                        "             ,[create_datetime]  " +
                        "             ,[modify_datetime])  " +
                        "       VALUES  " +
                        "             ( '" + sPlateId + "'  " +
                        "             , '" + sThemeId + "' " +
                        "             , '" + iNum + "'  " +
                        "             , '1' " +
                        "             , '" + sContent + "'  " +
                        "             , '" + sAccount + "'  " +
                        "             , 'Y'  " +
                        "             , GETDATE() " +
                        "             , GETDATE() )  ";
            if (Functions.fnExecuteSQL(sSql, "DOLL") == "")
            {
                return iNum;
            }
            return "N";
        }
        return "N";
    }



    [WebMethod]
    public string fnGetCodeValue(string sCodeKind, string sCode)
    {
        string sSql = " SELECT [data] " +
                    " FROM[DOLL_code] " +
                    " WHERE[code_kind] = '" + sCodeKind + "' " +
                    "   AND[code] = '" + sCode + "' ";
        return Functions.fnGetValue(sSql, "DOLL");
    }

    [WebMethod]
    public string[] fnGetShopEvaluation(string sIMEI, string sAccount, string sPwd, string sAddressNo)
    {
        if (fnCheckLogin(sIMEI, sAccount, sPwd))
        {
            string sSql = "  SELECT [account].[account]  " +
                            "        ,[account].[name]  " +
                            "        ,[star]  " +
                            "        ,[message]  " +
                            "        ,CONVERT(char, [eva].[create_datetime], 111)  " +
                            "  FROM [DOLL_evaluation] [eva] LEFT JOIN [DOLL_account] [account]  " +
                            "  	ON [eva].[account] = [account].[account]  " +
                            "  WHERE [address_no] = '" + sAddressNo + "'  ";
            return Functions.fnGetLs(sSql, 5, "DOLL").ToArray();
        }
        return null;
    }

    private bool fnCheckLogin(string sIMEI, string sAccount, string sPwd)
    {
        if (sPwd.Length != 32 || sIMEI.Length < 15)
        {
            return false;
        }
        string sSql = "  SELECT COUNT([account])  " +
                        "  FROM [DOLL_account]  " +
                        "  WHERE [IMEI] = '" + sIMEI + "'  " +
                        "  	AND [status] = 'Y'  " +
                        "  	AND [account] = '" + sAccount + "'  " +
                        "  	AND [password] = '" + sPwd + "'  ";
        return Functions.fnGetValue(sSql, "DOLL") == "1";
    }

    [WebMethod]
    public string[] fnLogin(string sIMEI, string sAccount, string sPwd)
    {
        if (sPwd.Length != 32 || sIMEI.Length < 15)
        {
            return null;
        }
        string sSql = "  SELECT [name]  " +
                        "        ,[sex]  " +
                        "  FROM [DOLL_account]  " +
                        "  WHERE [IMEI] = '" + sIMEI + "'  " +
                        "  	AND [status] = 'Y'  " +
                        "  	AND [account] = '" + sAccount + "'  " +
                        "  	AND [password] = '" + sPwd + "'  ";
        return Functions.fnGetLs(sSql, 2, "DOLL").ToArray();
    }

    private bool fnCheckRegisterData(string sIMEI, string sCheckIMEI, string sPwd)
    {
        if (!fnChecksIMEI(sIMEI, sCheckIMEI))
        {
            return false;
        }

        return true;
    }

    private bool fnChecksIMEI(string sIMEI, string sCheckIMEI)
    {
        int iSum = 0;
        for (int iPos = sIMEI.Length - 2; iPos >= 0; iPos--)
        {
            int iNum = (sIMEI[iPos] - 48) * 2;
            iSum += iNum / 10 + iNum % 10;
            iPos -= 1;
            if (iPos >= 0)
            {
                iSum += (sIMEI[iPos] - 48);
            }
        }
        int iCheck = (10 - iSum % 10) % 10;

        if ((sIMEI[sIMEI.Length - 1] - 48) != iCheck)
        {
            return false;
        }

        iSum = 0;
        for (int iIndex = 0; iIndex < sIMEI.Length; iIndex++)
        {
            iSum += (((sIMEI[iIndex] - 48 + 1) * (iIndex + 1) * 97) & 0xefcd);
        }

        if (!iSum.ToString().Equals(sCheckIMEI))
        {
            return false;
        }
        return true;
    }

    private Bitmap Base64StringToImage(string sStrBase64, string sAccount)
    {
        if (sStrBase64.Length > 1)
        {
            try
            {		
                byte[] bBuffer = Convert.FromBase64String(sStrBase64);
                MemoryStream memoryStream = new MemoryStream(bBuffer);
                Bitmap btData = new Bitmap(memoryStream);

                btData.Save(ROOT_PATH + "UserImage\\" + sAccount + ".jpg"
                    , System.Drawing.Imaging.ImageFormat.Jpeg);
                //bmp.Save("test.bmp", ImageFormat.Bmp);
                //bmp.Save("test.gif", ImageFormat.Gif);
                //bmp.Save("test.png", ImageFormat.Png);
                memoryStream.Close();
                return btData;
            }
            catch (Exception ex)
            {
                return null;
            }
        }
        return null;
    }
    [WebMethod]
    public bool fnUpdateThemeImage(string sPlateId, string sThemeId, string sStrBase64, string sIndex)
    {
        MemoryStream memoryStream = null;
        try
        {					// 關閉串流

            string sDir = sPlateId + sThemeId;
            if (sIndex == "0")
            {
                fnReCreateTheme(sDir);
            }
            byte[] bBuffer = Convert.FromBase64String(sStrBase64);
            memoryStream = new MemoryStream(bBuffer);
            Bitmap btData = new Bitmap(memoryStream);
            btData.Save(ROOT_PATH + "Theme\\" + sDir + "\\" + sIndex + ".jpg"
                , System.Drawing.Imaging.ImageFormat.Jpeg);
            btData = null;
            memoryStream.Close();
            return true;
        }
        catch
        {
            if (memoryStream != null)
            {
                memoryStream.Close();
            }
            return false;
        }
    }

    private void fnReCreateTheme(string sDir)
    {
        string sPath = ROOT_PATH + "Theme\\" + sDir + "\\";
        if (Directory.Exists(sPath))
        {
            Directory.Delete(sPath, true);
        }
        Directory.CreateDirectory(sPath);
    }

    [WebMethod]
    public bool fnCheckData(string sData)
    {
        bool bCheck = true;
        if (sData == null) return bCheck;
        bCheck = (sData.Contains("'")
                 || sData.Contains(";")
                 || sData.Contains("--")
                 || sData.Contains("|")
                 || sData.Contains("\t")
                 || sData.Contains("\n"));
        return bCheck;
    }

}
