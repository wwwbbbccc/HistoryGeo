package com.history.geography;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class TownshipVillageActivity extends AppCompatActivity {

    DBHelper dbHelper;
    ArrayList<Municipalitys> municipalitys;
    ArrayList<Townships> townships;
    ArrayList<Villages> villags;
    ActivityResultLauncher launcherTownship;
    ActivityResultLauncher launcherVillage;
    Spinner spinTownship;
    Spinner spinVillage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_township_village);
        initActivityResultForTownship();
        initActivityResultForVillage();
        dbHelper = new DBHelper(getApplicationContext());
        if (!dbHelper.checkDataBase())
            dbHelper.createDatabase();
        else
            dbHelper.openDataBase();
        Spinner spinProvince = findViewById(R.id.spinner_Province);//来获取spin对象
        Spinner spinCountys = findViewById(R.id.spinner_Countys);
        Spinner spinMunicipalitys = findViewById(R.id.spinner_Municipalitys);
        spinTownship = findViewById(R.id.spinner_Township);
        spinVillage = findViewById(R.id.spinner_Villiages);
        ArrayList<Provinces> provinces = dbHelper.getAllProvinces();

        //  建立Adapter绑定数据源
        provinceAdapter _MyAdapter = new provinceAdapter(this, provinces);
//绑定Adapter
        spinProvince.setAdapter(_MyAdapter);
        spinTownship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Townships obj = townships.get(i);
                SetVillagSpinner(obj.getLocation_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spinProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinVillage.setAdapter(null);
                Provinces obj = provinces.get(i);
                municipalitys = dbHelper.getMunicipalitysByProvince(obj.getLocation_id());
                if (municipalitys.isEmpty()) {
                    spinMunicipalitys.setAdapter(null);
                    ArrayList<Countys> countys = dbHelper.getCountysByMunicipality(obj.getLocation_id(), 5);
                    countysAdapter countyAdapter = new countysAdapter(TownshipVillageActivity.this, countys);
                    spinCountys.setAdapter(countyAdapter);
                    spinCountys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            spinVillage.setAdapter(null);
                            Countys obj = countys.get(i);

                            SetTownshipSpinner(obj.getLocation_id());
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                } else {
                    municipalitysAdapter municipalityAdapter = new municipalitysAdapter(TownshipVillageActivity.this, municipalitys);
                    spinMunicipalitys.setAdapter(municipalityAdapter);
                    spinMunicipalitys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            spinVillage.setAdapter(null);
                            Municipalitys obj = municipalitys.get(i);
                            ArrayList<Countys> countys = dbHelper.getCountysByMunicipality(obj.getLocation_id(), 7);
                            countysAdapter countyAdapter = new countysAdapter(TownshipVillageActivity.this, countys);
                            spinCountys.setAdapter(countyAdapter);
                            spinCountys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
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
        townshipsAdapter adapter = new townshipsAdapter(TownshipVillageActivity.this, townships);
        spinTownship.setAdapter(adapter);

    }

    private void SetVillagSpinner(String id) {

        villags = dbHelper.getVillagsByTownship(id, 11);
        villagesAdapter adapter = new villagesAdapter(TownshipVillageActivity.this, villags);
        spinVillage.setAdapter(adapter);
    }

    public void initActivityResultForTownship() {

        launcherTownship = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    String id = intent.getStringExtra("location_id");
                    String parentId = intent.getStringExtra("parent_id");
                    SetTownshipSpinner(parentId);
                    int index = 0;
                    for (Townships t : townships) {
                        if (id.equals(t.getLocation_id()))
                            break;
                        index++;
                    }
                    if (townships.size() > index)
                        spinTownship.setSelection(index);
                }
            }
        });
    }

    public void initActivityResultForVillage() {

        launcherVillage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    String id = intent.getStringExtra("location_id");
                    String parentId = intent.getStringExtra("parent_id");
                    SetVillagSpinner(parentId);
                    int index = 0;
                    for (Villages v : villags) {
                        if (id.equals(v.getLocation_id()))
                            break;
                        index++;
                    }
                    if (villags.size() > index)
                        spinVillage.setSelection(index);
                }
            }
        });
    }

    public void onBtnTownAddClick(View view) {
        Intent i = new Intent(TownshipVillageActivity.this, NewTownshipActivity.class);
        Bundle bundle = new Bundle();
        Spinner spinCounty = findViewById(R.id.spinner_Countys);
        Spinner spinMunicipality = findViewById(R.id.spinner_Municipalitys);
        Spinner spinProvince = findViewById(R.id.spinner_Province);
        Provinces objProvince = (Provinces) spinProvince.getSelectedItem();
        Municipalitys objMunicipality = (Municipalitys) spinMunicipality.getSelectedItem();
        Countys objCounty = (Countys) spinCounty.getSelectedItem();
        bundle.putString("province_Location_id", objProvince.getLocation_id());
        bundle.putString("province_Name_chs", objProvince.getName_chs());
        bundle.putString("pp_name", objCounty.getPp_name());
        if (null != objMunicipality) {
            bundle.putString("municipality_Location_id", objMunicipality.getLocation_id());
            bundle.putString("municipality_Name_chs", objMunicipality.getName_chs());
        }
        bundle.putString("county_Location_id", objCounty.getLocation_id());
        bundle.putString("county_Name_chs", objCounty.getName_chs());
        i.putExtra("countys", bundle);
        launcherTownship.launch(i);
    }

    public void onBtnVillageAddClick(View view) {
        Spinner spinTownship = findViewById(R.id.spinner_Township);
        if (spinTownship.getSelectedItem() == null)
            return;
        Intent i = new Intent(TownshipVillageActivity.this, NewVillageActivity.class);
        Bundle bundle = new Bundle();
        Spinner spinCounty = findViewById(R.id.spinner_Countys);
        Spinner spinMunicipality = findViewById(R.id.spinner_Municipalitys);
        Spinner spinProvince = findViewById(R.id.spinner_Province);
        Provinces objProvince = (Provinces) spinProvince.getSelectedItem();
        Municipalitys objMunicipality = (Municipalitys) spinMunicipality.getSelectedItem();
        Townships objTownship = (Townships) spinTownship.getSelectedItem();
        Countys objCounty = (Countys) spinCounty.getSelectedItem();
        bundle.putString("province_Location_id", objProvince.getLocation_id());
        bundle.putString("province_Name_chs", objProvince.getName_chs());
        bundle.putString("pp_name", objCounty.getPp_name());
        bundle.putString("township_Location_id", objTownship.getLocation_id());
        bundle.putString("township_Location_name", objTownship.getName_chs());
        if (null != objMunicipality) {
            bundle.putString("municipality_Location_id", objMunicipality.getLocation_id());
            bundle.putString("municipality_Name_chs", objMunicipality.getName_chs());
        }
        bundle.putString("county_Location_id", objCounty.getLocation_id());
        bundle.putString("county_Name_chs", objCounty.getName_chs());
        i.putExtra("countys", bundle);
        launcherVillage.launch(i);
    }
    private boolean delTownship(){
        Townships town=(Townships) spinTownship.getSelectedItem();
        if(null==town){
            Toast.makeText(this, "没有找到当前乡镇", Toast.LENGTH_SHORT).show();
            return false;
        }
        String townId = town.getLocation_id();
        String parentId=town.getParent_id();
        ArrayList<Villages> existVillages = dbHelper.getVillagsByTownship(townId,11);
        if(null!=existVillages && 0!=existVillages.size() && existVillages.get(0)!=null && !TextUtils.isEmpty( existVillages.get(0).getLocation_id())){
            Toast.makeText(this, town.getName_chs()+"下还有村，不能删除", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!dbHelper.deleteTable("townships","location_id=?",new String[]{town.getLocation_id()})){
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        SetTownshipSpinner(parentId);
        townId=townships.get(0).getLocation_id();
        SetVillagSpinner(townId);
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        return true;
    }
    private boolean delVillage(){
        Villages village=(Villages) spinVillage.getSelectedItem();
        if(null==village){
            Toast.makeText(this, "没有找到当前村", Toast.LENGTH_SHORT).show();
            return false;
        }
        String villageId = village.getLocation_id();
        String parentId=village.getParent_id();
        if(!dbHelper.deleteTable("villages","location_id=?",new String[]{village.getLocation_id()})){
            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        SetVillagSpinner(parentId);
        Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
        return true;
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
        getMenuInflater().inflate(R.menu.township_village_menu, menu);

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
            case R.id.add_Township:
                onBtnTownAddClick(null);
                break;
            case R.id.add_Village:
                onBtnVillageAddClick(null);
                break;
            case R.id.del_Township:
                delTownship();
                break;
            case R.id.del_Village:
                delVillage();
                break;
        }

        return false;
    }

}