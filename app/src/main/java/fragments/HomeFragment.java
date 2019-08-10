package fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.employeeattendence.R;
import com.example.employeeattendence.util.Constants;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import model.Attendence;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    TextView t1,t2;
    Button b1;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ArrayList<Attendence> attendenceList = new ArrayList<>();
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference(Constants.ATTENDENCE);
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        t1 = view.findViewById(R.id.t1);
        t2 = view.findViewById(R.id.t2);
        b1 = view.findViewById(R.id.b1);
        t1.setText("Welcome "+pref.getString(Constants.NAME,""));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                markAttendence();
                b1.setEnabled(false);
                b1.setText("You are marked for the day");
            }
        });
        String year,mon,day,date;
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String currentDateandTime = sdf.format(new Date());
        t2.setText(currentDateandTime);

        checkAttend();
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            pref = context.getSharedPreferences("prefs",MODE_PRIVATE);
            editor = pref.edit();
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void markAttendence(){
        String year,mon,day,course,uid,time,name;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String[] arr = currentDateandTime.split("/");
        year = arr[0];
        mon = arr[1];
        day = arr[2];
        time = arr[3];
        course = pref.getString(Constants.COURSE,"Not found");
        name = pref.getString(Constants.NAME,"Not Found");
        uid = FirebaseAuth.getInstance().getUid();
        String date = year+mon+day;
        Attendence attendence = new Attendence(date,time,course,uid,name,date+"_"+uid);

        myRef.push().setValue(attendence);
        editor.putInt(Constants.TODAY_ATTEND,Integer.parseInt(year+mon+day));
        editor.commit();

    }
    private void checkAttend(){
        String year,mon,day,uid;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd/HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String[] arr = currentDateandTime.split("/");
        year = arr[0];
        mon = arr[1];
        day = arr[2];
        uid = FirebaseAuth.getInstance().getUid();
        String date = year+mon+day;
        Query query = myRef.orderByChild("date_userId").equalTo(date+"_"+uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG,dataSnapshot.toString());
                if(dataSnapshot.getValue()!=null){
                    b1.setEnabled(false);
                    b1.setText("You are marked for the day");
                }else{
                    b1.setEnabled(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d(TAG,databaseError.toString());
            }
        });
    }

}
