package com.example.mathieuhp.planeat.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.activities.LoginActivity;
import com.example.mathieuhp.planeat.models.User;
import com.google.firebase.auth.FirebaseAuth;

public class UserFragment extends Fragment {

    Button deconnectionBtn;
    TextView email;
    EditText firstName;
    EditText lastName;
    User user;
    Button btnModif;

    TextView userText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);
        deconnectionBtn = (Button) view.findViewById(R.id.btn_deconnection);

        email = (TextView) view.findViewById(R.id.user_email);
        email.setText(user.getEmail());

        firstName = (EditText) view.findViewById(R.id.edit_first_name);
        firstName.setHint(user.getFirstName());

        lastName = (EditText) view.findViewById(R.id.edit_last_name);
        lastName.setHint(user.getLastName());

        btnModif = (Button) view.findViewById(R.id.btn_modif);


        userText = (TextView) view.findViewById(R.id.user);
        userText.setText(user.toString());



        // setup the firstName editText
        firstName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName.setText(user.getFirstName());
            }
        });
        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                btnModif.setVisibility(View.VISIBLE);
            }
        });

        // setup the lastName editText
        lastName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lastName.setText(user.getLastName());
            }
        });
        lastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
            @Override
            public void afterTextChanged(Editable editable) {
                btnModif.setVisibility(View.VISIBLE);
            }
        });

        // setup the save change button
        btnModif.setOnClickListener(new myOnClickListenerSavingChange(firstName, lastName));

        // setup deconnection button
        deconnectionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            user = bundle.getParcelable("user");
        }
    }


    private class myOnClickListenerSavingChange implements View.OnClickListener {

        private EditText firstNameEdit;
        private EditText lastNameEdit;

        private myOnClickListenerSavingChange(EditText firstNameEdit, EditText lastNameEdit) {
            this.firstNameEdit = firstNameEdit;
            this.lastNameEdit = lastNameEdit;
        }

        @Override
        public void onClick(View view) {
            user.setFirstName(firstNameEdit.getText().toString());
            user.setLastName(lastNameEdit.getText().toString());
            user.updateData();
            view.findViewById(R.id.btn_modif).setVisibility(View.INVISIBLE);
            Toast.makeText(getActivity(), getResources().getString(R.string.saved), Toast.LENGTH_SHORT).show();
        }
    }
}
