package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.pojo.NammaApartmentsSendMessage;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Shivam Lohani on 10/10/2018
 */
public class SendMessageAdapter extends RecyclerView.Adapter {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final List<NammaApartmentsSendMessage> previousMessagesDataList;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public SendMessageAdapter(Context mCtx, List<NammaApartmentsSendMessage> previousMessagesDataList) {
        this.mCtx = mCtx;
        this.previousMessagesDataList = previousMessagesDataList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    /*Determines the appropriate ViewType according to the sender of the message.*/
    @Override
    public int getItemViewType(int position) {
        NammaApartmentsSendMessage nammaApartmentsSendMessage = previousMessagesDataList.get(position);
        String receiverUID = nammaApartmentsSendMessage.getReceiverUID();

        if (receiverUID.equals(NammaApartmentsGlobal.userUID)) {
            /*If neighbour sent message to the user*/
            return VIEW_TYPE_MESSAGE_RECEIVED;
        } else {
            /*If user sent message to the neighbour*/
            return VIEW_TYPE_MESSAGE_SENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        /*Inflates the appropriate layout according to the ViewType*/
        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = inflater.inflate(R.layout.layout_sent_message, parent, false);
            return new SentMessageHolder(view);
        } else if (viewType == VIEW_TYPE_MESSAGE_RECEIVED) {
            view = inflater.inflate(R.layout.layout_received_message, parent, false);
            return new ReceivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        /*Creating an instance of NammaApartmentsSendMessage class and retrieving the values from Firebase*/
        NammaApartmentsSendMessage nammaApartmentsSendMessage = previousMessagesDataList.get(position);
        String message = nammaApartmentsSendMessage.getMessage();
        long timestamp = nammaApartmentsSendMessage.getTimeStamp();
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(timestamp);
        String time = DateFormat.format("hh:mm a", calendar).toString();

        /*Passes the message and message time to a ViewHolder so that the contents can be bound to UI.*/
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message, time);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message, time);
        }
    }

    @Override
    public int getItemCount() {
        return previousMessagesDataList.size();
    }

    /* ------------------------------------------------------------- *
     * Sent Message View Holder class
     * ------------------------------------------------------------- */

    private class SentMessageHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textSentMessage;
        private final TextView textSentMessageTime;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        SentMessageHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views on card view*/
            textSentMessage = itemView.findViewById(R.id.textSentMessage);
            textSentMessageTime = itemView.findViewById(R.id.textSentMessageTime);

            /*Setting Fonts for all the views on card view*/
            textSentMessage.setTypeface(setLatoRegularFont(mCtx));
            textSentMessageTime.setTypeface(setLatoRegularFont(mCtx));
        }

        /* ------------------------------------------------------------- *
         * Bind method
         * ------------------------------------------------------------- */

        void bind(final String message, final String time) {
            /*Setting Text to the view*/
            textSentMessage.setText(message);
            textSentMessageTime.setText(time);
        }
    }

    /* ------------------------------------------------------------- *
     * Received Message View Holder class
     * ------------------------------------------------------------- */

    private class ReceivedMessageHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textReceivedMessage;
        private final TextView textReceivedMessageTime;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            /*Getting Id's for all the views on card view*/
            textReceivedMessage = itemView.findViewById(R.id.textReceivedMessage);
            textReceivedMessageTime = itemView.findViewById(R.id.textReceivedMessageTime);

            /*Setting Fonts for all the views on card view*/
            textReceivedMessage.setTypeface(setLatoRegularFont(mCtx));
            textReceivedMessageTime.setTypeface(setLatoRegularFont(mCtx));
        }

        /* ------------------------------------------------------------- *
         * Bind method
         * ------------------------------------------------------------- */

        void bind(final String message, final String time) {
            /*Setting Text to the view*/
            textReceivedMessage.setText(message);
            textReceivedMessageTime.setText(time);
        }
    }
}
