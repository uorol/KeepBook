package com.example.keepbook;

import java.util.Date;
import java.util.Locale;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;

public class ItemAdapter extends ArrayAdapter<Item> {
 
    // 畫面資源編號
    private int resource;
    // 包裝的記事資料
    private List<Item> items;
	private Context context;
 
    public ItemAdapter(Context context, int resource, List<Item> items) {
        super(context, resource, items);
        this.resource = resource;
        this.context = context;
        this.items = items;
    }
 
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout itemView;
        // 讀取目前位置的記事物件
        final Item item = getItem(position);
 
        if (convertView == null) {
            // 建立項目畫面元件
            itemView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater li = (LayoutInflater)
                    getContext().getSystemService(inflater);
            li.inflate(resource, itemView, true);
        }
        else {
            itemView = (LinearLayout) convertView;
        }
 
        // 讀取記事顏色、已選擇、標題與日期時間元件
        RelativeLayout typeColor = (RelativeLayout) itemView.findViewById(R.id.type_color);
        ImageView selectedItem = (ImageView) itemView.findViewById(R.id.selected_item);
        ImageView clearItem = (ImageView) itemView.findViewById(R.id.clear_item);
        TextView titleViewT = (TextView) itemView.findViewById(R.id.title_text_T);
        TextView titleView = (TextView) itemView.findViewById(R.id.title_text);
        TextView contentView = (TextView) itemView.findViewById(R.id.content_text);
        TextView dateView = (TextView) itemView.findViewById(R.id.date_text);
 
        // 設定記事顏色
        GradientDrawable background = (GradientDrawable)typeColor.getBackground();
        background.setColor(item.getColor().parseColor());
 
        // 設定標題與日期時間
        titleView.setText(item.getTitle());

        String Direct;
        if (item.getDirect()==0) {
        	Direct = context.getResources().getString(R.string.direct_borrow);
        } else {
        	Direct = context.getResources().getString(R.string.direct_rent);
        }
        titleViewT.setText(Direct.substring(0,1));
    	if (item.getColor() == Colors.BLUE) {
    		titleViewT.setTextColor(Color.BLACK);
    	} else {
    		titleViewT.setTextColor(Color.WHITE);
    	}

    	contentView.setText(item.getContent());
    	
        dateView.setText("Create on: " + item.getLocaleDatetime());
 
        // 設定是否已選擇
        selectedItem.setVisibility(item.isSelected() ? View.VISIBLE : View.INVISIBLE);
        clearItem.setVisibility(item.getClear()==1 ? View.VISIBLE : View.INVISIBLE);
 
        return itemView;
    }
 
    // 設定指定編號的記事資料
    public void set(int index, Item item) {
        if (index >= 0 && index < items.size()) {
            items.set(index, item);
            notifyDataSetChanged();
        }
    }
 
    // 讀取指定編號的記事資料
    public Item get(int index) {
        return items.get(index);
    }
 
}