package com.szubov.android_hw_121;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ItemsDataAdapter extends BaseAdapter {

    private List<ItemData> mItems;
    private LayoutInflater mInflater;

    ItemsDataAdapter(Context context, List<ItemData> items) {
        if (items == null) {
            this.mItems = new ArrayList<>();
        } else {
            this.mItems = items;
        }
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    void addItem(ItemData item) {
        this.mItems.add(item);
        notifyDataSetChanged();
    }

    void removeItem(int position) {
        mItems.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public ItemData getItem(int position) {
        if (position < mItems.size()) {
            return mItems.get(position);
        } else  {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null){
            view = mInflater.inflate(R.layout.item_list_view, parent,false);
        }

        ItemData itemData = mItems.get(position);

        ImageView image = view.findViewById(R.id.icon);
        TextView title = view.findViewById(R.id.title);
        TextView subtitle = view.findViewById(R.id.subtitle);
        ImageView imageBtnDelete = view.findViewById(R.id.imageBtnDelete);

        imageBtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Log.d("ItemsDataAdapter", "ItemsDataAdapter -> imageDelete -> OnClick");
            removeItem(position);
            }
        });

        image.setImageDrawable(itemData.getImage());
        title.setText(itemData.getTitle());
        subtitle.setText(itemData.getSubtitle());

        return view;
    }

}
