package com.wordpress.ledrunning.currencyrates;

/**
 * Created by Ledrunner on 13.02.2018.
 */

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ledrunner on 13.02.2018.
 */

public class CustomAdapter extends BaseAdapter {

    private Context ctx;
    private ArrayList<CurrencyRateModel> arrayList;
    private LayoutInflater inflater;

    public CustomAdapter(Context ctx, ArrayList<CurrencyRateModel> arrayList) {

        this.ctx = ctx;
        this.arrayList = arrayList;
        inflater = ((Activity)ctx).getLayoutInflater();
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //TextView tv = (TextView)convertView;

        // For debug
       /* if(convertView == null) {

            tv = new TextView(ctx);

        }
        tv.setText("позиция =" + position);*/
        ViewHolder vh;
        if(convertView == null) {

            vh = new ViewHolder();
            convertView = inflater.inflate(R.layout.activity_get_currency_rates, null);
            vh.tvTitle = (TextView)convertView.findViewById(R.id.title);
            vh.tvDescription = (TextView)convertView.findViewById(R.id.description);
            vh.tvConvertedData = (TextView)convertView.findViewById(R.id.converted);
            convertView.setTag(vh);
        }
        else {

            vh = (ViewHolder) convertView.getTag();
        }

        CurrencyRateModel item = arrayList.get(position);

        vh.tvTitle.setText(item.getName()+" - "+item.getCharCode());
        vh.tvDescription.setText("Курс сегодня: "+ String.format("%.2f Р", item.getValue())
                + " Предыдущий: " + String.format("%.2f Р",item.getPrevious()));
        vh.tvConvertedData.setText("Конвертированные данные: "
                + String.format("%.2f Р", item.getRate()));

        return convertView;
    }

    static class ViewHolder {

        TextView tvTitle, tvDescription, tvConvertedData;
    }
}