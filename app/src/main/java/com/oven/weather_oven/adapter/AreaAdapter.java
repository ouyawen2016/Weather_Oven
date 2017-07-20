package com.oven.weather_oven.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.oven.weather_oven.R;
import java.util.List;


/**AreaAdapter 地区列表的适应器
 *
 * Created by oven on 2017/7/18.
 */

public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder>{
    private List<String> mAreaList;
    private AreaAdapter.OnItemClickListener onItemClickListener;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView areaName;
        public ViewHolder(View view){
            super(view);
            areaName = (TextView)view.findViewById(R.id.area_item);
        }
    }
    public AreaAdapter(List<String> areaList){
        mAreaList = areaList;
        notifyDataSetChanged();
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.choosearea_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        //绑定数据
        holder.areaName.setText(mAreaList.get(position));

        holder.areaName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onItemClickListener != null){
                    onItemClickListener.onItemClick(holder.itemView,holder.getLayoutPosition());
                }
            }
        }) ;
    }

    /**利用接口回调监听对应子项的点击事件
     *
     * @param listener
     * 传入监听器
     */
    public void setOnItemClickListener(AreaAdapter.OnItemClickListener listener){
        this.onItemClickListener = listener;
    }
    @Override
    public int getItemCount() {

        return mAreaList == null ? 0 : mAreaList.size();
    }


    public  interface OnItemClickListener{
        void onItemClick(View view,int position);
    }
}
