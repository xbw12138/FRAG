package com.example.frag_model;

import java.util.List;

import com.example.frag2.R;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListWorkAdapter extends BaseAdapter {

	private List<ListWorkModel> mDate;
	private Context mContext;

	public ListWorkAdapter(Context mContext, List<ListWorkModel> mDate) {
		this.mContext = mContext;
		this.mDate = mDate;
	}

	@Override
	public int getCount() {
		return mDate.size();
	}

	@Override
	public Object getItem(int position) {
		return mDate.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = View.inflate(mContext, R.layout.list_work_adapter, null);
		ListWorkModel model = mDate.get(position);
		TextView txt_id = (TextView) view.findViewById(R.id.order_id);
		TextView txt_type = (TextView) view.findViewById(R.id.order_type);
		TextView txt_num = (TextView) view.findViewById(R.id.order_num);
		TextView txt_add = (TextView) view.findViewById(R.id.order_add);
		TextView txt_time = (TextView) view.findViewById(R.id.order_time);
		TextView txt_send = (TextView) view.findViewById(R.id.order_send);
		ImageView image = (ImageView) view.findViewById(R.id.imageView);
		image.setVisibility(View.GONE); 
		// ������
		txt_id.setText(model.getWorkID());
		txt_type.setText(model.getStart_time());
		txt_num.setText(model.getEnd_time());
		txt_add.setText(model.getWork_describe());
		txt_time.setText(model.getWork_position());
		txt_send.setText(model.getWorkSendTime());
		if(model.getFinish().equals("Y")){
			image.setVisibility(View.VISIBLE); 
		}
		// ����
		return view;
	}

}
