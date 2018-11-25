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
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.SimpleFuture;

/**
 * Created by koush on 5/20/16.
 */
@SuppressLint("MissingPermission")
public class AccountAuthHelper {

    public static Future<UserToken> getGoogleSigninBackground(final Context context, String clientId) {
        final SimpleFuture<UserToken> ret = new SimpleFuture<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .requestIdToken(clientId)
        .requestId()
        .build();

        GoogleSignInClient client = GoogleSignIn.getClient(context, gso);

        client.silentSignIn()
        .addOnCompleteListener(task -> {
            try {
                ret.setComplete(new UserToken(task.getResult(ApiException.class).getId(), task.getResult(ApiException.class).getEmail(), task.getResult(ApiException.class).getIdToken()));
            }
            catch (Exception e) {
                ret.setComplete(e);
            }
        });

        return ret;
    }

    public static Future<UserToken> getGoogleSigninForeground(final WindowChromeCompatActivity activity, String clientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(clientId)
                .requestId()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(activity, gso);

        Intent intent = client.getSignInIntent();

        return activity.startActivityForResult(intent)
        .thenConvert(from -> {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(from);
            return new UserToken(task.getResult(ApiException.class).getId(), task.getResult(ApiException.class).getEmail(), task.getResult(ApiException.class).getIdToken());
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
