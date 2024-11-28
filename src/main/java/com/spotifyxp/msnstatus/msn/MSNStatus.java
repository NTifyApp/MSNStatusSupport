package com.spotifyxp.msnstatus.msn;

import com.spotifyxp.logging.ConsoleLogging;
import com.sun.jna.*;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.platform.win32.WinDef.*;

public class MSNStatus {
    public enum MsnMessageTypes {
        None, Music, Games, Office
    }

    /**
     * Sends info to MSN
     * @param statusEnabled - Does the message enable or disable the now playing information? When announcing a new song, this is "1", and at the end of a song (to clear the now playing from the messenger), this is "0".
     * @param messageType - Which content type it is e.g. Music
     * @param songTitle - The title of the song
     * @param artist - The name of the song artist
     * @param album - The name of the album which the song is on
     * @param wmContentId - This is the string "WMContentID"
     * @see <a href="https://github.com/segin/psymp3/wiki/MsnMsgrUiManager">MsnMsgrUiManager</a>
     */
    private static void set(boolean statusEnabled, MsnMessageTypes messageType, String songTitle, String artist, String album, String wmContentId) {
        String format = (messageType== MsnMessageTypes.Music) ? "{0} - {1}" : "{0}";

        if(wmContentId == null) wmContentId = "WMContentID";

        StringBuilder buffer = new StringBuilder();
        buffer.append("SpotifyXP").append("\\0");
        buffer.append(messageType);
        buffer.append("\\0").append((statusEnabled ? "1" : "0"));
        buffer.append("\\0").append(format);
        if(songTitle != null) buffer.append("\\0").append(songTitle);
        if(artist != null) buffer.append("\\0").append(artist);
        if(album != null) buffer.append("\\0").append(album);
        buffer.append("\\0").append(wmContentId);
        buffer.append("\\0");

        ConsoleLogging.debug("Sending to MSN: \"" + buffer + "\"");

        Memory messageMemory = new Memory((long) Native.WCHAR_SIZE * (buffer.length() + 1));
        messageMemory.setWideString(0, buffer.toString());

        WinUser.COPYDATASTRUCT data = new WinUser.COPYDATASTRUCT();
        data.dwData = new BaseTSD.ULONG_PTR(1351);
        data.lpData = messageMemory;
        data.cbData = (buffer.length() * 2) + 2;
        data.write();


        HWND handlePtr = User32.INSTANCE.FindWindowEx(null, null, "MsnMsgrUIManager", null);
        if (handlePtr != null) {
            User32.INSTANCE.SendMessage(handlePtr, 0x004A, new WPARAM(0), new LPARAM(Pointer.nativeValue(data.getPointer())));
        }
    }

    public static void setGame(String name) {
        set(true, MsnMessageTypes.Games, name, "", "", null);
    }

    public static void setMusic(String artist, String title, String album) {
        set(true, MsnMessageTypes.Music, artist, title, album, null);
    }

    public static void setOffice(String msg) {
        set(true, MsnMessageTypes.Office, msg, "", "", null);
    }

    public static void clear() {
        set(false, MsnMessageTypes.Music, "", "", "", null);
    }
}
