package com.example.snowex01;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


public class FrndIdSearchActivity extends AppCompatActivity {

    FirebaseAuth mFirebaseAuth;
    DatabaseReference myRef;
    private String muid = null;

    private String searchedUid;
    private String searchEmail;
    int frndCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_frnd);

        mFirebaseAuth = FirebaseAuth.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("FirebaseEmailAccount");
        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
        muid = firebaseUser.getUid();

        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 버튼이 눌렸을 때의 동작
                searchEmail = query;
                performSearch(query);
                return true;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어가 변경될 때의 동작 (실시간으로 검색 결과를 업데이트하는 경우에 사용)
                // newText를 사용하여 필요한 검색 로직을 구현
                return true;
            }
        });

        Button addFrndBtn = (Button) findViewById(R.id.add_frnd);
        addFrndBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addFriend(muid, searchedUid);
            }
        });

    }

    private void performSearch(String query) {
        DatabaseReference usersRef = myRef.child("userAccount");
        Query searchQuery = usersRef.orderByChild("email").equalTo(query);
        searchQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        searchedUid = userSnapshot.getKey(); // 사용자의 UID 가져오기
                    }
                } else {
                    searchedUid = null;
                    System.out.println("No results found for query: " + query);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Search error: " + databaseError.getMessage());
            }
        });
    }

    private void addFriend(String uid, String friendId) {
        if (friendId == null) {
            System.out.println("No friend ID found.");
            return;
        }

        DatabaseReference userFriendRef = myRef.child("userFriend").child(uid);
        userFriendRef.child(friendId).setValue(true)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        userFriendRef.child(friendId).child("frndEmail").setValue(searchEmail);
                        //친구의 위도 경도는 밑에 저장.
                        getLngLat(friendId);
                        frndCount++;

                        AlertDialog.Builder builder = new AlertDialog.Builder(FrndIdSearchActivity.this);
                        builder.setTitle(friendId).setMessage("친구 추가 성공");
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                });
    }

    private void getLngLat(String query) {
        DatabaseReference userFdRef = myRef.child("userPosition");
        Query getQuery = userFdRef.orderByKey().equalTo(query);
        getQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        Double frndLat = userSnapshot.child("lat").getValue(Double.class);
                        Double frndLng = userSnapshot.child("lng").getValue(Double.class);
                        System.out.println(frndLat + " " + frndLng);

//                        if(frndLat != null && frndLng != null) {
                            DatabaseReference userFriendRef = myRef.child("userFriend").child(muid).child(query);
                            userFriendRef.child("bool").setValue(false);
//                            userFriendRef.child("lat").setValue(frndLat);
//                            userFriendRef.child("lng").setValue(frndLng);
//                        }
                    }
                } else {
                    System.out.println("No results found for query: " + query);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Search error: " + databaseError.getMessage());
            }
        });
    }

}