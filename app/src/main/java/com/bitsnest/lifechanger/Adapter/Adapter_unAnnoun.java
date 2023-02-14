package com.bitsnest.lifechanger.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bitsnest.lifechanger.LC.ActivityMain;
import com.bitsnest.lifechanger.Model.Model_unAnnoun;
import com.bitsnest.lifechanger.R;
import com.bitsnest.lifechanger.Utils;
import com.bitsnest.lifechanger.lottiedialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Adapter_unAnnoun extends RecyclerView.Adapter<Adapter_unAnnoun.ViewHolder> {
    private List<Model_unAnnoun> model_unAnnoun;
    Context context;
    private FirebaseFirestore firestore;
    private Utils utils;



    LayoutInflater inflater;
    private OnItemClickListener mListener;

    /////////////////////click listner for outside adopter
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }



    // RecyclerView recyclerView;
    public Adapter_unAnnoun(Context context, List<Model_unAnnoun> model_unAnnoun) {
        this.model_unAnnoun = model_unAnnoun;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        firestore= FirebaseFirestore.getInstance();
        utils=new Utils(context);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_un_notify, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model_unAnnoun current_unAnnoun = model_unAnnoun.get(position);
        holder.txt_date.setText(model_unAnnoun.get(position).getDate());
        holder.txt_name.setText(model_unAnnoun.get(position).getHeading());
        holder.txt_notifi.setText(model_unAnnoun.get(position).getData());

        holder.layout_unAnnoun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                final lottiedialog lottie=new lottiedialog(context);
                lottie.show();
                Map<String, Object> m = new HashMap<>();
                m.put("status", "read");
                firestore.collection("Announcement")
                        .document(model_unAnnoun.get(position).getId()).update(m)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    holder.layout_unAnnoun.setBackgroundColor(Color.TRANSPARENT);// setBackgroundColor(Color.parseColor("#9F000000"));
                                    lottie.dismiss();
                                    Toast.makeText(context, "Status Updated!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(context, ActivityMain.class);
                                    context.startActivity(intent);
                                }

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull  Exception e) {
                                lottie.dismiss();
                            }
                        });
               /* Intent intent = new Intent(view.getContext(), ActivityNotificationDetails.class);
                intent.putExtra("id", current_unAnnoun.getNotificatioID().toString().trim());
                view.getContext().startActivity(intent);*/
                //Toast.makeText(view.getContext(),"click on item: "+current_unAnnoun.getAmount().toString().trim(), Toast.LENGTH_LONG).show();
                //Toast.makeText(view.getContext(),"click on item: "+current_investor.get_name_Investor(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return model_unAnnoun.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        public TextView txt_date,txt_name,txt_notifi;
        public LinearLayout layout_unAnnoun;
        public ViewHolder(View itemView) {
            super(itemView);
            this.txt_date = (TextView) itemView.findViewById(R.id.txt_date);
            this.txt_name = (TextView) itemView.findViewById(R.id.txt_header_notify);
            this.txt_notifi = (TextView) itemView.findViewById(R.id.txt_notifi);
            layout_unAnnoun = (LinearLayout)itemView.findViewById(R.id.layout_Notify);
        }

    }
}

