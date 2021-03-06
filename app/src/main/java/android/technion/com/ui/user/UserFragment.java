package android.technion.com.ui.user;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.technion.com.Database;
import android.technion.com.R;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.textservice.TextInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.facebook.AccessToken;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.net.URL;

public class UserFragment extends Fragment {
    private UserViewModel notificationsViewModel;
    private FirebaseAuth mAuth;
    private Database db;
    private String uid;

    private TextInputEditText userName;
    private TextInputEditText userEmail;
    private TextInputEditText userPhone;
    private TextView userText;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        db = new Database();

        notificationsViewModel =
                ViewModelProviders.of(this).get(UserViewModel.class);
        View root = inflater.inflate(R.layout.fragment_user, container, false);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null) {
            userName = root.findViewById(R.id.userName);
            userName.setKeyListener(null);

            userEmail = root.findViewById(R.id.userEmail);
            userEmail.setKeyListener(null);

            userPhone = root.findViewById(R.id.userPhone);
            userPhone.setKeyListener(null);

            uid = currentUser.getUid();
            TextView result = new TextView(getContext());
            db.checkUserExists(uid, result);
            if(result.getText().toString().equals("true")){
                db.getUserToTextViews(uid, userName, userEmail, userPhone);
            } else {
                userName.setText(currentUser.getDisplayName());
                userEmail.setText(currentUser.getEmail());
                userPhone.setText(currentUser.getPhoneNumber());
            }

            //Update user profile picture
            ImageView imgView = root.findViewById(R.id.profile_image);
            Uri imgUri = currentUser.getPhotoUrl();
            imgView.setImageURI(imgUri);
            Picasso.with(getActivity()).load(imgUri).into(imgView);
        }

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
//            user_name = currentUser.getDisplayName()
            db.getUserToTextViews(uid, userName, userEmail, userPhone);
        }
    }
}