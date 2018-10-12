package com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.navigationdrawer.myneighbours.pojo.NammaApartmentsSendMessage;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.utilities.Constants.ALL_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_CHATS;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_CHATS_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PRIVATE_USERS_REFERENCE;

public class RetrievingNeighboursList {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private int count = 0;
    private String currentUserApartmentName;
    private String currentUserFlatNumber;
    private String currentUserUID;
    private DatabaseReference currentUserDataReference;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public RetrievingNeighboursList(Context mCtx) {
        NammaApartmentsGlobal nammaApartmentsGlobal = ((NammaApartmentsGlobal) mCtx.getApplicationContext());
        NammaApartmentUser currentNammaApartmentUser = nammaApartmentsGlobal.getNammaApartmentUser();
        currentUserApartmentName = currentNammaApartmentUser.getFlatDetails().getApartmentName();
        currentUserFlatNumber = currentNammaApartmentUser.getFlatDetails().getFlatNumber();
        currentUserUID = currentNammaApartmentUser.getUID();
        currentUserDataReference = nammaApartmentsGlobal.getUserDataReference();
    }

    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve all user's UID from firebase under (users -> all -> usersUID) expect the current user UID
     *
     * @param neighboursUIDListCallback - callback to return list of neighbours UID
     */
    private void getNeighboursUIDList(NeighboursUIDListCallback neighboursUIDListCallback) {
        ALL_USERS_REFERENCE.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<String> neighbourUidList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot neighbourUIDSnapshot : dataSnapshot.getChildren()) {
                        String neighbourUID = neighbourUIDSnapshot.getValue(String.class);
                        /*Add all users UID to the list current except user's UID*/
                        if (!Objects.requireNonNull(neighbourUID).equals(currentUserUID)) {
                            count++;
                            neighbourUidList.add(neighbourUID);
                        }
                    }

