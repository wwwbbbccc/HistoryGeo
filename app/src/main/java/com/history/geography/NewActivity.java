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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class NewActivity extends AppCompatActivity {
    DBHelper dbHelper;
    Spinner spinNewProvince;
    Spinner spinNewMunicipality;
    Spinner spinNewCounty;
    Spinner spinNewTownship;
    Spinner spinNewVillage;
    Spinner spinNewHis;
    EditText edtNewGeographyName;
    EditText edtNewMemo;
    ArrayList<Municipalitys> municipalitys;
    ArrayList<Countys> countys;
    ArrayList<Townships> townships;
    ArrayList<Villages> villages;
    boolean isEdit = false;
    int editNo=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("data");

        dbHelper = new DBHelper(getApplicationContext());
        if (!dbHelper.checkDataBase())
            dbHelper.createDatabase();
        else
            dbHelper.openDataBase();

        spinNewProvince = findViewById(R.id.spinNewProvince);
        spinNewMunicipality = findViewById(R.id.spinNewMunicipality);
        spinNewCounty = findViewById(R.id.spinNewCounty);
        spinNewTownship = findViewById(R.id.spinNewTownship);
        spinNewVillage = findViewById(R.id.spinNewVillage);
        spinNewHis = findViewById(R.id.spinNewHis);
        edtNewGeographyName = findViewById(R.id.edtNewGeographyName);
        edtNewMemo = findViewById(R.id.edtNewMemo);
        ArrayList<Historys> historys = dbHelper.getAllHistorys();
        HistorysAdapter historysAdapter = new HistorysAdapter(this, historys);
        spinNewHis.setAdapter(historysAdapter);
        ArrayList<Provinces> provinces = dbHelper.getAllProvinces();
        Provinces tempPro = new Provinces(-1, "", "", "", "", "", "");
        provinces.add(0, tempPro);
        provinceAdapter p_adaper = new provinceAdapter(this, provinces);
        spinNewProvince.setAdapter(p_adaper);
        if (null != bundle) {
            editNo = bundle.getInt("no");
            String name_chs = bundle.getString("name_chs");
            String province_name = bundle.getString("province_name");
            String municipality_name = bundle.getString("municipality_name");
            String county_name = bundle.getString("county_name ");
            String township_name = bundle.getString("township_name");
            String village_name = bundle.getString("village_name");
            String history = bundle.getString("history");
            String history_id = bundle.getString("history_id");
            String memo = bundle.getString("memo");
            String isEditStr = bundle.getString("isEdit");
            String provinceId="";
            String municipalityId="";
            String countyId="";
            String townshipId="";
            if(isEditStr.equals("true"))
                isEdit=true;
            if (!TextUtils.isEmpty(province_name)) {
                int index = 0;
                for (Provinces p : provinces) {
                    if (province_name.equals(p.getName_chs()))
                        break;
                    index++;
                }
                if(provinces.size()>index) {
                    spinNewProvince.setSelection(index, false);
                    provinceId = provinces.get(index).getLocation_id();
                    municipalitys = dbHelper.getMunicipalitysByProvince(provinceId);
                    Municipalitys tempMun = new Municipalitys(-1, "", "", "", "", "", "", "", "");
                    municipalitys.add(0, tempMun);
                    municipalitysAdapter municipalityAdapter = new municipalitysAdapter(NewActivity.this, municipalitys);
                    spinNewMunicipality.setAdapter(municipalityAdapter);
                }
            }
            if (!TextUtils.isEmpty(municipality_name)) {
                int index = 0;
                for (Municipalitys p : municipalitys) {
                    if (municipality_name.equals(p.getName_chs()))
                        break;
                    index++;
                }
                if(municipalitys.size()>index) {
                    spinNewMunicipality.setSelection(index, false);
                    municipalityId = municipalitys.get(index).getLocation_id();
                    countys = dbHelper.getCountysByMunicipality(municipalityId, 7);
                    Countys tempCounty = new Countys(-1, "", "", "", "", "", "", "", "", "");
                    countys.add(0, tempCounty);
                    countysAdapter countyAdapter = new countysAdapter(NewActivity.this, countys);
                    spinNewCounty.setAdapter(countyAdapter);

                }
            }

            if (!TextUtils.isEmpty(county_name)) {
                int index = 0;
                for (Countys c : countys) {
                    if (county_name.equals(c.getName_chs()))
                        break;
                    index++;
                }
                if(countys.size()>index) {
                    spinNewCounty.setSelection(index, false);
                    countyId = countys.get(index).getLocation_id();
                    townships = dbHelper.getTownshipsByCounty(countyId, 9);
                    Townships tempTown = new Townships(-1, "", "", "", "", "", "", "", "", "");
                    townships.add(0, tempTown);
                    townshipsAdapter adapter = new townshipsAdapter(NewActivity.this, townships);
                    spinNewTownship.setAdapter(adapter);
                }
            }
            if (!TextUtils.isEmpty(township_name)) {
                int index = 0;
                for (Townships t : townships) {
                    if (township_name.equals(t.getName_chs()))
                        break;
                    index++;
                }
                if(townships.size()>index) {
                    spinNewTownship.setSelection(index, false);
                    townshipId = townships.get(index).getLocation_id();
                    villages = dbHelper.getVillagsByTownship(townshipId, 11);
                    Villages tempVill = new Villages(-1, "", "", "", "", "", "", "", "", "");
                    villages.add(0, tempVill);
                    villagesAdapter adapter = new villagesAdapter(NewActivity.this, villages);
                    spinNewVillage.setAdapter(adapter);
                }
            }
            if (!TextUtils.isEmpty(village_name)) {
                int index = 0;
                for (Villages v : villages) {
                    if (village_name.equals(v.getName_chs()))
                        break;
                    index++;
                }
                if(villages.size()>index) {
                    spinNewVillage.setSelection(index, false);
                }
            }
            if (!TextUtils.isEmpty(history_id)) {
                int index = 0;
                for (Historys h : historys) {
                    if (history_id.equals(String.valueOf(h.getNo())))
                        break;
                    index++;
                }
                if(historys.size()>index) {
                    spinNewHis.setSelection(index, false);
                }
            }
            edtNewGeographyName.setText( name_chs);
            edtNewMemo.setText(memo);
        }


        spinNewTownship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (0 == i)
                    return;
                spinNewVillage.setAdapter(null);
                Townships obj = townships.get(i);
                SetVillagSpinner(obj.getLocation_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinNewProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (0 == i)
                    return;
                spinNewMunicipality.setAdapter(null);
                spinNewCounty.setAdapter(null);
                spinNewTownship.setAdapter(null);
                spinNewVillage.setAdapter(null);
                Provinces obj = provinces.get(i);
                municipalitys = dbHelper.getMunicipalitysByProvince(obj.getLocation_id());

                if (municipalitys.isEmpty()) {
                    countys = dbHelper.getCountysByMunicipality(obj.getLocation_id(), 5);
                    Countys tempCounty = new Countys(-1, "", "", "", "", "", "", "", "", "");
                    countys.add(0, tempCounty);
                    countysAdapter countyAdapter = new countysAdapter(NewActivity.this, countys);
                    spinNewCounty.setAdapter(countyAdapter);
                    spinNewCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (0 == i)
                                return;
                            spinNewVillage.setAdapter(null);
                            Countys obj = countys.get(i);

                            SetTownshipSpinner(obj.getLocation_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    Municipalitys tempMun = new Municipalitys(-1, "", "", "", "", "", "", "", "");
                    municipalitys.add(0, tempMun);
                    municipalitysAdapter municipalityAdapter = new municipalitysAdapter(NewActivity.this, municipalitys);
                    spinNewMunicipality.setAdapter(municipalityAdapter);
                    spinNewMunicipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            if (0 == i)
                                return;
                            spinNewCounty.setAdapter(null);
                            spinNewTownship.setAdapter(null);
                            spinNewVillage.setAdapter(null);
                            Municipalitys obj = municipalitys.get(i);
                            countys = dbHelper.getCountysByMunicipality(obj.getLocation_id(), 7);
                            Countys tempCounty = new Countys(-1, "", "", "", "", "", "", "", "", "");
                            countys.add(0, tempCounty);
                            countysAdapter countyAdapter = new countysAdapter(NewActivity.this, countys);
                            spinNewCounty.setAdapter(countyAdapter);
                            spinNewCounty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (0 == i)
                                        return;
                                    Countys obj = countys.get(i);
                                    SetTownshipSpinner(obj.getLocation_id());
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }

                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void SetTownshipSpinner(String id) {

        townships = dbHelper.getTownshipsByCounty(id, 9);
        Townships tempTown = new Townships(-1, "", "", "", "", "", "", "", "", "");
        townships.add(0, tempTown);
        townshipsAdapter adapter = new townshipsAdapter(NewActivity.this, townships);
        spinNewTownship.setAdapter(adapter);

    }

    private void SetVillagSpinner(String id) {

        villages = dbHelper.getVillagsByTownship(id, 11);
        Villages tempVill = new Villages(-1, "", "", "", "", "", "", "", "", "");
        villages.add(0, tempVill);
        villagesAdapter adapter = new villagesAdapter(NewActivity.this, villages);
        spinNewVillage.setAdapter(adapter);
    }

    public void onbtnSaveNewClick(View view) {
        String geographyName = edtNewGeographyName.getText().toString().trim();
        String detail = edtNewMemo.getText().toString().trim();
        if (TextUtils.isEmpty(geographyName)) {
            //showAlertDialog("请输入地名", false);
            Toast.makeText(this, "请输入地名", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(detail)) {
            Toast.makeText(this, "请输入说明", Toast.LENGTH_SHORT).show();
            //showAlertDialog("请输入说明", false);
            return;
        }
        String proviceName = "";
        Provinces valProv = (Provinces) spinNewProvince.getSelectedItem();
        if (null != valProv)
            proviceName = valProv.getName_chs();
        Municipalitys mun = (Municipalitys) spinNewMunicipality.getSelectedItem();
        String municipalityName = "";
        if (null != mun)
            municipalityName = mun.getName_chs();
        Countys valCounty = (Countys) spinNewCounty.getSelectedItem();
        String countyName = "";
        if (null != valCounty)
            countyName = valCounty.getName_chs();
        String townshipName = "";
        Townships valTown = (Townships) spinNewTownship.getSelectedItem();
        if (null != valTown)
            townshipName = valTown.getName_chs();
        String villageName = "";
        Villages tempVill = (Villages) spinNewVillage.getSelectedItem();
        if (null != tempVill)
            villageName = tempVill.getName_chs();
        String reginName = ((Historys) spinNewHis.getSelectedItem()).getRegion();
        int reginNo = ((Historys) spinNewHis.getSelectedItem()).getNo();

        ContentValues record = new ContentValues();
        record.put("name_chs", geographyName);
        record.put("province_name", proviceName);
        record.put("municipality_name", municipalityName);
        record.put("county_name", countyName);
        record.put("township_name", townshipName);
        record.put("village_name", villageName);
        record.put("history", reginName);
        record.put("history_id", reginNo);
        record.put("memo", detail);
        if(!isEdit) {
            if (dbHelper.InsertTable("historygeographys", record)) {
                showAlertDialog("保存成功", true);
            } else
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
        else {
            if(dbHelper.UpdateTable("historygeographys",record,"no="+editNo,null)){
                showAlertDialog("保存成功", true);
            }
            else
                Toast.makeText(this, "保存失败", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(String info, boolean isClose) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示"); // 设置标题
        builder.setMessage(info); // 设置提示信息
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 点击确定按钮后的回调
                Intent resultIntent = new Intent();
//                resultIntent.putExtra("location_id", edtTownshipid.getText().toString().trim());
//                resultIntent.putExtra("parent_id", idCounty);
                setResult(RESULT_OK, resultIntent);
                if (isClose)
                    finish();
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
        getMenuInflater().inflate(R.menu.new_history_geography, menu);

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
            case R.id.save_geography:
                onbtnSaveNewClick(null);
                //Toast.makeText(this, "add", Toast.LENGTH_SHORT).show();
                break;
        }

        return false;
    }
}