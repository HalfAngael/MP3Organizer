import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileFilter;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.generic.AudioFileReader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

/**
 * Throw file in directory with music and folders. Program then organizes music files into 'Arist - Album' format
 */
public class Folderer
{
    public void folderFinder()
    {
        File currentDirec = new File(".");
        File[] fileList = currentDirec.listFiles();
        ArrayList<File> aListFile = new ArrayList<File>();
        ArrayList<File> aListDirs = new ArrayList<File>();
        AudioFileFilter filter = new AudioFileFilter();
        for (File ifFile : fileList)
        {
            if (ifFile.isFile())
            {
                if (filter.accept(ifFile))
                {
                    aListFile.add(ifFile);
                }
            }
            else if (ifFile.isDirectory())
            {
                if (ifFile.getName().contains("-"))
                {
                    aListDirs.add(ifFile);
                }
            }
        }
        for (File mp3 : aListFile)
        {
            try
            {
                AudioFile readFile = AudioFileIO.read(mp3);
                Tag tag = readFile.getTag();
                String artist = tag.getFirst(FieldKey.ARTIST);
                String album = tag.getFirst(FieldKey.ALBUM);
                String cat = artist + " - " + album;
                boolean found = false;
                for (File ifFile : aListDirs)
                {
                    if (ifFile.getName().equals(cat))
                    {
                        //Move file to directory
                        Files.move(Paths.get(mp3.getName()), Paths.get(ifFile.getName() + "/" + mp3.getName()), REPLACE_EXISTING);
                        found = true;
                    }
                }
                if (!found)
                {
                    File newFile = new File(cat);
                    newFile.mkdir();
                    //Make it a directory and move file inside
                    Files.move(Paths.get(mp3.getName()), Paths.get(cat + "/" + mp3.getName()), REPLACE_EXISTING);
                }
            }
            catch (Exception e)
            {
                System.out.print(e.getMessage());
            }
        }
    }

    public static void main(String[] args)
    {
        Folderer app = new Folderer();
        app.folderFinder();
    }
}