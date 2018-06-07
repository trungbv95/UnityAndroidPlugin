package com.example.unity;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.IOException;
import java.net.URLEncoder;
import android.os.Environment;
import android.util.Log;

import java.io.File;

/**
 * Created by buitr on 5/30/2018.
 */

public class FileBrowser
{
    private static MediaMetadataRetriever mmr;
    public static String GetExternalDrives()
    {
        File primary = Environment.getExternalStorageDirectory();
        String primaryPath = primary.getAbsolutePath();

        String result = primaryPath + ":";

        // Try paths saved at system environments
        // Credit: https://stackoverflow.com/a/32088396/2373034
        String strSDCardPath = System.getenv( "SECONDARY_STORAGE" );
        if( strSDCardPath == null || strSDCardPath.length() == 0 )
            strSDCardPath = System.getenv( "EXTERNAL_SDCARD_STORAGE" );

        if( strSDCardPath != null && strSDCardPath.length() > 0 )
        {
            String[] externalPaths = strSDCardPath.split( ":" );
            for( int i = 0; i < externalPaths.length; i++ )
            {
                String path = externalPaths[i];
                if( path != null && path.length() > 0 )
                {
                    File file = new File( path );
                    if( file.exists() && file.isDirectory() && file.canRead() && !file.getAbsolutePath().equalsIgnoreCase( primaryPath ) )
                    {
                        String absolutePath = file.getAbsolutePath() + File.separator + "Android";
                        if( new File( absolutePath ).exists() )
                        {
                            try
                            {
                                // Check if two paths lead to same storage (aliases)
                                if( !primary.getCanonicalPath().equals( file.getCanonicalPath() ) )
                                    result += file.getAbsolutePath() + ":";
                            }
                            catch( Exception e ) { }
                        }
                    }
                }
            }
        }

        // Try most common possible paths
        // Credit: https://gist.github.com/PauloLuan/4bcecc086095bce28e22
        String[] possibleRoots = new String[] { "/storage", "/mnt", "/storage/removable",
                "/removable", "/data", "/mnt/media_rw", "/mnt/sdcard0" };
        for( String root : possibleRoots )
        {
            try
            {
                File fileList[] = new File( root ).listFiles();
                for( File file : fileList )
                {
                    if( file.exists() && file.isDirectory() && file.canRead() && !file.getAbsolutePath().equalsIgnoreCase( primaryPath ) )
                    {
                        String absolutePath = file.getAbsolutePath() + File.separator + "Android";
                        if( new File( absolutePath ).exists() )
                        {
                            try
                            {
                                // Check if two paths lead to same storage (aliases)
                                if( !primary.getCanonicalPath().equals( file.getCanonicalPath() ) )
                                    result += file.getAbsolutePath() + ":";
                            }
                            catch( Exception ex ) { }
                        }
                    }
                }
            }
            catch( Exception e ) { }
        }

        return result;
    }

    public static String GetSongInformation(String path) {
        String result = new String();
        if(mmr != null)
        {
            mmr.release();
            mmr = null;
        }
        mmr = new MediaMetadataRetriever();

        mmr.setDataSource(path);

        String author = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR);
        String duration = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        result = author + ":" + duration;

        return result;
    }
}
