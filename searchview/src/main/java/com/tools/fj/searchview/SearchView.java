package com.tools.fj.searchview;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * @name 工程名：yaosuwang
 * @class 包名：com.yaosuce.yaosuwang.widget
 * @describe 描述：
 * @anthor 作者：whffi QQ:84569945
 * @time 时间：2017/7/24 13:46
 * @change 变更：
 * @chang 时间：
 * @class 描述：
 */

public class SearchView extends LinearLayout {
    private Context context;

    private DBHelper dbHelper;
    private List<String> records = new ArrayList<String>();
    private ClearEditText ClearEditText;
    private AppCompatTextView atv_cancel;
    private GridView gridView;
    private SearchHistoryAdapter searchHistoryAdapter;
    private SQLiteDatabase db;
    private String TAG = "Search_View";
    private LinearLayout ll_delete;
    private LinearLayout layout;

    private ImageView img_delete;
    //搜索框关键字
    private String keyword = "";


    private OnSearchClikckListener onSearchClikckListener;


    //记录未加载最后一条空数据时条目数
    private int count = 0;
    //读取ATTR判断那种搜索类型
    private int TYPE;
    private static final int DEFAULT_TYPE = 0;

    public Boolean getClick() {
        return click;
    }

    public void setClick(Boolean click) {
        this.click = click;
    }

    private Boolean click = false;

    public SearchView(Context context) {
        super(context);
        this.context = context;
            init();
    }

