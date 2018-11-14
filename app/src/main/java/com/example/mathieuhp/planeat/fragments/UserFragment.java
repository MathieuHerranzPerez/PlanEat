package com.example.mathieuhp.planeat.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mathieuhp.planeat.R;
import com.example.mathieuhp.planeat.activities.LoginActivity;
import com.example.mathieuhp.planeat.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserFragment extends Fragment {

    Button deconnectionBtn;
    TextView email;
    EditText firstName;
    EditText lastName;
    User user;
    Button btnModif;
    Button btnDelete;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, null);

        /* --- layout element binding --- */
        deconnectionBtn = (Button) view.findViewById(R.id.btn_deconnection);

        email = (TextView) view.findViewById(R.id.user_email);
        email.setText(user.getEmail());

        firstName = (EditText) view.findViewById(R.id.edit_first_name);
        firstName.setHint(user.getFirstName());

        lastName = (EditText) view.findViewById(R.id.edit_last_name);
        lastName.setHint(user.getLastName());

        btnModif = (Button) view.findViewById(R.id.btn_modif);

        btnDelete = (Button) view.findViewById(R.id.btn_delete_user);


        /* --- /layout element binding --- */



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
        btnModif.setOnClickListener(new OnClickListenerSavingChange(firstName, lastName));

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

        // setup delete button
        btnDelete.setOnClickListener(new OnClickListenerDeleteUser(user));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            // get the user from the activity
            user = bundle.getParcelable("user");
        }
    }


    private class OnClickListenerSavingChange implements View.OnClickListener {

        private EditText firstNameEdit;
        private EditText lastNameEdit;

        private OnClickListenerSavingChange(EditText firstNameEdit, EditText lastNameEdit) {
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

    /**
     * Delete the firebase user
     */
    private class OnClickListenerDeleteUser implements View.OnClickListener {

        private User user;

        private OnClickListenerDeleteUser(User user) {
            this.user = user;
        }

        @Override
        public void onClick(View view) {
            DialogDeleteUser confirmWindow = new DialogDeleteUser(getActivity(), user);
            confirmWindow.show();
        }
    }

    /**
     * provide a minimum security to avoid miss click by asking a confirmation from the real user
     */
    private class DialogDeleteUser extends Dialog implements android.view.View.OnClickListener {

        private Activity activity;
        private Button btnYes;
        private Button btnCancel;
        private User user;

        private DialogDeleteUser(Activity a, User user) {
            super(a);
            this.activity = a;
            this.user = user;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_delete_user);
            btnYes = (Button) findViewById(R.id.btn_yes);
            btnCancel = (Button) findViewById(R.id.btn_cancel);
            btnYes.setOnClickListener(this);
            btnCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_yes :
                    // TODO recipes suppression

                    // delete our user
                    user.deleteData();

                    // delete firebase authentification user
                    FirebaseUser userFirebase = FirebaseAuth.getInstance().getCurrentUser();
                    userFirebase.delete()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(activity, getResources().getString(R.string.delete_done),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    break;
                case R.id.btn_cancel :
                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }
}
