package com.A10.instablind.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hendraanggrian.appcompat.widget.SocialAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

// Assume User model and so on are created...
public class SearchFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<User> mUsers; //TODO import/create User
    private UserAdapter userAdapter; //TODO import/create UserAdapter

    private RecyclerView recyclerViewTags;
    private List<String> hashTags;
    private List<String> hashTagsCount;
    private TagAdapter tagAdapter; //TODO import/create TagAdapter

    private SocialAutoCompleteTextView search_bar;

}
