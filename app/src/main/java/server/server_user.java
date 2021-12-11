package server;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class server_user {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DatabaseReference GetReference(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
     return databaseReference;
    }

    public Query getQuery(String uid) {
        Query q = GetReference().orderByChild("uid").equalTo(uid);
        return q;
    }
}
