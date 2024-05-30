package com.history.geography;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class historyGeographyAdapter extends BaseAdapter {
    List<HistoryGeographys> mList;
    Context mContext;

    public historyGeographyAdapter(Context pContext, List<HistoryGeographys> pList) {
        this.mContext = pContext;
        this.mList = pList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 下面是重要代码
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater _LayoutInflater = LayoutInflater.from(mContext);
        convertView = _LayoutInflater.inflate(R.layout.listview_item, null);
        if (convertView != null) {
            TextView _TextView1 = (TextView) convertView.findViewById(R.id.txtShowProvince);
            TextView _TextView2 = (TextView) convertView.findViewById(R.id.txtMemo);
            TextView txtShowHistory = (TextView) convertView.findViewById(R.id.txtShowHistory);
            TextView txtid=(TextView)convertView.findViewById(R.id.txtid);
            TextView txtShowOldNamee=(TextView)convertView.findViewById(R.id.txtShowOldNamee);
            TextView txtLabOldName=(TextView)convertView.findViewById(R.id.txtLabOldName);
            HistoryGeographys geo = mList.get(position);
            if(null!=geo) {
                txtid.setText(String.valueOf(geo.getNo()));
                txtShowHistory.setText(geo.getHistory());
                String loc= (geo.getProvince_name()==null?"":geo.getProvince_name()) +
                        (geo.getMunicipality_name()==null?"":geo.getMunicipality_name())+
                        (geo.getCounty_name()==null?"":geo.getCounty_name()) +
                        (geo.getTownship_name()==null?"":geo.getTownship_name()) +
                        (geo.getVillage_name()==null?"":geo.getVillage_name());
                _TextView1.setText(loc);
                _TextView2.setText(geo.getMemo());
                txtShowOldNamee.setText(geo.getName_chs()==null?"":geo.getName_chs());
                if(0==geo.getNo())
                    txtLabOldName.setText("地名：");
                else
                    txtLabOldName.setText("旧称：");
                //txtName.setText(geo.getName_chs());
            }
        }
        return convertView;
    }

}
