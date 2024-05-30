package com.history.geography;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class NewVillageActivity extends AppCompatActivity {
    DBHelper dbHelper;
    String idProvince;
    String nameProvince;
    String idMunicipality;
    String nameMunicipality;
    String idCounty;
    String nameCounty;
    String ppName;
    String idTownship;
    String nameTonwship;
    TextView txtProvinceName;
    TextView txtMunicipalityName;
    TextView txtCountyName;
    TextView txtTownshipName;
    EditText edtVillageName;
    EditText edtVillageId;
    EditText edtLng;
    EditText edtLat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_village);
        dbHelper = new DBHelper(getApplicationContext());
        if (!dbHelper.checkDataBase())
            dbHelper.createDatabase();
        else
            dbHelper.openDataBase();

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("countys");
        idProvince = bundle.getString("province_Location_id");
        nameProvince = bundle.getString("province_Name_chs");
        idMunicipality = bundle.getString("municipality_Location_id");
        nameMunicipality = bundle.getString("municipality_Name_chs");
        idCounty = bundle.getString("county_Location_id");
        nameCounty = bundle.getString("county_Name_chs");
        ppName = bundle.getString("pp_name");
        idTownship=bundle.getString("township_Location_id");
        nameTonwship=bundle.getString("township_Location_name");
        txtProvinceName = findViewById(R.id.txtVProvince);
        txtProvinceName.setText(nameProvince);
        txtMunicipalityName = findViewById(R.id.txtVMunicipality);
        txtMunicipalityName.setText(nameMunicipality);
        txtCountyName = findViewById(R.id.txtVCounty);
        txtCountyName.setText(nameCounty);
        txtTownshipName = findViewById(R.id.txtVTownName);
        txtTownshipName.setText(nameTonwship);
        edtVillageName=findViewById(R.id.edtVillageName);

        edtVillageId=findViewById(R.id.edtVillageid);
        edtLng = findViewById(R.id.edtVLng);
        edtLat = findViewById(R.id.edtVlat);
        String countId;
        countId = idCounty;
        ArrayList<Villages> villages = dbHelper.getVillagsByTownship(idTownship, 9);
        ArrayList<Integer> nos = new ArrayList<Integer>();
        for (Villages t : villages) {
            String id = t.getLocation_id().substring(11);
            try {
                int no = Integer.parseInt(id);
                nos.add(no);
            } catch (Exception e) {
            }
        }
        int maxNo = 0;
        if (nos.isEmpty())
            maxNo = 0;
        else
            maxNo = Collections.max(nos) + 1;
        if (0 == maxNo)
            maxNo = 1;
        DecimalFormat decimalFormat = new DecimalFormat("00");
        String newNo = idTownship.substring(0, 11) + decimalFormat.format(maxNo) ;
        edtVillageId = findViewById(R.id.edtVillageid);
        edtVillageId.setText(newNo);
    }

    public void onBtnVillageSaveClick(View view) {
        String  villageName= edtVillageName.getText().toString().trim();
        if(TextUtils.isEmpty(villageName)){
            Toast.makeText(this,"请输入村名称", Toast.LENGTH_SHORT).show();
            return;
        }

        if(dbHelper.isExisVillageNamet(villageName,idTownship))
        {
            //showDialog(villageName+"已经在【"+nameTonwship+"】地区存在");
            Toast.makeText(this, villageName+"已经在【"+nameTonwship+"】地区存在", Toast.LENGTH_SHORT).show();
            return;
        }
        ContentValues record = new ContentValues();
        record.put("location_id", edtVillageId.getText().toString().trim());
        record.put("name_chs", edtVillageName.getText().toString().trim());
        record.put("parent_id", idTownship);
        record.put("lng", edtLng.getText().toString().trim());
        record.put("lat", edtLat.getText().toString().trim());
        record.put("pp_name", nameCounty);
        record.put("parent_name", nameTonwship);
        if (dbHelper.InsertTable("Villages", record)) {

            showAlertDialog("保存成功");

        } else
            Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
    }
    private void showAlertDialog(String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); // 设置标题
        builder.setMessage(info); // 设置提示信息
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮后的回调
                Intent resultIntent = new Intent();
                resultIntent.putExtra("location_id", edtVillageId.getText().toString().trim());
                resultIntent.putExtra("parent_id", idTownship);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showDialog(String info) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); // 设置标题
        builder.setMessage(info); // 设置提示信息
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮后的回调
//                Intent resultIntent = new Intent();
//                resultIntent.putExtra("location_id", edtTownshipid.getText().toString().trim());
//                resultIntent.putExtra("parent_id", idCounty);
//                setResult(RESULT_OK, resultIntent);
//                finish();
            }
        });
        // 创建并显示对话框
        AlertDialog dialog = builder.create();
        dialog.show();
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
        getMenuInflater().inflate(R.menu.new_village, menu);

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
            case R.id.save_village:
                onBtnVillageSaveClick(null);
                //Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }
}