package com.koushikdutta.boilerplate;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.future.SimpleFuture;
import com.koushikdutta.async.future.TransformFuture;

import java.io.IOException;

/**
 * Created by koush on 5/20/16.
 */
@SuppressLint("MissingPermission")
public class AccountAuthHelper {

    public static Future<String> getGoogleSigninBackground(final Context context, String clientId) {
        final SimpleFuture<String> ret = new SimpleFuture<>();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(clientId)
        .requestId()
        .build();

        GoogleSignInClient client = GoogleSignIn.getClient(context, gso);

        client.silentSignIn()
        .addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
            @Override
            public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                try {
                    ret.setComplete(task.getResult(ApiException.class).getIdToken());
                }
                catch (Exception e) {
                    ret.setComplete(e);
                }
            }
        });

        return ret;
    }


    public static Future<UserToken> getGoogleSigninForeground(final WindowChromeCompatActivity activity, String clientId) {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(clientId)
                .requestId()
                .build();

        GoogleSignInClient client = GoogleSignIn.getClient(activity, gso);

        Intent intent = client.getSignInIntent();

        return activity.startActivityForResult(intent)
        .then(new TransformFuture<UserToken, Intent>() {
            @Override
            protected void transform(Intent result) {
                try {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result);
                    setComplete(new UserToken(task.getResult(ApiException.class).getEmail(), task.getResult(ApiException.class).getIdToken()));
                }
                catch (Exception e) {
                    setComplete(e);
                }

            }
        });
    }

    private static TransformFuture<String, AccountManagerFuture<Bundle>> getAuthTransform() {
        return new TransformFuture<String, AccountManagerFuture<Bundle>>() {
            @Override
            protected void transform(AccountManagerFuture<Bundle> result) throws AuthenticatorException, OperationCanceledException, IOException {
                if (result == null)
                    throw new NullPointerException("auth future was null");
                Bundle bundle = result.getResult();
                if (bundle == null)
                    throw new NullPointerException("auth bundle was null");
                String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);
                if (token == null)
                    throw new NullPointerException("auth token provided by bundle was null");
                setComplete(token);
            }
        };
    }

    public static Future<String> getAuthTokenBackground(Context context, String accountString, final String tokenType) {
        if (accountString == null) {
            SimpleFuture<String> ret = new SimpleFuture<>();
            ret.setComplete(new Exception("Can not call from background with null account"));
            return ret;
        }

        final TransformFuture<String, AccountManagerFuture<Bundle>> ret = getAuthTransform();
        Account account = new Account(accountString, "com.google");
        AccountManager am = AccountManager.get(context);
        AccountManagerFuture<Bundle> future = am.getAuthToken(account, tokenType, null, true, new AccountManagerCallback<Bundle>() {
            @Override
            public void run(AccountManagerFuture<Bundle> future) {
                ret.onCompleted(null, future);
            }
        }, new Handler());

        // can finish synchronously?
        if (future.isDone())
            ret.onCompleted(null, future);
        return ret;
    }

    public static class UserToken {
        public UserToken(String id, String token) {
            this.id = id;
            this.token = token;
        }

        public final String id;
        public final String token;
    }

    public static Future<UserToken> getAuthTokenForeground(final WindowChromeCompatActivity activity, final String account, final String tokenType) {
        final SimpleFuture<UserToken> ret = new SimpleFuture<>();

        getAuthTokenBackground(activity, account, tokenType)
        .setCallback(new FutureCallback<String>() {
            String usedAccount;
            @Override
            public void onCompleted(Exception e, String token) {
                if (token != null) {
                    ret.setComplete(new UserToken(account, token));
                    return;
                }

                SimpleFuture<String> accountFuture;
                if (account != null) {
                    accountFuture = new SimpleFuture<>();
                    accountFuture.setComplete(account);
                }
                else {
                    AccountManager am = AccountManager.get(activity);
                    Intent choose = am.newChooseAccountIntent(null, null, new String[] { "com.google" }, false, null, null, null, null);
                    accountFuture = activity.startActivityForResult(choose).then(new TransformFuture<String, Intent>() {
                        @Override
                        protected void transform(Intent result) {
                            if (result == null)
                                throw new NullPointerException("account bundle was null");
                            String account = result.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                            if (account == null)
                                throw new NullPointerException("account in bundle was null");
                            setComplete(account);
                        }
                    });
                }

                ret.setComplete(accountFuture.then(new TransformFuture<AccountManagerFuture<Bundle>, String>() {
                    @Override
                    protected void transform(String account) {
                        usedAccount = account;

                        AccountManager am = AccountManager.get(activity);
                        AccountManagerFuture<Bundle> future = am.getAuthToken(new Account(account, "com.google"), tokenType, null, activity, new AccountManagerCallback<Bundle>() {
                            @Override
                            public void run(AccountManagerFuture<Bundle> future) {
                                setComplete(future);
                            }
                        }, new Handler());

                        if (future.isDone())
                            setComplete(future);
                    }
                })
                .then(getAuthTransform())
                .then(new TransformFuture<UserToken, String>() {
                    @Override
                    protected void transform(String token) {
                        setComplete(new UserToken(usedAccount, token));
                    }
                }));
            }
        });

        return ret;
    }
}
