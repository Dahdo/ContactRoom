package com.dahdotech.contactroom.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dahdotech.contactroom.R;
import com.dahdotech.contactroom.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private List<Contact> contactList;
    private Context context;
    private OnContactClickListener contactClickListener;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener contactClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.contactClickListener = contactClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view, contactClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.name.setText(contact.getName());
        holder.occupation.setText(contact.getOccupation());
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView name;
        public TextView occupation;
        OnContactClickListener onContactClickListener;

        public ViewHolder(@NonNull View itemView, OnContactClickListener onContactClickListener) {
            super(itemView);
            name = itemView.findViewById(R.id.row_name_text_view);
            occupation = itemView.findViewById(R.id.row_occupation_text_view);
            this.onContactClickListener = onContactClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onContactClickListener.onContactClick(getAdapterPosition());
        }
    }

    public  interface OnContactClickListener {
        void onContactClick(int postion);
    }
}
