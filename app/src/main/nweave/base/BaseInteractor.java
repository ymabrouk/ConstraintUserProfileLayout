package base;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.nweave.provider.model.City;
import com.nweave.provider.model.Model;
import com.nweave.provider.model.User;
import com.nweave.provider.model.Zone;
import com.nweave.provider.model.ZoneCategory;
import com.nweave.provider.preferences.Constants;
import com.nweave.provider.repository.DataSourceRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yassermabrouk on 11/1/16.
 */

public class BaseInteractor {

    private static final String TAG = BaseInteractor.class.getName();
    private DatabaseReference mDatabaseReference;

    public BaseInteractor() {
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void retrieveZoneCategories(final OnGetInteractorFinishedListener<ZoneCategory> onInteractorFinishedListener) {

        Query mCitiesQuery = mDatabaseReference.child("zone_categories");
        mCitiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "Load categories:onDataChange");
                List<ZoneCategory> categories = new ArrayList<>();

                for (DataSnapshot zoneDataSnapshot : dataSnapshot.getChildren()) {
                    ZoneCategory category = zoneDataSnapshot.getValue(ZoneCategory.class);
                    category.setKey(zoneDataSnapshot.getKey());
                    categories.add(category);
                }
                onInteractorFinishedListener.onSuccess(categories);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Load zones categories :onCancelled", databaseError.toException());
                onInteractorFinishedListener.onFailure(databaseError.getMessage());
            }
        });

    }

    public void saveZone(Zone zone, final OnSaveOrDeleteInteractorFinishedListener onInteractorFinishedListener) {

        Map<String, Object> childUpdates = new HashMap<>();

        Map<String, Object> zoneValues = zone.toMap();
        if(null == zone.getKey()){
            String key = mDatabaseReference.child(Constants.DB_OBJECT_ZONES).push().getKey();
            zone.setKey(key);
        }
        childUpdates.put("/zones/" + zone.getCity().getKey() + "/" + zone.getKey(), zoneValues);

        mDatabaseReference.updateChildren(childUpdates).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "addOnFailureListener:onFailure:" + e);
                onInteractorFinishedListener.onFailure(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "create Zones :onComplete:" + task.isSuccessful());
                if(task.isSuccessful()){
                    onInteractorFinishedListener.onSuccess();
                }else {
                    onInteractorFinishedListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public void deleteZone(Zone zone, final OnSaveOrDeleteInteractorFinishedListener onSaveOrDeleteInteractorFinishedListener) {
        mDatabaseReference.child(Constants.DB_OBJECT_ZONES).child(zone.getCity().getKey()).child(zone.getKey()).child("is_deleted").setValue(true).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                onSaveOrDeleteInteractorFinishedListener.onFailure(e.getMessage());
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "delete Zone :onComplete:" + task.isSuccessful());
                if(task.isSuccessful()){
                    onSaveOrDeleteInteractorFinishedListener.onSuccess();
                }else {
                    onSaveOrDeleteInteractorFinishedListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }


    public void requestCities(final OnGetInteractorFinishedListener<City> onInteractorFinishedListener){
        final List<City> mCachedCities = DataSourceRepository.getInstance().getCachedCities();
        if(null == mCachedCities){
            Query mCitiesQuery = mDatabaseReference.child(Constants.DB_OBJECT_CITIES);
            mCitiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.w(TAG, "Load Cities:onDataChange");
                    AsyncTask<DataSnapshot, Void, List<City> > asyncTask = new AsyncTask<DataSnapshot, Void, List<City>>() {

                        @Override
                        protected List<City> doInBackground(DataSnapshot... dataSnapshots) {
                            List<City> cities = new ArrayList<>();
                            for (DataSnapshot citySnapshot: dataSnapshots[0].getChildren()) {
                                City city=  citySnapshot.getValue(City.class);
                                city.setKey(citySnapshot.getKey());
                                cities.add(city);
                            }
                            return  cities;
                        }

                        @Override
                        protected void onPostExecute(List<City> cities) {
                            super.onPostExecute(cities);
                            DataSourceRepository.getInstance().setCachedCities(cities);
                            onInteractorFinishedListener.onSuccess(cities);
                        }
                    };
                    asyncTask.execute(dataSnapshot);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w(TAG, "Load Cities:onCancelled", databaseError.toException());
                    onInteractorFinishedListener.onFailure(databaseError.getMessage());
                }
            });

        } else {
            onInteractorFinishedListener.onSuccess(mCachedCities);
        }

    }

    public void requestZonesByCity(final City city, final OnGetInteractorFinishedListener<Zone> interactorFinishedListener){
        Query mCitiesQuery = mDatabaseReference.child(Constants.DB_OBJECT_ZONES).child(city.getKey()).orderByChild("is_deleted").equalTo(false);
        mCitiesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w(TAG, "Load Zones :onDataChange");
                List<Zone> zones = new ArrayList<>();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Zone zone=  snapshot.getValue(Zone.class);
                    zone.setKey(snapshot.getKey());
                    zone.convert();
                    zone.setCity(city);
                    zones.add(zone);
                }
                interactorFinishedListener.onSuccess(zones);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Load Zones:onCancelled", databaseError.toException());
                interactorFinishedListener.onFailure(databaseError.getMessage());
            }
        });
    }


    public void saveUserAccount(final User user, final OnSaveOrDeleteInteractorFinishedListener onSaveOrDeleteInteractorFinishedListener) {
        Map<String, Object> childUpdates = new HashMap<>();
        DatabaseReference mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> userValues = user.toMap();
        childUpdates.put(Constants.DB_OBJECT_ACCOUNTS  + "/" + user.getKey(), userValues);
        mDatabaseReference.updateChildren(childUpdates).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "addOnFailureListener:onFailure:" + e);
            }
        }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "create account Object :onComplete:" + task.isSuccessful());
                if (task.isSuccessful()) {
                    onSaveOrDeleteInteractorFinishedListener.onSuccess();
                } else {
                    onSaveOrDeleteInteractorFinishedListener.onFailure(task.getException().getMessage());
                }
            }
        });
    }

    public void createUser(final User user,final OnSaveOrDeleteInteractorFinishedListener onSaveOrDeleteInteractorFinishedListener ) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (task.isSuccessful()) {
                            Log.d(TAG, "create user with email and password");
                            user.setKey(FirebaseAuth.getInstance().getCurrentUser().getUid());
                            saveUserAccount(user, new OnSaveOrDeleteInteractorFinishedListener() {
                                @Override
                                public void onSuccess() {
                                    sendPasswordResetEmail(user);
                                    reAuthenticate(onSaveOrDeleteInteractorFinishedListener);

                                }

                                @Override
                                public void onFailure(String errorMessage) {
                                    onSaveOrDeleteInteractorFinishedListener.onFailure(errorMessage);
                                }
                            });
                        } else {
                            onSaveOrDeleteInteractorFinishedListener.onFailure(task.getException().getMessage());
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "addOnFailureListener:onFailure:" + e);
            }
        });
    }

    private void reAuthenticate(final OnSaveOrDeleteInteractorFinishedListener onSaveOrDeleteInteractorFinishedListener) {
        FirebaseAuth.getInstance().signOut();
        String ADMIN_USER = DataSourceRepository.getInstance().getCachedUserEmail();
        String password = DataSourceRepository.getInstance().getCachedUserPassword();
        FirebaseAuth.getInstance().signInWithEmailAndPassword(ADMIN_USER, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "reAuthenticate done.");
//                            onSaveOrDeleteInteractorFinishedListener.onSuccess();
                        }else {
//                            onSaveOrDeleteInteractorFinishedListener.onFailure(task.getException().getMessage());
                        }
                        onSaveOrDeleteInteractorFinishedListener.onSuccess();

                    }
                });

    }


    private void sendPasswordResetEmail(final User user) {
        FirebaseAuth.getInstance().sendPasswordResetEmail(user.getEmail())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "sendPasswordResetEmail : Email sent.");
                            mDatabaseReference.child(Constants.DB_OBJECT_ACCOUNTS).child(user.getKey()).child("password_reset_email_sent").setValue(true);
                        } else {
                            Log.d(TAG, "sendPasswordResetEmail : Failed to send email to " + user.getEmail());
                        }

                    }
                });
    }

    public interface OnGetInteractorFinishedListener<T extends Model> {

        void onSuccess(List<T> models);

        void onFailure(String errorMessage);

    }
    public interface OnSaveOrDeleteInteractorFinishedListener {

        void onSuccess();

        void onFailure(String errorMessage);

    }

}
