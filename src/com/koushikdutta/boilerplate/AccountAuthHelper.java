package com.koushikdutta.boilerplate;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.scratch.Deferred;
import com.koushikdutta.scratch.Promise;

/**
 * Created by koush on 5/20/16.
 */
@SuppressLint("MissingPermission")
public class AccountAuthHelper {

    public static Promise<UserToken> getGoogleSigninBackground(final Context context, String clientId) {
        final Deferred<UserToken> ret = new Deferred<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(clientId)
        .requestId()
        .build();

        GoogleSignInClient client = GoogleSignIn.getClient(context, gso);

        client.silentSignIn()
        .addOnCompleteListener(task -> {
            try {
                ret.resolve(new UserToken(task.getResult(ApiException.class).getId(), task.getResult(ApiException.class).getEmail(), task.getResult(ApiException.class).getIdToken()));
            }
            catch (Exception e) {
                ret.reject(e);
            }
        });

        return ret.getPromise();
    }

    public static Promise<UserToken> getGoogleSigninForeground(final WindowChromeCompatActivity activity, String clientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(clientId)
                .requestId()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(activity, gso);

        Intent intent = client.getSignInIntent();

        return activity.startActivityForResult(intent)
        .next(from -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(from);
            return Promise.resolve(new UserToken(task.getResult(ApiException.class).getId(), task.getResult(ApiException.class).getEmail(), task.getResult(ApiException.class).getIdToken()));
        });
    }

    public static class UserToken {
        public UserToken(String id, String email, String token) {
            this.id = id;
            this.email = email;
            this.token = token;
        }

        public final String id;
        public final String token;
        public String email;
    }
}
