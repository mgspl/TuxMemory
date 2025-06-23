{pkgs}:
with pkgs; let
  # android-studio is not available in aarch64-darwin
  conditionalPackages =
    if pkgs.system != "aarch64-darwin"
    then [android-studio]
    else [];
in
  with pkgs;
  # Configure your development environment.
  #
  # Documentation: https://github.com/numtide/devshell
    devshell.mkShell {
      name = "TuxMemory";
      motd = ''
        Entered the TuxMemory development environment.

        nohup idea-ultimate >/dev/null 2>&1 &
      '';
      env = [
        {
          name = "ANDROID_HOME";
          value = "${android-sdk}/share/android-sdk";
        }
        {
          name = "ANDROID_SDK_ROOT";
          value = "${android-sdk}/share/android-sdk";
        }
        {
          name = "JAVA_HOME";
          value = jdk.home;
        }
        {
          name = "GRADLE_OPTS";
          value = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${android-sdk}/share/android-sdk/build-tools/35.0.1/aapt2";
        }
      ];
      packages = [
        android-sdk
        gradle
        jdk
      ];
    }
