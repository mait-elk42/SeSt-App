package com.nsx.sest;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

enum Exec_Type {
    Encrypt,
    Decrypt
}

class SecStWorker {
    private byte[]      _pass;
    public SecStWorker(String _pass) {
        this._pass = _pass.getBytes();
    }
    public byte[]   execute(byte[] _bytes, Exec_Type type)
    {
        int i = 0;
        while ( i < _bytes.length )
        {
            if (type == Exec_Type.Encrypt)
                _bytes[i] += (byte) _pass[ i % (_pass.length -1) ];
            else if (type == Exec_Type.Decrypt)
                _bytes[i] -= (byte) _pass[ i % (_pass.length -1) ];
            i++;
        }
        return ( _bytes );
    }
}

public class SeStFileIO {
    private final   Context               _context;
    private final   SecStWorker           _worker;
    private final   String              _filename;
    private final   File                _file;

    SeStFileIO(Context _context, String _filename) throws FileNotFoundException {
        this._filename = _filename;
        this._context = _context;
        this._file = new File(this._filename);
        if (!_file.exists())
            throw new FileNotFoundException("File Not Found - الملف غير موجود");
        this._worker = new SecStWorker("mait-elk");
    }
    private byte[] ReadBytes() throws IOException {
        FileInputStream _fin_stream = new FileInputStream(_filename);
        byte[] data = new byte[_fin_stream.available()];
        _fin_stream.read(data);
        _fin_stream.close();
        return (data);
    }
    private void WriteBytes(byte[] data) throws IOException {
        FileOutputStream _fout_stream = new FileOutputStream(_filename);
        _fout_stream.write(data);
        _fout_stream.close();
    }
    public void exec(Exec_Type type) throws IOException {
        WriteBytes(_worker.execute(ReadBytes(), type));
        File file;
        if (type == Exec_Type.Encrypt)
            file = new File(_filename + ".$_$");
        else
            file = new File(_filename.substring(0, _filename.lastIndexOf(".")));
        _file.renameTo(file);
        updateMediaStore(file);
    }
    private void updateMediaStore(File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Android 10 and above: Use MediaStore
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.IS_PENDING, 1);

            ContentResolver resolver = _context.getContentResolver();
            Uri collection = MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri uri = resolver.insert(collection, values);

            if (uri != null) {
                values.clear();
                values.put(MediaStore.Images.Media.IS_PENDING, 0);
                resolver.update(uri, values, null, null);
            }
        } else {
            // Android 9 and below: Use ACTION_MEDIA_SCANNER_SCAN_FILE
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            intent.setData(Uri.fromFile(file));
            _context.sendBroadcast(intent);
        }
    }
    public String get_filename() {
        return _filename;
    }
}