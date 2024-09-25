package com.nsx.sest;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.renderscript.ScriptGroup;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.nsx.sest.databinding.ActivitySetupBinding;

import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@TargetApi(Build.VERSION_CODES.N)
public class TileControl extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        // Called when the user adds your tile to the Quick Settings
        Tile tile = getQsTile();
        tile.setState(Tile.STATE_INACTIVE);
        tile.updateTile();
        Toast.makeText(this, "Enjoy With New Stranger Data Security", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStartListening() {
        super.onStartListening();
        // Called when the tile becomes visible
    }

    @Override
    public void onStopListening() {
        super.onStopListening();
        // Called when the tile is no longer visible
    }
    class ConfigElement {
        public String target_path;
        public String[] target_extensions;

        public ConfigElement(String target_path, String[] target_extensions) {
            this.target_path = target_path;
            this.target_extensions = target_extensions;
        }
    }
    @Override
    public void onClick() {
        super.onClick();
        // Handle tile click
        Tile tile = getQsTile();
        List<ConfigElement> ConfigeElements = new ArrayList<>();
        Toast.makeText(this, "hello there", Toast.LENGTH_SHORT).show();
        try {
            byte[] config = new SeStFileIO(this, Environment.getExternalStorageDirectory().toString() + "/" + ".sestconfig").ReadBytes();
            String configdata = new String( config, StandardCharsets.UTF_8 );
            Toast.makeText( this, configdata, Toast.LENGTH_SHORT ).show();
            String[] lines = configdata.split("\n");
            for (String line: lines) {
                String target_path = line.split(":")[0].trim();
                String[] target_extensions = line.split(":")[1].split(",");
                {
                    int i = 0;
                    while (i < target_extensions.length) {
                        target_extensions[i] = target_extensions[i].trim();
                        i++;
                    }
                }
                ConfigeElements.add( new ConfigElement(target_path, target_extensions));
            }
//            for (ConfigElement element: ConfigeElements)
//            {
//                System.out.println("Target Path :" + element.target_path + ":");
//                for (String s: element.target_extensions) {
//                    System.out.println("->" + s +"<-");
//                }
//            }
        } catch (Exception e) {
            Toast.makeText(this, "Some Problem In Your Config File", Toast.LENGTH_SHORT).show();
            return ;
        }
        switch (tile.getState()) {
            case Tile.STATE_ACTIVE: {
                tile.setState(Tile.STATE_UNAVAILABLE);
                Toast.makeText(this, "Tile Deactivated", Toast.LENGTH_SHORT).show();
                showWorkerNotification(this, Exec_Type.Decrypt, tile, ConfigeElements);
            }
            break;
            case Tile.STATE_INACTIVE: {
                tile.setState(Tile.STATE_UNAVAILABLE);
                Toast.makeText(this, "Tile Activated", Toast.LENGTH_SHORT).show();
                showWorkerNotification(this, Exec_Type.Encrypt, tile, ConfigeElements);
            }
            break;
            case Tile.STATE_UNAVAILABLE: {
                Toast.makeText(this, "Cannot Change While Proccess Working.", Toast.LENGTH_SHORT).show();
            }
            break;
            default:

                break;
        }
        tile.updateTile();
    }
    public void showWorkerNotification(Context context, Exec_Type type, Tile tile, List<ConfigElement> ConfigElements) {
        String _what = "Encrypt";
        if (type == Exec_Type.Decrypt)
            _what = "Decrypt";
        final String what = _what;
        final String noti_title = "Finding..";
        String subtext = what+"ing...";
        final String channel_name = "Worker [IMPORTANT] ( Like You :) )";
        final String message = "Don't Turn Off Your Device!";
        final String channelId = "worker";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channel_name, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For New [Android versions]
            builder = new Notification.Builder(context, channelId);
        } else {
            // For Old [Android versions]
            builder = new Notification.Builder(context).setPriority(Notification.PRIORITY_HIGH);
        }

        // Notification properties :)
        builder.setSmallIcon(android.R.drawable.ic_lock_lock) // Default system icon
                .setContentTitle(noti_title)
                .setContentText(message)
                .setSubText(subtext)
                .setAutoCancel(true)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .setProgress(0, 0, false);

        // Show the notification
        notificationManager.notify(1, builder.build());

        new Thread(() -> {
            Handler handler = new Handler(Looper.getMainLooper());
            List<File> files = new ArrayList<>();
            if (type == Exec_Type.Encrypt) {
                for (ConfigElement element: ConfigElements) {
                    files.addAll(getAllFilesWith(element.target_path, element.target_extensions, false));
                }
            }
            else {
                for (ConfigElement element: ConfigElements) {
                    files.addAll(getAllFilesWith(element.target_path, new String[]{"$_$"}, false));
                }
            }
            int i = 0;
            for (File file: files)
            {
                int finalI = i;
                int finalI1 = i;
                handler.post( () -> {
                    builder.setContentTitle(finalI + "/" + files.size());
                    builder.setProgress(100, files.size() / 100 * finalI1, false);
                    notificationManager.notify(1, builder.build());
                });
                final String path = file.getAbsolutePath();
                try {
                    new SeStFileIO(this, path).exec(type);
                } catch (IOException e) {
                    Toast.makeText(context, "Something Wrong With File " + path, Toast.LENGTH_SHORT).show();
                }
                i++;
            }
            handler.post( () -> {
                if (type == Exec_Type.Encrypt)
                    tile.setState(Tile.STATE_ACTIVE);
                else
                    tile.setState(Tile.STATE_INACTIVE);
                tile.updateTile();
                notificationManager.cancel(1);
                showInfoNotification(context, type, files.size());
            } );
        }).start();

        // Dismiss notification after 3 seconds if needed // FOR DEBUGGIBG ONLY
//        new Handler(Looper.getMainLooper()).postDelayed(() -> {
//            notificationManager.cancel(1);
//            if (type == Exec_Type.Decrypt)
//                tile.setState(Tile.STATE_ACTIVE);
//            else
//                tile.setState(Tile.STATE_INACTIVE);
//            tile.updateTile();
//            showInfoNotification(context, true);
//        }, 3000);
    }
    public void showInfoNotification(Context context, Exec_Type type, int total) {
        String _what = "Encrypt";
        if (type == Exec_Type.Decrypt)
            _what = "Decrypt";
        final String what = _what;
        final String noti_title = what+"ed " + total + " Picture :)";
        String subtext = what+" Successfully";
        final String channel_name = "Noticer [RECOMMENDED]";
        final String message = what+"ing Successfully ✔";
        final String channelId = "noticer";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, channel_name, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // For New [Android versions]
            builder = new Notification.Builder(context, channelId);
        } else {
            // For Old [Android versions]
            builder = new Notification.Builder(context).setPriority(Notification.PRIORITY_HIGH);
        }

        // Notification properties :)
        builder.setSmallIcon(android.R.drawable.ic_lock_lock) // Default system icon
                .setContentTitle(noti_title)
                .setContentText(message)
                .setSubText(subtext);
        notificationManager.notify(2, builder.build());
    }
    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        // Called when the user removes your tile from the Quick Settings
    }
    public List<File> getAllFilesWith(String f, String[] targer_extensions, boolean from_recursive) {
        File directory = new File(Environment.getExternalStorageDirectory().toString()  + "/" + f);
        if (from_recursive)
            directory = new File(f);
        List<File> TargFiles = new ArrayList<>();
        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        TargFiles.addAll(getAllFilesWith(file.toString(), targer_extensions, true));
                    } else if (Extension_In(targer_extensions, file.getName())) {
                        TargFiles.add(file);
                    }
                }
            }
        }
        return (TargFiles);
    }
    private boolean Extension_In(String[] list, String element){
        for (String s: list)
        {
            if (element.toLowerCase().endsWith("."+s))
                return (true);
        }
        return (false);
    }
}