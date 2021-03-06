package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * This adapter will map the data retrieved from the DB to a list which will be diplayed
 * to the user
 */
public class RecylerViewAdapter extends RecyclerView.Adapter<RecylerViewAdapter.ViewHolder> {

    // list of transactionDetails of the current month
    // data will be save int the currentMonthTransaction
    private List<TransactionDetails> currentMonthTransactions;
    private Context context;

    // set a listener on the items of the lists to open the
    public void setOnItemClickListener(RecylerViewAdapter.onItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private onItemClickListener onItemClickListener;

    public RecylerViewAdapter(List<TransactionDetails> currentMonthTransactions, Context context) {
        this.currentMonthTransactions = currentMonthTransactions;
        this.context = context;
    }


    @NonNull
    @Override
    // inflate the generated list into the viewHolder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_items, parent, false);
        return new ViewHolder(view);
    }

    // bind the data to the viewHolder select each field from the interface
    // and writing its value
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransactionDetails transactionDetails = currentMonthTransactions.get(position);
        holder.intDay.setText(String.valueOf(transactionDetails.getDay()));
        holder.stringDay.setText(transactionDetails.getStringDay());
        holder.category.setText(transactionDetails.getCategory());
        holder.amount.setText(String.format("%.1f", transactionDetails.getAmount()));
        if (transactionDetails.getAmount() > 0)
            holder.amount.setTextColor(Color.GREEN);
        holder.monthYear.setText(transactionDetails.getMonth() + " " + transactionDetails.getYear());
        holder.categoryPhoto.setImageResource(transactionDetails.getCategoryPath());
        holder.transactionId.setText(transactionDetails.getTransactionId());


    }

    @Override
    public int getItemCount() {
        return currentMonthTransactions.size();
    }

    /**
     * ViewHolder class gets the reference to all the components
     * which belong to the view of an item list
     */

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView intDay;
        TextView stringDay;
        TextView monthYear;
        TextView amount;
        TextView transactionId;
        TextView category;
        ImageView categoryPhoto;
        ConstraintLayout relativeLayout;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // get the references
            category = itemView.findViewById(R.id.category);
            intDay = itemView.findViewById(R.id.transactionIntDay);
            stringDay = itemView.findViewById(R.id.transactionStringDay);
            amount = itemView.findViewById(R.id.transactionAmount);
            monthYear = itemView.findViewById(R.id.monthYear);
            relativeLayout = itemView.findViewById(R.id.relative_layout);
            categoryPhoto = itemView.findViewById(R.id.catergoryPhoto);
            transactionId = itemView.findViewById(R.id.transcation_id);
            transactionId.setVisibility(View.INVISIBLE);

            // setting a click listener to each items will bring
            // the user to an edit and delete transaction page
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (onItemClickListener != null) {

                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            onItemClickListener.onItemClick(position);
                        }
                    }

                }
            });
        }
    }

    public interface onItemClickListener {
        void onItemClick(int position);

    }

}
