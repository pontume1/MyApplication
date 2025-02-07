package com.luncher.bounjour.ringlerr.adapter;

/**
 * Created by santanu on 21/1/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luncher.bounjour.ringlerr.activity.ReminderDialog;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class ContactSelectSchedulerAdapter extends RecyclerView.Adapter<ContactSelectSchedulerAdapter.ViewHolder> {
    private List<String> values;
    private List<String> phone_no;
    private String sel_message;
    private Long sel_time;
    private Integer sel_timeAgo;
    public Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView txtHeader;
        public TextView txtFooter;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            txtHeader = (TextView) v.findViewById(com.luncher.bounjour.ringlerr.R.id.firstLine);
            txtFooter = (TextView) v.findViewById(com.luncher.bounjour.ringlerr.R.id.secondLine);
        }
    }

    public void add(int position, String item) {
        values.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        values.remove(position);
        notifyItemRemoved(position);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ContactSelectSchedulerAdapter(List<String> myDataset, List<String> phoneNo, String message, Long time, Integer timeAgo) {
        values = myDataset;
        phone_no = phoneNo;
        sel_message = message;
        sel_time = time;
        sel_timeAgo = timeAgo;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ContactSelectSchedulerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {


        context = parent.getContext();
        // create a new view
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(com.luncher.bounjour.ringlerr.R.layout.select_contact, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final String name = values.get(position);
        final String phone = phone_no.get(position);
        holder.txtHeader.setText(name);
        holder.layout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                            final Intent intent1 = new Intent(context, ReminderDialog.class);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                            intent1.putExtra("phone_no", phone);
                            intent1.putExtra("sim", 1);
                            intent1.putExtra("name", name);
                            intent1.putExtra("message", sel_message);
                            intent1.putExtra("timestamp", sel_time);
                            intent1.putExtra("timeAgo", sel_timeAgo);
                            //context.startActivity(intent1);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    context.startActivity(intent1);
                                }
                            }, 100);

            }
        });

        holder.txtFooter.setText(phone);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return values.size();
    }

}
