package com.crpilarsoubrier.habla.data;


import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.crpilarsoubrier.habla.data.dashboard.Dashboard;
import com.crpilarsoubrier.habla.data.dashboard.DashboardDao;
import com.crpilarsoubrier.habla.data.dashboard.PictoInDashboardDao;
import com.crpilarsoubrier.habla.data.dashboard.PictoInDashboardEntity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Picto.class, Dashboard.class, PictoInDashboardEntity.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{

    public abstract PictoDao pictoDao();
    public abstract PictoInDashboardDao pictoInDashboardDao();
    public abstract DashboardDao dashboardDao();
    private static final String ALBUM_NAME = "Habla";
    private static final String ASSETS_BUILTIN_DATA_DIRECTORY = "pictos";
    private static final String GENERIC_CATEGORY_IMAGE_FILENAME = "generico.png";
    private static final String GO_BACK_IMAGE_FILENAME = "volver.png";
    private static final Long MAIN_CATEGORY_ID = null;
    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;

    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "picto_database")
                            //.addCallback(sRoomDatabaseCallback) // Through a function
                            .addCallback(new RoomDatabase.Callback()  { // Inline callback to allow using context
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db){
                                    super.onCreate(db);
                                    //context.fileList();
                                    populateDatabase(context);
                                }
                    })
                            .build();
                }
            }
            // Should we load sample data here?
        }
        return INSTANCE;
    }

    /**
     * * We insert a category for each folder in /assets/pictos and a we insert a Picto
     * * for each file.
     * * We also copy the files from asset to AlbumStorageDir, following the android developers
     * * best practices.
     * * @todo Think twice of using AlbumStorageDir, InternalStorage or ExternalStorage
     */
    private static void populateDatabase(Context context) {

        databaseWriteExecutor.execute(() -> {
            // Populate the database in the background.
            PictoDao pictoDao = INSTANCE.pictoDao();
            pictoDao.deleteAll();

            File externalFileDir = getAppSpecificAlbumStorageDir(context, ALBUM_NAME);
            Log.println(Log.INFO, "populateDatabase", "externalFileDir: " + externalFileDir.toString());

            // Copy every file in the specified directory recursively
            loadBuiltinPictos(ASSETS_BUILTIN_DATA_DIRECTORY, externalFileDir, MAIN_CATEGORY_ID , context, pictoDao);

            DashboardDao dashboardDao = INSTANCE.dashboardDao();
            loadSampleDashboards(dashboardDao);
        });
    }

    // @todo Check if generic image filename exists
    private static void loadBuiltinPictos(String assetDirectory, File targetDirectory, Long parentId, Context context, PictoDao dao){

        AssetManager assetManager = context.getAssets();
        String[] files = null;
        String parentName = null;
        Log.println(Log.INFO, "populateDatabase", "parentId: " + parentId + " parentName: " + parentName);
        if (parentId != MAIN_CATEGORY_ID) parentName = (dao.getPictoById(parentId)).getText(); // If it has parent we want to know its name

        try {
            files = assetManager.list(assetDirectory);
            Log.println(Log.INFO, "populateDatabase", "files in " + assetDirectory + ": " + Arrays.toString(files));
        } catch (IOException e) {
            Log.e("populateDatabase", "Failed to get asset file list in " + assetDirectory, e);
        }
        for(String filename : files) {
            String inFilename = assetDirectory + "/" + filename;
            InputStream in = null;
            OutputStream out = null;
            boolean isASubfolder = false;
            if (!filename.contains(".")) isASubfolder = true;

            String text = filename.split("\\.")[0].replace("_", " ").replace("-"," ").toUpperCase();

            Log.println(Log.INFO, "populateDatabase", "Processing " + inFilename + "(filename: " + filename + ", text: "+ text + ") parentName: " + parentName + "(" + parentId + ")");
            if (text.equalsIgnoreCase(parentName)) {
                Log.println(Log.INFO, "populateDatabase", "Skipping filename " + filename + " because it is the filename of a parent");
                continue; // We have reached a the image file of the category. We do not want to create a picto for this, because we alredy have a picto representing the category
            }


            if (isASubfolder) {
                Log.println(Log.INFO, "populateDatabase", inFilename + " is a subfolder (filename: " + filename + ")");
                // If is a subfolder (the picto is a category, it is a picto which contains other pictos)
                // so we should search for the appropriate image file
                if (assetFileExists(assetManager, assetDirectory + "/" + filename, filename.toLowerCase() + ".png"))
                    inFilename = assetDirectory + "/" + filename + "/" + filename.toLowerCase() + ".png";
                else
                    inFilename = assetDirectory + "/" + GENERIC_CATEGORY_IMAGE_FILENAME; // We should check if generic image filename exists
                Log.println(Log.INFO, "populateDatabase", "Selected image file (inFilename): " + inFilename);
            }

            // Create and insert the picto into the database
            Log.println(Log.INFO, "populateDatabase", "Picto text: " + text);
            Picto picto = new Picto(filename, text);
            long rowId = dao.insert(picto); // Get the autogenerated insert ID
            String internalFilename;
            internalFilename = Long.toString(rowId) + "." +  inFilename.split("\\.")[1]; // Filename will be insertID.original extension (i.e. 27.png)
            Log.println(Log.INFO, "populateDatabase", "internalFilename: " + internalFilename);
            picto.setId(rowId); // My picto object must have the same ID as the row to allow update it
            picto.setPicFilePath(internalFilename); // New filename for the file image, id + original extension
            picto.setParentId(parentId);
            dao.update(picto);

            try {
                // Let's copy
                File outFile = new File(targetDirectory, internalFilename);
                Log.println(Log.INFO, "populateDatabase", "Let's copy from " + inFilename + " to " + outFile);
                in = assetManager.open(inFilename);
                out = new FileOutputStream(outFile);
                copyFile(in, out);
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
            } catch (IOException e) {
                Log.e("populateDatabase", "Failed to copy asset file: " + filename, e);
            }
            if (isASubfolder) { // We already have create the picto, but if it was a directory we should search for files inside it to create more pictos
                loadBuiltinPictos(assetDirectory + "/" + filename, targetDirectory, picto.getId(), context, dao);
            }
        }

        // Finally we copy the file that will be used in the GO BACK picto
        // To allow coming back when the user is in a specific category and want go to the main category
        String inFilename = GO_BACK_IMAGE_FILENAME;
        InputStream in = null;
        OutputStream out = null;
        String internalFilename = "-1000.png";
        try {
            // Let's copy
            File outFile = new File(targetDirectory, internalFilename);
            Log.println(Log.INFO, "populateDatabase", "Let's copy GO_BACK from " + inFilename + " to " + outFile);
            in = assetManager.open(inFilename);
            out = new FileOutputStream(outFile);
            copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch (IOException e) {
            Log.e("populateDatabase", "Failed to copy GO_BACK asset file: " + inFilename, e);
        }


    }

    public static boolean assetFileExists(AssetManager assetManager, String path, String filename) {
        //Log.println(Log.INFO, "populateDatabase", "Let's check if file exists. path: " + path + " filename: " + filename);
        try {
            //Log.println(Log.INFO, "populateDatabase", "assetManager.list(path): " + Arrays.toString(assetManager.list(path)));
            return Arrays.asList(assetManager.list(path)).contains(filename);
        } catch (IOException e) {
            return false;
        }
    }

    private static void copyFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }

    @Nullable
    static File getAppSpecificAlbumStorageDir(Context context, String albumName) {
        // Get the pictures directory that's inside the app-specific directory on
        // external storage.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_PICTURES), albumName);
        if (file == null || !file.mkdirs()) {
            Log.e("populateDatabase", "Directory not created");
        }
        return file;
    }

    /**
     * Some sample dashboards
     */
    private static void loadSampleDashboards(DashboardDao dashboardDao) {
        Dashboard dashboard = new Dashboard( "Asamblea", true, true, 1, 1);
        long dashboardId = dashboardDao.insert(dashboard);

        PictoInDashboardDao pictoInDashboardDao = INSTANCE.pictoInDashboardDao();
        PictoInDashboardEntity pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 1, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 2, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 3, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 4, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 5, true, 1);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 6, true, 1);
        pictoInDashboardDao.insert(pictoInDashboardEntity);

        dashboard = new Dashboard( "Paz", true, true, 1, 1);
        dashboardId = dashboardDao.insert(dashboard);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 7, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 8, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 9, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 10, true, 0);
        pictoInDashboardDao.insert(pictoInDashboardEntity);
        pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, 11, true, 1);
        pictoInDashboardDao.insert(pictoInDashboardEntity);

    }
}

