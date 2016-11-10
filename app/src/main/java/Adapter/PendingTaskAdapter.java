package Adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import Db.DatabaseHandler;
import Models.PendingPojo;
import workspace.mobile.android.task.R;

/**
 * Created by mraokorni on 11/6/2016.
 */

public class PendingTaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<PendingPojo> pojoList;
    Handler handler;
    Dialog cancel_dialog;

    public PendingTaskAdapter(List<PendingPojo> pendingList, Context context) {
        this.pojoList = pendingList;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder vholder = (MyViewHolder) holder;
        PendingPojo pojo = pojoList.get(position);

        vholder.taskNameTxt.setText(pojo.getName());
        vholder.row.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDeletePopup(position);
                return false;
            }
        });
        vholder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelPopup(position);
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {

                        deleteItem(position);
                        cancel_dialog.dismiss();
                    }
                }, 5000);
            }
        });


    }

    public void deleteItem(int position) {
        DatabaseHandler db = new DatabaseHandler(mContext);
        db.addDonePojo(pojoList.get(position));

        db.deletePendingPojo(pojoList.get(position));
        pojoList.remove(position);
        notifyDataSetChanged();
    }

    private void showCancelPopup(final int position) {
        cancel_dialog = new Dialog(mContext);
        cancel_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        cancel_dialog.setContentView(R.layout.cancel_popup);
        cancel_dialog.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;

        TextView cancelAction = (TextView) cancel_dialog.findViewById(R.id.cancel_action);
        cancelAction.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                // deleteItem(position);
                handler.removeMessages(0);
                cancel_dialog.dismiss();
            }
        });
        cancel_dialog.show();
    }

    @Override
    public int getItemCount() {
        return pojoList.size();
    }

    public void getUpdateList(List<PendingPojo> updatedList) {

        this.pojoList = updatedList;

        notifyDataSetChanged();
    }

    private class MyViewHolder extends RecyclerView.ViewHolder {
        TextView taskNameTxt;
        LinearLayout row;

        MyViewHolder(View view) {
            super(view);
            taskNameTxt = (TextView) view.findViewById(R.id.name_txt);
            row = (LinearLayout) view.findViewById(R.id.lay);
        }
    }

    private void showDeletePopup(final int position) {
        final Dialog m_dialog = new Dialog(mContext);
        m_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        m_dialog.setContentView(R.layout.areyousuredelepopup);
        m_dialog.getWindow().getAttributes().windowAnimations = R.style.popup_login_dialog_animation;

        TextView noTxt = (TextView) m_dialog.findViewById(R.id.noTxt);
        TextView yesTxt = (TextView) m_dialog.findViewById(R.id.yesTxt);
        noTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                m_dialog.dismiss();
            }
        });
        yesTxt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                DatabaseHandler db = new DatabaseHandler(mContext);
                db.deletePendingPojo(pojoList.get(position));
                m_dialog.dismiss();
                pojoList.remove(position);
                notifyDataSetChanged();
            }
        });
        m_dialog.show();
    }
}
