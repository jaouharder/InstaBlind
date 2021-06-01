package com.A10.instablind.Fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.A10.instablind.Adapter.PostAdapter;
import com.A10.instablind.Model.Post;
import com.A10.instablind.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewPosts;
    private PostAdapter postAdapter;
    private List<Post> postList;

    private List<String> followingList;

    private TextToSpeech textToSpeech;
    private ArrayList<String> objectList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_home, container, false);

        recyclerViewPosts = view.findViewById(R.id.recycler_view_posts);
        recyclerViewPosts.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setReverseLayout(true);
        recyclerViewPosts.setLayoutManager(linearLayoutManager);
        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerViewPosts.setAdapter(postAdapter);

        followingList = new ArrayList<>();

        checkFollowingUsers();


//        textToSpeech = new TextToSpeech(getContext(), new TextToSpeech.OnInitListener() {
//            @Override
//            public void onInit(int status) {
//                if(status != TextToSpeech.ERROR) {
//                    textToSpeech.setLanguage(Locale.US);
//                }
//            }
//        });
//        objectList = new ArrayList<>();
        return view;
    }

//    private void postDialogue() {
//        for (Post post : postList) {
//            getContent(post.getPostid());
//            if (objectList==null) continue;
//            Log.println(Log.ERROR, "OBJECTS", String.valueOf(objectList));
//            Log.println(Log.ERROR, "USER", String.valueOf(post.getPublisher()));
//
//            textToSpeech.speak("post by " + post.getPublisher() + "contains the following", TextToSpeech.QUEUE_FLUSH, null, "toggle");
//
//            for (String object : objectList) {
//                textToSpeech.speak(object, TextToSpeech.QUEUE_FLUSH, null, "toggle");
//            }
//            objectList.clear();
//        }
//    }

//    public void getContent(String postId) {
//        FirebaseDatabase.getInstance().getReference().child("objects").child(postId).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                objectList= (ArrayList<String>)dataSnapshot.getValue();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//    }

    private void checkFollowingUsers() {

        FirebaseDatabase.getInstance().getReference().child("Follow").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child("following").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                followingList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    followingList.add(snapshot.getKey());
                }
                followingList.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
                readPosts();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void readPosts() {

        FirebaseDatabase.getInstance().getReference().child("Posts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);

                    for (String id : followingList) {
                        if (post.getPublisher().equals(id)){
                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                postDialogue();  //speak after 2000ms
//            }
//        }, 2000);
    }
}
