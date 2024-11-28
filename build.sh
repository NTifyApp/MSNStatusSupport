if [ ! -d "../SpotifyXP" ]; then
    if [ ! -d "SpotifyXP" ]; then
      # Clone and build SpotifyXP
      git clone https://github.com/SpotifyXP/SpotifyXP
      cd SpotifyXP
      python3 build.py
    fi
else
  # Build cloned SpotifyXP
  cd ../SpotifyXP
  python3 init.py
  mvn package
fi
if [ ! -d "../SpotifyXP" ]; then
  cd ..
else
  cd ..
  cd MSNStatusSupport
fi
gradle buildJar