    public SearchView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();


    }

    public SearchView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, null, defStyleAttr);
        this.context = context;
        init();


    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        //取xml文件中设定的参数
        init();
    }




    private void init() {
        dbHelper = new DBHelper(context);
        TYPE = DEFAULT_TYPE;
        initView();

        ClearEditText.setOnKeyListener(new OnKeyListener() {// 输入完后按键盘上的搜索键


            // 修改回车键功能
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {

                    doSearch();
                }
                return false;
            }
        });
    }

    private void initView() {

        LayoutInflater.from(context).inflate(R.layout.titbar_search, this);
        ll_delete = (LinearLayout) findViewById(R.id.ll_delete);
        ClearEditText = (ClearEditText) findViewById(R.id.ClearEditText_search);
        atv_cancel = (AppCompatTextView) findViewById(R.id.atv_cancel);
        gridView = (GridView) findViewById(R.id.gridview);
        layout = (LinearLayout) findViewById(R.id.layout);
        atv_cancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (atv_cancel.getText().toString().equals("取消")) {

                    ((Activity) context).finish();
                } else if (atv_cancel.getText().toString().equals("搜索")) {
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            ((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    ClearEditText.clearFocus();
                    if (!ClearEditText.getText().toString().trim().equals("")) {
                        insertData(ClearEditText.getText().toString().trim(), TYPE);
                    }

                    setKeyword(ClearEditText.getText().toString().trim());
                    setGirdViewVisible(false);
                    if (onSearchClikckListener != null) {

                        onSearchClikckListener.onSearchClick(keyword);
                        onSearchClikckListener.onVisible(VISIBLE);
                    }

                }

            }
        });


        ClearEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length() == 0) {
                    atv_cancel.setText("取消");
                    searchHistoryAdapter.clear();
                    queryData("", TYPE);

                    if (getCount() == 0) {
                        setGirdViewVisible(false);
                    } else {

                        setGirdViewVisible(true);
                    }

                    if (onSearchClikckListener != null) {
                        searchHistoryAdapter.Refresh();

                        onSearchClikckListener.onVisible(GONE);
                    }

                } else {

                    atv_cancel.setText("搜索");
                    if (getClick()) {
                        setGirdViewVisible(false);
                        setClick(false);
                    } else {
                        queryData(s.toString().trim(), TYPE);
                        if (getCount() == 0) {
                            setGirdViewVisible(false);
                        } else {
                            setGirdViewVisible(true);
                        }

                        searchHistoryAdapter.Refresh();

                    }
                }
            }
        });


        searchHistoryAdapter = new SearchHistoryAdapter(context, records);
        gridView.setAdapter(searchHistoryAdapter);

        gridView.setBackgroundColor(getResources().getColor(R.color.app_bg));
        img_delete = (ImageView) findViewById(R.id.img_delete);
        img_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("确认删除所有记录？").setNegativeButton("取消", null)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteData(TYPE);
                                setGirdViewVisible(false);
                                dialog.dismiss();
                            }
                        }).show();


            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int p = position;
                if (searchHistoryAdapter.getmData().get(position).equals("")) {

                } else {
                    setGirdViewVisible(false);

                    setKeyword(records.get(position));
                    setClick(true);
                    onSearchClikckListener.onSearchClick(keyword);
                    onSearchClikckListener.onVisible(VISIBLE);

                    ClearEditText.setText(records.get(position));
                    ClearEditText.setSelection(records.get(position).length());
                    ClearEditText.clearFocus();
                    ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            ((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            insertData(records.get(p), TYPE);

                        }
                    }).start();


                }
            }
        });


    }

    /*插入数据*/
    private void insertData(String tempName, int TYPE) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from records where name  = '" + tempName + "' and keyword ='" + TYPE + "'");
        db.execSQL("insert into records(name,pinyin,keyword) values('" + tempName + "','','" + TYPE + "')");
        db.close();
    }


    private void queryData(String tempName, int TYPE) {

        //模糊搜索
        Cursor cursor = dbHelper.getReadableDatabase().rawQuery(
                "select  id as _id,name from records where name like '%" + tempName + "%' and keyword = '" + TYPE + "' order by id desc limit 0,10", null);
        //records = new String[cursor.getCount()];
        records.clear();
        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToNext();


            records.add(cursor.getString(1));

        }
        cursor.close();
        if (records.size() == 0) {
            setGirdViewVisible(false);
        }
        //   searchHistoryAdapter.Refresh();
        searchHistoryAdapter.Refresh();
        setCount(records.size());

    }

    /*检查数据库中是否已经有该条记录*/
    private boolean hasData(String tempName, int TYPE) {
        db = dbHelper.getReadableDatabase();
        //查询大类名称
        Cursor cursor = db.rawQuery("select id as _id,name from records where name =? and keyword='" + TYPE + "'", new String[]{tempName});
        return cursor.moveToNext();
    }

    /*清空数据*/
    private void deleteData(int TYPE) {
        db = dbHelper.getWritableDatabase();
        db.execSQL("delete from records where keyword='" + TYPE + "'");
        db.close();
    }

    private void setGirdViewVisible(Boolean viewvisible) {

        if (viewvisible) {
            ll_delete.setVisibility(VISIBLE);
            gridView.setVisibility(VISIBLE);
        } else {
            gridView.setVisibility(GONE);
            ll_delete.setVisibility(GONE);
        }

    }

    private void doSearch() {

        if (ClearEditText.getText().toString().trim().length() == 0) {
            Toast.makeText(context, "搜索内容不能为空", Toast.LENGTH_SHORT).show();

        } else {
            //关闭键盘
            ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                    ((Activity) context).getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            ClearEditText.clearFocus();
            if (!ClearEditText.getText().toString().trim().equals("")) {
                insertData(ClearEditText.getText().toString().trim(), TYPE);
            }

            setKeyword(ClearEditText.getText().toString().trim());
            setGirdViewVisible(false);
            if (onSearchClikckListener != null) {
                onSearchClikckListener.onSearchClick(keyword);
                onSearchClikckListener.onVisible(VISIBLE);
            }

        }


    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }


    public int getCount() {
        return count;
    }


    public void setCount(int count) {
        this.count = count;


    }

    public void sethint(String content) {
        ClearEditText.setHint(content);
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setOnSearchClikckListener(OnSearchClikckListener onSearchClikckListener) {
        this.onSearchClikckListener = onSearchClikckListener;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
        queryData("", TYPE);
    }

    public void setBackoundColor(int colorID) {
        layout.setBackgroundColor(colorID);

    }
}
