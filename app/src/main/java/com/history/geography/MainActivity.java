package com.history.geography;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    DBHelper dbHelper;
    EditText edtSearch = null;
    ActivityResultLauncher launcher;
    ListView lstView = null;
    int curIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initActivityResult();

        ListView lst = findViewById(R.id.listview);
        lst.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 0, 0, "删除");
                menu.add(0, 1, 1, "编辑");
            }
        });
        dbHelper = new DBHelper(getApplicationContext());
        if (!dbHelper.checkDataBase())
            dbHelper.createDatabase();

        edtSearch = findViewById(R.id.edtSearch);
        lstView = findViewById(R.id.listview);
        ImageButton btn = findViewById(R.id.btnSearch);
        btn.setFocusable(true);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strSearch = edtSearch.getText().toString().trim();
                if (TextUtils.isEmpty(strSearch))
                    return;
                ArrayList<HistoryGeographys> hisgeos = dbHelper.getHistoryGeographyByName(strSearch);
                if (0 == hisgeos.size())
                    return;
                historyGeographyAdapter hisAdapter = new historyGeographyAdapter(MainActivity.this, hisgeos);
                ListView lstView = findViewById(R.id.listview);
                lstView.setAdapter(hisAdapter);
            }
        });
        edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    onBtnSearchClick(null);
                    return true;
                }
                return false;
            }
        });
    }

    public void onBtnSearchClick(View view) {
        String strSearch = edtSearch.getText().toString().trim();

        lstView.setAdapter(null);
        if (TextUtils.isEmpty(strSearch)) {
            Toast.makeText(this, "请输入搜索地名", Toast.LENGTH_SHORT).show();
            return;
        }


        ArrayList<HistoryGeographys> hisgeos = dbHelper.getHistoryGeographyByName(strSearch);
        if (0 == hisgeos.size())
            return;
        historyGeographyAdapter hisAdapter = new historyGeographyAdapter(this, hisgeos);

        lstView.setAdapter(hisAdapter);
    }

    public void onBtnAddHisClick(View view) {
        Intent i = new Intent(MainActivity.this, NewActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        //Log.d("TEST", item.getTitle().toString());
        if (item.getMenuInfo() instanceof AdapterView.AdapterContextMenuInfo) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            //Log.d("TEST", info.position+"--");
            View view = info.targetView;

            TextView txtid = (TextView) view.findViewById(R.id.txtid);
            String no = txtid.getText().toString().trim();
            if ("删除" == item.getTitle().toString().trim()) {
                if (dbHelper.DeleteTable("HistoryGeographys", "no=?", new String[]{no})) {
                    showAlertDialog("删除成功", false);
                    onBtnSearchClick(null);
                } else {
                    showAlertDialog("删除失败", false);
                }
            } else {
                showEditHistoryGeography(no);
            }
        }
        return super.onContextItemSelected(item);
    }

    public void initActivityResult() {

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    onBtnSearchClick(null);
                }
            }
        });
    }

    private void showEditHistoryGeography(String no) {
        HistoryGeographys his = dbHelper.getHistoryGeographyByNo(no);
        if (his == null)
            return;
        Intent i = new Intent(MainActivity.this, NewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("no", his.getNo());
        bundle.putString("name_chs", his.getName_chs());
        bundle.putString("province_name", his.getProvince_name());
        bundle.putString("municipality_name", his.getMunicipality_name());
        bundle.putString("county_name ", his.getCounty_name());
        bundle.putString("township_name", his.getTownship_name());
        bundle.putString("village_name", his.getVillage_name());
        bundle.putString("history", his.getHistory());
        bundle.putString("history_id", String.valueOf(his.getHistory_id()));
        bundle.putString("memo", his.getMemo());
        bundle.putString("isEdit", "true");
        i.putExtra("data", bundle);
        launcher.launch(i);
    }

    private void showAlertDialog(String info, boolean isClose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); // 设置标题
        builder.setMessage(info); // 设置提示信息
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    /**
     * 重写activity 中创建菜单的选项
     *
     * @return 返回真假决定是否显示
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //通过inflater对象将自己写的资源文件转换成menu对象
        //参数1代表需要创建的菜单，参数2代表将菜单设置到对应的menu上
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    /**
     * 重写activity 中菜单选中事件
     *
     * @return 返回真假，对实现效果没有影响。
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.add_item:

                Intent i = new Intent(MainActivity.this, TownshipVillageActivity.class);
                startActivity(i);
                //Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
            case R.id.delete_item:
                onBtnAddHisClick(null);
                //Toast.makeText(this, "del", Toast.LENGTH_SHORT).show();
                break;
            case R.id.brower_item:
                showFirstHistoryGeoGraphy();
                break;
            case R.id.prev_item:
                showPrevHistoryGeoGraphy();
                break;
            case R.id.next_item:
                showNextHistoryGeoGraphy();
            case R.id.getPath_item:
                Pair<String,String> params=null;
                    params=dbHelper.getDatabasePath();


                showDialog("复制文件【"+params.first+"】到【"+params.second+"】");
                break;
        }

        return false;
    }
    private void showDialog(String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); // 设置标题
        builder.setMessage(info); // 设置提示信息
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮后的回调
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showFirstHistoryGeoGraphy() {
        curIndex = -1;
        lstView.setAdapter(null);
        edtSearch.setText("");
        HistoryGeographys his = dbHelper.getHistoryGeographyByNum(curIndex);
        if (null != his && !TextUtils.isEmpty(his.getName_chs())) {
            ArrayList<HistoryGeographys> hisgeos = new ArrayList<HistoryGeographys>();
            hisgeos.add(his);
            historyGeographyAdapter hisAdapter = new historyGeographyAdapter(this, hisgeos);
            lstView.setAdapter(hisAdapter);
            curIndex = his.getNo();
            edtSearch.setText(his.getName_chs());
        }

    }

    private void showPrevHistoryGeoGraphy() {
        edtSearch.setText("");
        if(0>curIndex)
            return;
        curIndex--;
        if(0>curIndex)
            return;
        lstView.setAdapter(null);
        HistoryGeographys his = dbHelper.getHistoryGeographyByNum(curIndex);
        if (null != his && !TextUtils.isEmpty(his.getName_chs())) {
            ArrayList<HistoryGeographys> hisgeos = new ArrayList<HistoryGeographys>();
            hisgeos.add(his);
            historyGeographyAdapter hisAdapter = new historyGeographyAdapter(this, hisgeos);
            lstView.setAdapter(hisAdapter);
            curIndex = his.getNo();
            edtSearch.setText(his.getName_chs());
        }

    }

    private void showNextHistoryGeoGraphy() {
        edtSearch.setText("");
        curIndex++;
        if(0>curIndex)
            return;
        lstView.setAdapter(null);
        HistoryGeographys his = dbHelper.getHistoryGeographyByNum(curIndex);
        if (null != his && !TextUtils.isEmpty(his.getName_chs())) {
            ArrayList<HistoryGeographys> hisgeos = new ArrayList<HistoryGeographys>();
            hisgeos.add(his);
            historyGeographyAdapter hisAdapter = new historyGeographyAdapter(this, hisgeos);
            lstView.setAdapter(hisAdapter);
            curIndex = his.getNo();
            edtSearch.setText(his.getName_chs());
        }

    }

    /**
     * @description 关闭界面软键盘方法 复制直接调closeKeyBoard()用即可实现关闭软键盘
     */
    private InputMethodManager mImm;

    public void closeKeyBoard() {
        mImm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mImm.isActive() && this.getCurrentFocus() != null) {
            if (this.getCurrentFocus().getWindowToken() != null) {
                mImm.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 点击软键盘外面的区域关闭软键盘
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                //这里调用关闭方法
                closeKeyBoard();
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏     *     * @param v     * @param event     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }
    private long exitTime = 0;//定义一个全局退出时间
    /**
     * 返回键监听，按两次退出程序
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000)
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}