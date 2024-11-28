package com.spotifyxp.msnstatus;

import com.spotifyxp.PublicValues;
import com.spotifyxp.deps.xyz.gianlu.librespot.audio.MetadataWrapper;
import com.spotifyxp.deps.xyz.gianlu.librespot.metadata.PlayableId;
import com.spotifyxp.deps.xyz.gianlu.librespot.player.Player;
import com.spotifyxp.logging.ConsoleLogging;
import com.spotifyxp.msnstatus.msn.MSNStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

public class PlayerListener implements Player.EventsListener {
    private MetadataWrapper trackMetadata;

    @Override
    public void onContextChanged(@NotNull Player player, @NotNull String newUri) {
    }

    @Override
    public void onTrackChanged(@NotNull Player player, @NotNull PlayableId id, @Nullable MetadataWrapper metadata, boolean userInitiated) {
    }

    @Override
    public void onPlaybackEnded(@NotNull Player player) {
        MSNStatus.clear();
    }

    @Override
    public void onPlaybackPaused(@NotNull Player player, long trackTime) {
        MSNStatus.clear();
    }

    @Override
    public void onPlaybackResumed(@NotNull Player player, long trackTime) {
        if(trackMetadata != null) {
            MSNStatus.setMusic(trackMetadata.getArtist(), trackMetadata.getName(), trackMetadata.getAlbumName());
        }
    }

    @Override
    public void onPlaybackFailed(@NotNull Player player, @NotNull Exception e) {
        MSNStatus.clear();
    }

    @Override
    public void onTrackSeeked(@NotNull Player player, long trackTime) {
    }

    @Override
    public void onMetadataAvailable(@NotNull Player player, @NotNull MetadataWrapper metadata) {
        if(metadata.isEpisode())  {
            trackMetadata = null;
            return;
        }
        trackMetadata = metadata;
        MSNStatus.setMusic(trackMetadata.getArtist(), trackMetadata.getName(), trackMetadata.getAlbumName());
    }

    @Override
    public void onPlaybackHaltStateChanged(@NotNull Player player, boolean halted, long trackTime) {
        if(halted) {
            MSNStatus.clear();
        } else {
            if(trackMetadata != null) {
                MSNStatus.setMusic(trackMetadata.getArtist(), trackMetadata.getName(), trackMetadata.getAlbumName());
            }
        }
    }

    @Override
    public void onInactiveSession(@NotNull Player player, boolean timeout) {
    }

    @Override
    public void onVolumeChanged(@NotNull Player player, @Range(from = 0, to = 1) float volume) {
    }

    @Override
    public void onPanicState(@NotNull Player player) {
        MSNStatus.clear();
    }

    @Override
    public void onStartedLoading(@NotNull Player player) {
    }

    @Override
    public void onFinishedLoading(@NotNull Player player) {
    }
}
