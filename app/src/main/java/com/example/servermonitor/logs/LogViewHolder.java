package com.example.servermonitor.logs;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.servermonitor.R;

public class LogViewHolder extends RecyclerView.ViewHolder {

    private final TextView textViewTimestamp;
    private final TextView textViewLevel;
    private final TextView textViewMessage;
    private final TextView textViewServerId;

    public LogViewHolder(@NonNull View itemView) {
        super(itemView);
        textViewTimestamp = itemView.findViewById(R.id.textViewTimestamp);
        textViewLevel = itemView.findViewById(R.id.textViewLevel);
        textViewMessage = itemView.findViewById(R.id.textViewMessage);
        textViewServerId = itemView.findViewById(R.id.textViewServerId);
    }

    public void bind(@NonNull LogInfo logInfo) {
        textViewTimestamp.setText(logInfo.getTimestamp());
        textViewLevel.setText(logInfo.getLevel());
        textViewMessage.setText(logInfo.getMessage());
        textViewServerId.setText(String.valueOf(logInfo.getServer_id()));
    }
}