                    if (count == neighbourUidList.size()) {
                        neighboursUIDListCallback.onCallBack(neighbourUidList);
                    }

                } else {
                    neighboursUIDListCallback.onCallBack(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to get chat room uid for particular neighbour
     *
     * @param neighbourUID        - uid of neighbour
     * @param chatRoomUIDCallback - callback to return chat room uid
     */
    private void getChatRoomUID(String neighbourUID, ChatRoomUIDCallback chatRoomUIDCallback) {
        DatabaseReference chatRoomReference = currentUserDataReference.child(FIREBASE_CHILD_CHATS)
                .child(neighbourUID);
        chatRoomReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    chatRoomUIDCallback.onCallBack(dataSnapshot.getValue(String.class));
                } else {
                    chatRoomUIDCallback.onCallBack(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to retrieve a list of all message uid for a particular chat room
     *
     * @param chatRoomUID            - uid of chat room
     * @param messageUIDListCallback - callback to return list of all message uid
     */
    private void getMessageUIDList(String chatRoomUID, MessageUIDListCallback messageUIDListCallback) {
        PRIVATE_CHATS_REFERENCE.child(chatRoomUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    List<String> messagesList = new ArrayList<>();
                    for (DataSnapshot messageListReference : dataSnapshot.getChildren()) {
                        messagesList.add(messageListReference.getKey());
                    }

                    if (dataSnapshot.getChildrenCount() == messagesList.size()) {
                        messageUIDListCallback.onCallBack(messagesList);
                    }
                } else {
                    messageUIDListCallback.onCallBack(new ArrayList<>());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * This method is invoked to retrieve the details of the message
     *
     * @param chatRoomUID         - chat room uid
     * @param messageUID          - message uid of which details to be retrieve
     * @param messageDataCallback - callback to return details of a particular message
     */
    private void getMessageDetails(String chatRoomUID, String messageUID, MessageDataCallback messageDataCallback) {
        PRIVATE_CHATS_REFERENCE.child(chatRoomUID)
                .child(messageUID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                NammaApartmentsSendMessage nammaApartmentsSendMessage = dataSnapshot.getValue(NammaApartmentsSendMessage.class);
                messageDataCallback.onCallBack(nammaApartmentsSendMessage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /* ------------------------------------------------------------- *
     * Public Method
     * ------------------------------------------------------------- */

    /**
     * This method is invoked to retrieve the user's data from firebase under (users->private->userUID).
     *
     * @param neighboursDataListCallback - callback to return list of users data
     */
    public void getNeighbourDataList(NeighboursDataListCallback neighboursDataListCallback) {
        getNeighboursUIDList(neighboursUIDList -> {
            if (!neighboursUIDList.isEmpty()) {
                count = 0;
                List<NammaApartmentUser> neighboursDataList = new ArrayList<>();

                for (String neighbourUID : neighboursUIDList) {
                    count++;
                    /*retrieving data from (users->private->userUID) in firebase*/
                    DatabaseReference neighbourDataReference = PRIVATE_USERS_REFERENCE.child(neighbourUID);
                    neighbourDataReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                            /*Getting user apartment name and flat number value*/
                            String userApartmentName = Objects.requireNonNull(nammaApartmentUser).getFlatDetails().getApartmentName();
                            String userFlatNumber = nammaApartmentUser.getFlatDetails().getFlatNumber();
                            /*Adding only that users data in the list who is admin of a flat and also that user is not a flat member of current user*/
                            if (nammaApartmentUser.getPrivileges().isAdmin() &&
                                    !((userApartmentName.equals(currentUserApartmentName)) && userFlatNumber.equals(currentUserFlatNumber))) {
                                neighboursDataList.add(nammaApartmentUser);
                            }

                            if (count == neighboursUIDList.size())
                                neighboursDataListCallback.onCallBack(neighboursDataList);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            } else {
                neighboursDataListCallback.onCallBack(new ArrayList<>());
            }
        });
    }

    /**
     * This method is invoke to retrieve list all previous chat messages with that neighbour
     *
     * @param neighbourUID                     - uid of neighbour
     * @param previousMessagesDataListCallBack - callback to return a list of all previous messages with that neighbour
     */
    public void getPreviousMessagesDataList(String neighbourUID, PreviousMessagesDataListCallBack previousMessagesDataListCallBack) {
        getChatRoomUID(neighbourUID, chatRoomUID -> {
            if (chatRoomUID != null) {
                getMessageUIDList(chatRoomUID, messageUIDList -> {
                    if (!messageUIDList.isEmpty()) {
                        count = 0;
                        List<NammaApartmentsSendMessage> previousMessageDataList = new ArrayList<>();
                        for (String messageUID : messageUIDList) {
                            count++;
                            getMessageDetails(chatRoomUID, messageUID, nammaApartmentsSendMessage -> {
                                previousMessageDataList.add(nammaApartmentsSendMessage);
                                if (count == messageUIDList.size()) {
                                    previousMessagesDataListCallBack.onCallBack(previousMessageDataList);
                                }
                            });
                        }

                    } else {
                        previousMessagesDataListCallBack.onCallBack(new ArrayList<>());
                    }
                });
            } else {
                previousMessagesDataListCallBack.onCallBack(new ArrayList<>());
            }
        });
    }

    /* ------------------------------------------------------------- *
     * Interfaces
     * ------------------------------------------------------------- */

    public interface NeighboursUIDListCallback {
        void onCallBack(List<String> neighboursUIDList);
    }

    public interface NeighboursDataListCallback {
        void onCallBack(List<NammaApartmentUser> neighboursDataList);
    }

    public interface ChatRoomUIDCallback {
        void onCallBack(String chatRoomUID);
    }

    public interface PreviousMessagesDataListCallBack {
        void onCallBack(List<NammaApartmentsSendMessage> previousMessagesDataList);
    }

    public interface MessageUIDListCallback {
        void onCallBack(List<String> messageUIDList);
    }

    public interface MessageDataCallback {
        void onCallBack(NammaApartmentsSendMessage nammaApartmentsSendMessage);
    }
}
