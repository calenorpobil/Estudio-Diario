package com.merlita.estudiodiario;

import android.animation.ValueAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<String> items;
    private int selectedPosition = -1;
    private OnButtonClickListener listener;

    public interface OnButtonClickListener {
        void onEditClick(int position);
        void onDeleteClick(int position);
        void onShareClick(int position);
    }

    public CustomAdapter(ArrayList<String> items, OnButtonClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.text_row_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvTitle.setText(items.get(position));

        // Animación de expansión/contracción
        if (position == selectedPosition) {
            expandButtonsRow(holder.buttonsRow);
        } else {
            collapseButtonsRow(holder.buttonsRow);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout buttonsRow;
        Button btnEdit, btnDelete, btnShare;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitulo);
            /*buttonsRow = itemView.findViewById(R.id.filaBotones);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnShare = itemView.findViewById(R.id.btnShare);*/

            itemView.setOnClickListener(v -> {
                int previous = selectedPosition;
                selectedPosition = getAdapterPosition();

                if (previous == selectedPosition) {
                    selectedPosition = -1;
                    notifyItemChanged(previous);
                } else {
                    notifyItemChanged(previous);
                    notifyItemChanged(selectedPosition);
                }
            });

            btnEdit.setOnClickListener(v -> {
                if (listener != null && selectedPosition != -1) {
                    listener.onEditClick(selectedPosition);
                }
            });

            btnDelete.setOnClickListener(v -> {
                if (listener != null && selectedPosition != -1) {
                    listener.onDeleteClick(selectedPosition);
                }
            });

            btnShare.setOnClickListener(v -> {
                if (listener != null && selectedPosition != -1) {
                    listener.onShareClick(selectedPosition);
                }
            });
        }
    }

    // Animación de expansión
    private void expandButtonsRow(View view) {
        view.setVisibility(View.VISIBLE);
        ValueAnimator anim = ValueAnimator.ofInt(0, view.getMeasuredHeight());
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            view.getLayoutParams().height = val;
            view.requestLayout();
        });
        anim.setDuration(300);
        anim.start();
    }

    // Animación de contracción
    private void collapseButtonsRow(View view) {
        ValueAnimator anim = ValueAnimator.ofInt(view.getMeasuredHeight(), 0);
        anim.addUpdateListener(valueAnimator -> {
            int val = (Integer) valueAnimator.getAnimatedValue();
            if (val == 0) view.setVisibility(View.GONE);
            view.getLayoutParams().height = val;
            view.requestLayout();
        });
        anim.setDuration(300);
        anim.start();
    }
}