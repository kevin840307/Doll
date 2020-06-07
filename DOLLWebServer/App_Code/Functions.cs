using System;
using System.Collections.Generic;
using System.Data;
using System.Drawing;
using System.IO;
using System.Web;
using System.Web.UI;
using System.Web.UI.WebControls;

/// <summary>
/// Summary description for Functions
/// </summary>
public class Functions
{
    public static string fnGetConStr(string sLsConn)
    {
        return System.Web.Configuration.WebConfigurationManager.ConnectionStrings[sLsConn].ConnectionString;
    }
    public static System.Data.DataTable fnGetDt(string sStr, string sLsConn)
    {
        System.Data.SqlClient.SqlConnection sqlConn = new System.Data.SqlClient.SqlConnection(fnGetConStr(sLsConn));
        System.Data.SqlClient.SqlDataAdapter sqldataAdapter = new System.Data.SqlClient.SqlDataAdapter(sStr, sqlConn);
        System.Data.DataTable dtData = new System.Data.DataTable();
        sqldataAdapter.Fill(dtData);
        return dtData;
    }

    public static System.Data.DataTable fnGetProgrameAuthority(string sId)
    {
        string sSql = " SELECT DISTINCT [program_d].[program_id] " +
                        " FROM [MNDTprogram_details] [program_d], [MNDTgroup_details] [group_d] " +
                        " WHERE [program_d].[group_id] = [group_d].[group_id] " +
                        "   AND [group_d].[account_id] = '" + sId + "' " +
                        "   AND [program_d].[au_run] = 'true' ";
        return Functions.fnGetDt(sSql, "MNDT");
    }

    public static System.Data.DataTable fnLoginDT(ref string sId, ref string sPassword)
    {
        string sSql = " SELECT [account_name] " +
                        " FROM [MNDTaccount] " +
                        " WHERE [account_id] = '" + sId + "' " +
                        "   AND [account_password] = '" + sPassword + "' ";
        System.Data.DataTable dtData = Functions.fnGetDt(sSql, "MNDT");
        return Functions.fnGetDt(sSql, "MNDT"); ;
    }

    public static string fnExecuteSQL(string sSql, string sConn)
    {
        System.Data.SqlClient.SqlConnection conn = null;
        conn = new System.Data.SqlClient.SqlConnection(fnGetConStr(sConn));
        System.Data.SqlClient.SqlCommand cmd = new System.Data.SqlClient.SqlCommand();

        cmd.Connection = conn;
        try
        {
            conn.Open();
            cmd.CommandText = sSql;
            cmd.ExecuteNonQuery();
            return "";
        }
        catch (Exception ex)
        {
            return ex.Message.ToString();
        }
        finally
        {
            conn.Close();
        }
    }

    public static string fnGetValue(string sSql, string sConn)
    {
        string sValue = null;
        System.Data.SqlClient.SqlConnection sqlConn = new System.Data.SqlClient.SqlConnection(fnGetConStr(sConn));
        sqlConn.Open();
        System.Data.SqlClient.SqlCommand sqlComm = null;
        sqlComm = new System.Data.SqlClient.SqlCommand(sSql, sqlConn);
        sValue = (sqlComm.ExecuteScalar() == null) ? " " : sqlComm.ExecuteScalar().ToString();
        sqlConn.Close();
        return sValue;
    }

    public static List<string> fnGetLs(string sqlstr, int acoumt, string ls_conn)
    {
        List<string> lsData = new List<string>();
        try
        {
            System.Data.SqlClient.SqlConnection conn = null;
            conn = new System.Data.SqlClient.SqlConnection(System.Web.Configuration.WebConfigurationManager.ConnectionStrings[ls_conn].ConnectionString.ToString());
            System.Data.SqlClient.SqlCommand cmd = new System.Data.SqlClient.SqlCommand(sqlstr, conn);
            conn.Open();
            System.Data.SqlClient.SqlDataReader reader = cmd.ExecuteReader();

            while (reader.Read())
            {
                for (int pos = 0; pos < acoumt; pos++)
                {
                    lsData.Add(reader[pos].ToString());
                }
            }
            reader.Close();
            cmd.Dispose();
        }
        catch
        {
        }
        return lsData;
    }


    public static void fnAddConditionDate(ref System.Web.UI.WebControls.TextBox textBox, ref string sSql, string sCondition)
    {
        if (textBox.Text.Length > 0)
        {
            sSql += " AND CONVERT(char, " + sCondition + ", 111) LIKE '" + textBox.Text + "' ";
        }
    }

    public static void fnAddCondition(ref System.Web.UI.WebControls.TextBox textBox, ref string sSql, string sCondition)
    {
        if (textBox.Text.Length > 0)
        {
            sSql += " AND " + sCondition + " LIKE '" + textBox.Text + "' ";
        }
    }

    public static void fnAddCondition(ref System.Web.UI.WebControls.DropDownList dropDownList, ref string sSql, string sCondition)
    {
        if (dropDownList.SelectedValue.Length > 0)
        {
            sSql += " AND " + sCondition + " LIKE '" + dropDownList.SelectedValue + "' ";
        }
    }

    public static void fnAddCondition(ref string sSql, ref string sChidSql, string sCondition)
    {
        if (sChidSql.Length > 0)
        {
            sSql += " AND " + sCondition + " IN (" + sChidSql + ") ";
        }
    }
    public static void fnSetDropDownList(string sSql, DropDownList dropData)
    {
        DataTable dtData = fnGetDt(sSql, "MNDT");

        if (dtData.Rows.Count > 0)
        {
            dropData.Items.Clear();
            dropData.Items.Add(new ListItem("", ""));
            for (int iPos = 0; iPos <= dtData.Rows.Count - 1; iPos++)
            {
                string sShow = dtData.Rows[iPos][0].ToString();
                string sValue = dtData.Rows[iPos][1].ToString();
                dropData.Items.Add(new ListItem(sValue, sShow));
            }
        }
    }



    public static string fnInsertSql(DataTable dtData, string sSql, int iAmount)
    {
        string sMessage = "匯入成功<br>";
        string sSaveSql = "";
        int iDTSize = dtData.Rows.Count;
        for (int iPos = 1; iPos < iDTSize; iPos++)
        {
            sSaveSql = sSql;
            if (dtData.Rows[iPos][0].ToString().Replace(" ", "").Length > 0)
            {
                for (int iCodePos = 1; iCodePos <= 5; iCodePos++)
                {
                    sSaveSql = sSaveSql.Replace("{code" + iCodePos + "}", dtData.Rows[iPos][iCodePos - 1].ToString());
                }
                string sExecutMessage = fnExecuteSQL(sSaveSql, "MNDT");
                if (sExecutMessage.Length > 0)
                {
                    sMessage += "匯入錯誤：第" + iPos + "筆" + " 訊息：" + sExecutMessage + "<br><br>";
                }
            }
            else
            {
                return sMessage;
            }
        }
        return sMessage;
    }
}