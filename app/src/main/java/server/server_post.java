package server;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class server_post {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public DatabaseReference GetReference(){
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Posts");
     return databaseReference;
    }

    public Query getQuery(String pid) {
        Query q = GetReference().orderByChild("pId").equalTo(pid);
        return q;
    }
}
