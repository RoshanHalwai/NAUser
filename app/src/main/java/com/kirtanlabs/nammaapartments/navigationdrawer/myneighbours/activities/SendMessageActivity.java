package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.activities;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.RetrievingNeighboursList;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.adapters.SendMessageAdapter;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.pojo.NammaApartmentsSendMessage;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CHATS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NEIGHBOUR_APARTMENT_NAME;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NEIGHBOUR_FLAT_NUMBER;
import static com.kirtanlabs.nammaapartments.utilities.Constants.NEIGHBOUR_UID;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_CHATS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USER_DATA_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Shivam Lohani on 10/9/2018
 */
public class SendMessageActivity extends BaseActivity implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private EditText editMessage;
    private DatabaseReference currentUserDataReference;
    private String neighbourUID;
    private String neighbourApartmentName;
    private String neighbourFlatNumber;
    private String chatRoomUID;
    private String currentUserUID;
    private String chatMembersCity;
    private String chatMembersSociety;

    /* ------------------------------------------------------------- *
     * Overriding BaseActivity Objects
     * ------------------------------------------------------------- */

    @Override
    protected int getLayoutResourceId() {
        return R.layout.activity_send_message;
    }

    @Override
    protected int getActivityTitle() {
        return R.string.send_message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*Getting Id's for all the views*/
        RecyclerView recyclerViewChatMessages = findViewById(R.id.recyclerViewChatMessages);
        editMessage = findViewById(R.id.editMessage);
        ImageView imageSendMessage = findViewById(R.id.imageSendMessage);

        /*Setting Fonts for all the views*/
        editMessage.setTypeface(setLatoRegularFont(this));

        /*Retrieving details of both member of a particular chat room*/
        getChatRoomMembersDetails();

        recyclerViewChatMessages.setHasFixedSize(true);
        recyclerViewChatMessages.setLayoutManager(new LinearLayoutManager(this));

        /*Retrieving Previous chat messages if any*/
        new RetrievingNeighboursList(SendMessageActivity.this)
                .getPreviousMessagesDataList(neighbourUID, previousMessagesDataList -> {
                    if (!previousMessagesDataList.isEmpty()) {
                        recyclerViewChatMessages.setAdapter(new SendMessageAdapter(SendMessageActivity.this, previousMessagesDataList));
                        recyclerViewChatMessages.scrollToPosition((previousMessagesDataList.size() - 1));
                    }
                });

        /*Setting OnClick Listeners to the views*/
        imageSendMessage.setOnClickListener(this);

    }

    /* ------------------------------------------------------------- *
     * Overriding on Click method
     * ------------------------------------------------------------- */

    @Override
    public void onClick(View v) {
        String message = editMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            storeMessageInFirebase(message);
            editMessage.getText().clear();
        }
    }

    /* ------------------------------------------------------------- *
     * Private methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to store the message in the firebase that is send by the user to neighbour
     *
     * @param message - that need to be stored
     */
    private void storeMessageInFirebase(final String message) {
        /*Checking if chat room is already created for neighbour with user or not in (usersData -> user's Flat number ->chats->neighbourUID)*/
        DatabaseReference currentUserChatReference = currentUserDataReference.child(FIREBASE_CHILD_CHATS)
                .child(neighbourUID);
        currentUserChatReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                /*Checking if is there any chat room with this neighbour or not*/
                if (dataSnapshot.exists()) {
                    chatRoomUID = dataSnapshot.getValue(String.class);
                } else {
                    chatRoomUID = currentUserChatReference.push().getKey();
                    /*Setting chat room uid in firebase under (userData -> user's flat number -> chats -> neighbour's uid)*/
                    currentUserChatReference.setValue(chatRoomUID);

                    /*Mapping usersUID with chat room uid in firebase under neighbour's (flat number-> chats) */
                    DatabaseReference neighbourChatRoomReference = PRIVATE_USER_DATA_REFERENCE
                            .child(chatMembersCity).child(chatMembersSociety).child(neighbourApartmentName)
                            .child(neighbourFlatNumber).child(FIREBASE_CHILD_CHATS).child(currentUserUID);
                    neighbourChatRoomReference.setValue(chatRoomUID);
                }

                /*Storing message details in firebase under (chats->private->chatRoomUID)*/
                DatabaseReference chatRoomReference = PRIVATE_CHATS_REFERENCE.child(Objects.requireNonNull(chatRoomUID));
                String messageUID = chatRoomReference.push().getKey();
                long timeStamp = System.currentTimeMillis();
                NammaApartmentsSendMessage nammaApartmentsSendMessage = new NammaApartmentsSendMessage(message, neighbourUID, timeStamp);
                chatRoomReference.child(messageUID).setValue(nammaApartmentsSendMessage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to get all the details of the user and neighbour to whom user want to chat with
     */
    private void getChatRoomMembersDetails() {
        /*Getting Current User Details*/
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) getApplicationContext());
        NammaApartmentUser currentNammaApartmentUser = nammaApartmentsGlobal.getNammaApartmentUser();
        currentUserUID = currentNammaApartmentUser.getUID();
        chatMembersCity = currentNammaApartmentUser.getFlatDetails().getCity();
        chatMembersSociety = currentNammaApartmentUser.getFlatDetails().getSocietyName();
        /*Getting current user's userData reference*/
        currentUserDataReference = nammaApartmentsGlobal.getUserDataReference();

        /*Getting Neighbour Details*/
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            neighbourUID = bundle.getString(NEIGHBOUR_UID);
            neighbourApartmentName = bundle.getString(NEIGHBOUR_APARTMENT_NAME);
            neighbourFlatNumber = bundle.getString(NEIGHBOUR_FLAT_NUMBER);
        }
    }
}
