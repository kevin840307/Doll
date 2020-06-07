package com.mndt.ghost.doll;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mndt.ghost.doll.Adapter.ButtonAdapter;
import com.mndt.ghost.doll.Forum.ForumActivity;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Image.ImageAction;
import com.mndt.ghost.doll.Image.URLImageData;
import com.mndt.ghost.doll.ImageSelect.ImageSelectActivity;
import com.mndt.ghost.doll.Map.WebMapActivity;
import com.mndt.ghost.doll.Message.ChatService;
import com.mndt.ghost.doll.Message.FloatWindowManager;
import com.mndt.ghost.doll.Message.MessageActivity;
import com.mndt.ghost.doll.Message.WindowServer;
import com.mndt.ghost.doll.View.MyLoveView;
import com.mndt.ghost.doll.View.PopularView;
import com.mndt.ghost.doll.View.SearchView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public static String MESSAGE = "無";
    private boolean bLeave = false;
    private final String TAG = "MainActivity";
    private SectionsPagerAdapter mSectionsPagerAdapter = null;
    private ViewPager mViewPager = null;
    private ButtonAdapter g_badtData = null;
    private ImageView g_imgLine = null;
    private int g_iTabLength = 0;
    private int g_iPage = 0;
    private final int PERMISSION_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        fnTransitionAnimation();
        setContentView(R.layout.activity_main);
        fnInit();
    }

    private final void fnInit() {
        fnCheckAPI();
        InitData();
        fnInitControl();
    }

    private final void InitData() {
        g_iTabLength = Data.WIDTH_PIXELS / 3;
        g_iPage = 0;
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitGridButton();
        fnInitImageView();
        fnInitNavigationView();
        fnInitViewPage();
        fnOpenMsgDialog();
    }


    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitGridButton() {
        final GridView gvButton = (GridView) findViewById(R.id.grid_main_button);
        gvButton.setNumColumns(3);
        final Integer[] iImage = {R.drawable.ic_whatshot_50dp, R.drawable.ic_search_50dp, R.drawable.ic_favorite_50dp};
        final String[] sName = {"熱門", "搜尋", "我的最愛"};
        g_badtData = new ButtonAdapter(this, iImage, sName);
        gvButton.setAdapter(g_badtData);
        gvButton.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.UPDATA_TOP_GRID_BUTTON, position);
            }
        });
    }

    private final void fnInitImageView() {
        g_imgLine = (ImageView) findViewById(R.id.img_main_line);
        final ViewGroup.LayoutParams layoutParams = g_imgLine.getLayoutParams();
        layoutParams.width = g_iTabLength;
        g_imgLine.setLayoutParams(layoutParams);
    }

    private final void fnInitViewPage() {
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), this);
        mViewPager = (ViewPager) findViewById(R.id.view_page);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                final LinearLayout.LayoutParams layoutParams = (android.widget.LinearLayout.LayoutParams) g_imgLine.getLayoutParams();
                if (g_iPage == position) { // 0 -> 1 -> 2
                    layoutParams.leftMargin = (int) (g_iPage * g_iTabLength + positionOffset * g_iTabLength);
                } else if (g_iPage > position) { // 2 ->  1 -> 0
                    layoutParams.leftMargin = (int) (g_iPage * g_iTabLength - ((1 - positionOffset) * g_iTabLength));
                }
                g_iPage = position;
                g_imgLine.setBackgroundColor(Color.WHITE);
                g_imgLine.setLayoutParams(layoutParams);
            }

            @Override
            public void onPageSelected(int position) {
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.UPDATA_TOP_GRID_BUTTON, position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        final FixedSpeedScroller fixedSpeedScroller = new FixedSpeedScroller(this);
        fixedSpeedScroller.setmDuration(900);
        fixedSpeedScroller.fnInitScroller(mViewPager);
    }

    private final void fnInitNavigationView() {
        final Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ActionBarDrawerToggle actionDrawer = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(actionDrawer);
        actionDrawer.syncState();
        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        fnReadNavAccountData();
    }


    private final void fnReadNavAccountData() {
        final String sType = getIntent().getStringExtra("type");
        if (sType.equals("1")) {
            new UserSharedPreferences().fnReadData(getApplication());
            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            final View viewHeader = navigationView.getHeaderView(0);
            final TextView textAccount = (TextView) viewHeader.findViewById(R.id.text_nav_account);
            final TextView textName = (TextView) viewHeader.findViewById(R.id.text_nav_name);
            textAccount.setText(UserSharedPreferences.g_sAccount);
            textName.setText(UserSharedPreferences.g_sName);
            fnfnRunGetUserImage(UserSharedPreferences.g_sAccount);
        }
    }

    private final void fnfnRunGetUserImage(final String sAccount) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String sDirName = "UserImage";
                final String sDataName = sAccount + ".jpg";
                final String sURL = Data.SERRVICE_URL + sDirName + "/" + sDataName;
                final URLImageData urlImageData = new URLImageData(sURL, sDirName, sDataName);
                ImageAction.fnDownloadImage(urlImageData, true);
                final Bitmap btData = ImageAction.fnGetUserImage(sAccount);
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1, btData);
            }
        }).start();
    }

    private final void fnSetUserImage(final Object objData) {
        if (objData != null) {
            final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            final View viewHeader = navigationView.getHeaderView(0);
            final RoundImageView imgUserImage = (RoundImageView) viewHeader.findViewById(R.id.img_nav_pic);
            imgUserImage.setImageBitmap((Bitmap) objData);
        }
    }

    private final void fnOpenMsgDialog() {
        if (!MESSAGE.equals("無")) {
            MESSAGE = MESSAGE.replace("GG", "\n");
            new MessageDialog(this).fnShow(MESSAGE);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            switch (getIntent().getStringExtra("flag")) {
                case "explode":
                    getWindow().setEnterTransition(new Explode());
                    getWindow().setExitTransition(new Explode());
                    break;
                case "slide":
                    getWindow().setEnterTransition(new Slide());
                    getWindow().setExitTransition(new Slide());
                    break;
                case "fade":
                    getWindow().setEnterTransition(new Fade());
                    getWindow().setExitTransition(new Fade());
                    break;
            }
        }
    }

    private final void fnUpdataGridButton(final int iPos) {
        g_badtData.POS = iPos;
        g_badtData.notifyDataSetChanged();
        mViewPager.setCurrentItem(iPos);
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.UPDATA_TOP_GRID_BUTTON:
                    fnUpdataGridButton(msg.arg1);
                    break;
                case HandlerMessage.SET_UI1:
                    fnSetUserImage(msg.obj);
                    break;
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        final int id = item.getItemId();
        if (id == R.id.nav_sign_exit) {
            bLeave = true;
            finish();
        } else if (id == R.id.nav_user_date) {
            fnOpenUserData();
        } else if (id == R.id.nav_map_gps) {
            fnOpenGPSNearShop();
        } else if (id == R.id.nav_msg) {
            fnOpenMessage();
        } else if (id == R.id.nav_fb_powder) {
            fnOpenFeacebookPowder();
        } else if (id == R.id.nav_fb_community2) {
            fnOpenFeacebookCommunity("189162511481267");
        } else if (id == R.id.nav_fb_community) {
            fnOpenFeacebookCommunity("691069547749631");
        } else if (id == R.id.nav_check_permission) {
            if (fnCheckAPI()) {
                Toast.makeText(this, "權限都已開啟", Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.nav_sign_out) {
            fnOpenLoginWay();
        } else if (id == R.id.nav_forum) {
            fnOpenForumActivity();
        }
        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private final void fnOpenForumActivity() {
        final Intent itStart = new Intent(MainActivity.this, ForumActivity.class);
        startActivity(itStart);
    }

    private final void fnOpenLoginWay() {
        final Intent itStart = new Intent(MainActivity.this, LoginWayActivity.class);
        new UserSharedPreferences().fnClearData(getApplication());
        startActivity(itStart);
        finish();
    }

    private final void fnOpenMessage() {
        if (UserSharedPreferences.g_sType.equals("1")) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (!Settings.canDrawOverlays(getApplicationContext())) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    startActivity(intent);
                    return;
                }
            }

            fnCloseFloatServer();
            fnOpenFloatServer();
            fnCloseMessagerServer();
            fnOpenMessagerServer();

        } else {
            Toast.makeText(getApplicationContext(), "登入後才可使用", Toast.LENGTH_SHORT).show();
            fnOpenLoginDialog();
        }
    }

    private final void fnOpenFloatServer() {
        Data.WIDTH = Data.WIDTH_PIXELS / 6;
        Data.HEIGHT = Data.WIDTH;
        final Intent itStartServer = new Intent(MainActivity.this, WindowServer.class);
        itStartServer.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(itStartServer);
    }

    private final void fnOpenMessagerServer() {
        final Intent itStart = new Intent(this, ChatService.class);
        itStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startService(itStart);
    }

    private final void fnCloseFloatServer() {
        ChatService.fnClose();
        final Intent itStart = new Intent(this, WindowServer.class);
        startService(itStart);
        FloatWindowManager.fnRemoveSmallWindow(getApplicationContext());
    }

    private final void fnCloseMessagerServer() {
        ChatService.fnClose();
        final Intent itStart = new Intent(this, ChatService.class);
        stopService(itStart);
    }

    private final void fnOpenLoginDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("登入")
                .setMessage("是否要進行登入")
                .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SysApplication.getInstance().fnClose();
                        fnOpenLogin();
                    }
                })
                .setNegativeButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SysApplication.getInstance().fnClose();
                        fnOpenRegister();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenLogin() {
        final Intent itStart = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenRegister() {
        final Intent itStart = new Intent(MainActivity.this, RegisterActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenUserData() {
        final Intent itStart = new Intent(MainActivity.this, UserViewActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.this).toBundle());
    }

    private final boolean fnCheckAPI() {
        if (Build.VERSION.SDK_INT >= 23) {
            final String sPermissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_FINE_LOCATION};
            final int iRead = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            final int iWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int iLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (iRead != PackageManager.PERMISSION_GRANTED
                    || iWrite != PackageManager.PERMISSION_GRANTED
                    || iLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, sPermissions, PERMISSION_RESULT);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_RESULT:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "權限已開啟重新載入即可", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "權限未開啟, 有些圖片.功能無法使用", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    private final void fnOpenFeacebookPowder() {
        String sUrl = "";
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            sUrl = "fb://page/120707168626004";
        } catch (PackageManager.NameNotFoundException e) {
            sUrl = "https://www.facebook.com/%E6%8A%93%E5%AF%B6%E9%BE%8D-120707168626004/";
        }
        final Intent itStart = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
        startActivity(itStart);
    }

    private final void fnOpenFeacebookCommunity(final String sCommunity) {
        String sUrl = "";
        try {
            getPackageManager().getPackageInfo("com.facebook.katana", 0);
            sUrl = "fb://group/" + sCommunity;
        } catch (PackageManager.NameNotFoundException e) {
            sUrl = "https://www.facebook.com/groups/" + sCommunity;
        }
        final Intent itStart = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
        startActivity(itStart);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenGPSNearShop() {
        final Intent itStart = new Intent(this, WebMapActivity.class);
        final Bundle bdData = new Bundle();
        bdData.putInt("type", 1);
        itStart.putExtras(bdData);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bLeave) {
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public static class PlaceholderFragment extends Fragment {
        public static Context g_conActivity = null;
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final int iPage = getArguments().getInt(ARG_SECTION_NUMBER);
            View viewShow = null;
            switch (iPage) {
                case 1:
                    viewShow = inflater.inflate(R.layout.popular_content, container, false);
                    new PopularView().fnInit(viewShow);
                    break;
                case 2:
                    viewShow = inflater.inflate(R.layout.search_content, container, false);
                    new SearchView().fnInit(viewShow);
                    break;
                case 3:
                    viewShow = inflater.inflate(R.layout.mylove_content, container, false);
                    new MyLoveView().fnInit(viewShow);
                    break;
            }
            return viewShow;
        }

    }

    private final void fnOpenDialog() {
        final UserSharedPreferences userSharedPreferences = new UserSharedPreferences();
        final String sEva = userSharedPreferences.fnGetEvaluation(this);
        if (sEva == null || !sEva.equals("1")) {
            final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(MainActivity.this)
                    .setTitle("離開")
                    .setMessage("您要為此APP評價嗎?")
                    .setPositiveButton("確認", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            userSharedPreferences.fnWriteEvaluation(getApplication());
                            fnOpenGooglePlay();
                            finish();
                        }
                    })
                    .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .show();
        } else {
            finish();
        }
    }

    private final void fnOpenGooglePlay() {
        try {
            final Uri uri = Uri.parse("market://details?id=" + getPackageName());
            final Intent itStart = new Intent(Intent.ACTION_VIEW, uri);
            itStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(itStart);
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "您的手機無安裝Play商店", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            fnOpenDialog();
        }
        return true;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Context g_conActivity = null;

        public SectionsPagerAdapter(FragmentManager fm, final Context conActivity) {
            super(fm);
            g_conActivity = conActivity;
        }

        @Override
        public Fragment getItem(int position) {
            PlaceholderFragment.g_conActivity = g_conActivity;
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "熱門";
                case 1:
                    return "搜尋";
                case 2:
                    return "我的最愛";
            }
            return null;
        }

    }

}
