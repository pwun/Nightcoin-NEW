package de.nightcoin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CoinListViewAdapter extends BaseAdapter{

	Context listContext;
	LayoutInflater inflater;
	ArrayList<CoinObject> list;
    boolean userModeActive;
	
	public CoinListViewAdapter(Context context, ArrayList<CoinObject> objectList, boolean userModeActice){
		super();
		listContext = context;
		inflater = LayoutInflater.from(listContext);
		list = new ArrayList<CoinObject>();
		list.addAll(objectList);
        this.userModeActive = userModeActice;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}
	
	public class ViewHolder {
		TextView value;
		TextView location;
		TextView date;
        TextView amount;
		ImageView image;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.coin_list_view_adapter, null);
			holder.image = (ImageView) convertView.findViewById(R.id.imageViewCoinListAdapterImage);
			holder.value = (TextView) convertView.findViewById(R.id.textViewCoinListViewAdapterValue);
			holder.location = (TextView) convertView.findViewById(R.id.textViewCoinListViewAdapterLocation);
			holder.date = (TextView) convertView.findViewById(R.id.textViewCoinListViewAdapterDate);
            holder.amount = (TextView) convertView.findViewById(R.id.textViewCoinListViewAdapterAmount);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.value.setText(list.get(position).getValue());

		holder.location.setText(list.get(position).getLocation());
		holder.date.setText(list.get(position).getDate());

        System.out.println("Amount= "+list.get(position).getAmount());
        System.out.println("CashedIn= "+list.get(position).getCashedIn());

            if (list.get(position).isLimited()) {
                    holder.amount.setText("noch " + (list.get(position).getAmount() - list.get(position).getCashedIn()) + " Stück verfügbar");

            } else {
                holder.amount.setText("Unbegrenzt");
            }


		holder.image.setImageResource(R.drawable.black_coin);
		
		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

                if (userModeActive) {
                    Intent intent = new Intent(listContext, ActivityEditCoin.class);

                    intent.putExtra("id", list.get(position).getId());
                    intent.putExtra("isNewCoin", false);
                    listContext.startActivity(intent);
                } else {
                    Intent intent = new Intent(listContext, ActivityCoinItemView.class);

                    intent.putExtra("id", list.get(position).getId());
                    listContext.startActivity(intent);
                }
			}
		});
		
		return convertView;
	}
	
	
	
}
